package com.yango.uc.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yango.common.web.vo.ResultVo;
import com.yango.uc.dao.model.PermissionPO;
import com.yango.uc.service.PermissionService;
import com.yango.uc.util.UcUtil;

@RestController
@RequestMapping("/rest/uc/permission")
public class PermissionRestController {

	@Autowired
	private PermissionService permissionService;

	/**
	 * 获取所有的权限树
	 * 
	 * @return
	 */
	@GetMapping("/tree")
	public ResultVo getTreeVo() {
		List<PermissionPO> list = permissionService.selectAll();
		return ResultVo.ok(UcUtil.combineTreeMenuVo(UcUtil.convert(list, true), true));
	}

	@GetMapping("/getMenuList")
	public ResultVo getMenuList() {
		List<PermissionPO> list = permissionService.selectAll();
		return ResultVo.ok(UcUtil.convert(list, false));
	}

	@GetMapping("/getAllList")
	public ResultVo getAllList() {
		List<PermissionPO> list = permissionService.selectAll();
		return ResultVo.ok(UcUtil.convert(list, true));
	}
	
}
