package com.jupiter.etl.jobinfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import com.jgraph.layout.organic.JGraphFastOrganicLayout;
import com.jgraph.layout.organic.JGraphOrganicLayout;
import com.jgraph.layout.tree.JGraphCompactTreeLayout;
import com.jgraph.layout.tree.JGraphRadialTreeLayout;
import com.jgraph.layout.tree.JGraphTreeLayout;
import com.jupiter.WriteLog;
import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.etl.jobinfo.entities.JobEdge;
import com.jupiter.etl.jobinfo.entities.JobLocation;
import com.jupiter.etl.jobinfo.entities.JobRelation;
import com.jupiter.mybatis.dao.DBUnit;
import com.jupiter.mybatis.po.User;

public class DesignerGraph extends JGraph implements KeyListener, MouseListener, MouseWheelListener {
	private int contorl = 0; // 是否按下ctrl
	private double d = 1.0; // 缩放参数
	private int fi = 0; // 查找到第几个(finderIndex)
	private double focusX = 0; // 焦点x坐标
	private double focusY = 0; // 焦点y坐标
	public ArrayList<Job> jobs = new ArrayList<Job>(); // 全部作业(不包括边):job+file+bo
	Logger logger = Logger.getLogger(DesignerGraph.class);

	public ArrayList<Job> getAllJobs() {
		return jobs;
	}

	public ArrayList<JobEdge> jobRelations = new ArrayList<JobEdge>(); // 全部边
	public ArrayList<Job> files = new ArrayList<Job>(); // TAB类型
	public ArrayList<JobEdge> filesEdgs = new ArrayList<JobEdge>(); // TAB类型的边
	public ArrayList<Job> boruntimes = new ArrayList<Job>(); // boruntime类型
	public ArrayList<JobEdge> boruntimesEdgs = new ArrayList<JobEdge>(); // boruntime类型的边
	public ArrayList<Job> outDatedJobs = new ArrayList<Job>(); // 过时的作业
	//public ArrayList<Job> outDatedEdges = new ArrayList<Job>(); // 过时的作业的边
	private Job endCell; // 调度结束标记,隐藏不可见
	// private JobEdge jobEdgeSelect = new JobEdge("",""); //选择的边显示颜色
	GraphUndoManager undoManager; // ctrl + z

	PopupMenu edgMenu = new PopupMenu(); // 右键菜单

	/**
	 * 
	 */
	private static final long serialVersionUID = -4023905464115881160L;
	GraphModel model = new DefaultGraphModel();
	public DefaultCellViewFactory defaultCellViewFactorynew = new DefaultCellViewFactory();
	GraphLayoutCache view = new GraphLayoutCache(model, defaultCellViewFactorynew, true);
	private User user;
	private JobioBaseJFrame jfram;

	public DesignerGraph() {
		this.init();
	}

	public DesignerGraph(User user) {
		this.user = user;
		this.init();
	}
	
	public DesignerGraph(User user,JobioBaseJFrame jfram) {
		this.user = user;
		this.jfram = jfram;
		this.init();
	}

	public void init() {
		this.setModel(model);
		this.setGraphLayoutCache(view);
		// this.setCellMenu(cellMenu); //初始化右键菜单
		// this.add(cellMenu); //添加到graph
		// 一些graph对象的简单调整
		// graph.setMoveable(false);//可否移动整个图形
		this.setDisconnectable(false);// 不能移动边的指向,但是可以移动图形
		this.setSizeable(false);
		// graph.setDisconnectOnMove(false);//可否移动整个边,但是在边的源点终点改变后失效
		/*
		 * 显示网格 { graph.setGridEnabled(true); graph.setGridVisible(true); }
		 * graph.setGridMode(JGraph.CROSS_GRID_MODE);
		 */
		// this.setMoveBelowZero(true); //是否允许cell越出左上角.通常设置为false,除非有特殊用处
		this.setAntiAliased(true);// 圆滑图像线条
		// graph.setSelectionEnabled(false);//能否选择单个cell
		this.setCloneable(true); // 是否可复制 ctrl+拖曳
		this.setBendable(true);
		this.setConnectable(true); // 拖曳连接
		// this.setFont(new Font("宋体",1,12)); //字体
		this.setFont(new Font("Default Sans Serif", 0, 10)); // 字体
		this.getSelectionModel().setChildrenSelectable(true);
		this.setLockedHandleColor(Color.green);
		this.setMarqueeColor(Color.black); // 鼠标拖曳选择框
		this.setHighlightColor(Color.red); // 高亮颜色
		// graph.setJumpToDefaultPort(true);
		this.setFocusable(true); // 可以或者焦点

		this.addMouseListener(this); // 监听鼠标
		this.addKeyListener(this); // 监听键盘
		this.addMouseWheelListener(this); // 监听滚轮
		undoManager = new GraphUndoManager(); // ctrl+z
		// Register UndoManager with the Model
		this.getModel().addUndoableEditListener(undoManager);// ctrl+z
		/*
		 * this.addMouseListener(new MouseAdapter() { public void
		 * mousePressed(MouseEvent e) { //
		 * logger.info(graph.getSelectionCells().length) // 右键双击删除 // Get
		 * Selected Cells Object[] cells = getSelectionCells(); if
		 * (e.isMetaDown() & e.getClickCount() == 2 & cells != null) { // Remove
		 * Cells (incl. Descendants) from the Model
		 * getModel().remove(getDescendants(cells)); } if (e.getClickCount() ==
		 * 2) { // Get Cell under Mousepointer int x = e.getX(), y = e.getY();
		 * Object cell = getFirstCellForLocation(x, y); // Print Cell Label if
		 * (cell != null ) { if(cell.getClass() == DefaultGraphCell.class &
		 * convertValueToString(cell) != "start"){ //String lab =
		 * graph.convertValueToString(cell); //logger.info("lab = " +
		 * lab); //Object clone = DefaultGraphModel.cloneCell(graph.getModel(),
		 * cell); //graph.getGraphLayoutCache().insert(clone);
		 * GraphConstants.setGradientColor(((DefaultGraphCell)
		 * cell).getAttributes(), Color.WHITE);// 设置颜色
		 * //GraphConstants.setOpaque
		 * (((DefaultGraphCell)cell).getAttributes(),true);// 设置透明度
		 * //graph.refresh(); getGraphLayoutCache().reload(); } } } } });
		 */
	}

