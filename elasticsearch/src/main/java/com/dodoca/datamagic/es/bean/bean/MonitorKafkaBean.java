package com.dodoca.datamagic.es.bean.bean;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MonitorKafkaBean {
	private Long dateTime = System.currentTimeMillis();// 初试时间
	private ReadWriteLock rwl = new ReentrantReadWriteLock();// 读写所

	public void setCurrentDateTime(Long time) {
		rwl.writeLock().lock();
		try {
			if (time == null)
				dateTime = System.currentTimeMillis();
			else
				dateTime = time;
		} catch (Exception e) {
		} finally {
			rwl.writeLock().unlock();
		}
	}

	public Long getDateTime() {

		Long dateTime = 0l;
		rwl.readLock().lock();
		try {
			dateTime = this.dateTime.longValue();
		} finally {
			rwl.readLock().unlock();
		}
		return dateTime;
	}
}
