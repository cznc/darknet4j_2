package cn.drknt.lib;

import cn.box;
import cn.matrix;
import cn.wrapper.data_wrapper;

public class data_struct extends data_wrapper{
	public int w, h;
    public matrix X=new matrix();
    public matrix y=new matrix();
    public boolean shallow;
    public int []num_boxes;
    public box []boxes;
}
