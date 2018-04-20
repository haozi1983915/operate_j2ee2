package com.maimob.server.db.daoImpl;

import com.maimob.server.base.BasicPage;
import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.OperateActionLoan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yang on 2018/4/20.
 */
@Repository
public class OperateActionLoanDaoImpl extends BaseDaoHibernate5<OperateActionLoan> {
    public List<OperateActionLoan> loanRetention(BasicPage<OperateActionLoan> page, BasicRequest request) {
        String hql = "select o.date AS date, sum(o.register) as register, sum(o.noId) as noId, " +
                "sum(o.today) as today, sum(o.one) as one, sum(o.two) as two, sum(o.three) as three, " +
                "sum(o.four) as four, sum(o.fiv) as fiv, sum(o.six) as six, sum(o.seven) as seven, sum(o.sevenLater) as sevenLater " +
                "from OperateActionLoan o where appid = "+request.getAppId()+"and date <= '"+request.getMaxDate()+"' and date >= '"+request.getMinDate()+
                "' group by o.date order by o.date desc";
        List<OperateActionLoan> results = this.queryForPage(hql, request.getPageNo(), request.getPageSize(), OperateActionLoan.class);
        int count = this.getCount(hql);
        page.setCurrentPage(request.getPageNo());
        page.setPageSize(request.getPageSize());
        page.setTotalPage(count);
        return results;
    }
}
