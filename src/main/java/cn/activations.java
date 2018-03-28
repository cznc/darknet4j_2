package cn;

import cn.drknt.lib.ACTIVATION;

public class activations extends activations_h
{

String get_activation_string(ACTIVATION a)
{
    switch(a){
        case LOGISTIC:
            return "logistic";
        case LOGGY:
            return "loggy";
        case RELU:
            return "relu";
        case ELU:
            return "elu";
        case RELIE:
            return "relie";
        case RAMP:
            return "ramp";
        case LINEAR:
            return "linear";
        case TANH:
            return "tanh";
        case PLSE:
            return "plse";
        case LEAKY:
            return "leaky";
        case STAIR:
            return "stair";
        case HARDTAN:
            return "hardtan";
        case LHTAN:
            return "lhtan";
        default:
            break;
    }
    return "relu";
}

public static ACTIVATION get_activation(String s)
{
    if (strcmp(s, "logistic")==0) return ACTIVATION.LOGISTIC;
    if (strcmp(s, "loggy")==0) return ACTIVATION.LOGGY;
    if (strcmp(s, "relu")==0) return ACTIVATION.RELU;
    if (strcmp(s, "elu")==0) return ACTIVATION.ELU;
    if (strcmp(s, "relie")==0) return ACTIVATION.RELIE;
    if (strcmp(s, "plse")==0) return ACTIVATION.PLSE;
    if (strcmp(s, "hardtan")==0) return ACTIVATION.HARDTAN;
    if (strcmp(s, "lhtan")==0) return ACTIVATION.LHTAN;
    if (strcmp(s, "linear")==0) return ACTIVATION.LINEAR;
    if (strcmp(s, "ramp")==0) return ACTIVATION.RAMP;
    if (strcmp(s, "leaky")==0) return ACTIVATION.LEAKY;
    if (strcmp(s, "tanh")==0) return ACTIVATION.TANH;
    if (strcmp(s, "stair")==0) return ACTIVATION.STAIR;
    fprintf(stderr, "Couldn't find activation function %s, going with ReLU\n", s);
    return ACTIVATION.RELU;
}

static float activate(float x, ACTIVATION a)
{
    switch(a){
        case LINEAR:
            return linear_activate(x);
        case LOGISTIC:
            return logistic_activate(x);
        case LOGGY:
            return loggy_activate(x);
        case RELU:
            return relu_activate(x);
        case ELU:
            return elu_activate(x);
        case RELIE:
            return relie_activate(x);
        case RAMP:
            return ramp_activate(x);
        case LEAKY:
            return leaky_activate(x);
        case TANH:
            return tanh_activate(x);
        case PLSE:
            return plse_activate(x);
        case STAIR:
            return stair_activate(x);
        case HARDTAN:
            return hardtan_activate(x);
        case LHTAN:
            return lhtan_activate(x);
    }
    return 0;
}

public static void activate_array(float []x, final int n, final ACTIVATION a) {
	activate_array(x, 0, n, a);
}
public static void activate_array(float []x, int x_offset, final int n, final ACTIVATION a)
{
    int i;
    for(i = 0; i < n; ++i){
        x[i] = activate(x[x_offset+i], a);
    }
}

static float gradient(float x, ACTIVATION a)
{
    switch(a){
        case LINEAR:
            return linear_gradient(x);
        case LOGISTIC:
            return logistic_gradient(x);
        case LOGGY:
            return loggy_gradient(x);
        case RELU:
            return relu_gradient(x);
        case ELU:
            return elu_gradient(x);
        case RELIE:
            return relie_gradient(x);
        case RAMP:
            return ramp_gradient(x);
        case LEAKY:
            return leaky_gradient(x);
        case TANH:
            return tanh_gradient(x);
        case PLSE:
            return plse_gradient(x);
        case STAIR:
            return stair_gradient(x);
        case HARDTAN:
            return hardtan_gradient(x);
        case LHTAN:
            return lhtan_gradient(x);
    }
    return 0;
}

public static void gradient_array(final float []x, final int n, final ACTIVATION a, float []delta)
{
    int i;
    for(i = 0; i < n; ++i){
        delta[i] *= gradient(x[i], a);
    }
} 

}