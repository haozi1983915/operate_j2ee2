package com.maimob.server.utils;

import java.util.ArrayList;

public class StringUtils {

    /**
     * 字符串补齐
     * @param source 源字符串
     * @param fillLength 补齐长度
     * @param fillChar 补齐的字符
     * @param isLeftFill true为左补齐，false为右补齐
     * @return
     */
    public static String stringFill(String source, int fillLength, char fillChar, boolean isLeftFill) {
        if (source == null || source.length() >= fillLength) return source;
         
        StringBuilder result = new StringBuilder(fillLength);
        int len = fillLength - source.length();
        if (isLeftFill) {
            for (; len > 0; len--) {
                result.append(fillChar);
            }
            result.append(source);
        } else {
            result.append(source);
            for (; len > 0; len--) {
                result.append(fillChar);
            }
        }
        return result.toString();
    }
    
    /**
     * 去掉字符串中的空格
     * @param str
     * @return
     */
    public static String removeSpace(String str){
        if(str == null) return str;
        return str.trim().replaceAll(" ","");
    }
    
    /**
     * 判断是否为空
     * @param str
     * @return
     */
    public static boolean isStrEmpty(String str){
        if(str == null)return true;
        String removeSpaceStr = removeSpace(str);
        return removeSpaceStr.isEmpty() || removeSpaceStr.equals("")
                ||removeSpaceStr.equals("null");
    }

    
    /**
     * 截取字符串
     * @param s
     * @param from
     * @return
     */
    protected static String substring(String s, int from) {
        try {
            return s.substring(from);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    protected static String substring(String s, int from, int len) {
        try {
            return s.substring(from, from + len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断一个字符串是否在一个字符串数组中
     * @param target
     * @param arr
     * @return
     */
    protected static boolean inArray(String target, String[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        if (target == null) {
            return false;
        }
        for (String s : arr) {
            if (target.equals(s)) {
                return true;
            }
        }
        return false;
    }

    
    public static String getNotNullString(String str){
        return isStrEmpty(str)?"":str;
    }
    
    public static String ArrayList2String(ArrayList<String> input){
        if(input == null)return null;
        StringBuilder builder = new StringBuilder();
        int length = input.size();
        for(int i = 0;i<length;i++){
            if(i == length-1){
                builder.append(input.get(i));
            }else{
                builder.append(input.get(i)).append(",");
            }
        }
        return builder.toString();
    }
    
    public static String replacePathWithUrl(String path){
        if(path == null)return null;
        if(path.contains("/home/maimob/Uploads/")){
            return path.replace("/home/maimob/Uploads/", "http://106.14.20.118:8080/MaiMobLoan/file/");
        }else{
            return path.replace("/mnt/Uploads/", "http://106.14.20.118:8080/MaiMobLoan/file/");
        }
        
    }
    
    public static String getSecretMobileNo(String mobileNo){
        if(isStrEmpty(mobileNo))return mobileNo;
        if(mobileNo.length() >7){
            return mobileNo.substring(0,3) + "****"+ mobileNo.substring(7, mobileNo.length());
        }else{
            return mobileNo;
        }
    }

}
