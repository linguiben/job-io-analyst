package com.jupiter.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.jupiter.WriteLog;
import com.jupiter.etl.jobinfo.entities.JobLocation;
import com.jupiter.etl.jobinfo.entities.JobRelation;

public class DBconnect {
	// @SuppressWarnings("unchecked")
//	private int rptdtdate = 0;
//
//	public int getRptdtdate() {
//		return rptdtdate;
//	}
//
//	public void setRptdtdate(int rptdtdate) {
//		this.rptdtdate = rptdtdate;
//	}
//
//	private static String g_host;
//	private static String g_dbname;
//	private static String g_user;
//
//	public String getG_user() {
//		return g_user;
//	}
//
//	public void setG_user(String gUser) {
//		g_user = gUser;
//	}
//
//	private static String g_pwd;
//	private static String g_port;
//	private static String g_dbtype;
//	private static String driverName = "";
//	private static String url = "";
//	private static Connection conn = null;
//	private static Statement stmt = null;
//	private static ResultSet rs = null;
//
//	public void setG_pwd(String g_pwd) {
//		DBconnect.g_pwd = g_pwd;
//	}
//
//	public String getG_pwd() {
//		return g_pwd;
//	}
//
//	public static String getUrl() {
//		return url;
//	}
//
//	public static void setUrl(String url) {
//		DBconnect.url = url;
//	}
//	public DBconnect() {
//		try {
//			InputStream inputStream = this.getClass().getClassLoader()
//					.getResourceAsStream("db.properties");
//			Properties p = new Properties();
//
//			try {
//				p.load(inputStream);
//			} catch (IOException e1) {
//				WriteLog.writeFile("1.读取配置文件出错,退出程序");
//			}
//			g_host = p.getProperty("SERVERNAME");
//			g_dbname = p.getProperty("DBNAME");
//			g_user = p.getProperty("DBUSER");
//			setG_pwd(p.getProperty("DBPASSWORD"));
//			g_port = p.getProperty("PORT");
//			g_dbtype = p.getProperty("DBTYPE");
//		} catch (Exception e1) {
//			WriteLog.writeFile("2.读取配置文件出错,退出程序");
//			e1.printStackTrace();
//			System.exit(0);
//		}
//		if (g_dbtype.toLowerCase().equals("db2")) {
//			driverName = "com.ibm.db2.jcc.DB2Driver";
//			setUrl("jdbc:" + g_dbtype.toLowerCase() + "://" + g_host + ":" + g_port + "/"
//					+ g_dbname);
//		} else if (g_dbtype.toLowerCase().equals("oracle")) {
//			driverName = "oracle.jdbc.driver.OracleDriver";
//			setUrl("jdbc:" + g_dbtype + "://" + g_host + ":" + g_port + "/"
//					+ g_dbname);
//		} else if (g_dbtype.toLowerCase().equals("sqlserver")) {
//			driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//			setUrl("jdbc:sqlserver://" + g_host + ":" + g_port
//					+ ";DatabaseName=" + g_dbname);
//		} else {
//			WriteLog.writeFile("配置文件数据库信息错误!无法识别此类型数据库！");
//			System.exit(0);
//		}
//		
//	}

	/*
	 * test
	 */
//	public int curRptdate() {
//		try {
//			Class.forName(driverName).newInstance();
//			conn = DriverManager.getConnection(url, g_user, g_pwd);
//		} catch (Exception e) {
//			return -1;
//		}
//		try {
//			String SQL = null;
//			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//			ResultSet rr = stmt.executeQuery("select sql from dbo.ui_report where flag = 9999");
//			if( rr .next()){
//					SQL = rr.getString("SQL");
//			}
//			CallableStatement stmt = conn.prepareCall(SQL);//"{call dbo.p_test}"
//			rs = stmt.executeQuery();// 判断当前报表日期
//			WriteLog.writeFile("成功调用：" + SQL);
//			if (rs.next())
//				rptdtdate = rs.getInt("rptdate");
//			conn.close();
//			WriteLog.writeFile("当前数据日期:" + rptdtdate);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return rptdtdate;
//	}
	
