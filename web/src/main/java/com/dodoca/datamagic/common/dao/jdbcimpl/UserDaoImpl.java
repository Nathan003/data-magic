package com.dodoca.datamagic.common.dao.jdbcimpl;

import com.dodoca.datamagic.common.dao.UserDao;
import com.dodoca.datamagic.common.service.impl.UserServiceBase;
import com.dodoca.datamagic.utils.model.User;
import com.dodoca.datamagic.utils.StringUtils;
import com.mysql.jdbc.Statement;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private static Logger log = Logger.getLogger(UserServiceBase.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> get() {
        String sql = "select * from magic_cube.user order by username";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class));
        if (users.isEmpty()) {
            log.debug("用户数量为0");
            return null;
        } else {
            log.debug("查询所有用户数据成功");
            return users;
        }
    }

    public User getById(String id) {

        String sql = "select * from magic_cube.user " + " where " + "user.id =  " + id;
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class));
        if (users.isEmpty()) {
            log.debug("通过id没有查询到该用户");
            return null;
        } else {
            log.debug("根据用户id查询用户成功");
            return users.get(0);
        }

    }

    /**
     * 批量插入数据
     */
    public boolean save(List<User> userList){
        final List<User> tempUserList = userList;
        String sql = "insert into magic_cube.user(username,role,shop_id,shop_name,shop_cate1_id,shop_cate1_name" +
                ",shop_cate2_id,shop_cate2_name,shop_line_id,shop_line) values(?,?,?,?,?,?,?,?,?,?)";
        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

                public void setValues(PreparedStatement pstm, int i) throws SQLException {
                    try {
                        setIntoPstm(pstm, tempUserList.get(i));
                    } catch (Exception e) {
                        log.debug("批量插入用户数据错误");
                    }
                }

                public int getBatchSize() {
                    return tempUserList.size();
                }
            });
            log.debug("批量插入成功");
            return true;
        } catch (Exception e) {
            //回滚
            log.debug("批量插入失败 错误信息：" + e);
            return false;
        }
    }

    public static PreparedStatement setIntoPstm(PreparedStatement pstm, User user) {
        //默认都不插入主键id,如果
        try {
            pstm.setObject(1, user.getUsername());
            pstm.setObject(2, user.getPassword());
            pstm.setObject(3, user.getRole());
            pstm.setObject(4, "".equals(user.getShopId()) ? null : user.getShopId());
            pstm.setObject(5, user.getShopLineId());
            pstm.setObject(6, user.getProject());
        } catch (Exception e) {

        }
        return pstm;
    }

    public User getByName(String username, String project) {
        String sql = "select * from magic_cube.user as user" +
                " where user.username='" + username + "' and user.project = '"+project+"' ";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class));
        if (users.isEmpty()) {
            log.debug("用户数量为0");
            return null;
        } else {
            log.debug("查询分页用户数据成功");
            return users.get(0);
        }
    }

    public List<User> get(int rows, int page, String project) {
        int begin = rows * (page -1 );
        String sql = "select * from magic_cube.user where project='" + project + "' order by username"
                + " limit " + begin + "," + rows;
        log.debug(sql);
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class));
        return users;
    }

    //更新数据
    public boolean update(User user) {
    	StringBuffer sql = new StringBuffer("update magic_cube.user set ");
    	// 选择性 更新
    	if(!StringUtils.isEmpty(user.getUsername())){
    		sql.append(" user.username = '"+user.getUsername()+"'");
    	}
    	if(!StringUtils.isEmpty(user.getPassword())){
    		sql.append(", user.password = '"+user.getPassword()+"'");
    	}
    	if(!StringUtils.isEmpty(user.getShopId())){
    		sql.append(", user.shop_id = '"+user.getShopId()+"'");
    	}
    	// 角色正确 -1,0,1,2
    	if(user.getRole() >= -1 && user.getRole() <= 2){
    		sql.append(", user.role = "+user.getRole());
    	}
    	if(!StringUtils.isEmpty(user.getShopLineId())){
    		sql.append(", user.shop_line_id = "+user.getShopLineId());
    	}
    	if(!StringUtils.isEmpty(user.getId())){
    		sql.append(" where user.id = "+user.getId());
    	}
    	
        //更新的数据有 password, role(0 神策管理员,1 神策分析师,2 神策普通用户) shop_line_id 
        jdbcTemplate.update(sql.toString());
        return true;
    }

    /**
     * 返回插入的用户的主键，都不插入主键id
     */
    public String save(User user) {
        final User tempUser = user;
        log.debug("插入单条user数据");
        //默认为插入 有id的user
        final String sql = "insert into magic_cube.user(username,password,role,shop_id,shop_line_id,project) values(?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(
                    java.sql.Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                try {
                    setIntoPstm(ps, tempUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ps;
            }
        }, keyHolder);
        log.debug("主键 :" + keyHolder.getKey().toString());
        return keyHolder.getKey().longValue() + "";
    }

    //模糊查询
    public List<User> getByBlurName(String username) {
        String sql = "select * from magic_cube.user u "
                + "where u.username like '%" + username + "%' ";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class));
        if (users.isEmpty()) {
            log.debug("用户数量为0");
            return null;
        } else {
            log.debug("模糊查询用户数据成功");
            return users;
        }
    }

    public int count(String project) {
        String sql = "select count(1) from magic_cube.user where project ='" + project + "'";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    public boolean delete(List<User> userList) {
        final List<User> tempUserList = userList;
        String sql = "delete from magic_cube.user where user.id  = ?";
        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement pstm, int i)
                        throws SQLException {
                    try {
                        pstm.setObject(1, tempUserList.get(i).getId());
                    } catch (Exception e) {
                        log.debug("批量删除用户数据错误");
                    }
                }

                public int getBatchSize() {
                    return tempUserList.size();
                }
            });
            log.debug("批量删除成功");
            return true;
        } catch (Exception e) {
            log.debug("批量删除失败" + e);
            return false;
        }
    }

    public static void main(String[] args) {
        String str = null;
        System.out.println("a" + str + "b");
    }
}
