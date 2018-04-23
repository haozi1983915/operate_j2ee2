package com.maimob.server.service.impl;

import com.maimob.server.base.BasicPage;
import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.daoImpl.*;
import com.maimob.server.db.dto.OperateActionDto;
import com.maimob.server.db.dto.OperateLoanDto;
import com.maimob.server.db.entity.*;
import com.maimob.server.service.MicroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2018/4/18.
 */
@Service
public class MicroServiceImpl implements MicroService {

    @Autowired
    private OperateReportFormAppTodayImpl dao;

    @Autowired
    private OperateActionIdcardDaoImpl operateActionIdcardDao;

    @Autowired
    private OperateActionSubmitImpl actionSubmitDao;

    @Autowired
    private OperateActionLoanDaoImpl loanDao;

    @Autowired
    private OperateActionSubmitTodayDaoImpl actionSubmitTodayDao;

    @Autowired
    private OperateActionLoanTodayDaoImpl actionLoanTodayDao;

    @Autowired
    private OperateActionAcctTodayDaoImpl actionAcctTodayDao;

    @Override
    public List<OperateReportFormAppToday> summary(BasicPage<OperateReportFormAppToday> page, BasicRequest request) {
        return dao.summary(page, request);
    }

    @Override
    public List<OperateActionIdcard> applyRetention(BasicPage<OperateActionIdcard> page, BasicRequest request) {
        return operateActionIdcardDao.applyRetention(page, request);
    }

    @Override
    public List<OperateActionSubmit> openRetention(BasicPage<OperateActionSubmit> page, BasicRequest request) {
        return actionSubmitDao.applyRetention(page, request);
    }

    @Override
    public List<OperateActionLoan> loanRetention(BasicPage<OperateActionLoan> page, BasicRequest request) {
        return loanDao.loanRetention(page, request);
    }

    @Override
    public List<OperateActionDto> openDistribution(BasicPage<OperateActionDto> page, BasicRequest request) {
        //查询申请开户数据
        List<OperateActionSubmitToday> submits = actionSubmitTodayDao.distribution(page, request);
        //查询开户数据
        List<OperateActionAcctToday> accts = actionAcctTodayDao.distribution(page, request);
        return open(submits, accts);
    }

    @Override
    public List<OperateLoanDto> loanDistribution(BasicPage<OperateLoanDto> page, BasicRequest request) {

        //查询开户数据
        List<OperateActionAcctToday> accts = actionAcctTodayDao.distribution(page, request);

        //查询提现数据
        List<OperateActionLoanToday> loans = actionLoanTodayDao.distribution(page, request);
        return loan(accts, loans);
    }

    private List<OperateActionDto> open(List<OperateActionSubmitToday> submits, List<OperateActionAcctToday> accts){
        if(CollectionUtils.isEmpty(submits) && CollectionUtils.isEmpty(accts)){
            return null;
        }
        List<OperateActionDto> results = new ArrayList<>();
        OperateActionDto dto;
        Long other = 0L;
        if(!CollectionUtils.isEmpty(submits)){
            for(OperateActionSubmitToday submit : submits){
                dto = new OperateActionDto();
                dto.setDate(submit.getDate());
                dto.setTodayApply(submit.getToday());
                other = submit.getYestoday() + submit.getSeven() + submit.getThirty() + submit.getOther();
                dto.setOtherApply(other);
                results.add(dto);
            }
        }

        if(!CollectionUtils.isEmpty(accts)){
            results = this.bindOpen(results, accts);
        }
        return results;
    }

    private List<OperateActionDto> bindOpen(List<OperateActionDto> dtos, List<OperateActionAcctToday> accts){
        OperateActionDto dto;
        if(CollectionUtils.isEmpty(dtos)){
            for(OperateActionAcctToday acct : accts){
                dto = new OperateActionDto();
                dto.setDate(acct.getDate());
                dto.setTodayApply(acct.getToday());
                dto.setOtherApply(acct.getYestoday() + acct.getSeven() + acct.getThirty() + acct.getOther());
                dtos.add(dto);
            }
            return dtos;
        }
        for(OperateActionDto actionDto : dtos){
            for(OperateActionAcctToday acct : accts){
                if(actionDto.getDate().equals(acct.getDate())){
                    actionDto.setTodayOpen(acct.getToday());
                    actionDto.setOtherOpen(acct.getSeven() + acct.getOther() + acct.getThirty() + acct.getYestoday());
                }
            }
        }
        return dtos;
    }

    private List<OperateLoanDto> loan(List<OperateActionAcctToday> submits, List<OperateActionLoanToday> accts){
        if(CollectionUtils.isEmpty(submits) && CollectionUtils.isEmpty(accts)){
            return null;
        }
        List<OperateLoanDto> results = new ArrayList<>();
        OperateLoanDto dto;
        Long other = 0L;
        if(!CollectionUtils.isEmpty(submits)){
            for(OperateActionAcctToday submit : submits){
                dto = new OperateLoanDto();
                dto.setDate(submit.getDate());
                dto.setTodayOpen(submit.getToday());
                other = submit.getYestoday() + submit.getSeven() + submit.getThirty() + submit.getOther();
                dto.setOtherOpen(other);
                results.add(dto);
            }
        }

        if(!CollectionUtils.isEmpty(accts)){
            results = this.bindLoan(results, accts);
        }
        return results;
    }

    private List<OperateLoanDto> bindLoan(List<OperateLoanDto> dtos, List<OperateActionLoanToday> accts){
        OperateLoanDto dto;
        if(CollectionUtils.isEmpty(dtos)){
            for(OperateActionLoanToday acct : accts){
                dto = new OperateLoanDto();
                dto.setDate(acct.getDate());
                dto.setTodayLoan(acct.getToday());
                dto.setOtherLoan(acct.getYestoday() + acct.getSeven() + acct.getThirty() + acct.getOther());
                dtos.add(dto);
            }
            return dtos;
        }
        for(OperateLoanDto actionDto : dtos){
            for(OperateActionLoanToday acct : accts){
                if(actionDto.getDate().equals(acct.getDate())){
                    actionDto.setTodayLoan(acct.getToday());
                    actionDto.setOtherLoan(acct.getSeven() + acct.getOther() + acct.getThirty() + acct.getYestoday());
                    break;
                }
            }
        }
        return dtos;
    }
}
