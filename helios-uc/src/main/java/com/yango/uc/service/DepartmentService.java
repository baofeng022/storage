package com.yango.uc.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yango.common.exception.ServiceException;
import com.yango.common.service.BaseService;
import com.yango.common.web.vo.ResultVo;
import com.yango.uc.dao.mapper.DepartmentMapper;
import com.yango.uc.dao.mapper.UserMapper;
import com.yango.uc.dao.model.DepartmentPO;
import com.yango.uc.dao.model.UserPO;
import com.yango.uc.web.vo.DepartmentVo;

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
public class DepartmentService extends BaseService<DepartmentMapper, DepartmentPO> {

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedissonClient redissonClient;
	
	public List<DepartmentVo> findAll() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", 0);
		params.put("limit", Integer.MAX_VALUE);
		List<DepartmentVo> list = departmentMapper.selectPageByCond(params);
		return list;
	}

	public ResultVo save(DepartmentVo departmentVo) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:department_add"));
		
		DepartmentPO parent = departmentMapper.selectById(Convert.toLong(departmentVo.getParentId()));
		
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");
			
			DepartmentPO po = new DepartmentPO();
			po.setDeptName(departmentVo.getDeptName());
			po.setDeptNo(queryNextDeptNo());
			po.setDeptType(departmentVo.getDeptType());
			po.setParentId(parent.getId());
			po.setFullDeptId(parent.getFullDeptId() + "_" + po.getId());
			departmentMapper.insert(po);
			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "DepartmentService.save", departmentVo);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}
	}

	public ResultVo update(DepartmentVo departmentVo) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:department_id:{}", departmentVo.getDeptId()));
		
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");

			DepartmentPO po = departmentMapper.selectById(Convert.toLong(departmentVo.getDeptId()));
			po.setDeptName(departmentVo.getDeptName());
			po.setDeptType(departmentVo.getDeptType());
			departmentMapper.updateById(po);
			
			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "DepartmentService.update", departmentVo);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}		
	}
	
	public ResultVo del(Long id) {
		RLock lock = redissonClient.getLock(StrUtil.format("locked:department_id:{}", id));
		try {
			Validate.isTrue(lock.tryLock(30, TimeUnit.SECONDS), "分布式锁锁定失败");

			// 判断是否有下级节点,有则不能删除
			List<DepartmentPO> children = departmentMapper.selectListByParentId(id);
			if (CollUtil.isNotEmpty(children)) {
				return ResultVo.fail(StrUtil.format("部门[{}]存在子节点不能删除", id));
			}
			
			// 判断是否有员工,有则不能删除
			List<UserPO> users = userMapper.selectListByDeptId(id);
			if (CollUtil.isNotEmpty(users)) {
				return ResultVo.fail(StrUtil.format("部门[{}]存在员工不能删除", id));
			}
			
			departmentMapper.deleteById(id);
			return ResultVo.ok();
		} catch (Exception e) {
			logError(e, "UserService.del", id);
			throw new ServiceException(e);
		} finally {
			lock.unlock();
		}
	}
	
	private String queryNextDeptNo() {
		DecimalFormat df = new DecimalFormat("B0000");
		Long cNo = 0l;
		String maxDeptNo = departmentMapper.queryMaxDeptNo();
		if (StrUtil.isNotBlank(maxDeptNo)) {
			try {
				cNo = df.parse(maxDeptNo).longValue();
			} catch (Exception e) {
				throw new ServiceException("最大部门编号转化错误");
			}
		}
		String d = df.format(cNo + 1);
		return d;
	}
	
}
