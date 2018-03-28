package cn.wrapper;

import cn.image;
import cn.layer;

public abstract class maxpool_layer_wrapper extends layer{
	public static image float_to_image(int w, int h, int c, float[] data) {
		return image.float_to_image(w, h, c, data);
	}
}
