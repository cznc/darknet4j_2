package cn.wrapper;

import java.io.IOException;

import cn.BaseFunction;
import cn.convolutional_layer;
import cn.data;
import cn.layer;
import cn.maxpool_layer;
import cn.network;
import cn.parser;
import cn.region_layer;
import cn.utils;
import cn.drknt.lib.blas;

public class network_wrapper extends BaseFunction{
	public static network parse_network_cfg(String filename) throws IOException, Exception{
		return parser.parse_network_cfg(filename);//Exception in thread "main" java.lang.StackOverflowError
	}
	public static void load_weights(network net, String filename) throws IOException, Exception{
		network.load_weights(net, filename);
	}
	public static float rand_uniform(float min, float max) {
		return utils.rand_uniform(min, max);
	}
	public static void fill_cpu(int N, float ALPHA, float[] X, int INCX) {
		blas.fill_cpu(N, ALPHA, X, INCX);
	}
	public static void get_next_batch(data d, int n, int offset, float[] X, float[] y) {
//		data.get_next_batch
	}
	public static void resize_convolutional_layer(layer l, int w, int h) {
		convolutional_layer.resize_convolutional_layer((convolutional_layer)l, w, h);
	}
	public static void resize_maxpool_layer(layer l, int w, int h) {
		maxpool_layer.resize_maxpool_layer((maxpool_layer)l, w, h);
	}
	public static void resize_region_layer(layer l, int w, int h) {
		region_layer.resize_region_layer(l, w, h);
	}
}
