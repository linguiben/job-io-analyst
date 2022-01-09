package com.jupiter.mybatis.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.jupiter.WriteLog;
import com.jupiter.etl.jobinfo.DesignerGraph;
import com.jupiter.etl.jobinfo.InitProperty;
import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.etl.jobinfo.entities.JobEdge;
import com.jupiter.etl.jobinfo.entities.JobLocation;
import com.jupiter.etl.jobinfo.entities.JobRelation;
import com.jupiter.etl.schedule.DefaultJob;
import com.jupiter.mybatis.mapper.DBUnitMapper;
import com.jupiter.mybatis.mapper.UserMapper;
import com.jupiter.mybatis.po.User;
//import com.jupiter.test.C3P0DataSourceFactory;
import com.jupiter.util.DBconnect;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Repository("dbUnit")
public class DBUnit {

	private static ComboPooledDataSource ds = new ComboPooledDataSource();
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//�������ڸ�ʽ
	static Logger logger = Logger.getLogger(DBUnit.class);
	
	
	@Autowired
    private DBUnitMapper dbUnitMapper;
	@Autowired
	private UserMapper userMapper;
	
	public boolean checkUser(User user) {
		User user1 = userMapper.checkUser(user);
		return user1 != null;
	}
	
	public List<String> CheckExistsJobname(String txt){
		List<String> jobnames = dbUnitMapper.checkJobnameByInput(txt);
		return jobnames;
	}
	
	//-2.�ж������job/file�Ƿ����
//	public static ArrayList<String> checkJobnameEixts(String txt){
//		ArrayList<String> jobnameList = new ArrayList<String>(); //��¼���ڵ�jobname
//		try {
//			Connection conn = ds.getConnection();
//			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
//			try {
//				String sql = "select distinct lower(trim(jobname))jobname from dbo.etl_jobino where lower(trim(jobname)) in("+txt+")";
//				ResultSet rs = stmt.executeQuery(sql);
//				while (rs.next()) {
//					jobnameList.add(rs.getString("jobname"));
//				}
//			} catch (Exception e) {
//				WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
//				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
//			}
//			conn.close();
//		} catch (Exception e) {
//			WriteLog.writeFile("δ֪����1,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
//			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
//		}
//		return jobnameList;
//	}
	public static ArrayList<String> checkFilenameEixts(String txt){
		ArrayList<String> filenameList = new ArrayList<String>(); //��¼���ڵ�tablename
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
			try {
				String sql = "select distinct upper(trim(inofile))inofile from dbo.etl_jobino where upper(trim(inofile)) in("+txt+")";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					filenameList.add(rs.getString("inofile"));
				}
			} catch (Exception e) {
				WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("δ֪����2,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
		}
		return filenameList;
	}
	
	//-2.1 �жϵ�job/file�Ƿ����
	public static ArrayList<String> checkNameEixts(String stype,String txt){
		ArrayList<String> l = new ArrayList<String>(); //��¼���ڵ�tablename\filename
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
			try {
				String sql = "select case when name is not null then jobname1 else '' end as jobname1 from table(dbo.strExists('"+stype+"','"+txt+"'))as t" +
						" where name is not null";
				System.out.println("getNotExistsJobs:"+sql);
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					l.add(rs.getString("jobname1"));
				}
			} catch (Exception e) {
				WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("δ֪����3,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
		}
		return l;
	}
	
