package com.yango.uc.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.apache.shiro.crypto.hash.Md5Hash;
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
import com.yango.uc.dao.mapper.DepartmentMapper;
import com.yango.uc.dao.mapper.PermissionMapper;
import com.yango.uc.dao.mapper.RoleMapper;
import com.yango.uc.dao.mapper.UserDeptPermMapper;
import com.yango.uc.dao.mapper.UserMapper;
import com.yango.uc.dao.mapper.UserRoleMapper;
import com.yango.uc.dao.model.DepartmentPO;
import com.yango.uc.dao.model.PermissionPO;
import com.yango.uc.dao.model.RolePO;
import com.yango.uc.dao.model.UserDeptPermPO;
import com.yango.uc.dao.model.UserPO;
import com.yango.uc.dao.model.UserRolePO;
import com.yango.uc.shiro.UserInfo;
import com.yango.uc.util.UcUtil;
import com.yango.uc.web.vo.UserPermVo;
import com.yango.uc.web.vo.UserVo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangbf
 * @since 2019-04-17
 */
@Service
@Transactional
public class UserService extends BaseService<UserMapper, UserPO> {

	public static final String DEFAILT_PASSWORD = "000000";

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private UserDeptPermMapper userDeptPermMapper;

	@Autowired
	private RedissonClient redissonClient;

	public UserInfo getUserInfo(Long userId) {
		UserPO user = userMapper.selectById(userId);

		UserInfo userInfo = new UserInfo(String.valueOf(user.getId()), user.getUsername(), user.getNickname());

		List<RolePO> roleList = roleMapper.findAllRoleByUserId(user.getId());
		RolePO role = CollUtil.isEmpty(roleList) ? new RolePO(2l, "游客") : roleList.get(0);

		boolean isAdmin = UcUtil.ROLE_ADMIN_ID.equals(StrUtil.toString(role.getId()));
		List<PermissionPO> permissions = isAdmin ? permissionMapper.findAllPermission() : permissionMapper.findAllPermissionByUserId(user.getId());

		userInfo.setIsAdmin(isAdmin ? "1" : "0");
		userInfo.setRoleName(role.getRoleName());
		userInfo.setPosition(user.getPosition());
		userInfo.setJobNo(user.getJobNo());
		userInfo.setUserNo(user.getUserNo());

		userInfo.setDeptNames(getAllDeptNames(user.getDeptId()));
		userInfo.setMobile(user.getMobile());
		userInfo.setPermissionList(UcUtil.convert(permissions, true));
		userInfo.setDataPermissionType(user.getDataPermissionType());
		
		return userInfo;
	}

	private String getAllDeptNames(Long deptId) {
		DepartmentPO dept = departmentMapper.selectById(deptId);

		List<Long> deptIds = CollUtil.toList();

		for (String idStr : dept.getFullDeptId().split("_")) {
			deptIds.add(Convert.toLong(idStr));
		}

		List<DepartmentPO> list = departmentMapper.selectBatchIds(deptIds);
		Map<Long, DepartmentPO> map = list.stream().collect(Collectors.toMap(DepartmentPO::getId, po -> po));

		DepartmentPO po = map.get(deptId);
		List<String> nameList = CollUtil.newArrayList();
		do {
			nameList.add(po.getDeptName());
			po = map.get(po.getParentId());
		} while (po != null);
		return CollUtil.join(CollUtil.reverse(nameList), " | ");
	}

	public UserPO selectByUsername(String username) {
		return userMapper.findUniqueByUsername(username);
	}

	public PageVo<UserVo> getPages(Map<String, Object> params, Integer pageNo, Integer pageSize) {
		Integer total = userMapper.selectCountByCond(params);

		PageVo<UserVo> pageVo = new PageVo<UserVo>(pageNo, pageSize, total);
		params.put("offset", (pageNo - 1) * pageSize);
		params.put("limit", pageSize);

		List<UserVo> list = userMapper.selectPageByCond(params);
		pageVo.setRows(list);

		return pageVo;
	}

