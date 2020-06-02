package com.yango.uc.service;

import com.yango.uc.dao.model.UserDeptPermPO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;

import com.yango.uc.dao.mapper.UserDeptPermMapper;
import com.yango.common.service.BaseService;
import com.yango.common.web.vo.ResultVo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangbf
 * @since 2019-12-18
 */
@Service
@Transactional
public class UserDeptPermService extends BaseService<UserDeptPermMapper, UserDeptPermPO> {

	@Autowired
	private UserDeptPermMapper userDeptPermMapper;

	public ResultVo findPermDeptIdsByUserId(Long userId) {
		try {

			List<UserDeptPermPO> pos = userDeptPermMapper.findAllByUserId(userId);
			String userPermDeptIds = "";
			if (CollUtil.isNotEmpty(pos)) {
				List<String> list = pos.stream().map(item -> {
					return Convert.toStr(item.getDeptId());
				}).collect(Collectors.toList());
				userPermDeptIds = CollUtil.join(list, ",");
			}
			return ResultVo.ok(MapUtil.of("userPermDeptIds", userPermDeptIds));
		} catch (Exception e) {
			logError(e, "UserDeptPermService.getUserDeptPermMapper", userId);
			return ResultVo.fail(e.getMessage());
		}
	}

}