	//获取job_relation
//	public List<JobRelation> getJobRelationList(String jobname, String filename) {
//		List<JobRelation> jobRelationList = new ArrayList<JobRelation>();
//		try {
//			Class.forName(driverName).newInstance();
//			conn = DriverManager.getConnection(url, g_user, g_pwd);
//			// stmt =
//			// conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				// rs =
//				// stmt.executeQuery("CALL DBO.job_location('"+jobname+"','"+filename+"')");
//				// rs =
//				// stmt.executeQuery("CALL DBO.job_location('jb_dwn_chdrpf_to_chdrpf')");
//				// //jb_dwn_chdrpf_to_chdrpf jb_acctcode_pre
//				// jb_dwn_aa01pf_to_aa01pf
//				rs = stmt
//						.executeQuery("select a.*,b.x,b.y,row_number()over(partition by BEHIND order by PATHLENGTH)rn from dbo.job_relation a inner join dbo.job_location b on a.BEHIND = b.JOBNAME order by RID,PREVIOUS,BNUMBER");
//				// select * from dbo.job_relation
//				// 将结果放入jobInfoList
//				while (rs.next()) { // RID PNUMBER PREVIOUS BNUMBER BEHIND
//									// PATHLENGTH RNUMBER2 X Y
//					JobRelation jobinfo = new JobRelation();
//					jobinfo.rid = rs.getInt("RID");
//					jobinfo.pnumber = rs.getInt("PNUMBER");
//					jobinfo.previous = rs.getString("PREVIOUS");
//					jobinfo.bnumber = rs.getInt("BNUMBER");
//					jobinfo.behind = rs.getString("BEHIND");
//					jobinfo.pathlength = rs.getInt("PATHLENGTH");
//					jobinfo.rnumber2 = rs.getInt("RNUMBER2");
//					jobinfo.x = rs.getInt("X");
//					jobinfo.y = rs.getInt("Y");
//					jobinfo.rn = rs.getInt("RN");
//					jobRelationList.add(jobinfo);
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("读取配置文件时出错:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\n", "未知错误1,请检查数据库连接 或 联系管理员.", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile(this.getClass().getName()+"未知错误2,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\n", "未知错误2,请检查数据库连接 或 联系管理员.", 0);
//		}
//
//		return jobRelationList;
//	}

	// 获取job_locations
//	public List<JobLocation> getJobLocationList(int jbtype_in, String jobname, String filename) {
//		List<JobLocation> jobLocationList = new ArrayList<JobLocation>();
//		try {
//			Class.forName(driverName).newInstance();
//			conn = DriverManager.getConnection(url, g_user, g_pwd);
//			// stmt =
//			// conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				// rs =
//				// stmt.executeQuery("CALL DBO.job_location('jb_etl_dtime_dimn_foretl')");
//				// //jb_dwn_aa01pf_to_aa01pf jb_dwn_chdrpf_to_chdrpf
//				stmt.executeUpdate("CALL DBO.job_location(" + jbtype_in + ",'" + jobname + "','" + filename + "')");
//				rs = stmt.executeQuery("select jobname,x,y,isvalid,jobtype,cost from dbo.job_location order by x,y");
//				// rs =
//				// stmt.executeQuery("select jobname,x,y,cost from dbo.job_location");
//				// 将结果放入jobInfoList
//				while (rs.next()) { // RID PNUMBER PREVIOUS BNUMBER BEHIND
//									// PATHLENGTH RNUMBER2 X Y
//					JobLocation jobLocation = new JobLocation();
//					jobLocation.jobname = rs.getString("jobname");
//					jobLocation.x = rs.getInt("X");
//					jobLocation.y = rs.getInt("Y");
//					jobLocation.isvalid = rs.getInt("ISVALID");
//					jobLocation.jobtype = rs.getString("JOBTYPE");
//					jobLocation.cost = rs.getInt("COST");
//					// jobLocation.rn = rs.getInt("RN");
//					jobLocationList.add(jobLocation);
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile(this.getClass().getName()+":未知错误3,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return jobLocationList;
//	}

	// 获取job_locations v2.1 --已移植到DBUit.java.getJobLocationList2_1
//	public List<JobLocation> getJobLocationList2_1(int jbtype_in, int sourceOrTarget, String jobname) {
//		List<JobLocation> jobLocationList = new ArrayList<JobLocation>();
//		try {
//			Class.forName(driverName).newInstance();
//			conn = DriverManager.getConnection(url, g_user, g_pwd);
//			// stmt =
//			// conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				// rs =
//				// stmt.executeQuery("CALL DBO.job_location('jb_etl_dtime_dimn_foretl')");
//				// //jb_dwn_aa01pf_to_aa01pf jb_dwn_chdrpf_to_chdrpf
//				stmt.executeUpdate("CALL DBO.job_location_v2_1(" + jbtype_in + "," + sourceOrTarget + ",'" + jobname + "')");
//				rs = stmt.executeQuery("select jobname,x,y,isvalid,jobtype,cost from dbo.job_location order by x,y");
//				// rs =
//				// stmt.executeQuery("select jobname,x,y,cost from dbo.job_location");
//				// 将结果放入jobInfoList
//				while (rs.next()) { // RID PNUMBER PREVIOUS BNUMBER BEHIND
//									// PATHLENGTH RNUMBER2 X Y
//					JobLocation jobLocation = new JobLocation();
//					jobLocation.jobname = rs.getString("jobname");
//					jobLocation.x = rs.getInt("X");
//					jobLocation.y = rs.getInt("Y");
//					jobLocation.isvalid = rs.getInt("ISVALID");
//					jobLocation.jobtype = rs.getString("JOBTYPE");
//					jobLocation.cost = rs.getInt("COST");
//					// jobLocation.rn = rs.getInt("RN");
//					jobLocationList.add(jobLocation);
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("未知错误,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return jobLocationList;
//	}
		
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		WriteLog.writeFile("处理开始....");
//		DBconnect dbconn = new DBconnect();
//		System.out.println("url = " + DBconnect.getUrl());
//		List<JobLocation> jobInfoList= dbconn.getJobLocationList(1,"jb_dwn_chdrpf_to_chdrpf","");
//		System.out.println("jobname = " + jobInfoList.get(3).jobname);
//		System.out.println("jobInfoList.size = " + jobInfoList.size());
//	}



}
