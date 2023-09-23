package com.jupiter.etl.schedule;

import java.awt.Color;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import com.jupiter.etl.jobinfo.DesignerGraph;
import com.jupiter.etl.jobinfo.DetailFrame;
import com.jupiter.etl.jobinfo.EditJobProperty;
import com.jupiter.etl.jobinfo.SaveScheduleType;
import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.etl.jobinfo.entities.JobLocation;
import com.jupiter.mybatis.dao.DBUnit;

public class MonitorGraph extends DesignerGraph {
	
	private static final long serialVersionUID = -763625233249157249L;
	
	private java.util.Timer timer;
	private Monitor monitor;
	private volatile DefaultGraphCell infoCell = new DefaultGraphCell("调度类型:"+Monitor.scheduleType+" 批次号:"+Monitor.batchno );           //显示调度类型和批次号
	
	public MonitorGraph(){
		GraphConstants.setBounds(infoCell.getAttributes(), new Rectangle2D.Double(10, 10, 35, 20));
		GraphConstants.setAbsolute(infoCell.getAttributes(), true);
		GraphConstants.setAbsoluteX(infoCell.getAttributes(), false);
		GraphConstants.setFont(infoCell.getAttributes(),new Font("黑体",0,16));
		GraphConstants.setAutoSize(infoCell.getAttributes(),true);
		GraphConstants.setEditable(infoCell.getAttributes(),true);
		this.getGraphLayoutCache().insert(infoCell);
		
	}
	
