package com.maimob.server.db.daoImpl;

import com.maimob.server.base.BasicPage;
import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.dto.OperateActionDto;
import com.maimob.server.db.dto.OperateLoanDto;
import com.maimob.server.db.entity.OperateActionAcctToday;
import com.maimob.server.db.entity.OperateActionSubmitToday;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yang on 2018/4/20.
 */
@Repository
public class OperateActionAcctTodayDaoImpl extends BaseDaoHibernate5<OperateActionAcctToday> {
    public List<OperateActionAcctToday> distribution(BasicPage<?> page, BasicRequest request) {
        String hql = "select o.date AS date, sum(o.today) as today, sum(o.yestoday) as yestoday, " +
                "sum(o.seven) as seven, sum(o.thirty) as thirty, sum(o.other) as other " +
                "from OperateActionAcctToday o where appid = "+request.getAppId()+"and date <= '"+request.getMaxDate()+"' and date >= '"+request.getMinDate()+
                "' group by o.date order by o.date desc";
        List<OperateActionAcctToday> results = this.queryForPage(hql, request.getPageNo(), request.getPageSize(), OperateActionAcctToday.class);
        int count = this.getCount(hql);
        page.setCurrentPage(request.getPageNo());
        page.setPageSize(request.getPageSize());
        page.setTotalRecords(count);
        return results;
    }
}