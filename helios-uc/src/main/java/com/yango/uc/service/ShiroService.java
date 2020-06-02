package com.yango.uc.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.springframework.stereotype.Component;

import com.yango.uc.shiro.UserInfo;

@Component
public class ShiroService {

	// 获取当前登录用户信息
	public UserInfo getUserInfo() {
		try {
			UserInfo userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();
			return userInfo;
		} catch (UnavailableSecurityManagerException e) {// 非web应用调用，No SecurityManager
			return null;
		}
	}

	// 获取当前登录用户登录姓名
	public String getUserInfoId() {
		UserInfo userInfo = getUserInfo();
		if (userInfo != null) {
			return userInfo.getId();
		} else {
			return null;
		}
	}

	// 获取当前登录用户登录姓名
	public String getUserInfoUsername() {
		UserInfo userInfo = getUserInfo();
		if (userInfo != null) {
			return userInfo.getUsername();
		} else {
			return null;
		}
	}

	// 获取当前登录用户姓名
	public String getUserInfoName() {
		UserInfo userInfo = getUserInfo();
		if (userInfo != null) {
			return userInfo.getName();
		} else {
			return null;
		}
	}
}
