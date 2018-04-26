package com.maimob.server.db.daoImpl;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.OperateChannelHistory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChannelHistoryImpl extends BaseDaoHibernate5<OperateChannelHistory> {

    public List<OperateChannelHistory> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from OperateChannelHistory o where o.delFlag = 0")
                .getResultList();
    }


    public void saveChannelHistory(OperateChannelHistory channelHistory) {
        Session session = sessionFactory.getCurrentSession();
        session.clear();
        session.saveOrUpdate(channelHistory);
        session.flush();
    }
}
