package com.yango.uc.web.vo;

import java.io.Serializable;

public class RoleVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String roleId;
	private String roleName;
	private String roleDes;
	private String roleNo;

	private String permIdStr;// 权限id集合转str
	private String btnPermIdStr;// 按钮权限菜单id字符串(renrenfast前端专用)

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDes() {
		return roleDes;
	}

	public void setRoleDes(String roleDes) {
		this.roleDes = roleDes;
	}

	public String getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}

	public String getPermIdStr() {
		return permIdStr;
	}

	public void setPermIdStr(String permIdStr) {
		this.permIdStr = permIdStr;
	}

	public String getBtnPermIdStr() {
		return btnPermIdStr;
	}

	public void setBtnPermIdStr(String btnPermIdStr) {
		this.btnPermIdStr = btnPermIdStr;
	}

}
