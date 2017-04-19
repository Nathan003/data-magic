package com.dodoca.datamagic.common.utils;

import com.dodoca.dataMagic.common.model.BaseResponse;
import com.dodoca.dataMagic.common.model.User;
import org.apache.log4j.Logger;

/**
 * Created by lifei on 2017/2/14.
 */
public class ValidUtils {

    private static Logger logger = Logger.getLogger(ValidUtils.class);

    public static boolean validLogin(String username, String token) {
        if (StringUtils.isEmpty(token) || !token.equals(DataMagicUtil.getToken(username))) {
            logger.debug("token:" + token + ",系统token:" + DataMagicUtil.getToken(username));
            return false;
        }
        return true;
    }

    /**
     * 校验用户对书签是否有查看权限
     * @param user
     * @param bookmarkID
     * @return
     */
    public static boolean isValidBookMarkAuthority(User user, String bookmarkID) {
        if(null == user){
            return false;
        }
        BaseResponse bookmarkResponse = DataMagicUtil.getBookmark(bookmarkID, user.getUsername());
        if (bookmarkResponse.getStatus() != 200) {
            return false;
        }
        return true;
    }
}
