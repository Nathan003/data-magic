package com.dodoca.datamagic.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by lifei on 2016/12/28.
 */
public class StringUtils {

    /**
     * 去除前后空格后，判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(null == str || str.trim().length() == 0){
            return true;
        }

        return false;
    }

    /**
     * 汉字转换位汉语拼音，英文字符不变
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines){
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines){
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    /**
     * 获取用户名，将用户名中的中文转为拼音并且将出字母数字下划线外的其他字符转成下划线
     * @param username
     * @return
     */
    public static String getUsername(String username){
        if(isEmpty(username)){
            return null;
        }
        return converterToSpell(username).replaceAll("[^a-zA-Z0-9]","_");
    }

    /**
     * 将Object转成字符串，null仍然返回null
     * @param obj
     * @return
     */
    public static  String objectToString(Object obj){
        if(null == obj){
            return null;
        }else {
            return obj.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println(converterToSpell("st_M-FUN").replaceAll("[^a-zA-Z0-9_]","_"));
    }
}
