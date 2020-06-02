package com.yango.uc.shiro.jwt;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.yango.uc.shiro.UserInfo;
import com.yango.uc.util.UcUtil;

public class JwtUserRealm extends AuthorizingRealm {

	@Autowired
	private RedissonClient redissonClient;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(userInfo.getPermissions());
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String accessToken = (String) token.getPrincipal();

		UserInfo userInfo = (UserInfo) redissonClient.getMapCache(UcUtil.LOGIN_TOKEN_REDIS_KEY_).get(accessToken);

		// token失效
		if (userInfo == null) {
			throw new IncorrectCredentialsException("token失效,请重新登录");
		}

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userInfo, accessToken, getName());
		return info;
	}

}
