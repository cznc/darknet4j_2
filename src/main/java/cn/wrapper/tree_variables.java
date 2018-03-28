package cn.wrapper;

import java.io.IOException;

import cn.BaseFunction;
import cn.data;

public class tree_variables extends BaseFunction
{
	public boolean[] leaf;
	public int n;
	public int[] parent;
	public int[] child;
	public int[] group;
	public String[] name;

	public int groups;
	public int[] group_size;
	public int[] group_offset;

//	public static String[] get_paths2(String filename)throws IOException,Exception{
//		return data.get_paths2(filename);
//	}
}
