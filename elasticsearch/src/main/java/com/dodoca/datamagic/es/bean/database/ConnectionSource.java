package com.dodoca.datamagic.es.bean.database;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 */
public final class ConnectionSource {
	private static BasicDataSource dataSource = null;

	/**
	 * 鐠囪褰囬弫鐗堝祦鎼存捇鍘ょ純顔芥瀮娴狅拷	 * 
	 * @return
	 */
	private static Properties getDBProperties() {
//		Properties pro = new Properties();
//		InputStream is = null;
//		is = ConnectionSource.class.getResourceAsStream("/onlineDB.properties");
//		try {
//			pro.load(is);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return pro;        
		return LoadConfigurationInfo.getInstance().getDBProperties();
	}

	public static void init() {
		if (dataSource != null) {
			try {
				dataSource.close();
			} catch (Exception e) {
			}
			dataSource = null;
		}
		try {
			Properties p = getDBProperties();
			dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * 閸忔娊妫存潻鐐村复濮癸拷	 */
	public static void shutdown() {
		try {
			dataSource.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized Connection getConnection() throws SQLException {
		if (dataSource == null) {
			init();
		}
		Connection conn = null;
		if (dataSource != null) {
			conn = dataSource.getConnection();
		}
		return conn;
	}
}
