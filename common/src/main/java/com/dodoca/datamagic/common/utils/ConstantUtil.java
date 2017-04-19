package com.dodoca.datamagic.common.utils;

import org.apache.log4j.Logger;

/**
 * Created by huhongda on 2016/11/14.
 */

public class ConstantUtil {

    private static Logger logger = Logger.getLogger(ConstantUtil.class);
    public static String PROJECT = "wxrrd_test";

    static {
        try {
            ConfigUtils.load("datamagic.properties");
            setProject();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    /**
     * 设置project
     * @throws Exception
     */
    public static void setProject() {

        PROJECT = ConfigUtils.getValue("project");

    }

    /**
     * 获取系统默认项目的配置值
     * @param key
     * @return
     */
    public static String getProjectValue(String key,String project) {
        return ConfigUtils.getValue(project + "." + key);
    }
    public static String getProjectValue(String key) {
        return getProjectValue(key,ConstantUtil.PROJECT);
    }

    /**
     * 获取系统默认项目的配置值
     * @param key
     * @return
     */
    public static String[] getProjectArray(String key) {
        return ConfigUtils.getArray(PROJECT + "." + key);
    }

    /**
     * 获取配置值
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return ConfigUtils.getValue(key);
    }

    /**
     * 获取配置值
     * @param key
     * @return
     */
    public static String[] getArray(String key) {
        return ConfigUtils.getArray(key);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(ConstantUtil.PROJECT);
        ConfigUtils.load("datamagic.properties");
        ConstantUtil.setProject();
        System.out.println(ConstantUtil.PROJECT);
    }
}
