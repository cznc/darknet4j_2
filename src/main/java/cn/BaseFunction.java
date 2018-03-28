package cn;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class BaseFunction{
	public static int gpu_index=0;
	public final static int SECRET_NUM = -1234;
	public final static float TWO_PI = 6.2831853071795864769252866f;
	public static final float FLT_MAX=Float.MAX_VALUE;
//	public static final float FLT_MIM=Float.MIN_VALUE;
	public static final int CLOCKS_PER_SEC=1000;//not sure
	protected long clock() {
		return System.currentTimeMillis();//not sure
	}
	protected float sec(long clock) {
		return clock/CLOCKS_PER_SEC;
	}
	static protected double what_time_is_it_now() {//minor=2
		return System.currentTimeMillis()/1000;
	}
	public static int atoi(String str) {
		return Integer.parseInt(str);
	}
	public static boolean atoboolean(String str) {
		return atoi(str)!=0;
	}
	public static float atof(String str) {
		return Float.parseFloat(str);
	}
	public static long time(int seed) {
		return System.currentTimeMillis()+seed;
	}
	public static final int RAND_MAX=32767;
	static Random rand = new Random(time(0));
	public static void srand(long seed) {
		rand .setSeed(seed);
	}
	
	/**
	 * Random.nextInt(n) => [0,n) .<br/>
	 * Random.nextInt() => negtive , zero, positive.
	 * @return [1,RAND_MAX]
	 */
	public static int rand() {
//		int i = rand.nextInt()%RAND_MAX; 
		int i = rand.nextInt(RAND_MAX)+1; 
		return i;
	}
	//////////Math
	protected static float pow(double a, float b) {
		return (float)Math.pow(a, b);
	}
	protected static float pow(float a, float b) {
		return (float)Math.pow(a, b);
	}
	protected static float sqrt(double a) {
		return (float)Math.sqrt(a);
	}
	protected static float fabs(float diff) {
		return abs(diff);
	}
	protected static float abs(float diff) {
		return Math.abs(diff);
	}
	protected static float exp(float diff) {
		return (float)Math.exp(diff);
	}
	protected static int floor(float diff) {
		return (int)Math.floor(diff);
	}
	protected static int floorf(float diff) {
		return floor(diff);
	}
	protected static float sin(double diff) {
		return (float)Math.sin(diff);
	}
	protected static float cos(double diff) {
		return (float)Math.cos(diff);
	}
	static protected int ceil(float diff) {
		return (int)Math.ceil(diff);
	}
	static protected float log(double val) {
		return (float)Math.log(val);
	}
	
	
	public final static int stdin=0;
	public final static int stdout=1;
	public final static int stderr=2;
	protected static void fprintf(int out, String str) {
		if(stderr==out) {
			System.err.print(str);
		}else {
			System.out.print(str);
		}
	}
	protected static void fprintf(int out, String format,Object... args) {
		if(stderr==out) {
			System.err.print(String.format(format, args));
		}else {
			System.out.print(String.format(format, args));
		}
	}
	protected static void printf(String str) {
		System.out.print(str);
	}
	protected static void printf(String format,Object... args) {
		System.out.print(String.format(format, args));
	}
	protected static void memcpy(float[] dest, float[] src, int length) {
		System.arraycopy(src, 0, dest, 0, length);
	}
	protected static void memcpy(float[] dest, int dest_offset, float[] src, int src_offset, int length) {
		System.arraycopy(src, src_offset, dest, dest_offset, length);
	}
	protected static void memset(float[] dest,int val, int length) {
		Arrays.fill(dest, 0, length, val);
	}
	//////////////
	protected static int strcmp(String str, String str2) {
		if( str.length()<str2.length())return -str2.charAt(str.length());
		if( str.length()>str2.length())return str.charAt(str2.length());
		for (int i = 0; i < str.length(); i++) {
			if(str.charAt(i)!=str2.charAt(i))return str.charAt(i)-str2.charAt(i);
		}
		return 0;
	}
	protected static int strlen(String str) {
		if(str==null)return 0;
		return str.length();
	}
	protected static int strlen(char[] str) {
		if(str==null)return 0;
		return str.length;
	}
	protected static boolean strstr(String path, String labels) {
		return path.indexOf(labels)>-1;
	}
	protected static int strchr(String str, char c) {
		return str.indexOf(c);
	}
	///////
//	calloc(l.batch*l.outputs, sizeof(float));
//	protected int sizeof() {
//		return 0;
//	}
	protected static char[] callocChar(int n,int size) {
//		if(size!=1) {
//			throw new Throwable("callocChar size="+size);
//		}
//		System.err.println(String.format("n=%d,size=%d,total=%d ", n, size, n*size));
		return new char[n*size];
	}
		protected static float[] calloc(int n,int size) {
//		System.err.println(String.format("n=%d,size=%d,total=%d ", n, size, n*size));
		return new float[n*size];//net.workspace = calloc(1, workspace_size);//workspace_size=l.c*l.w*l.h*l.size*l.size 
	}
//	protected static <T>T[] calloc(int n,T clzz) {
//		T buffers=new T[n];
//		for (int j = 0; j < buffers.length; j++) {
//			buffers[i]=new T(i);/////////by sjx
//		}
//		return buffers;
//	}
	protected static int[] callocInt(int n,int size) {
//		System.err.println(String.format("n=%d,size=%d,total=%d ", n, size, n*size));
		return new int[n*size];
	}
//	<T> T a(Class<T> c) {
//		return new T();
//	}
//	protected static boolean[] calloc(int n,int size, Class<Boolean> c) {//Erasure of method calloc(int, int, Class<Boolean>) is the same as another method in type BaseFunction
	protected static boolean[] callocBoolean(int n,int size) {
//		System.err.println(String.format("n=%d,size=%d,total=%d ", n, size, n*size));
		return new boolean[n*size];
	}	
	protected static float[] malloc(int n) {
//		System.err.println(String.format("n=%d ", n));
		return new float[n];
	}
	/**
	 * @param n
	 * @param size
	 * @param c not used, but for override
	 * @return
	 */
	protected static float[][] calloc(int n,int size, Class<float[]> c ) {
		return callocFloat2DArray(n, size);
	}
	protected static float[][] callocFloat2DArray(int n, int size) {
//		System.err.println(String.format("n=%d,size=%d,total=%d ", n, size, n*size));
		return new float[n*size][];
	}
//  realloc(l.output, l.batch*l.outputs*sizeof(float));
	public static float[] realloc(float[] old, int len){
//		return reallocFloat(old, len);
//	}
//  public static float[] reallocFloat(float[] old, int len){
//	  System.err.println(String.format("from=%d,to=%d", old.length, len));
  	if(old==null)return new float[len];
  	if(old.length==len)return old;

  	float[] new_array=new float[len];
  	System.arraycopy(old, 0, new_array, 0, Math.min(old.length, len));
//  	if(old.length<len) {
//  		memset(new_array, 0, length);
//  	}
  	return new_array;
  }
  public static float[][] reallocFloat2DArray(float[][] old, int len){
//	  System.err.println(String.format("from=%d,to=%d", old.length, len));
	  	if(old==null)return new float[len][];
	  	if(old.length==len)return old;

	  	float[][] new_array=new float[len][];
	  	System.arraycopy(old, 0, new_array, 0, Math.min(old.length, len));
	  	return new_array;
	  }
  public static int[] reallocInt(int[] old, int len){
//	  System.err.println(String.format("from=%d,to=%d", old.length, len));
  	if(old==null)return new int[len];
  	if(old.length==len)return old;
  	
  	int[] new_array=new int[len];
  	System.arraycopy(old, 0, new_array, 0, Math.min(old.length, len));
  	return new_array;
  }
//	protected static void error(String str)throws Exception {
//		utils.error(str);
//	}
//	protected static void file_error(String str)throws Exception {
//		utils.file_error(str);
//	}
	/////
//	protected static void top_k(float[] a, int n, int k, int[] index) {
//		utils.top_k(a, n, k, index);
//	}
//	static float rand_uniform(float min, float max) {
//    	return utils.rand_uniform(min, max);
//    }
//	public static float rand_normal()
//	{
//		return rand.nextFloat();
//	}
///////////
  protected static float[] splitFloat(String str) {
		String[] tmp=str.split(",");
		float[] ret=new float[tmp.length];
		for (int i = 0; i < ret.length; i++) {
			try {
				ret[i]=Float.parseFloat(tmp[i].trim());
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				ret[i]=0f;
			}
		}
		return ret;
	}
	protected static int[] splitInt(String str) {
		String[] tmp=str.split(",");
		int[] ret=new int[tmp.length];
		for (int i = 0; i < ret.length; i++) {
			try {
				ret[i]=Integer.parseInt(tmp[i].trim());
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				ret[i]=0;
			}
		}
		return ret;
	}
	static protected void free(Object obj) {
		
	}
	static protected void exit(int status) {
		System.exit(status);
	}
	protected static void strcat(StringBuffer buff, String str) {
		buff.append(str);
	}
}
