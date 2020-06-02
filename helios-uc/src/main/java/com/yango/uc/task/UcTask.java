package com.yango.uc.task;

import org.springframework.stereotype.Service;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler("demoJobHandler")
@Service
public class UcTask extends IJobHandler {

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		System.out.println("1");
		return ReturnT.SUCCESS;
	}

}
