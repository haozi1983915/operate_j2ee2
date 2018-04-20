package com.maimob.server.db.daoImpl;

import com.maimob.server.base.BasicPage;
import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.OperateActionIdcard;
import com.maimob.server.db.entity.OperateActionSubmit;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yang on 2018/4/19.
 */
@Repository
public class OperateActionSubmitImpl extends BaseDaoHibernate5<OperateActionSubmit> {

    public List<OperateActionSubmit> applyRetention(BasicPage<OperateActionSubmit> page, BasicRequest request) {
        String hql = "select o.date AS date, sum(o.register) as register, sum(o.noId) as noId, " +
                "sum(o.today) as today, sum(o.one) as one, sum(o.two) as two, sum(o.three) as three, " +
                "sum(o.four) as four, sum(o.fiv) as fiv, sum(o.six) as six, sum(o.seven) as seven, sum(o.sevenLater) as sevenLater " +
                "from OperateActionSubmit o where appid = "+request.getAppId()+"and date <= '"+request.getMaxDate()+"' and date >= '"+request.getMinDate()+
                "' group by o.date order by o.date desc";
        List<OperateActionSubmit> results = this.queryForPage(hql, request.getPageNo(), request.getPageSize(), OperateActionSubmit.class);
        int count = this.getCount(hql);
        page.setCurrentPage(request.getPageNo());
        page.setPageSize(request.getPageSize());
        page.setTotalPage(count);
        return results;
    }
}
