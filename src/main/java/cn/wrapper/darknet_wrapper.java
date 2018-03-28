package cn.wrapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import cn.BaseFunction;
import cn.detector;
import cn.image;
import cn.network;
import cn.parser;
import cn.utils;
import cn.drknt.lib.blas;

public class darknet_wrapper extends BaseFunction {
	static {
		System.out.println("dll search path: "+System.getProperty("java.library.path"));
		System.loadLibrary("darknet_jni_wrapper");
	}
	public static network parse_network_cfg(String filename) throws IOException, Exception{
		return parser.parse_network_cfg(filename);
	}
	public static void load_weights(network net, String filename) throws IOException{
		parser.load_weights(net, filename);
	}
	public static void axpy_cpu(int N, float ALPHA, float[] X, int INCX, float[] Y, int INCY) {
		blas.axpy_cpu(N, ALPHA, X, INCX, Y, INCY);
	}
	public static void scal_cpu(int N, double ALPHA, float[] X, int INCX) {
		blas.scal_cpu(N, (float)ALPHA, X, INCX);
	}
	public static void save_weights(network net, String filename)throws IOException, Exception{
		parser.save_weights(net, filename);
	}
	public static void set_batch_network(network net, int b) {
		network.set_batch_network(net, b);
	}
	public static image make_image(int w, int h, int c) {
		return image.make_image(w, h, c);
	}
	public static float[] network_predict(network net, float[] input) {
		return network.network_predict(net, input);
	}
	public static int find_int_arg(int argc, String[] argv, String arg, int def) {
		return utils.find_int_arg(argc, argv, arg, def);
	}
	public static boolean find_arg(int argc, String[] argv, String arg) {
		return 0!=utils.find_arg(argc, argv, arg);
	}
	public static void run_detector(int argc, String[] argv) {
		try {
			detector.run_detector(argc, argv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected static String[] setParams(  ) {
		String[] args=new String[4];
		int i=0;
		args[i++]="detector";
		args[i++]="train";
		args[i++]="face.data";
		args[i++]="yolo-face.cfg";

		System.err.println("THIS IS DEMO : "+Arrays.toString(args));
		return args;
	}
	protected static String[] before_main(String[] args) {
		if(args.length<2) {
			args=setParams( );
		}else{
			System.err.println("PARAM FROM OUTSIDE : "+Arrays.toString(args));
		}
		System.out.println("pwd: "+new File("").getAbsolutePath());
		String[] argv=new String[args.length+1];
		System.arraycopy(args, 0, argv, 1, args.length);
		return argv;
	}
	
}
