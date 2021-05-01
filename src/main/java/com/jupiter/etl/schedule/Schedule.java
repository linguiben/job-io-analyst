package com.jupiter.etl.schedule;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import com.jupiter.WriteLog;
import com.jupiter.mybatis.dao.DBUnit;

public class Schedule implements Runnable{
	
	private String scheduleType;
	private String batchno;
	private String dsServer;
	private String dsUser;
	private String dsPassword;
	private String dsProject;
	private String dsjobCommand;
	//private String scheduleType;
	//private String batchno;
	private boolean isInterrupt = false;
	private int[] scheduleStatus = new int[5];  //任务数、成功数、失败数、未完成数
	
	public boolean isSTOP() {
		return isInterrupt;
	}

	public void setSTOP(boolean sTOP) {
		isInterrupt = sTOP;
	}

	//调用无惨参数构造方法后,需设置其batchno
	public Schedule(){
		try {
			InputStream inputStream = this.getClass().getClassLoader()
					.getResourceAsStream("db.properties");
			Properties p = new Properties();
			try {
				p.load(inputStream);
			} catch (IOException e1) {
				WriteLog.writeFile("1.读取配置文件出错,退出程序");
				JOptionPane.showMessageDialog(null, e1.getMessage() + "请检查配置文件\n", "1.配置文件错误", 0);
			}
			dsServer = p.getProperty("DSServer");
			dsUser = p.getProperty("DSUser");
			dsPassword=p.getProperty("DSPassword");
			dsProject = p.getProperty("DSProject");
			this.dsjobCommand = "dsjob -server "+dsServer+" -user "+dsUser+" -password "+dsPassword+" -run -wait -jobstatus ";
		} catch (Exception e1) {
			WriteLog.writeFile("2.读取配置文件出错,退出程序");
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, e1.getMessage() + "请检查配置文件\n", "2.配置文件错误", 0);
		}
	}

	//开始新的调度
	public void startSchedule(String scheduleType,String batchno) {
		
		if(batchno == null || batchno.equals("")){
			SimpleDateFormat dfstr = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
			batchno = dfstr.format(new Date());
		}
		//Monitor.batchno = batchno;  //更新Monitor的批次号
		System.out.println("调度配型:" + scheduleType);// new Date()为获取当前系统时间
		System.out.println("调度批次号：" + batchno);// new Date()为获取当前系统时间

		// 初始化调度，将start丢入等待池
		DBUnit.initSchedule(scheduleType,batchno, "start");

		boolean isEnd = false;
		boolean isAbort = false;
		ExecutorService threadPool = Executors.newFixedThreadPool(Monitor.parallel); // 创建固定容量的线程池
		
		//当任务数=成功数+失败数,则isEnd=true;
		while (!this.isInterrupt && !isEnd && !isAbort) { //&& flag < 10
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.scheduleStatus = DBUnit.getScheduleStatus(this.scheduleType, this.batchno);
			//当未完成数=0 (任务数-成功数+失败数),则isEnd=true;
					System.out.println("isEnd = " + isEnd);
				if(scheduleStatus[3]==0){
					isEnd = true;
					break;
				}
			// 从等待池获取下家,其上家in(1,2,4)
			ArrayList<DefaultJob> jobs = DBUnit.getNextJobs(batchno);
			// System.out.println("返回的第一个job:"+jobs.get(0).getJobname());
			//int s = jobs.size();
			for (int i = 0,s = jobs.size(); i < s; i++) {
				DefaultJob job = jobs.get(0);
				// BlankJob线程
				if (job.getJobtype().equals("blankjob")) {
					WriteLog.writeFile(Monitor.batchno + "  " + job.getJobname() + "进入执行队列：");
					threadPool.execute(new BlankJob(scheduleType, job.getBatchno(), job.getJobname(), job.getOn_fail_action()));
					// Thread runBlankJob = new Thread(new
					// BlankJob(scheduleType,job.getBatchno(), job.getJobname(),
					// job.getOn_fail_action()));
					// runBlankJob.start();
				}// dsjob线程
				else if (job.getJobtype().equals("dsjob")) {
					Thread jobDsjob = new Thread(new Dsjob(scheduleType,job.batchno,dsServer, dsUser, dsPassword, dsProject,job.jobname, job.on_fail_action));
					jobDsjob.start();
				} else {
					WriteLog.writeFile("未知jobtype: " +job.getJobname() + " " + job.getJobtype());
					//this.STOP = true;
				}
				// Bojob线程
				// ShellJob线程
				jobs.remove(0);
			}
		}
		
		if (isEnd){//正常结束
			WriteLog.writeFile("调度已经完成,调度类型："+scheduleType+" 批次号:"+batchno);
			JOptionPane.showMessageDialog(null, "批次号:" + batchno, "完成！", 0);
			threadPool.shutdown();
		}else if(isInterrupt) { //手工终止
			WriteLog.writeFile("手动中断,调度类型："+scheduleType+" 批次号:"+batchno);
			JOptionPane.showMessageDialog(null, "调度已停止,但正在执行的作业可能不会终止,请自行处理。\n"+"批次号:" + batchno, "已手动停止", 0);
			threadPool.shutdownNow();
		}else { //异常结束
			//1)没有正在运行的作业(进程) 且2)获取不到下家 且3)调度未完成
		}
		
        while(!threadPool.isTerminated()){
        }
        	WriteLog.writeFile("Thread Pool is over");
	}
	
	//续跑
	public void continueSchedule(String babchno){
		
	}
	public static void main(String[] args) {
		/*Thread dsjob1 = new Dsjob("GZDBTSVR5","dsadm","dsadm","dev","jb_gx_etl_agencyinfo_dimn_d_all");
		Thread dsjob2 = new Dsjob("GZDBTSVR5","dsadm","dsadm","dev","jb_gd_etl_agencyinfo_dimn_d_all");
		dsjob1.start();
		dsjob2.start();*/
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Schedule sch = new Schedule();
		sch.startSchedule("s1","");
	}

	@Override
	public void run() {
		startSchedule(this.scheduleType,this.batchno);
		
	}
	
	public void stop(){
		this.isInterrupt = true;
	}

	public String getDsServer() {
		return dsServer;
	}

	public void setDsServer(String dsServer) {
		this.dsServer = dsServer;
	}

	public String getDsUser() {
		return dsUser;
	}

	public void setDsUser(String dsUser) {
		this.dsUser = dsUser;
	}

	public String getDsPassword() {
		return dsPassword;
	}

	public void setDsPassword(String dsPassword) {
		this.dsPassword = dsPassword;
	}

	public String getDsProject() {
		return dsProject;
	}

	public void setDsProject(String dsProject) {
		this.dsProject = dsProject;
	}

	public String getDsjobCommand() {
		return dsjobCommand;
	}

	public void setDsjobCommand(String dsjobCommand) {
		this.dsjobCommand = dsjobCommand;
	}

	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}
	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public int[] getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(int[] scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

}