	// 初始化右键菜单(jobCell)
	public void initCellMenu(PopupMenu menu, final String jobname, final Job job, final DesignerGraph graph) {
		MenuItem viewDetail = new MenuItem("View Detail");
		MenuItem analyzeBackwardItem = new MenuItem("Analyze Backward");
		MenuItem analyzeForwardItem = new MenuItem("Analyze Forward");
		MenuItem viewLog = new MenuItem("View Log");
		MenuItem editItem = new MenuItem("Edit");
		MenuItem collapseItem = new MenuItem("Collapse Selection");
		MenuItem selectRootsItem = new MenuItem("Select Roots");
		MenuItem selectChildrenItem = new MenuItem("Select Children");
		MenuItem selectOthersItem = new MenuItem("Select Others");
		MenuItem newAJobItem = new MenuItem("New A Job");
		menu.add(viewDetail);
		menu.add(analyzeBackwardItem);
		menu.add(analyzeForwardItem);
		menu.add(viewLog);
		menu.add(editItem);
		menu.add(collapseItem);
		menu.add(selectRootsItem);
		menu.add(selectChildrenItem);
		menu.add(selectOthersItem);
		menu.add(newAJobItem);
		menu.setEnabled(true);

		// 查看job/file详细信息
		viewDetail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// logger.info("查看输入输出");
				DetailFrame detailFrame = new DetailFrame(jobname, job.jobtype);
				detailFrame.setLocationRelativeTo(backgroundComponent);// backgroundComponent
				detailFrame.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
				detailFrame.setVisible(true);
				detailFrame.setAlwaysOnTop(true);
			}
		});
		// 编辑job属性
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditJobProperty edit = new EditJobProperty(null, jobname, true, job, graph);
				edit.setLocationRelativeTo(backgroundComponent);
				edit.setVisible(true);
			}
		});
		// 折叠
		collapseItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//logger.info("i am " + job.getClass().toString());
				getGraphLayoutCache().collapse(getSelectionCells());
			}
		});
		// Analyze Forward
		analyzeForwardItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//logger.info("i am " + job.getClass().toString());
				job.setX(getCellBounds(job).getX());
				job.setY(getCellBounds(job).getY());
				analyzeForward(job);
			}
		});

		// Analyze Backward
		analyzeBackwardItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//logger.info("i am " + job.getClass().toString());
				job.setX(getCellBounds(job).getX());
				job.setY(getCellBounds(job).getY());
				analyzeBackward(job);
			}
		});
	
		// 响应select roots事件
				selectRootsItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						selectRoots(job);
					}
				});

				// 响应select all事件
				selectChildrenItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						selectChildren(job);
					}
				});
	}

	// Analyze Forward
	public void analyzeForward(Job root) {
		logger.info("analyzeForward:" + root.getJobname());
		/*
		 * ArrayList children = DBUnit.getChildren(job); ArrayList roots = new
		 * ArrayList();
		 */
		ArrayList<Job> children = DBUnit.getChildren(root, this);
		ArrayList<JobEdge> jobRelations = new ArrayList<JobEdge>();
		i: for (int i = 0; i < children.size(); i++) {
			Job Child = children.get(i);
			for (int j = 0; j < this.jobRelations.size(); j++) {
				JobEdge jobEdge = this.jobRelations.get(j);
				if (jobEdge.headJobName.equals(root.jobname) && jobEdge.tailJobName.equals(Child.jobname)){
					//jobRelations.add(jobEdge);
					continue i; //jobEdge已经存在，不再new，跳出循环
				}
			}
			//创建一条jobEdge
			JobEdge jobRelation = new JobEdge(root, Child);
			jobRelations.add(jobRelation);
		}
		ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
		cells.addAll(children);
		cells.addAll(jobRelations);
		classfy(children);
		this.jobRelations.addAll(jobRelations);
		this.getGraphLayoutCache().insert(cells.toArray());
		this.getGraphLayoutCache().toFront(children.toArray());
		//this.undoManager.setLimit(1);
		if(children.size() > 0){
			this.focusX = children.get(0).getX();
			this.focusY = children.get(0).getY();
			this.jfram.gotoFocus(this.jfram.jScrollPane1,this);
		}
		//this.focusX = (int) this.getCellBounds(children.get(0)).getX();
		//this.focusY = (int) this.getCellBounds(children.get(0)).getY();
		//logger.info("I am :" + this.getBackgroundComponent().getClass().getName());
		//this.requestFocus();
	}

	// Analyze Backward
	public void analyzeBackward(Job child) {
		logger.info("analyze Backward:" + child.getJobname());
		ArrayList<Job> roots = DBUnit.getRoots(child, this);
		ArrayList<JobEdge> jobRelations = new ArrayList<JobEdge>();
		i: for (int i = 0; i < roots.size(); i++) {
			Job root = roots.get(i);
			for (int j = 0; j < this.jobRelations.size(); j++) {
				JobEdge jobEdge = this.jobRelations.get(j);
				if (jobEdge.headJobName.equals(root.jobname) && jobEdge.tailJobName.equals(child.jobname)){
					jobRelations.add(jobEdge);
					continue i;
				}
			}
			JobEdge jobRelation = new JobEdge(root, child);
			jobRelations.add(jobRelation);
		}
		ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
		cells.addAll(roots);
		cells.addAll(jobRelations);
		classfy(roots);
		this.jobRelations.addAll(jobRelations);
		this.getGraphLayoutCache().insert(cells.toArray());
		this.getGraphLayoutCache().toFront(cells.toArray());
	}

	// 对所有job进行分类，供界面是否显示
	public void classfy(ArrayList<Job> cells) {
		this.jobs.addAll(cells);
		for (int i = 0; i < cells.size(); i++) {
			Job job = cells.get(i);
			String jobtype = job.getJobtype();
			if (jobtype.equals("TAB")) { // TAB
				files.add(job);
			} else if (jobtype.equals("bojob")) { // boruntime
				boruntimes.add(job);
			}
			if (job.isValid == 0) {
				outDatedJobs.add(job); // 过时的作业
			}
		}
	}

	// 初始化右键菜单(schedule)
	public void initScheduleMenu(PopupMenu menu, final DesignerGraph graph) {
		MenuItem saveAll = new MenuItem("Save");
		MenuItem selectAllItem = new MenuItem("Select All");
		MenuItem clearAll = new MenuItem("Clear");
		menu.add(saveAll);
		menu.add(selectAllItem);
		menu.add(clearAll);
		menu.setEnabled(true);
		
		//响应save All事件
		saveAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaveScheduleType saveScheduleType = new SaveScheduleType(null, true, graph);
				saveScheduleType.setLocationRelativeTo(backgroundComponent);
				saveScheduleType.setVisible(true);
				// saveAllJobs();
			}
		});
		
		//响应clear All事件
		clearAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graph.getModel().remove(jobs.toArray());
				graph.getModel().remove(jobRelations.toArray());
				graph.jobs.clear();
				graph.jobRelations.clear();
				graph.files.clear();
				graph.filesEdgs.clear();
				graph.boruntimes.clear();
				//graph.boruntimesEdgs.clear();
				graph.outDatedJobs.clear();
				//graph.outDatedEdges.clear();
				logger.info("clear all");
			}
		});
		
		//响应select all事件
		selectAllItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});
	}


	// 演示,已弃用
	public ArrayList<DefaultGraphCell> initCellsList() {
		// 定义一个存放cell的集合类
		ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
		// 建立你的第一个vertex对象
		cells.add(new DefaultGraphCell(new String("jb_dwn_chdrpf_to_chdrpf")));
		GraphConstants.setBounds(cells.get(0).getAttributes(), new Rectangle2D.Double(20, 80, "jb_dwn_chdrpf_to_chdrpf".length() * 7, 20)); // 开始坐标xy,宽高
		// 设置颜色和透明属性
		GraphConstants.setGradientColor(cells.get(0).getAttributes(), Color.orange);
		GraphConstants.setOpaque(cells.get(0).getAttributes(), true);
		// 为这个vertex加入一个port
		DefaultPort port0 = new DefaultPort();
		cells.get(0).add(port0);
		// 同理加入第二个vertex
		cells.add(new DefaultGraphCell(new String("jb_hq_commission_file_month_temp2")));
		GraphConstants.setBounds(cells.get(1).getAttributes(), new Rectangle2D.Double(220, 20, "jb_hq_commission_file_month_temp2".length() < 23 ? 160
				: "jb_hq_commission_file_month_temp2".length() * 7, 20));
		GraphConstants.setGradientColor(cells.get(1).getAttributes(), Color.red);
		GraphConstants.setOpaque(cells.get(1).getAttributes(), true);
		DefaultPort port1 = new DefaultPort();
		cells.get(1).add(port1);
		// 同理加入第三个vertex
		cells.add(new DefaultGraphCell(new String("jb_hq_policy_file_temp3_first")));
		GraphConstants.setBounds(cells.get(2).getAttributes(), new Rectangle2D.Double(220, 140, "jb_hq_policy_file_temp3_first".length() < 23 ? 160
				: "jb_hq_policy_file_temp3_first".length() * 7, 20));
		GraphConstants.setGradientColor(cells.get(2).getAttributes(), Color.blue);
		GraphConstants.setOpaque(cells.get(2).getAttributes(), true);
		DefaultPort port2 = new DefaultPort();
		cells.get(2).add(port2);

		// 加入一条边，将jb_dwn_chdrpf_to_chdrpf和jb_hq_commission_file_month_temp2的两个port连接起来
		DefaultEdge edge = new DefaultEdge();
		edge.setSource(cells.get(0).getChildAt(0));
		edge.setTarget(cells.get(1).getChildAt(0));
		// 为edge设置一个箭头
		int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(edge.getAttributes(), arrow);
		GraphConstants.setEndFill(edge.getAttributes(), true); // 填充箭头
		// 将edge加入cell集合类
		cells.add(edge);
		// 同理
		DefaultEdge edge1 = new DefaultEdge();
		edge1.setSource(cells.get(0).getChildAt(0));
		edge1.setTarget(cells.get(2).getChildAt(0));
		GraphConstants.setLineEnd(edge1.getAttributes(), arrow); // 为edge1设置一个箭头
		// 将edge加入cell集合类
		cells.add(edge1);
		// 同理
		DefaultEdge edge2 = new DefaultEdge();
		edge2.setSource(cells.get(2).getChildAt(0));
		edge2.setTarget(cells.get(1).getChildAt(0));
		GraphConstants.setLineEnd(edge2.getAttributes(), arrow); // 为edge1设置一个箭头
		// 将edge加入cell集合类
		cells.add(edge2);

		return cells;
	}

	// 计算滚轮数字
	private double updatePreferredSize(int wheelRotation, Point stablePoint) {
		double scaleFactor = findScaleFactor(wheelRotation);
		return scaleFactor;
	}

	// 计算缩放的参数
	private double findScaleFactor(int wheelRotation) {
		double d = wheelRotation * 0.1;
		// return (d > 0) ? 1/d : -d;
		return d;
	}

