package com.yango.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@Configuration
public class MyBatisPlusConfiguration {

	/**
	 * 逻辑删除插件
	 * 
	 * @return
	 */
	@Bean
	public ISqlInjector sqlInjector() {
		return new LogicSqlInjector();
	}

	/**
	 * 乐观锁插件
	 * 
	 * @return
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}

    /**
     * 分页插件
     */
	@Profile({"dev","development","functional"})
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
	
}
