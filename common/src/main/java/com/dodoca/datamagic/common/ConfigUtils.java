package com.dodoca.datamagic.common;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Created by lifei on 2017/3/13.
 */
public class ConfigUtils {

    private static Logger logger = Logger.getLogger(ConfigUtils.class);

    public static CompositeConfiguration config = new CompositeConfiguration();
    public static final String SEPARATER = ",";

    /**
     * 加载properties文件
     *
     * @param path
     * @throws Exception
     */
    public static void load(String path) throws Exception {
        try {
            config.addConfiguration(new PropertiesConfiguration(path));
            // 当文件的内容发生改变时，配置对象也会刷新
        } catch (ConfigurationException e) {
            logger.debug("文件" + path + "不存在");
            throw new Exception("文件" + path + "不存在");
        }

//        DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder(path);
//        config = builder.getConfiguration(true);
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        String value = config.getString(key);
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        return value.trim();
    }

    public static String[] getArray(String key) {
        return config.getStringArray(key);
    }

    public static void main(String[] args) throws Exception {
        ConfigUtils.load("datamagic.properties");
        ConfigUtils.load("log4j.properties");
        String[] arr = config.getStringArray("default.project");
        for (String str : arr) {
            System.out.println(str);
        }
    }


}
