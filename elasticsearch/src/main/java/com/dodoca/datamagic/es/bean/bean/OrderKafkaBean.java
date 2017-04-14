package com.dodoca.datamagic.es.bean.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.util.List;

public class OrderKafkaBean {

	@Override
	public String toString() {
		return "OrderKafkaBean [tableName=" + tableName + ", updatedAt=" + updatedAt + ", orderId=" + orderId
				+ ", count=" + count + ", ts=" + ts + "]";
	}

	private String tableName = null;
	private String updatedAt = null;
	private int orderId = -1;
	private int count = 1;
	private long ts = -1;

	private static Logger logger = Logger.getLogger(OrderKafkaBean.class);

	public void increaseCount() {
		count++;
	}

	public int getCount() {
		return count;
	}

	public String getUpDatedAt() {
		return updatedAt;
	}

	public String getTableName() {
		return tableName;
	}

	public int getOrderId() {
		return orderId;
	}

	public OrderKafkaBean(String message) {
		try {
			JSONObject json = JSONObject.parseObject(message);
			this.tableName = json.getString("table");
			this.ts = json.getLongValue("ts");
			json = JSONObject.parseObject(json.getString("data"));
			this.updatedAt = json.getString("updated_at");
			if (tableName.equals("order"))
				this.orderId = json.getIntValue("id");
			else
				this.orderId = json.getIntValue("order_id");
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	public boolean isOrderTable(List<String> tables) {
		for (String table : tables)
			if (table.equals(tableName))
				return true;
		return false;
	}

	public long getTimeIntervalCurrTs() {
		return Math.abs(System.currentTimeMillis() - ts) / 1000;
	}

}
