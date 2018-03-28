package cn;

import cn.wrapper.list_variables;
import cn.wrapper.node;

public class list extends list_variables
{


public static list make_list()
{
	list l = new list();//malloc(sizeof(list));
	l.size = 0;
	l.front = null;
	l.back = null;
	return l;
}

/*
void transfer_node(list s, list d, node *n)
{
    node *prev, *next;
    prev = n.prev;
    next = n.next;
    if(prev) prev.next = next;
    if(next) next.prev = prev;
    --s.size;
    if(s.front == n) s.front = next;
    if(s.back == n) s.back = prev;
}
*/

Object list_pop(list l){
    if(null==l.back) return 0;
    node b = l.back;
    Object val = b.val;
    l.back = b.prev;
    if(l.back!=null) l.back.next = null;
    free(b);
    --l.size;
    
    return val;
}

public static void list_insert(list l, Object val)
{
	node _new = new node();//(sizeof(node));
	_new.val = val;
	_new.next = null;

	if(null==l.back){
		l.front = _new;
		_new.prev = null;
	}else{
		l.back.next = _new;
		_new.prev = l.back;
	}
	l.back = _new;
	++l.size;
}

static void free_node(node n)
{
	node next;
	while(n!=null) {
		next = n.next;
		free(n);
		n = next;
	}
}

public static void free_list(list l)
{
	free_node(l.front);
	free(l);
}

void free_list_contents(list l)
{
	node n = l.front;
	while(n!=null){
		free(n.val);
		n = n.next;
	}
}

Object []list_to_array(list l)
{
    Object []a = new Object[l.size];//calloc(l.size, sizeof(void*));
    int count = 0;
    node n = l.front;
    while(n!=null){
        a[count++] = n.val;
        n = n.next;
    }
    return a;
}
}
