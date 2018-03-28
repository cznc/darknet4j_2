package cn.wrapper;

import cn.BaseFunction;
import cn.layer;
import cn.tree;
import cn.drknt.lib.ACTIVATION;
import cn.drknt.lib.COST_TYPE;
import cn.drknt.lib.LAYER_TYPE;

public class layer_variables extends BaseFunction {
    public LAYER_TYPE type;
    public ACTIVATION activation;
    public COST_TYPE cost_type;
    
    public boolean batch_normalize;
    public int shortcut;
    public int batch;
    public int forced;
    public boolean flipped;
    public int inputs;
    public int outputs;
    public int nweights;
    public int nbiases;
    public int extra;
    public int truths;
    public int h,w,c;
    public int out_h, out_w, out_c;
    public int n;
    public int max_boxes;
    public int groups;
    public int size;
    public int side;
    public int stride;
    public int reverse;
    public int flatten;
    public int spatial;
    public int pad;
    public int sqrt;
    public int flip;
    public int index;
    public int indexSJX;//layer's index in the network
    public boolean binary;
    public boolean xnor;
    public int steps;
    public int hidden;
    public int truth;
    public float smooth;
    public float dot;
    public float angle;
    public float jitter;
    public float saturation;
    public float exposure;
    public float shift;
    public float ratio;
    public float learning_rate_scale;
    public boolean softmax;
    public int classes;
    public int coords;
    public boolean background;
    public boolean rescore;
    public int objectness;
    public int does_cost;
    public int joint;
    public int noadjust;
    public int reorg;
    public int log;
    public int tanh;

    public float alpha;
    public float beta;
    public float kappa;

    public float coord_scale;
    public float object_scale;
    public float noobject_scale;
    public float mask_scale;
    public float class_scale;
    public boolean bias_match;
    public boolean random;
    public float thresh;
    public int classfix;
    public int absolute;

    public int onlyforward;
    public boolean stopbackward;
    public boolean dontload;
    public boolean dontloadscales;

    public float temperature;
    public float probability;
    public float scale;

    public char[]  cweights;
    public int[]   indexes;
    public int   input_layers;
    public int   input_sizes;
    public int[]   map;
    public float rand;
    public float[] cost;
    public float state;
    public float prev_state;
    public float forgot_state;
    public float forgot_delta;
    public float state_delta;
    public float combine_cpu;
    public float combine_delta_cpu;

    public float concat;
    public float concat_delta;

    public float[] binary_weights;

    public float[] biases;
    public float[] bias_updates;

    public float[] scales;
    public float[] scale_updates;

    public float[] weights;
    public float[] weight_updates;

    public float[] delta;
    public float[] output;
    public float squared;
    public float norms;

    public float spatial_mean;
    public float[] mean;
    public float[] variance;

    public float[] mean_delta;
    public float[] variance_delta;

    public float[] rolling_mean;
    public float[] rolling_variance;

    public float[] x;
    public float[] x_norm;

    public float[] m;
    public float[] v;
     
    public float[] bias_m;
    public float[] bias_v;
    public float[] scale_m;
    public float[] scale_v;


    public float z_cpu;
    public float r_cpu;
    public float h_cpu;
    public float prev_state_cpu;

    public float temp_cpu;
    public float temp2_cpu;
    public float temp3_cpu;

    public float dh_cpu;
    public float hh_cpu;
    public float prev_cell_cpu;
    public float cell_cpu;
    public float f_cpu;
    public float i_cpu;
    public float g_cpu;
    public float o_cpu;
    public float c_cpu;
    public float dc_cpu; 

    public float[] binary_input;

    public layer input_layer;
    public layer self_layer;
    public layer output_layer;

    public layer reset_layer;
    public layer update_layer;
    public layer state_layer;

    public layer input_gate_layer;
    public layer state_gate_layer;
    public layer input_save_layer;
    public layer state_save_layer;
    public layer input_state_layer;
    public layer state_state_layer;

    public layer input_z_layer;
    public layer state_z_layer;

    public layer input_r_layer;
    public layer state_r_layer;

    public layer input_h_layer;
    public layer state_h_layer;
	
    public layer wz;
    public layer uz;
    public layer wr;
    public layer ur;
    public layer wh;
    public layer uh;
    public layer uo;
    public layer wo;
    public layer uf;
    public layer wf;
    public layer ui;
    public layer wi;
    public layer ug;
    public layer wg;

    public tree softmax_tree;

//     size_t workspace_size;
    public int workspace_size;

//#ifdef GPU
//    int indexes_gpu;
//
//    float z_gpu;
//    float r_gpu;
//    float h_gpu;
//
//    float temp_gpu;
//    float temp2_gpu;
//    float temp3_gpu;
//
//    float dh_gpu;
//    float hh_gpu;
//    float prev_cell_gpu;
//    float cell_gpu;
//    float f_gpu;
//    float i_gpu;
//    float g_gpu;
//    float o_gpu;
//    float c_gpu;
//    float dc_gpu; 
//
//    float m_gpu;
//    float v_gpu;
//    float bias_m_gpu;
//    float scale_m_gpu;
//    float bias_v_gpu;
//    float scale_v_gpu;
//
//    float combine_gpu;
//    float combine_delta_gpu;
//
//    float prev_state_gpu;
//    float forgot_state_gpu;
//    float forgot_delta_gpu;
//    float state_gpu;
//    float state_delta_gpu;
//    float gate_gpu;
//    float gate_delta_gpu;
//    float save_gpu;
//    float save_delta_gpu;
//    float concat_gpu;
//    float concat_delta_gpu;
//
//    float binary_input_gpu;
//    float binary_weights_gpu;
//
//    float mean_gpu;
//    float variance_gpu;
//
//    float rolling_mean_gpu;
//    float rolling_variance_gpu;
//
//    float variance_delta_gpu;
//    float mean_delta_gpu;
//
//    float x_gpu;
//    float x_norm_gpu;
//    float weights_gpu;
//    float weight_updates_gpu;
//    float weight_change_gpu;
//
//    float biases_gpu;
//    float bias_updates_gpu;
//    float bias_change_gpu;
//
//    float scales_gpu;
//    float scale_updates_gpu;
//    float scale_change_gpu;
//
//    float output_gpu;
//    float delta_gpu;
//    float rand_gpu;
//    float squared_gpu;
//    float norms_gpu;
//#ifdef CUDNN
//    cudnnTensorDescriptor_t srcTensorDesc, dstTensorDesc;
//    cudnnTensorDescriptor_t dsrcTensorDesc, ddstTensorDesc;
//    cudnnTensorDescriptor_t normTensorDesc;
//    cudnnFilterDescriptor_t weightDesc;
//    cudnnFilterDescriptor_t dweightDesc;
//    cudnnConvolutionDescriptor_t convDesc;
//    cudnnConvolutionFwdAlgo_t fw_algo;
//    cudnnConvolutionBwdDataAlgo_t bd_algo;
//    cudnnConvolutionBwdFilterAlgo_t bf_algo;
//#endif
//#endif
	    
}
