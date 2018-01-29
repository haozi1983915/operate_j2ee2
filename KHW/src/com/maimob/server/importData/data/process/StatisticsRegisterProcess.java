package com.maimob.server.importData.data.process;

import java.sql.ResultSet;

import com.maimob.server.importData.dao.IDao;


public class StatisticsRegisterProcess implements IDao
{
	
	
	
	@Override
	public Object Daologic(ResultSet rs) {
		 int cou = 0;
		try {

			while (rs.next()) {

				try {
					String[] cc = new String[5];
					cc[0] = rs.getString("province");
					cc[1] = rs.getString("city");
					cc[2] = rs.getString("top");
					cc[3] = rs.getString("runing");
					cc[4] = rs.getString("type");
					
					
					
					
					
					
					
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cou;
	}
	
	
	
}