	//-1.��ȡ���ȵ�������/�ɹ���/ʧ����/δ�����
			public static int[] getScheduleStatus(String ScheduleType ,String batchno){
				int[] scheduleStatus = new int[5];
				try {
					Connection conn = ds.getConnection();
					Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
					try {
						String sql = "select count(1)amount " +
									",sum(case when jobStatus in(1,2) then 1 else 0 end)successAmount " +
									",sum(case when jobStatus in(3,4) then 1 else 0 end)failAmount " +
									",sum(case when jobStatus = 0 or jobStatus is null then 1 else 0 end)unfinishedAmount " +
									"from ( " +
									"select row_number()over(partition by a.jobname order by b.endtime desc nulls first)rn,a.*,b.* from dbo.jobProperty a  " +
									"left join dbo.jobrunlog b on a.jobname = b.jobname and b.batchno = '"+batchno+"' " +
									"where a.scheduleType = '"+ScheduleType+"' " +
									")t where rn = 1 "; 
						ResultSet rs = stmt.executeQuery(sql);
						// ���������jobInfoList
						while (rs.next()) {
							scheduleStatus[0] = rs.getInt(1);
							scheduleStatus[1] = rs.getInt(2);
							scheduleStatus[2] = rs.getInt(3);
							scheduleStatus[3] = rs.getInt(4);
						}
					} catch (Exception e) {
						WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
						JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
					}
					conn.close();
				} catch (Exception e) {
					WriteLog.writeFile("δ֪����4,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
					JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
				}
				
				
				return scheduleStatus;
			}
	
	//0.��ʼ�����ȣ���start����ȴ���
		public static boolean initSchedule(String scheduleType,String batchno,String jobname){
			try {
				Connection conn = ds.getConnection();
				// System.out.println(conn);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String sql = "insert into dbo.jobWaitingPool " 
						+ "(scheduleType,batchno,headJobname,headJobtype,head_on_fail_action,headJobstatus,tailJobname,tailJobtype,tail_on_fail_action)values"
						+ "('"+scheduleType+"','"+batchno+"','','','',1,'"+jobname+"','blankjob','STOP')";
				System.out.println("��ʼ��:"+sql);
				WriteLog.writeFile("0.��ʼ�����ȣ���start����ȴ���\n" + sql);
				stmt.executeUpdate(sql);
				/*--��ʼ�����ȣ���start����ȴ���
				 * insert into dbo.jobWaitingPool
				(batchno,headJobname,headJobtype,tailJobname,tailJobtype,headJobstatus,on_fail_action)values
				('test001','','','start','BLANK',100,'STOP')*/
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				WriteLog.writeFile("initSchedule() δ֪����5,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
				return false;
			}
			return true;
		}
	
	//1.�ӵȴ��ػ�ȡ�¼�,�Ҹ��¼�δ�������
	public static ArrayList<DefaultJob> getNextJobs(String batchno) {
		ArrayList<DefaultJob> jobs = new ArrayList<DefaultJob>();
		try {
			Connection conn = ds.getConnection();
			// System.out.println(conn);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "select scheduleType,batchno,jobname,jobtype,jobStatusStr as on_fail_action from final table(" +
						  "insert into dbo.jobrunlog(scheduleType,batchno,jobname,jobtype,jobStatus,jobStatusStr,queueTime) " +
						  	"select distinct scheduleType,batchno,tailJobname,tailJobtype,100,tail_on_fail_action,current timestamp " +
						  	"from dbo.jobWaitingPool a " +
						  	"where batchno = '"+batchno+"' " +
						  	"and headjobstatus in(1,2,4) " +
						  	"and not exists(select 1 from dbo.jobWaitingPool b " +
					                     "where b.batchno = '"+batchno+"' " +
					                     "and a.tailJobtype = b.tailJobtype and a.tailJobname = b.tailJobname and b.headjobstatus not in(1,2,4)) " +
					         "and not exists(select 1 from dbo.jobrunlog c where c.batchno = '"+batchno+"' " +
					                     				"and a.tailJobtype = c.Jobtype and a.tailJobname = c.Jobname))"  ;
			WriteLog.writeFile("1.�ӵȴ��ػ�ȡ�¼�,�Ҹ��¼�δ�������\n" );  //+ sql
			//System.out.println("�����̴߳ӵȴ��ػ�ȡ�¼�:" + sql);
			/*--���ϼ�״̬ȫ��Ϊ1/2/4(ʧ������),�Ҹ��¼�δ�������
			select batchno,tailJobname,tailJobtype,tail_on_fail_action,min(datetime)datetime,count(1)headJobAmount,sum(headJobstatus)headJobstatus 
			from dbo.jobWaitingPool a 
			where batchno = '20170730153316'and headjobstatus in(1,2,4)
			and not exists(select 1 from dbo.jobWaitingPool b 
			               where b.batchno = '20170730153316' and a.tailJobtype = b.tailJobtype and a.tailJobname = b.tailJobname and b.headjobstatus not in(1,2,4))
			and not exists(select 1 from dbo.jobrunlog c 
			               where c.batchno = '20170730153316'and a.tailJobtype = c.Jobtype and a.tailJobname = c.Jobname)
			group by batchno,tailJobname,tailJobtype,tail_on_fail_action
           */
			ResultSet rs = stmt.executeQuery(sql);
			//ResultSet rs = stmt.(sql);
			while (rs.next()) {
				//System.out.println("��ȡ���¼� batchno:"+batchno + " tailJobname:" + rs.getString("tailJobname") +"     "+rs.getString("insertTime"));
				DefaultJob job = new DefaultJob();
				job.setBatchno(rs.getString("batchno"));
				job.setJobname(rs.getString("jobname"));
				job.setJobtype(rs.getString("jobtype"));
				//job.headJobAmount = rs.getInt("headJobAmount");  //û��
				//job.headJobstatus = rs.getInt("headJobstatus");  //û��
				//job.setDatetime(rs.getString("insertTime"));     //û��
				job.setOn_fail_action(rs.getString("on_fail_action"));  //on_fail_action
				jobs.add(job);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			WriteLog.writeFile("getNextJobs() δ֪����6,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
		}
		return jobs;
	}
	
	//2.��ʼ����
	public static boolean jobStart(String scheduleType,String batchno,String jobtype ,String jobname,int jobStatus){
		try {
			Connection conn = ds.getConnection();
			conn.setAutoCommit(true);//jdbcĬ���Զ��ύ
			// System.out.println(conn);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sqlStr = "update dbo.jobWaitingPool set HEADJOBSTATUS = 0 where batchno = '"+batchno+"' and headJobname = '"+jobname+"'";
			WriteLog.writeFile("2.��ʼ����\n" + sqlStr);
			//System.out.println(batchno + "  " + jobname+" is ready: "+sqlStr);
			//stmt.executeUpdate(sqlStr);
			stmt.addBatch(sqlStr);
			sqlStr = "insert into dbo.jobWaitingPool(scheduleType,batchno,headJobname,headjobtype,head_on_fail_action,tailJobname,tailJobtype,tail_on_fail_action,headJobstatus) " +
					"select '"+scheduleType+"','"+batchno+"',a.headJobname,b.jobtype,b.on_fail_action,a.tailJobname,c.Jobtype,c.on_fail_action" +
							",case when headJobname = '"+jobname+"' then "+jobStatus+" else 200 end headJobstatus " +
					"from dbo.jobschedule a " +
					"inner join dbo.jobProperty b on a.scheduleType = b.scheduleType and a.headJobname = b.jobname " +
					"inner join dbo.jobProperty c on a.scheduleType = c.scheduleType and a.tailJobname = c.jobname " +
					"where tailJobname in(select tailJobname from dbo.jobschedule b where headJobname = '"+jobname+"'" + " and a.scheduleType = b.scheduleType and b.scheduleType = '"+scheduleType+"') " + 
					"and not exists(select 1 from dbo.jobWaitingPool b where b.batchno = '"+batchno+"' and a.tailJobname = b.tailJobname)" +  
					"and a.scheduleType = '"+scheduleType+"'";
			//System.out.println(batchno + "  " + jobname+" is ready: "+sqlStr);
			WriteLog.writeFile("2.��ʼ����\n" + sqlStr);
			stmt.addBatch(sqlStr);
			/*sqlStr = "insert into dbo.jobrunlog" +
					"(scheduleType,batchno,jobtype,jobname,starttime,jobstatus) values " +
					"('"+scheduleType+"','"+batchno+"','"+jobtype+"','"+jobname+"',current timestamp,0)";*/
			sqlStr = "update(select * from dbo.jobRunlog where scheduleType = '"+scheduleType+"' and batchno = '"+batchno+"' and jobname = '"+jobname+"' " +
					         "order by id desc fetch first 1 rows only " +
						    ")t set startTime = current timestamp,jobstatus = 0";
			WriteLog.writeFile("2.��ʼ����\n" + sqlStr);
			//System.out.println(batchno + "  " + jobname+" is ready: "+sqlStr);
			/*
			 update dbo.jobWaitingPool set HEADJOBSTATUS = 0 where batchno = batchno and headJobname = jobname;
			 insert into dbo.jobrunlog
			(batchno,jobtype,jobname,starttime,jobstatus,jobstatusStr)vlaues
			(batchno,jobtype,jobname,current timestamp,0,'start')
			*/
			stmt.addBatch(sqlStr);
			stmt.executeBatch();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//3.������ɣ�����job״̬�� д��״̬ 1-�ɹ� 2-���� 3-ʧ�ܣ�֪ͨ�¼�1-���
	public static boolean jobEnd(String scheduleType,String batchno,String jobtype ,String jobname,int jobStatus){
		try {
			Connection conn = ds.getConnection();
			conn.setAutoCommit(true);//jdbcĬ���Զ��ύ
			// System.out.println(conn);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//String sqlStr = "insert into dbo.jobrunlog (batchno,jobtype,jobname,endtime,jobstatus,jobstatusstr)values ('"+batchno+"','"+jobtype+"','"+jobname+"',current timestamp,"+jobStatus+",'end')";
			String sqlStr = "update dbo.jobrunlog set endtime = current timestamp,jobstatus = "+jobStatus+" where batchno = '"+batchno+"' and jobtype = '"+jobtype+"' and jobname = '"+jobname+"'";
			WriteLog.writeFile("������ɣ�\n" + sqlStr);
			/*
			update dbo.jobWaitingPool set headJobstatus = 1 
			where batchno = 'batchno' and headJobname = 'jobname'
			 */
			//System.out.println(jobname+" ��������״̬��"+sqlStr);
			//stmt.executeUpdate(sqlStr);
			stmt.addBatch(sqlStr);
			sqlStr = "update dbo.jobWaitingPool set headJobstatus = "+jobStatus+" " +"where batchno = '"+batchno+"' and headJobname = '"+jobname+"'";
			WriteLog.writeFile("������ɣ�\n" + sqlStr);
			//System.out.println(sqlStr);
			//stmt.executeUpdate(sqlStr);
			stmt.addBatch(sqlStr);
			stmt.executeBatch();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//4.��չ�¼ң����¼��ӽ��ȴ��أ�����������ȴ����ϼ�����
	public static boolean jobPyramid(String scheduleType,String batchno,String jobtype ,String jobname,int jobStatus){
		//�¼ҿ����ж�λ�ϼң���ֻ�ᱻ��1λ�ϼ������ȴ�����,��ʱ���¼�״̬Ϊ100-ready
		try {
			Connection conn = ds.getConnection();
			conn.setAutoCommit(true);//jdbcĬ���Զ��ύ
			// System.out.println(conn);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sqlStr = "insert into dbo.jobWaitingPool(scheduleType,batchno,headJobname,headjobtype,head_on_fail_action,tailJobname,tailJobtype,tail_on_fail_action,headJobstatus) " +
					"select '"+scheduleType+"','"+batchno+"',a.headJobname,b.jobtype,b.on_fail_action,a.tailJobname,c.Jobtype,c.on_fail_action" +
							",case when headJobname = '"+jobname+"' then "+jobStatus+" else 200 end headJobstatus " +
					"from dbo.jobschedule a " +
					"inner join dbo.jobProperty b on a.scheduleType = b.scheduleType and a.headJobname = b.jobname " +
					"inner join dbo.jobProperty c on a.scheduleType = c.scheduleType and a.headJobname = c.jobname " +
					"where tailJobname in(select tailJobname from dbo.jobschedule b where headJobname = '"+jobname+"'" + " and a.scheduleType = b.scheduleType and b.scheduleType = '"+scheduleType+"') " + 
					"and not exists(select 1 from dbo.jobWaitingPool b where b.batchno = '"+batchno+"' and a.tailJobname = b.tailJobname)" +  
					"and a.scheduleType = '"+scheduleType+"'";
			WriteLog.writeFile("4.��չ�¼ң����¼��ӽ��ȴ��أ�����������ȴ����ϼ�����\n" + sqlStr); 		
			/*--���¼��ӽ��ȴ��أ�����������ȴ����ϼ�����
					--��չ���¼���ȴ���
					insert into dbo.jobWaitingPool(scheduleType,batchno,headJobname,headjobtype,head_on_fail_action,tailJobname,tailJobtype,tail_on_fail_action,headJobstatus) 
					select 's1','20170805151952',a.headJobname,b.jobtype,b.on_fail_action,a.tailJobname,c.Jobtype,c.on_fail_action
							,case when headJobname = 'start' then 1 else 100 end headJobstatus 
					from dbo.jobschedule a 
					inner join dbo.jobProperty b on a.scheduleType = b.scheduleType and a.headJobname = b.jobname
					inner join dbo.jobProperty c on a.scheduleType = c.scheduleType and a.headJobname = c.jobname
					where tailJobname in(select tailJobname from dbo.jobschedule b where headJobname = 'start' and a.scheduleType = b.scheduleType and b.scheduleType = 's1')
					and not exists(select 1 from dbo.jobWaitingPool b where b.batchno = '20170805151952' and a.tailJobname = b.tailJobname)
					and a.scheduleType = 's1'
			  		*/
			//System.out.println(jobname+" ��չ���¼�:");//+sqlStr);
			stmt.executeUpdate(sqlStr);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//10.��ȡjob��ϸ�������(˫���鿴)
	public static DefaultJob getJobDetail(String jobname,String jobtype){
		DefaultJob jobDetail = new DefaultJob();
		try {
			Connection conn = ds.getConnection();
			conn.setAutoCommit(true);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = null;
			if (jobtype.equals("TAB")||jobtype.equals("FILE")){//dsjob����
																												
							 
				jobname = jobname.substring(jobname.lastIndexOf(".") + 1);
				sql = "select * from dbo.etl_jobino where inofile = '" + jobname + "'";
			}else {//��������Ǳ�
				sql = "select * from dbo.etl_jobino where jobname = '" + jobname + "'";
			}

			try {
				ResultSet rs = stmt.executeQuery(sql); 
					jobDetail.getDetailList().add("ID	STYPE	SEQJOBNAME	JOBNAME	JOBTYPE	INO	SND	SCHEMA	INOFILE	JOBLOCATE	SRSNAME	REMARK	ISVALID	INSERTDATE	UPDATEDATE	BO_REPORTFOLDER	RMS_REPORTCODE	RMS_REPORTNAME");
				if (jobtype.matches("TAB|FILE")) {
					while (rs.next()) {
						if (rs.getString("INO").equalsIgnoreCase("out"))
							jobDetail.getInputList().add(rs.getString("jobname"));//
																																								  
						else
							jobDetail.getOutputList().add(rs.getString("jobname"));
						jobDetail.setJoblocate("");
														
						String detailStr = "";
						for (int i = 1; i <= 18; i++) {
							detailStr += rs.getString(i) + "\t";
						}
						jobDetail.getDetailList().add(detailStr);
					}
				} else {
					while (rs.next()) {
						// System.out.println(rs.getString("JOBLOCATE"));
						if (rs.getString("INO").equalsIgnoreCase("in"))
							jobDetail.getInputList().add(rs.getString("snd") + "." + rs.getString("schema") + "." + rs.getString("inofile"));//
						else
							jobDetail.getOutputList().add(rs.getString("snd") + "." + rs.getString("schema") + "." + rs.getString("inofile"));
						// jobDetail.getDetailList().add(rs.getMetaData());
						jobDetail.setJoblocate(rs.getString("JOBLOCATE"));
						String detailStr = "";
						for (int i = 1; i <= 18; i++) {
							detailStr += rs.getString(i) + "\t";
						}
						jobDetail.getDetailList().add(detailStr);
					}
				}
			} catch (Exception e) {
				WriteLog.writeFile("ִ��SQL����getJobDetail:\n"+sql+"\n" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
			}
			conn.close();
		} catch (SQLException e) {
			WriteLog.writeFile("�������ݿ��쳣:\n"+ e.getMessage());
			e.printStackTrace();
		}
		return jobDetail;
	}
	// ��ȡscheduJoblocation
	public static ArrayList<Job> initScheduleJobLocationList(String scheduleType) {
		ArrayList<Job> jobs = new ArrayList<Job>();
		try {
			Connection conn = ds.getConnection();
			// stmt =
			// conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
			try {
				//ResultSet rs = stmt.executeQuery("call dbo.initSchedule()");
				String sql = "select * from dbo.jobProperty where scheduleType = '"+scheduleType+"'";
				logger.info(sql);
				ResultSet rs = stmt.executeQuery(sql);
				// ���������jobInfoList
				while (rs.next()) {
					//DefaultJob job = new DefaultJob();
					Job job = new Job(rs.getString("jobname"),rs.getInt("ISVALID"),rs.getString("JOBTYPE"));
					job.jobtype = rs.getString("JOBTYPE");
					job.jobname = rs.getString("jobname");
					job.x = rs.getDouble("X");
					job.y = rs.getDouble("Y");
					job.isValid = rs.getInt("ISVALID");
					job.isSchedule = rs.getInt("isSchedule");
					job.on_fail_action = rs.getString("ON_FAIL_ACTION");
					job.cost = rs.getString("COST");
					job.groupID = rs.getInt("groupID");
					job.groupName = rs.getString("groupName");
					job.memo = rs.getString("memo");
					job.params = rs.getString("params");
					job.jobstatus = 1000;//rs.getInt("jobstatus");��ʼ״̬Ϊ1000-waiting
					jobs.add(job);
				}
			} catch (Exception e) {
				WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("δ֪����7,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
		}
		return jobs;
	}
	
	//��ȡscheduleJobRelation
	public static List<JobEdge> initScheduleJobRelationList(String scheduleType) {
		List<JobEdge> jobEdges = new ArrayList<JobEdge>(); 
		try {
			Connection conn = ds.getConnection();
			//stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
		try{
			String sql = "select * from dbo.jobSchedule where scheduletype = '"+scheduleType+"'";
			logger.info(sql);
			ResultSet	rs = stmt.executeQuery(sql);
		// ���������jobInfoList
		while (rs.next()) {
			JobEdge jobEdge = new JobEdge();
			jobEdge.headJobName = rs.getString("headJobName");
			//jobEdge.headJobType = rs.getString("headJobType");
			//jobEdge.headJobstatus = rs.getInt("headJobstatus");
			//jobEdge.head_on_fail_action = rs.getString("head_on_fail_action");
			jobEdge.tailJobName = rs.getString("tailJobName");
			//jobEdge.tailJobType = rs.getString("tailJobType");
			//jobEdge.tail_on_fail_action = rs.getString("tail_on_fail_action");
			jobEdges.add(jobEdge);
		}
		}catch (Exception e){
			WriteLog.writeFile("��ȡ�����ļ�ʱ����:" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\n","δ֪����1,�������ݿ����� �� ��ϵ����Ա.",0);
		}
		conn.close();
	} catch (Exception e) {
		WriteLog.writeFile("δ֪����8,�������ݿ����� �� ��ϵ����Ա...\n"+e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage() + "\n","δ֪����2,�������ݿ����� �� ��ϵ����Ա.",0);
	}
	return jobEdges;
	}

	//ˢ�µ���״̬
	public static List<JobLocation> refreshScheduleJobLocationList(String batchno) {
		List<JobLocation> jobLocationList = new ArrayList<JobLocation>();
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
			try {
				/*String sql = "merge into dbo.job_location a using " +
							 "(select jobname,max(jobstatus)jobstatus from dbo.jobrunlog b where b.batchno = '"+batchno+"' group by jobname )b "+
							 "on a.jobname = b.jobname " +
							 "when matched then update set a.jobstatus = b.jobstatus " +
							 "else ignore";*/
				//��jobrunlog��ȡ���µ�job״̬
				String sql = "select jobname,jobstatus from (select jobname,jobstatus " +
							 			",row_number()over(partition by jobname order by insertTime desc)rn " + 
							 			"from dbo.jobrunlog "+
							 			"where batchno = '"+batchno+"')t where rn = 1 " +
							 	  "union all " +
							 "select tailJobname jobname ,100 jobstatus from dbo.jobWaitingPool a " +
                              "where not exists(select 1 from dbo.jobRunlog b " +  
                            		  "where a.scheduleType = b.scheduleType and a.batchno = b.batchno and b.batchno = '"+batchno+"') " +
                                      "and a.batchno = '"+batchno+"' " ;
				//WriteLog.writeFile("ˢ�µ���״̬\n" + sql); 	
				//System.out.println("refreshScheduleJob:"+sql);
				//stmt.executeUpdate(sql);
				ResultSet rs = stmt.executeQuery(sql);
				// ���������jobInfoList
				while (rs.next()) {
					JobLocation jobLocation = new JobLocation();
					jobLocation.jobname = rs.getString("jobname");
					//jobLocation.x = rs.getInt("X");
					//jobLocation.y = rs.getInt("Y");
					//jobLocation.isvalid = rs.getInt("ISVALID");
					//jobLocation.jobtype = rs.getString("JOBTYPE");
					jobLocation.status = rs.getInt("jobstatus");
					//jobLocation.cost = rs.getInt("COST");
					// jobLocation.rn = rs.getInt("RN");
					jobLocationList.add(jobLocation);
				}
			} catch (Exception e) {
				WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("δ֪����9,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
		}
		return jobLocationList;
	}
			
	// ������ҵ����������ϵ�����ݿ�
	public static boolean saveAllJob(String scheduleType, ArrayList<Job> jobs, ArrayList<JobEdge> jobEdges) {
		// 1.ɾ����Ҫ���������
		String sqlPre1 = "delete from dbo.jobProperty where scheduleType = '" + scheduleType + "'";
		String sqlPre2 = "delete from dbo.jobSchedule where scheduleType = '" + scheduleType + "'";
		// 2.����jobProperty��SQL���
		Job jb = jobs.get(0);
		String sql1 = "insert into dbo.jobProperty"
				+ "(scheduleType,jobtype,jobname,x,y,isvalid,isSchedule,on_fail_action,COST,groupID,groupName,memo,params)values";
		for (int i = 0; i < jobs.size(); i++) {
			jb = jobs.get(i);
			sql1 += ((i==0)?"":",") + "('" + scheduleType + "','" + jb.jobtype + "','" + jb.jobname + "'," + jb.x + "," + jb.y + "," + jb.isValid + "," + jb.isSchedule + ",'"
					+ jb.on_fail_action + "','" + jb.cost + "'," + jb.groupID + ",'" + jb.groupName + "','" + jb.memo + "','" + jb.params + "')\n";
		}
		// 3.����jobSchedule��SQL���
		JobEdge jobEdge = jobEdges.get(0);
		String sql2 = "insert into dbo.jobSchedule"
				+ "(scheduleType,headJobname,tailJobname)values ";
		for (int i = 0; i < jobEdges.size(); i++) {
			jobEdge = jobEdges.get(i);
			sql2 += ((i==0)?"":",") + "('" + scheduleType + "','" + jobEdge.headJobName + "','"+ jobEdge.tailJobName +"')\n";
		}
		WriteLog.writeFile("1.ɾ����Ҫ���������\n"+sqlPre1);
		WriteLog.writeFile("1.ɾ����Ҫ���������\n"+sqlPre2);
		WriteLog.writeFile("2.����jobProperty��SQL���\n"+sql1);
		WriteLog.writeFile("3.����jobSchedule��SQL���\n"+sql2);
		/*System.out.println(sqlPre1);
		System.out.println(sqlPre2);
		System.out.println(sql1);
		System.out.println(sql2);*/
		// 4.ִ��SQL���
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
			try {
				conn.setAutoCommit(false);
				stmt.executeUpdate(sqlPre1);
				stmt.executeUpdate(sqlPre2);
				stmt.executeUpdate(sql1);
				stmt.executeUpdate(sql2);
				conn.commit();
				/*stmt.addBatch(sqlPre1);
				stmt.addBatch(sqlPre2);
				stmt.addBatch(sql1);
				stmt.addBatch(sql2);
				stmt.executeBatch();*/
			} catch (Exception e) {
				WriteLog.writeFile("saveAllJob() ִ��SQL����:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����ʧ��", 0);
				return false;
			}
		} catch (Exception e) {
			WriteLog.writeFile("saveAllJob() δ֪����10,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "δ֪����", 0);
			return false;
		} finally{
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(null, "     ��ϲ\n","����ɹ�.",1);
		return true;
	}

	// sample for function
	@SuppressWarnings("unused")
	public static boolean sample(ArrayList<DefaultJob> jobs, ArrayList<JobEdge> edgs) {
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
			try {
				String sql = "s";
				ResultSet rs = stmt.executeQuery(sql);
				// ���������jobInfoList
				while (rs.next()) {
					JobLocation jobLocation = new JobLocation();
				}
			} catch (Exception e) {
				WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("δ֪����11,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
		}
		return true;
	}

	//��ȡ���������б�
	public static ArrayList<String> getScheduleType() {
		ArrayList<String> batchnos = new ArrayList<String>();
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
			try {
				String sql = "select distinct scheduleType from dbo.jobProperty";
				System.out.println(sql);
				ResultSet rs = stmt.executeQuery(sql);
				// ���������jobInfoList
				while (rs.next()) {
					batchnos.add(rs.getString("scheduleType"));
				}
			} catch (Exception e) {
				WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
			}
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("δ֪����12,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
		}
		return batchnos;
	}

	//��ȡ�����б�
	public static ArrayList<String> getBatchno(String scheduleType){
		ArrayList<String> batchnos = new ArrayList<String>();
		try {
		Connection conn = ds.getConnection();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
		try {
			//jobRunlog��Ҫ����scheduleType�ֶ�
			String sql = "select distinct batchno from dbo.jobRunLog where scheduleType = '"+scheduleType+"' order by batchno desc fetch first 16 rows only";// where scheduleType = '" + scheduleType +"'";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			// ���������jobInfoList
			while (rs.next()) {
				batchnos.add(rs.getString("batchno"));
			}
		} catch (Exception e) {
			WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
		}
		conn.close();
	} catch (Exception e) {
		WriteLog.writeFile("δ֪����13,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
	}
	return batchnos;
}
	
	// ��ȡjob_locations v3.0
	public static List<JobLocation> getJobLocationList3_0(User user,int jbtype_in, int sourceOrTarget, String jobname) {
		List<JobLocation> jobLocationList = new ArrayList<JobLocation>();
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
			try {
				//String sql = "CALL DBO.job_location_v2_1(" + jbtype_in + "," + sourceOrTarget + ",'" + jobname + "')";
				String sql = "CALL DBO.job_location_v3_1(" + jbtype_in + "," + sourceOrTarget + ",'" + jobname + "','"+user.getUserID()+"','"+sdf.format(user.getOptionTime())+"')";
				WriteLog.writeFile("excute:" + sql);
				stmt.executeUpdate(sql);
				sql = "select jobname,x,y,isvalid,jobtype,cost from dbo.job_location where userid = '"+user.getUserID()+"' and datime = '"+sdf.format(user.getOptionTime())+"' order by x,y";
				WriteLog.writeFile("excute:" + sql);
				ResultSet rs = stmt.executeQuery(sql);
				// ���������jobInfoList
				while (rs.next()) { // RID PNUMBER PREVIOUS BNUMBER BEHIND
					// PATHLENGTH RNUMBER2 X Y
					JobLocation jobLocation = new JobLocation();
					jobLocation.jobname = rs.getString("jobname");
					jobLocation.x = rs.getInt("X");
					jobLocation.y = rs.getInt("Y");
					jobLocation.isvalid = rs.getInt("ISVALID");
					jobLocation.jobtype = rs.getString("JOBTYPE");
					jobLocation.cost = rs.getInt("COST");
					// jobLocation.rn = rs.getInt("RN");
					jobLocationList.add(jobLocation);
				}
			} catch (Exception e) {
				WriteLog.writeFile("ִ��SQL����:" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "ִ��SQL����", 0);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			WriteLog.writeFile("δ֪����14,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nurl = " + "", "����", 0);
		}
		return jobLocationList;
	}

	//��ȡjob_relation
	public static List<JobRelation> getJobRelationList(User user) {
			List<JobRelation> jobRelationList = new ArrayList<JobRelation>();
			String userID = user.getUsername();
			Date optionTime = user.getOptionTime();
			try {
				Connection conn = ds.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // ���36001����
				try {
					String sql = "";
					sql = "select a.*,b.x,b.y,row_number()over(partition by BEHIND order by PATHLENGTH)rn " +
							"from dbo.job_relation a " +
							"inner join dbo.job_location b on a.BEHIND = b.JOBNAME and a.userid = b.userid and a.datime = b.datime " +
							"where a.userid = '"+userID+"' and a.datime = '"+sdf.format(optionTime)+
							"' order by RID,PREVIOUS,BNUMBER";
					WriteLog.writeFile("excute:"+sql);
					ResultSet rs = stmt.executeQuery(sql);
					// select * from dbo.job_relation
					// ���������jobInfoList
					while (rs.next()) { // RID PNUMBER PREVIOUS BNUMBER BEHIND
										// PATHLENGTH RNUMBER2 X Y
						JobRelation jobinfo = new JobRelation();
						jobinfo.rid = rs.getInt("RID");
						jobinfo.pnumber = rs.getInt("PNUMBER");
						jobinfo.previous = rs.getString("PREVIOUS");
						jobinfo.bnumber = rs.getInt("BNUMBER");
						jobinfo.behind = rs.getString("BEHIND");
						jobinfo.pathlength = rs.getInt("PATHLENGTH");
						jobinfo.rnumber2 = rs.getInt("RNUMBER2");
						jobinfo.x = rs.getInt("X");
						jobinfo.y = rs.getInt("Y");
						jobinfo.rn = rs.getInt("RN");
						jobRelationList.add(jobinfo);
					}
				} catch (Exception e) {
					WriteLog.writeFile("DBUnit : δ֪����1,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
					JOptionPane.showMessageDialog(null, e.getMessage() + "\n", "δ֪����1,�������ݿ����� �� ��ϵ����Ա.", 0);
				}
				stmt.close();
				conn.close();
			} catch (Exception e) {
				WriteLog.writeFile("DBUnit : δ֪����2,�������ݿ����� �� ��ϵ����Ա...\n" + e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage() + "\n", "δ֪����2,�������ݿ����� �� ��ϵ����Ա.", 0);
			}

			return jobRelationList;
		}

	//��¼
	public static int login(InitProperty ip) {
		int loginStatus = 0;
		String sql = "";
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
		} catch (Exception e) {
			//�޷��������ݿ�
			return -1;
		}
		try {
			sql = "select count(1) as LoginStatus from dbo.Jobio_User where UserName = '"
					+ip.getLoginUser()+"' and Password = '" 
					+ip.getLoginPassword()+"'";
			com.jupiter.WriteLog.writeFile("excute:"+sql);
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				loginStatus = rs.getInt("LOGINSTATUS");
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			//��¼�û��������
			e.printStackTrace();
		}
		return loginStatus;
	}
	
	public int getLoginStatus(String userName,String password) {

		return 0;
	}

	//��ȡ���°汾
	/*public static double getLastVersion(){
		double version = 0;
			Connection conn;
			try {
				conn = ds.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
				ResultSet rs = stmt.executeQuery("select max(version)version from dbo.Jobio_Version");
				if (rs.next())
					version = rs.getDouble("VERSION");
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return version;
	}*/
	
	public double getLastVersion(){
		double lastVersion = dbUnitMapper.getLastVersion();
		return lastVersion;
	}
	
	//��ǰ�汾�Ƿ����
	/*public static boolean isVersionUseable(double version){
		boolean useable = false;
		try {
			Connection conn = ds.getConnection();
			Statement stmt;
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery("select useable from dbo.Jobio_Version where version = "+version);
				if (rs.next())
					useable = rs.getString("useable").equalsIgnoreCase("Y") ? true : false;
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
		return useable;
	}*/
	public boolean isVersionUseable(double version){
		String useable = dbUnitMapper.isVersionUseable(version);
		if(useable == null)
			return false;
		return useable.equalsIgnoreCase("Y") ? true : false;
	}
	
	public static void main(String[] args) throws Exception {	
		//DBUnit t = new DBUnit();
		String xmlPath = "applicationContext.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
		//Login login = new Login();
		DBUnit dbUnit = (DBUnit) applicationContext.getBean("dbUnit");
//		System.out.println(DBUnit.getLastVersion());
		System.out.println(dbUnit.isVersionUseable(3.1));
	}
	
	//��ȡ����job�ĺ���
	public static ArrayList<Job> getChildren(Job job,DesignerGraph graph) {
		ArrayList<Job> children = new ArrayList<Job>();
		ArrayList<Job> jobsExists = graph.getAllJobs();
		double rootX = job.getX();
		double rootY = job.getY();
		String rootJobtype = job.getJobtype();
		String sql = "";
		if (rootJobtype.equals("TAB"))
			sql = "select distinct jobname,isvalid,jobtype from dbo.etl_jobino where inofile = '" 
					+ job.jobname + "' and ino = 'in'";
		else
			sql = "select distinct b.jobname,b.ISVALID,b.jobtype from dbo.etl_jobino a ,dbo.etl_jobino b where a.inofile = b.inofile and a.ino = 'out' and b.ino = 'in' and a.jobname = '" 
					+ job.jobname + "' and a.jobname <> b.jobname";
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
			WriteLog.writeFile("excute:"+sql);
			ResultSet rs = stmt.executeQuery(sql);
			double i = 0;
			int j = -1;
			int length = job.jobname.length()*7;
			int vx = 50 + length;
			int vy = 28;
			f : while(rs.next()){
				String jobname = rs.getString("JOBNAME");
				int isValid = rs.getInt("ISVALID");
				String jobtype = rs.getString("JOBTYPE");
				for(int e=0;e<jobsExists.size();e++){
					Job jobExists = jobsExists.get(e);
					if(jobname.equalsIgnoreCase(jobExists.getJobname())){
						//�����ͬ��Job�Ѿ����ڣ�����ѭ��
						children.add(jobExists);
						continue f;
					}
				}
				i = (i+0.5);
				j *= -1;
				int p = (int) (Math.floor(i)*j);
				int vx1 = vx*vx-(vy*p)*(vy*p);
				if(vx1 < 0) {
					i = 0.5;
					vx += 200;
					//vy += 6;
					rs.previous();
					continue;
				}
				double x = rootX+Math.sqrt(vx1);
				double y = rootY+vy*p;
				if(y<0){
					rs.previous();
					continue;
				}
				System.out.println("x:"+x +",y:"+y);
				Job child = new Job(x,y,jobname,isValid,jobtype);
				//System.out.println(child.printProperty());
				children.add(child);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			return children;
		}
		return children;
	}
	
	//��ȡ����job��roots
		public static ArrayList<Job> getRoots(Job job,DesignerGraph graph) {
			ArrayList<Job> roots = new ArrayList<Job>();
			ArrayList<Job> jobsExists = graph.getAllJobs();
			double rootX = job.getX();
			double rootY = job.getY();
			String rootJobtype = job.getJobtype();
			String sql = "";
			if (rootJobtype.equals("TAB"))
				sql = "select distinct jobname,isvalid,jobtype from dbo.etl_jobino where inofile = '" 
						+ job.jobname + "' and ino = 'out'";
			else
				sql = "select distinct b.jobname,b.ISVALID,b.jobtype from dbo.etl_jobino a ,dbo.etl_jobino b where a.inofile = b.inofile and a.ino = 'in' and b.ino = 'out' and a.jobname = '" 
						+ job.jobname + "' and a.jobname <> b.jobname";
			try {
				Connection conn = ds.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
				WriteLog.writeFile("excute:"+sql);
				ResultSet rs = stmt.executeQuery(sql);
				double i = 0;
				int j = -1;
				int length = job.jobname.length()*7;
				int vx = 50 + length;
				int vy = 28;
				f : while(rs.next()){
					String jobname = rs.getString("JOBNAME");
					int isValid = rs.getInt("ISVALID");
					String jobtype = rs.getString("JOBTYPE");
					for(int e=0;e<jobsExists.size();e++){
						Job jobExists = jobsExists.get(e);
						if(jobname.equalsIgnoreCase(jobExists.getJobname())){
							//�����ͬ��Job�Ѿ����ڣ�����ѭ��
							roots.add(jobExists);
							continue f;
						}
					}
					i = (i+0.5);
					j *= -1;
					int p = (int) (Math.floor(i)*j);
					int vx1 = vx*vx-(vy*p)*(vy*p);
					if(vx1 < 0) {
						i = 0.5;
						vx += 200;
						//vy += 6;
						rs.previous();
						continue;
					}
					double x = rootX+Math.sqrt(vx1);
					double y = rootY+vy*p;
					if(y<0){
						rs.previous();
						continue;
					}
					System.out.println("x:"+x +",y:"+y);
					Job child = new Job(x,y,jobname,isValid,jobtype);
					//System.out.println(child.printProperty());
					roots.add(child);
				}
				stmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				return roots;
			}
			return roots;
		}

}
