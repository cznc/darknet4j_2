
package cn;

import java.io.IOException;

import cn.wrapper.FILE;

public class utils extends BaseFunction
{

/*
// old timing. is it better? who knows!!
double get_wall_time()
{
    struct timeval time;
    if (gettimeofday(&time,NULL)){
        return 0;
    }
    return (double)time.tv_sec + (double)time.tv_usec * .000001;
}
*/

//double what_time_is_it_now()
//{
//    struct timespec now;
//    clock_gettime(CLOCK_REALTIME, &now);
//    return now.tv_sec + now.tv_nsec*1e-9;
//}

//int[] read_intlist(char[] gpu_list, int[] ngpus, int d)
//{
//    int[] gpus = null;
//    if(gpu_list!=null){
//        int len = strlen(gpu_list);
//        ngpus[0] = 1;
//        int i;
//        for(i = 0; i < len; ++i){
//            if (gpu_list[i] == ',') ++ngpus[0];
//        }
//        gpus = callocInt(ngpus[1], 1/*sizeof(int)*/);
//        for(i = 0; i < ngpus[0]; ++i){
//            gpus[i] = atoi(gpu_list);
//            gpu_list = strchr(gpu_list, ',')+1;
//        }
//    } else {
//        gpus = calloc(1, 1/*sizeof(float)*/);
//        gpus[0] = d;
//        ngpus[0] = 1;
//    }
//    return gpus;
//}

public static int[] read_map(String filename)
{
//    int n = 0;
    int[] map = null;
//    String str;
//    FILE *file = fopen(filename, "r");
//    if(!file) file_error(filename);
//    while((str=fgetl(file))){
//        ++n;
//        map = realloc(map, n*1/*sizeof(int)*/);
//        map[n-1] = atoi(str);
//    }
    return map;
}

//void sorta_shuffle(Object[] arr, /*size_t*/int n, /*size_t*/int size, /*size_t*/int sections)
//{
//    /*size_t*/int i;
//    for(i = 0; i < sections; ++i){
//        /*size_t*/int start = n*i/sections;
//        /*size_t*/int end = n*(i+1)/sections;
//        /*size_t*/int num = end-start;
//        shuffle(arr+(start*size), num, size);
//    }
//}
//
//void shuffle(Object[] arr, /*size_t*/int n, /*size_t*/int size)
//{
//    /*size_t*/int i;
//    Object[] swp = calloc(1, size);
//    for(i = 0; i < n-1; ++i){
//        /*size_t*/int j = i + rand()/(RAND_MAX / (n-i)+1);
//        memcpy(swp,          arr+(j*size), size);
//        memcpy(arr+(j*size), arr+(i*size), size);
//        memcpy(arr+(i*size), swp,          size);
//    }
//}

int[] random_index_order(int min, int max)
{
    int[] inds = callocInt(max-min, 1/*sizeof(int)*/);
    int i;
    for(i = min; i < max; ++i){
        inds[i] = i;
    }
    for(i = min; i < max-1; ++i){
        int swap = inds[i];
        int index = i + rand()%(max-i);
        inds[i] = inds[index];
        inds[index] = swap;
    }
    return inds;
}

static void del_arg(int argc, String[] argv, int index)
{
    int i;
    for(i = index; i < argc-1; ++i) argv[i] = argv[i+1];
    argv[i] = null;
}

public static int find_arg(int argc, String[] argv, String arg)
{
    int i;
    for(i = 0; i < argc; ++i) {
        if(null==argv[i]) continue;
        if(0==strcmp(argv[i], arg)) {
            del_arg(argc, argv, i);
            return 1;
        }
    }
    return 0;
}

public static int find_int_arg(int argc, String[] argv, String arg, int def)
{
    int i;
    for(i = 0; i < argc-1; ++i){
        if(null==argv[i]) continue;
        if(0==strcmp(argv[i], arg)){
            def = atoi(argv[i+1]);
            del_arg(argc, argv, i);
            del_arg(argc, argv, i);
            break;
        }
    }
    return def;
}

public static float find_float_arg(int argc, String[] argv, String arg, float def)
{
    int i;
    for(i = 0; i < argc-1; ++i){
        if(null==argv[i]) continue;
        if(0==strcmp(argv[i], arg)){
            def = atof(argv[i+1]);
            del_arg(argc, argv, i);
            del_arg(argc, argv, i);
            break;
        }
    }
    return def;
}

public static String find_char_arg(int argc, String[] argv, String arg, String def)
{
    int i;
    for(i = 0; i < argc-1; ++i){
        if(null==argv[i]) continue;
        if(0==strcmp(argv[i], arg)){
            def = argv[i+1];
            del_arg(argc, argv, i);
            del_arg(argc, argv, i);
            break;
        }
    }
    return def;
}


//public static String basecfg(String cfgfile)
//{
//    String c = cfgfile;
//    char[] next;
//    while((next = strchr(c, '/')))
//    {
//        c = next+1;
//    }
//    c = copy_string(c);
//    next = strchr(c, '.');
//    if (next) *next = 0;
//    return c;
//}
public static String basecfg(String cfgfile)
{
	cfgfile=cfgfile.replace('\\', '/');
	int index1=cfgfile.lastIndexOf('/');
	int index2=cfgfile.lastIndexOf('.');
	if(index1==-1 && index2==-1) {
		return cfgfile;
	}
	if(index1==-1) {
		return cfgfile.substring(0,index2);
	}
	return cfgfile.substring(index1+1, index2);
}

//int alphanum_to_int(char c)
//{
//    return (c < 58) ? c - 48 : c-87;
//}
//char int_to_alphanum(int i)
//{
//    if (i == 36) return '.';
//    return (i < 10) ? i + 48 : i + 87;
//}

void pm(int M, int N, float[] A)
{
    int i,j;
    for(i =0 ; i < M; ++i){
        printf("%d ", i+1);
        for(j = 0; j < N; ++j){
            printf("%2.4f, ", A[i*N+j]);
        }
        printf("\n");
    }
    printf("\n");
}

//void find_replace(String str, String orig, String rep, String output)
//{
//    char buffer[4096] = {0};
//    String p;
//
//    sprintf(buffer, "%s", str);
//    if(!(p = strstr(buffer, orig))){  // Is 'orig' even in 'str'?
//        sprintf(output, "%s", str);
//        return;
//    }
//
//    *p = '\0';
//
//    sprintf(output, "%s%s%s", buffer, rep, p+strlen(orig));
//}
public static String find_replace(String father, String origstr, String newstr, String dest) {//dest is not used
	return father.replace(origstr, newstr);
}
//float sec(/*clock_t*/long clocks)
//{
//    return (float)clocks/CLOCKS_PER_SEC;
//}

public static void top_k(float []a, int n, int k, int[] index)
{
    int i,j;
    for(j = 0; j < k; ++j) index[j] = -1;
    for(i = 0; i < n; ++i){
        int curr = i;
        for(j = 0; j < k; ++j){
            if((index[j] < 0) || a[curr] > a[index[j]]){
                int swap = curr;
                curr = index[j];
                index[j] = swap;
            }
        }
    }
}

public static void error(final String s)
{
//    perror(s);
	System.err.println(s);
    assert(false);
    exit(-1);
}

//String read_file(String filename)
//{
//    FILE *fp = fopen(filename, "rb");
//    /*size_t*/int size;
//
//    fseek(fp, 0, SEEK_END); 
//    size = ftell(fp);
//    fseek(fp, 0, SEEK_SET); 
//
//    unsigned String text = calloc(size+1, 1/*sizeof(char)*/);
//    fread(text, 1, size, fp);
//    fclose(fp);
//    return text;
//}

void malloc_error()
{
    fprintf(stderr, "Malloc error\n");
    exit(-1);
}

public static void file_error(String s)
{
    fprintf(stderr, "Couldn't open file: %s\n", s);
    exit(0);
}

//list split_str(String s, char delim)
//{
//    /*size_t*/int i;
//    /*size_t*/int len = strlen(s);
//    list l = make_list();
//    list_insert(l, s);
//    for(i = 0; i < len; ++i){
//        if(s[i] == delim){
//            s[i] = '\0';
//            list_insert(l, &(s[i+1]));
//        }
//    }
//    return l;
//}

void strip(char[] s)
{
    /*size_t*/int i;
    /*size_t*/int len = strlen(s);
    /*size_t*/int offset = 0;
    for(i = 0; i < len; ++i){
        char c = s[i];
        if(c==' '||c=='\t'||c=='\n') ++offset;
        else s[i-offset] = c;
    }
    s[len-offset] = '\0';
}

void strip_char(char[] s, char bad)
{
    /*size_t*/int i;
    /*size_t*/int len = strlen(s);
    /*size_t*/int offset = 0;
    for(i = 0; i < len; ++i){
        char c = s[i];
        if(c==bad) ++offset;
        else s[i-offset] = c;
    }
    s[len-offset] = '\0';
}

void free_ptrs(Object[][] ptrs, int n)
{
    int i;
    for(i = 0; i < n; ++i) free(ptrs[i]);
    free(ptrs);
}

public static String fgetl(FILE fp) throws IOException {
	return fp.readLine();
}
//String fgetl(FILE *fp) {
//{
//    if(feof(fp)) return 0;
//    /*size_t*/int size = 512;
//    String line = malloc(size*1/*sizeof(char)*/);
//    if(!fgets(line, size, fp)){
//        free(line);
//        return 0;
//    }
//
//    /*size_t*/int curr = strlen(line);
//
//    while((line[curr-1] != '\n') && !feof(fp)){
//        if(curr == size-1){
//            size *= 2;
//            line = realloc(line, size*1/*sizeof(char)*/);
//            if(!line) {
//                printf("%ld\n", size);
//                malloc_error();
//            }
//        }
//        /*size_t*/int readsize = size-curr;
//        if(readsize > INT_MAX) readsize = INT_MAX-1;
//        fgets(&line[curr], readsize, fp);
//        curr = strlen(line);
//    }
//    if(line[curr-1] == '\n') line[curr-1] = '\0';
//
//    return line;
//}

//int read_int(int fd)
//{
//    int n = 0;
//    int next = read(fd, &n, 1/*sizeof(int)*/);
//    if(next <= 0) return -1;
//    return n;
//}

//void write_int(int fd, int n)
//{
//    int next = write(fd, &n, 1/*sizeof(int)*/);
//    if(next <= 0) error("read failed");
//}

//int read_all_fail(int fd, String buffer, /*size_t*/int bytes)
//{
//    /*size_t*/int n = 0;
//    while(n < bytes){
//        int next = read(fd, buffer + n, bytes-n);
//        if(next <= 0) return 1;
//        n += next;
//    }
//    return 0;
//}

//int write_all_fail(int fd, String buffer, /*size_t*/int bytes)
//{
//    /*size_t*/int n = 0;
//    while(n < bytes){
//        /*size_t*/int next = write(fd, buffer + n, bytes-n);
//        if(next <= 0) return 1;
//        n += next;
//    }
//    return 0;
//}

//void read_all(int fd, String buffer, /*size_t*/int bytes)
//{
//    /*size_t*/int n = 0;
//    while(n < bytes){
//        int next = read(fd, buffer + n, bytes-n);
//        if(next <= 0) error("read failed");
//        n += next;
//    }
//}

//void write_all(int fd, String buffer, /*size_t*/int bytes)
//{
//    /*size_t*/int n = 0;
//    while(n < bytes){
//        /*size_t*/int next = write(fd, buffer + n, bytes-n);
//        if(next <= 0) error("write failed");
//        n += next;
//    }
//}


//String copy_string(String s)
//{
//    String copy = malloc(strlen(s)+1);
//    strncpy(copy, s, strlen(s)+1);
//    return copy;
//}

//list parse_csv_line(char[] line)
//{
//    list l = make_list();
//    char[] c, p;
//    boolean in = false;
//    int i, p_i;
//    for(c = line, p = line, i=0; c[i] != '\0'; ++i){
//        if(c[i] == '"') in = !in;
//        else if(c[i] == ',' && !in){
//            c[i] = '\0';
//            list_insert(l, copy_string(p));
//            p = c+1;
//        }
//    }
//    list_insert(l, copy_string(p));
//    return l;
//}

int count_fields(char[] line)
{
    int count = 0,i;
    boolean done = false;
    char[] c;
    for(c = line,i=0; !done; ++i){
        done = (c[i] == '\0');
        if(c[i] == ',' || done) ++count;
    }
    return count;
}

//float[] parse_fields(char[] line, int n)
//{
//    float[] field = calloc(n, 1/*sizeof(float)*/);
//    char[] c, p, end;
//    int count = 0,i;
//    boolean done = false;
//    for(c = line, p = line, i=0; !done; ++i){
//        done = (c[i] == '\0');
//        if(c[i] == ',' || done){
//            c[i] = '\0';
//            field[count] = strtod(p, &end);
//            if(p == c) field[count] = nan("");
//            if(end != c && (end != c-1 || *end != '\r')) field[count] = nan(""); //DOS file formats!
//            p = c+1;
//            ++count;
//        }
//    }
//    return field;
//}

public static float sum_array(float[] a, int n) {
	return sum_array(a, 0, n);
}
public static float sum_array(float[] a, int a_offset, int n)
{
    int i;
    float sum = 0;
    for(i = 0; i < n; ++i) sum += a[a_offset+i];
    return sum;
}

float mean_array(float[] a, int n)
{
    return sum_array(a,n)/n;
}

void mean_arrays(float[][] a, int n, int els, float[] avg)
{
    int i;
    int j;
    memset(avg, 0, els*1/*sizeof(float)*/);
    for(j = 0; j < n; ++j){
        for(i = 0; i < els; ++i){
            avg[i] += a[j][i];
        }
    }
    for(i = 0; i < els; ++i){
        avg[i] /= n;
    }
}

void print_statistics(float[] a, int n)
{
    float m = mean_array(a, n);
    float v = variance_array(a, n);
    printf("MSE: %.6f, Mean: %.6f, Variance: %.6f\n", mse_array(a, n), m, v);
}

float variance_array(float[] a, int n)
{
    int i;
    float sum = 0;
    float mean = mean_array(a, n);
    for(i = 0; i < n; ++i) sum += (a[i] - mean)*(a[i]-mean);
    float variance = sum/n;
    return variance;
}

public static int constrain_int(int a, int min, int max)
{
    if (a < min) return min;
    if (a > max) return max;
    return a;
}

public static float constrain(float min, float max, float a)
{
    if (a < min) return min;
    if (a > max) return max;
    return a;
}

public static float dist_array(float[] a, float[] b, int n, int sub)
{
    int i;
    float sum = 0;
    for(i = 0; i < n; i += sub) sum += pow(a[i]-b[i], 2);
    return sqrt(sum);
}

float mse_array(float[] a, int n)
{
    int i;
    float sum = 0;
    for(i = 0; i < n; ++i) sum += a[i]*a[i];
    return sqrt(sum/n);
}

void normalize_array(float[] a, int n)
{
    int i;
    float mu = mean_array(a,n);
    float sigma = sqrt(variance_array(a,n));
    for(i = 0; i < n; ++i){
        a[i] = (a[i] - mu)/sigma;
    }
    mu = mean_array(a,n);
    sigma = sqrt(variance_array(a,n));
}

void translate_array(float[] a, int n, float s)
{
    int i;
    for(i = 0; i < n; ++i){
        a[i] += s;
    }
}

public static float mag_array(float[] a, int n)
{
    int i;
    float sum = 0;
    for(i = 0; i < n; ++i){
        sum += a[i]*a[i];   
    }
    return sqrt(sum);
}

void scale_array(float[] a, int n, float s)
{
    int i;
    for(i = 0; i < n; ++i){
        a[i] *= s;
    }
}

int sample_array(float[] a, int n)
{
    float sum = sum_array(a, n);
    scale_array(a, n, 1/sum);
    float r = rand_uniform(0, 1);
    int i;
    for(i = 0; i < n; ++i){
        r = r - a[i];
        if (r <= 0) return i;
    }
    return n-1;
}

int max_int_index(int[] a, int n)
{
    if(n <= 0) return -1;
    int i, max_i = 0;
    int max = a[0];
    for(i = 1; i < n; ++i){
        if(a[i] > max){
            max = a[i];
            max_i = i;
        }
    }
    return max_i;
}

public static int max_index(float[] a, int n)
{
    if(n <= 0) return -1;
    int i, max_i = 0;
    float max = a[0];
    for(i = 1; i < n; ++i){
        if(a[i] > max){
            max = a[i];
            max_i = i;
        }
    }
    return max_i;
}

public static int rand_int(int min, int max)
{
    if (max < min){
        int s = min;
        min = max;
        max = s;
    }
    int r = (rand()%(max - min + 1)) + min;
    return r;
}

// From http://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
static boolean haveSpare = false;
static double rand1, rand2;
public static float rand_normal()
{
    

    if(haveSpare)
    {
        haveSpare = false;
        return sqrt(rand1) * sin(rand2);
    }

    haveSpare = true;

    rand1 = rand() / ((double) RAND_MAX);
    if(rand1 < 1e-100) rand1 = 1e-100;
    rand1 = -2 * log(rand1);
    rand2 = (rand() / ((double) RAND_MAX)) * TWO_PI;

    return sqrt(rand1) * cos(rand2);
}

/*
   float rand_normal()
   {
   int n = 12;
   int i;
   float sum= 0;
   for(i = 0; i < n; ++i) sum += (float)rand()/RAND_MAX;
   return sum-n/2.;
   }
 */

/*size_t*/int rand_size_t()
{
    return  ((/*size_t*/int)(rand()&0xff) << 56) | 
        ((/*size_t*/int)(rand()&0xff) << 48) |
        ((/*size_t*/int)(rand()&0xff) << 40) |
        ((/*size_t*/int)(rand()&0xff) << 32) |
        ((/*size_t*/int)(rand()&0xff) << 24) |
        ((/*size_t*/int)(rand()&0xff) << 16) |
        ((/*size_t*/int)(rand()&0xff) << 8) |
        ((/*size_t*/int)(rand()&0xff) << 0);
}

public static float rand_uniform(float min, float max)
{
    if(max < min){
        float swap = min;
        min = max;
        max = swap;
    }
    return ((float)rand()/RAND_MAX * (max - min)) + min;
}

public static float rand_scale(float s)
{
    float scale = rand_uniform(1, s);
    if(rand()%2!=0) return scale;
    return 1.f/scale;
}

//float[][] one_hot_encode(float[] a, int n, int k)
//{
//    int i;
//    float[][] t = calloc(n, sizeof(float*));
//    for(i = 0; i < n; ++i){
//        t[i] = calloc(k, 1/*sizeof(float)*/);
//        int index = (int)a[i];
//        t[i][index] = 1;
//    }
//    return t;
//}

}