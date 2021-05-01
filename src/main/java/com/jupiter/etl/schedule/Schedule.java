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
	private int[] scheduleStatus = new int[5];  //���������ɹ�����ʧ������δ�����
	
	public boolean isSTOP() {
		return isInterrupt;
	}

	public void setSTOP(boolean sTOP) {
		isInterrupt = sTOP;
	}

	//�����޲Ҳ������췽����,��������batchno
	public Schedule(){
		try {
			InputStream inputStream = this.getClass().getClassLoader()
					.getResourceAsStream("db.properties");
			Properties p = new Properties();
			try {
				p.load(inputStream);
			} catch (IOException e1) {
				WriteLog.writeFile("1.��ȡ�����ļ�����,�˳�����");
				JOptionPane.showMessageDialog(null, e1.getMessage() + "���������ļ�\n", "1.�����ļ�����", 0);
			}
			dsServer = p.getProperty("DSServer");
			dsUser = p.getProperty("DSUser");
			dsPassword=p.getProperty("DSPassword");
			dsProject = p.getProperty("DSProject");
			this.dsjobCommand = "dsjob -server "+dsServer+" -user "+dsUser+" -password "+dsPassword+" -run -wait -jobstatus ";
		} catch (Exception e1) {
			WriteLog.writeFile("2.��ȡ�����ļ�����,�˳�����");
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, e1.getMessage() + "���������ļ�\n", "2.�����ļ�����", 0);
		}
	}

	//��ʼ�µĵ���
	public void startSchedule(String scheduleType,String batchno) {
		
		if(batchno == null || batchno.equals("")){
			SimpleDateFormat dfstr = new SimpleDateFormat("yyyyMMddHHmmss");// �������ڸ�ʽ
			batchno = dfstr.format(new Date());
		}
		//Monitor.batchno = batchno;  //����Monitor�����κ�
		System.out.println("��������:" + scheduleType);// new Date()Ϊ��ȡ��ǰϵͳʱ��
		System.out.println("�������κţ�" + batchno);// new Date()Ϊ��ȡ��ǰϵͳʱ��

		// ��ʼ�����ȣ���start����ȴ���
		DBUnit.initSchedule(scheduleType,batchno, "start");

		boolean isEnd = false;
		boolean isAbort = false;
		ExecutorService threadPool = Executors.newFixedThreadPool(Monitor.parallel); // �����̶��������̳߳�
		
		//��������=�ɹ���+ʧ����,��isEnd=true;
		while (!this.isInterrupt && !isEnd && !isAbort) { //&& flag < 10
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.scheduleStatus = DBUnit.getScheduleStatus(this.scheduleType, this.batchno);
			//��δ�����=0 (������-�ɹ���+ʧ����),��isEnd=true;
					System.out.println("isEnd = " + isEnd);
				if(scheduleStatus[3]==0){
					isEnd = true;
					break;
				}
			// �ӵȴ��ػ�ȡ�¼�,���ϼ�in(1,2,4)
			ArrayList<DefaultJob> jobs = DBUnit.getNextJobs(batchno);
			// System.out.println("���صĵ�һ��job:"+jobs.get(0).getJobname());
			//int s = jobs.size();
			for (int i = 0,s = jobs.size(); i < s; i++) {
				DefaultJob job = jobs.get(0);
				// BlankJob�߳�
				if (job.getJobtype().equals("blankjob")) {
					WriteLog.writeFile(Monitor.batchno + "  " + job.getJobname() + "����ִ�ж��У�");
					threadPool.execute(new BlankJob(scheduleType, job.getBatchno(), job.getJobname(), job.getOn_fail_action()));
					// Thread runBlankJob = new Thread(new
					// BlankJob(scheduleType,job.getBatchno(), job.getJobname(),
					// job.getOn_fail_action()));
					// runBlankJob.start();
				}// dsjob�߳�
				else if (job.getJobtype().equals("dsjob")) {
					Thread jobDsjob = new Thread(new Dsjob(scheduleType,job.batchno,dsServer, dsUser, dsPassword, dsProject,job.jobname, job.on_fail_action));
					jobDsjob.start();
				} else {
					WriteLog.writeFile("δ֪jobtype: " +job.getJobname() + " " + job.getJobtype());
					//this.STOP = true;
				}
				// Bojob�߳�
				// ShellJob�߳�
				jobs.remove(0);
			}
		}
		
		if (isEnd){//��������
			WriteLog.writeFile("�����Ѿ����,�������ͣ�"+scheduleType+" ���κ�:"+batchno);
			JOptionPane.showMessageDialog(null, "���κ�:" + batchno, "��ɣ�", 0);
			threadPool.shutdown();
		}else if(isInterrupt) { //�ֹ���ֹ
			WriteLog.writeFile("�ֶ��ж�,�������ͣ�"+scheduleType+" ���κ�:"+batchno);
			JOptionPane.showMessageDialog(null, "������ֹͣ,������ִ�е���ҵ���ܲ�����ֹ,�����д���\n"+"���κ�:" + batchno, "���ֶ�ֹͣ", 0);
			threadPool.shutdownNow();
		}else { //�쳣����
			//1)û���������е���ҵ(����) ��2)��ȡ�����¼� ��3)����δ���
		}
		
        while(!threadPool.isTerminated()){
        }
        	WriteLog.writeFile("Thread Pool is over");
	}
	
	//����
	public void continueSchedule(String babchno){
		
	}
	public static void main(String[] args) {
		/*Thread dsjob1 = new Dsjob("GZDBTSVR5","dsadm","dsadm","dev","jb_gx_etl_agencyinfo_dimn_d_all");
		Thread dsjob2 = new Dsjob("GZDBTSVR5","dsadm","dsadm","dev","jb_gd_etl_agencyinfo_dimn_d_all");
		dsjob1.start();
		dsjob2.start();*/
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
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
