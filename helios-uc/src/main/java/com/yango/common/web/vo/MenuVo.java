package com.yango.common.web.vo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;

public class MenuVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String code;
	private String path;
	private String icon;
	private String sort;

	private String parentId;

	private Set<MenuVo> subList = new TreeSet<MenuVo>(new Comparator<MenuVo>() {
		@Override
		public int compare(MenuVo o1, MenuVo o2) {
			int o1Sort = NumberUtil.isInteger(o1.getSort()) ? NumberUtil.parseInt(o1.getSort()) : 999;
			int o2Sort = NumberUtil.isInteger(o2.getSort()) ? NumberUtil.parseInt(o2.getSort()) : 999;
			return (o1Sort - o2Sort) >= 0 ? 1 : -1;
		}
	});

	public MenuVo() {
		super();
	}

	public MenuVo(String id, String name, String code, String path, String icon, String sort,String parentId) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.path = path;
		this.icon = icon;
		this.sort = sort;
		this.parentId = parentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Set<MenuVo> getSubList() {
		return subList;
	}

	public void setSubList(Set<MenuVo> subList) {
		this.subList = subList;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTitle() {
		return name;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Set<MenuVo> getChildren() {
		return !CollUtil.isEmpty(subList) ? subList : null;
	}

}
