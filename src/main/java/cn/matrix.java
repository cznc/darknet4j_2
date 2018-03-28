
package cn;

import cn.wrapper.matrix_variables;
import cn.wrapper.matrix_wrapper;

public class matrix extends matrix_wrapper
{
	/**
     * sjx : moved from data.java
     * cols 相同, rows 累加, vals 扩容和并入m2的值；
     * */
    public matrix concat_matrix(matrix m2)
    {
        int i, count = 0;
        count=this.rows;
        this.cols=m2.cols;
//        this.vals = callocFloat2DArray(this.rows+ m2.rows, 1/*sizeof(float*)*/);//这不是扩容，这是把旧的给丢弃了；
        this.vals = reallocFloat2DArray(this.vals, this.rows+ m2.rows);//扩容
        for(i = 0; i < m2.rows; ++i){//并入
        	if(count>=this.vals.length || i>=m2.vals.length) {
        		System.err.println("有致命错误！！");
        	}
            this.vals[count++] = m2.vals[i];//这里不是修改值，仅仅是修改受限指针；
        }
        this.rows += m2.rows;//rows增长, cols不变
        return this;
    }
    
public static void free_matrix(matrix m)
{
    int i;
    for(i = 0; i < m.rows; ++i) free(m.vals[i]);
    free(m.vals);
}

float matrix_topk_accuracy(matrix truth, matrix guess, int k)
{
    int[] indexes = new int[k];//calloc(k, sizeof(int));
    int n = truth.cols;
    int i,j;
    int correct = 0;
    for(i = 0; i < truth.rows; ++i){
        top_k(guess.vals[i], n, k, indexes);
        for(j = 0; j < k; ++j){
            int _class = indexes[j];
            if(truth.vals[i][_class]!=0){
                ++correct;
                break;
            }
        }
    }
    free(indexes);
    return (float)correct/truth.rows;
}

void scale_matrix(matrix m, float scale)
{
    int i,j;
    for(i = 0; i < m.rows; ++i){
        for(j = 0; j < m.cols; ++j){
            m.vals[i][j] *= scale;
        }
    }
}

matrix resize_matrix(matrix m, int size)
{
    int i;
    if (m.rows == size) return m;
    if (m.rows < size) {
        m.vals = reallocFloat2DArray(m.vals, size*1/*sizeof(float*)*/);
        for (i = m.rows; i < size; ++i) {
            m.vals[i] = calloc(m.cols, 1/*sizeof(float)*/);
        }
    } else if (m.rows > size) {
        for (i = size; i < m.rows; ++i) {
            free(m.vals[i]);
        }
        m.vals = reallocFloat2DArray(m.vals, size*1/*sizeof(float*)*/);
    }
    m.rows = size;
    return m;
}

void matrix_add_matrix(matrix from, matrix to)
{
    assert(from.rows == to.rows && from.cols == to.cols);
    int i,j;
    for(i = 0; i < from.rows; ++i){
        for(j = 0; j < from.cols; ++j){
            to.vals[i][j] += from.vals[i][j];
        }
    }
}

matrix copy_matrix(matrix m)
{
    matrix c = new matrix();
    c.rows = m.rows;
    c.cols = m.cols;
    c.vals = callocFloat2DArray(c.rows, 1/*sizeof(float *)*/);
    int i;
    for(i = 0; i < c.rows; ++i){
        c.vals[i] = calloc(c.cols, 1/*sizeof(float)*/);
        copy_cpu(c.cols, m.vals[i], 1, c.vals[i], 1);
    }
    return c;
}

public static matrix make_matrix(int rows, int cols)
{
    int i;
    matrix m=new matrix();
    m.rows = rows;
    m.cols = cols;
    m.vals = callocFloat2DArray(m.rows, 1/*sizeof(float *)*/);
    for(i = 0; i < m.rows; ++i){
        m.vals[i] = calloc(m.cols, 1/*sizeof(float)*/);
    }
    return m;
}

matrix hold_out_matrix(matrix m, int n)
{
    int i;
    matrix h=new matrix();
    h.rows = n;
    h.cols = m.cols;
    h.vals = callocFloat2DArray(h.rows, 1/*sizeof(float *)*/);
    for(i = 0; i < n; ++i){
        int index = rand()%m.rows;
        h.vals[i] = m.vals[index];
        m.vals[index] = m.vals[--(m.rows)];
    }
    return h;
}

float []pop_column(matrix m, int c)
{
    float []col = calloc(m.rows, 1/*sizeof(float)*/);
    int i, j;
    for(i = 0; i < m.rows; ++i){
        col[i] = m.vals[i][c];
        for(j = c; j < m.cols-1; ++j){
            m.vals[i][j] = m.vals[i][j+1];
        }
    }
    --m.cols;
    return col;
}

//matrix csv_to_matrix(String filename)
//{
//    FILE *fp = fopen(filename, "r");
//    if(!fp) file_error(filename);
//
//    matrix m;
//    m.cols = -1;
//
//    String line;
//
//    int n = 0;
//    int size = 1024;
//    m.vals = callocFloat2DArray(size, 1/*sizeof(float*)*/);
//    while((line = fgetl(fp))){
//        if(m.cols == -1) m.cols = count_fields(line);
//        if(n == size){
//            size *= 2;
//            m.vals = reallocFloat2DArray(m.vals, size*1/*sizeof(float*)*/);
//        }
//        m.vals[n] = parse_fields(line, m.cols);
//        free(line);
//        ++n;
//    }
//    m.vals = reallocFloat2DArray(m.vals, n*1/*sizeof(float*)*/);
//    m.rows = n;
//    return m;
//}

void matrix_to_csv(matrix m)
{
    int i, j;

    for(i = 0; i < m.rows; ++i){
        for(j = 0; j < m.cols; ++j){
            if(j > 0) printf(",");
            printf("%.17g", m.vals[i][j]);
        }
        printf("\n");
    }
}

void print_matrix(matrix m)
{
    int i, j;
    printf("%d X %d Matrix:\n",m.rows, m.cols);
    printf(" __");
    for(j = 0; j < 16*m.cols-1; ++j) printf(" ");
    printf("__ \n");

    printf("|  ");
    for(j = 0; j < 16*m.cols-1; ++j) printf(" ");
    printf("  |\n");

    for(i = 0; i < m.rows; ++i){
        printf("|  ");
        for(j = 0; j < m.cols; ++j){
            printf("%15.7f ", m.vals[i][j]);
        }
        printf(" |\n");
    }
    printf("|__");
    for(j = 0; j < 16*m.cols-1; ++j) printf(" ");
    printf("__|\n");
}
}