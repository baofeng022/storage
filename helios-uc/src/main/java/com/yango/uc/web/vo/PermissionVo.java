package com.yango.uc.web.vo;

import java.io.Serializable;

public class PermissionVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String parentId;
	private String permissionName;
	private String permissionCode;
	private String path;
	private String icon;
	private String sort;
	private String type;// 3:菜单,4:按钮

	private String childNum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChildNum() {
		return childNum;
	}

	public void setChildNum(String childNum) {
		this.childNum = childNum;
	}

}
