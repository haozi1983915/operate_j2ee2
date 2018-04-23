package com.maimob.server.controller.backend;

import com.maimob.server.base.BasicPage;
import com.maimob.server.base.BasicRequest;
import com.maimob.server.base.BasicResponse;
import com.maimob.server.controller.BaseController;
import com.maimob.server.db.dto.OperateActionDto;
import com.maimob.server.db.dto.OperateLoanDto;
import com.maimob.server.db.entity.OperateActionIdcard;
import com.maimob.server.db.entity.OperateActionLoan;
import com.maimob.server.db.entity.OperateActionSubmit;
import com.maimob.server.db.entity.OperateReportFormAppToday;
import com.maimob.server.enums.MsgCodeEnum;
import com.maimob.server.service.MicroService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yang on 2018/4/18.
 */
@Controller
@RequestMapping("/op/micro")
@CrossOrigin(origins="*", maxAge = 3600)
@Api(value="埋点数据", description = "埋点数据")
public class MicroPointController extends BaseController {

    @Autowired
    private MicroService microService;

    @RequestMapping(value = "/summary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value="整体概况",httpMethod="POST",notes="整体概况",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BasicResponse summary(@RequestBody BasicRequest request){
        /*BasicResponse validator = BasicResponse.validator(request);
        if(validator != null){
            return validator;
        }*/
        BasicPage<OperateReportFormAppToday> page = this.setPage(request.getPageNo(), request.getPageSize());
        List<OperateReportFormAppToday> results =  microService.summary(page, request);
        if(CollectionUtils.isEmpty(results)){
            return BasicResponse.fail(MsgCodeEnum.NO_DATA);
        }
        page.setList(results);
        return BasicResponse.result(MsgCodeEnum.SUCCESS, page);
    }

    @RequestMapping(value = "/applyRetention", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value="注册申请留存",httpMethod="POST",notes="注册申请留存",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BasicResponse applyRetention(@RequestBody BasicRequest request){
        /*BasicResponse validator = BasicResponse.validator(request);
        if(validator != null){
            return validator;
        }*/
        BasicPage<OperateActionIdcard> page = this.setPage(request.getPageNo(), request.getPageSize());
        List<OperateActionIdcard> results =  microService.applyRetention(page, request);
        if(CollectionUtils.isEmpty(results)){
            return BasicResponse.fail(MsgCodeEnum.NO_DATA);
        }
        page.setList(results);
        return BasicResponse.result(MsgCodeEnum.SUCCESS, page);
    }

    @RequestMapping(value = "/openDistribution", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value="开户分布",httpMethod="POST",notes="开户分布",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BasicResponse openDistribution(@RequestBody BasicRequest request){
        /*BasicResponse validator = BasicResponse.validator(request);
        if(validator != null){
            return validator;
        }*/
        BasicPage<OperateActionDto> page = this.setPage(request.getPageNo(), request.getPageSize());
        List<OperateActionDto> results =  microService.openDistribution(page, request);
        if(CollectionUtils.isEmpty(results)){
            return BasicResponse.fail(MsgCodeEnum.NO_DATA);
        }
        page.setList(results);
        return BasicResponse.result(MsgCodeEnum.SUCCESS, page);
    }

    @RequestMapping(value = "/openRetention", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value="申请开户留存",httpMethod="POST",notes="注册申请留存",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BasicResponse openRetention(@RequestBody BasicRequest request){
        /*BasicResponse validator = BasicResponse.validator(request);
        if(validator != null){
            return validator;
        }*/
        BasicPage<OperateActionSubmit> page = this.setPage(request.getPageNo(), request.getPageSize());
        List<OperateActionSubmit> results =  microService.openRetention(page, request);
        if(CollectionUtils.isEmpty(results)){
            return BasicResponse.fail(MsgCodeEnum.NO_DATA);
        }
        page.setList(results);
        return BasicResponse.result(MsgCodeEnum.SUCCESS, page);
    }

    @RequestMapping(value = "/loanDistribution", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value="提现分布",httpMethod="POST",notes="提现分布",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BasicResponse loanDistribution(@RequestBody BasicRequest request){
       /*BasicResponse validator = BasicResponse.validator(request);
        if(validator != null){
            return validator;
        }*/
        BasicPage<OperateLoanDto> page = this.setPage(request.getPageNo(), request.getPageSize());
        List<OperateLoanDto> results =  microService.loanDistribution(page, request);
        if(CollectionUtils.isEmpty(results)){
            return BasicResponse.fail(MsgCodeEnum.NO_DATA);
        }
        page.setList(results);
        return BasicResponse.result(MsgCodeEnum.SUCCESS, page);
    }

    @RequestMapping(value = "/loanRetention", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value="开户提现留存",httpMethod="POST",notes="开户提现留存",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BasicResponse loanRetention(@RequestBody BasicRequest request){
        /*BasicResponse validator = BasicResponse.validator(request);
        if(validator != null){
            return validator;
        }*/
        BasicPage<OperateActionLoan> page = this.setPage(request.getPageNo(), request.getPageSize());
        List<OperateActionLoan> results =  microService.loanRetention(page, request);
        if(CollectionUtils.isEmpty(results)){
            return BasicResponse.fail(MsgCodeEnum.NO_DATA);
        }
        page.setList(results);
        return BasicResponse.result(MsgCodeEnum.SUCCESS, page);
    }


}