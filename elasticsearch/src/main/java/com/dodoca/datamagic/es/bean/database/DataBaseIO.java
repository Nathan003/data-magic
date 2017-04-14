package com.dodoca.datamagic.es.bean.database;

import com.alibaba.fastjson.JSON;
import com.dodoca.datamagic.es.bean.bean.Order ;
import com.dodoca.datamagic.es.bean.bean.SelffetchStore ;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author qzli
 */
public class DataBaseIO {

	public static void main(String[] args) {
		DataBaseIO dbio = new DataBaseIO();
		Map<Integer, Order> abc = dbio.getOrderHistory(100000000, 100000100);
		for (Map.Entry<Integer, Order> entry : abc.entrySet()) {
			System.out.println(entry.getValue().toString());
		}
	}

	private static Logger logger = Logger.getLogger(DataBaseIO.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static TimeZone tz = TimeZone.getTimeZone("UTC");
	private static Connection conn = null;

	public DataBaseIO() {
		try {
			if (conn == null)
				conn = ConnectionSource.getConnection();
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	public List<Integer> getOrderID(String startTime, String endTime) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		List<Integer> orderIDs = Lists.newArrayList();
		String sql = "select id from wxrrd.order where updated_at>='" + startTime + "' and updated_at <= '" + endTime
				+ "'";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orderIDs.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			logger.error("connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
		return orderIDs;
	}

	public void getOrderStoreManage(int orderId, Order order) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select store_id from order_store_manage where order_id = " + orderId;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				order.setStore_id(rs.getInt("store_id"));
			}
		} catch (SQLException e) {
			logger.error("connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
	}

	public void getOrderStoreManageHistory(Map<Integer, Order> orders, int startID, int endID) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select order_id,store_id from order_store_manage where order_id>=" + startID + " and order_id<="
				+ endID;
		logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int order_id = 0;
			while (rs.next()) {
				order_id = rs.getInt("order_id");
				if (!orders.containsKey(order_id))
					continue;
				Order order = orders.get(order_id);
				order.setStore_id(rs.getInt("store_id"));
				orders.put(order_id, order);
			}
		} catch (SQLException e) {
			logger.error("connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
	}

	public void getOrderConsigneeAddr(int orderId, Order order) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select consignee,mobile,country,province,city,district,country_name,province_name,city_name,district_name,address,selffetch_store"
				+ " from order_consignee_addr where order_id = " + orderId;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				order.setConsignee(rs.getString("consignee"));
				order.setMobile(rs.getString("mobile"));
				order.setCountry(rs.getInt("country"));
				order.setProvince(rs.getInt("province"));
				order.setCity(rs.getInt("city"));
				order.setDistrict(rs.getInt("district"));
				order.setCountry_name(rs.getString("country_name"));
				order.setProvince_name(rs.getString("province_name"));
				order.setCity_name(rs.getString("city_name"));
				order.setDistrict_name(rs.getString("district_name"));
				order.setAddress(rs.getString("address"));
				String storeString = rs.getString("selffetch_store");
				if (storeString != null && !storeString.isEmpty()) {
					// if (storeString.startsWith("["))
					// order.setSelffetch_store(array.parse(storeString));
					if (storeString.startsWith("{")) {
						SelffetchStore ss = JSON.parseObject(storeString, SelffetchStore.class);
						order.setSelffetch_store(ss);
					}
				}

			}
		} catch (SQLException e) {
			logger.error("connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
	}

	public void getOrderConsigneeAddrHistory(Map<Integer, Order> orders, int startID, int endID) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select order_id,consignee,mobile,country,province,city,district,country_name,province_name,city_name,district_name,address,selffetch_store"
				+ " from order_consignee_addr where order_id>=" + startID + " and order_id<=" + endID;
		logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int order_id = 0;
			while (rs.next()) {
				try {
					order_id = rs.getInt("order_id");
					if (!orders.containsKey(order_id))
						continue;
					Order order = orders.get(order_id);
					order.setConsignee(rs.getString("consignee"));
					order.setMobile(rs.getString("mobile"));
					order.setCountry(rs.getInt("country"));
					order.setProvince(rs.getInt("province"));
					order.setCity(rs.getInt("city"));
					order.setDistrict(rs.getInt("district"));
					order.setCountry_name(rs.getString("country_name"));
					order.setProvince_name(rs.getString("province_name"));
					order.setCity_name(rs.getString("city_name"));
					order.setDistrict_name(rs.getString("district_name"));
					order.setAddress(rs.getString("address"));
					String storeString = rs.getString("selffetch_store");
					if (storeString != null && !storeString.isEmpty()) {
						if (storeString.startsWith("{")) {
							SelffetchStore ss = JSON.parseObject(storeString, SelffetchStore.class);
							order.setSelffetch_store(ss);
						}
					}
					orders.put(order_id, order);
				} catch (SQLException e) {
					logger.error("connection error in function  " + e);
				}

			}
		} catch (SQLException e) {
			logger.error("connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
	}

	public void excuteSQL(String sql) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
		} catch (SQLException e) {
			logger.error("connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
	}

	public void getOrderFields(int orderId, Order order) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select fields from order_related_info where order_id=" + orderId;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				order.setFields(rs.getString("fields"));
			}
		} catch (SQLException e) {
			logger.error("connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
	}

	public void getOrderFieldsHistory(Map<Integer, Order> orders, int startID, int endID) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select order_id,fields from order_related_info where order_id>=" + startID + " and order_id<="
				+ endID;
		logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int order_id = 0;
			while (rs.next()) {
				try {
					order_id = rs.getInt("order_id");
					if (!orders.containsKey(order_id))
						continue;
					Order order = orders.get(order_id);
					order.setFields(rs.getString("fields"));
					orders.put(order_id, order);
				} catch (SQLException e) {
					logger.error("数据解析异常  " + e.getStackTrace());
				}
			}
		} catch (SQLException e) {
			logger.error("connection error in function  " + e.getStackTrace());
		} finally {
			close(pstmt, rs);
		}
	}

	public void getOrderGoods(int orderId, Order order) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select goods_name, refund_status from order_goods where order_id=  " + orderId;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			List<Integer> refundList = new ArrayList<Integer>();
			List<String> goodsNameList = new ArrayList<String>();
			int index = 0;
			while (rs.next()) {
				index++;
				refundList.add(rs.getInt("refund_status"));
				goodsNameList.add(rs.getString("goods_name"));
			}
			if (index == 0)
				return;
			else {
				order.setRefund_status(refundList);
				order.setGoods_name(goodsNameList);
			}
		} catch (SQLException e) {
			logger.error("connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
	}

	public void getOrderGoodsHistory(Map<Integer, Order> orders, int startID, int endID) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select order_id,goods_name, refund_status from order_goods where order_id>=" + startID
				+ " and order_id<=" + endID;
		logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			Map<Integer, List<Integer>> refundList = Maps.newHashMap();
			Map<Integer, List<String>> goodsNameList = Maps.newHashMap();
			int order_id, refund_status;
			String goods_name;
			while (rs.next()) {
				try {
					order_id = rs.getInt("order_id");
					refund_status = rs.getInt("refund_status");
					goods_name = rs.getString("goods_name");
					if (!orders.containsKey(order_id))
						continue;
					if (!goodsNameList.containsKey(order_id))
						goodsNameList.put(order_id, new ArrayList<String>());
					goodsNameList.get(order_id).add(goods_name);
					if (!refundList.containsKey(order_id))
						refundList.put(order_id, new ArrayList<Integer>());
					refundList.get(order_id).add(refund_status);
				} catch (Exception e) {
					logger.error("connection error in function  " + e);
				}
			}
			for (Map.Entry<Integer, List<Integer>> entry : refundList.entrySet()) {
				Order order = orders.get(entry.getKey());
				if (entry.getValue() != null)
					order.setRefund_status(entry.getValue());
			}
			for (Map.Entry<Integer, List<String>> entry : goodsNameList.entrySet()) {
				Order order = orders.get(entry.getKey());
				if (entry.getValue() != null)
					order.setGoods_name(entry.getValue());
			}
		} catch (SQLException e) {
			logger.error("connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
	}

	public String getUpdatedAt(String sql) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String updatedAt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				updatedAt = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info(e);
		} finally {
			close(pstmt, rs);
		}
		return updatedAt;
	}

	/**
	 * 获取表中的推荐列表
	 * 
	 * @param tableName
	 * @param goods_id
	 * @return
	 * @throws ParseException
	 */
	public void getOrder(int order_id, Order order) {
		df.setTimeZone(tz);
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		String sql = "select deal_id,dog_shop_id,id,merchant_id,shop_id,from_merchant_id,from_shop_id,member_id,nickname,order_sn,order_title,amount,goods_amount,shipment_fee,is_selffetch,support_payment,exclude_payment,to_share,payment,is_sys,is_supplier,payment_id,payment_name,payment_code,payment_sn,trade_sn,star,remarks,type,order_type,status,comment_status,extend_days,is_valid,give_credit,status_remark,`explain`,refund_status AS order_refund_status,allow_refund,extend_info,memo,pay_at,finished_at,shipments_at,is_peerpay,is_deleted,coupon_amount,gid,is_finish,is_settled,is_start_order,has_self_quantity,share_give_credit,created_at,updated_at,expire_at,is_virtual,is_rrds"
				+ " from `order` where id = " + order_id;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				order.setId(rs.getInt("id"));
				order.setMerchant_id(rs.getInt("merchant_id"));
				order.setShop_id(rs.getInt("shop_id"));
				order.setFrom_merchant_id(rs.getInt("from_merchant_id"));
				order.setFrom_shop_id(rs.getInt("from_shop_id"));
				order.setMember_id(rs.getInt("member_id"));
				order.setNickname(rs.getString("nickname"));
				order.setOrder_sn(rs.getString("order_sn"));
				order.setMemo(rs.getString("memo"));
				order.setAmount(rs.getDouble("amount"));
				order.setGoods_amount(rs.getDouble("goods_amount"));
				order.setIs_selffetch(rs.getInt("is_selffetch"));
				order.setPayment_name(rs.getString("payment_name"));
				order.setPayment_sn(rs.getString("payment_sn"));
				order.setIs_deleted(rs.getInt("is_deleted"));
				order.setPayment_code(rs.getString("payment_code"));
				order.setTrade_sn(rs.getString("trade_sn"));
				order.setStar(rs.getInt("star"));
				order.setRemarks(rs.getString("remarks"));
				order.setType(rs.getInt("type"));
				order.setOrder_type(rs.getInt("order_type"));
				order.setStatus(rs.getInt("status"));
				order.setIs_valid(rs.getInt("is_valid"));
				order.setOrder_refund_status(rs.getInt("order_refund_status"));
				if (null != rs.getString("pay_at")) {
					Date date = sdf.parse(rs.getString("pay_at"));
					date.setHours(date.getHours() + 8);
					String payAt = df.format(date);
					// String payAt = df.format(date);
					order.setPay_at(payAt);
				}
				if (null != rs.getString("finished_at")) {
					Date date = sdf.parse(rs.getString("finished_at"));
					date.setHours(date.getHours() + 8);
					String finished_at = df.format(date);
					order.setFinished_at(finished_at);
				}
				order.setShipment_fee(rs.getDouble("shipment_fee"));
				order.setIs_start_order(rs.getInt("is_start_order"));
				order.setHas_self_quantity(rs.getInt("has_self_quantity"));
				if (null != rs.getString("created_at")) {
					Date date = sdf.parse(rs.getString("created_at"));
					date.setHours(date.getHours() + 8);
					String createdAt = df.format(date);

					// String createdAt = df.format(date);
					order.setCreated_at(createdAt);
					order.setKibana_at(createdAt + "+08:00");
				}
				order.setIs_virtual(rs.getInt("is_virtual"));
				order.setOrder_title(rs.getString("order_title"));
				order.setSupport_payment(rs.getString("support_payment"));
				order.setExclude_payment(rs.getString("exclude_payment"));
				order.setTo_share(rs.getInt("to_share"));
				order.setPayment(rs.getString("payment"));
				order.setIs_sys(rs.getInt("is_sys"));
				order.setIs_supplier(rs.getInt("is_supplier"));
				order.setPayment_id(rs.getInt("payment_id"));
				order.setComment_status(rs.getInt("comment_status"));
				order.setExtend_days(rs.getInt("extend_days"));
				order.setGive_credit(rs.getInt("give_credit"));
				order.setStatus_remark(rs.getString("status_remark"));
				order.setExplain(rs.getString("explain"));
				order.setAllow_refund(rs.getInt("allow_refund"));
				order.setExtend_info(rs.getString("extend_info"));
				if (null != rs.getString("shipments_at")) {
					Date date = sdf.parse(rs.getString("shipments_at"));
					date.setHours(date.getHours() + 8);
					order.setShipments_at(df.format(date));
				}
				order.setIs_peerpay(rs.getInt("is_peerpay"));
				order.setCoupon_amount(rs.getDouble("coupon_amount"));
				order.setGid(rs.getInt("gid"));
				order.setIs_finish(rs.getInt("is_finish"));
				order.setIs_settled(rs.getInt("is_settled"));
				order.setShare_give_credit(rs.getInt("share_give_credit"));
				if (null != rs.getString("updated_at")) {
					Date date = sdf.parse(rs.getString("updated_at"));
					date.setHours(date.getHours() + 8);
					order.setUpdated_at(df.format(date));
				}
				if (null != rs.getString("expire_at")) {
					Date date = sdf.parse(rs.getString("expire_at"));
					date.setHours(date.getHours() + 8);
					String payAt = df.format(date);
					order.setExpire_at(payAt);
				}
				order.setIs_rrds(rs.getInt("is_rrds"));
				order.setDeal_id(rs.getInt("deal_id"));
				order.setDog_shop_id(rs.getInt("dog_shop_id"));
			}
		} catch (Exception e) {
			logger.info(order_id + "  connection error in function  " + e);
		} finally {
			close(pstmt, rs);
		}
	}

	public Map<Integer, Order> getOrderHistory(int startID, int endID) {
		df.setTimeZone(tz);
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Map<Integer, Order> ordersMap = Maps.newHashMap();
		String sql = "select deal_id,dog_shop_id,id,merchant_id,shop_id,from_merchant_id,from_shop_id,member_id,nickname,order_sn,order_title,amount,goods_amount,shipment_fee,is_selffetch,support_payment,exclude_payment,to_share,payment,is_sys,is_supplier,payment_id,payment_name,payment_code,payment_sn,trade_sn,star,remarks,type,order_type,status,comment_status,extend_days,is_valid,give_credit,status_remark,`explain`,refund_status AS order_refund_status,allow_refund,extend_info,memo,pay_at,finished_at,shipments_at,is_peerpay,is_deleted,coupon_amount,gid,is_finish,is_settled,is_start_order,has_self_quantity,share_give_credit,created_at,updated_at,expire_at,is_virtual,is_rrds"
				+ " from `order` where id>=" + startID + " and id<=" + endID;
		logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Order order = new Order();
				try {
					order.setId(rs.getInt("id"));
					order.setMerchant_id(rs.getInt("merchant_id"));
					order.setShop_id(rs.getInt("shop_id"));
					order.setFrom_merchant_id(rs.getInt("from_merchant_id"));
					order.setFrom_shop_id(rs.getInt("from_shop_id"));
					order.setMember_id(rs.getInt("member_id"));
					order.setNickname(rs.getString("nickname"));
					order.setOrder_sn(rs.getString("order_sn"));
					order.setMemo(rs.getString("memo"));
					order.setAmount(rs.getDouble("amount"));
					order.setGoods_amount(rs.getDouble("goods_amount"));
					order.setIs_selffetch(rs.getInt("is_selffetch"));
					order.setPayment_name(rs.getString("payment_name"));
					order.setPayment_sn(rs.getString("payment_sn"));
					order.setIs_deleted(rs.getInt("is_deleted"));
					order.setPayment_code(rs.getString("payment_code"));
					order.setTrade_sn(rs.getString("trade_sn"));
					order.setStar(rs.getInt("star"));
					order.setRemarks(rs.getString("remarks"));
					order.setType(rs.getInt("type"));
					order.setOrder_type(rs.getInt("order_type"));
					order.setStatus(rs.getInt("status"));
					order.setIs_valid(rs.getInt("is_valid"));
					order.setOrder_refund_status(rs.getInt("order_refund_status"));
					if (null != rs.getString("pay_at")) {
						Date date = sdf.parse(rs.getString("pay_at"));
						date.setHours(date.getHours() + 8);
						String payAt = df.format(date);

						order.setPay_at(payAt);
					}
					if (null != rs.getString("finished_at")) {
						Date date = sdf.parse(rs.getString("finished_at"));
						date.setHours(date.getHours() + 8);
						String finished_at = df.format(date);
						order.setFinished_at(finished_at);
					}
					order.setShipment_fee(rs.getDouble("shipment_fee"));
					order.setIs_start_order(rs.getInt("is_start_order"));
					order.setHas_self_quantity(rs.getInt("has_self_quantity"));
					if (null != rs.getString("created_at")) {
						Date date = sdf.parse(rs.getString("created_at"));
						date.setHours(date.getHours() + 8);
						String createdAt = df.format(date);
						order.setCreated_at(createdAt);
						order.setKibana_at(createdAt + "+08:00");
					}
					order.setIs_virtual(rs.getInt("is_virtual"));
					order.setOrder_title(rs.getString("order_title"));
					order.setSupport_payment(rs.getString("support_payment"));
					order.setExclude_payment(rs.getString("exclude_payment"));
					order.setTo_share(rs.getInt("to_share"));
					order.setPayment(rs.getString("payment"));
					order.setIs_sys(rs.getInt("is_sys"));
					order.setIs_supplier(rs.getInt("is_supplier"));
					order.setPayment_id(rs.getInt("payment_id"));
					order.setComment_status(rs.getInt("comment_status"));
					order.setExtend_days(rs.getInt("extend_days"));
					order.setGive_credit(rs.getInt("give_credit"));
					order.setStatus_remark(rs.getString("status_remark"));
					order.setExplain(rs.getString("explain"));
					order.setAllow_refund(rs.getInt("allow_refund"));
					order.setExtend_info(rs.getString("extend_info"));
					if (null != rs.getString("shipments_at")) {
						Date date = sdf.parse(rs.getString("shipments_at"));
						date.setHours(date.getHours() + 8);
						order.setShipments_at(df.format(date));
					}
					order.setIs_peerpay(rs.getInt("is_peerpay"));
					order.setCoupon_amount(rs.getDouble("coupon_amount"));
					order.setGid(rs.getInt("gid"));
					order.setIs_finish(rs.getInt("is_finish"));
					order.setIs_settled(rs.getInt("is_settled"));
					order.setShare_give_credit(rs.getInt("share_give_credit"));
					if (null != rs.getString("updated_at")) {
						Date date = sdf.parse(rs.getString("updated_at"));
						date.setHours(date.getHours() + 8);
						order.setUpdated_at(df.format(date));
					}
					if (null != rs.getString("expire_at")) {
						Date date = sdf.parse(rs.getString("expire_at"));
						date.setHours(date.getHours() + 8);
						order.setExpire_at(df.format(date));
					}
					order.setIs_rrds(rs.getInt("is_rrds"));
					order.setDeal_id(rs.getInt("deal_id"));
					order.setDog_shop_id(rs.getInt("dog_shop_id"));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ordersMap.put(order.getId(), order);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt, rs);
		}
		return ordersMap;
	}

	public Boolean close(PreparedStatement pstmt, ResultSet rs) {
		Boolean flag = true;
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			flag = false;
		}
		return flag;
	}
}
