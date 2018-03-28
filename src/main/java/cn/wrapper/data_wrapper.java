package cn.wrapper;

import cn.BaseFunction;
import cn.image;
import cn.matrix;
import cn.utils;

public class data_wrapper extends BaseFunction{
	public static image load_image(String filename, int w, int h, int c) {
		return image.load_image(filename, w, h, c);
	}
	public static matrix make_matrix(int rows, int cols) {
		return matrix.make_matrix(rows, cols);
	}
	public static image make_image(int w, int h, int c) {
		return image.make_image(w, h, c);
	}
	public static image load_image_color(String filename, int w, int h) {
		return image.load_image_color(filename, w, h);
	}
	public static void flip_image(image a) {
		image.flip_image(a);
	}
	public static void place_image(image im, float w, float h, float dx, float dy, image canvas) {
		image.place_image(im, (int)w, (int)h, (int)dx, (int)dy, canvas);
	}
	public static void random_distort_image(image im, float hue, float saturation, float exposure) {
		image.random_distort_image(im, hue, saturation, exposure);
	}
	public static void free_image(image m) {
		image.free_image(m);
	}
	public static void fill_image(image m, double s) {
		image.fill_image(m, (float)s);
	}
	protected static float rand_uniform(double min, double max) {
		return utils.rand_uniform((float)min, (float)max);
	}
	public static String find_replace(String father, String origstr, String newstr, String dest) {//dest is not used
		return utils.find_replace(father, origstr, newstr, dest);
	}
	public static int constrain_int(int a, int min, int max) {
		return utils.constrain_int(a, min, max);
	}
	public static float constrain(float min, float max, float a) {
		return utils.constrain(min, max, a);
	}
	public static void pthread_join(pthread_t thread, int millionsec) {
		try {
			thread.join(millionsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void free_matrix(matrix m) {
		matrix.free(m);
	}
}
