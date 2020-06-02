package com.yango.uc.shiro.jwt;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yango.common.web.vo.ResultVo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

public class JwtFilter extends AuthenticatingFilter {

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		//获取请求token
        String token = getRequestToken((HttpServletRequest) request);
		
        if(StrUtil.isBlank(token)){
            return null;
        }
        
		return new JwtToken(token);
	}

	@Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }

        return false;
    }

	@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回401
        String token = getRequestToken((HttpServletRequest) request);
        if(StrUtil.isBlank(token)){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
            
            ResultVo vo = ResultVo.unAuth();
            httpResponse.getWriter().print(JSONUtil.toJsonStr(vo));

            return false;
        }

        return executeLogin(request, response);
    }
	
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        try {
        	String ctoken = getRequestToken((HttpServletRequest) request);
            if(!StrUtil.isBlank(ctoken)){
	            //处理登录失败的异常
	        	ResultVo vo = ResultVo.unAuth();
	            httpResponse.getWriter().print(JSONUtil.toJsonStr(vo));
            }
        } catch (IOException e1) {

        }

        return false;
    }
	
    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if(StrUtil.isBlank(token)){
            token = httpRequest.getParameter("token");
        }

        return token;
    }
}
