package com.yango.uc.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yango.common.web.vo.ResultVo;
import com.yango.uc.service.DepartmentService;
import com.yango.uc.web.vo.DepartmentVo;

import cn.hutool.core.convert.Convert;

@RestController
@RequestMapping("/rest/uc/department")
public class DepartmentRestController {

	@Autowired
	private DepartmentService departmentService;

	@GetMapping("/getAll")
	public ResultVo getAll() {
		return ResultVo.ok(departmentService.findAll());
	}

	@PostMapping("/save")
	public ResultVo save(@RequestBody DepartmentVo departmentVo) {
		return departmentService.save(departmentVo);
	}

	@PostMapping("/update")
	public ResultVo update(@RequestBody DepartmentVo departmentVo) {
		return departmentService.update(departmentVo);
	}

	@DeleteMapping("/del")
	public ResultVo del(String id) {
		return departmentService.del(Convert.toLong(id));
	}

}
