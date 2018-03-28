package cn;

import java.io.IOException;

import cn.wrapper.tree_variables;

public class tree extends tree_variables
{

void change_leaves(tree t, String leaf_list) throws IOException, Exception
{
//    list llist = get_paths(leaf_list);
//    String[] leaves = (String[] )list_to_array(llist);
	String[] leaves = data.get_paths2(leaf_list);
    int n = leaves.length;
    int i,j;
    int found = 0;
    for(i = 0; i < t.n; ++i){
        t.leaf[i] = false;
        for(j = 0; j < n; ++j){
            if (0==strcmp(t.name[i], leaves[j])){
                t.leaf[i] = true;
                ++found;
                break;
            }
        }
    }
    fprintf(stderr, "Found %d leaves.\n", found);
}

public static float get_hierarchy_probability(float []x, tree hier, int c, int stride) {
	return get_hierarchy_probability(x, 0, hier, c, stride);
}
public static float get_hierarchy_probability(float []x, int x_offset, tree hier, int c, int stride)
{
    float p = 1;
    while(c >= 0){
        p = p * x[x_offset+c*stride];
        c = hier.parent[c];
    }
    return p;
}

public static void hierarchy_predictions(float []predictions, int n, tree hier, boolean only_leaves, int stride) {
	hierarchy_predictions(predictions, 0, n, hier, only_leaves, stride);
}
public static void hierarchy_predictions(float []predictions, int predictions_offset, int n, tree hier, boolean only_leaves, int stride)
{
    int j;
    for(j = 0; j < n; ++j){
        int parent = hier.parent[j];
        if(parent >= 0){
            predictions[predictions_offset+j*stride] *= predictions[predictions_offset+parent*stride]; 
        }
    }
    if(only_leaves){
        for(j = 0; j < n; ++j){
            if(!hier.leaf[j]) predictions[predictions_offset+j*stride] = 0;
        }
    }
}

public static int hierarchy_top_prediction(float []predictions, tree hier, float thresh, int stride) {
	return hierarchy_top_prediction(predictions, 0, hier, thresh, stride);
}
public static int hierarchy_top_prediction(float []predictions, int predictions_offset, tree hier, float thresh, int stride)
{
    float p = 1;
    int group = 0;
    int i;
    while(true/*1*/){
        float max = 0;
        int max_i = 0;

        for(i = 0; i < hier.group_size[group]; ++i){
            int index = i + hier.group_offset[group];
            float val = predictions[(i + hier.group_offset[group])*stride];
            if(val > max){
                max_i = index;
                max = val;
            }
        }
        if(p*max > thresh){
            p = p*max;
            group = hier.child[max_i];
            if(hier.child[max_i] < 0) return max_i;
        } else if (group == 0){
            return max_i;
        } else {
            return hier.parent[hier.group_offset[group]];
        }
    }
//    return 0;
}

public static tree read_tree(String filename)
{

		System.out.println("Tree.read_tree() is not implied.");
	    tree t = new tree();
//    tree t = new tree();
//    FILE *fp = fopen(filename, "r");
//
//    String line;
//    int last_parent = -1;
//    int group_size = 0;
//    int groups = 0;
//    int n = 0;
//    while((line=fgetl(fp)) != 0){
//        String id = calloc(256, 1/*sizeof(char)*/);
//        int parent = -1;
//        sscanf(line, "%s %d", id, &parent);
//        t.parent = reallocInt(t.parent, (n+1)*1/*sizeof(int)*/);
//        t.parent[n] = parent;
//
//        t.child = reallocInt(t.child, (n+1)*1/*sizeof(int)*/);
//        t.child[n] = -1;
//
//        t.name = realloc(t.name, (n+1)*1/*sizeof(char*)*/);
//        t.name[n] = id;
//        if(parent != last_parent){
//            ++groups;
//            t.group_offset = reallocInt(t.group_offset, groups * 1/*sizeof(int)*/);
//            t.group_offset[groups - 1] = n - group_size;
//            t.group_size = reallocInt(t.group_size, groups * 1/*sizeof(int)*/);
//            t.group_size[groups - 1] = group_size;
//            group_size = 0;
//            last_parent = parent;
//        }
//        t.group = reallocInt(t.group, (n+1)*1/*sizeof(int)*/);
//        t.group[n] = groups;
//        if (parent >= 0) {
//            t.child[parent] = groups;
//        }
//        ++n;
//        ++group_size;
//    }
//    ++groups;
//    t.group_offset = reallocInt(t.group_offset, groups * 1/*sizeof(int)*/);
//    t.group_offset[groups - 1] = n - group_size;
//    t.group_size = reallocInt(t.group_size, groups * 1/*sizeof(int)*/);
//    t.group_size[groups - 1] = group_size;
//    t.n = n;
//    t.groups = groups;
//    t.leaf = callocBoolean(n, 1/*sizeof(int)*/);
//    int i;
//    for(i = 0; i < n; ++i) t.leaf[i] = true;
//    for(i = 0; i < n; ++i) if(t.parent[i] >= 0) t.leaf[t.parent[i]] = false;
//
//    fclose(fp);
//    tree tree_ptr = new tree();// calloc(1, sizeof(tree));
//    tree_ptr = t;
//    //error(0);
//    return tree_ptr
	    return t;
}
}