package com.jupiter.mybatis.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.jgraph.graph.DefaultGraphModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.jupiter.WriteLog;
import com.jupiter.etl.jobinfo.DesignerGraph;
import com.jupiter.etl.jobinfo.InitProperty;
import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.etl.jobinfo.entities.JobEdge;
import com.jupiter.etl.jobinfo.entities.JobLocation;
import com.jupiter.etl.jobinfo.entities.JobRelation;
import com.jupiter.etl.schedule.DefaultJob;
import com.jupiter.mybatis.mapper.DBUnitMapper;
import com.jupiter.mybatis.po.User;
import com.jupiter.util.DBconnect;
import com.mchange.v2.c3p0.ComboPooledDataSource;

//@Controller("dbUtil")
public class DBUnit_bk {
//
//	private static ComboPooledDataSource ds = new ComboPooledDataSource();
//	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式
//	
//	@Autowired
//    private DBUnitMapper etlJobInoMapper;
//	
//	public List<String> CheckExistsJobname(String txt){
//		List<String> jobnames = etlJobInoMapper.checkJobnameByInput(txt);
//		
//		return jobnames;
//	}
//	
//	//-2.判断输入的job/file是否存在
//	public static ArrayList<String> checkJobnameEixts(String txt){
//		ArrayList<String> jobnameList = new ArrayList<String>(); //记录存在的jobname
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				String sql = "select distinct lower(trim(jobname))jobname from dbo.etl_jobino where lower(trim(jobname)) in("+txt+")";
//				ResultSet rs = stmt.executeQuery(sql);
//				while (rs.next()) {
//					jobnameList.add(rs.getString("jobname"));
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("未知错误1,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return jobnameList;
//	}
//	public static ArrayList<String> checkFilenameEixts(String txt){
//		ArrayList<String> filenameList = new ArrayList<String>(); //记录存在的tablename
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				String sql = "select distinct upper(trim(inofile))inofile from dbo.etl_jobino where upper(trim(inofile)) in("+txt+")";
//				ResultSet rs = stmt.executeQuery(sql);
//				while (rs.next()) {
//					filenameList.add(rs.getString("inofile"));
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("未知错误2,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return filenameList;
//	}
//	
//	//-2.1 判断的job/file是否存在
//	public static ArrayList<String> checkNameEixts(String stype,String txt){
//		ArrayList<String> l = new ArrayList<String>(); //记录存在的tablename\filename
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				String sql = "select case when name is not null then jobname1 else '' end as jobname1 from table(dbo.strExists('"+stype+"','"+txt+"'))as t" +
//						" where name is not null";
//				ResultSet rs = stmt.executeQuery(sql);
//				while (rs.next()) {
//					l.add(rs.getString("jobname1"));
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("未知错误3,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return l;
//	}
//	
//	//-1.获取调度的任务数/成功数/失败数/未完成数
//			public static int[] getScheduleStatus(String ScheduleType ,String batchno){
//				int[] scheduleStatus = new int[5];
//				try {
//					Connection conn = ds.getConnection();
//					Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//					try {
//						String sql = "select count(1)amount " +
//									",sum(case when jobStatus in(1,2) then 1 else 0 end)successAmount " +
//									",sum(case when jobStatus in(3,4) then 1 else 0 end)failAmount " +
//									",sum(case when jobStatus = 0 or jobStatus is null then 1 else 0 end)unfinishedAmount " +
//									"from ( " +
//									"select row_number()over(partition by a.jobname order by b.endtime desc nulls first)rn,a.*,b.* from dbo.jobProperty a  " +
//									"left join dbo.jobrunlog b on a.jobname = b.jobname and b.batchno = '"+batchno+"' " +
//									"where a.scheduleType = '"+ScheduleType+"' " +
//									")t where rn = 1 "; 
//						ResultSet rs = stmt.executeQuery(sql);
//						// 将结果放入jobInfoList
//						while (rs.next()) {
//							scheduleStatus[0] = rs.getInt(1);
//							scheduleStatus[1] = rs.getInt(2);
//							scheduleStatus[2] = rs.getInt(3);
//							scheduleStatus[3] = rs.getInt(4);
//						}
//					} catch (Exception e) {
//						WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//						JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//					}
//					conn.close();
//				} catch (Exception e) {
//					WriteLog.writeFile("未知错误4,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//					JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//				}
//				
//				
//				return scheduleStatus;
//			}
//	
//	//0.初始化调度，将start丢入等待池
//		public static boolean initSchedule(String scheduleType,String batchno,String jobname){
//			try {
//				Connection conn = ds.getConnection();
//				// System.out.println(conn);
//				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//				String sql = "insert into dbo.jobWaitingPool " 
//						+ "(scheduleType,batchno,headJobname,headJobtype,head_on_fail_action,headJobstatus,tailJobname,tailJobtype,tail_on_fail_action)values"
//						+ "('"+scheduleType+"','"+batchno+"','','','',1,'"+jobname+"','blankjob','STOP')";
//				System.out.println("初始化:"+sql);
//				WriteLog.writeFile("0.初始化调度，将start丢入等待池\n" + sql);
//				stmt.executeUpdate(sql);
//				/*--初始化调度，将start丢入等待池
//				 * insert into dbo.jobWaitingPool
//				(batchno,headJobname,headJobtype,tailJobname,tailJobtype,headJobstatus,on_fail_action)values
//				('test001','','','start','BLANK',100,'STOP')*/
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//				WriteLog.writeFile("initSchedule() 未知错误5,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//				return false;
//			}
//			return true;
//		}
//	
//	//1.从等待池获取下家,且该下家未进入调度
//	public static ArrayList<DefaultJob> getNextJobs(String batchno) {
//		ArrayList<DefaultJob> jobs = new ArrayList<DefaultJob>();
//		try {
//			Connection conn = ds.getConnection();
//			// System.out.println(conn);
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			String sql = "select scheduleType,batchno,jobname,jobtype,jobStatusStr as on_fail_action from final table(" +
//						  "insert into dbo.jobrunlog(scheduleType,batchno,jobname,jobtype,jobStatus,jobStatusStr,queueTime) " +
//						  	"select distinct scheduleType,batchno,tailJobname,tailJobtype,100,tail_on_fail_action,current timestamp " +
//						  	"from dbo.jobWaitingPool a " +
//						  	"where batchno = '"+batchno+"' " +
//						  	"and headjobstatus in(1,2,4) " +
//						  	"and not exists(select 1 from dbo.jobWaitingPool b " +
//					                     "where b.batchno = '"+batchno+"' " +
//					                     "and a.tailJobtype = b.tailJobtype and a.tailJobname = b.tailJobname and b.headjobstatus not in(1,2,4)) " +
//					         "and not exists(select 1 from dbo.jobrunlog c where c.batchno = '"+batchno+"' " +
//					                     				"and a.tailJobtype = c.Jobtype and a.tailJobname = c.Jobname))"  ;
//			WriteLog.writeFile("1.从等待池获取下家,且该下家未进入调度\n" );  //+ sql
//			//System.out.println("调度线程从等待池获取下家:" + sql);
//			/*--当上家状态全部为1/2/4(失败跳过),且该下家未进入调度
//			select batchno,tailJobname,tailJobtype,tail_on_fail_action,min(datetime)datetime,count(1)headJobAmount,sum(headJobstatus)headJobstatus 
//			from dbo.jobWaitingPool a 
//			where batchno = '20170730153316'and headjobstatus in(1,2,4)
//			and not exists(select 1 from dbo.jobWaitingPool b 
//			               where b.batchno = '20170730153316' and a.tailJobtype = b.tailJobtype and a.tailJobname = b.tailJobname and b.headjobstatus not in(1,2,4))
//			and not exists(select 1 from dbo.jobrunlog c 
//			               where c.batchno = '20170730153316'and a.tailJobtype = c.Jobtype and a.tailJobname = c.Jobname)
//			group by batchno,tailJobname,tailJobtype,tail_on_fail_action
//           */
//			ResultSet rs = stmt.executeQuery(sql);
//			//ResultSet rs = stmt.(sql);
//			while (rs.next()) {
//				//System.out.println("获取到下家 batchno:"+batchno + " tailJobname:" + rs.getString("tailJobname") +"     "+rs.getString("insertTime"));
//				DefaultJob job = new DefaultJob();
//				job.setBatchno(rs.getString("batchno"));
//				job.setJobname(rs.getString("jobname"));
//				job.setJobtype(rs.getString("jobtype"));
//				//job.headJobAmount = rs.getInt("headJobAmount");  //没用
//				//job.headJobstatus = rs.getInt("headJobstatus");  //没用
//				//job.setDatetime(rs.getString("insertTime"));     //没用
//				job.setOn_fail_action(rs.getString("on_fail_action"));  //on_fail_action
//				jobs.add(job);
//			}
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			WriteLog.writeFile("getNextJobs() 未知错误6,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//		}
//		return jobs;
//	}
//	
//	//2.开始运行
//	public static boolean jobStart(String scheduleType,String batchno,String jobtype ,String jobname,int jobStatus){
//		try {
//			Connection conn = ds.getConnection();
//			conn.setAutoCommit(true);//jdbc默认自动提交
//			// System.out.println(conn);
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			String sqlStr = "update dbo.jobWaitingPool set HEADJOBSTATUS = 0 where batchno = '"+batchno+"' and headJobname = '"+jobname+"'";
//			WriteLog.writeFile("2.开始运行\n" + sqlStr);
//			//System.out.println(batchno + "  " + jobname+" is ready: "+sqlStr);
//			//stmt.executeUpdate(sqlStr);
//			stmt.addBatch(sqlStr);
//			sqlStr = "insert into dbo.jobWaitingPool(scheduleType,batchno,headJobname,headjobtype,head_on_fail_action,tailJobname,tailJobtype,tail_on_fail_action,headJobstatus) " +
//					"select '"+scheduleType+"','"+batchno+"',a.headJobname,b.jobtype,b.on_fail_action,a.tailJobname,c.Jobtype,c.on_fail_action" +
//							",case when headJobname = '"+jobname+"' then "+jobStatus+" else 200 end headJobstatus " +
//					"from dbo.jobschedule a " +
//					"inner join dbo.jobProperty b on a.scheduleType = b.scheduleType and a.headJobname = b.jobname " +
//					"inner join dbo.jobProperty c on a.scheduleType = c.scheduleType and a.tailJobname = c.jobname " +
//					"where tailJobname in(select tailJobname from dbo.jobschedule b where headJobname = '"+jobname+"'" + " and a.scheduleType = b.scheduleType and b.scheduleType = '"+scheduleType+"') " + 
//					"and not exists(select 1 from dbo.jobWaitingPool b where b.batchno = '"+batchno+"' and a.tailJobname = b.tailJobname)" +  
//					"and a.scheduleType = '"+scheduleType+"'";
//			//System.out.println(batchno + "  " + jobname+" is ready: "+sqlStr);
//			WriteLog.writeFile("2.开始运行\n" + sqlStr);
//			stmt.addBatch(sqlStr);
//			/*sqlStr = "insert into dbo.jobrunlog" +
//					"(scheduleType,batchno,jobtype,jobname,starttime,jobstatus) values " +
//					"('"+scheduleType+"','"+batchno+"','"+jobtype+"','"+jobname+"',current timestamp,0)";*/
//			sqlStr = "update(select * from dbo.jobRunlog where scheduleType = '"+scheduleType+"' and batchno = '"+batchno+"' and jobname = '"+jobname+"' " +
//					         "order by id desc fetch first 1 rows only " +
//						    ")t set startTime = current timestamp,jobstatus = 0";
//			WriteLog.writeFile("2.开始运行\n" + sqlStr);
//			//System.out.println(batchno + "  " + jobname+" is ready: "+sqlStr);
//			/*
//			 update dbo.jobWaitingPool set HEADJOBSTATUS = 0 where batchno = batchno and headJobname = jobname;
//			 insert into dbo.jobrunlog
//			(batchno,jobtype,jobname,starttime,jobstatus,jobstatusStr)vlaues
//			(batchno,jobtype,jobname,current timestamp,0,'start')
//			*/
//			stmt.addBatch(sqlStr);
//			stmt.executeBatch();
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//	//3.运行完成，返回job状态， 写入状态 1-成功 2-警告 3-失败，通知下家1-完成
//	public static boolean jobEnd(String scheduleType,String batchno,String jobtype ,String jobname,int jobStatus){
//		try {
//			Connection conn = ds.getConnection();
//			conn.setAutoCommit(true);//jdbc默认自动提交
//			// System.out.println(conn);
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			//String sqlStr = "insert into dbo.jobrunlog (batchno,jobtype,jobname,endtime,jobstatus,jobstatusstr)values ('"+batchno+"','"+jobtype+"','"+jobname+"',current timestamp,"+jobStatus+",'end')";
//			String sqlStr = "update dbo.jobrunlog set endtime = current timestamp,jobstatus = "+jobStatus+" where batchno = '"+batchno+"' and jobtype = '"+jobtype+"' and jobname = '"+jobname+"'";
//			WriteLog.writeFile("运行完成，\n" + sqlStr);
//			/*
//			update dbo.jobWaitingPool set headJobstatus = 1 
//			where batchno = 'batchno' and headJobname = 'jobname'
//			 */
//			//System.out.println(jobname+" 更新运行状态："+sqlStr);
//			//stmt.executeUpdate(sqlStr);
//			stmt.addBatch(sqlStr);
//			sqlStr = "update dbo.jobWaitingPool set headJobstatus = "+jobStatus+" " +"where batchno = '"+batchno+"' and headJobname = '"+jobname+"'";
//			WriteLog.writeFile("运行完成，\n" + sqlStr);
//			//System.out.println(sqlStr);
//			//stmt.executeUpdate(sqlStr);
//			stmt.addBatch(sqlStr);
//			stmt.executeBatch();
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//	//4.发展下家，把下家扔进等待池，并告诉它需等待的上家名单
//	public static boolean jobPyramid(String scheduleType,String batchno,String jobtype ,String jobname,int jobStatus){
//		//下家可能有多位上家，但只会被第1位上家拉到等待池里,此时该下家状态为100-ready
//		try {
//			Connection conn = ds.getConnection();
//			conn.setAutoCommit(true);//jdbc默认自动提交
//			// System.out.println(conn);
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			String sqlStr = "insert into dbo.jobWaitingPool(scheduleType,batchno,headJobname,headjobtype,head_on_fail_action,tailJobname,tailJobtype,tail_on_fail_action,headJobstatus) " +
//					"select '"+scheduleType+"','"+batchno+"',a.headJobname,b.jobtype,b.on_fail_action,a.tailJobname,c.Jobtype,c.on_fail_action" +
//							",case when headJobname = '"+jobname+"' then "+jobStatus+" else 200 end headJobstatus " +
//					"from dbo.jobschedule a " +
//					"inner join dbo.jobProperty b on a.scheduleType = b.scheduleType and a.headJobname = b.jobname " +
//					"inner join dbo.jobProperty c on a.scheduleType = c.scheduleType and a.headJobname = c.jobname " +
//					"where tailJobname in(select tailJobname from dbo.jobschedule b where headJobname = '"+jobname+"'" + " and a.scheduleType = b.scheduleType and b.scheduleType = '"+scheduleType+"') " + 
//					"and not exists(select 1 from dbo.jobWaitingPool b where b.batchno = '"+batchno+"' and a.tailJobname = b.tailJobname)" +  
//					"and a.scheduleType = '"+scheduleType+"'";
//			WriteLog.writeFile("4.发展下家，把下家扔进等待池，并告诉它需等待的上家名单\n" + sqlStr); 		
//			/*--把下家扔进等待池，并告诉它需等待的上家名单
//					--发展新下家入等待池
//					insert into dbo.jobWaitingPool(scheduleType,batchno,headJobname,headjobtype,head_on_fail_action,tailJobname,tailJobtype,tail_on_fail_action,headJobstatus) 
//					select 's1','20170805151952',a.headJobname,b.jobtype,b.on_fail_action,a.tailJobname,c.Jobtype,c.on_fail_action
//							,case when headJobname = 'start' then 1 else 100 end headJobstatus 
//					from dbo.jobschedule a 
//					inner join dbo.jobProperty b on a.scheduleType = b.scheduleType and a.headJobname = b.jobname
//					inner join dbo.jobProperty c on a.scheduleType = c.scheduleType and a.headJobname = c.jobname
//					where tailJobname in(select tailJobname from dbo.jobschedule b where headJobname = 'start' and a.scheduleType = b.scheduleType and b.scheduleType = 's1')
//					and not exists(select 1 from dbo.jobWaitingPool b where b.batchno = '20170805151952' and a.tailJobname = b.tailJobname)
//					and a.scheduleType = 's1'
//			  		*/
//			//System.out.println(jobname+" 发展新下家:");//+sqlStr);
//			stmt.executeUpdate(sqlStr);
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//	
//	//10.获取job详细输入输出(双击查看)
//	public static DefaultJob getJobDetail(String jobname,String jobtype){
//		DefaultJob jobDetail = new DefaultJob();
//		try {
//			Connection conn = ds.getConnection();
//			conn.setAutoCommit(true);
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			String sql = null;
//			if (jobtype.equals("dsjob")||jobtype.equals("bojob"))//dsjob类型
//				sql = "select * from dbo.etl_jobino where jobname = '" + jobname + "'";
//			else {//若输入的是表
//				jobname = jobname.substring(jobname.lastIndexOf(".") + 1);
//				sql = "select * from dbo.etl_jobino where inofile = '" + jobname + "'";
//			}
//
//			try {
//				ResultSet rs = stmt.executeQuery(sql); 
//					jobDetail.getDetailList().add("ID	STYPE	SEQJOBNAME	JOBNAME	JOBTYPE	INO	SND	SCHEMA	INOFILE	JOBLOCATE	SRSNAME	REMARK	ISVALID	INSERTDATE	UPDATEDATE	BO_REPORTFOLDER	RMS_REPORTCODE	RMS_REPORTNAME");
//				if (jobtype.equals("dsjob")||jobtype.equals("bojob")) {
//					while (rs.next()) {
//						// System.out.println(rs.getString("JOBLOCATE"));
//						if (rs.getString("INO").equalsIgnoreCase("in"))
//							jobDetail.getInputList().add(rs.getString("snd") + "." + rs.getString("schema") + "." + rs.getString("inofile"));//
//						else
//							jobDetail.getOutputList().add(rs.getString("snd") + "." + rs.getString("schema") + "." + rs.getString("inofile"));
//						// jobDetail.getDetailList().add(rs.getMetaData());
//						jobDetail.setJoblocate(rs.getString("JOBLOCATE"));
//						String detailStr = "";
//						for (int i = 1; i <= 18; i++) {
//							detailStr += rs.getString(i) + "\t";
//						}
//						jobDetail.getDetailList().add(detailStr);
//					}
//				} else {
//					while (rs.next()) {
//						if (rs.getString("INO").equalsIgnoreCase("out"))
//							jobDetail.getInputList().add(rs.getString("jobname"));//
//						else
//							jobDetail.getOutputList().add(rs.getString("jobname"));
//						jobDetail.setJoblocate("");
//						String detailStr = "";
//						for (int i = 1; i <= 18; i++) {
//							detailStr += rs.getString(i) + "\t";
//						}
//						jobDetail.getDetailList().add(detailStr);
//					}
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误getJobDetail:\n"+sql+"\n" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (SQLException e) {
//			WriteLog.writeFile("连接数据库异常:\n"+ e.getMessage());
//			e.printStackTrace();
//		}
//		return jobDetail;
//	}
//	
//	// 获取scheduJoblocation
//	public static ArrayList<Job> initScheduleJobLocationList(String scheduleType) {
//		ArrayList<Job> jobs = new ArrayList<Job>();
//		try {
//			Connection conn = ds.getConnection();
//			// stmt =
//			// conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				//ResultSet rs = stmt.executeQuery("call dbo.initSchedule()");
//				ResultSet rs = stmt.executeQuery("select * from dbo.jobProperty where scheduleType = '"+scheduleType+"'");
//				// 将结果放入jobInfoList
//				while (rs.next()) {
//					//DefaultJob job = new DefaultJob();
//					Job job = new Job(rs.getString("jobname"),rs.getInt("ISVALID"),rs.getString("JOBTYPE"));
//					job.jobtype = rs.getString("JOBTYPE");
//					job.jobname = rs.getString("jobname");
//					job.x = rs.getDouble("X");
//					job.y = rs.getDouble("Y");
//					job.isValid = rs.getInt("ISVALID");
//					job.isSchedule = rs.getInt("isSchedule");
//					job.on_fail_action = rs.getString("ON_FAIL_ACTION");
//					job.cost = rs.getString("COST");
//					job.groupID = rs.getInt("groupID");
//					job.groupName = rs.getString("groupName");
//					job.memo = rs.getString("memo");
//					job.params = rs.getString("params");
//					job.jobstatus = 1000;//rs.getInt("jobstatus");初始状态为1000-waiting
//					jobs.add(job);
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("未知错误7,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return jobs;
//	}
//	
//	//获取scheduleJobRelation
//	public static List<JobEdge> initScheduleJobRelationList(String scheduleType) {
//		List<JobEdge> jobEdges = new ArrayList<JobEdge>(); 
//		try {
//			Connection conn = ds.getConnection();
//			//stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
//		try{
//			String sql = "select * from dbo.jobSchedule where scheduletype = '"+scheduleType+"'";
//			System.out.println("initScheduleJobRelationList "+sql);
//			ResultSet	rs = stmt.executeQuery(sql);
//		// 将结果放入jobInfoList
//		while (rs.next()) {
//			JobEdge jobEdge = new JobEdge();
//			jobEdge.headJobName = rs.getString("headJobName");
//			//jobEdge.headJobType = rs.getString("headJobType");
//			//jobEdge.headJobstatus = rs.getInt("headJobstatus");
//			//jobEdge.head_on_fail_action = rs.getString("head_on_fail_action");
//			jobEdge.tailJobName = rs.getString("tailJobName");
//			//jobEdge.tailJobType = rs.getString("tailJobType");
//			//jobEdge.tail_on_fail_action = rs.getString("tail_on_fail_action");
//			jobEdges.add(jobEdge);
//		}
//		}catch (Exception e){
//			WriteLog.writeFile("读取配置文件时出错:" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\n","未知错误1,请检查数据库连接 或 联系管理员.",0);
//		}
//		conn.close();
//	} catch (Exception e) {
//		WriteLog.writeFile("未知错误8,请检查数据库连接 或 联系管理员...\n"+e.getMessage());
//		JOptionPane.showMessageDialog(null, e.getMessage() + "\n","未知错误2,请检查数据库连接 或 联系管理员.",0);
//	}
//	return jobEdges;
//	}
//
//	//刷新调度状态
//	public static List<JobLocation> refreshScheduleJobLocationList(String batchno) {
//		List<JobLocation> jobLocationList = new ArrayList<JobLocation>();
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				/*String sql = "merge into dbo.job_location a using " +
//							 "(select jobname,max(jobstatus)jobstatus from dbo.jobrunlog b where b.batchno = '"+batchno+"' group by jobname )b "+
//							 "on a.jobname = b.jobname " +
//							 "when matched then update set a.jobstatus = b.jobstatus " +
//							 "else ignore";*/
//				//从jobrunlog获取最新的job状态
//				String sql = "select jobname,jobstatus from (select jobname,jobstatus " +
//							 			",row_number()over(partition by jobname order by insertTime desc)rn " + 
//							 			"from dbo.jobrunlog "+
//							 			"where batchno = '"+batchno+"')t where rn = 1 " +
//							 	  "union all " +
//							 "select tailJobname jobname ,100 jobstatus from dbo.jobWaitingPool a " +
//                              "where not exists(select 1 from dbo.jobRunlog b " +  
//                            		  "where a.scheduleType = b.scheduleType and a.batchno = b.batchno and b.batchno = '"+batchno+"') " +
//                                      "and a.batchno = '"+batchno+"' " ;
//				//WriteLog.writeFile("刷新调度状态\n" + sql); 	
//				//System.out.println("refreshScheduleJob:"+sql);
//				//stmt.executeUpdate(sql);
//				ResultSet rs = stmt.executeQuery(sql);
//				// 将结果放入jobInfoList
//				while (rs.next()) {
//					JobLocation jobLocation = new JobLocation();
//					jobLocation.jobname = rs.getString("jobname");
//					//jobLocation.x = rs.getInt("X");
//					//jobLocation.y = rs.getInt("Y");
//					//jobLocation.isvalid = rs.getInt("ISVALID");
//					//jobLocation.jobtype = rs.getString("JOBTYPE");
//					jobLocation.status = rs.getInt("jobstatus");
//					//jobLocation.cost = rs.getInt("COST");
//					// jobLocation.rn = rs.getInt("RN");
//					jobLocationList.add(jobLocation);
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("未知错误9,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return jobLocationList;
//	}
//			
//	// 保存作业及其依赖关系到数据库
//	public static boolean saveAllJob(String scheduleType, ArrayList<Job> jobs, ArrayList<JobEdge> jobEdges) {
//		// 1.删除将要插入的数据
//		String sqlPre1 = "delete from dbo.jobProperty where scheduleType = '" + scheduleType + "'";
//		String sqlPre2 = "delete from dbo.jobSchedule where scheduleType = '" + scheduleType + "'";
//		// 2.生成jobProperty的SQL语句
//		Job jb = jobs.get(0);
//		String sql1 = "insert into dbo.jobProperty"
//				+ "(scheduleType,jobtype,jobname,x,y,isvalid,isSchedule,on_fail_action,COST,groupID,groupName,memo,params)values";
//		for (int i = 0; i < jobs.size(); i++) {
//			jb = jobs.get(i);
//			sql1 += ((i==0)?"":",") + "('" + scheduleType + "','" + jb.jobtype + "','" + jb.jobname + "'," + jb.x + "," + jb.y + "," + jb.isValid + "," + jb.isSchedule + ",'"
//					+ jb.on_fail_action + "','" + jb.cost + "'," + jb.groupID + ",'" + jb.groupName + "','" + jb.memo + "','" + jb.params + "')\n";
//		}
//		// 3.生成jobSchedule的SQL语句
//		JobEdge jobEdge = jobEdges.get(0);
//		String sql2 = "insert into dbo.jobSchedule"
//				+ "(scheduleType,headJobname,tailJobname)values ";
//		for (int i = 0; i < jobEdges.size(); i++) {
//			jobEdge = jobEdges.get(i);
//			sql2 += ((i==0)?"":",") + "('" + scheduleType + "','" + jobEdge.headJobName + "','"+ jobEdge.tailJobName +"')\n";
//		}
//		WriteLog.writeFile("1.删除将要插入的数据\n"+sqlPre1);
//		WriteLog.writeFile("1.删除将要插入的数据\n"+sqlPre2);
//		WriteLog.writeFile("2.生成jobProperty的SQL语句\n"+sql1);
//		WriteLog.writeFile("3.生成jobSchedule的SQL语句\n"+sql2);
//		/*System.out.println(sqlPre1);
//		System.out.println(sqlPre2);
//		System.out.println(sql1);
//		System.out.println(sql2);*/
//		// 4.执行SQL语句
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				conn.setAutoCommit(false);
//				stmt.executeUpdate(sqlPre1);
//				stmt.executeUpdate(sqlPre2);
//				stmt.executeUpdate(sql1);
//				stmt.executeUpdate(sql2);
//				conn.commit();
//				/*stmt.addBatch(sqlPre1);
//				stmt.addBatch(sqlPre2);
//				stmt.addBatch(sql1);
//				stmt.addBatch(sql2);
//				stmt.executeBatch();*/
//			} catch (Exception e) {
//				WriteLog.writeFile("saveAllJob() 执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "保存失败", 0);
//				return false;
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("saveAllJob() 未知错误10,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "未知错误", 0);
//			return false;
//		}
//		JOptionPane.showMessageDialog(null, "     恭喜\n","保存成功.",1);
//		return true;
//	}
//
//	// sample for function
//	public static boolean sample(ArrayList<DefaultJob> jobs, ArrayList<JobEdge> edgs) {
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				String sql = "s";
//				ResultSet rs = stmt.executeQuery(sql);
//				// 将结果放入jobInfoList
//				while (rs.next()) {
//					JobLocation jobLocation = new JobLocation();
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("未知错误11,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return true;
//	}
//
//	//获取调度类型列表
//	public static ArrayList<String> getScheduleType() {
//		ArrayList<String> batchnos = new ArrayList<String>();
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				String sql = "select distinct scheduleType from dbo.jobProperty";
//				System.out.println(sql);
//				ResultSet rs = stmt.executeQuery(sql);
//				// 将结果放入jobInfoList
//				while (rs.next()) {
//					batchnos.add(rs.getString("scheduleType"));
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("未知错误12,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return batchnos;
//	}
//
//	//获取批次列表
//	public static ArrayList<String> getBatchno(String scheduleType){
//		ArrayList<String> batchnos = new ArrayList<String>();
//		try {
//		Connection conn = ds.getConnection();
//		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//		try {
//			//jobRunlog需要增加scheduleType字段
//			String sql = "select distinct batchno from dbo.jobRunLog where scheduleType = '"+scheduleType+"' order by batchno desc fetch first 16 rows only";// where scheduleType = '" + scheduleType +"'";
//			System.out.println(sql);
//			ResultSet rs = stmt.executeQuery(sql);
//			// 将结果放入jobInfoList
//			while (rs.next()) {
//				batchnos.add(rs.getString("batchno"));
//			}
//		} catch (Exception e) {
//			WriteLog.writeFile("执行SQL错误:" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "执行SQL错误", 0);
//		}
//		conn.close();
//	} catch (Exception e) {
//		WriteLog.writeFile("未知错误13,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//		JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//	}
//	return batchnos;
//}
//	
//	// 获取job_locations v3.0
//	public static List<JobLocation> getJobLocationList3_0(User user,int jbtype_in, int sourceOrTarget, String jobname) {
//		List<JobLocation> jobLocationList = new ArrayList<JobLocation>();
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//			try {
//				//String sql = "CALL DBO.job_location_v2_1(" + jbtype_in + "," + sourceOrTarget + ",'" + jobname + "')";
//				String sql = "CALL DBO.job_location_v3_0(" + jbtype_in + "," + sourceOrTarget + ",'" + jobname + "','"+user.getUserID()+"','"+sdf.format(user.getOptionTime())+"')";
//				WriteLog.writeFile("excute:" + sql);
//				stmt.executeUpdate(sql);
//				sql = "select jobname,x,y,isvalid,jobtype,cost from dbo.job_location where userid = '"+user.getUserID()+"' and datime = '"+sdf.format(user.getOptionTime())+"' order by x,y";
//				WriteLog.writeFile("excute:" + sql);
//				ResultSet rs = stmt.executeQuery(sql);
//				// 将结果放入jobInfoList
//				while (rs.next()) { // RID PNUMBER PREVIOUS BNUMBER BEHIND
//					// PATHLENGTH RNUMBER2 X Y
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
//			stmt.close();
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("未知错误14,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + DBconnect.getUrl(), "错误", 0);
//		}
//		return jobLocationList;
//	}
//
//	//获取job_relation
//	public static List<JobRelation> getJobRelationList(User user) {
//			List<JobRelation> jobRelationList = new ArrayList<JobRelation>();
//			String userID = user.getUserID();
//			Date optionTime = user.getOptionTime();
//			try {
//				Connection conn = ds.getConnection();
//				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // 解决36001错误
//				try {
//					String sql = "";
//					sql = "select a.*,b.x,b.y,row_number()over(partition by BEHIND order by PATHLENGTH)rn " +
//							"from dbo.job_relation a " +
//							"inner join dbo.job_location b on a.BEHIND = b.JOBNAME and a.userid = b.userid and a.datime = b.datime " +
//							"where a.userid = '"+userID+"' and a.datime = '"+sdf.format(optionTime)+
//							"' order by RID,PREVIOUS,BNUMBER";
//					WriteLog.writeFile("excute:"+sql);
//					ResultSet rs = stmt.executeQuery(sql);
//					// select * from dbo.job_relation
//					// 将结果放入jobInfoList
//					while (rs.next()) { // RID PNUMBER PREVIOUS BNUMBER BEHIND
//										// PATHLENGTH RNUMBER2 X Y
//						JobRelation jobinfo = new JobRelation();
//						jobinfo.rid = rs.getInt("RID");
//						jobinfo.pnumber = rs.getInt("PNUMBER");
//						jobinfo.previous = rs.getString("PREVIOUS");
//						jobinfo.bnumber = rs.getInt("BNUMBER");
//						jobinfo.behind = rs.getString("BEHIND");
//						jobinfo.pathlength = rs.getInt("PATHLENGTH");
//						jobinfo.rnumber2 = rs.getInt("RNUMBER2");
//						jobinfo.x = rs.getInt("X");
//						jobinfo.y = rs.getInt("Y");
//						jobinfo.rn = rs.getInt("RN");
//						jobRelationList.add(jobinfo);
//					}
//				} catch (Exception e) {
//					WriteLog.writeFile("DBUnit : 未知错误1,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//					JOptionPane.showMessageDialog(null, e.getMessage() + "\n", "未知错误1,请检查数据库连接 或 联系管理员.", 0);
//				}
//				stmt.close();
//				conn.close();
//			} catch (Exception e) {
//				WriteLog.writeFile("DBUnit : 未知错误2,请检查数据库连接 或 联系管理员...\n" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\n", "未知错误2,请检查数据库连接 或 联系管理员.", 0);
//			}
//
//			return jobRelationList;
//		}
//
//	//登录
//	public static int login(InitProperty ip) {
//		int loginStatus = 0;
//		String sql = "";
//		Connection conn = null;
//		Statement stmt = null;
//		try {
//			conn = ds.getConnection();
//			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
//		} catch (Exception e) {
//			//无法连接数据库
//			return -1;
//		}
//		try {
//			sql = "select count(1) as LoginStatus from dbo.Jobio_User where UserName = '"
//					+ip.getLoginUser()+"' and Password = '" 
//					+ip.getLoginPassword()+"'";
//			com.jupiter.WriteLog.writeFile("excute:"+sql);
//			ResultSet rs = stmt.executeQuery(sql);
//			if (rs.next())
//				loginStatus = rs.getInt("LOGINSTATUS");
//			stmt.close();
//			conn.close();
//		} catch (SQLException e) {
//			//登录用户密码错误
//			e.printStackTrace();
//		}
//		return loginStatus;
//	}
//
//	//获取最新版本
//	public static double getLastVersion(){
//		double version = 0;
//			Connection conn;
//			try {
//				conn = ds.getConnection();
//				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
//				ResultSet rs = stmt.executeQuery("select max(version)version from dbo.Jobio_Version");
//				if (rs.next())
//					version = rs.getDouble("VERSION");
//				stmt.close();
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		return version;
//	}
//	
//	//当前版本是否可用
//	public static boolean isVersionUseable(double version){
//		boolean useable = false;
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt;
//				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//				ResultSet rs = stmt.executeQuery("select useable from dbo.Jobio_Version where version = "+version);
//				if (rs.next())
//					useable = rs.getString("useable").equalsIgnoreCase("Y") ? true : false;
//				stmt.close();
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} 
//			
//		return useable;
//	}
//	
//	public static void main(String[] args) throws Exception {
//		//DBUnit t = new DBUnit();
//		/*int[] arr = DBUnit.getScheduleStatus("s1", "20170806020126");
//		for (int n : arr)
//			System.out.println(n + ",");*/
//		//System.out.println(DBUnit.checkNameEixts("bus", "a,zz,s01_echdpf,aa01pf,jb_dwn_aa01pf_to_aa01pf").toString());
//		System.out.println(DBUnit_bk.getLastVersion());
//		System.out.println(DBUnit_bk.isVersionUseable(3.0));
//	}
//	
//	//获取单个job的孩子
//	public static ArrayList<Job> getChildren(Job job,DesignerGraph graph) {
//		ArrayList<Job> children = new ArrayList<Job>();
//		ArrayList<Job> jobsExists = graph.getAllJobs();
//		double rootX = job.getX();
//		double rootY = job.getY();
//		String rootJobtype = job.getJobtype();
//		String sql = "";
//		if (rootJobtype.equals("TAB"))
//			sql = "select distinct jobname,isvalid,jobtype from dbo.etl_jobino where inofile = '" 
//					+ job.jobname + "' and ino = 'in'";
//		else
//			sql = "select distinct b.jobname,b.ISVALID,b.jobtype from dbo.etl_jobino a ,dbo.etl_jobino b where a.inofile = b.inofile and a.ino = 'out' and b.ino = 'in' and a.jobname = '" 
//					+ job.jobname + "' and a.jobname <> b.jobname";
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
//			WriteLog.writeFile("excute:"+sql);
//			ResultSet rs = stmt.executeQuery(sql);
//			double i = 0;
//			int j = -1;
//			int length = job.jobname.length()*7;
//			int vx = 50 + length;
//			int vy = 28;
//			f : while(rs.next()){
//				String jobname = rs.getString("JOBNAME");
//				int isValid = rs.getInt("ISVALID");
//				String jobtype = rs.getString("JOBTYPE");
//				for(int e=0;e<jobsExists.size();e++){
//					Job jobExists = jobsExists.get(e);
//					if(jobname.equalsIgnoreCase(jobExists.getJobname())){
//						//如果相同的Job已经存在，结束循环
//						children.add(jobExists);
//						continue f;
//					}
//				}
//				i = (i+0.5);
//				j *= -1;
//				int p = (int) (Math.floor(i)*j);
//				int vx1 = vx*vx-(vy*p)*(vy*p);
//				if(vx1 < 0) {
//					i = 0.5;
//					vx += 200;
//					//vy += 6;
//					rs.previous();
//					continue;
//				}
//				double x = rootX+Math.sqrt(vx1);
//				double y = rootY+vy*p;
//				if(y<0){
//					rs.previous();
//					continue;
//				}
//				System.out.println("x:"+x +",y:"+y);
//				Job child = new Job(x,y,jobname,isValid,jobtype);
//				//System.out.println(child.printProperty());
//				children.add(child);
//			}
//			stmt.close();
//			conn.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return children;
//		}
//		return children;
//	}
//	
//	//获取单个job的roots
//		public static ArrayList<Job> getRoots(Job job,DesignerGraph graph) {
//			ArrayList<Job> roots = new ArrayList<Job>();
//			ArrayList<Job> jobsExists = graph.getAllJobs();
//			double rootX = job.getX();
//			double rootY = job.getY();
//			String rootJobtype = job.getJobtype();
//			String sql = "";
//			if (rootJobtype.equals("TAB"))
//				sql = "select distinct jobname,isvalid,jobtype from dbo.etl_jobino where inofile = '" 
//						+ job.jobname + "' and ino = 'out'";
//			else
//				sql = "select distinct b.jobname,b.ISVALID,b.jobtype from dbo.etl_jobino a ,dbo.etl_jobino b where a.inofile = b.inofile and a.ino = 'in' and b.ino = 'out' and a.jobname = '" 
//						+ job.jobname + "' and a.jobname <> b.jobname";
//			try {
//				Connection conn = ds.getConnection();
//				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
//				WriteLog.writeFile("excute:"+sql);
//				ResultSet rs = stmt.executeQuery(sql);
//				double i = 0;
//				int j = -1;
//				int length = job.jobname.length()*7;
//				int vx = 50 + length;
//				int vy = 28;
//				f : while(rs.next()){
//					String jobname = rs.getString("JOBNAME");
//					int isValid = rs.getInt("ISVALID");
//					String jobtype = rs.getString("JOBTYPE");
//					for(int e=0;e<jobsExists.size();e++){
//						Job jobExists = jobsExists.get(e);
//						if(jobname.equalsIgnoreCase(jobExists.getJobname())){
//							//如果相同的Job已经存在，结束循环
//							roots.add(jobExists);
//							continue f;
//						}
//					}
//					i = (i+0.5);
//					j *= -1;
//					int p = (int) (Math.floor(i)*j);
//					int vx1 = vx*vx-(vy*p)*(vy*p);
//					if(vx1 < 0) {
//						i = 0.5;
//						vx += 200;
//						//vy += 6;
//						rs.previous();
//						continue;
//					}
//					double x = rootX+Math.sqrt(vx1);
//					double y = rootY+vy*p;
//					if(y<0){
//						rs.previous();
//						continue;
//					}
//					System.out.println("x:"+x +",y:"+y);
//					Job child = new Job(x,y,jobname,isValid,jobtype);
//					//System.out.println(child.printProperty());
//					roots.add(child);
//				}
//				stmt.close();
//				conn.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//				return roots;
//			}
//			return roots;
//		}

}
