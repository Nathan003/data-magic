package com.dodoca.datamagic.es.bean.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果映射�?
 * @author David Day
 */
public interface ResultConverter<T> {
	
	/**
	 * 结束映射
	 * @param rs 结果�?
	 * @return 映射结果
	 * @throws SQLException
	 */
	public T convert(ResultSet rs) throws SQLException ;

}