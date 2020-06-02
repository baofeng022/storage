package com.yango.uc.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yango.common.web.vo.PageVo;
import com.yango.common.web.vo.ResultVo;
import com.yango.uc.dao.model.RolePO;
import com.yango.uc.service.RoleService;
import com.yango.uc.web.vo.RoleVo;

import cn.hutool.core.convert.Convert;

@RestController
@RequestMapping("/rest/uc/role")
public class RoleRestController {

	@Autowired
	private RoleService roleService;

	@GetMapping("/getAllRoles")
	public ResultVo list() {
		List<RolePO> list = roleService.list(new QueryWrapper<>());
		return ResultVo.ok(list.stream().map(po -> {
			RoleVo vo = new RoleVo();
			vo.setRoleId(Convert.toStr(po.getId()));
			vo.setRoleName(po.getRoleName());
			return vo;
		}).collect(Collectors.toList()));
	}

	@GetMapping("/getPages")
	public ResultVo getPages(String pageNo, String pageSize) {
		Map<String, Object> params = new HashMap<>();
		PageVo<RoleVo> pageVo = roleService.getPages(params, Convert.toInt(pageNo), Convert.toInt(pageSize));
		return ResultVo.ok(pageVo);
	}

	@GetMapping("/findById")
	public ResultVo findById(String id) {
		RoleVo roleVo = roleService.findById(Convert.toLong(id));
		return ResultVo.ok(roleVo);
	}

	@PostMapping("/save")
	public ResultVo save(@RequestBody RoleVo userVo) {
		return roleService.save(userVo);
	}

	@PostMapping("/update")
	public ResultVo update(@RequestBody RoleVo userVo) {
		return roleService.update(userVo);
	}

	@DeleteMapping("/del")
	public ResultVo del(String id) {
		return roleService.del(Convert.toLong(id));
	}

}
