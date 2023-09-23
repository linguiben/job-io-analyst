package com.jupiter.etl.jobinfo.entities;

import com.jupiter.WriteLog;
import com.jupiter.util.DBconnect;

public class JobLocation {

	public String jobname = null;
	public int x = 0;
	public int y = 0;
	public int isvalid = 0;
	public String jobtype = null;
	public int cost = 0;
	public int rn = 0;
	public int status = 1000;
	
	public static void main(String args[]) throws Exception {
		WriteLog.writeFile("处理开始....");
//		new DBconnect();
//		System.out.println("url = " + DBconnect.getUrl());		
	}

}
