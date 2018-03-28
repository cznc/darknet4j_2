package cn.wrapper;

import cn.activations;
import cn.batchnorm_layer;
import cn.image;
import cn.layer;
import cn.network;
import cn.utils;
import cn.drknt.lib.ACTIVATION;
import cn.drknt.lib.blas;
import cn.drknt.lib.col2im;
import cn.drknt.lib.im2col;

public abstract class convolutional_layer_wrapper extends layer {
	static protected float rand_normal() {
		return utils.rand_normal();
	}

	protected static float sum_array(float[] a, int n) {
		return utils.sum_array(a, 0, n);
	}
	protected static float sum_array(float[] a, int a_offset, int n) {
		return utils.sum_array(a, a_offset, n);
	}

	protected void fill_cpu(int N, float ALPHA, float[] X, int INCX) {
		blas.fill_cpu(N, ALPHA, X, INCX);
	}

	protected static void activate_array(float[] x, final int n, final ACTIVATION a) {
		activations.activate_array(x, n, a);
	}
	public static void scal_cpu(int N, float ALPHA, float []X, int INCX) {
		blas.scal_cpu(N, ALPHA, X, INCX);
	}
	public static void axpy_cpu(int N, float ALPHA, float []X, int INCX, float []Y, int INCY) {
		blas.axpy_cpu(N, ALPHA, X, INCX, Y, INCY);
	}
	public static void im2col_cpu(float[] data_im, int data_im_offset, int channels, int height, int width, int ksize,
			int stride, int pad, float[] data_col, int data_col_offset) {
		im2col.im2col_cpu(data_im, data_im_offset, channels, height, width, ksize, stride, pad, data_col,
				data_col_offset);
	}
	public static void gemm(int TA, int TB, int M, int N, int K, float ALPHA, 
	        float[] A, int A_offset, int lda, 
	        float[] B, int B_offset, int ldb,
	        float BETA,
	        float[] C, int C_offset, int ldc) {
		cn.drknt.lib.gemm.gemm_cpu(TA!=0, TB!=0, M, N, K, ALPHA, A, A_offset, lda, B, B_offset, ldb, BETA, C, C_offset, ldc);
	}
	public static void forward_batchnorm_layer(layer l, network net) {
		batchnorm_layer.forward_batchnorm_layer(l, net);
	}
	public static void backward_batchnorm_layer(layer l, network net) {
		batchnorm_layer.backward_batchnorm_layer(l, net);
	}
	public static void gradient_array(final float []x, final int n, final ACTIVATION a, float []delta) {
		activations.gradient_array(x, n, a, delta);
	}
	public static void col2im_cpu(float[] data_col, int data_col_offset,
	         int channels,  int height,  int width,
	         int ksize,  int stride, int pad, float[] data_im, int data_im_offset) 
	{
		col2im.col2im_cpu(data_col, data_col_offset, channels, height, width, ksize, stride, pad, data_im, data_im_offset);
	}
	public static image float_to_image(int w, int h, int c, float[] data, int data_offset) {
		return image.float_to_image(w, h, c, data, data_offset);
	}
	public static image float_to_image(int w, int h, int c, float[] data) {
		return image.float_to_image(w, h, c, data);
	}
	public static void rgbgr_image(image im) {
		image.rgbgr_image(im);
	}
	public static void scale_image(image m, float s) {
		image.scale_image(m, s);
	}
	public static image copy_image(image p) {
		return image.copy_image(p);
	}
	public static void normalize_image(image p) {
		image.normalize_image(p);
	}
	public static void show_images(image []ims, int n, String window) {
		image.show_images(ims, n, window);
	}
	public static image collapse_image_layers(image source, int border) {
		return image.collapse_image_layers(source, border);
	}
	public static void free_image(image m) {
		image.free_image(m);
	}
}