	// 根据UserId查询单个UserVo
	public UserVo getByUserId(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", Convert.toLong(userId));
		params.put("offset", 0);
		params.put("limit", Integer.MAX_VALUE);

		List<UserVo> list = userMapper.selectPageByCond(params);
		UserVo userVo = CollUtil.isEmpty(list) ? null : list.get(0);
		return userVo;
	}

	// 保存
	public ResultVo save(UserVo userVo) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:uc_user_name:{}", userVo.getUsername()));
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");

			UserPO po = userMapper.findUniqueByUsername(userVo.getUsername());
			if (po != null) {
				return ResultVo.fail(StrUtil.format("用户[{}]已经存在", userVo.getUsername()));
			}

			UserPO userPO = new UserPO();
			BeanUtil.copyProperties(userVo, userPO);

			userPO.setPassword(generatePwd(userVo.getPassword(), Convert.toStr(userPO.getId())));
			userPO.setUserNo(queryNextUserNo());
			userPO.setStatus("1");
			userMapper.insert(userPO);

			UserRolePO userRolePO = new UserRolePO();
			userRolePO.setRoleId(Convert.toLong(userVo.getRoleId()));
			userRolePO.setUserId(userPO.getId());
			userRoleMapper.insert(userRolePO);

			String dataPermissionType = userVo.getDataPermissionType();
			if (dataPermissionType.equals("2") || dataPermissionType.equals("3")) {
				String[] permDeptIds = StrUtil.split(userVo.getUserPermDeptIds(), ",");
				for(String permDeptId : permDeptIds) {
					DepartmentPO deptPO = departmentMapper.selectById(Convert.toLong(permDeptId));
					UserDeptPermPO deptPermPO = new UserDeptPermPO();
					
					deptPermPO.setUserId(userPO.getId());
					deptPermPO.setDeptId(deptPO.getId());
					deptPermPO.setFullDeptId(deptPO.getFullDeptId());
					userDeptPermMapper.insert(deptPermPO);
				}
			}
			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "UserService.save", userVo);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}
	}

	// 修改
	public ResultVo update(UserVo userVo) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:uc_user_name:{}", userVo.getUsername()));
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");

			UserPO userPO = userMapper.selectById(Convert.toLong(userVo.getUserId()));
			BeanUtil.copyProperties(userVo, userPO, new String[] { "username", "userNo", "password" });
			if (StrUtil.isNotBlank(userVo.getPassword())) {
				userPO.setPassword(generatePwd(userVo.getPassword(), Convert.toStr(userPO.getId())));
			}

			userMapper.updateById(userPO);

			List<RolePO> list = roleMapper.findAllRoleByUserId(userPO.getId());
			RolePO rolePO = list.get(0);

			if (!rolePO.getId().equals(Convert.toLong(userVo.getRoleId()))) {
				userRoleMapper.deleteByUserId(userPO.getId());

				UserRolePO userRolePO = new UserRolePO();
				userRolePO.setUserId(userPO.getId());
				userRolePO.setRoleId(Convert.toLong(userVo.getRoleId()));
				userRoleMapper.insert(userRolePO);
			}
			
			userDeptPermMapper.delByUserId(userPO.getId());
			String dataPermissionType = userVo.getDataPermissionType();
			if (dataPermissionType.equals("2") || dataPermissionType.equals("3")) {
				String[] permDeptIds = StrUtil.split(userVo.getUserPermDeptIds(), ",");
				
				for(String permDeptId : permDeptIds) {
					DepartmentPO deptPO = departmentMapper.selectById(Convert.toLong(permDeptId));
					UserDeptPermPO deptPermPO = new UserDeptPermPO();
					
					deptPermPO.setUserId(userPO.getId());
					deptPermPO.setDeptId(deptPO.getId());
					deptPermPO.setFullDeptId(deptPO.getFullDeptId());
					userDeptPermMapper.insert(deptPermPO);
				}
			}
			
			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "UserService.update", userVo);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}
	}

	// 删除
	public ResultVo del(Long id) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:uc_user_id:{}", id));
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");
			userRoleMapper.deleteByUserId(id);
			userMapper.deleteById(id);
			userDeptPermMapper.delByUserId(id);
			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "UserService.del", id);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}
	}

	// 密码重置
	public ResultVo resetPwd(Long id) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:uc_user_id:{}", id));
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");
			UserPO userPO = userMapper.selectById(id);
			userPO.setPassword(generatePwd(DEFAILT_PASSWORD, Convert.toStr(userPO.getId())));
			userMapper.updateById(userPO);
			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "UserService.resetPwd", id);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}
	}

	private String generatePwd(String password, String salt) {
		Md5Hash encryptPwd = new Md5Hash(password, salt, 2);
		return encryptPwd.toHex();
	}

	// 获取所有leader员工
	public ResultVo findAll() {
		List<UserPO> users = userMapper.selectByMap(new HashMap<String, Object>());

		List<UserVo> list = users.stream().map(po -> {
			UserVo vo = new UserVo();
			vo.setUserId(Convert.toStr(po.getId()));
			vo.setNickname(po.getNickname());
			return vo;
		}).collect(Collectors.toList());
		return ResultVo.ok(list);
	}

	// 获取下一个userNo
	private String queryNextUserNo() {
		DecimalFormat df = new DecimalFormat("C0000");
		Long cNo = 0l;
		String maxUserNo = userMapper.queryMaxUserNo();
		if (StrUtil.isNotBlank(maxUserNo)) {
			try {
				cNo = df.parse(maxUserNo).longValue();
			} catch (Exception e) {
				throw new ServiceException("最大用户编号转化错误");
			}
		}
		String d = df.format(cNo + 1);
		return d;
	}

	public ResultVo updatePwd(Long id, String oldPwd, String newPwd) {
		UserPO userPO = userMapper.selectById(id);

		String opwd = generatePwd(oldPwd, Convert.toStr(userPO.getId()));
		if (!userPO.getPassword().equals(opwd)) {
			return ResultVo.fail("原始密码不一致");
		}

		String newpassword = generatePwd(newPwd, Convert.toStr(userPO.getId()));
		userPO.setPassword(newpassword);
		userMapper.updateById(userPO);
		return ResultVo.ok();
	}

	/**
	 * 根据username查询出所属所有权限用户的username(改进:登录记录数据权限,1和9业务sql可以自己处理)
	 * 
	 * @param userId
	 * @return
	 */
	public ResultVo findAllOwnPermUsernameByUsername(String username) {
		UserPO userPO = userMapper.findUniqueByUsername(username);
		if (userPO == null) {
			return ResultVo.fail(StrUtil.format("[username = {}]用户不存在", username));
		}

		Set<String> dataSet = Sets.newHashSet();
		String dataPermissionType = userPO.getDataPermissionType();
		switch (dataPermissionType) {
		case "1":
			dataSet.add(StrUtil.toString(username));
			break;
		case "2": {
			List<UserDeptPermPO> userDeptPermPOs = userDeptPermMapper.findAllByUserId(userPO.getId());

			for (UserDeptPermPO permPO : userDeptPermPOs) {
				List<UserPO> userList = userMapper.selectListByDeptId(permPO.getDeptId());
				dataSet.addAll(userList.stream().map(item -> {
					return item.getUsername();
				}).collect(Collectors.toList()));
			}
			break;
		}
		case "3": { // 指定部门及以下
			List<UserDeptPermPO> userDeptPermPOs = userDeptPermMapper.findAllByUserId(userPO.getId());

			for (UserDeptPermPO permPO : userDeptPermPOs) {
				List<UserPO> userList = userMapper.selectAllSubUserListByFullDeptId(permPO.getFullDeptId());
				dataSet.addAll(userList.stream().map(item -> {
					return item.getUsername();
				}).collect(Collectors.toList()));
			}
			break;
		}
		case "9": {
			List<UserPO> userList = userMapper.findAll();
			dataSet.addAll(userList.stream().map(item -> {
				return item.getUsername();
			}).collect(Collectors.toList()));
			break;
		}
		default:
			return ResultVo.fail(StrUtil.format("[username = {}]没有配置用户权限", username));
		}
		UserPermVo vo = new UserPermVo();
		vo.setUsername(username);
		vo.setData(dataSet);
		return ResultVo.ok(vo);
	}

}
