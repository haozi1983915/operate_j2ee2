package com.maimob.server.db.daoImpl;

import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.OperateReportFormAppToday;
import com.maimob.server.db.entity.Optimization;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by yang on 2018/4/18.
 */
@Repository
public class OperateReportFormAppTodayImpl extends BaseDaoHibernate5<Optimization> {

    public List<OperateReportFormAppToday> summary(BasicRequest request) {
        String hql = "SELECT SUM(o.register) AS todayRegister, SUM(o.idCard) AS todayIdCard, SUM(o.debitCard) AS todayDebitCard," +
                "SUM(o.homeJob) AS todayHomeJob, SUM(o.vedio) AS todayVedio, SUM(o.upload) AS todayUpload, SUM(o.account) AS todayAccount, SUM(o.loan) AS todayLoan," +
                " o.date AS date FROM OperateReportFormAppToday o GROUP BY o.date ORDER BY o.date DESC";

        List<OperateReportFormAppToday> results = sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setResultTransformer(Transformers.aliasToBean(OperateReportFormAppToday.class))
                .list();

        hql = "SELECT SUM(o.register) AS register, SUM(o.idCard) AS idCard, SUM(o.debitCard) AS debitCard," +
                "SUM(o.homeJob) AS homeJob, SUM(o.vedio) AS vedio, SUM(o.upload) AS upload, SUM(o.account) AS account, SUM(o.loan) AS loan " +
                "FROM OperateReportFormAppToday o";

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

    private List<OperateReportFormAppToday> batch(List<OperateReportFormAppToday> results, OperateReportFormAppToday totals) throws InvocationTargetException, IllegalAccessException {
        for(OperateReportFormAppToday reportFormAppToday : results){
            reportFormAppToday.setRegister(totals.getRegister());
            reportFormAppToday.setAccount(totals.getAccount());
            reportFormAppToday.setDebitCard(totals.getDebitCard());
            reportFormAppToday.setHomeJob(totals.getHomeJob());
            reportFormAppToday.setIdCard(totals.getIdCard());
            reportFormAppToday.setLoan(totals.getLoan());
            reportFormAppToday.setUpload(totals.getUpload());
            reportFormAppToday.setVedio(totals.getVedio());
            reportFormAppToday.setOtherAccount(totals.getAccount() - reportFormAppToday.getTodayAccount());
            reportFormAppToday.setOtherDebitCard(totals.getDebitCard() - reportFormAppToday.getTodayDebitCard());
            reportFormAppToday.setOtherHomeJob(totals.getHomeJob() - reportFormAppToday.getTodayHomeJob());
            reportFormAppToday.setOtherIdCard(totals.getIdCard() - reportFormAppToday.getTodayIdCard());
            reportFormAppToday.setOtherLoan(totals.getLoan() - reportFormAppToday.getTodayLoan());
            reportFormAppToday.setOtherRegister(totals.getRegister() - reportFormAppToday.getTodayRegister());
            reportFormAppToday.setOtherUpload(totals.getUpload() - reportFormAppToday.getTodayUpload());
            reportFormAppToday.setOtherVedio(totals.getVedio() - reportFormAppToday.getTodayVedio());
        }
        return results;
    }
}
