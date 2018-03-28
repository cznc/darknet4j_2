package cn;

import cn.drknt.lib.LAYER_TYPE;
import cn.drknt.lib.Update_args;
import cn.drknt.lib.blas;

public class softmax_layer extends layer
{
	@Override
	public void forward(layer l, network net) {
		// TODO Auto-generated method stub
		forward_softmax_layer((softmax_layer)l, net);
	}
	@Override
	public void backward(layer l, network net) {
		// TODO Auto-generated method stub
		backward_softmax_layer((softmax_layer)l, net);
	}
	@Override
	public void update(layer l, Update_args update_args) {
		// TODO Auto-generated method stub
		System.out.println("do nonthing in update() in "+this.getClass().getSimpleName());
	}
	

softmax_layer make_softmax_layer(int batch, int inputs, int groups)
{
    assert(inputs%groups == 0);
    fprintf(stderr, "softmax                                        %4d\n",  inputs);
    softmax_layer l = new softmax_layer();//{0};
    l.type = LAYER_TYPE.SOFTMAX;
    l.batch = batch;
    l.groups = groups;
    l.inputs = inputs;
    l.outputs = inputs;
    l.output = calloc(inputs*batch, 1/*sizeof(float)*/);
    l.delta = calloc(inputs*batch, 1/*sizeof(float)*/);

//    l.forward = forward_softmax_layer;
//    l.backward = backward_softmax_layer;
//    #ifdef GPU
//    l.forward_gpu = forward_softmax_layer_gpu;
//    l.backward_gpu = backward_softmax_layer_gpu;
//
//    l.output_gpu = cuda_make_array(l.output, inputs*batch); 
//    l.delta_gpu = cuda_make_array(l.delta, inputs*batch); 
//    #endif
    return l;
}

void forward_softmax_layer(final softmax_layer l, network net)
{
    if(l.softmax_tree!=null){
        int i;
        int count = 0;
        for (i = 0; i < l.softmax_tree.groups; ++i) {
            int group_size = l.softmax_tree.group_size[i];
            blas.softmax_cpu(net.input, count, group_size, l.batch, l.inputs, 1, 0, 1, l.temperature, l.output, count);
            count += group_size;
        }
    } else {
    	blas.softmax_cpu(net.input, l.inputs/l.groups, l.batch, l.inputs, l.groups, l.inputs/l.groups, 1, l.temperature, l.output);
    }
}

void backward_softmax_layer(final softmax_layer l, network net)
{
    blas.axpy_cpu(l.inputs*l.batch, 1, l.delta, 1, net.delta, 1);
}

//#ifdef GPU
//
//void pull_softmax_layer_output(final softmax_layer layer)
//{
//    cuda_pull_array(layer.output_gpu, layer.output, layer.inputs*layer.batch);
//}
//
//void forward_softmax_layer_gpu(final softmax_layer l, network net)
//{
//    if(l.softmax_tree){
//        int i;
//        int count = 0;
//        for (i = 0; i < l.softmax_tree.groups; ++i) {
//            int group_size = l.softmax_tree.group_size[i];
//            softmax_gpu(net.input_gpu + count, group_size, l.batch, l.inputs, 1, 0, 1, l.temperature, l.output_gpu + count);
//            count += group_size;
//        }
//    } else {
//        if(l.spatial){
//            softmax_gpu(net.input_gpu, l.c, l.batch*l.c, l.inputs/l.c, l.w*l.h, 1, l.w*l.h, 1, l.output_gpu);
//        }else{
//            softmax_gpu(net.input_gpu, l.inputs/l.groups, l.batch, l.inputs, l.groups, l.inputs/l.groups, 1, l.temperature, l.output_gpu);
//        }
//    }
//}
//
//void backward_softmax_layer_gpu(final softmax_layer layer, network net)
//{
//    axpy_gpu(layer.batch*layer.inputs, 1, layer.delta_gpu, 1, net.delta_gpu, 1);
//}
//
//#endif
}