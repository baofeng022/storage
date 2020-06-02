package com.yango.common.web.vo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class TreeVo<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T data;

	private Set<TreeVo<T>> children;

	public TreeVo(T data) {
		this.data = data;
		this.children = new TreeSet<TreeVo<T>>();
	}

	public TreeVo(T data, Comparator<TreeVo<T>> comparator) {
		this.data = data;
		this.children = new TreeSet<TreeVo<T>>(comparator);
	}

	public T getData() {
		return data;
	}

	public Set<TreeVo<T>> getChildren() {
		return children;
	}

	public void setChildren(Set<TreeVo<T>> children) {
		this.children = children;
	}

	public void setData(T data) {
		this.data = data;
	}

}
