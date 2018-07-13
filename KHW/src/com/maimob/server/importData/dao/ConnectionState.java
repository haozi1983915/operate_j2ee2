package com.maimob.server.importData.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ConnectionState {

	public boolean isrun = false;
	boolean isclose = false;
	Connection conn = null;
	

    private static ComboPooledDataSource OdataSource;
    private static ComboPooledDataSource LdataSource;
    private static ComboPooledDataSource SdataSource;
    

	public ConnectionState(String path) {
		try {
				
				Class.forName("com.mysql.cj.jdbc.Driver");
				String jdbcstr = "";
				if(path.equals("db_operate"))
				{
					jdbcstr = "jdbc:mysql://120.55.184.17:3306/"+path+"?user=root&password=maimob20171031&serverTimezone=UTC&useUnicode=true&useSSL=true&characterEncoding=UTF-8";
//					jdbcstr = "jdbc:mysql://114.80.124.186:9150/"+path+"?user=root&password=maimob123&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
					conn = DriverManager.getConnection(jdbcstr);
					 
					
//					if(OdataSource == null)
//					{
//
//
//						Properties properties = new Properties();
//						// 使用ClassLoader加载properties配置文件生成对应的输入流
//						InputStream in = ConnectionState.class.getClassLoader().getResourceAsStream("config/hibernate/jdbc.properties");
//						// 使用properties对象加载输入流
//						properties.load(in);
//						//获取key对应的value值
//						String Driver = properties.getProperty("master.jdbc.driverClassName");
//						String url = properties.getProperty("master.jdbc.url");
//						String username = properties.getProperty("master.jdbc.username");
//						String password = properties.getProperty("master.jdbc.password");
//						
//
//						OdataSource = new ComboPooledDataSource();
//						OdataSource.setDriverClass(Driver);
//						OdataSource.setJdbcUrl(url);
//						OdataSource.setUser(username);
//						OdataSource.setPassword(password);
//						OdataSource.setMinPoolSize(20);
//						OdataSource.setAcquireIncrement(20);
//						OdataSource.setMaxPoolSize(200);
//						OdataSource.setInitialPoolSize(20);
//						OdataSource.setMaxIdleTime(60);
//					}
//					conn = OdataSource.getConnection();

//					if(OdataSource == null)
//					{
//						OdataSource = new ComboPooledDataSource();
//						OdataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
//						OdataSource.setJdbcUrl("jdbc:mysql://106.14.21.177:3306/"+path+"?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8");
//						OdataSource.setUser("root");
//						OdataSource.setPassword("maimob123");
//					}
					
				}
				else if(path.equals("system"))
				{
					jdbcstr = "jdbc:mysql://120.55.184.17:3306/"+path+"?user=root&password=maimob20171031&serverTimezone=UTC&useUnicode=true&useSSL=true&characterEncoding=UTF-8";
					conn = DriverManager.getConnection(jdbcstr);
//					if(SdataSource == null)
//					{
//						SdataSource = new ComboPooledDataSource();
//						SdataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
//						SdataSource.setJdbcUrl("jdbc:mysql://120.55.184.17:3306/"+path+"?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8");
//						SdataSource.setUser("root");
//						SdataSource.setPassword("maimob123");
//					}
//					conn = SdataSource.getConnection();
				}
				else
				{
//					jdbcstr = "jdbc:mysql://rr-uf62yf2t57x3b947h.mysql.rds.aliyuncs.com:3306/"+path+"?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
//					conn = DriverManager.getConnection(jdbcstr,"mailoan","Maimob789&*(");

					if(LdataSource == null)
					{
						LdataSource = new ComboPooledDataSource();
						LdataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
						LdataSource.setJdbcUrl("jdbc:mysql://rr-uf62yf2t57x3b947h.mysql.rds.aliyuncs.com:3306/"+path+"?serverTimezone=UTC&useUnicode=true&useSSL=true&characterEncoding=UTF-8&useOldAliasMetadataBehavior=true");
						LdataSource.setUser("mailoan");
						LdataSource.setPassword("Maimob789&*(");
					}
					

//					if(LdataSource == null)
//					{
//						LdataSource = new ComboPooledDataSource();
//						LdataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
//						LdataSource.setJdbcUrl("jdbc:mysql://localhost:3306/"+path+"?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8");
//						LdataSource.setUser("root");
//						LdataSource.setPassword("maimob20171031");
//					}
					conn = LdataSource.getConnection();
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void close()
	{
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	

	public boolean CheckConn() {
		try {
			if (conn.isClosed()) {
				isclose = true;
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			isclose = true;
			isrun = true;
			return false;
		}

		return true;
	}


	public String QueryJsonStr(String sql) throws SQLException {
		return QueryJsonObj(sql).toString();
	}
	

	public JSONArray QueryJsonObj(String sql) throws SQLException   {

		JSONArray list = new JSONArray();
		try {
			isrun = true;
			ResultSet rs = null;
			Statement stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			ResultSetMetaData rsm = rs.getMetaData();
			int colNum = 0;
			colNum = rsm.getColumnCount();
			String[] cns = new String[colNum];
			for (int i = 1; i <= colNum; i++) {
				cns[i - 1] = rsm.getColumnName(i);
			}

			while (rs.next()) {
				
				JSONObject obj = new JSONObject();
				
				for (int i = 0; i < cns.length; i++) {
					String var = rs.getString(cns[i]);
					obj.put(cns[i], var);
				}

				list.add(obj);
			}

			rs.close();

			stat.close();
			isrun = false;
		} catch (SQLException e) {
			isrun = false;
			throw e;
		}
		
		return list;
	}
	
	


	public Object QueryByLogic(String sql,IDao logic) throws SQLException   {
		Object obj = null;
		try {
			isrun = true;
			ResultSet rs = null;
			Statement stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			obj = logic.Daologic(rs);
			
			rs.close();

			stat.close();
			isrun = false;
		} catch (SQLException e) {
			isrun = false;
			throw e;
		}
		
		return obj;
	}
	
	


	public List<Map<String, String>> Query(String sql) throws SQLException   {
		List<Map<String, String>> rsMap = null;
		try {
			isrun = true;
			ResultSet rs = null;
			Statement stat = conn.createStatement();
			rs = stat.executeQuery(sql);
//			PreparedStatement ps = conn.prepareStatement(sql);
//			rs = ps.executeQuery();
			
			ResultSetMetaData rsm = rs.getMetaData();
			int colNum = 0;
			colNum = rsm.getColumnCount();
			String[] cns = new String[colNum];
			for (int i = 1; i <= colNum; i++) {
				cns[i - 1] = rsm.getColumnName(i);
				if(cns[i - 1].equals("adminid1"))
				{
					cns[i - 1] = "adminid";
					
				}
				
			}
			rsMap = new ArrayList<Map<String, String>>();
			while (rs.next()) {
				Map<String, String> row = new HashMap<String, String>();
				for (int i = 0; i < cns.length; i++) {
					String var = rs.getString(cns[i]);
					row.put(cns[i], var);
				}

				rsMap.add(row);
			}

			rs.close();
			stat.close();

//			ps.close();
			isrun = false;
		} catch (SQLException e) {
			isrun = false;
			throw e;
		}
		
		return rsMap;
	}



	public int Update(String sql) throws SQLException {
		isrun = true;
		int effect = 0;
		
		try {
			Statement stat = conn.createStatement();
			effect = stat.executeUpdate(sql);
			stat.close();
			isrun = false;
		} catch (SQLException e) {
			isrun = false;
			throw e;
		}

		return effect;
	}

	public boolean UpdateAll(String sql, String[] args, int len, int cou) {

		isrun = true;
		try {
			conn.setAutoCommit(false);
			PreparedStatement prep = conn.prepareStatement(sql);
			int sl = len / cou;
			for (int i = 0; i < sl; i++) {
				for (int j = 1; j <= cou; j++) {
					int inx = i * cou + j;
					prep.setString(j, args[inx]);
				}
				prep.addBatch();
			}
			prep.executeBatch();
			conn.setAutoCommit(true);
			prep.close();
		} catch (Exception e) {
			isrun = false;
			return false;
		}
		isrun = false;
		return true;
	}

	int addTime = 0;
	PreparedStatement prep = null;

	public boolean UpdateStart(String sql) throws SQLException {
		addTime = 0;
		isrun = true;
		try {
			conn.setAutoCommit(false);
			prep = conn.prepareStatement(sql);
		} catch (Exception e) {
			e.printStackTrace();
			isrun = false;
			return false;
		}
		isrun = false;
		return true;
	}

	public boolean UpdateIng(String[] args) throws SQLException {
		isrun = true;
		try {
			addTime++;
	
			for (int j = 0; j < args.length; j++) {
				prep.setString(j + 1, args[j]);
			}
			prep.addBatch();
			if (addTime % 100 == 0) {
				prep.executeBatch();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		isrun = false;
		return true;
	}
	

	public boolean UpdateIngForHistory(String[] args) throws SQLException {
		isrun = true;
		try {
			addTime++;
	
			prep.setString(1, args[0]);
			prep.setDouble(2, Double.parseDouble(args[1]));
			prep.setDouble(3, Double.parseDouble(args[2]));
			prep.setDouble(4, Double.parseDouble(args[3]));
			prep.setDouble(5, Double.parseDouble(args[4]));
			prep.setDouble(6, Double.parseDouble(args[5]));
			prep.setDouble(7, Double.parseDouble(args[6]));
			
			prep.addBatch();
			if (addTime % 100 == 0) {
				prep.executeBatch();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		isrun = false;
		return true;
	}
	

	public boolean UpdateEnd() throws SQLException {
		isrun = true;
		try {
			prep.executeBatch();
			conn.setAutoCommit(true);
			prep.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
		isrun = false;
		return true;
	}

}
