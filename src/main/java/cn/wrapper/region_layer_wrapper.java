package cn.wrapper;

import cn.activations;
import cn.box;
import cn.layer;
import cn.tree;
import cn.utils;
import cn.drknt.lib.ACTIVATION;

public abstract class region_layer_wrapper extends layer{
	protected static box float_to_box(float []f, int f_offset, int stride) {
		return box.float_to_box(f, f_offset, stride);
	}
	protected static float box_iou(box a, box b) {
		return box.box_iou(a, b);
	}
	protected static float get_hierarchy_probability(float []x, int x_offset, tree hier, int c, int stride) {
		return tree.get_hierarchy_probability(x, x_offset, hier, c, stride);
	}
	protected static void activate_array(float []x, int x_offset, final int n, final ACTIVATION a) {
		activations.activate_array(x, x_offset, n, a);
	}
	protected static float mag_array(float[] a, int n) {
		return utils.mag_array(a, n);
	}
	protected static void hierarchy_predictions(float []predictions, int predictions_offset, int n, tree hier, boolean only_leaves, int stride) {
		tree.hierarchy_predictions(predictions, predictions_offset, n, hier, only_leaves, stride);
	}
	protected static int hierarchy_top_prediction(float []predictions, int predictions_offset, tree hier, float thresh, int stride) {
		return tree.hierarchy_top_prediction(predictions, predictions_offset, hier, thresh, stride);
	}
}
