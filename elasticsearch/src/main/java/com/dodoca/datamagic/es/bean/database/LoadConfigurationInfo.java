package com.dodoca.datamagic.es.bean.database;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadConfigurationInfo {
	private static Logger logger = Logger.getLogger(LoadConfigurationInfo.class);
	private static LoadConfigurationInfo singleton = null;
	private Properties dbProperties = new Properties();
	private Properties esProperties = new Properties();
	
	static{
		getInstance("db.properties", "es.properties");
	}
	
	public Properties getDBProperties() {
		return dbProperties;
	}

	public Properties getInterfaceProperties() {
		return esProperties;
	}

	private LoadConfigurationInfo(String fileNameOfDB, String fileNameOfInterface) {
		LoadDBProperty(fileNameOfDB);
		LoadInterfaceProperty(fileNameOfInterface);
	}

	public static LoadConfigurationInfo getInstance() {
		if (singleton == null) {
			logger.error("Properties singleton is null");
		}
		return singleton;
	}
	
	public static LoadConfigurationInfo getInstance(String fileNameOfDB, String fileNameOfInterface) {
		if (singleton == null) {
			singleton = new LoadConfigurationInfo(fileNameOfDB, fileNameOfInterface);
			logger.info(LoadConfigurationInfo.getInstance().getDBProperties().toString());
			logger.info(LoadConfigurationInfo.getInstance().getInterfaceProperties().get("zkConnect"));
		}
		return singleton;
	}

	public void reset(String fileNameOfDB, String fileNameOfInterface) {
		LoadDBProperty(fileNameOfDB);
		LoadInterfaceProperty(fileNameOfInterface);
	}

	private void LoadDBProperty(String fileNameOfDB) {
		
		InputStream is = LoadConfigurationInfo.class.getResourceAsStream("/" + fileNameOfDB);
		try {
			dbProperties.load(is);
		} catch (IOException e) {
			logger.error("SQL path.properties load fail " + e);
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("Load DB Properties error ");
			}
		}
	}

	private void LoadInterfaceProperty(String fileNameOfInterface) {
		InputStream is = LoadConfigurationInfo.class.getResourceAsStream("/" + fileNameOfInterface);
		try {
			esProperties.load(is);

		} catch (IOException e) {
			logger.error("File path.properties load fail " + e);
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("Load path Properties error ");
			}
		}
	}
}
