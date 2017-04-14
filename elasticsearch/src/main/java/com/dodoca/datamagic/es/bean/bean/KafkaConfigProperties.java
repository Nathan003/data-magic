package com.dodoca.datamagic.es.bean.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * Kafka Consumer Config Properties
 * 
 * Singleton
 * 
 * @author Wang yicheng
 *
 */
public class KafkaConfigProperties {
	
	private static final String CONF_TYPE = "/test_kafka.properties";
	private static Properties properties = null;
	
	
	/**
	 * 获取Consumer 连接 配置信息,加载配置对象
	 * @return
	 * @throws IOException
	 */
	public static synchronized Properties getConsumerProperties() throws IOException{
		
		if(properties==null){
			properties = new Properties();
			InputStream is = KafkaConfigProperties.class.getResourceAsStream(CONF_TYPE);
			properties.load(is);
		}
		
		return properties;
			
	}

}
