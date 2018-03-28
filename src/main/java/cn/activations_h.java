package cn;


public class activations_h extends BaseFunction{

static final float stair_activate(float x)
{
    int n = floor(x);
    if (n%2 == 0) return floor(x/2.f);
    else return (x - n) + floor(x/2.f);
}
static final float hardtan_activate(float x)
{
    if (x < -1) return -1;
    if (x > 1) return 1;
    return x;
}
static final float linear_activate(float x){return x;}
static final float logistic_activate(float x){return 1.f/(1.f + exp(-x));}
static final float loggy_activate(float x){return 2.f/(1.f + exp(-x)) - 1;}
static final float relu_activate(float x){return x*(x>0?1:0);}
static final float elu_activate(float x){return (x >= 0?1:0)*x + (x < 0?1:0)*(exp(x)-1);}
static final float relie_activate(float x){return (x>0) ? x : .01f*x;}
static final float ramp_activate(float x){return x*(x>0?1:0)+.1f*x;}
static final float leaky_activate(float x){return (x>0) ? x : .1f*x;}
static final float tanh_activate(float x){return (exp(2*x)-1)/(exp(2*x)+1);}
static final float plse_activate(float x)
{
    if(x < -4) return .01f * (x + 4);
    if(x > 4)  return .01f * (x - 4) + 1;
    return .125f*x + .5f;
}

static final float lhtan_activate(float x)
{
    if(x < 0) return .001f*x;
    if(x > 1) return .001f*(x-1) + 1;
    return x;
}
static final float lhtan_gradient(float x)
{
    if(x > 0 && x < 1) return 1;
    return .001f;
}

static final float hardtan_gradient(float x)
{
    if (x > -1 && x < 1) return 1;
    return 0;
}
static final float linear_gradient(float x){return 1;}
static final float logistic_gradient(float x){return (1-x)*x;}
static final float loggy_gradient(float x)
{
    float y = (x+1.f)/2.f;
    return 2*(1-y)*y;
}
static final float stair_gradient(float x)
{
    if (floor(x) == x) return 0;
    return 1;
}
static final float relu_gradient(float x){return (x>0?1:0);}
static final float elu_gradient(float x){return (x >= 0?1:0) + (x < 0?1:0)*(x + 1);}
static final float relie_gradient(float x){return (x>0) ? 1 : .01f;}
static final float ramp_gradient(float x){return (x>0?1:0)+.1f;}
static final float leaky_gradient(float x){return (x>0) ? 1 : .1f;}
static final float tanh_gradient(float x){return 1-x*x;}
static final float plse_gradient(float x){return (x < 0 || x > 1) ? .01f : .125f;}

}
