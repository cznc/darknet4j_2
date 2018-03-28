package cn.wrapper;

import cn.image;
import cn.utils;
import cn.drknt.lib.blas;

public class image_wrapper extends image_struct{
	static protected void fill_cpu(int N, float ALPHA, float[] X, int INCX) {
		blas.fill_cpu(N, ALPHA, X, INCX);
	}
	protected float rand_normal() {
		return utils.rand_normal();
	}
	public static int rand_int(int min, int max) {
		return utils.rand_int(min, max);
	}
	protected int constrain_int(int a, int min, int max) {
		return utils.constrain_int(a, min, max);
	}
	protected static float dist_array(float[] a, float[] b, int n, int sub) {
		return utils.dist_array(a, b, n, sub);
	}
	protected static void fill_image(image m, float s) {
		image.fill_image(m, s);
	}
	protected static float rand_scale(float s) {
		return utils.rand_scale(s);
	}
	protected static float rand_uniform(float min, float max) {
		return utils.rand_uniform(min, max);
	}
	public static float mag_array(float[] a, int n) {
		return utils.mag_array(a, n);
	}
	public static int max_index(float[] a, int n) {
		return utils.max_index(a, n);
	}
}
