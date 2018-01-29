package com.maimob.server.importData.dao;

import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory {
	
	static final int connCount = 30;
	
//	public static ConnectionState[] conns = new ConnectionState[connCount];
	public static Map <String,ConnectionState[]> connMap = new HashMap<String,ConnectionState[]>();

	
	 
	public synchronized static ConnectionState getConnection(String path)
	{
		ConnectionState[] conns = null;

		conns = connMap.get(path);
		if(conns == null)
		{
			conns = new ConnectionState[connCount];
			connMap.put(path, conns);
		}
		
		
		
		
		
		int i = 0;
		while(true)
		{
			if(conns[i] == null)
			{
				conns[i] = new ConnectionState(path);
				return conns[i];
			}
			else
			{
				if(!conns[i].isrun)
				{
					return conns[i];
				}
				
			}
			
			i++;
			
			if(i >= conns.length)
			{
				i = 0;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
				
		}
		
	}
	
	
//	private void make() {
//		try {
//			Class.forName("org.sqlite.JDBC");
//		    Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
//		    Statement stat = conn.createStatement();
//		    stat.executeUpdate("drop table if exists people;");
//		    stat.executeUpdate("create table people (name, occupation);");
//		    PreparedStatement prep = conn.prepareStatement(
//		      "insert into people values (?, ?);");
//
//		    prep.setString(1, "Gandhi");
//		    prep.setString(2, "politics");
//		    prep.addBatch();
//		    prep.setString(1, "Turing");
//		    prep.setString(2, "computers");
//		    prep.addBatch();
//		    prep.setString(1, "Wittgenstein");
//		    prep.setString(2, "smartypants");
//		    prep.addBatch();
//
//		    conn.setAutoCommit(false);
//		    prep.executeBatch();
//		    conn.setAutoCommit(true);
//
//		    ResultSet rs = stat.executeQuery("select * from people;");
//		    while (rs.next()) {
//		      //System.out.println("name = " + rs.getString("name"));
//		      //System.out.println("job = " + rs.getString("occupation"));
//		    }
//		    rs.close();
//		    conn.close();
//		    
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//	}
	
	
	
	
	
	
	

}






