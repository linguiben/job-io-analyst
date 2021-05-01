package com.jupiter.etl.schedule;

import java.util.Random;

import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.mybatis.dao.DBUnit;

//����ҵ,������ʾ���ȹ���
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

	// job���й���
	public void runJob() {
		// 1.֪ͨ�¼����Ѿ���ʼ.�����Լ����¼������ȴ��أ�����������ȴ�����������. ��־��¼ 0-start
		this.jobstatus = 0;
		DBUnit.jobStart(this.scheduleType,batchno, jobtype, jobname, this.jobstatus);
		System.out.println(Monitor.batchno + "  " +this.jobname + " runStart !");
		// 2.����
		int jobstatus = (new Random()).nextInt(4) + 1;
		try {
			Thread.sleep(((new Random()).nextInt(3)+1)*5000);  //5~15�����
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.jobstatus = jobstatus;
		// 3.������ɣ�����job״̬��1000-unready 100-ready(waiting) 1-�ɹ� 2-���� 3-ʧ�� 4-ʧ�ܵ��ɺ��ԣ�֪ͨ�¼��Լ���״̬
		DBUnit.jobEnd(scheduleType,batchno, jobtype, jobname, this.jobstatus);
		System.out.println(Monitor.batchno + "  " + this.jobname + " run finished! jobstatus:" + jobstatus);
		/**����
		4.��չ���¼ҽ��ȴ��أ�����������ȴ�������������֪ͨ�¼��Լ���״̬
		DBUnit.jobPyramid(this.scheduleType,batchno, jobtype, jobname, this.jobstatus);
		*/
	}
	
}
