package com.jupiter.etl.jobinfo;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultGraphSelectionModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
/*import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.StandardBorderPainter;
import org.jvnet.substance.button.ClassicButtonShaper;
import org.jvnet.substance.painter.StandardGradientPainter;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;
import org.jvnet.substance.theme.SubstanceEbonyTheme;
import org.jvnet.substance.title.FlatTitlePainter;
import org.jvnet.substance.watermark.SubstanceBinaryWatermark;*/

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import com.jgraph.layout.organic.JGraphFastOrganicLayout;
import com.jgraph.layout.organic.JGraphOrganicLayout;
import com.jgraph.layout.tree.JGraphCompactTreeLayout;
import com.jgraph.layout.tree.JGraphRadialTreeLayout;
import com.jgraph.layout.tree.JGraphTreeLayout;
import com.jupiter.etl.jobinfo.entities.JobLocation;
import com.jupiter.etl.jobinfo.entities.JobRelation;
import com.jupiter.util.DBconnect;

public class JobioFrame extends JFrame{
//
//	private int x = 800;
//	private double d = 1.0;
//	private int contorl = 0;
//	
//	public int getX() {
//		return x;
//	}
//
//	public void setX(int x) {
//		this.x = x;
//	}
//
//	private int y = 500;	
//	
//	public int getY() {
//		return y;
//	}
//
//	public void setY(int y) {
//		this.y = y;
//	}
//
//	public JobioFrame(){
//		// 设置图形界面外观
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		// 装载可选择的主题  
//        /*try {  
//            //设置外观  
//            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());  
//            JFrame.setDefaultLookAndFeelDecorated(true);  
//            //设置主题   
//            SubstanceLookAndFeel.setCurrentTheme(new SubstanceEbonyTheme());  
//            //设置按钮外观  
//            SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());  
//            //设置水印  
//            SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBinaryWatermark());  
//            //设置边框  
//            SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());  
//            //设置渐变渲染  
//            SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());  
//            //设置标题  
//            SubstanceLookAndFeel.setCurrentTitlePainter(new FlatTitlePainter());  
//        } catch (Exception e) {  
//            System.out.println(e.getMessage());  
//        }*/
//	}
//	
//	private double updatePreferredSize(int wheelRotation, Point stablePoint) {
//		double scaleFactor = findScaleFactor(wheelRotation);
//		return scaleFactor;
//	}
//
//	private double findScaleFactor(int wheelRotation) {
//		double d = wheelRotation * 0.1;
//		//return (d > 0) ? 1/d : -d;
//		return d;
//	}
//	
//	static ArrayList<DefaultGraphCell> c1 = new ArrayList<DefaultGraphCell>();
//	static ArrayList<DefaultGraphCell> c2 = new ArrayList<DefaultGraphCell>();
//	
//	public ArrayList<DefaultGraphCell> initCellsList() {
//		// 定义一个存放cell的集合类
//		ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
//		// 建立你的第一个vertex对象
//		cells.add(new DefaultGraphCell(new String("jb_dwn_chdrpf_to_chdrpf")));
//		GraphConstants.setBounds(
//				cells.get(0).getAttributes(),
//				new Rectangle2D.Double(20, 80, "jb_dwn_chdrpf_to_chdrpf".length()*7, 20)); // 开始坐标xy,宽高
//		// 设置颜色和透明属性
//		GraphConstants.setGradientColor(cells.get(0).getAttributes(),
//				Color.orange);
//		GraphConstants.setOpaque(cells.get(0).getAttributes(), true);
//		// 为这个vertex加入一个port
//		DefaultPort port0 = new DefaultPort();
//		cells.get(0).add(port0);
//		// 同理加入第二个vertex
//		cells.add(new DefaultGraphCell(new String(
//				"jb_hq_commission_file_month_temp2")));
//		GraphConstants
//				.setBounds(
//						cells.get(1).getAttributes(),
//						new Rectangle2D.Double(
//								220,
//								20,
//								"jb_hq_commission_file_month_temp2".length() < 23 ? 160
//										: "jb_hq_commission_file_month_temp2"
//												.length() * 7, 20));
//		GraphConstants
//				.setGradientColor(cells.get(1).getAttributes(), Color.red);
//		GraphConstants.setOpaque(cells.get(1).getAttributes(), true);
//		DefaultPort port1 = new DefaultPort();
//		cells.get(1).add(port1);
//		// 同理加入第三个vertex
//		cells.add(new DefaultGraphCell(new String(
//				"jb_hq_policy_file_temp3_first")));
//		GraphConstants.setBounds(cells.get(2).getAttributes(),
//				new Rectangle2D.Double(220, 140,
//						"jb_hq_policy_file_temp3_first".length() < 23 ? 160
//								: "jb_hq_policy_file_temp3_first".length() * 7,
//						20));
//		GraphConstants.setGradientColor(cells.get(2).getAttributes(),
//				Color.blue);
//		GraphConstants.setOpaque(cells.get(2).getAttributes(), true);
//		DefaultPort port2 = new DefaultPort();
//		cells.get(2).add(port2);
//
//		// 加入一条边，将jb_dwn_chdrpf_to_chdrpf和jb_hq_commission_file_month_temp2的两个port连接起来
//		DefaultEdge edge = new DefaultEdge();
//		edge.setSource(cells.get(0).getChildAt(0));
//		edge.setTarget(cells.get(1).getChildAt(0));
//		// 为edge设置一个箭头
//		int arrow = GraphConstants.ARROW_CLASSIC;
//		GraphConstants.setLineEnd(edge.getAttributes(), arrow);
//		GraphConstants.setEndFill(edge.getAttributes(), true); // 填充箭头
//		// 将edge加入cell集合类
//		cells.add(edge);
//		// 同理
//		DefaultEdge edge1 = new DefaultEdge();
//		edge1.setSource(cells.get(0).getChildAt(0));
//		edge1.setTarget(cells.get(2).getChildAt(0));
//		GraphConstants.setLineEnd(edge1.getAttributes(), arrow); // 为edge1设置一个箭头
//		// 将edge加入cell集合类
//		cells.add(edge1);
//		// 同理
//		DefaultEdge edge2 = new DefaultEdge();
//		edge2.setSource(cells.get(2).getChildAt(0));
//		edge2.setTarget(cells.get(1).getChildAt(0));
//		GraphConstants.setLineEnd(edge2.getAttributes(), arrow); // 为edge1设置一个箭头
//		// 将edge加入cell集合类
//		cells.add(edge2);
//		
//		this.setX(1200);
//		this.setY(600);
//		return cells;
//	}
//	
//	public ArrayList<DefaultGraphCell> getJobLocationsList(int stype,String jobname,String filename) {
//		ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
//		DBconnect dbconn = new DBconnect();
//		List<JobLocation> jobLocation= dbconn.getJobLocationList(stype,jobname,filename);
//		if(jobLocation.size()>0){
//			cells.add(new DefaultGraphCell("start"));
//			GraphConstants.setBounds(cells.get(0).getAttributes(),new Rectangle2D.Double(20, jobLocation.get(0).y/2, 35, 20));
//			//GraphConstants.setLineStyle(cells.get(0).getAttributes(),1);
//		}
//		else{
//			cells.add(new DefaultGraphCell("null"));
//			GraphConstants.setBounds(cells.get(0).getAttributes(),new Rectangle2D.Double(20, 10, 35, 20));;
//		}
//		GraphConstants.setBackground(cells.get(0).getAttributes(),Color.GRAY);
//		GraphConstants.setOpaque(cells.get(0).getAttributes(), true);
//		GraphConstants.setAbsolute(cells.get(0).getAttributes(), true);
//		GraphConstants.setVerticalAlignment(cells.get(0).getAttributes(), 0);
//		cells.get(0).add(new DefaultPort());
//		for(int i=1,j=1; i<=jobLocation.size(); i++){
//			JobLocation jb = jobLocation.get(i-1);
//			cells.add(new DefaultGraphCell(jb.jobname));
//			GraphConstants.setBounds(
//					cells.get(j).getAttributes(),
//					new Rectangle2D.Double(jb.x*3-200, jb.y/2, jb.jobname.length()*7, 16)); // 开始坐标xy,宽高
//			//GraphConstants.setFont(cells.get(j).getAttributes(),new Font("宋体",1,9));
//			if(jb.jobname.equalsIgnoreCase(filename)){//设置血缘分析终点颜色
//				GraphConstants.setBackground(cells.get(j).getAttributes(),Color.ORANGE);
//				GraphConstants.setOpaque(cells.get(j).getAttributes(), true);
//			}
//			DefaultPort port0 = new DefaultPort();
//			cells.get(j).add(port0);
//			//GraphConstants.setOpaque(cells.get(j).getAttributes(), true);
//			j++;
//			}
//		return cells;
//	}
//	
//	public ArrayList<DefaultGraphCell> getJobRelationsList(ArrayList<DefaultGraphCell> c1) {
//		ArrayList<DefaultGraphCell> c2 = new ArrayList<DefaultGraphCell>();
//		DBconnect dbconn = new DBconnect();
//		List<JobRelation> jobInfoList = dbconn.getJobRelationList("", "");
//		if(jobInfoList.size()>0)
//		for (int i = 0; i < jobInfoList.size(); i++) {
//			DefaultEdge edge1 = new DefaultEdge();
//			for (int j = 0; j < c1.size(); j++) {
//				int f = 0;
//				// 搜索边的目标
//				if (jobInfoList.get(i).behind.equals(c1.get(j).toString()))
//					{edge1.setTarget(c1.get(j).getChildAt(0)); f++;
//					}//c1.get(j).getChildAt(0)
//				// 搜索边的源
//				if (jobInfoList.get(i).previous.equals(c1.get(j).toString()))
//					{edge1.setSource(c1.get(j).getChildAt(0)); f++;}
//				if (f==2) break;
//			}
//			// 为edge设置一个箭头
//			GraphConstants.setLineEnd(edge1.getAttributes(),GraphConstants.ARROW_CLASSIC);
////			GraphConstants.setEndFill(edge1.getAttributes(), true); // 填充箭头
//			c2.add(edge1);
//		}
//
//		return c2;
//	}
//	
//	public ArrayList<DefaultGraphCell> getCellsList(String jobname,String filename) {
//		ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
//		
//		DBconnect dbconn = new DBconnect();
//		List<JobRelation> jobInfoList= dbconn.getJobRelationList(jobname,filename);
//	
//		cells.add(new DefaultGraphCell("start"));
//		GraphConstants.setBounds(cells.get(0).getAttributes(),new Rectangle2D.Double(20, jobInfoList.get(0).y/2, 35, 20)); 
//		GraphConstants.setGradientColor(cells.get(0).getAttributes(),Color.orange);//设置颜色
//		GraphConstants.setOpaque(cells.get(0).getAttributes(), true);//设置透明度
//		cells.get(0).add(new DefaultPort());
//		
//		for(int i=1,j=1; i<=jobInfoList.size(); i++){
//		JobRelation jb = jobInfoList.get(i-1);
//		if(jb.rn != 1) continue;//非顶点退出
//		// 建立你的第一个vertex对象
//		cells.add(new DefaultGraphCell(jb.behind));
//		GraphConstants.setBounds(
//				cells.get(j).getAttributes(),
//				new Rectangle2D.Double(jb.x+jb.pathlength*200, jb.y/2, jb.behind.length()*6.5, 20)); // 开始坐标xy,宽高
//		// 为这个vertex加入一个port
//		DefaultPort port0 = new DefaultPort();
//		cells.get(j).add(port0);
//		j++;
//		}
//		
//		for(int i=1; i<=jobInfoList.size(); i++){
//			DefaultEdge edge1 = new DefaultEdge();
//			//搜索边的目标
//			for(int j=1; j<cells.size(); j++){
//				if(jobInfoList.get(i-1).behind.equals(cells.get(j).toString()))
//					edge1.setTarget(cells.get(j).getChildAt(0)); 
//				if(jobInfoList.get(i-1).pathlength==1)
//					edge1.setSource(cells.get(0).getChildAt(0));
//			}
//			//搜索边的源
//			for(int j=1; j<cells.size(); j++){
//				if(jobInfoList.get(i-1).previous.equals(cells.get(j).toString()))
//					edge1.setSource(cells.get(j).getChildAt(0)); 
//			}
//			// 为edge设置一个箭头 
//			  GraphConstants.setLineEnd(edge1.getAttributes(), GraphConstants.ARROW_CLASSIC);
//			  //GraphConstants.setLineBegin(edge1.getAttributes(), GraphConstants.ARROW_CLASSIC);
//			  GraphConstants.setEndFill(edge1.getAttributes(), true); // 填充箭头 
//			cells.add(edge1);
//		}
//	
//		return cells;
//	}
//
//	public void init(final JobioFrame jobioFrame){
//
//		// model用于控制整个模型显示属性等，view用于控制你的图形显示属性，这里都用默认即可
//		final GraphModel model = new DefaultGraphModel();
//		GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
//		// JGraph对象
//		final JGraph graph = new JGraph(model, view);
//		// 将以上定义的cells对象加入graph对象
//		/*if (c1.isEmpty()){
//		}else{
//			Iterator it = c1.iterator();
//			while (it.hasNext()) {
//				graph.getGraphLayoutCache().insert(it.next());
//			}
//		}*/
//		
//		// 一些graph对象的简单调整
//		// graph.setMoveable(false);//可否移动整个图形
//		graph.setDisconnectable(false);//不能移动边的指向,但是可以移动图形
//		graph.setSizeable(false);
//		// graph.setDisconnectOnMove(false);//可否移动整个边,但是在边的源点终点改变后失效
//		/*
//		 * 显示网格 { graph.setGridEnabled(true); graph.setGridVisible(true); }
//		 * graph.setGridMode(JGraph.CROSS_GRID_MODE);
//		 */
//		// graph.setMoveBelowZero(true); //是否允许cell越出左上角.通常设置为false,除非有特殊用处
//		graph.setAntiAliased(true);// 圆滑图像线条
//		// graph.setSelectionEnabled(false);//能否选择单个cell
//		graph.setCloneable(true);
//		graph.setBendable(true);
//		graph.setConnectable(true);
//		graph.setFont(new Font("宋体",1,12));
//		graph.getSelectionModel().setChildrenSelectable(true);
//		graph.setLockedHandleColor(Color.black);
//		graph.setMarqueeColor(Color.black);
//		graph.setHighlightColor(Color.black);
//		//graph.setJumpToDefaultPort(true);
//		/*
//		 * 加入改变边属性代码位置
//		 */
//		
//		///final JFrame jobioFrame = new JFrame();
//		Label labelJobname = new Label("jobname");
//		Label labelFileName = new Label("filename");
//		
//		Panel pnlPanel = new Panel();
//		pnlPanel.setPreferredSize(new Dimension(800,28));
//		final TextField txtJobname = new TextField(20);
//		final TextField txtFilename = new TextField(20);
//		txtJobname.setText("jb_");
//		
//		Button btnStart = new Button("开始分析");
//		Button btnFinder = new Button("查找");
//		Button btnLayout = new Button("重新排布");
//		Button btnHelp = new Button("帮助");
//		
//		JTable table = null;  
//		//初始化下拉列表框  
//        final JComboBox box = new JComboBox();  
//        box.addItem("快速布局");  
//        box.addItem("有机布局");
//        box.addItem("放射布局");  
//        box.addItem("简洁布局"); 
//        box.addItem("曲线布局"); 
//        
//        
//		pnlPanel.add(labelJobname);
//		pnlPanel.add(txtJobname);
//		pnlPanel.add(labelFileName);
//		pnlPanel.add(txtFilename);
//		pnlPanel.add(btnStart);
//		pnlPanel.add(btnFinder);		
//		pnlPanel.add(box);
//		pnlPanel.add(btnLayout);
//		pnlPanel.add(btnHelp);
//		
//		//开始分析
//		btnStart.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e) {
//				/*ArrayList<DefaultGraphCell> cells1 = jobioFrame.getCellsList(txtJobname.getText(),txtFilename.getText());
//				//c1 = jobioFrame.getCellsList(txtJobname.getText(),txtFilename.getText());
//				graph.getGraphLayoutCache().insert(cells1.toArray());*/
//				
//				
//				ArrayList<DefaultGraphCell> c1; //= jobioFrame.getCellsList(txtJobname.getText(),txtFilename.getText());
//				ArrayList<DefaultGraphCell> c2; //= jobioFrame.getCellsList(txtJobname.getText(),txtFilename.getText());
//				//graph.getModel().remove(c1.toArray());
//				//graph.getModel().remove(c2.toArray());
//				c1 = jobioFrame.getJobLocationsList(1,txtJobname.getText(),txtFilename.getText());
//				c2 = jobioFrame.getJobRelationsList(c1);
//				JobioFrame.c1.addAll(c1);
//				JobioFrame.c2.addAll(c2);
//				/*if (c1.isEmpty()){
//				}else{
//					Iterator it = c1.iterator();
//					while (it.hasNext()) {
//						graph.getGraphLayoutCache().insert(it.next());
//					}
//				}*/
//				graph.getGraphLayoutCache().insert(c1.toArray());
//				graph.getGraphLayoutCache().insert(c2.toArray());
//				graph.getGraphLayoutCache().toFront(c1.toArray());
//				
//				
//				//JGraphFacade facade = new JGraphFacade(graph); 
//				//JGraphLayout layout = new JGraphFastOrganicLayout(); 
//				//layout.run(facade);
//				//((JGraphLayout) graph.getLayout()).run(facade);
//			}
//		});
//		
//		// 查找
//		btnFinder.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String lookUp = txtJobname.getText();
//				for (int i = 0; i < c1.size(); i++) {
//					if (c1.get(i).toString().equalsIgnoreCase(lookUp)) {
//						GraphConstants.setGradientColor(c1.get(i).getAttributes(), Color.GREEN);// 设置颜色
//						GraphConstants.setOpaque(c1.get(i).getAttributes(),true);// 设置透明度
//						graph.refresh();
//						graph.getGraphLayoutCache().reload();
//						//graph.setSelectionCell(c1.get(i));
//					}
//				}
//			}
//		});
//		
//		//帮助
//		btnHelp.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(jobioFrame,"注：jobname留空则为血缘分析，否则为影响分析\n\n"
//						+"快捷键:\n"
//			+"复制：ctrl+鼠标拖曳\n"
//						+"缩放：ctrl+滚轮\n"
//			+"强选：alt+左键\n"
//						+"删除：del\n"
//			+"消色：双击\n"
//						+"若布局较乱，多尝试几次其他布局方式\n"
//						+"\n作者：︶ㄣ木木ヾ王子,2017-07","jbio......", 0);
//			}
//		});
//		
//		//重新布局
//		btnLayout.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				/*  test layout */
//				JGraphFacade facade = new JGraphFacade(graph); // Pass the facade the JGraph instance
//				JGraphLayout layout = null;
//				switch (box.getSelectedIndex()) {
//				//new JGraphRadialTreeLayout();// new JGraphCompactTreeLayout();//new JGraphFastOrganicLayout(); 
//				case 1:layout = new JGraphOrganicLayout(); break;   //有机
//				case 2: layout = new JGraphRadialTreeLayout(); break;   //放射布局
//				case 3: layout = new JGraphCompactTreeLayout(); break;  //简洁树形
//				case 4: layout = new JGraphHierarchicalLayout(); break; //曲线
//				default: layout = new JGraphFastOrganicLayout(); break; //快速
//				}
//				layout.run(facade); // Run the layout on the facade. Note that layouts do not implement the Runnable interface, to avoid confusion
//				Map nested = facade.createNestedMap(true, true); // Obtain a map of the resulting attribute changes from the facade
//				graph.getGraphLayoutCache().edit(nested); // Apply the results to the actual graph
//				/*  test layout */		
//			}
//		});
//		
//		/*//滚轮放大缩小
//		graph.addMouseWheelListener(new MouseWheelListener() {
//			public void mouseWheelMoved(MouseWheelEvent e) {
//				double d0 = jobioFrame.updatePreferredSize(e.getWheelRotation(), e.getPoint());
//				jobioFrame.d -= d0;
//				jobioFrame.d = (jobioFrame.d > 0.01) ? jobioFrame.d : jobioFrame.d+d0;
//				System.out.println("d1 = " + jobioFrame.d);
//				graph.setScale(jobioFrame.d);
//			}
//		});*/
//		//鼠标滚轮事件
//				graph.addMouseWheelListener(new MouseWheelListener() {
//					public void mouseWheelMoved(MouseWheelEvent e) {
//						if(jobioFrame.contorl==1){
//						double d0 = jobioFrame.updatePreferredSize(e.getWheelRotation(), e.getPoint());
//						jobioFrame.d -= d0;
//						jobioFrame.d = (jobioFrame.d < 0.1) ? 0.1 : jobioFrame.d;
//						jobioFrame.d = (jobioFrame.d > 4)   ? 4   : jobioFrame.d;
//						graph.setScale(jobioFrame.d,e.getPoint());
//						}
//					}
//				});
//		
//
//		 //MouseListener that Prints the Cell on Doubleclick
//		graph.addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent e) {
//				// System.out.println(graph.getSelectionCells().length)
//				// 右键双击删除
//				// Get Selected Cells
//				/*Object[] cells = graph.getSelectionCells();
//				if (e.isMetaDown() & e.getClickCount() == 2 & cells != null) {
//					// Remove Cells (incl. Descendants) from the Model
//					graph.getModel().remove(graph.getDescendants(cells));
//				}*/
//				if (e.getClickCount() == 2) {
//					// Get Cell under Mousepointer
//					int x = e.getX(), y = e.getY();
//					Object cell = graph.getFirstCellForLocation(x, y);
//					// Print Cell Label
//					if (cell != null ) {
//						if(cell.getClass() == DefaultGraphCell.class & graph.convertValueToString(cell) != "start"){
//						//String lab = graph.convertValueToString(cell);
//						//System.out.println("lab = " + lab);
//						//Object clone = DefaultGraphModel.cloneCell(graph.getModel(), cell);
//						//graph.getGraphLayoutCache().insert(clone);
//						GraphConstants.setGradientColor(((DefaultGraphCell) cell).getAttributes(), Color.WHITE);// 设置颜色
//						//GraphConstants.setOpaque(((DefaultGraphCell)cell).getAttributes(),true);// 设置透明度
//						//graph.refresh();
//						graph.getGraphLayoutCache().reload();
//						}
//					}
//				}
//			}
//		});
//		
//		jobioFrame.add(pnlPanel,BorderLayout.NORTH);
//		//frame.getContentPane().add(new JScrollPane(graph),BorderLayout.SOUTH);
//		JScrollPane jspane = new JScrollPane(graph);
//		jobioFrame.getContentPane().add(jspane);
//		jspane.setWheelScrollingEnabled(true);
//		
//		/*//键盘删除DELETE
//		KeyStroke stroke = KeyStroke.getKeyStroke("DELETE");
//		InputMap inputMap = jspane
//				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//		inputMap.put(stroke, "DELETE");
//		jspane.getActionMap().put("DELETE", new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("我是del");
//				Object[] cells = graph.getSelectionCells();
//				graph.getModel().remove(graph.getDescendants(cells));
//			}
//		});*/
//		//键盘事件
//				graph.addKeyListener(new KeyAdapter(){
//					public void keyPressed(KeyEvent e) {
//						Object[] cells = graph.getSelectionCells();
//						if(e.getKeyCode()==KeyEvent.VK_CONTROL)
//							jobioFrame.contorl = 1;
//						if(e.getKeyCode()==KeyEvent.VK_DELETE){
//							graph.getModel().remove(graph.getDescendants(cells));
//						}
//						if(e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_C)
//							;
//				    }
//					public void keyReleased(KeyEvent e) {  
//						if(e.getKeyCode()==KeyEvent.VK_CONTROL)
//							jobioFrame.contorl = 0;
//				    }
//					public void keyTyped(KeyEvent e) {
//						if(e.getKeyCode()==KeyEvent.VK_T)
//							System.out.println("我是home");
//				    }
//				});
//		
//		
//		jobioFrame.pack();
//		jobioFrame.setSize(jobioFrame.x, jobioFrame.y); //1200 600
//		int windowWidth = jobioFrame.getWidth(); // 获得窗口宽
//		int windowHeight = jobioFrame.getHeight(); // 获得窗口高
//		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
//		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
//		int screenWidth = screenSize.width; // 获取屏幕的宽
//		int screenHeight = screenSize.height; // 获取屏幕的高
//		jobioFrame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2
//				- windowHeight / 2); // 居中
//		jobioFrame.setVisible(true);
//		jobioFrame.setDefaultCloseOperation(3);
//		jobioFrame.setTitle("jbio");
//		jobioFrame.requestFocus();
//		
//		/*System.out.println("===" + graph.getModel().getRootCount());
//		//System.out.println("===" + graph.getModel().getRootAt(0));
//		System.out.println("==="
//				+ graph.getModel().getIndexOfRoot("jb_dwn_chdrpf_to_chdrpf"));
//		System.out.println("==="
//				+ graph.getModel().contains("jb_dwn_chdrpf_to_chdrpf"));
//		//System.out.println("===" + model.getChildCount(cells1.get(2)));
//		System.out.println("cells.size=" + c1.size());
//		// 得到指定边的源
//		System.out.println("===" + graph.getModel().getParent(graph.getModel().getSource(c2.get(4))));
//		// 单元克隆
//		Object clone = DefaultGraphModel.cloneCell(graph.getModel(), c1);*/
//	}
//	
//	public static void main(String[] args) {
//		final JobioFrame jobioFrame = new JobioFrame();
//		jobioFrame.init(jobioFrame);
//	}
		
}