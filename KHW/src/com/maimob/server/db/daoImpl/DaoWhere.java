package com.maimob.server.db.daoImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.StringUtils;

public class DaoWhere {
	
	

    public static String[] getAdminWhere(String json,int type)
    {

        String[] wherestr = new String[3];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
        if(!json.equals(""))
        {
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}
            
            JSONObject jobj = JSONObject.parseObject(json);
            String name = jobj.getString("name");
            String email = jobj.getString("email");
            String departmentId = jobj.getString("departmentId");
            String level = jobj.getString("level");
            int pageid = Integer.parseInt(jobj.getString("pageId"));
            int page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));

            if(!StringUtils.isStrEmpty(name))
            {
            	where.append(" and name like '%"+name+"%' ");
            }

            if(!StringUtils.isStrEmpty(email))
            {
            	where.append(" and email like '%"+email+"%' ");
            }

            if(!StringUtils.isStrEmpty(departmentId))
            {
            	where.append(" and departmentId = "+departmentId+" ");
            }

            if(!StringUtils.isStrEmpty(level))
            {
            	where.append(" and level = "+level+" ");
            }
            
            if(where.length() > 0)
            {
            	wherestr[0] = " where "+where.toString();
            }
            if(type == 1)
            {
            	wherestr[0] += " order by level asc ";
            	wherestr[1] = (pageid*page_AdminCou)+"";
            	wherestr[2] = (page_AdminCou)+"";
            	
            }
        }
        
    	return wherestr;
    }
    
    
	


    public static String[] getProxyWhere(JSONObject jobj,int type)
    {
        String[] wherestr = new String[3];
        String where = new String();

        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

        String id = jobj.getString("id");
        if(!StringUtils.isStrEmpty(id))
        {
        	where += " and id = "+id+" ";
        }
        else
        {
            String company = jobj.getString("company");
            if(!StringUtils.isStrEmpty(company))
            {
            	where += " and company like '%"+company+"%' ";
            }
        }
        
        if(where.toString().equals(""))
        {

            where = " channelCou = 0 or 1=1 "+where;
        	
        }
        else
        {
            where = " 1=1 "+where;
        }
        
        

        
         
        
        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
        if(type == 1)
        {
        	wherestr[1] = (pageid*page_AdminCou)+"";
        	wherestr[2] = (page_AdminCou)+"";
        }
    
        
    	return wherestr;
    }
    
	


    public static String[] getChannelWhere(JSONObject jobj,int type)
    {
        String[] wherestr = new String[3];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");

        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

        String proxyId = jobj.getString("proxyId");
        if(!StringUtils.isStrEmpty(proxyId))
        {
            where.append(" and proxyId = "+proxyId+" ");
        }

        String channelName = jobj.getString("channelName");
        
        if(!StringUtils.isStrEmpty(channelName))
        {
            where.append(" and channelName like '%"+channelName.trim()+"%' ");
        }

        String channel = jobj.getString("channel");
        
        if(!StringUtils.isStrEmpty(channel))
        {
            where.append(" and channel like '%"+channel.trim()+"%' ");
        }
        

        String channelNo = jobj.getString("channelNo");
        
        if(!StringUtils.isStrEmpty(channelNo))
        {
            where.append(" and channelNo like '%"+channelNo.trim()+"%' ");
        }
        

        String attribute = jobj.getString("attribute");
        if(!StringUtils.isStrEmpty(attribute))
        {
            where.append(" and attribute = "+attribute+" ");
        }
        

        String channeltype = jobj.getString("type");
        if(!StringUtils.isStrEmpty(channeltype))
        {
            where.append(" and type = "+channeltype+"  ");
        }
        

        String subdivision = jobj.getString("subdivision");
        if(!StringUtils.isStrEmpty(subdivision))
        {
            where.append(" and subdivision = "+subdivision+"  ");
        }

        String level = jobj.getString("level");
        if(!StringUtils.isStrEmpty(level))
        {
            where.append(" and level = "+level+" ");
        }

        String adminId = jobj.getString("adminId");
        if(!StringUtils.isStrEmpty(adminId))
        {
            where.append(" and adminId = "+adminId+" ");
        }

        String status = jobj.getString("status");
        if(!StringUtils.isStrEmpty(status))
        {
            where.append(" and status = "+status+" ");
        }
    	
    
        

        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
        if(type == 1)
        {
        	wherestr[1] = (pageid*page_AdminCou)+"";
        	wherestr[2] = (page_AdminCou)+"";
        }
    
        
    	return wherestr;
    }
    
	


    public static String[] getFromWhereForFrom(JSONObject jobj,int type)
    {
        String[] wherestr = new String[4];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
        
        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		String dateType = jobj.getString("dateType");
		
        String maxDate = jobj.getString("maxDate");

        String minDate = jobj.getString("minDate");
        
        if(dateType.equals("2"))
        {
        	maxDate = maxDate.substring(0,maxDate.lastIndexOf("-"));
        }
        if(dateType.equals("2"))
        {
        	minDate = minDate.substring(0,minDate.lastIndexOf("-"));
        }
        
        
        int day = AppTools.daysBetween(minDate,maxDate);
        
        wherestr[3] = day+"";
        if(!StringUtils.isStrEmpty(maxDate) && minDate.equals(maxDate))
        {
            where.append(" and date = '"+maxDate+"' ");
        }
        else
        {
            if(!StringUtils.isStrEmpty(maxDate))
            {
                where.append(" and date <= '"+maxDate+"' ");
            }
            if(!StringUtils.isStrEmpty(minDate))
            {
                where.append(" and date >= '"+minDate+"' ");
            }

        	
        }

        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
        if(type == 1)
        {
        	wherestr[1] = (pageid*page_AdminCou)+"";
        	wherestr[2] = (page_AdminCou)+"";
        }
    
        
    	return wherestr;
    }
    

    public static String[] getFromWhereForHj(JSONObject jobj,int type)
    {
        String[] wherestr = new String[4];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
        
        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		String dateType = jobj.getString("dateType");
		
        String maxDate = jobj.getString("maxDate");

        String minDate = jobj.getString("minDate");
        
        if(dateType.equals("2"))
        {
        	maxDate = maxDate.substring(0,maxDate.lastIndexOf("-"));
        }
        if(dateType.equals("2"))
        {
        	minDate = minDate.substring(0,minDate.lastIndexOf("-"));
        }
        
        
        int day = AppTools.daysBetween(minDate,maxDate);
        
        wherestr[3] = day+"";
        if(!StringUtils.isStrEmpty(maxDate) && minDate.equals(maxDate))
        {
            where.append(" and date like '"+maxDate+"%' ");
        }
        else
        {
            if(!StringUtils.isStrEmpty(maxDate))
            {
                where.append(" and date <= '"+maxDate+"' ");
            }
            if(!StringUtils.isStrEmpty(minDate))
            {
                where.append(" and date >= '"+minDate+"' ");
            }

        	
        }

        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
        if(type == 1)
        {
        	wherestr[1] = (pageid*page_AdminCou)+"";
        	wherestr[2] = (page_AdminCou)+"";
        }
    
        
    	return wherestr;
    }
    
	


    public static String[] getOptimizationTaskWhere(JSONObject jobj)
    {
        String[] wherestr = new String[4];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
        
        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		
        String maxDate = jobj.getString("maxDate");

        String minDate = jobj.getString("minDate");
        
        
        int day = AppTools.daysBetween(minDate,maxDate);
        
        wherestr[3] = day+"";
        if(!StringUtils.isStrEmpty(maxDate) && minDate.equals(maxDate))
        {
            where.append(" and (startdate = '"+maxDate+"' or enddate = '"+maxDate+"' ) ");
        }
        else if(!StringUtils.isStrEmpty(maxDate) && !StringUtils.isStrEmpty(minDate))
        {
            where.append(" and ( startdate <= '"+maxDate+"' and startdate >= '"+minDate+"'  or  enddate <= '"+maxDate+"' and enddate >= '"+minDate+"' ) ");
        }
        

        String tableId = jobj.getString("tableId");
        
        
        if(!StringUtils.isStrEmpty(tableId))
        {
            where.append(" and tableId = "+tableId+" ");
        }
        

        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
    
        
    	return wherestr;
    }
    


}
