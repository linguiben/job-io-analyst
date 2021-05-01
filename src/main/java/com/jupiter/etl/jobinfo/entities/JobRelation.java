package com.jupiter.etl.jobinfo.entities;

import com.jupiter.WriteLog;
import com.jupiter.util.DBconnect;

public class JobRelation {

	public int rid = 0;
	public int pnumber = 0;
	public String previous = null;
	public int bnumber = 0;
	public String behind = null;
	public int pathlength = 0;
	public int rnumber2 = 0;
	public int x = 0;
	public int y = 0;
	public int rn = 0;
	
	public static void main(String args[]) throws Exception {
		WriteLog.writeFile("处理开始....");
//		new DBconnect();
//		System.out.println("url = " + DBconnect.getUrl());		
	}

}
