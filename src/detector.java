package cn.drknt.exmpls;

import java.io.File;

import cn.drknt.cfg.list;
import cn.drknt.cfg.parser;
import cn.drknt.dta.data;
import cn.drknt.dta.data_type;
import cn.drknt.dta.load_args;
import cn.drknt.lyr.layer;
import cn.drknt.ntwrk.network;
import cn.drknt.utls.BaseFunction;
import cn.drknt.utls.option_list;
import cn.drknt.utls.pthread_t;
import cn.drknt.utls.utils;

public class detector extends BaseFunction{


static int coco_ids[] = {1,2,3,4,5,6,7,8,9,10,11,13,14,15,16,17,18,19,20,21,22,23,24,25,27,28,31,32,33,34,35,36,37,38,39,40,41,42,43,44,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,67,70,72,73,74,75,76,77,78,79,80,81,82,84,85,86,87,88,89,90};

void train_detector(String datacfg, String cfgfile, String weightfile, int[] gpus, int ngpus, boolean clear) throws Exception
{
    list options = option_list.read_data_cfg(datacfg);
    String train_images = option_list.option_find_str(options, "train", "data/train.list");
    String backup_directory = option_list.option_find_str(options, "backup", "/backup/");
    File backupDir=new File(backup_directory);
    if(!backupDir.exists()) {
    	backupDir.mkdirs();
    	System.out.println("make dir: "+ backupDir.getAbsolutePath());
	}
    
    srand(time(0));
    String base = utils.basecfg(cfgfile);
    printf("%s\n", base);
    float avg_loss = -1;
    network[] nets = new network[ngpus];//calloc(ngpus, sizeof(network));

    srand(time(0));
    int seed = rand();
    int i;
    for(i = 0; i < ngpus; ++i){
        srand(seed);
//#ifdef GPU
//        cuda_set_device(gpus[i]);
//#endif
        nets[i] = parser.parse_network_cfg(cfgfile);
        if(weightfile!=null){
            parser.load_weights(nets[i], weightfile);
        }
        if(clear) nets[i].seen[0] = 0;
        nets[i].learning_rate *= ngpus;
    }
    srand(time(0));
    network net = nets[0];

    int imgs = net.batch * net.subdivisions * ngpus;
    printf("Learning Rate: %g, Momentum: %g, Decay: %g\n", net.learning_rate, net.momentum, net.decay);
    data train=new data(), buffer=new data();//一个训练，一个异步加载做好准备

    layer l = net.layers[net.n - 1];

    int classes = l.classes;
    float jitter = l.jitter;

//    list[] plist = get_paths(train_images);
//    //int N = plist.size;
//    String[] paths = (String[] )list_to_array(plist);
    String[] paths = data.get_paths2(train_images);
	int init_w = net.w;
	int init_h = net.h;

    load_args args = new load_args();
    args.w = net.w;
    args.h = net.h;
    args.paths = paths;
    args.n = imgs;
    args.m = paths.length;
    args.classes = classes;
    args.jitter = jitter;
    args.num_boxes = l.max_boxes;
	args.small_object = l.small_object;
    args.d = buffer;//&buffer;让线程往args.d中加载数据时，即往buffer中加载图片数据
    args.type = data_type.DETECTION_DATA;
	args.threads = 1;// 8;

    args.angle = net.angle;
    args.exposure = net.exposure;
    args.saturation = net.saturation;
    args.hue = net.hue;

    pthread_t load_thread = data.load_data(args);
    load_thread.start();//启动该线程加载数据
    /*clock_t*/long time;
    int count = 0;//原来是0，改为1避免进入while后第一次加载白费；
    //while(i*imgs < N*120){
    while(network.get_current_batch(net) < net.max_batches){
		if(l.random && count++%10 == 0){
            printf("Resizing\n");
			int dim = (rand() % 12 + (init_w/32 - 5)) * 32;	// +-160
            //int dim = (rand() % 10 + 10) * 32;
            //if (get_current_batch(net)+100 > net.max_batches) dim = 544;
            //int dim = (rand() % 4 + 16) * 32;
            printf("%d\n", dim);
            args.w = dim;
            args.h = dim;

//            pthread_join(load_thread, 0);
            load_thread.join();//等待加载数据的线程加载完毕（才往后执行）
//            train = buffer;
//            free_data(train);//扔掉这批数据
            buffer=new data();//这是java的扔掉这批数据
            args.d = buffer;//让线程往args.d中加载数据时，即往buffer中加载图片数据
            load_thread = data.load_data(args);//并重新加载，因为 args.w, args.h 被调整了；
            load_thread.start();//启动该线程加载数据
            for(i = 0; i < ngpus; ++i){//启动完，主线程继续往下执行
                network.resize_network(nets[i], dim, dim);//主线程继续执行，调整dim
            }
            net = nets[0];//这行似乎多余
        }
        time=clock();
//        pthread_join(load_thread, 0);
        load_thread.join();//主线程继续执行，到这里等待数据加载完毕//第二个周期，这里等待的是上一轮下面这个线程；
        train = buffer;//数据加载完毕后，将执行信息拷贝给train；（数据是不会拷贝的，太耗时了；）
        buffer=new data();//相当于给buffer分配一块新内存；
        args.d = buffer;//让线程往args.d也指向这块新内容；
        load_thread = data.load_data(args);//继续马不停蹄异步加载数据，尽量不让主线程等待数据
        load_thread.start();//启动该线程加载数据
        /*
           int k;
           for(k = 0; k < l.max_boxes; ++k){
           box b = float_to_box(train.y.vals[10] + 1 + k*5);
           if(!b.x) break;
           printf("loaded: %f %f %f %f\n", b.x, b.y, b.w, b.h);
           }
           image im = float_to_image(448, 448, 3, train.X.vals[10]);
           int k;
           for(k = 0; k < l.max_boxes; ++k){
           box b = float_to_box(train.y.vals[10] + 1 + k*5);
           printf("%d %d %d %d\n", truth.x, truth.y, truth.w, truth.h);
           draw_bbox(im, b, 8, 1,0,0);
           }
           save_image(im, "truth11");
         */

        printf("Loaded: %f seconds\n", sec(clock()-time));//主线程执行子线程join方法耗时()实际上还包括了启动另外一个线程等操作的耗时

        time=clock();
        float loss = 0;
//#ifdef GPU
//        if(ngpus == 1){
//            loss = train_network(net, train);
//        } else {
//            loss = train_networks(nets, ngpus, train, 4);
//        }
//#else
//        System.err.println(detector.class.getName()+" : 开始训练...");
        loss = network.train_network(net, train);//主线程去做训练。得到loss值；
//#endif
        if (avg_loss < 0) avg_loss = loss;
        avg_loss = avg_loss*.9f + loss*.1f;

        i = network.get_current_batch(net);
        printf("%d: %f, %f avg, %f rate, %f seconds, %d images\n", network.get_current_batch(net), loss, avg_loss, network.get_current_rate(net), sec(clock()-time), i*imgs);
		//if (i % 1000 == 0 || (i < 1000 && i % 100 == 0)) {
		if (i % 11 == 0) {//如果训练了100批次了，把训练结果临时保存下来；
//#ifdef GPU
//			if (ngpus != 1) sync_nets(nets, ngpus, 0);
//#endif
//			char buff[256];
//			sprintf(buff, "%s/%s_%d.weights", backup_directory, base, i);
			String buff=String.format("%s/%s_%d.weights", backup_directory, base, i);
			
			parser.save_weights(net, buff);
		}
//        free_data(train);
    }
//#ifdef GPU
//    if(ngpus != 1) sync_nets(nets, ngpus, 0);
//#endif
//    char buff[256];
//    sprintf(buff, "%s/%s_final.weights", backup_directory, base);
    String buff=String.format("%s/%s_final.weights", backup_directory, base);//所有批次统统训练完毕，存成文件；
    parser.save_weights(net, buff);
}


//static int get_coco_image_id(String filename)
//{
//    String p = strrchr(filename, '_');
//    return atoi(p+1);
//}
//
//static void print_cocos(FILE *fp, String image_path, box *boxes, float[][] probs, int num_boxes, int classes, int w, int h)
//{
//    int i, j;
//    int image_id = get_coco_image_id(image_path);
//    for(i = 0; i < num_boxes; ++i){
//        float xmin = boxes[i].x - boxes[i].w/2.;
//        float xmax = boxes[i].x + boxes[i].w/2.;
//        float ymin = boxes[i].y - boxes[i].h/2.;
//        float ymax = boxes[i].y + boxes[i].h/2.;
//
//        if (xmin < 0) xmin = 0;
//        if (ymin < 0) ymin = 0;
//        if (xmax > w) xmax = w;
//        if (ymax > h) ymax = h;
//
//        float bx = xmin;
//        float by = ymin;
//        float bw = xmax - xmin;
//        float bh = ymax - ymin;
//
//        for(j = 0; j < classes; ++j){
//            if (probs[i][j]) fprintf(fp, "{\"image_id\":%d, \"category_id\":%d, \"bbox\":[%f, %f, %f, %f], \"score\":%f},\n", image_id, coco_ids[j], bx, by, bw, bh, probs[i][j]);
//        }
//    }
//}

//void print_detector_detections(FILE **fps, String id, box *boxes, float[][] probs, int total, int classes, int w, int h)
//{
//    int i, j;
//    for(i = 0; i < total; ++i){
//        float xmin = boxes[i].x - boxes[i].w/2.;
//        float xmax = boxes[i].x + boxes[i].w/2.;
//        float ymin = boxes[i].y - boxes[i].h/2.;
//        float ymax = boxes[i].y + boxes[i].h/2.;
//
//        if (xmin < 0) xmin = 0;
//        if (ymin < 0) ymin = 0;
//        if (xmax > w) xmax = w;
//        if (ymax > h) ymax = h;
//
//        for(j = 0; j < classes; ++j){
//            if (probs[i][j]) fprintf(fps[j], "%s %f %f %f %f %f\n", id, probs[i][j],
//                    xmin, ymin, xmax, ymax);
//        }
//    }
//}
//
//void print_imagenet_detections(FILE *fp, int id, box *boxes, float[][] probs, int total, int classes, int w, int h)
//{
//    int i, j;
//    for(i = 0; i < total; ++i){
//        float xmin = boxes[i].x - boxes[i].w/2.;
//        float xmax = boxes[i].x + boxes[i].w/2.;
//        float ymin = boxes[i].y - boxes[i].h/2.;
//        float ymax = boxes[i].y + boxes[i].h/2.;
//
//        if (xmin < 0) xmin = 0;
//        if (ymin < 0) ymin = 0;
//        if (xmax > w) xmax = w;
//        if (ymax > h) ymax = h;
//
//        for(j = 0; j < classes; ++j){
//            int _class = j;
//            if (probs[i][_class]) fprintf(fp, "%d %d %f %f %f %f %f\n", id, j+1, probs[i][_class],
//                    xmin, ymin, xmax, ymax);
//        }
//    }
//}
//
void validate_detector(String datacfg, String cfgfile, String weightfile) {
	System.err.println("validate_detector not impl..quit.");
	System.exit(-8);
}
//void validate_detector(String datacfg, String cfgfile, String weightfile)
//{
//    int j;
//    list options = option_list.read_data_cfg(datacfg);
//    String valid_images = option_list.option_find_str(options, "valid", "data/train.list");
//    String name_list = option_list.option_find_str(options, "names", "data/names.list");
//    String prefix = option_list.option_find_str(options, "results", "results");
//    String[] names = option_list.get_labels(name_list);
//    String mapf = option_list.option_find_str(options, "map", null);
//    int[] map = null;
//    if (mapf) map = read_map(mapf);
//
//    network net = parse_network_cfg_custom(cfgfile, 1);
//    if(weightfile){
//        load_weights(&net, weightfile);
//    }
//    set_batch_network(&net, 1);
//    fprintf(stderr, "Learning Rate: %g, Momentum: %g, Decay: %g\n", net.learning_rate, net.momentum, net.decay);
//    srand(time(0));
//
//    String base = "comp4_det_test_";
//    list[] plist = get_paths(valid_images);
//    String[] paths = (String[] )list_to_array(plist);
//
//    layer l = net.layers[net.n-1];
//    int classes = l.classes;
//
//    char buff[1024];
//    String type = option_find_str(options, "eval", "voc");
//    FILE *fp = 0;
//    FILE **fps = 0;
//    int coco = 0;
//    int imagenet = 0;
//    if(0==strcmp(type, "coco")){
//        snprintf(buff, 1024, "%s/coco_results.json", prefix);
//        fp = fopen(buff, "w");
//        fprintf(fp, "[\n");
//        coco = 1;
//    } else if(0==strcmp(type, "imagenet")){
//        snprintf(buff, 1024, "%s/imagenet-detection.txt", prefix);
//        fp = fopen(buff, "w");
//        imagenet = 1;
//        classes = 200;
//    } else {
//        fps = calloc(classes, sizeof(FILE *));
//        for(j = 0; j < classes; ++j){
//            snprintf(buff, 1024, "%s/%s%s.txt", prefix, base, names[j]);
//            fps[j] = fopen(buff, "w");
//        }
//    }
//
//
//    box *boxes = calloc(l.w*l.h*l.n, sizeof(box));
//    float[][] probs = calloc(l.w*l.h*l.n, sizeof(float[] ));
//    for(j = 0; j < l.w*l.h*l.n; ++j) probs[j] = calloc(classes, sizeof(float[] ));
//
//    int m = plist.size;
//    int i=0;
//    int t;
//
//    float thresh = .005;
//    float nms = .45;
//
//    int nthreads = 4;
//    image[] val = calloc(nthreads, 1/*sizeof(image)*/);
//    image[] val_resized = calloc(nthreads, 1/*sizeof(image)*/);
//    image[] buf = calloc(nthreads, 1/*sizeof(image)*/);
//    image[] buf_resized = calloc(nthreads, 1/*sizeof(image)*/);
//    pthread_t *thr = calloc(nthreads, sizeof(pthread_t));
//
//    load_args args = {0};
//    args.w = net.w;
//    args.h = net.h;
//    args.type = IMAGE_DATA;
//
//    for(t = 0; t < nthreads; ++t){
//        args.path = paths[i+t];
//        args.im = &buf[t];
//        args.resized = &buf_resized[t];
//        thr[t] = load_data_in_thread(args);
//    }
//    /*time_t*/long start = time(0);
//    for(i = nthreads; i < m+nthreads; i += nthreads){
//        fprintf(stderr, "%d\n", i);
//        for(t = 0; t < nthreads && i+t-nthreads < m; ++t){
//            pthread_join(thr[t], 0);
//            val[t] = buf[t];
//            val_resized[t] = buf_resized[t];
//        }
//        for(t = 0; t < nthreads && i+t < m; ++t){
//            args.path = paths[i+t];
//            args.im = &buf[t];
//            args.resized = &buf_resized[t];
//            thr[t] = load_data_in_thread(args);
//        }
//        for(t = 0; t < nthreads && i+t-nthreads < m; ++t){
//            String path = paths[i+t-nthreads];
//            String id = basecfg(path);
//            float[] X = val_resized[t].data;
//            network_predict(net, X);
//            int w = val[t].w;
//            int h = val[t].h;
//            get_region_boxes(l, w, h, thresh, probs, boxes, 0, map);
//            if (nms) do_nms_sort(boxes, probs, l.w*l.h*l.n, classes, nms);
//            if (coco){
//                print_cocos(fp, path, boxes, probs, l.w*l.h*l.n, classes, w, h);
//            } else if (imagenet){
//                print_imagenet_detections(fp, i+t-nthreads+1, boxes, probs, l.w*l.h*l.n, classes, w, h);
//            } else {
//                print_detector_detections(fps, id, boxes, probs, l.w*l.h*l.n, classes, w, h);
//            }
//            free(id);
//            free_image(val[t]);
//            free_image(val_resized[t]);
//        }
//    }
//    for(j = 0; j < classes; ++j){
//        if(fps) fclose(fps[j]);
//    }
//    if(coco){
//        fseek(fp, -2, SEEK_CUR); 
//        fprintf(fp, "\n]\n");
//        fclose(fp);
//    }
//    fprintf(stderr, "Total Detection Time: %f Seconds\n", (double)(time(0) - start));
//}
//
void validate_detector_recall(String datacfg, String cfgfile, String weightfile) {
	System.err.println("validate_detector_recall not impl..quit.");
	System.exit(-8);
}
//void validate_detector_recall(String datacfg, String cfgfile, String weightfile)
//{
//    network net = parse_network_cfg_custom(cfgfile, 1);
//    if(weightfile){
//        load_weights(&net, weightfile);
//    }
//    set_batch_network(&net, 1);
//    fprintf(stderr, "Learning Rate: %g, Momentum: %g, Decay: %g\n", net.learning_rate, net.momentum, net.decay);
//    srand(time(0));
//
//	list[] options = read_data_cfg(datacfg);
//	String valid_images = option_find_str(options, "valid", "data/train.txt");
//    list[] plist = get_paths(valid_images);
//    String[] paths = (String[] )list_to_array(plist);
//
//    layer l = net.layers[net.n-1];
//    int classes = l.classes;
//
//    int j, k;
//    box[] boxes = calloc(l.w*l.h*l.n, sizeof(box));
//    float[][] probs = calloc(l.w*l.h*l.n, sizeof(float[] ));
//    for(j = 0; j < l.w*l.h*l.n; ++j) probs[j] = calloc(classes, sizeof(float[] ));
//
//    int m = plist.size;
//    int i=0;
//
//	float thresh = .001;// .001;	// .2;
//    float iou_thresh = .5;
//    float nms = .4;
//
//    int total = 0;
//    int correct = 0;
//    int proposals = 0;
//    float avg_iou = 0;
//
//    for(i = 0; i < m; ++i){
//        String path = paths[i];
//        image orig = load_image_color(path, 0, 0);
//        image sized = resize_image(orig, net.w, net.h);
//        String id = basecfg(path);
//        network_predict(net, sized.data);
//        get_region_boxes(l, 1, 1, thresh, probs, boxes, 1, 0);
//        if (nms) do_nms(boxes, probs, l.w*l.h*l.n, 1, nms);
//
//        char labelpath[4096];
//        find_replace(path, "images", "labels", labelpath);
//        find_replace(labelpath, "JPEGImages", "labels", labelpath);
//        find_replace(labelpath, ".jpg", ".txt", labelpath);
//        find_replace(labelpath, ".JPEG", ".txt", labelpath);
//	find_replace(labelpath, ".png", ".txt", labelpath);
//
//        int num_labels = 0;
//        box_label truth = read_boxes(labelpath, &num_labels);
//        for(k = 0; k < l.w*l.h*l.n; ++k){
//            if(probs[k][0] > thresh){
//                ++proposals;
//            }
//        }
//		for (j = 0; j < num_labels; ++j) {
//			++total;
//			box t = { truth[j].x, truth[j].y, truth[j].w, truth[j].h };
//			float best_iou = 0;
//			for (k = 0; k < l.w*l.h*l.n; ++k) {
//				float iou = box_iou(boxes[k], t);
//				if (probs[k][0] > thresh && iou > best_iou) {
//					best_iou = iou;
//				}
//			}
//            avg_iou += best_iou;
//            if(best_iou > iou_thresh){
//                ++correct;
//            }
//        }
//
//        fprintf(stderr, "%5d %5d %5d\tRPs/Img: %.2f\tIOU: %.2f%%\tRecall:%.2f%%\n", i, correct, total, (float)proposals/(i+1), avg_iou*100/total, 100.*correct/total);
//        free(id);
//        free_image(orig);
//        free_image(sized);
//    }
//}
//
void test_detector(String datacfg, String cfgfile, String weightfile, String filename, float thresh) {
	System.err.println("test_detector not impl..quit.");
	System.exit(-8);
}
//void test_detector(String datacfg, String cfgfile, String weightfile, String filename, float thresh)
//{
//    list[] options = read_data_cfg(datacfg);
//    String name_list = option_find_str(options, "names", "data/names.list");
//    String[] names = get_labels(name_list);
//
//    image[][] alphabet = load_alphabet();
//    network net = parse_network_cfg_custom(cfgfile, 1);
//    if(weightfile){
//        load_weights(&net, weightfile);
//    }
//    set_batch_network(&net, 1);
//    srand(2222222);
//    /*clock_t*/long time;
//    char buff[256];
//    String input = buff;
//    int j;
//    float nms=.4;
//    while(1){
//        if(filename){
//            strncpy(input, filename, 256);
//			if (input[strlen(input) - 1] == 0x0d) input[strlen(input) - 1] = 0;
//        } else {
//            printf("Enter Image Path: ");
//            fflush(stdout);
//            input = fgets(input, 256, stdin);
//            if(!input) return;
//            strtok(input, "\n");
//        }
//        image im = load_image_color(input,0,0);
//        image sized = resize_image(im, net.w, net.h);
//        layer l = net.layers[net.n-1];
//
//        box *boxes = calloc(l.w*l.h*l.n, sizeof(box));
//        float[][] probs = calloc(l.w*l.h*l.n, sizeof(float[] ));
//        for(j = 0; j < l.w*l.h*l.n; ++j) probs[j] = calloc(l.classes, sizeof(float[] ));
//
//        float[] X = sized.data;
//        time=clock();
//        network_predict(net, X);
//        printf("%s: Predicted in %f seconds.\n", input, sec(clock()-time));
//        get_region_boxes(l, 1, 1, thresh, probs, boxes, 0, 0);
//        if (nms) do_nms_sort(boxes, probs, l.w*l.h*l.n, l.classes, nms);
//        draw_detections(im, l.w*l.h*l.n, thresh, boxes, probs, names, alphabet, l.classes);
//        save_image(im, "predictions");
//        show_image(im, "predictions");
//
//        free_image(im);
//        free_image(sized);
//        free(boxes);
//        free_ptrs((void **)probs, l.w*l.h*l.n);
////#ifdef OPENCV
////        cvWaitKey(0);
////        cvDestroyAllWindows();
////#endif
//        if (filename) break;
//    }
//}

public void run_detector(int argc, String[] argv)throws Exception
{
	String out_filename = utils.find_char_arg(argc, argv, "-out_filename", 0);
    String prefix = utils.find_char_arg(argc, argv, "-prefix", 0);
    float thresh = utils.find_float_arg(argc, argv, "-thresh", .24);
    int cam_index = utils.find_int_arg(argc, argv, "-c", 0);
    int frame_skip = utils.find_int_arg(argc, argv, "-s", 0);
    if(argc < 4){
        fprintf(stderr, "usage: %s %s [train/test/valid] [cfg] [weights (optional)]\n", argv[0], argv[1]);
        return;
    }
    String gpu_list = utils.find_char_arg(argc, argv, "-gpus", 0);
    int[] gpus = null;
    int gpu = 0;
    int ngpus = 0;
    if(gpu_list!=null){
        printf("%s\n", gpu_list);
        int len = strlen(gpu_list);
        ngpus = 1;
        int i;
        for(i = 0; i < len; ++i){
            if (gpu_list.charAt(i) == ',') ++ngpus;
        }
        gpus = callocInt(ngpus, 1/*sizeof(int)*/);
        for(i = 0; i < ngpus; ++i){
            gpus[i] = atoi(gpu_list);
            gpu_list = gpu_list.substring(strchr(gpu_list, ',')+1);
        }
    } else {
        gpu = gpu_index;
        gpus = new int[1];gpus[0]=gpu;
        ngpus = 1;
    }

    boolean clear = utils.find_arg(argc, argv, "-clear");

    String datacfg = argv[3];
    String cfg = argv[4];
    String weights = (argc > 5) ? argv[5] : null;
//	if(weights!=null)
//		if (weights[strlen(weights) - 1] == 0x0d) weights[strlen(weights) - 1] = 0;
    String filename = (argc > 6) ? argv[6]: null;
    if(0==strcmp(argv[2], "test")) test_detector(datacfg, cfg, weights, filename, thresh);
    else if(0==strcmp(argv[2], "train")) train_detector(datacfg, cfg, weights, gpus, ngpus, clear);
    else if(0==strcmp(argv[2], "valid")) validate_detector(datacfg, cfg, weights);
    else if(0==strcmp(argv[2], "recall")) validate_detector_recall(datacfg, cfg, weights);
    else if(0==strcmp(argv[2], "demo")) {
        list options = option_list.read_data_cfg(datacfg);
        int classes = option_list.option_find_int(options, "classes", 20);
        String name_list = option_list.option_find_str(options, "names", "data/names.list");
        String[] names = data.get_labels(name_list);
//		if(filename)
//			if (filename[strlen(filename) - 1] == 0x0d) filename[strlen(filename) - 1] = 0;
//        demo(cfg, weights, thresh, cam_index, filename, names, classes, frame_skip, prefix, out_filename);
    }
}
}