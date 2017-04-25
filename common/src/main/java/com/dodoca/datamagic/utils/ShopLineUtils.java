package com.dodoca.datamagic.utils;


import com.dodoca.datamagic.utils.model.ShopLine;

import java.sql.*;

public class ShopLineUtils {
	/**
	 * 从resultset中 取值，填充shopLineShop
	 * @param rs
	 * @param shopLine
	 * @throws Exception
	 */
	public static ShopLine setIntoShopLine(ResultSet rs, ShopLine shopLine) throws Exception{
		shopLine.setShopLineId(rs.getString("shop_line_id"));
		shopLine.setShopLine(rs.getString("shop_line"));
		
		return shopLine;
	}
	
	/**
	 * 给PreparedStatement注入参数
	 * @param pstm
	 * @param shopLine
	 * @return
	 * @throws Exception
	 */
	public static PreparedStatement setIntoPstm(PreparedStatement pstm,ShopLine shopLine) throws Exception{
		
		pstm.setObject(1, shopLine.getShopLineId());
		pstm.setObject(2, shopLine.getShopLine());
		return pstm;
	}
	
	/**
	 * 根据connection和sql返回result
	 * @param connection
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static  ResultSet query(Connection connection, String sql) throws SQLException {
		
        if (null == connection || null == sql) {
            return null;
        }
        ResultSet resultSet = null;
        Statement statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);
        return resultSet;
    }
	
	/**
	 * 根据属性值 查询user对象
	 * @param property 根据什么属性进行查询
	 * @param value 属性传入的值
	 * @return user对象
	 * @throws Exception
	 */
	public static ShopLine selectShopLineByProperty(String property,Object value)throws Exception{
		ShopLine shopLine = new ShopLine();
//		Connection connection = DBUtil.getConnection();
//		ResultSet rs = query(connection,"select * from shop_line_shop s "+" where "+"s."+property+" = "+value);
//		
//		while(rs.next()){
//			setIntoShopLine(rs, shopLine);
//		}
//		rs.close();
//		connection.close();
		return shopLine;
	}
}
