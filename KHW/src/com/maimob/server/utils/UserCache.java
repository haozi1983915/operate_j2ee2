package com.maimob.server.utils;

import java.util.List;

import com.maimob.server.db.entity.Operate_reportform_day;
import com.maimob.server.db.entity.Operate_reportform_month;

public class UserCache {
	public long id;
	public List<Long> channelids = null;
	
	public Operate_reportform_day od;
	public Operate_reportform_month om;

}
