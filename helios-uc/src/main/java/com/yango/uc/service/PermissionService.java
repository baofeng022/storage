package com.yango.uc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yango.common.service.BaseService;
import com.yango.uc.dao.mapper.PermissionMapper;
import com.yango.uc.dao.model.PermissionPO;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-17
 */
@Service
@Transactional
public class PermissionService extends BaseService<PermissionMapper, PermissionPO> {

	@Autowired
	private PermissionMapper permissionMapper;
	
	public List<PermissionPO> selectAll() {
		return permissionMapper.findAllPermission();
	}
	
}