//	// 获取job列表及其位置 v2.0之前
//	public ArrayList<Job> getJobLocationsList(int jbtype_in, String jobname, String filename) {
//		ArrayList<Job> cells = new ArrayList<Job>();
//		DBconnect dbconn = new DBconnect();
//		if (jobname.equals("jb_"))
//			jobname = "";
//		List<JobLocation> jobLocationList = dbconn.getJobLocationList(jbtype_in, jobname, filename);
//		if (jobLocationList.size() > 0) {
//			cells.add(new Job("start", 1, "blankjob"));
//			int i = 0;
//			int x = jobLocationList.get(0).x;
//			int minY = jobLocationList.get(0).y;
//			int maxY = jobLocationList.get(0).y;
//			while (x == jobLocationList.get(i).x) {
//				if (minY > jobLocationList.get(i).y)
//					minY = jobLocationList.get(i).y;
//				else if (maxY < jobLocationList.get(i).y)
//					maxY = jobLocationList.get(i).y;
//				i++;
//			}
//			// GraphConstants.setBounds(cells.get(0).getAttributes(),new
//			// Rectangle2D.Double(20, jobLocationList.get(0).y/2, 35,
//			// 20));//x,y,长,宽
//			GraphConstants.setBounds(cells.get(0).getAttributes(), new Rectangle2D.Double(20, (minY + maxY) / 2, 35, 20));// x,y,长,宽
//			// GraphConstants.setLineStyle(cells.get(0).getAttributes(),1);
//		} else {
//			cells.add(new Job("null", 1, "blankjob"));
//			GraphConstants.setBounds(cells.get(0).getAttributes(), new Rectangle2D.Double(20, 10, 35, 20));
//			;
//		}
//		GraphConstants.setBackground(cells.get(0).getAttributes(), Color.GRAY);
//		GraphConstants.setOpaque(cells.get(0).getAttributes(), true);
//		GraphConstants.setAbsolute(cells.get(0).getAttributes(), true);
//		GraphConstants.setVerticalAlignment(cells.get(0).getAttributes(), 0);
//		cells.get(0).add(new DefaultPort());
//		this.jobs.add(cells.get(0));
//		for (int i = 0; i < jobLocationList.size(); i++) {
//			JobLocation jb = jobLocationList.get(i);
//			Job jCell = new Job(jb.jobname, jobLocationList.get(i).isvalid, jobLocationList.get(i).jobtype);
//			cells.add(jCell);
//			GraphConstants.setBounds(jCell.getAttributes(), new Rectangle2D.Double(jb.x, jb.y, jb.jobname.length() * 7, 16)); // 开始坐标xy,宽高
//			// System.out.printf(jb.jobname);//+": x = " +
//			// this.getCellBounds(cells.get(i)).getX());
//			// logger.info("y = " +
//			// this.getCellBounds(cells.get(0)).toString());
//			// logger.info(jb.jobname + " x = " +
//			// GraphConstants.getBounds(jCell.getAttributes()).getX());
//
//			// GraphConstants.setLabelPosition(cells.get(j).getAttributes(),new
//			// Point2D.Double(jb.x*3-200, jb.y/2));
//			GraphConstants.setAutoSize(jCell.getAttributes(), true);
//			GraphConstants.setFont(jCell.getAttributes(), new Font("Default Sans Serif", 0, 10));
//			if (jb.jobtype.equals("TAB")) {// 设置血缘分析终点颜色 fn.contains(jb.jobname)
//				// jb.jobname.equalsIgnoreCase(filename)
//				files.add(jCell);
//				GraphConstants.setBackground(jCell.getAttributes(), Color.ORANGE);
//				GraphConstants.setOpaque(jCell.getAttributes(), true);
//			} else if (jb.jobtype.equals("bojob")) {
//				boruntimes.add(jCell);
//				GraphConstants.setBackground(jCell.getAttributes(), Color.CYAN);
//				GraphConstants.setOpaque(jCell.getAttributes(), true);
//			} else if (jb.jobtype.equals("BlankJob")) {
//				GraphConstants.setBackground(jCell.getAttributes(), Color.ORANGE);
//				GraphConstants.setOpaque(jCell.getAttributes(), true);
//			}
//			this.jobs.add(jCell);
//			DefaultPort port0 = new DefaultPort();
//			jCell.add(port0);
//			// GraphConstants.setOpaque(cells.get(j).getAttributes(), true);
//		}
//		return cells;
//	}

	// 获取job列表及其位置 v2.1\2.2
	public ArrayList<Job> getJobLocationsList2_1(User user, int jbtype_in, int sourceOrTarget, String jobname) {
		ArrayList<Job> cells = new ArrayList<Job>();
		List<JobLocation> jobLocationList = DBUnit.getJobLocationList3_0(user, jbtype_in, sourceOrTarget, jobname);
		if (jobLocationList.size() <= 0) {
			// 返回为空时
			Job nullJob = new Job(20, 10, "null", 1, "blankjob");
			cells.add(nullJob);
			// GraphConstants.setBounds(nullJob.getAttributes(), new
			// Rectangle2D.Double(20, 10, 35, 20));
			this.classfy(cells);
			return cells;
		} else {
			int i = 0;
			int x = jobLocationList.get(0).x;
			int minY = jobLocationList.get(0).y;
			int maxY = jobLocationList.get(0).y;
			while (i < jobLocationList.size() && x == jobLocationList.get(i).x) {
				if (minY > jobLocationList.get(i).y)
					minY = jobLocationList.get(i).y;
				else if (maxY < jobLocationList.get(i).y)
					maxY = jobLocationList.get(i).y;
				i++;
			}
			// 创建起始节点
			Job job0 = new Job(20.0, (minY + maxY) / 2, "start", 1, "blankjob");
			cells.add(job0);
			// 设置"start"节点的属性
			GraphConstants.setBackground(job0.getAttributes(), Color.GRAY); // 颜色
			GraphConstants.setOpaque(job0.getAttributes(), true); // 不透明
			GraphConstants.setAbsolute(job0.getAttributes(), true); // 可变大小?
			GraphConstants.setVerticalAlignment(job0.getAttributes(), 0);
			// GraphConstants.setBounds(cells.get(0).getAttributes(),new
			// Rectangle2D.Double(20, jobLocationList.get(0).y/2, 35,
			// 20));//x,y,长,宽
			/*
			 * GraphConstants.setBounds(job0.getAttributes(), new
			 * Rectangle2D.Double(20, (minY + maxY) / 2, 35, 20));// x,y,长,宽
			 * job0.setX(20.0); job0.setY((minY + maxY) / 2);
			 */
			// GraphConstants.setLineStyle(cells.get(0).getAttributes(),1);
		}
		// this.allCells.add(cells.get(0));
		for (int i = 0; i < jobLocationList.size(); i++) {
			JobLocation jb = jobLocationList.get(i);
			/*System.out.printf(i + ":" + jb.jobname);
			System.out.printf(": x = " + jb.x);
			System.out.printf(",y = " + jb.y + "\n");*/
			Job jCell = new Job(jb.x, jb.y, jb.jobname, jobLocationList.get(i).isvalid, jobLocationList.get(i).jobtype);
			// jCell.setX(jb.x);
			// jCell.setY(jb.y);
			cells.add(jCell);
			// GraphConstants.setBounds(jCell.getAttributes(), new
			// Rectangle2D.Double(jb.x, jb.y, jb.jobname.length() * 7, 16)); //
			// 开始坐标xy,宽高
			// logger.info(jb.jobname + " x = " +
			// GraphConstants.getBounds(jCell.getAttributes()).getX());

			// GraphConstants.setLabelPosition(cells.get(j).getAttributes(),new
			// Point2D.Double(jb.x*3-200, jb.y/2));
			// GraphConstants.setAutoSize(jCell.getAttributes(), true);
			// GraphConstants.setFont(jCell.getAttributes(), new
			// Font("Default Sans Serif", 0, 10));
			// 分别用CF1、cBO1记录 表、bountime
			/*
			 * if (jCell.jobtype.equals("TAB")) { files.add(jCell); } else if
			 * (jCell.jobtype.equals("bojob")) { boruntimes.add(jCell); } //
			 * 记录过时的作业和表 if (jCell.isValid == 0) { outDatedJobs.add(jCell); }
			 */
			// jCell.setColor();// 颜色
			// this.allCells.add(jCell);// 全部
			// DefaultPort port0 = new DefaultPort();
			// jCell.add(port0);
			// GraphConstants.setOpaque(cells.get(j).getAttributes(), true);
		}
		this.classfy(cells);
		return cells;
	}

	// 获取job的依赖关系
	public ArrayList<JobEdge> getJobRelationsList(User user, ArrayList<Job> c1) {
		ArrayList<JobEdge> c2 = new ArrayList<JobEdge>();
		// List<JobRelation> jobInfoList = dbconn.getRelationList("","");
		List<JobRelation> jobInfoList = DBUnit.getJobRelationList(user);
		if (jobInfoList.size() > 0)
			for (int i = 0; i < jobInfoList.size(); i++) {
				JobEdge edge1 = new JobEdge(jobInfoList.get(i).previous, jobInfoList.get(i).behind);
				this.jobRelations.add(edge1); // 全部边放进c2
				for (int j = 0; j < c1.size(); j++) {
					int f = 0;
					Job jCell = c1.get(j);
					// 搜索边的源
					if (jobInfoList.get(i).previous.equals(jCell.toString())) {
						edge1.setSource(jCell.getChildAt(0));
						if (jCell.isValid == 0) {
							//logger.info(jCell.toString() + ".isValid=" + jCell.isValid);
							GraphConstants.setLineColor(edge1.getAttributes(), Color.lightGray);
						}
						f++;
					}
					// 搜索边的目标
					if (jobInfoList.get(i).behind.equals(jCell.toString())) {
						edge1.setTarget(jCell.getChildAt(0));
						if (jCell.isValid == 0) {
							//logger.info(jCell.toString() + ".isValid=" + jCell.isValid);
							GraphConstants.setLineColor(edge1.getAttributes(), Color.lightGray);
						}
						if (jCell.getJobtype().equals("TAB")) {
							this.filesEdgs.add(edge1); // 指向file的边
						} else if (jCell.getJobtype().equals("bojob")) {
							this.boruntimesEdgs.add(edge1);
						}
						f++;
					}// c1.get(j).getChildAt(0)
					if (f == 2)
						break;
				}
				// 为edge设置一个箭头
				GraphConstants.setLineEnd(edge1.getAttributes(), GraphConstants.ARROW_CLASSIC);
				// GraphConstants.setEndFill(edge1.getAttributes(), true); //
				// 填充箭头
				c2.add(edge1);
			}

		return c2;
	}

	// 是否显示维表/事实表
	public void setTabVisible(boolean b) {
		if (b == false)
			this.getGraphLayoutCache().setVisible(this.files.toArray(), false);
		else
			this.getGraphLayoutCache().setVisible(this.files.toArray(), true);

		/*
		 * 落后的办法，已弃用 if(b==false){
		 * //this.getModel().remove(tabCells.toArray());/
		 * /this.getDescendants(tabCells.toArray())
		 * //this.getModel().remove(listEdges.toArray(new
		 * Object[listEdges.size()]));
		 * this.getModel().remove(this.getDescendants(cF1.toArray())); //
		 * 得到指定边的目标 //logger.info("===" +
		 * getModel().getParent(getModel().getTarget(cF2.get(1))));
		 * this.getModel().remove(cF2.toArray()); }else{ Iterator<JobEdge> it =
		 * cF2.iterator(); while(it.hasNext()){ JobEdge jEdge =
		 * (JobEdge)it.next(); //指定源 for(int i=0;i<c1.size();i++){ JobGraphCell
		 * jCell = c1.get(i); if(jEdge.getPrevious().equals(jCell.toString()))
		 * jEdge.setSource(jCell.getChildAt(0)); } //指定目标 for(int
		 * i=0;i<cF1.size();i++){ JobGraphCell jCell = cF1.get(i); if
		 * (jEdge.getBehind().equals(jCell.toString())) { jCell.add(new
		 * DefaultPort()); //logger.info("jCell.getChildCount()=" +
		 * jCell.getChildCount()); 有多余的port
		 * jEdge.setTarget(jCell.getChildAt(0)); } } }
		 * this.getGraphLayoutCache().insert(cF1.toArray());
		 * this.getGraphLayoutCache().insert(cF2.toArray());
		 * this.getGraphLayoutCache().toFront(cF1.toArray()); }
		 */
	}

	// 是否显示boruntime
	public void setBOVisible(boolean selected) {
		if (selected == false)
			this.getGraphLayoutCache().setVisible(this.boruntimes.toArray(), false);
		else
			this.getGraphLayoutCache().setVisible(this.boruntimes.toArray(), true);

		/*
		 * if(selected==false){
		 * //this.getModel().remove(tabCells.toArray());//this
		 * .getDescendants(tabCells.toArray())
		 * //this.getModel().remove(listEdges.toArray(new
		 * Object[listEdges.size()]));
		 * this.getModel().remove(this.getDescendants(cBO1.toArray())); //
		 * 得到指定边的目标 //logger.info("===" +
		 * getModel().getParent(getModel().getTarget(cF2.get(1))));
		 * this.getModel().remove(cBO2.toArray()); }else{ Iterator<JobEdge> it =
		 * cBO2.iterator(); while(it.hasNext()){ JobEdge jEdge =
		 * (JobEdge)it.next(); //指定源 for(int i=0;i<c1.size();i++){ JobGraphCell
		 * jCell = c1.get(i); if(jEdge.getPrevious().equals(jCell.toString()))
		 * jEdge.setSource(jCell.getChildAt(0)); } //指定目标 for(int
		 * i=0;i<cBO1.size();i++){ JobGraphCell jCell = cBO1.get(i); if
		 * (jEdge.getBehind().equals(jCell.toString())) { jCell.add(new
		 * DefaultPort()); //logger.info("jCell.getChildCount()=" +
		 * jCell.getChildCount()); 有多余的port
		 * jEdge.setTarget(jCell.getChildAt(0)); } } }
		 * this.getGraphLayoutCache().insert(cBO1.toArray());
		 * this.getGraphLayoutCache().insert(cBO2.toArray());
		 * this.getGraphLayoutCache().toFront(cBO1.toArray()); }
		 */
	}

	// 是否显示过时的Job
	public void setOutDatedVisible(boolean b) {
		if (b == false)
			this.getGraphLayoutCache().setVisible(this.outDatedJobs.toArray(), false);
		else
			this.getGraphLayoutCache().setVisible(this.outDatedJobs.toArray(), true);
	}

	// 重新布局
	public void rLayout(int arg) {
		JGraphFacade facade = new JGraphFacade(this); // Pass the facade the
														// JGraph instance
		JGraphLayout layout = null;
		JGraphTreeLayout treeLayout = null;
		JGraphCompactTreeLayout jctl = null;
		JGraphOrganicLayout jol = null;
		// logger.info("arg = " + arg);
		switch (arg) {
		// new JGraphRadialTreeLayout();// new JGraphCompactTreeLayout();//new
		// JGraphFastOrganicLayout();
		case 1:
			layout = new JGraphFastOrganicLayout();
			break; // 快速
		case 2:
			jol = new JGraphOrganicLayout();
			jol.setApproxNodeDimensions(true);
			layout = jol;
			break; // 有机
		case 3:
			treeLayout = new JGraphTreeLayout();
			treeLayout.setAlignment(SwingConstants.BOTTOM);
			treeLayout.setLevelDistance(80.0);
			treeLayout.setNodeDistance(2);
			// treeLayout.setRouteTreeEdges(true);
			layout = treeLayout;
			break; // 树形布局
		case 4:
			layout = new JGraphRadialTreeLayout();
			break; // 放射布局
		case 5:
			jctl = new JGraphCompactTreeLayout();
			jctl.setPositionMultipleTrees(true);
			layout = jctl;
			break; // 简洁树形
		case 6:
			layout = new JGraphHierarchicalLayout();
			break; // 曲线
		}
		layout.run(facade); // Run the layout on the facade. Note that layouts
							// do not implement the Runnable interface, to avoid
							// confusion
		Map<?, ?> nested = facade.createNestedMap(true, true); // Obtain a map
																// of the
																// resulting
																// attribute
																// changes from
																// the facade
		this.getGraphLayoutCache().edit(nested); // Apply the results to the
													// actual graph
	}

	// 默认布局
	public void defaultLayout() {
		for (int i = 0; i < jobs.size(); i++) {
			Job jb = jobs.get(i);
			GraphConstants.setBounds(jb.getAttributes(), new Rectangle2D.Double(jb.x, jb.y, jb.jobname.length() * 7, 16));
		}
		this.getGraphLayoutCache().reload();
		this.refresh();
		// this.setSize(1500, 3000);
		// GraphConstants.setBounds(jCell.getAttributes(),new
		// Rectangle2D.Double(jb.x, jb.y, jb.jobname.length()*7, 16)); //
		// 开始坐标xy,宽高
	}

	// 已弃用
	/*
	 * public ArrayList<DefaultGraphCell> getCellsList(String jobname,String
	 * filename) { ArrayList<DefaultGraphCell> cells = new
	 * ArrayList<DefaultGraphCell>();
	 * 
	 * DBconnect dbconn = new DBconnect(); List<JobRelation> jobInfoList=
	 * dbconn.getRelationList(jobname,filename);
	 * 
	 * cells.add(new DefaultGraphCell("start"));
	 * GraphConstants.setBounds(cells.get(0).getAttributes(),new
	 * Rectangle2D.Double(20, jobInfoList.get(0).y/2, 35, 20));
	 * GraphConstants.setGradientColor
	 * (cells.get(0).getAttributes(),Color.orange);//设置颜色
	 * GraphConstants.setOpaque(cells.get(0).getAttributes(), true);//设置透明度
	 * cells.get(0).add(new DefaultPort());
	 * 
	 * for(int i=1,j=1; i<=jobInfoList.size(); i++){ JobRelation jb =
	 * jobInfoList.get(i-1); if(jb.rn != 1) continue;//非顶点退出 // 建立你的第一个vertex对象
	 * cells.add(new DefaultGraphCell(jb.behind)); GraphConstants.setBounds(
	 * cells.get(j).getAttributes(), new
	 * Rectangle2D.Double(jb.x+jb.pathlength*200, jb.y/2,
	 * jb.behind.length()*6.5, 20)); // 开始坐标xy,宽高 // 为这个vertex加入一个port
	 * DefaultPort port0 = new DefaultPort(); cells.get(j).add(port0); j++; }
	 * 
	 * for(int i=1; i<=jobInfoList.size(); i++){ DefaultEdge edge1 = new
	 * DefaultEdge(); //搜索边的目标 for(int j=1; j<cells.size(); j++){
	 * if(jobInfoList.get(i-1).behind.equals(cells.get(j).toString()))
	 * edge1.setTarget(cells.get(j).getChildAt(0));
	 * if(jobInfoList.get(i-1).pathlength==1)
	 * edge1.setSource(cells.get(0).getChildAt(0)); } //搜索边的源 for(int j=1;
	 * j<cells.size(); j++){
	 * if(jobInfoList.get(i-1).previous.equals(cells.get(j).toString()))
	 * edge1.setSource(cells.get(j).getChildAt(0)); } // 为edge设置一个箭头
	 * GraphConstants.setLineEnd(edge1.getAttributes(),
	 * GraphConstants.ARROW_CLASSIC);
	 * //GraphConstants.setLineBegin(edge1.getAttributes(),
	 * GraphConstants.ARROW_CLASSIC);
	 * GraphConstants.setEndFill(edge1.getAttributes(), true); // 填充箭头
	 * cells.add(edge1); }
	 * 
	 * return cells; }
	 */

	// 显示初始化调度状态1-job
	public ArrayList<Job> getScheduleJobLocationList(String scheduleType) {
		// ArrayList<Job> cells = new ArrayList<Job>(); //创建Jobs
		ArrayList<Job> jobs = DBUnit.initScheduleJobLocationList(scheduleType);
		if (jobs.size() > 0) {
			// /cells.add(new Job("start", jobs.get(0).isValid,
			// jobs.get(0).jobtype));
			// GraphConstants.setBounds(cells.get(0).getAttributes(), new
			// Rectangle2D.Double(20, jobs.get(0).y / 2, 35, 20));
			// GraphConstants.setLineStyle(cells.get(0).getAttributes(),1);
		} else {
			jobs.add(new Job("null", 1, ""));
			GraphConstants.setBounds(jobs.get(0).getAttributes(), new Rectangle2D.Double(20, 10, 35, 20));
		}
		// GraphConstants.setBackground(cells.get(0).getAttributes(),
		// Color.GRAY); // 背景颜色
		// GraphConstants.setOpaque(cells.get(0).getAttributes(), true); // 不透明
		// GraphConstants.setAbsolute(cells.get(0).getAttributes(), true); //位置?
		// GraphConstants.setVerticalAlignment(cells.get(0).getAttributes(), 0);
		// cells.get(0).add(new DefaultPort()); // 加入port
		// c1.add(cells.get(0));
		for (int i = 0; i < jobs.size(); i++) {
			Job jb = jobs.get(i);
			// Job jCell = new Job(jb.jobname, jobs.get(i).isValid,
			// jobs.get(i).jobtype);
			// cells.add(jCell);
			// GraphConstants.setBounds(jCell.getAttributes(), new
			// Rectangle2D.Double(jb.x * 3 - 200, jb.y / 2, jb.jobname.length()
			// * 7, 16)); // 开始坐标xy,宽高
			if (jb.jobname.equals("end"))
				endCell = jb;
			GraphConstants.setBounds(jb.getAttributes(), new Rectangle2D.Double(jb.x, jb.y, jb.jobname.length() * 7, 16));
			GraphConstants.setAutoSize(jb.getAttributes(), true); // 自动大小
			GraphConstants.setFont(jb.getAttributes(), new Font("Default Sans Serif", 0, 10)); // 字体
			this.jobs.add(jb);
			jb.add(new DefaultPort());
			jb.jobstatus = jb.jobstatus;
			GraphConstants.setOpaque(jb.getAttributes(), true); // 不透明
			// 设置颜色-jobstatus: 1000-排队(gray) 100-ready(white) 0-开始(running/blue)
			// 1-成功(green) 2-警告(yellow) 3-失败(red) 4-失败跳过(orange)
			switch (jb.jobstatus) {
			case 1000:
				GraphConstants.setBackground(jb.getAttributes(), Color.WHITE);
				break; // not ready
			case 100:
				GraphConstants.setBackground(jb.getAttributes(), new Color(232, 232, 232));
				break; // ready - waiting
			case 0:
				GraphConstants.setBackground(jb.getAttributes(), new Color(152, 245, 255));
				break;// running
			case 1:
				GraphConstants.setBackground(jb.getAttributes(), Color.GREEN);
				break;
			case 2:
				GraphConstants.setBackground(jb.getAttributes(), Color.YELLOW);
				break;
			case 3:
				GraphConstants.setBackground(jb.getAttributes(), Color.RED);
				break;
			case 4:
				GraphConstants.setBackground(jb.getAttributes(), Color.PINK);
			}
		}
		return jobs;
	}

	// 初始化调度状态2-依赖关系
	public List<JobEdge> getScheduleJobRelationsList(String scheduleType, ArrayList<Job> c1) {
		List<JobEdge> jobEdges = DBUnit.initScheduleJobRelationList(scheduleType);
		this.jobRelations.addAll(jobEdges); // 全部边放进c2
		if (jobEdges.size() > 0)
			for (int i = 0; i < jobEdges.size(); i++) {
				JobEdge edge1 = jobEdges.get(i);
				for (int j = 0; j < c1.size(); j++) {
					int f = 0;
					Job jCell = c1.get(j);
					// 搜索边的源
					if (edge1.headJobName.equals(jCell.toString())) {
						edge1.setSource(jCell.getChildAt(0));
						f++;
					}
					// 搜索边的目标
					if (edge1.tailJobName.equals(jCell.toString())) {
						edge1.setTarget(jCell.getChildAt(0));
						f++;
					}
					if (f == 2)
						break;
				}
				// 为edge设置一个箭头
				GraphConstants.setLineEnd(edge1.getAttributes(), GraphConstants.ARROW_CLASSIC);
				// GraphConstants.setEndFill(edge1.getAttributes(), true); //
				// 填充箭头
			}
		return jobEdges;

	}

	//选择全部
	public void selectAll() {
		// 得到所有cells
		Object[] allCells = DefaultGraphModel.getAll(this.getModel());
		this.setSelectionCells(allCells);
		this.requestFocus();
	}
	
	// 选择跟节点
	public void selectRoots(Job job) {
		// 得到所有cells
		//Object[] allCells = DefaultGraphModel.getAll(this.getModel());
		//Object[] roots = DefaultGraphModel.getRoots(this.getModel());
		Job[] jobs = {job};
		 Set<JobEdge> jobRelations = DefaultGraphModel.getEdges(model, jobs);
		 ArrayList<Object> roots = new ArrayList();
		 Iterator<JobEdge> it = jobRelations.iterator();
		 String targetJobname = job.getJobname();
		 while(it.hasNext()){
			 Object object = it.next();
			 //logger.info(object.getClass().getName());
			 JobEdge jobRelation = (JobEdge) object;
			 if(jobRelation.tailJobName.equals(targetJobname)){
				 Object sourceJob = DefaultGraphModel.getSourceVertex(model,jobRelation);
				 //logger.info("sourceJob:"+sourceJob.toString() + "," + sourceJob.getClass().getName());
				 roots.add(jobRelation);
				 roots.add((Job)sourceJob);
			 }else{
				 continue;
			 }
		 }
		//this.getSelectionCells(objs);
		//roots.add((JobEdge)sourceJob);
		this.setSelectionCells(roots.toArray());
		this.requestFocus();
	}
	
	// 选择子节点
	public void selectChildren(Job job) {
		Job[] jobs = {job};
		 Set<JobEdge> jobRelations = DefaultGraphModel.getEdges(model, jobs);
		 ArrayList<Object> children = new ArrayList();
		 Iterator<JobEdge> it = jobRelations.iterator();
		 String sourceJobname = job.getJobname();
		 while(it.hasNext()){
			 Object object = it.next();
			 //logger.info(object.getClass().getName());
			 JobEdge jobRelation = (JobEdge) object;
			 if(jobRelation.headJobName.equals(sourceJobname)){
				 Object targetJob = DefaultGraphModel.getTargetVertex(model,jobRelation);
				 //logger.info("sourceJob:"+targetJob.toString() + "," + targetJob.getClass().getName());
				 children.add(jobRelation);
				 children.add((Job)targetJob);
			 }else{
				 continue;
			 }
		 }
		//this.getSelectionCells(objs);
		//roots.add((JobEdge)sourceJob);
		this.setSelectionCells(children.toArray());
		this.requestFocus();
		
	}

	// 查找
	public boolean finder(String lookUp) {
		// String lookUp = txtJobname.getText() + "," + txtFilename.getText();
		// List<String> fs = Arrays.asList(lookUp.split(","));
		int i = lookUp.indexOf(",");
		lookUp = i <= 0 ? lookUp : lookUp.substring(0, i);
		// 得到所有cells
		Object[] allCells = DefaultGraphModel.getAll(this.getModel());
		ArrayList<Object> selectionCells = new ArrayList<Object>();
		int fed = 0;// 寻找次数,找到最后一个时为1
		for (fi = fi >= allCells.length ? 0 : fi; fed == 0 || fi < allCells.length; fi++) {
			if (allCells.length == 0){
				break;
			}
			if (fed == 0 && fi + 1 == allCells.length) {
				fi = 0;
				fed++;
			}
//			logger.info("fi=" + fi);
//			logger.info("allCells.length=" + allCells.length);
			if (allCells[fi].getClass() == Job.class && this.getCellBounds(allCells[fi]) != null
					&& allCells[fi].toString().toLowerCase().indexOf(lookUp.toLowerCase()) >= 0) {
				selectionCells.add(allCells[fi]);
				// logger.info("x="+((Job)allCells[j]).x+"  y="+((Job)allCells[j]).y);
				// logger.info("x,y:"+this.getCellBounds(allCells[j]).getX()+","+this.getCellBounds(allCells[j]).getY());
				this.focusX = (int) this.getCellBounds(allCells[fi]).getX();
				this.focusY = (int) this.getCellBounds(allCells[fi]).getY();
				break;
			}
		}
		// logger.info("fi=" + fi + " allCells.length=" +
		// allCells.length);
		if (fi >= allCells.length)
			return false; // 没有找到,返回false
		else {
			// logger.info("11");
			this.setSelectionCells(selectionCells.toArray());
			// this.refresh();
			// this.getGraphLayoutCache().reload();
			this.requestFocus();
			// this.jspane.setSize(1600, 1200); //jspane跟随滑动
			// this.jspane.getHorizontalScrollBar().setValues(800, 600, 1600,
			// 1200);
			// this.setFocusTraversalKeysEnabled(true);
			fi++;
			return true; // 找到则返回true
		}
	}

	// 保存编排好的作业
	public void saveAllJobs(String scheduleType) {
		logger.info("save all jobs");
		Object[] allCells = DefaultGraphModel.getAll(getModel());
		ArrayList<Job> jobs = new ArrayList<Job>();
		ArrayList<JobEdge> jobEdges = new ArrayList<JobEdge>();
		for (int i = 0; i < allCells.length; i++) {
			// jobs
			if (getCellBounds(allCells[i]) == null) {
				continue;
			} else if (allCells[i].getClass() == Job.class) {
				Job job = (Job) allCells[i];
				job.jobname = job.toString();
				// job.isSchedule = allCells[i]).is
				/*
				 * job.x = (getCellBounds(allCells[i]).getX()+200)/3; job.y =
				 * getCellBounds(allCells[i]).getY()*2;
				 */
				job.on_fail_action = job.on_fail_action != null && job.on_fail_action.equals("skip:") ? "skip" : "stop";
				job.x = getCellBounds(allCells[i]).getX();
				job.y = getCellBounds(allCells[i]).getY();
				logger.debug(allCells[i].getClass().toString() + ": " + job.jobname + " : " + allCells[i] + " x:" + getCellBounds(allCells[i]).getX()
						+ " y:" + getCellBounds(allCells[i]).getY());
				jobs.add(job);
			}// edges
			else if (allCells[i].getClass() == JobEdge.class) {
				JobEdge jobEdge = ((JobEdge) allCells[i]);
				// jobEdge.
				// Job headJob = (Job)(model.getSource(jobEdge));
				Job headJob = (Job) getModel().getParent(getModel().getSource(jobEdge));
				Job tailJob = (Job) getModel().getParent(getModel().getTarget(jobEdge));
				jobEdge.headJobName = headJob.toString();
				// jobEdge.headJobType = headJob.jobtype;
				jobEdge.tailJobName = tailJob.toString();
				// jobEdge.tailJobType = tailJob.jobtype;
				logger.info(allCells[i].getClass().toString() + ": " + allCells[i] + jobEdge.headJobName + "->" + jobEdge.tailJobName);
				jobEdges.add(jobEdge);
			}
		}
		DBUnit.saveAllJob(scheduleType, jobs, jobEdges);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_T)
			logger.info("我是home");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			contorl = 1;
			if (this.getMouseWheelListeners().length == 0)
				this.addMouseWheelListener(this);
		}
		contorl = 1;
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			Object[] cells = this.getSelectionCells();// 记录选择的cell
			List<Object> listEdges = new ArrayList<Object>();
			if (e.isShiftDown()) {
				for (int c = 0; c < cells.length; c++) {
					int numChildren = this.getModel().getChildCount(cells[c]);
					for (int i = 0; i < numChildren; i++) {
						Object port = this.getModel().getChild(cells[c], i);
						if (this.getModel().isPort(port)) {
							Iterator<?> iter = this.getModel().edges(port);
							while (iter.hasNext()) {
								listEdges.add(iter.next());
							}
						}
					}
				}
			}
			// 从存储列表中删除
			this.jobs.removeAll(Arrays.asList(cells));
			this.files.removeAll(Arrays.asList(cells));
			this.boruntimes.removeAll(Arrays.asList(cells));
			listEdges.addAll(Arrays.asList(cells));
			this.jobRelations.removeAll(listEdges);
			this.filesEdgs.removeAll(listEdges);
			this.boruntimesEdgs.removeAll(listEdges);
			// 从graph中移除
			this.getModel().remove(this.getDescendants(listEdges.toArray()));
		}
		
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
			try {
				this.undoManager.redo();
			} catch (javax.swing.undo.CannotRedoException e1) {
				WriteLog.writeFile("DsignerGraph.undoManager:" + e1.getMessage());
			}
		}

		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
			try {
				this.undoManager.undo();
			} catch (javax.swing.undo.CannotUndoException e1) {
				WriteLog.writeFile("DsignerGraph.undoManager:" + e1.getMessage());
			}
			// 1.显示bo、TAB 2.刷新c1~cb2; 3.显示bo tab?
			/*
			 * Object[] allCells = DefaultGraphModel.getAll(this.getModel());
			 * this.setTabVisible(true); this.setBOVisible(true);
			 * this.c1.clear(); this.cF1.clear(); this.cBO1.clear();
			 * this.c2.clear(); this.cF2.clear(); this.cBO2.clear(); for(int
			 * i=0;i<allCells.length;i++){ //job
			 * if(allCells[0].getClass()==JobGraphCell.class ){ JobGraphCell
			 * jobCell= (JobGraphCell)allCells[0];
			 * if(jobCell.getJobtype().equals("bojob")){
			 * cBO1.add(jobCell);//bojob类 }else
			 * if(jobCell.getJobtype().equals("TAB")){ cF1.add(jobCell);//TAB
			 * }else{ c1.add(jobCell);//dsjob } } }
			 * if(allCells[0].getClass()==JobEdge.class){ JobEdge jobEdge =
			 * (JobEdge)allCells[0]; JobGraphCell jobCell =
			 * (JobGraphCell)getModel
			 * ().getParent(getModel().getTarget(jobEdge));
			 * if(jobCell.getJobtype().equals("bojob")){
			 * cBO2.add(jobEdge);//bojob类 }else
			 * if(jobCell.getJobtype().equals("TAB")){ cF2.add(jobEdge);//TAB
			 * }else{ c2.add(jobEdge);//dsjob } }
			 */
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			this.contorl = 0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		Object cell = this.getFirstCellForLocation(x, y);
		if (e.getButton() == MouseEvent.BUTTON3 && cell != null && cell.getClass() == Job.class) {
			String lab = this.convertValueToString(cell);
			PopupMenu cellMenu = new PopupMenu("lab"); // 右键菜单
			this.initCellMenu(cellMenu, lab, (Job) cell, this);
			this.add(cellMenu);
			cellMenu.show(this, x, y);
		} else if (e.getButton() == MouseEvent.BUTTON3 && cell == null) {
			PopupMenu scheduleMenu = new PopupMenu("schedule"); // 右键菜单
			this.initScheduleMenu(scheduleMenu, this);
			this.add(scheduleMenu);
			scheduleMenu.show(this, x, y);
		} else if (e.getClickCount() == 2 && cell != null && cell.getClass() == Job.class) {
			// slogger.info("jobtype="+((Job)cell).jobtype);
			this.doubleClickAction(cell, ((Job) cell).jobtype);
		}
	}

	// 响应鼠标双击,双击查看输入输入
	public void doubleClickAction(Object cell, String jobtype) {
		// logger.info("查看输入输出");
		DetailFrame detailFrame = new DetailFrame(this.convertValueToString(cell), jobtype);
		detailFrame.setLocationRelativeTo(backgroundComponent);// backgroundComponent
		detailFrame.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		detailFrame.setVisible(true);
		detailFrame.setAlwaysOnTop(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Get Cell under Mousepointer
		/*
		 * int x = e.getX(), y = e.getY(); if (e.getClickCount() == 2) { Object
		 * cell = this.getFirstCellForLocation(x, y); // Print Cell Label if
		 * (cell != null) { if (cell.getClass() == JobGraphCell.class &&
		 * this.convertValueToString(cell) != "start") {
		 * GraphConstants.setGradientColor(((JobGraphCell)
		 * cell).getAttributes(), Color.WHITE);// 设置颜色
		 * GraphConstants.setOpaque(((JobGraphCell) cell).getAttributes(),
		 * false);// 设置透明度 // graph.refresh(); //
		 * GraphConstants.setAutoSize(((DefaultGraphCell) //
		 * cell).getAttributes(), true); if (((JobGraphCell)
		 * cell).getJobtype().equals("TAB")) {
		 * GraphConstants.setGradientColor(((JobGraphCell)
		 * cell).getAttributes(), Color.ORANGE);// 设置颜色
		 * GraphConstants.setOpaque(((JobGraphCell) cell).getAttributes(),
		 * true);// 设置透明度 } if (((JobGraphCell)
		 * cell).getJobtype().equals("bojob")) {
		 * GraphConstants.setGradientColor(((JobGraphCell)
		 * cell).getAttributes(), Color.CYAN);// 设置颜色
		 * GraphConstants.setOpaque(((JobGraphCell) cell).getAttributes(),
		 * true);// 设置透明度 } this.getGraphLayoutCache().reload(); } } } else if
		 * (e.getClickCount() == 1) { Object cell =
		 * this.getFirstCellForLocation(x, y); if (cell!=null && cell.getClass()
		 * == JobEdge.class) {
		 * //logger.info(GraphConstants.getLineColor(jobEdgeSelect
		 * .getAttributes()));
		 * //logger.info(GraphConstants.getLineWidth(jobEdgeSelect
		 * .getAttributes()));
		 * GraphConstants.setLineWidth(jobEdgeSelect.getAttributes(), 1);
		 * GraphConstants.setLineColor(jobEdgeSelect.getAttributes(),
		 * Color.black); //GraphConstants.setLineWidth(map, 5); jobEdgeSelect =
		 * (JobEdge) cell;
		 * GraphConstants.setLineWidth(jobEdgeSelect.getAttributes(), 2);
		 * GraphConstants.setLineColor(jobEdgeSelect.getAttributes(),
		 * Color.red); this.getGraphLayoutCache().reload(); } else {
		 * GraphConstants.setLineWidth(jobEdgeSelect.getAttributes(), 1);
		 * GraphConstants.setLineColor(jobEdgeSelect.getAttributes(),
		 * Color.black); this.getGraphLayoutCache().reload(); } }
		 */
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isControlDown()) {// this.contorl == 1
			double d0 = this.updatePreferredSize(e.getWheelRotation(), e.getPoint());
			this.d -= d0;
			this.d = (this.d < 0.1) ? 0.1 : this.d;
			this.d = (this.d > 4) ? 4 : this.d;
			this.setScale(this.d, e.getPoint());
		} else {
			MouseWheelListener[] ls = this.getMouseWheelListeners();
			for (MouseWheelListener l : ls) {
				this.removeMouseWheelListener(l);
			}
		}
	}

	public Job getEndCell() {
		return endCell;
	}

	public void setEndCell(Job endCell) {
		this.endCell = endCell;
	}

	public double getFocusX() {
		return focusX;
	}

	public void setFocusX(double focusX) {
		this.focusX = focusX;
	}

	public double getFocusY() {
		return focusY;
	}

	public void setFocusY(double focusY) {
		this.focusY = focusY;
	}

}
