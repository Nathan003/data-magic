package com.dodoca.datamagic.utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by lifei on 2017/1/9.
 */
public class SecurityUtil {

    private static Logger logger = Logger.getLogger(SecurityUtil.class);
    private static String key = "9e7373ab5b745971";

    /**
     * AES加密
     * @param input
     * @param key
     * @return
     */
    public static String encrypt(String input, String key){

        if(null == input){
            return null;
        }
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            return new String(Base64.encodeBase64(cipher.doFinal(input.getBytes())));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     * @param input
     * @param key
     * @return
     */
    public static String decrypt(String input, String key){
        if(null == input){
            return null;
        }
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            System.out.println(Cipher.DECRYPT_MODE);
            cipher.init(Cipher.DECRYPT_MODE, skey);
            return new String(cipher.doFinal( Base64.decodeBase64(input)));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String input){
        return decrypt(input,key);
    }

    public static void main(String[] args) {
//        String key = "9e7373ab5b745971";
//        String data = "{\"userinfo\":{\"shop_line_id\":-1,\"username\":\"lizhenzhu@dodoca.com\"},\"created_at\":\"2017-01-19 14:08:20\"}";
//
//        System.out.println(SecurityUtil.encrypt(data, key));
//
//        System.out.println(SecurityUtil.decrypt(SecurityUtil.encrypt(data, key), key));
//        {"token":"aSW+xYrGXiUnkcUxVo4Abegm+wbDgBgqNh0OgTMmq82TDPolOvxFraSnG2bY7tA4Tucd1juojK/HGvTDH7t2AGgKBwWECZbsrq80Zi+2HD+VC9yFAwXL8eAmNWtG0XV4918R/SU9QcYkDMsQOWivaw=="}

        String str = "aSW+xYrGXiUnkcUxVo4Abegm+wbDgBgqNh0OgTMmq82TDPolOvxFraSnG2bY7tA4Tucd1juojK/HGvTDH7t2AGgKBwWECZbsrq80Zi+2HD+VC9yFAwXL8eAmNWtG0XV4918R/SU9QcYkDMsQOWivaw==";
        System.out.println("aa" + decrypt(str) + "bb");

    	str = "{\"userinfo\":{\"shop_id\":\"1230\",\"username\":\"admin_1230_1230\"},\"created_at\":\"2017-03-16 14:57:01\"}";
        System.out.println(encrypt(str,key));
    }
}

