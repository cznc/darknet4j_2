package cn.wrapper;

import cn.layer;
import cn.tree;

public class network_variables extends network_wrapper {
	public int n;
    public int batch;
    public /*size_t**/int[] seen;
    public int[] t;
    public float epoch;
    public int subdivisions;
    public layer[] layers;
    public float[] output;
    public Learning_rate_policy policy;

    public float learning_rate;
    public float momentum;
    public float decay;
    public float gamma;
    public float scale;
    public float power;
    public int time_steps;
    public int step;
    public int max_batches;
    public float[] scales;
    public int[] steps;
    public int num_steps;
    public int burn_in;

    public boolean adam;
    public float B1;
    public float B2;
    public float eps;

    public int inputs;
    public int outputs;
    public int truths;
    public int notruth;
    public int h, w, c;
    public int max_crop;
    public int min_crop;
    public float max_ratio;
    public float min_ratio;
    public int center;
    public float angle;
    public float aspect;
    public float exposure;
    public float saturation;
    public float hue;
    public int random;

    public int gpu_index;
    public tree hierarchy;

    public float[] input;
    public float[] truth;
    public float[] delta;
    public float[] workspace;
    public boolean train;
    public int index;
    public float[] cost;

//#ifdef GPU
//    float[] input_gpu;
//    float[] truth_gpu;
//    float[] delta_gpu;
//    float[] output_gpu;
//#endif

}
