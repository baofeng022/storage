package com.yango.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CrossInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new OptionsInterceptor()).addPathPatterns("/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 设置允许跨域的路径
		registry.addMapping("/**")
				// 设置允许跨域请求的域名
				.allowedOrigins("*")
				// 是否允许证书 不再默认开启
				.allowCredentials(true)
				// 设置允许的方法
				.allowedMethods("*").maxAge(36000);

	}

	@Profile({ "dev", "development", "functional" })
	@Autowired
	public void setToken(Docket docket) {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		// 增加一个request的header参数
		tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		pars.add(tokenPar.build());
		docket.globalOperationParameters(pars);
	}
	
	@ConditionalOnMissingBean
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SPRING_WEB);
	}
	
	
}
