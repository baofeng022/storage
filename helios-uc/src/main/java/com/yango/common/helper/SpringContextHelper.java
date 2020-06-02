package com.yango.common.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHelper implements ApplicationContextAware {

	private static ApplicationContext ctx;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHelper.ctx = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	public static <T> T getBean(Class<T> t) {
		return ctx.getBean(t);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String className){
		return (T) ctx.getBean(className);
	}
}
