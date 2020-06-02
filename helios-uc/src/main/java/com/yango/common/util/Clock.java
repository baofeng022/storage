/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.yango.common.util;

import java.util.Date;

/**
 * 日期提供者，使用它而不是直接取得系统时间，方便测试。
 * 
 * @author calvin
 */
public interface Clock {

	static final Clock DEFAULT = new DefaultClock();

	Date getCurrentDate();

	long getCurrentTimeInMillis();

	/**
	 * 默认时间提供者，返回当前的时间，线程安全。
	 */
	public static class DefaultClock implements Clock {

		@Override
		public Date getCurrentDate() {
			return new Date();
		}

		@Override
		public long getCurrentTimeInMillis() {
			return System.currentTimeMillis();
		}
	}

	/**
	 * 可配置的时间提供者，用于测试.
	 */
	public static class MockClock implements Clock {

		private long time;

		private long tickTime;

		private boolean isRun;

		public MockClock() {
			this(new Date());
		}

		public MockClock(Date date) {
			this(date, false);
		}

		public MockClock(Date date, boolean isRun) {
			this.time = date.getTime();
			this.tickTime = new Date().getTime();
			this.isRun = isRun;
		}

		@Override
		public Date getCurrentDate() {
			return new Date(getCurrentTimeInMillis());
		}

		@Override
		public long getCurrentTimeInMillis() {
			return isRun ? (time + new Date().getTime() - tickTime) : time;
		}

		/**
		 * 重新设置日期。
		 */
		public void update(Date newDate) {
			time = newDate.getTime();
			this.tickTime = new Date().getTime();
		}

		/**
		 * 重新设置时间。
		 */
		public void update(long newTime) {
			this.time = newTime;
			this.tickTime = new Date().getTime();
		}

	}

}
