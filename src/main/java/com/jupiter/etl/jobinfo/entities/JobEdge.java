
package com.jupiter.etl.jobinfo.entities;


import java.awt.Color;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;


public class JobEdge extends DefaultEdge{
	
	public String headJobName;
	public String headJobType;
	public int headJobstatus;
	public String head_on_fail_action;
	public String tailJobName;
	public String tailJobType;
	public int tailJobstatus;
	public String tail_on_fail_action;
	public int dependence;
	
public JobEdge(String p,String b) {
	//super(s); //调用父类的构造方法
	headJobName = p;
	tailJobName = b;
	}

	public JobEdge() {
	}

	public JobEdge(Job job1, Job job2) {
		headJobName = job1.getJobname();
		this.headJobType = job1.jobtype;
		this.headJobstatus = job1.isValid;
		tailJobName = job2.getJobname();
		this.tailJobType = job2.jobtype;
		this.tailJobstatus = job2.isValid;
		this.setSource(job1.getChildAt(0));
		this.setTarget(job2.getChildAt(0));
		if (job1.isValid*job2.isValid == 0)
		GraphConstants.setLineColor(this.getAttributes(), Color.lightGray);
		GraphConstants.setLineEnd(this.getAttributes(), GraphConstants.ARROW_CLASSIC); //设置箭头
	}

public String getPrevious() {
	return headJobName;
}

public void setPrevious(String previous) {
	this.headJobName = previous;
}

public String getBehind() {
	return tailJobName;
}

public void setBehind(String behind) {
	this.tailJobName = behind;
}

public int getStatus() {
	return tailJobstatus;
}

public void setStatus(int status) {
	this.tailJobstatus = status;
}

public int getDependence() {
	return dependence;
}

public void setDependence(int dependence) {
	this.dependence = dependence;
}

}
