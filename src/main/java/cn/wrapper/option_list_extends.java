package cn.wrapper;

import cn.list;
import cn.option_list;

public class option_list_extends extends option_list {
	public static boolean option_find_boolean(list l, String key, int def)
	{
		String v = option_find(l, key);
		if(v!=null) return atoboolean(v);
		fprintf(stderr, "%s: Using default '%d'\n", key, def);
		return false;
	}

	public static boolean option_find_boolean_quiet(list l, String key, int def)
	{
		String v = option_find(l, key);
		if(v!=null) return atoboolean(v);
		return false;
	}
}
