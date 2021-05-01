package com.jupiter.etl.schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

import com.jupiter.WriteLog;
import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.mybatis.dao.DBUnit;

public class Dsjob extends Job implements Runnable {

	private String dsjobServer;  //������
	private String user;         //�û�
	private String password;    //����
	private String dsjobProject; //����
	private String dsjobResetCommand;  //����
	private String dsjobRunCommand;
	private String dsjobGetLogCommand;
	private String dsjobStopCommand;

	public Dsjob(String scheduleType, String batchno,String dsjobServer, String user, String password, String dsjobProject, String dsjobName, String on_fail_action) {
		this.scheduleType = scheduleType;
		this.batchno = batchno;
		this.dsjobServer = dsjobServer;
		this.user = user;
		this.password = password;
		this.dsjobProject = dsjobProject;
		this.jobname = dsjobName;
		this.on_fail_action = on_fail_action;
		this.jobtype = "dsjob";
		this.dsjobRunCommand = "dsjob -server " + dsjobServer + " -user " + user + " -password " + password + " -run -wait -jobstatus " + dsjobProject + " "
				+ dsjobName;
	}

	@Override
	public void run() {
		runDsjob();
	}

	public void runDsjob() {
		// 1.֪ͨ�¼����Ѿ���ʼ.�����Լ����¼������ȴ��أ�����������ȴ�����������. ��־��¼ 0-start
		this.jobstatus = 0;
		DBUnit.jobStart(this.scheduleType,batchno, jobtype, jobname, this.jobstatus);
		// 2.����
		int jobstatus = (new Random()).nextInt(4) + 1;
		WriteLog.writeFile(dsjobRunCommand);
		try {
			Process p = Runtime.getRuntime().exec("id");
			BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = bf.readLine();
			String tempAll = "";
			while (str != null) {
				tempAll += str + "\r\n";
				WriteLog.writeFile(str);
				str = bf.readLine();
			}
			WriteLog.writeFile(str);
			bf = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			str = bf.readLine();
			while (str != null) {
				tempAll += str + "\r\n";
				WriteLog.writeFile(str);
				str = bf.readLine();
			}
			WriteLog.writeFile(str);
		} catch (Exception e) {
		}
		this.jobstatus = jobstatus;
		// 3.������ɣ�����job״̬��1000-unready 100-ready(waiting) 1-�ɹ� 2-���� 3-ʧ�� 4-ʧ�ܵ��ɺ��ԣ�֪ͨ�¼��Լ���״̬
				DBUnit.jobEnd(scheduleType,batchno, jobtype, jobname, this.jobstatus);
				System.out.println(Monitor.batchno + "  " + this.jobname + " run finished! jobstatus:" + jobstatus);
	}

	public void getDsjobStatus() {

	}

	public void resetDsjob() {

	}
	
	public static void main(String[] args) {
		Dsjob dsjob = new Dsjob("testDsjob","testbatchno","195","dsadm","password","dev","testJobname","stop");
		dsjob.runDsjob();
		
	}

}
