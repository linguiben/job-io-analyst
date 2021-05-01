package com.jupiter.sqlparse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import com.jupiter.WriteLog;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ParseJobio {

	//private static String logFile = "D:\\jupiter\\workplace\\012.dsjob_analysis\\1.business\\5.dsexport\\parseXML.log";
	private static String logFile = "jobino.log";
	private static ComboPooledDataSource ds = new ComboPooledDataSource();

	// 获取Stype,Jobname,RECORD,StageID,StageName,StageType,InputPins,OutputPins,Server,UserName,TimeOut,Input_BEFORESQL,Input_SQL,Input_AFTERSQL,Oupt_BEFORESQL,Output_SQL,Oupt_AFTERSQL
	public static HashSet<JobInputOutput> getJobIo(ParseSQL p,String stype, String jobname) {
		HashSet<JobInputOutput> jobinoSet = new HashSet<JobInputOutput>();
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "select * from table(db2inst1.get_JobioSQL('" + stype + "','" + jobname + "'))t " + "where STAGETYPE <> 'CInterProcess' ";
					/*debug临时增加*/ //+ " and server = '#v_jb_dw_svr#' and stageName = 'ETL_NBP_MISS_BRANCH_PARAM'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				/*String StageType = rs.getString("StageType");
				String StageID = rs.getString("StageID");
				String StageName = rs.getString("StageName");*/
			    String Joblocate = rs.getString("JOBLOCATE");
				String ServerName = rs.getString("SERVER");
				String jobType = rs.getString("JOBTYPE");
				JobInfo job = new JobInfo(stype,jobType,Joblocate,jobname);
				
				p.parseSQLs(rs.getString("Input_BEFORESQL"),ServerName,job);
				jobinoSet.addAll(p.getJobinoSet());
				p.parseSQLs(rs.getString("Input_SQL"),ServerName,job);
				jobinoSet.addAll(p.getJobinoSet());
				p.parseSQLs(rs.getString("Input_AFTERSQL"),ServerName,job);
				jobinoSet.addAll(p.getJobinoSet());
				
				p.parseSQLs(rs.getString("Output_BEFORESQL"),ServerName,job);
				jobinoSet.addAll(p.getJobinoSet());
				p.parseSQLs(rs.getString("Output_SQL"),ServerName,job);
				jobinoSet.addAll(p.getJobinoSet());
				p.parseSQLs(rs.getString("Output_AFTERSQL"),ServerName,job);
				jobinoSet.addAll(p.getJobinoSet());
							
				
				// 用Jobino记录分析后的输入输出
				/*Iterator<Jobino> it = jobinoSet.iterator();
				while (it.hasNext()) {
					Jobino i = it.next();
					i.jobname = Jobname;
					i.serverName = ServerName;
				}*/
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,e.toString(),"error:",0);
			System.exit(0);
		}

		WriteLog.writeFile(logFile, "jobinoSet:" + jobinoSet.toString());
		// writeJobIo(jobinoSet);
		return jobinoSet;
	}

	// 将不支持的Stype.Jobname.StageID记录下来
	public static void writeErrLog(String Stype, String Jobname, String StageID,String Remark, String Err) {
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "insert into dsxml_JobioSQL_UnSupport(Stype,Jobname,StageID,Remark,Err) values('" + Stype + "','" + Jobname + "','" + StageID + "','" +Remark+"','" 
			+Err.replace("'", "''")+ "')";
			WriteLog.writeFile(logFile,"ErrLog sql: " + sql);
			stmt.execute(sql);
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 写入ETL_JOBINO
	public static void writeJobIo(Set<JobInputOutput> set) {
	    if(set.size()==0) return;
		Iterator<JobInputOutput> it = set.iterator();
		String sql = "";
		while (it.hasNext()) {
			JobInputOutput j = (JobInputOutput) it.next();
            /*
             * String jobtype = ""; switch(j.stype.substring(0, 3)) { case "ktl" : jobtype =
             * "ktljob"; break; case "pro" : jobtype = "proc"; break; default: jobtype =
             * "dsjob"; }
             */
			sql += ",('" + j.stype + "','" + j.jobtype+ "','" + j.joblocate+ "','" + j.jobname + "','" + j.ino + "','" + j.serverName + "','" + j.schema + "','" + j.inofile.replace("'","''") + "')";
		}

		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sql = "insert into dbo.etl_jobino_tmp(STYPE,JOBTYPE,JOBLOCATE,JOBNAME,INO,SND,SCHEMA,INOFILE)values " + (sql.length()>0 ? sql.substring(1) : "");
			WriteLog.writeFile(logFile,"sql: " + sql);
			stmt.execute(sql);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,e.toString(),"error:",0);
			System.exit(0);
		}

	}

	public static String getLogFile() {
		return logFile;
	}
	public static void setLogFile(String logFile) {
		ParseJobio.logFile = logFile;
	}
	
	
	public static void main(String[] args) {
		//get Job List
		String stype = "ktlzbx_20210113";//fin_20200511 bus_20200511 adpp_20200825 ktlrep_20210111 ktlzbx_20210113
		String jobname = "";  //jb_dwn_ot_activity_point_tbl_all kjb_ods_to_ods_hldtran_inc
		ParseSQL p = new ParseSQL();
		String sql = "";
		List<String> j = new ArrayList<String>();
		try {
			Connection conn = ds.getConnection();
			//conn.setTransactionIsolation(Connection.)
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if(stype.substring(0, 3).equalsIgnoreCase("ktl")) {
			    sql = "select Jobname from ktl_jobInfo where stype = '"+stype+"'" + (jobname.isEmpty() ? "" : "and jobname = '"+jobname+"'");
			}
			else {
			    sql = "select Jobname from dsxml_Jobinfo where stype = '"+stype+"' and jobname not like 'CopyOf%' and jobname not like '副本%' "
			            + (jobname.isEmpty() ? "" : "and jobname = '"+jobname+"'");// order by rand() fetch first 200 rows only ";
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				j.add(rs.getString("Jobname"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			WriteLog.writeFile(ParseJobio.getLogFile(),e.toString());
		}
		// 分析jobsql
		int size = j.size();
		for (int i = 0; i < size; i++) {
			System.out.println(i+1+"/"+size+":" + j.get(i));
			HashSet<JobInputOutput> s = ParseJobio.getJobIo(p, stype, j.get(i));
			if (s.size() > 0)
				writeJobIo(s);
		}
	}

}

