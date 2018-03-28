package cn.drknt.lib;

import cn.BaseFunction;

public class gemm extends BaseFunction
{

void gemm_bin(int M, int N, int K, float ALPHA, 
        char  []A, int A_offset, int lda, 
        float[] B, int B_offset, int ldb,
        float[] C, int C_offset, int ldc)
{
    int i,j,k;
    for(i = 0; i < M; ++i){
        for(k = 0; k < K; ++k){
            char A_PART = A[A_offset+i*lda+k];
            if(A_PART!=0){
                for(j = 0; j < N; ++j){
                    C[C_offset+i*ldc+j] += B[B_offset+k*ldb+j];
                }
            } else {
                for(j = 0; j < N; ++j){
                    C[C_offset+i*ldc+j] -= B[B_offset+k*ldb+j];
                }
            }
        }
    }
}

float []random_matrix(int rows, int cols)
{
    int i;
    float []m = calloc(rows*cols, 1/*sizeof(float)*/);
    for(i = 0; i < rows*cols; ++i){
        m[i] = (float)rand()/RAND_MAX;
    }
    return m;
}

void time_random_matrix(boolean TA, boolean TB, int m, int k, int n)
{
    float []a;
    if(!TA) a = random_matrix(m,k);
    else a = random_matrix(k,m);
    int lda = (!TA)?k:m;
    float []b;
    if(!TB) b = random_matrix(k,n);
    else b = random_matrix(n,k);
    int ldb = (!TB)?n:k;

    float []c = random_matrix(m,n);
    int i;
    /*clock_t*/long start = clock(), end;
    for(i = 0; i<10; ++i){
        gemm_cpu(TA,TB,m,n,k,1,a,0,lda,b,0,ldb,1,c,0,n);
    }
    end = clock();
    printf("Matrix Multiplication %dx%d * %dx%d, TA=%d, TB=%d: %f ms\n",m,k,k,n, TA, TB, (float)(end-start)/CLOCKS_PER_SEC);
    free(a);
    free(b);
    free(c);
}


//public static void gemm(boolean TA, boolean TB, int M, int N, int K, float ALPHA, 
//        float []A, int A_offset, int lda, 
//        float []B, int B_offset, int ldb,
//        float BETA,
//        float []C, int C_offset, int ldc)
//{
//    gemm_cpu( TA,  TB,  M, N, K, ALPHA,A, A_offset,lda, B, B_offset, ldb,BETA,C, C_offset,ldc);
//}

static void gemm_nn(int M, int N, int K, float ALPHA, 
        float[] A, int A_offset, int lda, 
        float[] B, int B_offset, int ldb,
        float[] C, int C_offset, int ldc)
{
    int i,j,k;
//    #pragma omp parallel for
    for(i = 0; i < M; ++i){
        for(k = 0; k < K; ++k){
            /*register*/ float A_PART = ALPHA*A[A_offset+i*lda+k];
            for(j = 0; j < N; ++j){
                C[C_offset+i*ldc+j] += A_PART*B[B_offset+k*ldb+j];
            }
        }
    }
}

static void gemm_nt(int M, int N, int K, float ALPHA, 
        float[] A, int A_offset, int lda, 
        float[] B, int B_offset, int ldb,
        float[] C, int C_offset, int ldc)
{
    int i,j,k;
//    #pragma omp parallel for
    for(i = 0; i < M; ++i){
        for(j = 0; j < N; ++j){
        	/*register*/ float sum = 0;
            for(k = 0; k < K; ++k){
                sum += ALPHA*A[A_offset+i*lda+k]*B[B_offset+j*ldb + k];
            }
            C[C_offset+i*ldc+j] += sum;
        }
    }
}

static void gemm_tn(int M, int N, int K, float ALPHA, 
        float[] A, int A_offset, int lda, 
        float[] B, int B_offset, int ldb,
        float[] C, int C_offset, int ldc)
{
    int i,j,k;
//    #pragma omp parallel for
    for(i = 0; i < M; ++i){
        for(k = 0; k < K; ++k){
        	/*register*/ float A_PART = ALPHA*A[A_offset+k*lda+i];
            for(j = 0; j < N; ++j){
                C[C_offset+i*ldc+j] += A_PART*B[B_offset+k*ldb+j];
            }
        }
    }
}

static void gemm_tt(int M, int N, int K, float ALPHA, 
        float[] A, int A_offset, int lda, 
        float[] B, int B_offset, int ldb,
        float[] C, int C_offset, int ldc)
{
    int i,j,k;
//    #pragma omp parallel for
    for(i = 0; i < M; ++i){
        for(j = 0; j < N; ++j){
        	/*register*/ float sum = 0;
            for(k = 0; k < K; ++k){
                sum += ALPHA*A[A_offset+i+k*lda]*B[B_offset+k+j*ldb];
            }
            C[C_offset+i*ldc+j] += sum;
        }
    }
}

public static void gemm_cpu(boolean TA, boolean TB, int M, int N, int K, float ALPHA, 
        float[] A, int A_offset, int lda, 
        float[] B, int B_offset, int ldb,
        float BETA,
        float[] C, int C_offset, int ldc)
{
    //printf("cpu: %d %d %d %d %d %f %d %d %f %d\n",TA, TB, M, N, K, ALPHA, lda, ldb, BETA, ldc);
    int i, j;
    for(i = 0; i < M; ++i){
        for(j = 0; j < N; ++j){
            C[i*ldc + j] *= BETA;
        }
    }
    if(!TA && !TB)
        gemm_nn(M, N, K, ALPHA,A, A_offset,lda, B, B_offset, ldb,C, C_offset,ldc);
    else if(TA && !TB)
        gemm_tn(M, N, K, ALPHA,A, A_offset,lda, B, B_offset, ldb,C, C_offset,ldc);
    else if(!TA && TB)
        gemm_nt(M, N, K, ALPHA,A, A_offset,lda, B, B_offset, ldb,C, C_offset,ldc);
    else
        gemm_tt(M, N, K, ALPHA,A, A_offset,lda, B, B_offset, ldb,C, C_offset,ldc);
}

//#ifdef GPU
//
//#include <math.h>
//
//void gemm_gpu(int TA, int TB, int M, int N, int K, float ALPHA, 
//        float *A_gpu, int lda, 
//        float *B_gpu, int ldb,
//        float BETA,
//        float *C_gpu, int ldc)
//{
//    cublasHandle_t handle = blas_handle();
//    cudaError_t status = cublasSgemm(handle, (TB ? CUBLAS_OP_T : CUBLAS_OP_N), 
//            (TA ? CUBLAS_OP_T : CUBLAS_OP_N), N, M, K, &ALPHA, B_gpu, ldb, A_gpu, lda, &BETA, C_gpu, ldc);
//    check_error(status);
//}
//
//#include <stdio.h>
//#include <stdlib.h>
//#include <string.h>
//#include <time.h>
//
//void time_gpu_random_matrix(int TA, int TB, int m, int k, int n)
//{
//    float *a;
//    if(!TA) a = random_matrix(m,k);
//    else a = random_matrix(k,m);
//    int lda = (!TA)?k:m;
//    float *b;
//    if(!TB) b = random_matrix(k,n);
//    else b = random_matrix(n,k);
//    int ldb = (!TB)?n:k;
//
//    float *c = random_matrix(m,n);
//    int i;
//    clock_t start = clock(), end;
//    for(i = 0; i<32; ++i){
//        gemm_gpu(TA,TB,m,n,k,1,a,lda,b,ldb,1,c,n);
//    }
//    end = clock();
//    printf("Matrix Multiplication %dx%d * %dx%d, TA=%d, TB=%d: %lf s\n",m,k,k,n, TA, TB, (float)(end-start)/CLOCKS_PER_SEC);
//    free(a);
//    free(b);
//    free(c);
//}
//
//void time_gpu(int TA, int TB, int m, int k, int n)
//{
//    int iter = 10;
//    float *a = random_matrix(m,k);
//    float *b = random_matrix(k,n);
//
//    int lda = (!TA)?k:m;
//    int ldb = (!TB)?n:k;
//
//    float *c = random_matrix(m,n);
//
//    float *a_cl = cuda_make_array(a, m*k);
//    float *b_cl = cuda_make_array(b, k*n);
//    float *c_cl = cuda_make_array(c, m*n);
//
//    int i;
//    clock_t start = clock(), end;
//    for(i = 0; i<iter; ++i){
//        gemm_gpu(TA,TB,m,n,k,1,a_cl,lda,b_cl,ldb,1,c_cl,n);
//        cudaThreadSynchronize();
//    }
//    double flop = ((double)m)*n*(2.*k + 2.)*iter;
//    double gflop = flop/pow(10., 9);
//    end = clock();
//    double seconds = sec(end-start);
//    printf("Matrix Multiplication %dx%d * %dx%d, TA=%d, TB=%d: %lf s, %lf GFLOPS\n",m,k,k,n, TA, TB, seconds, gflop/seconds);
//    cuda_free(a_cl);
//    cuda_free(b_cl);
//    cuda_free(c_cl);
//    free(a);
//    free(b);
//    free(c);
//}
//
//
//void test_gpu_accuracy(int TA, int TB, int m, int k, int n)
//{
//    srand(0);
//    float *a;
//    if(!TA) a = random_matrix(m,k);
//    else a = random_matrix(k,m);
//    int lda = (!TA)?k:m;
//    float *b;
//    if(!TB) b = random_matrix(k,n);
//    else b = random_matrix(n,k);
//    int ldb = (!TB)?n:k;
//
//    float *c = random_matrix(m,n);
//    float *c_gpu = random_matrix(m,n);
//    memset(c, 0, m*n*sizeof(float));
//    memset(c_gpu, 0, m*n*sizeof(float));
//    int i;
//    //pm(m,k,b);
//    gemm_gpu(TA,TB,m,n,k,1,a,lda,b,ldb,1,c_gpu,n);
//    //printf("GPU\n");
//    //pm(m, n, c_gpu);
//
//    gemm_cpu(TA,TB,m,n,k,1,a,lda,b,ldb,1,c,n);
//    //printf("\n\nCPU\n");
//    //pm(m, n, c);
//    double sse = 0;
//    for(i = 0; i < m*n; ++i) {
//        //printf("%f %f\n", c[i], c_gpu[i]);
//        sse += pow(c[i]-c_gpu[i], 2);
//    }
//    printf("Matrix Multiplication %dx%d * %dx%d, TA=%d, TB=%d: %g SSE\n",m,k,k,n, TA, TB, sse/(m*n));
//    free(a);
//    free(b);
//    free(c);
//    free(c_gpu);
//}
//
//int test_gpu_blas()
//{
//    /*
//       test_gpu_accuracy(0,0,10,576,75); 
//
//       test_gpu_accuracy(0,0,17,10,10); 
//       test_gpu_accuracy(1,0,17,10,10); 
//       test_gpu_accuracy(0,1,17,10,10); 
//       test_gpu_accuracy(1,1,17,10,10); 
//
//       test_gpu_accuracy(0,0,1000,10,100); 
//       test_gpu_accuracy(1,0,1000,10,100); 
//       test_gpu_accuracy(0,1,1000,10,100); 
//       test_gpu_accuracy(1,1,1000,10,100); 
//
//       test_gpu_accuracy(0,0,10,10,10); 
//
//       time_gpu(0,0,64,2916,363); 
//       time_gpu(0,0,64,2916,363); 
//       time_gpu(0,0,64,2916,363); 
//       time_gpu(0,0,192,729,1600); 
//       time_gpu(0,0,384,196,1728); 
//       time_gpu(0,0,256,196,3456); 
//       time_gpu(0,0,256,196,2304); 
//       time_gpu(0,0,128,4096,12544); 
//       time_gpu(0,0,128,4096,4096); 
//     */
//    time_gpu(0,0,64,75,12544); 
//    time_gpu(0,0,64,75,12544); 
//    time_gpu(0,0,64,75,12544); 
//    time_gpu(0,0,64,576,12544); 
//    time_gpu(0,0,256,2304,784); 
//    time_gpu(1,1,2304,256,784); 
//    time_gpu(0,0,512,4608,196); 
//    time_gpu(1,1,4608,512,196); 
//
//    return 0;
//}
//#endif

};