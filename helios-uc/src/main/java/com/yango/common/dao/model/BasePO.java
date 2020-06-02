package com.yango.common.dao.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yango.common.helper.DateHelper;
import com.yango.common.helper.SpringContextHelper;
import com.yango.uc.service.ShiroService;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

public class BasePO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	protected Long id;

	@TableLogic
	private String isValid;

	@Version
	private Integer version;

	private Long createUser;
	private Date createDate;

	private Long updateUser;
	private Date updateDate;

	public synchronized Long getId() {
		if (id == null) {
			id = IdWorker.getId();
		}
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIsValid() {
		if (isValid == null) {
			isValid = "1";
		}
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Integer getVersion() {
		if (version == null) {
			version = 1;
		}
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getCreateUser() {
		if(createUser == null) {
			String userId = SpringContextHelper.getBean(ShiroService.class).getUserInfoId();
			if(StrUtil.isNullOrUndefined(userId)) {
				userId = "0";
			}
			createUser = Convert.toLong(userId);
		}
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCreateDate() {
		if (createDate == null) {
			createDate = DateHelper.getCurrentDate();
		}
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getUpdateUser() {
		String userId = SpringContextHelper.getBean(ShiroService.class).getUserInfoId();
		if(StrUtil.isNullOrUndefined(userId)) {
			userId = "0";
		}
		updateUser = Convert.toLong(userId);
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUpdateDate() {
		return DateHelper.getCurrentDate();
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Transient
	public Date getModifyDate() {
		return updateDate;
	}

}
