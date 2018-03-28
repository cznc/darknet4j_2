package cn.wrapper;

import cn.matrix;
import cn.utils;
import cn.drknt.lib.blas;

public class matrix_wrapper extends matrix_variables{
    
	protected static void top_k(float []a, int n, int k, int []index) {
		utils.top_k(a, n, k, index);
	}
	protected static void copy_cpu(int N, float[] X, int INCX, float[] Y, int INCY) {
		blas.copy_cpu(N, X, INCX, Y, INCY);
	}
	
}
