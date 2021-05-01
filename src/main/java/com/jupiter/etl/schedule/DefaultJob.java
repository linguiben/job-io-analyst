package com.jupiter.etl.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DefaultJob {
	public String batchno;   //调度批次号
	public String joblocate;   //程序所在目录
	public String jobname;   //名称
	public String jobtype;   //类型
	public double x = 0;   //x坐标
	public double y = 0;   //y坐标
	public int isValid = 0;   //是否有效
	public int isSchedule = 0;   //本次是否调度
	public String jobCommand;   //运行作业的命令
	public String startTimie;   //调度开始时间
	public String endTime;   //调度结束时间
	public String datetime;   //耗时
	public int headJobAmount;  //父节点个数
	public int headJobstatus;  //父节点的状态
	public int jobstatus;  //大于1000-unready,100-ready,0-start,1-finished success,2-warning,3-faile,4-faild&skip
	public String head_on_fail_action;  //前面的作业是否skip
	public String on_fail_action;       //this是否skip
	public String params ;
	public String cost;                //平均消耗时长
	public int groupID;                 //所属组ID
	public String groupName;                //组名称
	public String memo;                //备注
	public ArrayList<String> inputList = new ArrayList<String>();  //输入表列表
	public ArrayList<String> outputList = new ArrayList<String>(); //输出表列表
	public ArrayList<String> detailList = new ArrayList<String>(); //备用
		
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
