package com.dodoca.datamagic.utils.model;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable,Cloneable {

	// Fields

	private String id; //主键ID
	private String username; //用户名（账号）
	private String password; //密码
	private int role; //角色
	private String type; //用户类型
	private String shopId; //商铺ID
	private String project; //项目名称
	//将id属性记为string
	private String shopName; //商铺名称
	private String shopCate1Id; //商铺一级行业类别ID
	private String shopCate1Name;//商铺一级行业类别
	private String shopCate2Id;//商铺二级行业类别ID
	private String shopCate2Name; //商铺二级行业类别
	private String shopLineId; //KPI运营组ID
	private String shopLine; //KPI运营组
	private String token;
	private String createdAt;//创建时间
	private String updatedAt;//更新时间
	
	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String id, String username, int role) {
		this.id = id;
		this.username = username;
		this.role = role;
	}

	public User(String id, String username, String password, int role, String project) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.project = project;
	}

	public User(String id, String username, String password, int role,
				String type, String shopId, String project, String shopName,
				String shopCate1Id, String shopCate1Name, String shopCate2Id,
				String shopCate2Name, String shopLineId, String shopLine,
				String token, String createdAt, String updatedAt) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.type = type;
		this.shopId = shopId;
		this.project = project;
		this.shopName = shopName;
		this.shopCate1Id = shopCate1Id;
		this.shopCate1Name = shopCate1Name;
		this.shopCate2Id = shopCate2Id;
		this.shopCate2Name = shopCate2Name;
		this.shopLineId = shopLineId;
		this.shopLine = shopLine;
		this.token = token;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// EventProperty accessors

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRole() {
		return this.role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getShopId() {
		return this.shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return this.shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopCate1Id() {
		return this.shopCate1Id;
	}

	public void setShopCate1Id(String shopCate1Id) {
		this.shopCate1Id = shopCate1Id;
	}

	public String getShopCate1Name() {
		return this.shopCate1Name;
	}

	public void setShopCate1Name(String shopCate1Name) {
		this.shopCate1Name = shopCate1Name;
	}

	public String getShopCate2Id() {
		return this.shopCate2Id;
	}

	public void setShopCate2Id(String shopCate2Id) {
		this.shopCate2Id = shopCate2Id;
	}

	public String getShopCate2Name() {
		return this.shopCate2Name;
	}

	public void setShopCate2Name(String shopCate2Name) {
		this.shopCate2Name = shopCate2Name;
	}

	public String getShopLineId() {
		return this.shopLineId;
	}

	public void setShopLineId(String shopLineId) {
		this.shopLineId = shopLineId;
	}

	public String getShopLine() {
		return this.shopLine;
	}

	public void setShopLine(String shopLine) {
		this.shopLine = shopLine;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCreatedAt() {
		return createdAt;	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}


	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", role=" + role +
				", shopId='" + shopId + '\'' +
				", shopName='" + shopName + '\'' +
				", shopCate1Id='" + shopCate1Id + '\'' +
				", shopCate1Name='" + shopCate1Name + '\'' +
				", shopCate2Id='" + shopCate2Id + '\'' +
				", shopCate2Name='" + shopCate2Name + '\'' +
				", shopLineId='" + shopLineId + '\'' +
				", shopLine='" + shopLine + '\'' +
				", token='" + token + '\'' +
				", createdAt='" + createdAt + '\'' +
				", updatedAt='" + updatedAt + '\'' +
				'}';
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	//对象的 浅克隆,当有 引用类型 时候 需要 进行深克隆
	public User clone(){
		User user = null;
		try {
			user = (User)super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}