package com.maimob.server.controller;

import java.util.HashMap;
import java.util.Map;

public class RequestParameter {
	
	
	public Map<String,String> rps = new HashMap<String,String>();
	public boolean isComplete = true;
	public String get(String key)
	{
		return rps.get(key);
	}
	

	public String set(String key,String value)
	{
		return rps.put(key, value);
	}
	
	
	public boolean check(String key)
	{
		if(rps.get(key) == null || rps.get(key).equals(""))
		{
			return false;
		}
		return true;
	}
	

}
