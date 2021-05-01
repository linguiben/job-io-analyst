package com.jupiter.etl.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DefaultJob {
	public String batchno;   //�������κ�
	public String joblocate;   //��������Ŀ¼
	public String jobname;   //����
	public String jobtype;   //����
	public double x = 0;   //x����
	public double y = 0;   //y����
	public int isValid = 0;   //�Ƿ���Ч
	public int isSchedule = 0;   //�����Ƿ����
	public String jobCommand;   //������ҵ������
	public String startTimie;   //���ȿ�ʼʱ��
	public String endTime;   //���Ƚ���ʱ��
	public String datetime;   //��ʱ
	public int headJobAmount;  //���ڵ����
	public int headJobstatus;  //���ڵ��״̬
	public int jobstatus;  //����1000-unready,100-ready,0-start,1-finished success,2-warning,3-faile,4-faild&skip
	public String head_on_fail_action;  //ǰ�����ҵ�Ƿ�skip
	public String on_fail_action;       //this�Ƿ�skip
	public String params ;
	public String cost;                //ƽ������ʱ��
	public int groupID;                 //������ID
	public String groupName;                //������
	public String memo;                //��ע
	public ArrayList<String> inputList = new ArrayList<String>();  //������б�
	public ArrayList<String> outputList = new ArrayList<String>(); //������б�
	public ArrayList<String> detailList = new ArrayList<String>(); //����
		
	public String getJoblocate() {
		return joblocate;
	}
	public void setJoblocate(String joblocate) {
		this.joblocate = joblocate;
	}
	public ArrayList<String> getInputList() {
		return inputList;
	}
	public void setInputList(ArrayList<String> inputList) {
		this.inputList = inputList;
	}
	public ArrayList<String> getOutputList() {
		return outputList;
	}
	public void setOutputList(ArrayList<String> outputList) {
		this.outputList = outputList;
	}
	public String getBatchno() {
		return batchno;
	}
	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public String getJobtype() {
		return jobtype;
	}
	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}
	public int getJobstatus() {
		return jobstatus;
	}
	public void setJobstatus(int jobstatus) {
		this.jobstatus = jobstatus;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String dATETIME) {
		datetime = dATETIME;
	}
	public String getOn_fail_action() {
		return on_fail_action;
	}
	public void setOn_fail_action(String on_fail_action) {
		this.on_fail_action = on_fail_action;
	}
	public String getHead_on_fail_action() {
		return head_on_fail_action;
	}
	public void setHead_on_fail_action(String head_on_fail_action) {
		this.head_on_fail_action = head_on_fail_action;
	}
	public ArrayList<String> getDetailList() {
		return detailList;
	}
	public void setDetailList(ArrayList<String> detailList) {
		this.detailList = detailList;
	}
	public String getStartTimie() {
		return startTimie;
	}
	public void setStartTimie(String startTimie) {
		this.startTimie = startTimie;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getJobCommand() {
		return jobCommand;
	}
	public void setJobCommand(String jobCommand) {
		this.jobCommand = jobCommand;
	}

}
