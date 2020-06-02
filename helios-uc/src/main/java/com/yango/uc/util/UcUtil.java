package com.yango.uc.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.yango.common.web.vo.MenuVo;
import com.yango.uc.dao.model.PermissionPO;
import com.yango.uc.web.vo.PermissionVo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;

public class UcUtil {

	// TOKEN 超时时间
	public static final Long TOKEN_EXPIRE_TIME_ = 24 * 60 * 60 * 1000L;
	
	// 登录成功loginToken的Key
	public static final String LOGIN_TOKEN_REDIS_KEY_ = "LOGIN_TOKEN_";
	
	
	// 超级管理员角色id
	public static final String ROLE_ADMIN_ID = "1";

	// 权限Type
	public static final String PERMISSION_TYPE_SYSTEM = "1";// 系统(默认根目录)
	public static final String PERMISSION_TYPE_MENU = "3";// 菜单
	public static final String PERMISSION_TYPE_BUTTON = "4";// 按钮(后端对应的接口)

	// 合并成Tree形态的MenuVo
	public static MenuVo combineTreeMenuVo(List<PermissionVo> permissions, boolean isContainsBtn) {
		Map<String, List<PermissionVo>> parentIdMap = permissions.parallelStream()
				.filter(po -> isContainsBtn || Sets.newHashSet(PERMISSION_TYPE_MENU, PERMISSION_TYPE_SYSTEM).contains(po.getType()))
				.collect(Collectors.groupingBy(PermissionVo::getParentId));
		MenuVo menuVo = new MenuVo("1", "系统", "", "", "", "1","0");
		combine(parentIdMap, menuVo);
		return menuVo;
	}

	private static void combine(Map<String, List<PermissionVo>> parentIdMap, MenuVo parentVo) {
		List<PermissionVo> children = parentIdMap.get(parentVo.getId());
		if (!CollectionUtil.isEmpty(children)) {
			children.forEach(item -> {
				MenuVo mVo = new MenuVo(item.getId(), item.getPermissionName(), item.getPermissionCode(), item.getPath(), item.getIcon(), item.getSort(),item.getParentId());
				parentVo.getSubList().add(mVo);
				combine(parentIdMap, mVo);
			});
		}
	}
	
	
	/**
	 * 权限对象转化
	 * 
	 * @param list
	 * @return
	 */
	public static List<PermissionVo> convert(List<PermissionPO> list, boolean isContainsBtn) {
		final Map<Long, List<PermissionPO>> parentIdMap = list.parallelStream()
				.filter(po -> isContainsBtn || Sets.newHashSet(PERMISSION_TYPE_MENU, PERMISSION_TYPE_SYSTEM).contains(po.getType()))
				.collect(Collectors.groupingBy(PermissionPO::getParentId));
		
		return list.stream().map(po -> {
			PermissionVo vo = new PermissionVo();
			BeanUtil.copyProperties(po, vo);
			List<PermissionPO> children = parentIdMap.get(po.getId());
			vo.setChildNum(Convert.toStr(CollectionUtil.isEmpty(children) ? 0 : children.size()));
			return vo;
		}).collect(Collectors.toList());
		
	}
	
}
