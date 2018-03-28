package cn.wrapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.BaseFunction;
import cn.activations;
import cn.convolutional_layer;
import cn.layer;
import cn.list;
import cn.maxpool_layer;
import cn.network;
import cn.option_list;
import cn.region_layer;
import cn.tree;
import cn.utils;
import cn.drknt.lib.ACTIVATION;
import cn.drknt.lib.blas;

public class parser_wrapper extends BaseFunction{
	public static float option_find_float(list l, String key, double def) {
		return option_list.option_find_float(l, key, (float)def);
	}
	public static float option_find_float_quiet(list l, String key, float def) {
		return option_list.option_find_float_quiet(l, key, def);
	}
	public static int option_find_int(list l, String key, int def) {
		return option_list.option_find_int(l, key, def);
	}
	public static int option_find_int_quiet(list l, String key, int def) {
		return option_list.option_find_int_quiet(l, key, def);
	}
	public static String option_find_str(list l, String key, String def) {
		return option_list.option_find_str(l, key, def);
	}
	public static String option_find_str(list l, String key, int def) {
		return option_list.option_find_str(l, key, null);
	}
	public static String option_find(list l, String key) {
		return option_list.option_find(l, key);
	}
	public static network make_network(int n) {
		return network.make_network(n);
	}
	public static layer get_network_output_layer(network net) {
		return network.get_network_output_layer(net);
	}
	public static tree read_tree(String filename) {
		return tree.read_tree(filename);
	}
	public static int[] read_map(String filename) {
		return utils.read_map(filename);
	}
	public static void error(final String s) {
		utils.error(s);
	}
	public static void file_error(final String s) {
		utils.file_error(s);
	}
	//new add
	public static boolean option_find_boolean(list l, String key, int def) {
		return option_list_extends.option_find_boolean(l, key, def);
	}
	//new add
	public static boolean option_find_boolean_quiet(list l, String key, int def) {
		return option_list_extends.option_find_boolean_quiet(l, key, def);
	}
	public static void option_unused(list l) {
		option_list.option_unused(l);
	}
	public static void free_list(list l) {
		list.free_list(l);
	}
	public static void list_insert(list l, Object val) {
		list.list_insert(l, val);
	}
	public static ACTIVATION get_activation(String s) {
		return activations.get_activation(s);
	}
	public static convolutional_layer make_convolutional_layer(int batch, int h, int w, int c, int n, int groups, int size, int stride, int padding, ACTIVATION activation, boolean batch_normalize, boolean binary, boolean xnor, boolean adam) {
		return convolutional_layer.make_convolutional_layer(batch, h, w, c, n, groups, size, stride, padding, activation, batch_normalize, binary, xnor, adam);
	}
	public static layer make_region_layer(int batch, int w, int h, int n, int classes, int coords) {
		return region_layer.make_region_layer(batch, w, h, n, classes, coords);
	}
	public static maxpool_layer make_maxpool_layer(int batch, int h, int w, int c, int size, int stride, int padding) {
		return maxpool_layer.make_maxpool_layer(batch, h, w, c, size, stride, padding);
	}
	public static void binarize_weights(float []weights, int n, int size, float []binary) {
		convolutional_layer.binarize_weights(weights, n, size, binary);
	}
	public static void fwrite(float data, int n, int size, FILE fp)throws IOException, Exception{//n not used
		fp.writeFloat(data);
	}
	public static void fwrite(int data, int n, int size, FILE fp)throws IOException, Exception{//n not used
		fp.writeInt(data);
	}
	public static void fwrite(float[] data, int n, int size, FILE fp)throws IOException, Exception{//n not used
		for (int i = 0; i < size; i++) {
			fp.writeFloat(data[i]);
		}
	}
	public static void fread(float[] dest, int n, int size, FILE fp) throws IOException {//n is not used
		for (int i = 0; i < size; i++) {
			dest[i]=fp.readFloat();
		}
	}
	public static void fread(int[] dest, int n, int size, FILE fp) throws IOException {//n is not used
		for (int i = 0; i < size; i++) {
			dest[i]=fp.readInt();
		}
	}
	/**
	 * @param dest not used
	 * @param n
	 * @param size
	 * @param fp
	 * @return
	 * @throws IOException
	 */
	public static int fread(int dest, int n, int size, FILE fp) throws IOException {//n is not used
		return fp.readInt();
	}
	public static FILE fopen(String filename, String actions) throws IOException {
		File file=new File(filename);
//	    if(!file.exists())file.createNewFile();
		FILE fp= new FILE(filename, actions);
	    return fp;
	}
	public static void fflush(int fileid) {
		
	}
	public static void fclose(FILE fp) throws IOException {
		fp.close();
	}
	public static void fill_cpu(int N, float ALPHA, float[] X, int INCX) {
		blas.fill_cpu(N, ALPHA, X, INCX);
	}
}
