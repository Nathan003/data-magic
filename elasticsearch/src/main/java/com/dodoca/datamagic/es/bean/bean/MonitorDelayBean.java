package com.dodoca.datamagic.es.bean.bean;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MonitorDelayBean {

	private Boolean isUpdate = false;// 初试时间
	private List<String> orderIDs = new ArrayList<String>();
	private ReadWriteLock rwl = new ReentrantReadWriteLock();// 读写所
	private static Logger logger = Logger.getLogger(MonitorDelayBean.class);

	public void setIsUpdate(boolean b,String orderID) {
		rwl.writeLock().lock();
		try {
			isUpdate = b;
			if(b)
				orderIDs.add(orderID);
			else 
				orderIDs.clear();
		} catch (Exception e) {
		} finally {
			rwl.writeLock().unlock();
		}
	}

	public boolean getUpdateBoolean() {

		boolean b = false;
		rwl.readLock().lock();
		try {
			b = this.isUpdate.booleanValue();
			if(b)
				logger.info("delay orderids = "+orderIDs);
		} finally {
			rwl.readLock().unlock();
		}
		return b;
	}
	
	public static void main(String[]	args){
	       List<Integer> list = new ArrayList<Integer>();  
	        //为arraylist添加一些元素  
	        for(int i = 0 ; i < 10 ; i++){  
	            list.add(i);  
	        }
	        System.err.println(list);
	}
}
