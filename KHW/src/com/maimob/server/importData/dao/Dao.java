package com.maimob.server.importData.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;

public class Dao {
	
	ConnectionState conn = null;
	
	public Dao(String dbPuth) {
		conn = ConnectionFactory.getConnection(dbPuth);
	}

 
	
	public JSONArray QueryJson(String sql) throws SQLException {
		return conn.QueryJsonObj(sql);
	}
	
	public String QueryJsonStr(String sql) throws SQLException {
		return conn.QueryJsonStr(sql);
	}
	
	public List<Map<String,String>> Query(String sql) throws SQLException {
		return conn.Query(sql);
	}
	
	
	public Object QueryByLogic(String sql,IDao logic) throws SQLException {
		return conn.QueryByLogic(sql, logic);
	}
	
	
	public int Update(String sql) throws SQLException
	{
		return conn.Update(sql);
	}
	
	
	public boolean UpdateStart(String sql) throws SQLException
	{
		return conn.UpdateStart(sql);
	}

	
	public boolean UpdateIng(String[] args) throws SQLException
	{
		return conn.UpdateIng(args);
	}
	
	public boolean UpdateEnd() throws SQLException
	{
		return conn.UpdateEnd();
	}
	

}
