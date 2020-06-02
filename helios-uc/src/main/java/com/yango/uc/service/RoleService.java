package com.yango.uc.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.yango.common.exception.ServiceException;
import com.yango.common.service.BaseService;
import com.yango.common.web.vo.PageVo;
import com.yango.common.web.vo.ResultVo;
import com.yango.uc.dao.mapper.PermissionMapper;
import com.yango.uc.dao.mapper.RoleMapper;
import com.yango.uc.dao.mapper.RolePermissionMapper;
import com.yango.uc.dao.mapper.UserRoleMapper;
import com.yango.uc.dao.model.PermissionPO;
import com.yango.uc.dao.model.RolePO;
import com.yango.uc.dao.model.RolePermissionPO;
import com.yango.uc.dao.model.UserRolePO;
import com.yango.uc.util.UcUtil;
import com.yango.uc.web.vo.RoleVo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

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
public class RoleService extends BaseService<RoleMapper, RolePO> {
	
	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	@Autowired
	private PermissionMapper permissionMapper;
	
	@Autowired
	private RedissonClient redissonClient;

	public RolePO selectRoleByUserId(Long userId) {
		List<RolePO> list = roleMapper.findAllRoleByUserId(userId);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
//			return new RolePO("游客");
			return null;
		}
	}

	public PageVo<RoleVo> getPages(Map<String, Object> params, Integer pageNo, Integer pageSize) {
		Integer total = roleMapper.selectCountByCond(params);

		PageVo<RoleVo> pageVo = new PageVo<RoleVo>(pageNo, pageSize, total);
		params.put("offset", (pageNo - 1) * pageSize);
		params.put("limit", pageSize);

		List<RoleVo> list = roleMapper.selectPageByCond(params);
		pageVo.setRows(list);

		return pageVo;
	}

	public RoleVo findById(Long id) {
		RolePO rolePO = roleMapper.selectById(id);
		
		List<PermissionPO> list = permissionMapper.findAllPermissionByRoleId(id);
		
		RoleVo roleVo = new RoleVo();
		roleVo.setRoleId(Convert.toStr(rolePO.getId()));
		roleVo.setRoleName(rolePO.getRoleName());
		List<String> permIds = list.stream().map(po -> Convert.toStr(po.getId())).collect(Collectors.toList());
		List<String> btnPermIds = list.stream().filter(po->UcUtil.PERMISSION_TYPE_BUTTON.equals(po.getType())).map(po -> Convert.toStr(po.getId())).collect(Collectors.toList());
		roleVo.setPermIdStr(CollUtil.join(permIds, ","));
		roleVo.setBtnPermIdStr(CollUtil.join(btnPermIds, ","));
		
		return roleVo;
	}
	
	// 保存(rolename不能重复)
	public ResultVo save(RoleVo roleVo) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:uc_role_name:{}", roleVo.getRoleName()));
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");
			
			RolePO rolePO = new RolePO();
			rolePO.setRoleName(roleVo.getRoleName());
			rolePO.setRoleNo(queryNextRoleNo());
			rolePO.setRoleDes(roleVo.getRoleDes());
			rolePO.setStatus("1");
			roleMapper.insert(rolePO);
			
			String[] ids = roleVo.getBtnPermIdStr().split(",");
			List<PermissionPO> perms = permissionMapper.selectBatchIds(CollUtil.toList(ids).stream().map(str -> Convert.toLong(str)).collect(Collectors.toList()));
			Set<Long> permIds = Sets.newHashSet();
			perms.forEach(item -> {
				String[] fIds = item.getFullPermId().split("_");
				for (String fId : fIds) {
					permIds.add(Convert.toLong(fId));
				}
			});
			
			permIds.forEach(pId -> {
				RolePermissionPO rpPO = new RolePermissionPO();
				rpPO.setRoleId(rolePO.getId());
				rpPO.setPermissionId(pId);
				rolePermissionMapper.insert(rpPO);
			});

			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "RoleService.save", roleVo);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}
	}

	// 修改
	public ResultVo update(RoleVo roleVo) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:uc_role_name:{}", roleVo.getRoleName()));
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");
			// 检验rolename不存在
			RolePO rolePO = roleMapper.selectById(Convert.toLong(roleVo.getRoleId()));
			if (!rolePO.getRoleName().equals(roleVo.getRoleName())) {
				RolePO po = roleMapper.findUniqueByRolename(roleVo.getRoleName());
				if (po != null) {
					return ResultVo.fail(StrUtil.format("角色[{}]已经存在", roleVo.getRoleName()));
				}
			}
			rolePO.setRoleName(roleVo.getRoleName());
			rolePO.setRoleDes(roleVo.getRoleDes());
			roleMapper.updateById(rolePO);
			
			// 先删除原先的权限配置
			rolePermissionMapper.deleteByRoleId(Convert.toLong(roleVo.getRoleId()));
			String[] ids = roleVo.getBtnPermIdStr().split(",");
			List<PermissionPO> perms = permissionMapper.selectBatchIds(CollUtil.toList(ids).stream().map(str -> Convert.toLong(str)).collect(Collectors.toList()));
			Set<Long> permIds = Sets.newHashSet();
			perms.forEach(item -> {
				String[] fIds = item.getFullPermId().split("_");
				for (String fId : fIds) {
					permIds.add(Convert.toLong(fId));
				}
			});
			
			permIds.forEach(pId -> {
				RolePermissionPO rpPO = new RolePermissionPO();
				rpPO.setRoleId(rolePO.getId());
				rpPO.setPermissionId(pId);
				rolePermissionMapper.insert(rpPO);
			});
			
			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "RoleService.update", roleVo);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}
	}

	// 删除
	public ResultVo del(Long id) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:uc_role_id:{}", id));
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");
			List<UserRolePO> list = userRoleMapper.findAllByRoleId(id);
			if (CollUtil.isNotEmpty(list)) {
				return ResultVo.fail("存在用户关联权限");
			}
			rolePermissionMapper.deleteByRoleId(id);
			roleMapper.deleteById(id);

			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "RoleService.del", id);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}
	}
	
	// 获取下一个userNo
	private String queryNextRoleNo() {
		DecimalFormat df = new DecimalFormat("A0000");
		Long cNo = 0l;
		String maxRoleNo = roleMapper.queryMaxRoleNo();
		if (StrUtil.isNotBlank(maxRoleNo)) {
			try {
				cNo = df.parse(maxRoleNo).longValue();
			} catch (Exception e) {
				throw new ServiceException("最大角色编号转化错误");
			}
		}
		String d = df.format(cNo + 1);
		return d;
	}
	
}
