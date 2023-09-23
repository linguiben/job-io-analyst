package com.jupiter;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.jupiter.WriteLog;
import com.jupiter.DSXMLParse.Collection;
import com.jupiter.DSXMLParse.Job;
import com.jupiter.DSXMLParse.Property;
import com.jupiter.DSXMLParse.Record;
import com.jupiter.DSXMLParse.SubRecord;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class WriteDSParse {

	private static ComboPooledDataSource ds = new ComboPooledDataSource();
	public SimpleDateFormat dfstr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式

	//0.Job
	public static boolean writeJob(int JobID, String Identifier, String DateModified, String TimeModified) {
		try {
			Connection conn = ds.getConnection();
			// System.out.println(conn);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "INSERT INTO DBO.PARSE_JOB" 
				+ "(JobID,Identifier,DateModified,TimeModified)VALUES" 
				+ "("+JobID + ",'" + Identifier + "','" + DateModified + "','" + TimeModified + "')";
			System.out.println("writeJob.sql:" + sql);
			stmt.executeUpdate(sql);
			//conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean clearAll() {
		try {
			Connection conn = ds.getConnection();
			// System.out.println(conn);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.addBatch("delete from dbo.parse_job");
			stmt.addBatch("delete from dbo.Parse_Record");
			stmt.addBatch("delete from dbo.Parse_Property");
			stmt.addBatch("delete from dbo.Parse_Collection");
			stmt.addBatch("delete from dbo.Parse_SubRecord");
			/*String sql = "delete from dbo.parse_job; " +
					"delete from dbo.Parse_Record;" +
					"delete from DBO.Parse_Property; " +
					"delete from dbo.Parse_Collection; " +
					"delete from dbo.Parse_SubRecord; ";*/
			/*String sql2 = "delete from dbo.parse_job ";
			String sql3 = "delete from dbo.parse_job ";
			String sql4 = "delete from dbo.parse_job ";
			String sql5 = "delete from dbo.parse_job ";*/
			//stmt.executeUpdate(sql);
			stmt.executeBatch();
			//conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// 1.保存JobList
	public static boolean writeJobs(ArrayList<Job> jobs) {
		// 1.删除将要插入的数据
		//String sqlPre1 = "delete from dbo.parse_job ";
		// 2.生成jobProperty的SQL语句
		Job jb = jobs.get(0);
		// 第一段前面无需逗号
		String sql1 = "INSERT INTO DBO.PARSE_JOB" 
			+ "(JobID,Identifier,DateModified,TimeModified)VALUES" 
			+ "("+jb.getJobID()+",'"+jb.getIdentifier()+"','"+jb.getDateModified()+"','"+jb.getTimeModified()+"')\n";
		for (int i = 1; i < jobs.size(); i++) {
			jb = jobs.get(i);
			sql1 += ",(" +jb.getJobID()+",'"+jb.getIdentifier()+"','"+jb.getDateModified()+"','"+jb.getTimeModified()+"')\n";
		}
		//com.WriteLog.writeFile(sqlPre1);
		com.jupiter.WriteLog.writeFile(sql1);
		// 3.执行SQL语句
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
			try {
				//stmt.executeUpdate(sqlPre1);
				stmt.executeUpdate(sql1);
			} catch (Exception e) {
				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "执行SQL错误", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("未知错误,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "错误", 0);
		}
		return true;
	}

	// 2.保存RecordList
	public static boolean writeRecords(ArrayList<Record> records) {
		// 1.删除将要插入的数据
		//String sqlPre1 = "delete from dbo.Parse_Record ";
		// 2.生成jobProperty的SQL语句
		Record rc = records.get(0);
		// 第一段前面无需逗号
		String sql1 = "INSERT INTO DBO.Parse_Record" 
			+ "(JobID,RecordID,Readonly,Identifier,Type)VALUES" 
			+ "("+rc.getJobID()+","+rc.getRecordID()+","+rc.getReadonly()+",'"+rc.getIdentifier()+"','"+rc.getType()+"')\n";
		for (int i = 1; i < records.size(); i++) {
			rc = records.get(i);
			sql1 += ",("+rc.getJobID()+","+rc.getRecordID()+","+rc.getReadonly()+",'"+rc.getIdentifier()+"','"+rc.getType()+"')\n";
		}
		//com.WriteLog.writeFile(sqlPre1);
		com.jupiter.WriteLog.writeFile(sql1);
		// 3.执行SQL语句
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
			try {
				//stmt.executeUpdate(sqlPre1);
				stmt.executeUpdate(sql1);
			} catch (Exception e) {
				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "执行SQL错误", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("未知错误,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "错误", 0);
		}
		return true;
	}
	
	// 3.保存PropertyList
	public static boolean writePropertys(ArrayList<Property> propertys) {
		// 1.删除将要插入的数据
		//String sqlPre1 = "delete from dbo.Parse_Property ";
		// 2.生成jobProperty的SQL语句
		Property pp = propertys.get(0);
		// 第一段前面无需逗号
		String sql1 = "INSERT INTO DBO.Parse_Property" 
			+ "(JobID,RecordID,PropertyID,CollectionID,SubRecordID,SubPropertyID,Name,Value,PreFormatted)VALUES" 
			+ "("+pp.getJobID()+","+pp.getRecordID()+","+pp.getPropertyID()+","+pp.getCollectionID()+","+pp.getSubRecordID()+","+pp.getSubPropertyID()+",'"+pp.getName()+"','"+pp.getValue()+"','"+pp.getPreFormatted()+"')\n";
		for (int i = 1; i < propertys.size(); i++) {
			pp = propertys.get(i);
			sql1 += ",("+pp.getJobID()+","+pp.getRecordID()+","+pp.getPropertyID()+","+pp.getCollectionID()+","+pp.getSubRecordID()+","+pp.getSubPropertyID()+",'"+pp.getName()+"','"+pp.getValue()+"','"+pp.getPreFormatted()+"')\n";
		}
		//com.WriteLog.writeFile(sqlPre1);
		com.jupiter.WriteLog.writeFile(sql1);
		// 3.执行SQL语句
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
			try {
				//stmt.executeUpdate(sqlPre1);
				stmt.executeUpdate(sql1);
			} catch (Exception e) {
				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "执行SQL错误", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("未知错误,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "错误", 0);
		}
		return true;
	}
	
	// 4.保存CollectionList
	public static boolean writeCollections(ArrayList<Collection> collections) {
		// 1.删除将要插入的数据
		//String sqlPre1 = "delete from dbo.Parse_Collection ";
		// 2.生成jobProperty的SQL语句
		Collection clt = collections.get(0);
		// 第一段前面无需逗号
		String sql1 = "INSERT INTO DBO.Parse_Collection" 
			+ "(JobID,RecordID,CollectionID,Name,Type)VALUES" 
			+ "("+clt.getJobID()+","+clt.getRecordID()+","+clt.getCollectionID()+",'"+clt.getName()+"','"+clt.getType()+"')\n";
		for (int i = 1; i < collections.size(); i++) {
			clt = collections.get(i);
			sql1 += ",("+clt.getJobID()+","+clt.getRecordID()+","+clt.getCollectionID()+",'"+clt.getName()+"','"+clt.getType()+"')\n";
		}
		//com.WriteLog.writeFile(sqlPre1);
		com.jupiter.WriteLog.writeFile(sql1);
		// 3.执行SQL语句
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
			try {
				//stmt.executeUpdate(sqlPre1);
				stmt.executeUpdate(sql1);
			} catch (Exception e) {
				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "执行SQL错误", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("未知错误,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "错误", 0);
		}
		return true;
	}
	
	// 5.保存SubRecordList
	public static boolean writeSubRecords(ArrayList<SubRecord> subRecords) {
		// 1.删除将要插入的数据
		//String sqlPre1 = "delete from dbo.Parse_SubRecord ";
		// 2.生成jobProperty的SQL语句
		SubRecord sr = subRecords.get(0);
		// 第一段前面无需逗号
		String sql1 = "INSERT INTO DBO.Parse_SubRecord" 
			+ "(JobID,RecordID,CollectionID,SubRecordID)VALUES" 
			+ "("+sr.getJobID()+","+sr.getRecordID()+","+sr.getCollectionID()+","+sr.getSubRecordID()+")\n";
		for (int i = 1; i < subRecords.size(); i++) {
			sr = subRecords.get(i);
			sql1 += ",("+sr.getJobID()+","+sr.getRecordID()+","+sr.getCollectionID()+","+sr.getSubRecordID()+")\n";
		}
		//com.WriteLog.writeFile(sqlPre1);
		com.jupiter.WriteLog.writeFile(sql1);
		// 3.执行SQL语句
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
			try {
				//stmt.executeUpdate(sqlPre1);
				stmt.executeUpdate(sql1);
			} catch (Exception e) {
				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "执行SQL错误", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("未知错误,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + ds.getJdbcUrl(), "错误", 0);
		}
		return true;
	}
	
	public static void main(String[] args) {
		WriteDSParse t = new WriteDSParse();
		t.writeJob(2, "parse1", "2018-04-13", "14.29.02");
		//System.out.println(ds.getJdbcUrl());
	}

}
