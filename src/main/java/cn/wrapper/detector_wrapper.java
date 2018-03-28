package cn.wrapper;

import java.io.File;
import java.io.IOException;

import cn.BaseFunction;
import cn.box;
import cn.data;
import cn.image;
import cn.layer;
import cn.list;
import cn.network;
import cn.option_list;
import cn.parser;
import cn.region_layer;
import cn.utils;
import cn.drknt.lib.Load_args;

public class detector_wrapper extends BaseFunction {
	public static list read_data_cfg(String filename) {
		return option_list.read_data_cfg(filename);
	}
	public static String basecfg(String cfgfile) {
		return utils.basecfg(cfgfile);
	}
	public static network load_network(String cfg, String weights, int clear) throws IOException, Exception {
		return network.load_network(cfg, weights, clear!=0);
	}
	public static Load_args get_base_args(network net) {
		return network.get_base_args(net);
	}
	public static String option_find_str(list l, String key, String def) {
		return option_list.option_find_str(l, key, def);
	}
	public static String find_char_arg(int argc, String[] argv, String arg, String def) {
		return utils.find_char_arg(argc, argv, arg, def);
	}
	public static String find_char_arg(int argc, String[] argv, String arg, int def) {
		return utils.find_char_arg(argc, argv, arg, null);
	}
	public static float find_float_arg(int argc, String[] argv, String arg, double def) {
		return utils.find_float_arg(argc, argv, arg, (float)def);
	}
	public static int find_int_arg(int argc, String[] argv, String arg, int def) {
		return utils.find_int_arg(argc, argv, arg, def);
	}
	public static int find_arg(int argc, String[] argv, String arg) {
		return utils.find_arg(argc, argv, arg);
	}
	public static int resize_network(network net, int w, int h)throws Exception{
		return network.resize_network(net, w, h);
	}
	public static int get_current_batch(network net) {
		return network.get_current_batch(net);
	}
	public static float train_network(network net, data d) {
		assert(d.X.rows!=0 || net.batch != 0);
		float loss = network.train_network(net, d);
		assert(!Float.isNaN(loss));
		return loss;
	}
	public static float get_current_rate(network net) {
		return network.get_current_rate(net);
	}
	public static void save_weights(network net, String filename)throws IOException, Exception{
		File file=new File(filename);
		if(!file.exists()) {
			file=file.getParentFile();
			if(!file.exists()) {
				System.out.println("create folder: "+file.getAbsolutePath());
				file.mkdirs();
			}
		}
		parser.save_weights(net, filename);
	}
	public static int option_find_int(list list, String key, int def) {
		return option_list.option_find_int(list, key, def);
	}
	public static void free_data(data d) {
		data.free_data(d);
	}
	public static pthread_t load_data(Load_args args) {
		return data.load_data(args);
	}
	public static void pthread_join(pthread_t thread, int millionsec) {
		try {
			thread.join(millionsec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static String[] get_paths2(String filename)throws IOException,Exception {
		return data.get_paths2(filename);
	}
	public static String[] get_labels(String filename)throws IOException,Exception {
		return data.get_labels(filename);
	}
	public static image[][] load_alphabet(){
		return image.load_alphabet();
	}
	public static void set_batch_network(network net, int b) {
		network.set_batch_network(net, b);
	}
	public static image load_image_color(String filename, int w, int h) {
		return image.load_image_color(filename, w, h);
	}
	public static image letterbox_image(image im, int w, int h) {
		return image.letterbox_image(im, w, h);
	}
	public static float[] network_predict(network net, float[] input) {
		return network.network_predict(net, input);
	}
	public static void get_region_boxes(layer l, int w, int h, int netw, int neth, float thresh, float [][]probs, box []boxes, float [][]masks, int only_objectness, int map, float tree_thresh, int relative){
		region_layer.get_region_boxes(l, w, h, netw, neth, thresh, probs, boxes, masks, only_objectness!=0, null, tree_thresh, relative!=0);
	}
	public static void do_nms_sort(box []boxes, float [][]probs, int total, int classes, float thresh) {
		box.do_nms_sort(boxes, probs, total, classes, thresh);
	}
	public static void draw_detections(image im, int num, float thresh, box []boxes, float[][] probs, float[][] masks, String[] names, image [][]alphabet, int classes) {
		image.draw_detections(im, num, thresh, boxes, probs, masks, names, alphabet, classes);
	}
	public static void save_image(image im, final String name) {
		image.save_image(im, name);
	}
	
}
