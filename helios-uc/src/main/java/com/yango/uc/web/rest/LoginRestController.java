package com.yango.uc.web.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yango.common.exception.ServiceException;
import com.yango.common.helper.RequestContextHelper;
import com.yango.common.web.vo.ResultVo;
import com.yango.uc.dao.model.UserPO;
import com.yango.uc.service.UserService;
import com.yango.uc.shiro.UserInfo;
import com.yango.uc.shiro.jwt.UUIDTokenGenerator;
import com.yango.uc.util.UcUtil;
import com.yango.uc.web.vo.LoginFormVo;

import cn.hutool.core.convert.Convert;

@RestController
@RequestMapping("/rest/uc/login")
public class LoginRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedissonClient redissonClient;
	
	@PostMapping("/auth")
	public ResultVo authLogin(@RequestBody LoginFormVo loginFormVo) {
		try {
			String username = loginFormVo.getUsername();
			String password = loginFormVo.getPassword();
			if(username == null) {
				throw new ServiceException("用户名不能为空");
			}
			if(password == null) {
				throw new ServiceException("密码不能为空");
			}
			UserPO user = userService.selectByUsername(username);
			if(user == null) {
				throw new UnknownAccountException("账户不存在");
			}
			Md5Hash encryptPwd = new Md5Hash(password, Convert.toStr(user.getId()), 2);
			if (!encryptPwd.toHex().equals(user.getPassword())) {
				throw new IncorrectCredentialsException("用户名与密码不匹配");
			}
			if (!"1".equals(user.getStatus())) {
				throw new LockedAccountException("账户已被锁定");
			}
			if (!"1".equals(user.getIsValid())) {
				throw new DisabledAccountException("账户已被注销");
			}

			String token = UUIDTokenGenerator.generateValue();
			UserInfo userInfo = userService.getUserInfo(user.getId());
			
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("userId", Convert.toStr(user.getId()));
			resultMap.put("username", user.getUsername());
			resultMap.put("token", token);
			
			redissonClient.getMapCache(UcUtil.LOGIN_TOKEN_REDIS_KEY_).put(token, userInfo, UcUtil.TOKEN_EXPIRE_TIME_, TimeUnit.MILLISECONDS);
			
			return ResultVo.ok(resultMap);
		} catch (UnknownAccountException | IncorrectCredentialsException | DisabledAccountException e) {
			return ResultVo.fail(e.getMessage());
		}
	}

	@GetMapping("/getInfo")
	public ResultVo getInfo() {
		UserInfo userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();
		return ResultVo.ok(userInfo);
	}

	@PostMapping("/logout")
	public ResultVo logout() {
		try {
			String token = RequestContextHelper.getHttpServletRequest().getHeader("Token");
			redissonClient.getMapCache(UcUtil.LOGIN_TOKEN_REDIS_KEY_).remove(token);
		} catch (Exception e) {
		}
		return ResultVo.ok();
	}

}
