package com.yango.common.service;

import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.dialect.slf4j.Slf4jLogFactory;

public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

	public static final Log logger = Slf4jLogFactory.get(BaseService.class);
	
	
	public void logError(Exception ex, String method, Object ...params) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("message", ex.getMessage());
		map.put("method", method);
		map.put("params", params);
		logger.error(ex, JSONUtil.toJsonStr(map));
	}
	
}
