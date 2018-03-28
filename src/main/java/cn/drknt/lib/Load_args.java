package cn.drknt.lib;

import cn.data;
import cn.image;
import cn.tree;

public class Load_args {
    public int threads;
    public String[] paths;//ptr//ptr
    public String path;//ptr
    public int n;
    public int m;
    public String[] labels;//ptr//ptr
    public int h;
    public int w;
    public int out_w;
    public int out_h;
    public int nh;
    public int nw;
    public int num_boxes;
    public int min, max, size;
    public int classes;
    public int background;
    public int scale;
    public int center;
    public int coords;
    public float jitter;
    public float angle;
    public float aspect;
    public float saturation;
    public float exposure;
    public float hue;
    public data d;//ptr
    public image im;//ptr
    public image resized;//ptr
    public Data_type type;
    public tree hierarchy;//ptr
	
}
