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
	public String scheduleType; //调度类型,用于调度线程里区别不同类型有相同的作业
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
	public String datetime = "00:00:00";   //耗时
	public int headJobAmount;  //父节点个数
	public int headJobstatus;  //父节点的状态
	public int jobstatus;  //大于100-waiting,100-ready,0-start,1-finished success,2-warning,3-faile,4-faild&skip
	public String head_on_fail_action;  //前面的作业是否skip
	public String on_fail_action;       //this是否skip
	public String params ;
	public String cost = "00:00:00";                //平均消耗时长
	public int groupID;                 //所属组ID
	public String groupName;                //组名称
	public String memo;                //备注
	public ArrayList<String> inputList = new ArrayList<String>();  //输入表列表
	public ArrayList<String> outputList = new ArrayList<String>(); //输出表列表
	public ArrayList<String> detailList = new ArrayList<String>(); //备用
	Logger logger = Logger.getLogger(Job.class);
	
	//带位置创建
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
		this.add(new DefaultPort());  //可连接
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
		this.add(new DefaultPort());  //可连接
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

	//根据Job类型设置颜色
	public void setColor(){
		logger.debug(this.jobname+".isValid=" +this.isValid );
		if(this.isValid == 0){
			//过时的作业显示为浅灰
			GraphConstants.setForeground(this.getAttributes(),Color.lightGray);
			//GraphConstants.setOpaque(jCell.getAttributes(), true);
		}else if(this.jobtype.equals("TAB")){//fn.contains(jb.jobname)
			//表显示为橙色
				//jb.jobname.equalsIgnoreCase(filename)
			GraphConstants.setForeground(this.getAttributes(),Color.PINK);
			//GraphConstants.setOpaque(jCell.getAttributes(), true);
		}else if(this.jobtype.equals("bojob")){
			//boruntime显示为蓝色
			GraphConstants.setForeground(this.getAttributes(),Color.BLUE);
			//GraphConstants.setOpaque(jCell.getAttributes(), true);
		}else if(this.jobtype.equals("shell")){
			//boruntime显示为粉色
			GraphConstants.setForeground(this.getAttributes(),Color.RED);
			//GraphConstants.setOpaque(jCell.getAttributes(), true);
		}else if(this.jobtype.equals("BlankJob")) {
			//空作业(影响分析时,输入的表,为了与TAB类型区分,记为BlankJob)显示为浅灰
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
