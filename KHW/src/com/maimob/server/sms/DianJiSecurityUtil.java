package com.maimob.server.sms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DianJiSecurityUtil {
    
    public static Logger logger = LoggerFactory.getLogger(DianJiSecurityUtil.class);
    
    /**
     * MD5 32位 加密
     */
    public  static String getMD532Str(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //result = buf.toString().substring(8, 24);
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("getMD532Str NoSuchAlgorithmException e = {}",e);
        }
        return result;
    }
    
}
