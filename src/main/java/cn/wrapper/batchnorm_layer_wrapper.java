package cn.wrapper;

import cn.convolutional_layer;
import cn.layer;
import cn.drknt.lib.blas;

public abstract class batchnorm_layer_wrapper extends layer{
	public static void copy_cpu(int N, float []X, int INCX, float []Y, int INCY) {
		blas.copy_cpu(N, X, INCX, Y, INCY);
	}
	public static void mean_cpu(float []x, int batch, int filters, int spatial, float []mean) {
		blas.mean_cpu(x, batch, filters, spatial, mean);
	}
	public static void variance_cpu(float []x, float []mean, int batch, int filters, int spatial, float []variance) {
		blas.variance_cpu(x, mean, batch, filters, spatial, variance);
	}
	public static void scal_cpu(int N, float ALPHA, float []X, int INCX) {
		blas.scal_cpu(N, ALPHA, X, INCX);
	}
	public static void axpy_cpu(int N, float ALPHA, float []X, int INCX, float []Y, int INCY) {
		blas.axpy_cpu(N, ALPHA, X, INCX, Y, INCY);
	}
	public static void normalize_cpu(float []x, float []mean, float []variance, int batch, int filters, int spatial) {
		blas.normalize_cpu(x, mean, variance, batch, filters, spatial);
	}
	public static void scale_bias(float[] output, float[] scales, int batch, int n, int size) {
		convolutional_layer.scale_bias(output, scales, batch, n, size);
	}
	public static void backward_bias(float[] bias_updates, float[] delta, int batch, int n, int size) {
		convolutional_layer.backward_bias(bias_updates, delta, batch, n, size);
	}
	public static void add_bias(float[] output, float[] biases, int batch, int n, int size) {
		convolutional_layer.add_bias(output, biases, batch, n, size);
	}
}
