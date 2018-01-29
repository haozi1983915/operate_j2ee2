package com.maimob.server.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMSUtils {

    private static final String account = "maiguang106";
    
    private static final String password = "LoBhEdzY";
    
    private static final String url = "http://139.129.128.71:8086/msgHttp/json/mt";
    
    public static final String testTemplet = "尊敬的用户，您本次手机验证码是：559264，请在验证页面输入该数字。";
    
    private static Logger logger = LoggerFactory.getLogger(SMSUtils.class);
    
    public static String send(String mobile,String content){
        String resultContent = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();  
        long timestamps = System.currentTimeMillis();
        
        formparams.add(new BasicNameValuePair("account", account));
        formparams.add(new BasicNameValuePair("password", DianJiSecurityUtil.getMD532Str(password+mobile+timestamps)));
        formparams.add(new BasicNameValuePair("mobile", mobile));
        formparams.add(new BasicNameValuePair("content", content));
        formparams.add(new BasicNameValuePair("timestamps", timestamps+""));

        UrlEncodedFormEntity uefEntity;
        try {
            long start = System.currentTimeMillis();
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                resultContent = EntityUtils.toString(entity, "UTF-8");
            }
            long end = System.currentTimeMillis();
            logger.info("cost --> {}",(end -start));
            
        } catch (UnsupportedEncodingException e) {
            logger.error(" UnsupportedEncodingException {}",e);
        } catch (ClientProtocolException e) {
            logger.error(" ClientProtocolException {}",e);
        } catch (IOException e) {
            logger.error(" IOException {}",e);
        }finally {
            // 关闭连接,释放资源
            try {
                if(response != null){
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                logger.error(" IOException {}",e);
            }
        }
        return resultContent;
    }

    public static String query(){
        String resultContent = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        
        HttpPost httpPost = new HttpPost("http://139.129.128.71:8086/msgHttp/json/balance");
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();  
        long timestamps = System.currentTimeMillis();
        
        formparams.add(new BasicNameValuePair("account", account));
        formparams.add(new BasicNameValuePair("password", DianJiSecurityUtil.getMD532Str(password+timestamps)));
        formparams.add(new BasicNameValuePair("timestamps", timestamps+""));

        UrlEncodedFormEntity uefEntity;
        try {
            long start = System.currentTimeMillis();
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                resultContent = EntityUtils.toString(entity, "UTF-8");
            }
            long end = System.currentTimeMillis();
            logger.info("cost --> {}",(end -start));
            
        } catch (UnsupportedEncodingException e) {
            logger.error(" UnsupportedEncodingException {}",e);
        } catch (ClientProtocolException e) {
            logger.error(" ClientProtocolException {}",e);
        } catch (IOException e) {
            logger.error(" IOException {}",e);
        }finally {
            // 关闭连接,释放资源
            try {
                if(response != null){
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                logger.error(" IOException {}",e);
            }
        }
        return resultContent;
    }

    public static String parmsSend(String objjson){
        String resultContent = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        
        HttpPost httpPost = new HttpPost("http://139.129.128.71:8086/msgHttp/json/multimt");
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();  
        long timestamps = System.currentTimeMillis();
        
        formparams.add(new BasicNameValuePair("account", account));
        formparams.add(new BasicNameValuePair("password", DianJiSecurityUtil.getMD532Str(password+timestamps)));
        formparams.add(new BasicNameValuePair("objjson", objjson));
        formparams.add(new BasicNameValuePair("timestamps", timestamps+""));

        UrlEncodedFormEntity uefEntity;
        try {
            long start = System.currentTimeMillis();
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                resultContent = EntityUtils.toString(entity, "UTF-8");
            }
            long end = System.currentTimeMillis();
            logger.info("cost --> {}",(end -start));
            
        } catch (UnsupportedEncodingException e) {
            logger.error(" UnsupportedEncodingException {}",e);
        } catch (ClientProtocolException e) {
            logger.error(" ClientProtocolException {}",e);
        } catch (IOException e) {
            logger.error(" IOException {}",e);
        }finally {
            // 关闭连接,释放资源
            try {
                if(response != null){
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                logger.error(" IOException {}",e);
            }
        }
        return resultContent;
    }

    public static int getRandNum(int min, int max) {
        int randNum = min + (int)(Math.random() * ((max - min) + 1));
        return randNum;
    }

    public static int getSixDynamicPwd(){
        return (int)((Math.random()*9+1)*100000);
    }

    public static int getFourDynamicPwd(){
        return (int)((Math.random()*9+1)*1000);
    }
}
