package com.jupiter.etl.jobinfo.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

public class Job extends DefaultGraphCell{

	private static final long serialVersionUID = -5759372474279563972L;
	public String scheduleType; //��������,���ڵ����߳�������ͬ��������ͬ����ҵ
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
	public String datetime = "00:00:00";   //��ʱ
	public int headJobAmount;  //���ڵ����
	public int headJobstatus;  //���ڵ��״̬
	public int jobstatus;  //����100-waiting,100-ready,0-start,1-finished success,2-warning,3-faile,4-faild&skip
	public String head_on_fail_action;  //ǰ�����ҵ�Ƿ�skip
	public String on_fail_action;       //this�Ƿ�skip
	public String params ;
	public String cost = "00:00:00";                //ƽ������ʱ��
	public int groupID;                 //������ID
	public String groupName;                //������
	public String memo;                //��ע
	public ArrayList<String> inputList = new ArrayList<String>();  //������б�
	public ArrayList<String> outputList = new ArrayList<String>(); //������б�
	public ArrayList<String> detailList = new ArrayList<String>(); //����
	Logger logger = Logger.getLogger(Job.class);
	
	//��λ�ô���
	public Job(double x,double y,String jobname, int isvalid, String jobtype) {
		super(jobname);
		this.jobname = jobname;
		this.isValid = isvalid;
		this.setJobtype(jobtype);
		this.x = x;
		this.y = y;
		GraphConstants.setBounds(this.getAttributes(), new Rectangle2D.Double(x, y, this.jobname.length() * 7, 16));
		GraphConstants.setAutoSize(this.getAttributes(), true);
		GraphConstants.setFont(this.getAttributes(), new Font("Default Sans Serif", 0, 10));
		this.add(new DefaultPort());  //������
		this.setColor();
	}
	
	public Job(String jobname, int isvalid, String jobtype) {//,double x,double y
		super(jobname);
		this.jobname = jobname;
		this.isValid = isvalid;
		this.setJobtype(jobtype);
		GraphConstants.setBounds(this.getAttributes(), new Rectangle2D.Double(0, 0, this.jobname.length() * 7, 16));
		GraphConstants.setAutoSize(this.getAttributes(), true);
		GraphConstants.setFont(this.getAttributes(), new Font("Default Sans Serif", 0, 10));
		this.setColor();
		this.add(new DefaultPort());  //������
	}

	public Job(String s1) {
		super(s1);
	}
	public Job(){
		//super();
	}
	
	public String printProperty(){
		return userObject.toString() + ", x:" + x + ", y:" + y;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getOn_fail_action() {
		return on_fail_action;
	}
	public void setOn_fail_action(String on_fail_action) {
		this.on_fail_action = on_fail_action;
	}

	//����Job����������ɫ
	public void setColor(){
		logger.debug(this.jobname+".isValid=" +this.isValid );
		if(this.isValid == 0){
			//��ʱ����ҵ��ʾΪǳ��
			GraphConstants.setForeground(this.getAttributes(),Color.lightGray);
			//GraphConstants.setOpaque(jCell.getAttributes(), true);
		}else if(this.jobtype.equals("TAB")){//fn.contains(jb.jobname)
			//����ʾΪ��ɫ
				//jb.jobname.equalsIgnoreCase(filename)
			GraphConstants.setForeground(this.getAttributes(),Color.PINK);
			//GraphConstants.setOpaque(jCell.getAttributes(), true);
		}else if(this.jobtype.equals("bojob")){
			//boruntime��ʾΪ��ɫ
			GraphConstants.setForeground(this.getAttributes(),Color.BLUE);
			//GraphConstants.setOpaque(jCell.getAttributes(), true);
		}else if(this.jobtype.equals("shell")){
			//boruntime��ʾΪ��ɫ
			GraphConstants.setForeground(this.getAttributes(),Color.RED);
			//GraphConstants.setOpaque(jCell.getAttributes(), true);
		}else if(this.jobtype.equals("BlankJob")) {
			//����ҵ(Ӱ�����ʱ,����ı�,Ϊ����TAB��������,��ΪBlankJob)��ʾΪǳ��
			//GraphConstants.setBackground(jCell.getAttributes(), Color.yellow);
			//GraphConstants.setGradientColor(arg0, arg1)
			//GraphConstants.setForeground(arg0, arg1)
			GraphConstants.setForeground(this.getAttributes(), Color.DARK_GRAY);
			//GraphConstants.setOpaque(jCell.getAttributes(), true);
		}else{
			GraphConstants.setForeground(this.getAttributes(), Color.BLACK);
		}
	}

	public String getJobtype() {
		return jobtype;
	}

	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}

	public String getBatchno() {
		return batchno;
	}
	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}
	public String getJoblocate() {
		return joblocate;
	}
	public void setJoblocate(String joblocate) {
		this.joblocate = joblocate;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	public int getIsSchedule() {
		return isSchedule;
	}
	public void setIsSchedule(int isSchedule) {
		this.isSchedule = isSchedule;
	}
	public String getJobCommand() {
		return jobCommand;
	}
	public void setJobCommand(String jobCommand) {
		this.jobCommand = jobCommand;
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
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public int getHeadJobAmount() {
		return headJobAmount;
	}
	public void setHeadJobAmount(int headJobAmount) {
		this.headJobAmount = headJobAmount;
	}
	public int getHeadJobstatus() {
		return headJobstatus;
	}
	public void setHeadJobstatus(int headJobstatus) {
		this.headJobstatus = headJobstatus;
	}
	public int getJobstatus() {
		return jobstatus;
	}
	public void setJobstatus(int jobstatus) {
		this.jobstatus = jobstatus;
	}
	public String getHead_on_fail_action() {
		return head_on_fail_action;
	}
	public void setHead_on_fail_action(String head_on_fail_action) {
		this.head_on_fail_action = head_on_fail_action;
	}
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getMeno() {
		return memo;
	}
	public void setMeno(String meno) {
		memo = meno;
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
	public ArrayList<String> getDetailList() {
		return detailList;
	}
	public void setDetailList(ArrayList<String> detailList) {
		this.detailList = detailList;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

}
