package com.yango.uc.web.vo;

import java.util.Set;

public class UserPermVo {

	private String username;
	private Set<String> data;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<String> getData() {
		return data;
	}

	public void setData(Set<String> data) {
		this.data = data;
	}

}
