package com.maimob.server.controller.proxy;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.base.BasicRequest;
import com.maimob.server.base.BasicResponse;
import com.maimob.server.controller.BaseController;
import com.maimob.server.db.entity.OperateReportFormAppToday;
import com.maimob.server.enums.MsgCodeEnum;
import com.maimob.server.service.MicroService;
import org.hibernate.jpamodelgen.xml.jaxb.Basic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yang on 2018/4/18.
 */
@Controller
@RequestMapping("/micro")
@CrossOrigin(origins="*", maxAge = 3600)
public class MicroPointController extends BaseController {

    @Autowired
    private MicroService microService;

    @RequestMapping(value = "/summary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String summary(BasicRequest request){
        /*BasicResponse validator = BasicResponse.validator(request);
        if(validator != null){
            return JSONObject.toJSONString(validator);
        }*/

        List<OperateReportFormAppToday> results =  microService.summary(request);
        if(CollectionUtils.isEmpty(results)){
            return JSONObject.toJSONString(BasicResponse.fail(MsgCodeEnum.NO_DATA));
        }
        return JSONObject.toJSONString(BasicResponse.result(MsgCodeEnum.SUCESS, results));
    }

    @RequestMapping(value = "/applyRetention", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String applyRetention(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "/openDistribution", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String openDistribution(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "/openRetention", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String openRetention(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "/loanDistribution", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String loanDistribution(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "/loanRetention", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String loanRetention(HttpServletRequest request){
        return null;
    }


}
