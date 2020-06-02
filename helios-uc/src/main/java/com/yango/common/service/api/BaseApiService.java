package com.yango.common.service.api;

import java.util.Map;

import com.google.common.collect.Maps;
import com.yango.common.web.vo.ResultVo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.dialect.slf4j.Slf4jLogFactory;

/**
 * 请求第三方数据(内部系统:不添加安全机制,以及只有get方法)
 * 
 * @author zhangbf
 *
 */
public class BaseApiService {

	public static final Log logger = Slf4jLogFactory.get(BaseApiService.class);

	public void logError(String url, String method, String message, Object... params) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("url", url);
		map.put("method", method);
		map.put("message", message);
		map.put("params", params);
		logger.error(JSONUtil.toJsonStr(map));
	}

	public <T> T httpGet(String url, Map<String, Object> params, Class<T> tclass) {
		try {
			String jsonStr = HttpRequest.get(url).form(params).timeout(2000).execute().body();
			JSONObject jsonObj = JSONUtil.parseObj(jsonStr);
			String flag = jsonObj.getStr("flag");
			String code = jsonObj.getStr("code");
			String message = jsonObj.getStr("message");
			JSONObject attachment = jsonObj.getJSONObject("attachment");

			if (ResultVo.SUCCESS_FLAG.equals(flag) && ResultVo.OK_CODE.equals(code) && ObjectUtil.isNotNull(attachment)) {// 请求成功,并且数据正常返回
				return JSONUtil.toBean(attachment, tclass);
			} else {
				logError(url, "GET", message, params);
			}
		} catch (Exception e) {
			logError(url, "GET", e.getMessage(), params);
			e.printStackTrace();
		}
		return null;
	}

}
