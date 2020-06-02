package com.yango.uc.shiro;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yango.common.web.vo.MenuVo;
import com.yango.uc.util.UcUtil;
import com.yango.uc.web.vo.PermissionVo;

import cn.hutool.core.util.StrUtil;

public class UserInfo {

	private String id;
	private String username;
	private String name;
	private String roleName;
	private String isAdmin;
	
	private String position;
	
	private String jobNo;
	private String userNo;
	private String deptNames;
	private String mobile;
	private String dataPermissionType;
	
	private List<PermissionVo> permissionList = Lists.newArrayList();
	
	public UserInfo() {
	}

	public UserInfo(String id, String username, String name) {
		this.id = id;
		this.username = username;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getDeptNames() {
		return deptNames;
	}

	public void setDeptNames(String deptNames) {
		this.deptNames = deptNames;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<PermissionVo> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<PermissionVo> permissionList) {
		this.permissionList = permissionList;
	}

	public Set<String> getPermissions() {
		return permissionList.stream().filter(vo -> StrUtil.isNotBlank(vo.getPermissionCode())).map(PermissionVo::getPermissionCode).collect(Collectors.toSet());
	}
	
	public String getDataPermissionType() {
		return dataPermissionType;
	}

	public void setDataPermissionType(String dataPermissionType) {
		this.dataPermissionType = dataPermissionType;
	}
	
	public MenuVo getMenuVo() {
		return UcUtil.combineTreeMenuVo(permissionList, false);
	}

	public List<PermissionVo> getMenuList() {
		return permissionList.stream()
				.filter(po -> Sets.newHashSet(UcUtil.PERMISSION_TYPE_MENU, UcUtil.PERMISSION_TYPE_SYSTEM)
				.contains(po.getType()))
				.collect(Collectors.toList());
	}

}
