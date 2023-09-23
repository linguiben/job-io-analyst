package com.jupiter.etl.schedule;

import java.util.Random;

import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.mybatis.dao.DBUnit;

//空作业,用于演示调度过程
public class BlankJob extends Job implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7119647224993686398L;

	public BlankJob(String scheduleType,String batchno, String jobname, String on_fail_action) {
		this.scheduleType = scheduleType;
		this.batchno = batchno;
		this.jobname = jobname;
		this.jobtype = "blankjob";
		this.jobstatus = 100;
		this.on_fail_action = on_fail_action;
	}

	public void run() {
		runJob();
	}

	// job运行过程
	public void runJob() {
		// 1.通知下家我已经开始.并将自己的下家拉进等待池，并告诉它需等待的上线名单. 日志记录 0-start
		this.jobstatus = 0;
		DBUnit.jobStart(this.scheduleType,batchno, jobtype, jobname, this.jobstatus);
		System.out.println(Monitor.batchno + "  " +this.jobname + " runStart !");
		// 2.运行
		int jobstatus = (new Random()).nextInt(4) + 1;
		try {
			Thread.sleep(((new Random()).nextInt(3)+1)*5000);  //5~15秒结束
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.jobstatus = jobstatus;
		// 3.运行完成，更新job状态，1000-unready 100-ready(waiting) 1-成功 2-警告 3-失败 4-失败但可忽略，通知下家自己的状态
		DBUnit.jobEnd(scheduleType,batchno, jobtype, jobname, this.jobstatus);
		System.out.println(Monitor.batchno + "  " + this.jobname + " run finished! jobstatus:" + jobstatus);
		/**作废
		4.发展新下家进等待池，并告诉它需等待的上线名单，通知下家自己的状态
		DBUnit.jobPyramid(this.scheduleType,batchno, jobtype, jobname, this.jobstatus);
		*/
	}
	
}
