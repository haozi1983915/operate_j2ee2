package com.maimob.server.db.daoImpl;

import com.maimob.server.base.BasicPage;
import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.OperateActionIdcard;
import com.maimob.server.db.entity.OperateReportFormAppToday;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by yang on 2018/4/18.
 */
@Repository
public class OperateReportFormAppTodayImpl extends BaseDaoHibernate5<OperateReportFormAppToday> {

    public List<OperateReportFormAppToday> summary(BasicPage<OperateReportFormAppToday> page, BasicRequest request) {

        String hql = packageHql(request, " group by o.date order by o.date");
        List<OperateReportFormAppToday> results =
                queryForPage(hql, request.getPageNo(), request.getPageSize(), OperateReportFormAppToday.class);

        hql = "select count(*), o.date from OperateReportFormAppToday o where o.appid = "+request.getAppId()+
                "and o.date <= '"+request.getMaxDate()+"' and  o.date >= '"+request.getMinDate()+"' GROUP BY o.date";
        int totalRecords = getCount(hql);

        page.setCurrentPage(request.getPageNo());
        page.setPageSize(request.getPageSize());
        page.setTotalRecords(totalRecords);

        hql = packageHql(request);

        OperateReportFormAppToday totals =(OperateReportFormAppToday) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setResultTransformer(Transformers.aliasToBean(OperateReportFormAppToday.class))
                .uniqueResult();

        if(!CollectionUtils.isEmpty(results)){
            try {
                results = batch(results, totals);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private List<OperateReportFormAppToday> batch(List<OperateReportFormAppToday> results, OperateReportFormAppToday totals)
            throws InvocationTargetException, IllegalAccessException {
        for(OperateReportFormAppToday reportFormAppToday : results){
            reportFormAppToday.setRegister(totals.getRegister());
            reportFormAppToday.setAccount(totals.getAccount());
            reportFormAppToday.setDebitCard(totals.getDebitCard());
            reportFormAppToday.setHomeJob(totals.getHomeJob());
            reportFormAppToday.setIdCard(totals.getIdCard());
            reportFormAppToday.setLoan(totals.getLoan());
            reportFormAppToday.setUpload(totals.getUpload());
            reportFormAppToday.setVedio(totals.getVedio());
            reportFormAppToday.setUnAccount(totals.getUnAccount());
            reportFormAppToday.setOtherAccount(totals.getAccount() - reportFormAppToday.getTodayAccount());
            reportFormAppToday.setOtherDebitCard(totals.getDebitCard() - reportFormAppToday.getTodayDebitCard());
            reportFormAppToday.setOtherHomeJob(totals.getHomeJob() - reportFormAppToday.getTodayHomeJob());
            reportFormAppToday.setOtherIdCard(totals.getIdCard() - reportFormAppToday.getTodayIdCard());
            reportFormAppToday.setOtherLoan(totals.getLoan() - reportFormAppToday.getTodayLoan());
            reportFormAppToday.setOtherRegister(totals.getRegister() - reportFormAppToday.getTodayRegister());
            reportFormAppToday.setOtherUpload(totals.getUpload() - reportFormAppToday.getTodayUpload());
            reportFormAppToday.setOtherVedio(totals.getVedio() - reportFormAppToday.getTodayVedio());
            reportFormAppToday.setOtherUnAccount(totals.getUnAccount() - reportFormAppToday.getTodayUnAccount());
        }
        return results;
    }

    private String packageHql(BasicRequest request, String group){
        String hql = "SELECT SUM(o.register) AS todayRegister, SUM(o.idCard) AS todayIdCard, SUM(o.debitCard) AS todayDebitCard," +
                "SUM(o.homeJob) AS todayHomeJob, SUM(o.vedio) AS todayVedio, SUM(o.upload) AS todayUpload, SUM(o.account) AS todayAccount, SUM(o.unAccount) AS todayUnAccount, SUM(o.loan) AS todayLoan," +
                " o.date AS date FROM OperateReportFormAppToday o where 1 = 1";
        StringBuffer sb = new StringBuffer();
        sb.append(hql);
        if(StringUtils.isEmpty(request.getMaxDate())
                && StringUtils.isEmpty(request.getMinDate()) && request.getAppId() == null){
            if(!StringUtils.isEmpty(group)){
                sb.append(group);
            }

        }else{
            if(!StringUtils.isEmpty(request.getMinDate())){
                sb.append(" and o.date >= '" + request.getMinDate()+"'");
            }
            if(!StringUtils.isEmpty(request.getMaxDate())){
                sb.append(" and o.date <= '" + request.getMaxDate()+"'");
            }
            if(request.getAppId() != null){
                sb.append(" and o.appid =" + request.getAppId());
            }
            if(!StringUtils.isEmpty(group)){
                sb.append(group);
            }
        }
        return sb.toString();
    }

    private String packageHql(BasicRequest request){
        String hql = "SELECT SUM(o.register) AS register, SUM(o.idCard) AS idCard, SUM(o.debitCard) AS debitCard," +
                "SUM(o.homeJob) AS homeJob, SUM(o.vedio) AS vedio, SUM(o.upload) AS upload, SUM(o.account) AS account, SUM(o.unAccount) AS unAccount, SUM(o.loan) AS loan " +
                "FROM OperateReportFormAppToday o where 1 = 1";
        StringBuffer sb = new StringBuffer();
        sb.append(hql);
        if(StringUtils.isEmpty(request.getMaxDate())
                && StringUtils.isEmpty(request.getMinDate()) && request.getAppId() == null){
            return sb.toString();

        }else{
            if(!StringUtils.isEmpty(request.getMinDate())){
                sb.append(" and o.date >= '" + request.getMinDate()+"'");
            }
            if(!StringUtils.isEmpty(request.getMaxDate())){
                sb.append(" and o.date <= '" + request.getMaxDate()+"'");
            }
            if(request.getAppId() != null){
                sb.append(" and o.appid =" + request.getAppId());
            }
        }
        return sb.toString();
    }


}