	//初始化菜单(jobCell)
	public void initCellMenu(PopupMenu menu,final String jobname,final Job job,final DesignerGraph graph){
		MenuItem viewDetail = new MenuItem("View Input/Output Detail");
		MenuItem runLonelyItem = new MenuItem("Run lonely");
		MenuItem stopItem = new MenuItem("Stop");
		MenuItem viewLog = new MenuItem("View Log");
		MenuItem runFromThisItem = new MenuItem("Run From This");
		MenuItem editItem = new MenuItem("Edit");
		MenuItem collapseItem = new MenuItem("Collapse Selection");
		menu.add(viewDetail);
		menu.add(runLonelyItem);
		menu.add(stopItem);
		menu.add(runFromThisItem);
		menu.add(viewLog);
		menu.add(editItem);
		menu.add(collapseItem);
		menu.setEnabled(true);
		viewDetail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("查看输入输出");
				DetailFrame detailFrame = new DetailFrame(jobname,"dsjob");
				detailFrame.setLocationRelativeTo(backgroundComponent);//backgroundComponent
				detailFrame.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
				detailFrame.setVisible(true);
				detailFrame.setAlwaysOnTop(true);
			}
		});
		//运行指定job
		runLonelyItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("运行选择的作业");
				System.out.println(getSelectionCell());
				System.out.println("x:"+getCellBounds(getSelectionCell()).getX());
				System.out.println("y:"+getCellBounds(getSelectionCell()).getY());
				
				 Thread runBlankJob = new Thread(
						 new  BlankJob(Monitor.scheduleType,Monitor.batchno, job.getJobname(),job.getOn_fail_action()));
				 runBlankJob.start();
			}
		});
		//编辑job属性
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditJobProperty edit = new EditJobProperty(null,jobname, true,job,graph);
				edit.setLocationRelativeTo(backgroundComponent);
				edit.setVisible(true);
			}
		});
		//折叠
		collapseItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.print("i am "+job.getClass().toString());
				getGraphLayoutCache().collapse(getSelectionCells());
			}
		});
	}

	// 初始化菜单(schedule)
	public void initScheduleMenu(PopupMenu menu,final DesignerGraph graph) {
		MenuItem refresh = new MenuItem("Refresh");
		MenuItem cacelAutoRefresh = new MenuItem("Cancel Auto refresh");
		MenuItem startBatch = new MenuItem("Start New Batch");
		MenuItem stopBatch = new MenuItem("Stop Batch");
		MenuItem continuBatch = new MenuItem("Continu Batch");
		MenuItem saveAll = new MenuItem("Save All");
		menu.add(refresh);
		menu.add(cacelAutoRefresh);
		menu.add(startBatch);
		menu.add(stopBatch);
		menu.add(continuBatch);
		menu.add(saveAll);
		menu.setEnabled(true);
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshScheduleJobLocationList(Monitor.batchno);
			}
		});
		cacelAutoRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (timer != null)
					timer.cancel();
			}
		});
		startBatch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monitor.startSchedule(e);
			}
		});
		stopBatch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monitor.stopSchedule(e);
			}
		});
		saveAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaveScheduleType saveScheduleType = new SaveScheduleType(null,true,graph);
				saveScheduleType.setLocationRelativeTo(backgroundComponent);
				saveScheduleType.setVisible(true);
				//saveAllJobs();
			}
		});
	}

	//刷新调度状态
	//刷新调度状态
		public void refreshScheduleJobLocationList(String batchno){
			List<JobLocation> jobLocationList = DBUnit.refreshScheduleJobLocationList(batchno);
			for (int i = 0; i < jobLocationList.size(); i++) {
				JobLocation jb = jobLocationList.get(i);
				int status = jb.status;
				for(int j=0;j<this.jobs.size();j++){
					Job jCell = this.jobs.get(j);
					//若名称相同,且状态不同,则更新状态和显示颜色
					if(jb.jobname.equals(this.convertValueToString(jCell)) && jb.status!=jCell.jobstatus){
						jCell.jobstatus = status;
						GraphConstants.setOpaque(jCell.getAttributes(), true);// 设置非透明度
						switch (status) {
						case 1000:GraphConstants.setBackground(jCell.getAttributes(), Color.WHITE);break;  //unready
						case 100:GraphConstants.setBackground(jCell.getAttributes(), new Color(232,232,232));break;        //ready - waiting
						case 0:GraphConstants.setBackground(jCell.getAttributes(), new Color(152,245,255));break;//running
						case 1:GraphConstants.setBackground(jCell.getAttributes(), Color.GREEN);break;
						case 2:GraphConstants.setBackground(jCell.getAttributes(), Color.YELLOW);break;
						case 3:GraphConstants.setBackground(jCell.getAttributes(), Color.RED);break;
						case 4:GraphConstants.setBackground(jCell.getAttributes(), Color.PINK);
						}
			//getGraphLayoutCache().refresh(new DefaultCellViewFactory().createView(this.model, jCell), true);
					}
				}
			}
			this.getGraphLayoutCache().editCell(infoCell, infoCell.getAttributes());
			this.refresh();
			this.getGraphLayoutCache().reload();
			//this.getGraphLayoutCache().update();
			//getGraphLayoutCache().setVisible(c1.toArray(),false);
		}
		
		//响应鼠标双击,双击编辑调度属性
		public void doubleClickAction(Object cell){
			EditJobProperty edit = new EditJobProperty(null,this.convertValueToString(cell), true,(Job)cell,this);
			edit.setLocationRelativeTo(backgroundComponent);
			edit.setVisible(true);
			//this.refresh();
			//this.getGraphLayoutCache().insert(cell);  //重新插入将刷新 cellview
			//new DefaultCellViewFactory().createView(model, cell);//得到cellview
			this.getGraphLayoutCache().editCell(cell, ((Job)cell).getAttributes());  //编辑也可以刷新cellview
			//this.getGraphLayoutCache().reload();
			//getGraphLayoutCache().setVisible(cell, false);
			//getGraphLayoutCache().setVisible(cell, true);
		}
	
	public java.util.Timer getTimer() {
		return timer;
	}

	public void setTimer(java.util.Timer timer) {
		this.timer = timer;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public DefaultGraphCell getInfoCell() {
		return infoCell;
	}

	public void setInfoCell(DefaultGraphCell infoCell) {
		this.infoCell = infoCell;
	}

	
}
