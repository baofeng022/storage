package com.yango.uc.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yango.common.exception.ServiceException;
import com.yango.common.web.vo.ResultVo;
import com.yango.uc.service.UserService;
import com.yango.uc.web.vo.UserVo;

@RestController
@RequestMapping("/api/uc/user")
public class UserApiController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/getByUserId")
	public ResultVo getByUserId(String userId) {
		return ResultVo.ok(userService.getByUserId(userId));
	}
	
	@GetMapping("/test")
	public UserVo test() {
		throw new ServiceException("出错了 测试");
	}

	@GetMapping("/getPermAllUsernameByUsername")
	public ResultVo getPermAllUsernameByUsername(String username) {
		return userService.findAllOwnPermUsernameByUsername(username);
	}
	
	
}
