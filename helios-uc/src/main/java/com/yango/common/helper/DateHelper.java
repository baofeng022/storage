package com.yango.common.helper;

import java.util.Date;

import org.springframework.core.env.Environment;

import com.yango.common.exception.ServiceException;
import com.yango.common.util.Clock;

import cn.hutool.core.date.DateTime;

public class DateHelper {

	private static Clock clock = Clock.DEFAULT;

	public static Date getCurrentDate() {
		return clock.getCurrentDate();
	}

	public static void setCurrentDate(Date date) {
		Environment env = SpringContextHelper.getBean(Environment.class);
		String profile = env.getActiveProfiles()[0];
		if (!"production".equals(profile)) {
			clock = new Clock.MockClock(date);
		} else {
			throw new ServiceException("生产环境不能修改时间");
		}
	}

	public static String getCurrentDateStr(String format) {
		return new DateTime(getCurrentDate()).toString(format);
	}

}
