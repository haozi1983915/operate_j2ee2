package com.maimob.server.utils;

import java.io.IOException;
import java.security.MessageDigest;

import com.maimob.server.encoder.BASE64Decoder;
import com.maimob.server.encoder.BASE64Encoder;


/**
 * @author 宋超.
 * @version 1.1.1
 * @project Ledaikuan
 * @date 2017/3/10
 */

public class PWDUtils {

    private static final char[] DIGITS = new char[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };

    private static final String SUFFIX = "2sdf65fw5ef513as2d56ew5f1sd2fw";

    /**
     * MD5
     * 
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            str = bytes2HexString(messageDigest.digest());
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bytes2HexString(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer(32);
        int j = bytes.length;
        int i = 0;
        while (i < j) {
            int k = bytes[i];
            char c = DIGITS[(k & 0xF)];
            stringBuffer.append(DIGITS[((k & 0xF0) >>> 4)]);
            stringBuffer.append(c);
            i += 1;
        }
        return stringBuffer.toString();
    }

    /**
     * 加密算法
     * 
     * @param pwd
     * @return
     */
    public static String encryptMD5AndBase64(String pwd) {
        return encryptBase64(md5(pwd) + SUFFIX);
    }

    /**
     * The Base64 Content-Transfer-Encoding is designed to represent arbitrary
     * sequences of octets in a form that need not be humanly readable.
     * 
     * @param str
     * @return
     */
    public static String encryptBase64(String str) {
        String result = str;
        if (!StringUtils.isStrEmpty(str)) {
            try {
                result = new BASE64Encoder().encode(str.getBytes("utf-8")).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String decryptBASE64(String key) {
        byte[] bt;
        try {
            bt = (new BASE64Decoder()).decodeBuffer(key);
            return new String(bt, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
