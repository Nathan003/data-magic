package com.dodoca.datamagic.common.utils;

import com.dodoca.datamagic.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huhongda on 2016/11/14.
 */

@Component
public class ConstantUtil {

    private static Logger logger = Logger.getLogger(ConstantUtil.class);
    public static final String PROJECT = "wxrrd_test_product_new";
//    public static final String PROJECT = "dd_datamagic";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static ConstantUtil constantUtil;
    public static Map<String, Object> SYS_PARAM;
    public static final String SEPARATER = ",";

    @PostConstruct
    public void init() {
        constantUtil = this;
        constantUtil.jdbcTemplate = this.jdbcTemplate;
        loadDB();
    }

    public static void loadDB() {
        String sql = "select `key`,`value` from magic_cube.t_sys_param where deleted = 0";
        List list = constantUtil.jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            Map<String, Object> rs = new HashMap<String, Object>();

            public Map mapRow(ResultSet resultSet, int i) throws SQLException {
                String key = resultSet.getString("key");
                String value = resultSet.getString("value");
                if (!StringUtils.isEmpty(key) && key.endsWith("_dashvoards") && !StringUtils.isEmpty(value)) {
                    rs.put(key, value.replaceAll(" ", "").split(SEPARATER));
                } else {
                    rs.put(key, value);
                }
                return rs;
            }
        });
        if (list.size() > 0) {
            SYS_PARAM = (Map<String, Object>) list.get(list.size() - 1);
        }
    }

    /**
     * 根据key从数据库获取数据
     * @param key
     * @return
     */
    public static String getValueToDB(String key){
        String sql = "select `value` from magic_cube.t_sys_param where `key` = '" + key + "' and deleted = 0";
        try {
            return constantUtil.jdbcTemplate.queryForObject(sql, String.class);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 从数据库中获取项目参数，在获取数据时，使用项目名.key获取数据
     *
     * @param key
     * @return
     */
    public static String getProjectValueToDB(String key) {
        return getValueToDB(PROJECT + "." + key);
    }

    /**
     * 从数据库中获取项目参数，在获取数据时，使用项目名.key获取数据
     *
     * @param key
     * @return
     */
    public static String[] getProjectArrayToDB(String key) {
        String value = getProjectValueToDB(key);
        if(StringUtils.isEmpty(value)){
            return new String[]{};
        }
        return value.split(SEPARATER);
    }


    /**
     * 获取项目参数，在获取数据时，使用项目名.key获取数据
     *
     * @param key
     * @return
     */
    public static String getProjectValue(String key) {
        return (String)SYS_PARAM.get(PROJECT + "." + key);
    }

    /**
     * 获取项目参数，在获取数据时，使用项目名.key获取数据
     *
     * @param key
     * @return
     */
    public static String[] getProjectArray(String key) {
        return (String[]) SYS_PARAM.get(PROJECT + "." + key);
    }

    /**
     * 获取参数，在获取数据时，使用key获取数据
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return (String)SYS_PARAM.get(key);
    }

    /**
     * 获取参数，在获取数据时，使用key获取数据
     *
     * @param key
     * @return
     */
    public static String[] getArray(String key) {
        return (String[]) SYS_PARAM.get(key);
    }

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                new String[]{"spring/applicationContext-dao.xml", "spring/applicationContext-service.xml"});
        ConstantUtil cu = (ConstantUtil) ac.getBean("constantUtil");
//        cu.loadDB();
        System.out.println(getArray("wxrrd_test_product_new.platform_dashvoards"));
    }
}
