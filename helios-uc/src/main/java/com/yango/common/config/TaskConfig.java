package com.yango.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

import cn.hutool.core.convert.Convert;

@Configuration
public class TaskConfig {

	private static final Logger logger = LoggerFactory.getLogger(TaskConfig.class);

	@Autowired
	private Environment env;

	@Profile({ "production", "functional", "development" })
	@Bean(initMethod = "start", destroyMethod = "destroy")
	public XxlJobSpringExecutor xxlJobExecutor() {
		logger.info(">>>>>>>>>>> xxl-job config init.");
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		xxlJobSpringExecutor.setAdminAddresses(env.getProperty("xxl.job.admin.addresses"));
		xxlJobSpringExecutor.setAppName(env.getProperty("xxl.job.executor.appname"));
		xxlJobSpringExecutor.setIp(env.getProperty("xxl.job.executor.ip", ""));
		xxlJobSpringExecutor.setPort(Convert.toInt(env.getProperty("xxl.job.executor.port")));
		xxlJobSpringExecutor.setAccessToken(env.getProperty("xxl.job.accessToken"));
		xxlJobSpringExecutor.setLogPath(env.getProperty("xxl.job.executor.logpath"));
		xxlJobSpringExecutor.setLogRetentionDays(Convert.toInt(env.getProperty("xxl.job.executor.logretentiondays")));
		return xxlJobSpringExecutor;
	}

}
