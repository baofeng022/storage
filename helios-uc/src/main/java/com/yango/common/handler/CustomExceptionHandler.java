package com.yango.common.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.google.common.collect.Maps;
import com.yango.common.exception.ServiceException;
import com.yango.common.web.vo.ResultVo;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.dialect.slf4j.Slf4jLogFactory;

@ControllerAdvice(annotations = { RestController.class })
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	public static final Log logger = Slf4jLogFactory.get("com.yango.RestController");
	
	public static final String JSON_UTF_8 = "application/json; charset=UTF-8";
	
	/**
	 * 抓获的异常
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { ServiceException.class })
	public final ResponseEntity<ResultVo> handleServiceException(ServiceException ex, HttpServletRequest request) {
		// 注入servletRequest，用于出错时打印请求URL与来源地址
		logError(ex, request);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(JSON_UTF_8));
		ResultVo resultVo = ResultVo.ex();
		return new ResponseEntity<ResultVo>(resultVo, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 未抓获的异常
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { Exception.class })
	public final ResponseEntity<ResultVo> handleGeneralException(Exception ex, HttpServletRequest request) {
		logError(ex, request);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(JSON_UTF_8));

		ResultVo resultVo = ResultVo.ex();
		return new ResponseEntity<ResultVo>(resultVo, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 重载ResponseEntityExceptionHandler的方法，加入日志
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		logError(ex);
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
		}
		return new ResponseEntity<Object>(body, headers, status);
	}

	public void logError(Exception ex) {
		Map<String, String> map = Maps.newHashMap();
		map.put("message", ex.getMessage());
		logger.error(ex, JSONUtil.toJsonStr(map));
	}

	public void logError(Exception ex, HttpServletRequest request) {
		Map<String, String> map = Maps.newHashMap();
		map.put("message", ex.getMessage());
		map.put("from", request.getRemoteAddr());
		String queryString = request.getQueryString();
		map.put("path", queryString != null ? (request.getRequestURI() + "?" + queryString) : request.getRequestURI());
		logger.error(ex, JSONUtil.toJsonStr(map));
	}

}
