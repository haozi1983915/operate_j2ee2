package com.maimob.server.base;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.enums.MsgCodeEnum;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.jpamodelgen.xml.jaxb.Basic;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;

/**
 * Created by yang on 2018/4/18.
 */
@ApiModel
public class BasicResponse<Data> {

    @Autowired
    private static DaoService dao;

    @ApiModelProperty(value = "状态码")
    protected int status; //

    @ApiModelProperty(value = "msg")
    protected String statusMsg;

    protected String sessionId;

    protected Data data;

    public BasicResponse() {
        try {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            Class<Data> clazz = (Class<Data>) parameterizedType.getActualTypeArguments()[0];
            data = clazz.newInstance();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public BasicResponse(int status) {
        super();

        this.status = status;
    }


    public BasicResponse(int status, String statusMsg) {
        this(status);
        this.statusMsg = statusMsg;

    }

    public BasicResponse(int status, String statusMsg, Data data) {
        this(status, statusMsg);

        this.data = data;
    }

    public static <Data> BasicResponse<Data> success() {
        BasicResponse<Data> response = new BasicResponse<Data>(0, "成功！");

        return response;
    }

    public static <Data> BasicResponse<Data> success(Data data) {
        BasicResponse<Data> response = new BasicResponse<Data>(0, "成功！");
        response.setData(data);

        return response;
    }

    public static <Data> BasicResponse<Data> success(String statusMsg) {
        BasicResponse<Data> response = new BasicResponse<Data>(0, statusMsg);
        response.setData(null);

        return response;
    }

    public static <Data> BasicResponse<Data> success(int status, String statusMsg, Data data) {
        BasicResponse<Data> response = new BasicResponse<Data>(status, statusMsg);
        response.setData(data);

        return response;
    }

    public static <Data> BasicResponse<Data> success(String statusMsg, Data data) {
        BasicResponse<Data> response = new BasicResponse<Data>(0, statusMsg);
        response.setData(data);

        return response;
    }

    public static <Data> BasicResponse<Data> result(MsgCodeEnum codeEnum) {
        BasicResponse<Data> response = new BasicResponse<Data>(codeEnum.getCode(), codeEnum.getDesc());

        return response;
    }

    public static <Data> BasicResponse<Data> result(MsgCodeEnum codeEnum, Data data) {
        BasicResponse<Data> response = BasicResponse.result(codeEnum);
        response.setData(data);
        return response;
    }

    public static <Data> BasicResponse<Data> fail(MsgCodeEnum codeEnum) {
        BasicResponse<Data> response = new BasicResponse<Data>(codeEnum.getCode(), codeEnum.getDesc());
        response.setData(null);

        return response;
    }

    public static <Data> BasicResponse<Data> fail(int status, Data data) {
        BasicResponse<Data> response = new BasicResponse<Data>(status, null);
        response.setData(data);

        return response;
    }

    public static <Data> BasicResponse<Data> fail(int status, String statusMsg) {
        BasicResponse<Data> response = new BasicResponse<Data>(status, statusMsg);
        response.setData(null);

        return response;
    }

    public static <Data> BasicResponse<Data> fail(int status, String statusMsg, Data data) {
        BasicResponse<Data> response = new BasicResponse<Data>(status, statusMsg);
        response.setData(data);

        return response;
    }

    public static BasicResponse validator(BasicRequest request){
        BasicResponse response = new BasicResponse();
        //String json = checkParameter(request);
        if (request == null || request.getSessionId() == null) {
            return BasicResponse.fail(MsgCodeEnum.NO_PARAM);
        }
        String adminid = request.getSessionId();

        Admin admin = getAdmin(adminid);
        if (admin == null) {
            response.setStatus(1);
            response.setStatusMsg("请重新登录");
            return response;
        }
        return null;
    }

    private static Admin getAdmin(String adminid) {
        Cache.AdminCatche(dao);
        if (StringUtils.isStrEmpty(adminid))
            return null;

        Admin admin = Cache.getAdminCatche(Long.parseLong(adminid));
        if (admin != null) {

            if (System.currentTimeMillis() - admin.getLoginDate() > 7200000) {
                admin = null;
            } else {
                admin.setLoginDate(System.currentTimeMillis());
            }
        }
        return admin;
    }

    private static String checkParameter(HttpServletRequest request) {
        String value = "";
        try {
            request.setCharacterEncoding("UTF-8");
            String uri = request.getRequestURI();

            StringBuffer sb = new StringBuffer();
            String line = null;
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null)
                    sb.append(line);
            } catch (Exception e) {
                e.printStackTrace();
            }

            value = sb.toString();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
