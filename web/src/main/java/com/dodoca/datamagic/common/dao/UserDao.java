package com.dodoca.datamagic.common.dao;

import com.dodoca.datamagic.utils.model.User;

import java.util.List;

public interface UserDao {

	/**
	 * 获取用户数据
	 * @return
	 * @throws Exception
	 */
	public abstract List<User> get();

	/**
	 * 根据ID获取用户数据
	 * @param id
	 * @return
	 */
	public abstract User getById(String id);

	/**
	 * 根据用户名和项目名称 获得用户相关的数据
	 * @param username 用户名称
	 * @param project 项目名称
	 * @return
	 */
	public abstract User getByName(String username, String project);


	/**
	 * 保存数据
	 * @param user
	 * @return
	 */
	public abstract String save(User user);

	/**
	 * 批量保存数据
	 * @param userList
	 * @return
	 */
	public abstract boolean save(List<User> userList);

	/**
	 * 更新数据
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
	 * 分页查询
	 * @param rows
	 * @param page
	 * @param project
	 * @return
	 */
	public abstract List<User> get(int rows, int page,String project);

	/**
	 * 根据用户名模糊查询
	 * @param username
	 * @return
	 */
	public abstract List<User> getByBlurName(String username);

	/**
	 * 获取指定项目下的用户数
	 * @param project
	 * @return
	 */
	int count(String project);
}
