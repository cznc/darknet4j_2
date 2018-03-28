package cn;

import java.io.BufferedReader;
import java.io.FileReader;

import cn.wrapper.node;
import cn.wrapper.option_list_wrapper;

public class option_list extends option_list_wrapper
{

	public static list read_data_cfg(String filename)
	{
		BufferedReader file=null;
//	    FILE *file = fopen(filename, "r");
//	    if(file == 0) file_error(filename);
	    String line;
	    int nu = 0;
//	    ListDarknet options = make_list();
//	    while((line=fgetl(file)) != 0){
	    list options=new list();
	    try {
		
		    file=new BufferedReader(new FileReader(filename));
		    while((line=file.readLine())!=null) {
		        ++ nu;
		//        strip(line);
		        line=line.trim();
		        if(line.length()==0)continue;
		        switch(line.charAt(0)){
		            case '\0':
		            case '#':
		            case ';':
		//                free(line);
		                break;
		            default:
		                if(!read_option(line, options)){
		//                    fprintf(stderr, "Config file error line %d, could parse: %s\n", nu, line);
		                	System.err.print(String.format("Config file error line %d, could parse: %s\n", nu, line));
		//                    free(line);
		                }
		                break;
		        }
		    }
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
			//    fclose(file);
				if(file!=null)
					file.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	    return options;
	}
//list[] read_data_cfg(String filename)
//{
//    FILE *file = fopen(filename, "r");
//    if(file == 0) file_error(filename);
//    String line;
//    int nu = 0;
//    list[] options = make_list();
//    while((line=fgetl(file)) != 0){
//        ++ nu;
//        strip(line);
//        switch(line[0]){
//            case '\0':
//            case '#':
//            case ';':
//                free(line);
//                break;
//            default:
//                if(!read_option(line, options)){
//                    fprintf(stderr, "Config file error line %d, could parse: %s\n", nu, line);
//                    free(line);
//                }
//                break;
//        }
//    }
//    fclose(file);
//    return options;
//}

//metadata get_metadata(String file)
//{
//    metadata m = {0};
//    list[] options = read_data_cfg(file);
//
//    String name_list = option_find_str(options, "names", 0);
//    if(!name_list) name_list = option_find_str(options, "labels", 0);
//    if(!name_list) {
//        fprintf(stderr, "No names or labels found\n");
//    } else {
//        m.names = get_labels(name_list);
//    }
//    m.classes = option_find_int(options, "classes", 2);
//    free_list(options);
//    return m;
//}

//int read_option(String s, list options)
//{
//    /*size_t*/int i;
//    /*size_t*/int len = strlen(s);
//    String val = 0;
//    for(i = 0; i < len; ++i){
//        if(s[i] == '='){
//            s[i] = '\0';
//            val = s+i+1;
//            break;
//        }
//    }
//    if(i == len-1) return 0;
//    String key = s;
//    option_insert(options, key, val);
//    return 1;
//}
	static boolean read_option(String s, list options)
	{
		int index=s.indexOf('=');
		if(index==-1)return false;
	    String key = s.substring(0, index).trim();
		String val = s.substring(index+1).trim();
	    option_insert(options, key, val);
	    return true;
	}
static void option_insert(list l, String key, String val)
{
    kvp p = new kvp();//(sizeof(kvp));
    p.key = key;
    p.val = val;
    p.used = false;
    list_insert(l, p);
}

public static void option_unused(list l)
{
    node n = l.front;
    while(n!=null){
        kvp p = (kvp)n.val;
        if(!p.used){
            fprintf(stderr, "Unused field: '%s = %s'\n", p.key, p.val);
        }
        n = n.next;
    }
}

public static String option_find(list l, String key)
{
    node n = l.front;
    while(n!=null){
        kvp p = (kvp)n.val;
        if(strcmp(p.key, key) == 0){
            p.used = true;
            return p.val;
        }
        n = n.next;
    }
    return null;
}
public static String option_find_str(list l, String key, String def)
{
    String v = option_find(l, key);
    if(v!=null) return v;
    if(def!=null) fprintf(stderr, "%s: Using default '%s'\n", key, def);
    return def;
}

public static int option_find_int(list l, String key, int def)
{
    String v = option_find(l, key);
    if(v!=null) return atoi(v);
    fprintf(stderr, "%s: Using default '%d'\n", key, def);
    return def;
}

public static int option_find_int_quiet(list l, String key, int def)
{
    String v = option_find(l, key);
    if(v!=null) return atoi(v);
    return def;
}

public static float option_find_float_quiet(list l, String key, float def)
{
    String v = option_find(l, key);
    if(v!=null) return atof(v);
    return def;
}

public static float option_find_float(list l, String key, float def)
{
    String v = option_find(l, key);
    if(v!=null) return atof(v);
    fprintf(stderr, "%s: Using default '%f'\n", key, def);
    return def;
}
}
class kvp{
	String key;
	String val;
	boolean used=false;
}