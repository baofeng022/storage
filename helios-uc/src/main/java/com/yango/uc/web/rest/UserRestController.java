package com.yango.uc.web.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yango.common.web.vo.PageVo;
import com.yango.common.web.vo.ResultVo;
import com.yango.uc.service.ShiroService;
import com.yango.uc.service.UserDeptPermService;
import com.yango.uc.service.UserService;
import com.yango.uc.web.vo.UserVo;

import cn.hutool.core.convert.Convert;

@RestController
@RequestMapping("/rest/uc/user")
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private ShiroService shiroService;
	
	@Autowired
	private UserDeptPermService userDeptPermService;
	
	@GetMapping("/getPages")
	public ResultVo getPages(String roleId, String departmentId, String pageNo, String pageSize) {
		Map<String, Object> params = new HashMap<>();
		params.put("roleId", Convert.toLong(roleId));
		params.put("departmentId", Convert.toLong(departmentId));
		PageVo<UserVo> pageVo = userService.getPages(params, Convert.toInt(pageNo), Convert.toInt(pageSize));
		return ResultVo.ok(pageVo);
	}

	@PostMapping("/save")
	public ResultVo save(@RequestBody UserVo userVo) {
		return userService.save(userVo);
	}

	@PostMapping("/update")
	public ResultVo update(@RequestBody UserVo userVo) {
		return userService.update(userVo);
	}

	@DeleteMapping("/del")
	public ResultVo del(String id) {
		return userService.del(Convert.toLong(id));
	}

	@PostMapping("/resetPwd")
	public ResultVo resetPwd(String id) {
		return userService.resetPwd(Convert.toLong(id));
	}

	@GetMapping("/getAllLeader")
	public ResultVo getAllLeader() {
		return userService.findAll();
	}
	
	@GetMapping("/findPermDeptIdsByUserId")
	public ResultVo findPermDeptIdsByUserId(String userId) {
		return userDeptPermService.findPermDeptIdsByUserId(Convert.toLong(userId));
	}
	
	@PostMapping("/updatePwd")
	public ResultVo updatePwd(String oldPwd, String newPwd) {
		return userService.updatePwd(Convert.toLong(shiroService.getUserInfoId()), oldPwd, newPwd);
	}
	
}
