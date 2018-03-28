package cn.drknt.dta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cn.drknt.bx.box;
import cn.drknt.cfg.tree;
import cn.drknt.img.image;
import cn.drknt.mtrx.matrix;
import cn.drknt.utls.BaseFunction;
import cn.drknt.utls.pthread_t;
import cn.drknt.utls.utils;

public class data extends BaseFunction{
	public data() {
		
	}
	public data(int indexSJX) {
		this.indexSJX=indexSJX;
	}
    public int w, h;
    public matrix X=new matrix();
    public matrix y=new matrix();
    public int shallow;
    public int[] num_boxes;
    public box[][] boxes;
    public int indexSJX;//序号，调试用
//pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

//from data.h
static final float distance_from_edge(int x, int max)
{
    int dx = (max/2) - x;
    if (dx < 0) dx = -dx;
    dx = (max/2) + 1 - dx;
    dx *= 2;
    float dist = (float)dx/max;
    if (dist > 1) dist = 1;
    return dist;
}

public static String[] get_paths2(String filename)throws IOException,Exception {
	File file=new File(filename);
    if(!file.exists()) utils.file_error(filename);
    BufferedReader fp=new BufferedReader(new FileReader(file));
    String line;
    int nu = 0;
    List<String>list=new ArrayList<String>();
    while((line=fp.readLine()) != null){
    	list.add(line);
    }
    fp.close();
    String[] ret=new String[list.size()];
    ret=list.toArray(ret);
    return ret;
}
//list[] get_paths(String filename)
//{
//    String path;
//    FILE *file = fopen(filename, "r");
//    if(!file) file_error(filename);
//    list[] lines = make_list();
//    while((path=fgetl(file))){
//        list_insert(lines, path);
//    }
//    fclose(file);
//    return lines;
//}

/*
String[] get_random_paths_indexes(String[] paths, int n, int m, int[] indexes)
{
    String[] random_paths = calloc(n, sizeof(String));
    int i;
    pthread_mutex_lock(&mutex);
    for(i = 0; i < n; ++i){
        int index = random_gen()%m;
        indexes[i] = index;
        random_paths[i] = paths[index];
        if(i == 0) printf("%s\n", paths[index]);
    }
    pthread_mutex_unlock(&mutex);
    return random_paths;
}
*/
synchronized String[] get_random_paths(String[] paths, int n, int m)
{
	printf("load %d from %d images random in thread: %s, %s\n",n,m,Thread.currentThread().getId(), Thread.currentThread().getName());
    String[] random_paths = new String[n];//calloc(n, sizeof(char*));
    int i;
//    pthread_mutex_lock(mutex);
    for(i = 0; i < n; ++i){
        int index = rand()%m;
        random_paths[i] = paths[index];
//        printf("load image random: %s\n ",random_paths[i]);
        //if(i == 0) printf("%s\n", paths[index]);
    }
//    pthread_mutex_unlock(mutex);
    return random_paths;
}
//String[] get_random_paths(String[] paths, int n, int m)
//{
//    String[] random_paths = new String[n];//calloc(n, sizeof(String));
//    int i;
//    pthread_mutex_lock(&mutex);
//	//printf("n = %d \n", n);
//    for(i = 0; i < n; ++i){		
//        int index = utils.random_gen() % m;
//        random_paths[i] = paths[index];
//        //if(i == 0) printf("%s\n", paths[index]);
//		//printf("grp: %s\n", paths[index]);
//    }
//    pthread_mutex_unlock(&mutex);
//    return random_paths;
//}

//String[] find_replace_paths(String[] paths, int n, String find, String replace)
//{
//    String[] replace_paths = new String[n];//calloc(n, sizeof(String));
//    int i;
//    for(i = 0; i < n; ++i){
//        char replaced[4096];
//        find_replace(paths[i], find, replace, replaced);
//        replace_paths[i] = copy_string(replaced);
//    }
//    return replace_paths;
//}
String[] find_replace_paths(String[] paths, int n, String find, String replace)
{
    String[] replace_paths = new String[n];//calloc(n, sizeof(char*));
    int i;
    for(i = 0; i < n; ++i){
//        char replaced[4096];
//        String replaced=new String();
//        find_replace(paths[i], find, replace, replaced);
//    	String replaced=paths[i].replace(find, replace);
//        replace_paths[i] = copy_string(replaced);
        replace_paths[i] = paths[i].replace(find, replace);
    }
    return replace_paths;
}
matrix load_image_paths_gray(String[] paths, int n, int w, int h)
{
    int i;
    matrix X=new matrix();
    X.rows = n;
    X.vals = callocFloat2DArray(X.rows, 1/*sizeof(float*)*/);
    X.cols = 0;

    for(i = 0; i < n; ++i){
        image im = image.load_image(paths[i], w, h, 3);

        image gray = image.grayscale_image(im);
//        free_image(im);
        im = gray;

        X.vals[i] = im.data;
        X.cols = im.h*im.w*im.c;
    }
    return X;
}

matrix load_image_paths(String[] paths, int n, int w, int h)
{
    int i;
    matrix X=new matrix();
    X.rows = n;
    X.vals = callocFloat2DArray(X.rows, 1/*sizeof(float*)*/);
    X.cols = 0;

    for(i = 0; i < n; ++i){
        image im = image.load_image_color(paths[i], w, h);
        X.vals[i] = im.data;
        X.cols = im.h*im.w*im.c;
    }
    return X;
}

matrix load_image_augment_paths(String[] paths, int n, int min, int max, int size, float angle, float aspect, float hue, float saturation, float exposure)
{
    int i;
    matrix X=new matrix();
    X.rows = n;
    X.vals = callocFloat2DArray(X.rows, 1/*sizeof(float*)*/);
    X.cols = 0;

    for(i = 0; i < n; ++i){
        image im = image.load_image_color(paths[i], 0, 0);
        image crop = image.random_augment_image(im, angle, aspect, min, max, size);
        int flip = utils.random_gen()%2;
        if (flip!=0) image.flip_image(crop);
        image.random_distort_image(crop, hue, saturation, exposure);

        /*
        show_image(im, "orig");
        show_image(crop, "crop");
        cvWaitKey(0);
        */
//        free_image(im);
        X.vals[i] = crop.data;
        X.cols = crop.h*crop.w*crop.c;
    }
    return X;
}

public static box_label[] read_boxes(String filename, int[] n)//throws Exception
{
//    FILE *file = fopen(filename, "r");
    File file=new File(filename);
    if(!file.exists()) {
    	try {
			utils.file_error(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    float x, y, h, w;
    int id;
    int count = 0;
    int size = 64;
    box_label[] boxes = new box_label[size];//calloc(size, sizeof(box_label));
    for (int i = 0; i < boxes.length; i++) {/////////sjx
    	boxes[i]=new box_label();
	}
    
    Scanner fp = null;
    try {
    	fp=new Scanner(new FileReader(file));
        while(fp.hasNext()) {
        	id=fp.nextInt();
        	x=fp.nextFloat();
        	y=fp.nextFloat();
        	w=fp.nextFloat();
        	h=fp.nextFloat();
//        }
//        while(fscanf(file, "%d %f %f %f %f", &id, &x, &y, &w, &h) == 5){
            if(count == size) {
                size = size * 2;
                boxes = new box_label[size];//(boxes, size*sizeof(box_label));
                for (int i = 0; i < boxes.length; i++) {/////////sjx
                	boxes[i]=new box_label();
				}
            }
            boxes[count].id = id;
            boxes[count].x = x;
            boxes[count].y = y;
            boxes[count].h = h;
            boxes[count].w = w;
            boxes[count].left   = x - w/2;
            boxes[count].right  = x + w/2;
            boxes[count].top    = y - h/2;
            boxes[count].bottom = y + h/2;
            ++count;
        }	
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}finally {
		try {
			if(fp!=null) {
				fp.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
    
//    fclose(file);
    
    n[0] = count;
    return boxes;
}
//box_label[] read_boxes(String filename, int[] n)
//{
//    box_label boxes = calloc(1, sizeof(box_label));
//    FILE *file = fopen(filename, "r");
//    if(!file) file_error(filename);
//    float x, y, h, w;
//    int id;
//    int count = 0;
//    while(fscanf(file, "%d %f %f %f %f", &id, &x, &y, &w, &h) == 5){
//        boxes = realloc(boxes, (count+1)*sizeof(box_label));
//        boxes[count].id = id;
//        boxes[count].x = x;
//        boxes[count].y = y;
//        boxes[count].h = h;
//        boxes[count].w = w;
//        boxes[count].left   = x - w/2;
//        boxes[count].right  = x + w/2;
//        boxes[count].top    = y - h/2;
//        boxes[count].bottom = y + h/2;
//        ++count;
//    }
//    fclose(file);
//    n[0] = count;
//    return boxes;
//}

static void randomize_boxes(box_label[] b, int n)
{
    int i;
    for(i = 0; i < n; ++i){
        box_label swap = b[i];
        int index = utils.random_gen()%n;
        b[i] = b[index];
        b[index] = swap;
    }
}

static void correct_boxes(box_label[] boxes, int n, float dx, float dy, float sx, float sy, boolean flip)
{
    int i;
    for(i = 0; i < n; ++i){
        if(boxes[i].x == 0 && boxes[i].y == 0) {
            boxes[i].x = 999999;
            boxes[i].y = 999999;
            boxes[i].w = 999999;
            boxes[i].h = 999999;
            continue;
        }
        boxes[i].left   = boxes[i].left  * sx - dx;
        boxes[i].right  = boxes[i].right * sx - dx;
        boxes[i].top    = boxes[i].top   * sy - dy;
        boxes[i].bottom = boxes[i].bottom* sy - dy;

        if(flip){
            float swap = boxes[i].left;
            boxes[i].left = 1.f - boxes[i].right;
            boxes[i].right = 1.f - swap;
        }

        boxes[i].left =  utils.constrain(0, 1, boxes[i].left);
        boxes[i].right = utils.constrain(0, 1, boxes[i].right);
        boxes[i].top =   utils.constrain(0, 1, boxes[i].top);
        boxes[i].bottom =   utils.constrain(0, 1, boxes[i].bottom);

        boxes[i].x = (boxes[i].left+boxes[i].right)/2;
        boxes[i].y = (boxes[i].top+boxes[i].bottom)/2;
        boxes[i].w = (boxes[i].right - boxes[i].left);
        boxes[i].h = (boxes[i].bottom - boxes[i].top);

        boxes[i].w = utils.constrain(0, 1, boxes[i].w);
        boxes[i].h = utils.constrain(0, 1, boxes[i].h);
    }
}

void fill_truth_swag(String path, float[] truth, int classes, boolean flip, float dx, float dy, float sx, float sy)
{
//    char labelpath[4096];
	String labelpath="";
	labelpath=utils.find_replace(path, "images", "labels", labelpath);
	labelpath=utils.find_replace(labelpath, "JPEGImages", "labels", labelpath);
	labelpath=utils.find_replace(labelpath, ".jpg", ".txt", labelpath);
	labelpath=utils.find_replace(labelpath, ".JPG", ".txt", labelpath);
	labelpath=utils.find_replace(labelpath, ".JPEG", ".txt", labelpath);

    int[] count = new int[] {0};
    box_label[] boxes = read_boxes(labelpath, count);
    randomize_boxes(boxes, count[0]);
    correct_boxes(boxes, count[0], dx, dy, sx, sy, flip);
    float x,y,w,h;
    int id;
    int i;

    for (i = 0; i < count[0] && i < 30; ++i) {
        x =  boxes[i].x;
        y =  boxes[i].y;
        w =  boxes[i].w;
        h =  boxes[i].h;
        id = boxes[i].id;

        if (w < .0 || h < .0) continue;

        int index = (4+classes) * i;

        truth[index++] = x;
        truth[index++] = y;
        truth[index++] = w;
        truth[index++] = h;

        if (id < classes) truth[index+id] = 1;
    }
//    free(boxes);
}

//void fill_truth_region(String path, float[] truth, int classes, int num_boxes, int flip, float dx, float dy, float sx, float sy)
//{
//    char labelpath[4096];
//    find_replace(path, "images", "labels", labelpath);
//    find_replace(labelpath, "JPEGImages", "labels", labelpath);
//
//    find_replace(labelpath, ".jpg", ".txt", labelpath);
//    find_replace(labelpath, ".png", ".txt", labelpath);
//    find_replace(labelpath, ".JPG", ".txt", labelpath);
//    find_replace(labelpath, ".JPEG", ".txt", labelpath);
//    int count = 0;
//    box_label boxes = read_boxes(labelpath, &count);
//    randomize_boxes(boxes, count);
//    correct_boxes(boxes, count, dx, dy, sx, sy, flip);
//    float x,y,w,h;
//    int id;
//    int i;
//
//    for (i = 0; i < count; ++i) {
//        x =  boxes[i].x;
//        y =  boxes[i].y;
//        w =  boxes[i].w;
//        h =  boxes[i].h;
//        id = boxes[i].id;
//
//        if (w < .01 || h < .01) continue;
//
//        int col = (int)(x*num_boxes);
//        int row = (int)(y*num_boxes);
//
//        x = x*num_boxes - col;
//        y = y*num_boxes - row;
//
//        int index = (col+row*num_boxes)*(5+classes);
//        if (truth[index]) continue;
//        truth[index++] = 1;
//
//        if (id < classes) truth[index+id] = 1;
//        index += classes;
//
//        truth[index++] = x;
//        truth[index++] = y;
//        truth[index++] = w;
//        truth[index++] = h;
//    }
//    free(boxes);
//}
void fill_truth_region(String path, float[] truth, int classes, int num_boxes, boolean flip, float dx, float dy, float sx, float sy)
{
//    char labelpath[4096];
//    find_replace(path, "images", "labels", labelpath);
//    find_replace(labelpath, "JPEGImages", "labels", labelpath);
//
//    find_replace(labelpath, ".jpg", ".txt", labelpath);
//    find_replace(labelpath, ".png", ".txt", labelpath);
//    find_replace(labelpath, ".JPG", ".txt", labelpath);
//    find_replace(labelpath, ".JPEG", ".txt", labelpath);
	String  labelpath=path.replace("images", "labels").replace("JPEGImages", "labels").replace(".jpg", ".txt").replace(".png", ".txt").replace(".JPG", ".txt").replace(".JPEG", ".txt");
    int[] count = new int[1];
    box_label[] boxes = read_boxes(labelpath, count);
    randomize_boxes(boxes, count[0]);
    correct_boxes(boxes, count[0], dx, dy, sx, sy, flip);
    float x,y,w,h;
    int id;
    int i;

    for (i = 0; i < count[0]; ++i) {
        x =  boxes[i].x;
        y =  boxes[i].y;
        w =  boxes[i].w;
        h =  boxes[i].h;
        id = boxes[i].id;

        if (w < .005 || h < .005) continue;

        int col = (int)(x*num_boxes);
        int row = (int)(y*num_boxes);

        x = x*num_boxes - col;
        y = y*num_boxes - row;

        int index = (col+row*num_boxes)*(5+classes);
        if (truth[index]!=0) continue;
        truth[index++] = 1;

        if (id < classes) truth[index+id] = 1;
        index += classes;

        truth[index++] = x;
        truth[index++] = y;
        truth[index++] = w;
        truth[index++] = h;
    }
//    free(boxes);
}
void fill_truth_detection(String path, int num_boxes, float[] truth, int classes, boolean flip, float dx, float dy, float sx, float sy, int small_object)
{
//    char labelpath[4096];
	String labelpath=null;
	labelpath=utils.find_replace(path, "images", "labels", labelpath);
	labelpath=utils.find_replace(labelpath, "JPEGImages", "labels", labelpath);

	labelpath=utils.find_replace(labelpath, "raw", "labels", labelpath);
	labelpath=utils.find_replace(labelpath, ".jpg", ".txt", labelpath);
	labelpath=utils.find_replace(labelpath, ".png", ".txt", labelpath);
	labelpath=utils.find_replace(labelpath, ".JPG", ".txt", labelpath);
	labelpath=utils.find_replace(labelpath, ".JPEG", ".txt", labelpath);
    int[] count = new int[] {0};
    box_label[] boxes = read_boxes(labelpath, count);
	if (small_object == 1) {
		for (int i = 0; i < count[0]; ++i) {
			if (boxes[i].w < 0.01) boxes[i].w = 0.01f;
			if (boxes[i].h < 0.01) boxes[i].h = 0.01f;
		}
	}
    randomize_boxes(boxes, count[0]);
    correct_boxes(boxes, count[0], dx, dy, sx, sy, flip);
    if(count[0] > num_boxes) count[0] = num_boxes;
    float x,y,w,h;
    int id;
    int i;

    for (i = 0; i < count[0]; ++i) {
        x =  boxes[i].x;
        y =  boxes[i].y;
        w =  boxes[i].w;
        h =  boxes[i].h;
        id = boxes[i].id;

		// not detect small objects
		if ((w < 0.01 || h < 0.01)) continue;

        truth[i*5+0] = x;
        truth[i*5+1] = y;
        truth[i*5+2] = w;
        truth[i*5+3] = h;
        truth[i*5+4] = id;
    }
//    free(boxes);
}
//void fill_truth_detection(String path, int num_boxes, float[] truth, int classes, boolean flip, float dx, float dy, float sx, float sy)//throws IOException,Exception
//{
//	System.out.print("Data.fill_truth_detection...");
////    char labelpath[4096];
////    find_replace(path, "images", "labels", labelpath);
////    find_replace(labelpath, "JPEGImages", "labels", labelpath);
////
////    find_replace(labelpath, "raw", "labels", labelpath);
////    find_replace(labelpath, ".jpg", ".txt", labelpath);
////    find_replace(labelpath, ".png", ".txt", labelpath);
////    find_replace(labelpath, ".JPG", ".txt", labelpath);
////    find_replace(labelpath, ".JPEG", ".txt", labelpath);
//	String labelpath=path.replace("images", "labels");
//	labelpath=labelpath.replace("JPEGImages", "labels")
//			.replace("raw", "labels").replace(".jpg", ".txt")
//			.replace(".png", ".txt").replace(".JPG", ".txt")
//			.replace(".JPEG", ".txt");
//    int[] count = new int[1];
//    box_label[] boxes = read_boxes(labelpath, count);
//    randomize_boxes(boxes, count[0]);
//    correct_boxes(boxes, count[0], dx, dy, sx, sy, flip);
//    if(count[0] > num_boxes) count[0] = num_boxes;
//    float x,y,w,h;
//    int id;
//    int i;
//
//    for (i = 0; i < count[0]; ++i) {
//        x =  boxes[i].x;
//        y =  boxes[i].y;
//        w =  boxes[i].w;
//        h =  boxes[i].h;
//        id = boxes[i].id;
//
//        if ((w < .001 || h < .001)) continue;
//
//        truth[i*5+0] = x;
//        truth[i*5+1] = y;
//        truth[i*5+2] = w;
//        truth[i*5+3] = h;
//        truth[i*5+4] = id;
//    }
////    free(boxes);
//    System.out.println("Done!");
//}
//public static final int NUMCHARS=37;
//
//void print_letters(float[] pred, int n)
//{
//    int i;
//    for(i = 0; i < n; ++i){
//        int index = max_index(pred+i*NUMCHARS, NUMCHARS);
//        printf("%c", int_to_alphanum(index));
//    }
//    printf("\n");
//}
//
//void fill_truth_captcha(String path, int n, float[] truth)
//{
//    String begin = strrchr(path, '/');
//    ++begin;
//    int i;
//    for(i = 0; i < strlen(begin) && i < n && begin[i] != '.'; ++i){
//        int index = alphanum_to_int(begin[i]);
//        if(index > 35) printf("Bad %c\n", begin[i]);
//        truth[i*NUMCHARS+index] = 1;
//    }
//    for(;i < n; ++i){
//        truth[i*NUMCHARS + NUMCHARS-1] = 1;
//    }
//}
//
//data load_data_captcha(String[] paths, int n, int m, int k, int w, int h)
//{
//    if(m) paths = get_random_paths(paths, n, m);
//    data d = {0};
//    d.shallow = 0;
//    d.X = load_image_paths(paths, n, w, h);
//    d.y = make_matrix(n, k*NUMCHARS);
//    int i;
//    for(i = 0; i < n; ++i){
//        fill_truth_captcha(paths[i], k, d.y.vals[i]);
//    }
//    if(m) free(paths);
//    return d;
//}
//
//data load_data_captcha_encode(String[] paths, int n, int m, int w, int h)
//{
//    if(m) paths = get_random_paths(paths, n, m);
//    data d = {0};
//    d.shallow = 0;
//    d.X = load_image_paths(paths, n, w, h);
//    d.X.cols = 17100;
//    d.y = d.X;
//    if(m) free(paths);
//    return d;
//}
//
void fill_truth(String path, String[] labels, int k, float[] truth)
{
    int i;
    memset(truth, 0, k*1/*sizeof(float)*/);
    int count = 0;
    for(i = 0; i < k; ++i){
        if(strstr(path, labels[i])){
            truth[i] = 1;
            ++count;
        }
    }
    if(count != 1) printf("Too many or too few labels: %d, %s\n", count, path);
}

void fill_hierarchy(float[] truth, int k, tree hierarchy)
{
    int j;
    for(j = 0; j < k; ++j){
        if(truth[j]!=0){
            int parent = hierarchy.parent[j];
            while(parent >= 0){
                truth[parent] = 1;
                parent = hierarchy.parent[parent];
            }
        }
    }
    int i;
    int count = 0;
    for(j = 0; j < hierarchy.groups; ++j){
        //printf("%d\n", count);
        boolean mask = true;
        for(i = 0; i < hierarchy.group_size[j]; ++i){
            if(truth[count + i]!=0){
                mask = false;
                break;
            }
        }
        if (mask) {
            for(i = 0; i < hierarchy.group_size[j]; ++i){
                truth[count + i] = SECRET_NUM;
            }
        }
        count += hierarchy.group_size[j];
    }
}

matrix load_labels_paths(String[] paths, int n, String[] labels, int k, tree hierarchy)
{
    matrix y = matrix.make_matrix(n, k);
    if(labels==null)return y;
    int i;
    for(i = 0; i < n; ++i){
        fill_truth(paths[i], labels, k, y.vals[i]);
        if(hierarchy!=null){
            fill_hierarchy(y.vals[i], k, hierarchy);
        }
    }
    return y;
}

//matrix load_tags_paths(String[] paths, int n, int k)
//{
//    matrix y = make_matrix(n, k);
//    int i;
//    int count = 0;
//    for(i = 0; i < n; ++i){
//        char label[4096];
//        find_replace(paths[i], "imgs", "labels", label);
//        find_replace(label, "_iconl.jpeg", ".txt", label);
//        FILE *file = fopen(label, "r");
//        if(!file){
//            find_replace(label, "labels", "labels2", label);
//            file = fopen(label, "r");
//            if(!file) continue;
//        }
//        ++count;
//        int tag;
//        while(fscanf(file, "%d", &tag) == 1){
//            if(tag < k){
//                y.vals[i][tag] = 1;
//            }
//        }
//        fclose(file);
//    }
//    printf("%d/%d\n", count, n);
//    return y;
//}
matrix load_tags_paths(String[] paths, int n, int k)
{
    matrix y = matrix.make_matrix(n, k);
    int i;
    int count = 0;
    File file=null;
    Scanner fp = null;
    for(i = 0; i < n; ++i){
//        char label[4096];
//        find_replace(paths[i], "imgs", "labels", label);
//        find_replace(label, "_iconl.jpeg", ".txt", label);
    	String label=paths[i].replace("imgs", "labels").replace("_iconl.jpeg", ".txt");
//        FILE *file = fopen(label, "r");
    	file=new File(label);
        if(!file.exists()){
//            find_replace(label, "labels", "labels2", label);
        	label=label.replace("labels", "labels2");
//            file = fopen(label, "r");
//            if(!file) continue;
        	file=new File(label);
        	if(!file.exists())continue;
        }
        ++count;
        int tag;
//        while(fscanf(file, "%d", &tag) == 1){
        try {
        	fp = new Scanner(new FileReader(file));
            while(fp.hasNext()) {
            	tag=fp.nextInt();
                if(tag < k){
                    y.vals[i][tag] = 1;
                }
            }
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
			try {
//		        fclose(file);
		        if(fp!=null) {
		        	fp.close();
		        }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
        
    }
//    printf("%d/%d\n", count, n);
    System.out.println(String.format("%d/%d\n", count, n));
    return y;
}
//String[] get_labels(String filename)
//{
//    list[] plist = get_paths(filename);
//    String[] labels = (String[] )list_to_array(plist);
//    free_list(plist);
//    return labels;
//}
public static String[] get_labels(String filename)throws IOException,Exception {
	File file=new File(filename);
    if(!file.exists()) utils.file_error(filename);
    BufferedReader fp=new BufferedReader(new FileReader(file));
    String line;
    int nu = 0;
    List<String>list=new ArrayList<String>();
    while((line=fp.readLine()) != null){
    	list.add(line);
    }
    fp.close();
    String[] ret=new String[list.size()];
    ret=list.toArray(ret);
    return ret;
}
static void free_data(data d)
{
//    if(!d.shallow){
//        free_matrix(d.X);
//        free_matrix(d.y);
//    }else{
//        free(d.X.vals);
//        free(d.y.vals);
//    }
}

data load_data_region(int n, String[] paths, int m, int w, int h, int size, int classes, float jitter, float hue, float saturation, float exposure)
{
    String[] random_paths = get_random_paths(paths, n, m);
    int i;
    data d = this;
    d.shallow = 0;

    d.X.rows = n;
    d.X.vals = callocFloat2DArray(d.X.rows, 1/*sizeof(float*)*/);
    d.X.cols = h*w*3;


    int k = size*size*(5+classes);
    d.y = matrix.make_matrix(n, k);
    for(i = 0; i < n; ++i){
        image orig = image.load_image_color(random_paths[i], 0, 0);

        int oh = orig.h;
        int ow = orig.w;

        int dw = (int)(ow*jitter);
        int dh = (int)(oh*jitter);

        int pleft  = (int)utils.rand_uniform(-dw, dw);
        int pright = (int)utils.rand_uniform(-dw, dw);
        int ptop   = (int)utils.rand_uniform(-dh, dh);
        int pbot   = (int)utils.rand_uniform(-dh, dh);

        int swidth =  ow - pleft - pright;
        int sheight = oh - ptop - pbot;

        float sx = (float)swidth  / ow;
        float sy = (float)sheight / oh;

        boolean flip = (utils.random_gen()%2)!=0;
        image cropped = image.crop_image(orig, pleft, ptop, swidth, sheight);

        float dx = ((float)pleft/ow)/sx;
        float dy = ((float)ptop /oh)/sy;

        image sized = image.resize_image(cropped, w, h);
        if(flip) image.flip_image(sized);
        image.random_distort_image(sized, hue, saturation, exposure);
        d.X.vals[i] = sized.data;

        fill_truth_region(random_paths[i], d.y.vals[i], classes, size, flip, dx, dy, 1.f/sx, 1.f/sy);

//        free_image(orig);
//        free_image(cropped);
    }
//    free(random_paths);
    return d;
}

data load_data_compare(int n, String[] paths, int m, int classes, int w, int h)
{
    if(m!=0) paths = get_random_paths(paths, 2*n, m);
    int i,j;
    data d = this;
    d.shallow = 0;
    Scanner fp=null;

    d.X.rows = n;
    d.X.vals = callocFloat2DArray(d.X.rows, 1/*sizeof(float*)*/);
    d.X.cols = h*w*6;

    int k = 2*(classes);
    d.y = matrix.make_matrix(n, k);
    for(i = 0; i < n; ++i){
        image im1 = image.load_image_color(paths[i*2],   w, h);
        image im2 = image.load_image_color(paths[i*2+1], w, h);

        d.X.vals[i] = calloc(d.X.cols, 1/*sizeof(float)*/);
        memcpy(d.X.vals[i],         im1.data, h*w*3*1/*sizeof(float)*/);
        memcpy(d.X.vals[i],  h*w*3, im2.data, 0, h*w*3*1/*sizeof(float)*/);

        int id;
        float iou;

//        char imlabel1[4096];
//        char imlabel2[4096];
        String imlabel1=utils.find_replace(paths[i*2],   "imgs", "labels", null);
        imlabel1=utils.find_replace(imlabel1, "jpg", "txt", imlabel1);
//        FILE *fp1 = fopen(imlabel1, "r");
//        while(fscanf(fp1, "%d %f", &id, &iou) == 2){
//            if (d.y.vals[i][2*id] < iou) d.y.vals[i][2*id] = iou;
//        }
        File file1=new File(imlabel1);
        try {
        	fp = new Scanner(new FileReader(file1));
            while(fp.hasNext()) {
            	id=fp.nextInt();
            	iou=fp.nextFloat();
            	if (d.y.vals[i][2*id] < iou) d.y.vals[i][2*id] = iou;
            }	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(fp!=null) {
					fp.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
        String imlabel2=utils.find_replace(paths[i*2+1], "imgs", "labels", null);
        imlabel2=utils.find_replace(imlabel2, "jpg", "txt", imlabel2);
//        FILE *fp2 = fopen(imlabel2, "r");
//        while(fscanf(fp2, "%d %f", &id, &iou) == 2){
//            if (d.y.vals[i][2*id + 1] < iou) d.y.vals[i][2*id + 1] = iou;
//        }
        File file2=new File(imlabel2);
        try {
        	fp = new Scanner(new FileReader(file2));
            while(fp.hasNext()) {
            	id=fp.nextInt();
            	iou=fp.nextFloat();
            	if (d.y.vals[i][2*id + 1] < iou) d.y.vals[i][2*id + 1] = iou;
            }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(fp!=null) {
					fp.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
        
        for (j = 0; j < classes; ++j){
            if (d.y.vals[i][2*j] > .5 &&  d.y.vals[i][2*j+1] < .5){
                d.y.vals[i][2*j] = 1;
                d.y.vals[i][2*j+1] = 0;
            } else if (d.y.vals[i][2*j] < .5 &&  d.y.vals[i][2*j+1] > .5){
                d.y.vals[i][2*j] = 0;
                d.y.vals[i][2*j+1] = 1;
            } else {
                d.y.vals[i][2*j]   = SECRET_NUM;
                d.y.vals[i][2*j+1] = SECRET_NUM;
            }
        }
//        fclose(fp1);
//        fclose(fp2);

//        free_image(im1);
//        free_image(im2);
    }
//    if(m!=0) free(paths);
    return d;
}

data load_data_swag(String[] paths, int n, int classes, float jitter)
{
    int index = utils.random_gen()%n;
    String random_path = paths[index];

    image orig = image.load_image_color(random_path, 0, 0);
    int h = orig.h;
    int w = orig.w;

    data d = this;
    d.shallow = 0;
    d.w = w;
    d.h = h;

    d.X.rows = 1;
    d.X.vals = callocFloat2DArray(d.X.rows, 1/*sizeof(float*)*/);
    d.X.cols = h*w*3;

    int k = (4+classes)*30;
    d.y = matrix.make_matrix(1, k);

    int dw = (int)(w*jitter);
    int dh = (int)(h*jitter);

    int pleft  = (int)utils.rand_uniform(-dw, dw);
    int pright = (int)utils.rand_uniform(-dw, dw);
    int ptop   = (int)utils.rand_uniform(-dh, dh);
    int pbot   = (int)utils.rand_uniform(-dh, dh);

    int swidth =  w - pleft - pright;
    int sheight = h - ptop - pbot;

    float sx = (float)swidth  / w;
    float sy = (float)sheight / h;

    boolean flip = ((int)utils.random_gen()%2)!=0;
    image cropped = image.crop_image(orig, pleft, ptop, swidth, sheight);

    float dx = ((float)pleft/w)/sx;
    float dy = ((float)ptop /h)/sy;

    image sized = image.resize_image(cropped, w, h);
    if(flip) image.flip_image(sized);
    d.X.vals[0] = sized.data;

    fill_truth_swag(random_path, d.y.vals[0], classes, flip, dx, dy, 1.f/sx, 1.f/sy);

//    free_image(orig);
//    free_image(cropped);

    return d;
}

data load_data_detection(int n, String[] paths, int m, int w, int h, int boxes, int classes, float jitter, float hue, float saturation, float exposure, int small_object)
{
//	System.out.println(" load_data_detection 干活；数据容器 "+this.indexSJX +" n="+n);
    String[] random_paths = get_random_paths(paths, n, m);
    int i;
    data d = this;
    d.shallow = 0;

    d.X.rows = n;
    d.X.vals = callocFloat2DArray(d.X.rows, 1/*sizeof(float*)*/);
    d.X.cols = h*w*3;

    d.y = matrix.make_matrix(n, 5*boxes);
    for(i = 0; i < n; ++i){
        image orig = image.load_image_color(random_paths[i], 0, 0);

        int oh = orig.h;
        int ow = orig.w;

        int dw = (int)(ow*jitter);
        int dh = (int)(oh*jitter);

        int pleft  = (int)utils.rand_uniform_strong(-dw, dw);
        int pright = (int)utils.rand_uniform_strong(-dw, dw);
        int ptop   = (int)utils.rand_uniform_strong(-dh, dh);
        int pbot   = (int)utils.rand_uniform_strong(-dh, dh);

        int swidth =  ow - pleft - pright;
        int sheight = oh - ptop - pbot;

        float sx = (float)swidth  / ow;
        float sy = (float)sheight / oh;

        boolean flip = ((int)utils.random_gen()%2)!=0;
        image cropped = image.crop_image(orig, pleft, ptop, swidth, sheight);

        float dx = ((float)pleft/ow)/sx;
        float dy = ((float)ptop /oh)/sy;

        image sized = image.resize_image(cropped, w, h);
        if(flip) image.flip_image(sized);
        image.random_distort_image(sized, hue, saturation, exposure);
        d.X.vals[i] = sized.data;

        fill_truth_detection(random_paths[i], boxes, d.y.vals[i], classes, flip, dx, dy, 1.f/sx, 1.f/sy, small_object);

//        free_image(orig);
//        free_image(cropped);
    }
//    free(random_paths);
//    System.out.println(" load_data_detection 干活毕；数据容器 "+this.indexSJX +" n="+n+" d.X.cols="+d.X.cols+" d.X.rows="+d.X.rows);
    return d;
}


static Object load_thread(Object ptr, data d)
{
	//srand(time(0));
    //printf("Loading data: %d\n", random_gen());
    load_args a = (load_args)ptr;
//    printf("Loading data 线程 %s : 数据容器 %d , 时刻 %d \n",Thread.currentThread().getName() , d.indexSJX, System.currentTimeMillis());
    if(a.exposure == 0) a.exposure = 1;
    if(a.saturation == 0) a.saturation = 1;
    if(a.aspect == 0) a.aspect = 1;

    if (a.type == data_type.OLD_CLASSIFICATION_DATA){
        d.load_data_old(a.paths, a.n, a.m, a.labels, a.classes, a.w, a.h);
    } else if (a.type == data_type.CLASSIFICATION_DATA){
        d.load_data_augment(a.paths, a.n, a.m, a.labels, a.classes, a.hierarchy, a.min, a.max, a.size, a.angle, a.aspect, a.hue, a.saturation, a.exposure);
    } else if (a.type == data_type.SUPER_DATA){
        d.load_data_super(a.paths, a.n, a.m, a.w, a.h, a.scale);
    } else if (a.type == data_type.WRITING_DATA){
        d.load_data_writing(a.paths, a.n, a.m, a.w, a.h, a.out_w, a.out_h);
    } else if (a.type == data_type.REGION_DATA){
        d.load_data_region(a.n, a.paths, a.m, a.w, a.h, a.num_boxes, a.classes, a.jitter, a.hue, a.saturation, a.exposure);
    } else if (a.type == data_type.DETECTION_DATA){
        d.load_data_detection(a.n, a.paths, a.m, a.w, a.h, a.num_boxes, a.classes, a.jitter, a.hue, a.saturation, a.exposure, a.small_object);
    } else if (a.type == data_type.SWAG_DATA){
        d.load_data_swag(a.paths, a.n, a.classes, a.jitter);
    } else if (a.type == data_type.COMPARE_DATA){
        d.load_data_compare(a.n, a.paths, a.m, a.classes, a.w, a.h);
    } else if (a.type == data_type.IMAGE_DATA){
        (a.im) = image.load_image_color(a.path, 0, 0);
        (a.resized) = image.resize_image(a.im, a.w, a.h);
    } else if (a.type == data_type.TAG_DATA){
        d.load_data_tag(a.paths, a.n, a.m, a.classes, a.min, a.max, a.size, a.angle, a.aspect, a.hue, a.saturation, a.exposure);
    }
//    free(ptr);
    return 0;
}

static pthread_t load_data_in_thread(load_args args, data d)
{
	pthread_t thread=new pthread_t() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			load_thread(args,d);
		}
	};
//    pthread_t thread;
//    load_args *ptr = calloc(1, sizeof(struct load_args));
//    *ptr = args;
//    if(pthread_create(&thread, 0, load_thread, ptr)) error("Thread creation failed");
    return thread;
}

static Object load_threads(Object ptr)
{
	//srand(time(0));
    int i;
    load_args args = (load_args)ptr;
    if (args.threads == 0) args.threads = 1;
//    data out = args.d;
    int total = args.n;
//    free(ptr);
    data[] buffers = new data[args.threads];//calloc(args.threads, sizeof(data));
    pthread_t[] threads = new pthread_t[args.threads];//calloc(args.threads, sizeof(pthread_t));
    for(i = 0; i < args.threads; ++i){
    	buffers[i]=new data(i);/////////by sjx
//        args.d = buffers[i];//可以知道的是args中数据不可能被修改，因为所有线程共用的；args.d 是一个线程一块，是各线程用来写的；
        args.n = (i+1) * total/args.threads - i * total/args.threads;
//        System.out.println(String.format(" 第 %d 个线程令其加载到的图片 %d 个", i, args.n));
        threads[i] = load_data_in_thread(args,buffers[i]);
    	threads[i].start();//启动
//    	System.out.println("在前一行启动第 "+i +" 个线程，时刻="+System.currentTimeMillis());
    }
//    long time=System.currentTimeMillis();
//	long timei=System.currentTimeMillis();
    for(i = 0; i < args.threads; ++i){
//        pthread_join(threads[i], 0);
    	try {
    		
//    		timei=System.currentTimeMillis();
			threads[i].join();
//			System.err.println("threads["+i+"].join(); takes "+(System.currentTimeMillis()-timei)+" ms");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.print("threads["+i+"].join(); ex:");
			e.printStackTrace();
		}
    }
//    System.out.println(data.class.getName()+" : 异步加载的图片数据，在这里汇集(等待全部加载数据的线程完毕，耗时 "+(System.currentTimeMillis()-time)+" ms)");
    args.d.concat_datas(buffers, args.threads);
//    args.d=out;
//    out.shallow = 0;
//    for(i = 0; i < args.threads; ++i){
//        buffers[i].shallow = 1;
//        free_data(buffers[i]);
//    }
//    free(buffers);
//    free(threads);
    return 0;
}

public static pthread_t load_data(load_args args)
{
    pthread_t thread=new pthread_t() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			load_threads(args);
		}
    	
    };
//    pthread_t thread;
//    struct load_args *ptr = calloc(1, sizeof(struct load_args));
//    *ptr = args;
//    if(pthread_create(&thread, 0, load_threads, ptr)) error("Thread creation failed");
    return thread;
}

data load_data_writing(String[] paths, int n, int m, int w, int h, int out_w, int out_h)
{
    if(m!=0) paths = get_random_paths(paths, n, m);
    String[] replace_paths = find_replace_paths(paths, n, ".png", "-label.png");
    data d = this;
    d.shallow = 0;
    d.X = load_image_paths(paths, n, w, h);
    d.y = load_image_paths_gray(replace_paths, n, out_w, out_h);
//    if(m) free(paths);
//    int i;
//    for(i = 0; i < n; ++i) free(replace_paths[i]);
//    free(replace_paths);
    return d;
}

data load_data_old(String[] paths, int n, int m, String[] labels, int k, int w, int h)
{
    if(m!=0) paths = get_random_paths(paths, n, m);
    data d = this;
    d.shallow = 0;
    d.X = load_image_paths(paths, n, w, h);
    d.y = load_labels_paths(paths, n, labels, k, null);
//    if(m) free(paths);
    return d;
}

/*
   data load_data_study(String[] paths, int n, int m, String[] labels, int k, int min, int max, int size, float angle, float aspect, float hue, float saturation, float exposure)
   {
   data d = {0};
   d.indexes = calloc(n, sizeof(int));
   if(m) paths = get_random_paths_indexes(paths, n, m, d.indexes);
   d.shallow = 0;
   d.X = load_image_augment_paths(paths, n, min, max, size, angle, aspect, hue, saturation, exposure);
   d.y = load_labels_paths(paths, n, labels, k);
   if(m) free(paths);
   return d;
   }
 */

data load_data_super(String[] paths, int n, int m, int w, int h, int scale)
{
    if(m!=0) paths = get_random_paths(paths, n, m);
    data d = this;
    d.shallow = 0;

    int i;
    d.X.rows = n;
    d.X.vals = callocFloat2DArray(n, 1/*sizeof(float*)*/);
    d.X.cols = w*h*3;

    d.y.rows = n;
    d.y.vals = callocFloat2DArray(n, 1/*sizeof(float*)*/);
    d.y.cols = w*scale * h*scale * 3;

    for(i = 0; i < n; ++i){
        image im = image.load_image_color(paths[i], 0, 0);
        image crop = image.random_crop_image(im, w*scale, h*scale);
        int flip = utils.random_gen()%2;
        if (flip!=0) image.flip_image(crop);
        image resize = image.resize_image(crop, w, h);
        d.X.vals[i] = resize.data;
        d.y.vals[i] = crop.data;
//        free_image(im);
    }

//    if(m!=0) free(paths);
    return d;
}

data load_data_augment(String[] paths, int n, int m, String[] labels, int k, tree hierarchy, int min, int max, int size, float angle, float aspect, float hue, float saturation, float exposure)
{
    if(m!=0) paths = get_random_paths(paths, n, m);
    data d = this;
    d.shallow = 0;
    d.X = load_image_augment_paths(paths, n, min, max, size, angle, aspect, hue, saturation, exposure);
    d.y = load_labels_paths(paths, n, labels, k, hierarchy);
//    if(m) free(paths);
    return d;
}

data load_data_tag(String[] paths, int n, int m, int k, int min, int max, int size, float angle, float aspect, float hue, float saturation, float exposure)
{
    if(m!=0) paths = get_random_paths(paths, n, m);
    data d = new data();// {0};
    d.w = size;
    d.h = size;
    d.shallow = 0;
    d.X = load_image_augment_paths(paths, n, min, max, size, angle, aspect, hue, saturation, exposure);
    d.y = load_tags_paths(paths, n, k);
//    if(m) free(paths);
    return d;
}

//static matrix concat_matrix(matrix m1, matrix m2) // sjx : moved to matrix.java
//{
//    int i, count = 0;
//    matrix m=new matrix();
//    m.cols = m1.cols;
//    m.rows = m1.rows+m2.rows;
//    m.vals = callocFloat2DArray(m1.rows + m2.rows, 1/*sizeof(float*)*/);
//    for(i = 0; i < m1.rows; ++i){
//        m.vals[count++] = m1.vals[i];
//    }
//    for(i = 0; i < m2.rows; ++i){
//        m.vals[count++] = m2.vals[i];
//    }
//    return m;
//}
/**
 *sjx
 * */
private data concat_data(data d2)
{
    this.shallow = 1;
    this.X.concat_matrix(d2.X);
    this.y.concat_matrix(d2.y);
    return this;
}
/**
 * sjx
 * */
private data concat_datas(data[] d, int n)
{
    int i;
    for(i = 0; i < n; ++i){
//    	System.out.println(String.format("汇总数据： 第 %d 个线程加载到的图片 %d x %d ", i, d[i].X.cols, d[i].X.rows));
    	this.concat_data(d[i]);
    }
    return this;
}

//data load_categorical_data_csv(String filename, int target, int k)
//{
//    data d = new data();
//    d.shallow = 0;
//    matrix X = csv_to_matrix(filename);
//    float[] truth_1d = pop_column(X, target);
//    float[][] truth = one_hot_encode(truth_1d, X.rows, k);
//    matrix y;
//    y.rows = X.rows;
//    y.cols = k;
//    y.vals = truth;
//    d.X = X;
//    d.y = y;
//    free(truth_1d);
//    return d;
//}
//
//data load_cifar10_data(String filename)
//{
//    data d = {0};
//    d.shallow = 0;
//    long i,j;
//    matrix X = make_matrix(10000, 3072);
//    matrix y = make_matrix(10000, 10);
//    d.X = X;
//    d.y = y;
//
//    FILE *fp = fopen(filename, "rb");
//    if(!fp) file_error(filename);
//    for(i = 0; i < 10000; ++i){
//        unsigned char bytes[3073];
//        fread(bytes, 1, 3073, fp);
//        int class = bytes[0];
//        y.vals[i][class] = 1;
//        for(j = 0; j < X.cols; ++j){
//            X.vals[i][j] = (double)bytes[j+1];
//        }
//    }
//    //translate_data_rows(d, -128);
//    scale_data_rows(d, 1./255);
//    //normalize_data_rows(d);
//    fclose(fp);
//    return d;
//}

public static void get_random_batch(data d, int n, float[] X, float[] y)
{
    int j;
    for(j = 0; j < n; ++j){
        int index = utils.random_gen()%d.X.rows;
        memcpy(X,j*d.X.cols, d.X.vals[index],0, d.X.cols*1/*sizeof(float)*/);
        memcpy(y,j*d.y.cols, d.y.vals[index],0, d.y.cols*1/*sizeof(float)*/);
    }
}
/**
 * 这里把一次数据加载，重新拷贝一遍
 * */
public static void get_next_batch(data d, int n, int offset, float[] X, float[] y)
{
    int j;
    for(j = 0; j < n; ++j){
        int index = offset + j;
        memcpy(X, j*d.X.cols, d.X.vals[index],0, d.X.cols*1/*sizeof(float)*/);
        memcpy(y,j*d.y.cols, d.y.vals[index],0, d.y.cols*1/*sizeof(float)*/);
    }
}

void smooth_data(data d)
{
    int i, j;
    float scale = 1.f / d.y.cols;
    float eps = .1f;
    for(i = 0; i < d.y.rows; ++i){
        for(j = 0; j < d.y.cols; ++j){
            d.y.vals[i][j] = eps * scale + (1-eps) * d.y.vals[i][j];
        }
    }
}

//data load_all_cifar10()
//{
//    data d = {0};
//    d.shallow = 0;
//    int i,j,b;
//    matrix X = make_matrix(50000, 3072);
//    matrix y = make_matrix(50000, 10);
//    d.X = X;
//    d.y = y;
//
//
//    for(b = 0; b < 5; ++b){
////        char buff[256];
////        sprintf(buff, "data/cifar/cifar-10-batches-bin/data_batch_%d.bin", b+1);
//        String buff=String.format("data/cifar/cifar-10-batches-bin/data_batch_%d.bin", b+1);
//        FILE *fp = fopen(buff, "rb");
//        if(!fp) file_error(buff);
//        for(i = 0; i < 10000; ++i){
//            unsigned char bytes[3073];
//            fread(bytes, 1, 3073, fp);
//            int class = bytes[0];
//            y.vals[i+b*10000][class] = 1;
//            for(j = 0; j < X.cols; ++j){
//                X.vals[i+b*10000][j] = (double)bytes[j+1];
//            }
//        }
//        fclose(fp);
//    }
//    //normalize_data_rows(d);
//    //translate_data_rows(d, -128);
//    scale_data_rows(d, 1./255);
//    smooth_data(d);
//    return d;
//}

//data load_go(String filename)
//{
//    FILE *fp = fopen(filename, "rb");
//    matrix X = make_matrix(3363059, 361);
//    matrix y = make_matrix(3363059, 361);
//    int row, col;
//
//    if(!fp) file_error(filename);
//    String label;
//    int count = 0;
//    while((label = fgetl(fp))){
//        int i;
//        if(count == X.rows){
//            X = resize_matrix(X, count*2);
//            y = resize_matrix(y, count*2);
//        }
//        sscanf(label, "%d %d", &row, &col);
//        String board = fgetl(fp);
//
//        int index = row*19 + col;
//        y.vals[count][index] = 1;
//
//        for(i = 0; i < 19*19; ++i){
//            float val = 0;
//            if(board[i] == '1') val = 1;
//            else if(board[i] == '2') val = -1;
//            X.vals[count][i] = val;
//        }
//        ++count;
//        free(label);
//        free(board);
//    }
//    X = resize_matrix(X, count);
//    y = resize_matrix(y, count);
//
//    data d = {0};
//    d.shallow = 0;
//    d.X = X;
//    d.y = y;
//
//
//    fclose(fp);
//
//    return d;
//}


void randomize_data(data d)
{
    int i;
    for(i = d.X.rows-1; i > 0; --i){
        int index = utils.random_gen()%i;
        float[] swap = d.X.vals[index];
        d.X.vals[index] = d.X.vals[i];
        d.X.vals[i] = swap;

        swap = d.y.vals[index];
        d.y.vals[index] = d.y.vals[i];
        d.y.vals[i] = swap;
    }
}

void scale_data_rows(data d, float s)
{
    int i;
    for(i = 0; i < d.X.rows; ++i){
        utils.scale_array(d.X.vals[i], d.X.cols, s);
    }
}

void translate_data_rows(data d, float s)
{
    int i;
    for(i = 0; i < d.X.rows; ++i){
    	utils.translate_array(d.X.vals[i], d.X.cols, s);
    }
}

void normalize_data_rows(data d)
{
    int i;
    for(i = 0; i < d.X.rows; ++i){
    	utils.normalize_array(d.X.vals[i], d.X.cols);
    }
}

//data get_data_part(data d, int part, int total)
//{
//    data p = new data();
//    p.shallow = 1;
//    p.X.rows = d.X.rows * (part + 1) / total - d.X.rows * part / total;
//    p.y.rows = d.y.rows * (part + 1) / total - d.y.rows * part / total;
//    p.X.cols = d.X.cols;
//    p.y.cols = d.y.cols;
//    p.X.vals = d.X.vals + d.X.rows * part / total;
//    p.y.vals = d.y.vals + d.y.rows * part / total;
//    return p;
//}

data get_random_data(data d, int num)
{
    data r = new data();
    r.shallow = 1;

    r.X.rows = num;
    r.y.rows = num;

    r.X.cols = d.X.cols;
    r.y.cols = d.y.cols;

    r.X.vals = new float[num][];//calloc(num, sizeof(float[] ));
    r.y.vals = new float[num][];//calloc(num, sizeof(float[] ));

    int i;
    for(i = 0; i < num; ++i){
        int index = utils.random_gen()%d.X.rows;
        r.X.vals[i] = d.X.vals[index];
        r.y.vals[i] = d.y.vals[index];
    }
    return r;
}

data[] split_data(data d, int part, int total)
{
    data[] split = new data[2];//calloc(2, sizeof(data));
    int i;
    int start = part*d.X.rows/total;
    int end = (part+1)*d.X.rows/total;
    data train=new data();
    data test=new data();
    train.shallow = test.shallow = 1;

    test.X.rows = test.y.rows = end-start;
    train.X.rows = train.y.rows = d.X.rows - (end-start);
    train.X.cols = test.X.cols = d.X.cols;
    train.y.cols = test.y.cols = d.y.cols;

    train.X.vals = new float[train.X.rows][];//calloc(train.X.rows, 1/*sizeof(float*)*/);
    test.X.vals = new float[test.X.rows][];//calloc(test.X.rows, 1/*sizeof(float*)*/);
    train.y.vals = new float[train.y.rows][];//calloc(train.y.rows, 1/*sizeof(float*)*/);
    test.y.vals = new float[test.y.rows][];//calloc(test.y.rows, 1/*sizeof(float*)*/);

    for(i = 0; i < start; ++i){
        train.X.vals[i] = d.X.vals[i];
        train.y.vals[i] = d.y.vals[i];
    }
    for(i = start; i < end; ++i){
        test.X.vals[i-start] = d.X.vals[i];
        test.y.vals[i-start] = d.y.vals[i];
    }
    for(i = end; i < d.X.rows; ++i){
        train.X.vals[i-(end-start)] = d.X.vals[i];
        train.y.vals[i-(end-start)] = d.y.vals[i];
    }
    split[0] = train;
    split[1] = test;
    return split;
}
@Override
public String toString() {
	return "data [w=" + w + ", h=" + h + ", X=" + X + ", y=" + y + ", shallow=" + shallow + ", num_boxes="
			+ Arrays.toString(num_boxes) + ", boxes=" + Arrays.toString(boxes) + ", indexSJX=" + indexSJX + "]";
}

}