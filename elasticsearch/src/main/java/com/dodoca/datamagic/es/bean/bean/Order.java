package com.dodoca.datamagic.es.bean.bean;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable{
	
	private Integer id;
	
	private Integer deal_id;
	private Integer dog_shop_id;
	
	public Integer getDeal_id() {
		return deal_id;
	}

	public void setDeal_id(Integer deal_id) {
		this.deal_id = deal_id;
	}

	public Integer getDog_shop_id() {
		return dog_shop_id;
	}

	public void setDog_shop_id(Integer dog_shop_id) {
		this.dog_shop_id = dog_shop_id;
	}

	private Integer merchant_id;
	
	private Integer shop_id;
	
	private Integer from_merchant_id;
	
	private Integer from_shop_id;
	
	private Integer member_id;
	
	private String nickname;
	
	private String order_sn;
	
	//给商家的留言
	private String memo;
	
	private Double amount;
	
	private Double goods_amount;
	
	@Override
	public String toString() {
		return "Order [id=" + id + ", merchant_id=" + merchant_id
				+ ", shop_id=" + shop_id + ", from_merchant_id="
				+ from_merchant_id + ", from_shop_id=" + from_shop_id
				+ ", member_id=" + member_id + ", nickname=" + nickname
				+ ", order_sn=" + order_sn + ", memo=" + memo + ", amount="
				+ amount + ", goods_amount=" + goods_amount + ", is_selffetch="
				+ is_selffetch + ", payment_name=" + payment_name
				+ ", payment_sn=" + payment_sn + ", is_deleted=" + is_deleted
				+ ", payment_code=" + payment_code + ", trade_sn=" + trade_sn
				+ ", star=" + star + ", remarks=" + remarks + ", type=" + type
				+ ", order_type=" + order_type + ", status=" + status
				+ ", is_valid=" + is_valid + ", order_refund_status="
				+ order_refund_status + ", pay_at=" + pay_at + ", finished_at="
				+ finished_at + ", shipment_fee=" + shipment_fee
				+ ", is_start_order=" + is_start_order + ", has_self_quantity="
				+ has_self_quantity + ", created_at=" + created_at
				+ ", is_virtual=" + is_virtual + ", order_title=" + order_title
				+ ", support_payment=" + support_payment + ", exclude_payment="
				+ exclude_payment + ", to_share=" + to_share + ", payment="
				+ payment + ", is_sys=" + is_sys + ", is_supplier="
				+ is_supplier + ", payment_id=" + payment_id
				+ ", comment_status=" + comment_status + ", extend_days="
				+ extend_days + ", give_credit=" + give_credit
				+ ", status_remark=" + status_remark + ", explain=" + explain
				+ ", allow_refund=" + allow_refund + ", extend_info="
				+ extend_info + ", shipments_at=" + shipments_at
				+ ", is_peerpay=" + is_peerpay + ", coupon_amount="
				+ coupon_amount + ", gid=" + gid + ", is_finish=" + is_finish
				+ ", is_settled=" + is_settled + ", share_give_credit="
				+ share_give_credit + ", updated_at=" + updated_at
				+ ", expire_at=" + expire_at + ", is_rrds=" + is_rrds
				+ ", consignee=" + consignee + ", mobile=" + mobile
				+ ", country=" + country + ", province=" + province + ", city="
				+ city + ", district=" + district + ", country_name="
				+ country_name + ", province_name=" + province_name
				+ ", city_name=" + city_name + ", district_name="
				+ district_name + ", address=" + address + ", selffetch_store="
				+ selffetch_store + ", kibana_at=" + kibana_at + ", fields="
				+ fields + ", store_id=" + store_id + ", refund_status="
				+ refund_status + ", goods_name=" + goods_name + "]";
	}

	private Integer is_selffetch;
	
	private String payment_name;
	
	private String payment_sn;
	
	private Integer is_deleted;
	
	private String payment_code;
	
	//支付流水号
	private String trade_sn;
	
	//加星订单
	private Integer star;
	
	private String remarks;
	
	private Integer type;
	
	//0普通订单1厂家订单2商超订单
	private Integer order_type;
	
	//订单状态
	private Integer status;
	
	//是否有效，0无效，1有效
	private Integer is_valid;
	
	//维权订单 0:否  1:是
	private Integer order_refund_status;
	
	private String pay_at;
	
	private String finished_at;
	
	//运费
	private Double shipment_fee;
	
	//是否发起订单(厂家版)0不是1是
	private Integer is_start_order;
	
	//是否有自有库存(厂家版)0没有1有
	private Integer has_self_quantity;
	
	private String created_at;
	
	private Integer is_virtual;
	//新增得25個字段
	private String order_title;
	
	private String support_payment;
	
	private String exclude_payment;//不支持得支付方式
	
	private Integer to_share;//标记优惠共享 0:否 1:是
	
	private String payment;
	
	private Integer is_sys;//是否代收款
	
	private Integer is_supplier;//是否厂家代收款
	
	private Integer payment_id;
	
	private Integer comment_status;//评论状态 1:未评 2:首评 3:回复 4:追评
	
	private Integer extend_days;//延迟收货（天）
	
	private Integer give_credit;//自动送积分 0:未送 1:已送
	
	private String status_remark;//状态备注
	
	private String explain;//订单关闭说明（原因）
	
	private Integer allow_refund;//默认允许退款 0不可退，1可退
	
	private String extend_info;//订单扩展信息
	
	private String shipments_at;//发货时间
	
	private Integer is_peerpay;//是否是代付订单 0 否 1 是
	
	private double coupon_amount;
	
	private Integer gid;//在哪个推客小店下单
	
	private Integer is_finish;//是否完成可结算佣金0否1是
	
	private Integer is_settled;//0未结算 1已结算
	
	private Integer share_give_credit;//晒单成功是否送了积分
	
	private String updated_at;
	
	private String expire_at;
	
	private Integer is_rrds;//0旧数据，1人人电商新数据
	
	//新增加載完畢
	
	private String consignee;
	
	private String mobile;
	
	private Integer country;
	
	private Integer province;
	
	private Integer city;
	
	private Integer district;
	
	private String country_name;
	
	private String province_name;
	
	private String city_name;
	
	private String district_name;
	
	private String address;
	
	private SelffetchStore selffetch_store;
	
	private String kibana_at;
	
	private String fields;

	private int store_id;
	
	private List<Integer> refund_status;
	
	private List<String> goods_name;

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(Integer merchant_id) {
		this.merchant_id = merchant_id;
	}

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	public Integer getFrom_merchant_id() {
		return from_merchant_id;
	}

	public void setFrom_merchant_id(Integer from_merchant_id) {
		this.from_merchant_id = from_merchant_id;
	}

	public Integer getFrom_shop_id() {
		return from_shop_id;
	}

	public void setFrom_shop_id(Integer from_shop_id) {
		this.from_shop_id = from_shop_id;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getGoods_amount() {
		return goods_amount;
	}

	public void setGoods_amount(Double goods_amount) {
		this.goods_amount = goods_amount;
	}

	public Integer getIs_selffetch() {
		return is_selffetch;
	}

	public void setIs_selffetch(Integer is_selffetch) {
		this.is_selffetch = is_selffetch;
	}

	public String getPayment_name() {
		return payment_name;
	}

	public void setPayment_name(String payment_name) {
		this.payment_name = payment_name;
	}

	public String getPayment_sn() {
		return payment_sn;
	}

	public void setPayment_sn(String payment_sn) {
		this.payment_sn = payment_sn;
	}

	public Integer getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Integer is_deleted) {
		this.is_deleted = is_deleted;
	}

	public String getPayment_code() {
		return payment_code;
	}

	public void setPayment_code(String payment_code) {
		this.payment_code = payment_code;
	}

	public String getTrade_sn() {
		return trade_sn;
	}

	public void setTrade_sn(String trade_sn) {
		this.trade_sn = trade_sn;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getOrder_type() {
		return order_type;
	}

	public void setOrder_type(Integer order_type) {
		this.order_type = order_type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(Integer is_valid) {
		this.is_valid = is_valid;
	}

	public Integer getOrder_refund_status() {
		return order_refund_status;
	}

	public void setOrder_refund_status(Integer order_refund_status) {
		this.order_refund_status = order_refund_status;
	}


	public Double getShipment_fee() {
		return shipment_fee;
	}

	public void setShipment_fee(Double shipment_fee) {
		this.shipment_fee = shipment_fee;
	}

	public Integer getIs_start_order() {
		return is_start_order;
	}

	public void setIs_start_order(Integer is_start_order) {
		this.is_start_order = is_start_order;
	}

	public Integer getHas_self_quantity() {
		return has_self_quantity;
	}

	public void setHas_self_quantity(Integer has_self_quantity) {
		this.has_self_quantity = has_self_quantity;
	}


	public Integer getIs_virtual() {
		return is_virtual;
	}

	public void setIs_virtual(Integer is_virtual) {
		this.is_virtual = is_virtual;
	}

	public String getOrder_title() {
		return order_title;
	}

	public void setOrder_title(String order_title) {
		this.order_title = order_title;
	}

	public String getSupport_payment() {
		return support_payment;
	}

	public void setSupport_payment(String support_payment) {
		this.support_payment = support_payment;
	}

	public String getExclude_payment() {
		return exclude_payment;
	}

	public void setExclude_payment(String exclude_payment) {
		this.exclude_payment = exclude_payment;
	}

	public Integer getTo_share() {
		return to_share;
	}

	public void setTo_share(Integer to_share) {
		this.to_share = to_share;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public Integer getIs_sys() {
		return is_sys;
	}

	public void setIs_sys(Integer is_sys) {
		this.is_sys = is_sys;
	}

	public Integer getIs_supplier() {
		return is_supplier;
	}

	public void setIs_supplier(Integer is_supplier) {
		this.is_supplier = is_supplier;
	}

	public Integer getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(Integer payment_id) {
		this.payment_id = payment_id;
	}

	public Integer getComment_status() {
		return comment_status;
	}

	public void setComment_status(Integer comment_status) {
		this.comment_status = comment_status;
	}

	public Integer getExtend_days() {
		return extend_days;
	}

	public void setExtend_days(Integer extend_days) {
		this.extend_days = extend_days;
	}

	public Integer getGive_credit() {
		return give_credit;
	}

	public void setGive_credit(Integer give_credit) {
		this.give_credit = give_credit;
	}

	public String getStatus_remark() {
		return status_remark;
	}

	public void setStatus_remark(String status_remark) {
		this.status_remark = status_remark;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public Integer getAllow_refund() {
		return allow_refund;
	}

	public void setAllow_refund(Integer allow_refund) {
		this.allow_refund = allow_refund;
	}

	public String getExtend_info() {
		return extend_info;
	}

	public void setExtend_info(String extend_info) {
		this.extend_info = extend_info;
	}


	public Integer getIs_peerpay() {
		return is_peerpay;
	}

	public void setIs_peerpay(Integer is_peerpay) {
		this.is_peerpay = is_peerpay;
	}

	public double getCoupon_amount() {
		return coupon_amount;
	}

	public void setCoupon_amount(double coupon_amount) {
		this.coupon_amount = coupon_amount;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public Integer getIs_finish() {
		return is_finish;
	}

	public void setIs_finish(Integer is_finish) {
		this.is_finish = is_finish;
	}

	public Integer getIs_settled() {
		return is_settled;
	}

	public void setIs_settled(Integer is_settled) {
		this.is_settled = is_settled;
	}

	public Integer getShare_give_credit() {
		return share_give_credit;
	}

	public void setShare_give_credit(Integer share_give_credit) {
		this.share_give_credit = share_give_credit;
	}


	public Integer getIs_rrds() {
		return is_rrds;
	}

	public void setIs_rrds(Integer is_rrds) {
		this.is_rrds = is_rrds;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getCountry() {
		return country;
	}

	public void setCountry(Integer country) {
		this.country = country;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getDistrict() {
		return district;
	}

	public void setDistrict(Integer district) {
		this.district = district;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	public SelffetchStore getSelffetch_store() {
		return selffetch_store;
	}

	public void setSelffetch_store(SelffetchStore selffetch_store) {
		this.selffetch_store = selffetch_store;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getPay_at() {
		return pay_at;
	}

	public void setPay_at(String pay_at) {
		this.pay_at = pay_at;
	}

	public String getFinished_at() {
		return finished_at;
	}

	public void setFinished_at(String finished_at) {
		this.finished_at = finished_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getShipments_at() {
		return shipments_at;
	}

	public void setShipments_at(String shipments_at) {
		this.shipments_at = shipments_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getExpire_at() {
		return expire_at;
	}

	public void setExpire_at(String expire_at) {
		this.expire_at = expire_at;
	}

	public String getKibana_at() {
		return kibana_at;
	}

	public void setKibana_at(String kibana_at) {
		this.kibana_at = kibana_at;
	}

	public Order() {
		super();
	}

	

	public List<Integer> getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(List<Integer> refund_status) {
		this.refund_status = refund_status;
	}

	public List<String> getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(List<String> goods_name) {
		this.goods_name = goods_name;
	}

	public Order(Integer id, Integer merchant_id, Integer shop_id,
			Integer from_merchant_id, Integer from_shop_id, Integer member_id,
			String nickname, String order_sn, String memo, Double amount,
			Double goods_amount, Integer is_selffetch, String payment_name,
			String payment_sn, Integer is_deleted, String payment_code,
			String trade_sn, Integer star, String remarks, Integer type,
			Integer order_type, Integer status, Integer is_valid,
			Integer order_refund_status, String pay_at, String finished_at,
			Double shipment_fee, Integer is_start_order,
			Integer has_self_quantity, String created_at, Integer is_virtual,
			String order_title, String support_payment, String exclude_payment,
			Integer to_share, String payment, Integer is_sys,
			Integer is_supplier, Integer payment_id, Integer comment_status,
			Integer extend_days, Integer give_credit, String status_remark,
			String explain, Integer allow_refund, String extend_info,
			String shipments_at, Integer is_peerpay, double coupon_amount,
			Integer gid, Integer is_finish, Integer is_settled,
			Integer share_give_credit, String updated_at, String expire_at,
			Integer is_rrds, String consignee, String mobile, Integer country,
			Integer province, Integer city, Integer district,
			String country_name, String province_name, String city_name,
			String district_name, String address,
			SelffetchStore selffetch_store, String kibana_at, String fields,
			int store_id, List<Integer> refund_status, List<String> goods_name) {
		super();
		this.id = id;
		this.merchant_id = merchant_id;
		this.shop_id = shop_id;
		this.from_merchant_id = from_merchant_id;
		this.from_shop_id = from_shop_id;
		this.member_id = member_id;
		this.nickname = nickname;
		this.order_sn = order_sn;
		this.memo = memo;
		this.amount = amount;
		this.goods_amount = goods_amount;
		this.is_selffetch = is_selffetch;
		this.payment_name = payment_name;
		this.payment_sn = payment_sn;
		this.is_deleted = is_deleted;
		this.payment_code = payment_code;
		this.trade_sn = trade_sn;
		this.star = star;
		this.remarks = remarks;
		this.type = type;
		this.order_type = order_type;
		this.status = status;
		this.is_valid = is_valid;
		this.order_refund_status = order_refund_status;
		this.pay_at = pay_at;
		this.finished_at = finished_at;
		this.shipment_fee = shipment_fee;
		this.is_start_order = is_start_order;
		this.has_self_quantity = has_self_quantity;
		this.created_at = created_at;
		this.is_virtual = is_virtual;
		this.order_title = order_title;
		this.support_payment = support_payment;
		this.exclude_payment = exclude_payment;
		this.to_share = to_share;
		this.payment = payment;
		this.is_sys = is_sys;
		this.is_supplier = is_supplier;
		this.payment_id = payment_id;
		this.comment_status = comment_status;
		this.extend_days = extend_days;
		this.give_credit = give_credit;
		this.status_remark = status_remark;
		this.explain = explain;
		this.allow_refund = allow_refund;
		this.extend_info = extend_info;
		this.shipments_at = shipments_at;
		this.is_peerpay = is_peerpay;
		this.coupon_amount = coupon_amount;
		this.gid = gid;
		this.is_finish = is_finish;
		this.is_settled = is_settled;
		this.share_give_credit = share_give_credit;
		this.updated_at = updated_at;
		this.expire_at = expire_at;
		this.is_rrds = is_rrds;
		this.consignee = consignee;
		this.mobile = mobile;
		this.country = country;
		this.province = province;
		this.city = city;
		this.district = district;
		this.country_name = country_name;
		this.province_name = province_name;
		this.city_name = city_name;
		this.district_name = district_name;
		this.address = address;
		this.selffetch_store = selffetch_store;
		this.kibana_at = kibana_at;
		this.fields = fields;
		this.store_id = store_id;
		this.refund_status = refund_status;
		this.goods_name = goods_name;
	}
}
