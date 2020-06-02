package com.yango.uc.service;

import com.yango.uc.dao.model.RolePermissionPO;
import com.yango.uc.dao.mapper.RolePermissionMapper;
import com.yango.common.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class RolePermissionService extends BaseService<RolePermissionMapper, RolePermissionPO> {

}
