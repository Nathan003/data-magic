package com.dodoca.datamagic.common.service;

import com.dodoca.datamagic.utils.model.User ;

import java.util.List;
import java.util.Map;

public interface UserServie {

	/**
	 * 获取所有数据
	 */
	public abstract List<User> get() throws Exception;

	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 */
	public abstract User getById(String id) throws Exception;

	/**
	 * 根据用户的 name 和 项目名 查找用户
	 * @param username
	 * @return
	 */
	public abstract User getByName(String username);

	/**
	 * 保存数据
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public abstract String save(User user) throws Exception;

	/**
	 * 批量保存数据，如果失败，则所有数据都不保存
	 * @param userList
	 * @return
	 * @throws Exception
	 */
	public abstract boolean save(List<User> userList) throws Exception;

	/**
	 * 根据ID更新数据
	 * @param user
	 * @return
	 */
	public abstract boolean update(User user);

	/**
	 * 批量删除数据
	 * @param userList
	 * @return
	 */
	public abstract boolean delete(List<User> userList);


	/**
	 * 分页获取数据
	 * @param rows
	 * @param page
	 * @return
	 */
	public abstract List<User> get(int rows, int page);

	/**
	 * 模糊查询username
	 * @param username
	 * @return
	 */
	public abstract List<User> getByBlurName(String username);

	/**
	 * 保存用户至数据魔方，如果保存失败，则删除中间系统的用户数据
	 * @param user
	 * @return
	 */
	void saveToDataMagic(User user) throws Exception;

	/**
	 * 批量删除数据魔方中的用户
	 * @param userList
	 * @return
	 * @throws Exception
	 */
	List<User> deleteFromDataMagic(List<User> userList) throws Exception;


	/**
	 * 根据id更新用户角色
	 * @param id
	 * @param role
	 * @return
	 */
	public abstract User updateDataMagicRole(String id,String role);

	/**
	 * 根据用户信息组装用户
	 * @param userInfo
	 * @return
	 */
	User getUser(Map<String, Object> userInfo);

	/**
	 * 判断项目中用户是否存在
	 * @param username 用户名称
	 * @return
	 */
	boolean exists(String username);

	/**
	 * 创建用户并授权
	 * @param user
	 */
	void createUserAndDashboardsAuth(User user) throws Exception;

	/**
	 * 用户概览授权
	 * @param user
	 */
	void dashboardsAuth(User user);

	/**
	 * 校验用户是否有使用数据魔方的权限
	 * @param user
	 * @return
	 */
	boolean isValidDatamagicAuthority(User user);

	/**
	 * 校验用户是否有查看shop的权限
	 * @param user
	 * @param shopID
	 * @return
	 */
	boolean isValidShopAuthority(User user, String shopID);

	/**
	 * 查询当前项目下有多少用户
	 * @return
	 */
	int count();
}