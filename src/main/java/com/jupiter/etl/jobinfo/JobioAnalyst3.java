/*
 * Designer2_1.java
 *
 * Created on __DATE__, __TIME__
 */

package com.jupiter.etl.jobinfo;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.jgraph.graph.DefaultGraphCell;

import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.mybatis.po.User;

/**
 * @author  Jupiter 2018-01-31
 */
public class JobioAnalyst3 extends JobioBaseJFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(this.getClass());

	//DesignerGraph graph = new DesignerGraph(user,this);

	public JobioAnalyst3(User user) {
		this.user = user;
		this.graph = new DesignerGraph(user, this);
		// 设置图形界面外观
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		initComponents();
		jScrollPane1.getVerticalScrollBar().setUnitIncrement(20); //设置滚轮速度
		jScrollPane1.getHorizontalScrollBar().setUnitIncrement(20);

		graph.setEditable(false); //不可编辑 job

		buttonGroup1.add(jRadioButton1);
		buttonGroup1.add(jRadioButton2);
		buttonGroup1.add(jRadioButton3);
		buttonGroup1.add(jRadioButton4);

		choice1.add("血缘分析");
		choice1.add("影响分析");
		choice1.add("Both");

		//增加操作监控
		jRadioButton1.addActionListener(this);
		jRadioButton2.addActionListener(this);
		jRadioButton3.addActionListener(this);
		jRadioButton4.addActionListener(this);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		jCheckBoxShowOutDated.addActionListener(this);
		jCheckBox1.addActionListener(this);
		jCheckBox2.addActionListener(this);
		jComboBox1.addActionListener(this);
		/*for (int i = 0; i < jComboBox1.getItemCount(); i++) {
			(  jComboBox1.getItemAt(i)).
		}*/

		txtJobname.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (txtJobname.getText().equals("jobname/filename")) {
					txtJobname.requestFocus();
					// txtJobname.setSelectionEnd(1000); txtJobname.selectAll();
				}
			}

			// 鼠标移开时判断出输入的job、file和非法输入
			public void mouseExited(MouseEvent e) {
			}
		});
		txtJobname.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtJobname.getText().equals("jobname/filename")) {
					txtJobname.setText("");
					txtJobname.setFont(new Font("微软雅黑", 0, 12));
					txtJobname.setForeground(new java.awt.Color(0, 0, 0));
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if ("".equals(txtJobname.getText())) {
					txtJobname.setText("jobname/filename");
					txtJobname.setFont(new java.awt.Font("微软雅黑", 2, 12));
					txtJobname.setForeground(new java.awt.Color(204, 204, 255));
				} /*else if (!txtJobname.getText().equals("jobname/filename")) {
					String txt = txtJobname.getText().replace(" ", "");
					String[] ls = txt.split(",");
					String stype = jRadioButton1.isSelected() ? "bus" : "";
					strNotExists.clear();
					strNotExists = new ArrayList<String>(Arrays.asList(ls));
					strNotExists.removeAll(DBUnit.checkNameEixts(stype, txt));
					}*/
			}
		});

		txtJobname.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				txtJobname.setText(txtJobname.getText().replace(" ", ""));// 去空格
			}
		});

	}

	/** Creates new form Designer2_1 */
	public JobioAnalyst3() {
		this.user = user;
		this.graph = new DesignerGraph(user, this);
		// 设置图形界面外观
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		initComponents();
		jScrollPane1.getVerticalScrollBar().setUnitIncrement(20); //设置滚轮速度
		jScrollPane1.getHorizontalScrollBar().setUnitIncrement(20);

		graph.setEditable(false); //不可编辑 job

		buttonGroup1.add(jRadioButton1);
		buttonGroup1.add(jRadioButton2);
		buttonGroup1.add(jRadioButton3);
		buttonGroup1.add(jRadioButton4);

		choice1.add("血缘分析");
		choice1.add("影响分析");
		choice1.add("Both");

		//增加操作监控
		jRadioButton1.addActionListener(this);
		jRadioButton2.addActionListener(this);
		jRadioButton3.addActionListener(this);
		jRadioButton4.addActionListener(this);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		jCheckBoxShowOutDated.addActionListener(this);
		jCheckBox1.addActionListener(this);
		jCheckBox2.addActionListener(this);
		jComboBox1.addActionListener(this);
		/*for (int i = 0; i < jComboBox1.getItemCount(); i++) {
			(  jComboBox1.getItemAt(i)).
		}*/

		txtJobname.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (txtJobname.getText().equals("jobname/filename")) {
					txtJobname.requestFocus();
					// txtJobname.setSelectionEnd(1000); txtJobname.selectAll();
				}
			}

			// 鼠标移开时判断出输入的job、file和非法输入
			public void mouseExited(MouseEvent e) {
			}
		});
		txtJobname.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtJobname.getText().equals("jobname/filename")) {
					txtJobname.setText("");
					txtJobname.setFont(new Font("微软雅黑", 0, 12));
					txtJobname.setForeground(new java.awt.Color(0, 0, 0));
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if ("".equals(txtJobname.getText())) {
					txtJobname.setText("jobname/filename");
					txtJobname.setFont(new java.awt.Font("微软雅黑", 2, 12));
					txtJobname.setForeground(new java.awt.Color(204, 204, 255));
				} /*else if (!txtJobname.getText().equals("jobname/filename")) {
					String txt = txtJobname.getText().replace(" ", "");
					String[] ls = txt.split(",");
					String stype = jRadioButton1.isSelected() ? "bus" : "";
					strNotExists.clear();
					strNotExists = new ArrayList<String>(Arrays.asList(ls));
					strNotExists.removeAll(DBUnit.checkNameEixts(stype, txt));
					}*/
			}
		});

		txtJobname.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				txtJobname.setText(txtJobname.getText().replace(" ", ""));// 去空格
			}
		});

	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		jPanel1 = new javax.swing.JPanel();
		jRadioButton1 = new javax.swing.JRadioButton();
		jRadioButton2 = new javax.swing.JRadioButton();
		jCheckBox1 = new javax.swing.JCheckBox();
		jCheckBox2 = new javax.swing.JCheckBox();
		choice1 = new java.awt.Choice();
		jLabel1 = new javax.swing.JLabel();
		txtJobname = new javax.swing.JTextField();
		button1 = new java.awt.Button();
		button2 = new java.awt.Button();
		jComboBox1 = new javax.swing.JComboBox();
		button3 = new java.awt.Button();
		jCheckBoxShowOutDated = new javax.swing.JCheckBox();
		jRadioButton3 = new javax.swing.JRadioButton();
		jRadioButton4 = new javax.swing.JRadioButton();
		jScrollPane1 = new javax.swing.JScrollPane(graph);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Jobio Analyst V"+user.getVersion()+"    Welcome! "+user.getUsername());

		jRadioButton1.setSelected(true);
		jRadioButton1.setText("bus");
		jRadioButton1.setToolTipText("\u4e1a\u52a1\u4ed3\u5e93");
		jRadioButton1.setActionCommand("bus");
		jRadioButton1.addAncestorListener(new javax.swing.event.AncestorListener() {
			public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
			}

			public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
				jRadioButton1AncestorAdded(evt);
			}

			public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
			}
		});

		jRadioButton2.setText("fin");
		jRadioButton2.setToolTipText("\u8d22\u52a1\u6570\u636e\u4ed3\u5e93");
		jRadioButton2.setActionCommand("fin");
		jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButton2ActionPerformed(evt);
			}
		});

		jCheckBox1.setSelected(true);
		jCheckBox1.setText("table");
		jCheckBox1.setToolTipText("\u53d6\u6d88/\u663e\u793atable");

		jCheckBox2.setSelected(true);
		jCheckBox2.setText("boruntime");
		jCheckBox2.setToolTipText("\u663e\u793a/\u53d6\u6d88\u663e\u793abojob");

		jLabel1.setText("Job/Tab");

		txtJobname.setFont(new java.awt.Font("微软雅黑", 2, 12));
		txtJobname.setForeground(new java.awt.Color(204, 204, 255));
		txtJobname.setText("jobname/filename");
		txtJobname.setToolTipText("\u8f93\u5165job\u6216table,\u9017\u53f7\u5206\u9694");

		button1.setActionCommand("\u5206\u6790");
		button1.setLabel("\u5206\u6790");

		button2.setLabel("\u67e5\u627e");

		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "默认布局", "快速布局", "有机布局", "树形布局", "放射布局", "简洁布局", "曲线布局" }));
		jComboBox1.setToolTipText("\u91cd\u65b0\u5e03\u5c40");

		button3.setLabel("\u5e2e\u52a9");

		jCheckBoxShowOutDated.setSelected(true);
		jCheckBoxShowOutDated.setText("outDated");
		jCheckBoxShowOutDated.setToolTipText("\u53d6\u6d88/\u663e\u793atable");

		buttonGroup1.add(jRadioButton3);
		jRadioButton3.setText("adp");
		jRadioButton3.setToolTipText("adpp");
		jRadioButton3.setActionCommand("adpp");
		jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButton3ActionPerformed(evt);
			}
		});

		buttonGroup1.add(jRadioButton4);
		jRadioButton4.setText("zbx");
		jRadioButton4.setToolTipText("zbx");
		jRadioButton4.setActionCommand("zbx");
		jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButton4ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout
						.createSequentialGroup()
						.addComponent(jRadioButton1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jRadioButton2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jRadioButton3)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jRadioButton4)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jCheckBoxShowOutDated)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jCheckBox1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jCheckBox2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(choice1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(txtJobname, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(2, 2, 2)
						.addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(1, 1, 1)
						.addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(2, 2, 2)
						.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addGap(1, 1, 1)
						.addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(79, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton1).addComponent(jRadioButton2)
								.addComponent(jCheckBoxShowOutDated).addComponent(jCheckBox1).addComponent(jRadioButton3).addComponent(jRadioButton4))
				.addComponent(jCheckBox2)
				.addGroup(
						jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1)
								.addComponent(txtJobname, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(choice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

		txtJobname.getAccessibleContext().setAccessibleDescription("");
		button1.getAccessibleContext().setAccessibleDescription(
				"\u5b8c\u5168\u5339\u914d(snd.schema.tabname)\u3002\u4ec5\u5339\u914d\u8868\u540d\uff1a\u53f3\u952e\u9009\u62e9-Analyze Forward\u3002");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 979, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)));

		getAccessibleContext().setAccessibleName("Jobio V3.0");

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void jRadioButton1AncestorAdded(javax.swing.event.AncestorEvent evt) {
		// TODO add your handling code here:
	}

	private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		JobioAnalyst3 ds = new JobioAnalyst3();
		int windowWidth = ds.getWidth(); // 获得窗口宽
		int windowHeight = ds.getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		ds.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 3 - windowHeight / 3); // 居中
		ds.setVisible(true);
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private java.awt.Button button1;
	private java.awt.Button button2;
	private java.awt.Button button3;
	private javax.swing.ButtonGroup buttonGroup1;
	private java.awt.Choice choice1;
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JCheckBox jCheckBox2;
	private javax.swing.JCheckBox jCheckBoxShowOutDated;
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JRadioButton jRadioButton1;
	private javax.swing.JRadioButton jRadioButton2;
	private javax.swing.JRadioButton jRadioButton3;
	private javax.swing.JRadioButton jRadioButton4;
	//private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField txtJobname;
	// End of variables declaration//GEN-END:variables
	int jbtype_in = 1;
	int analyze_in = 0;
	ArrayList<String> jobnameList = new ArrayList<String>(); //记录输入的jobname
	ArrayList<String> filenameList = new ArrayList<String>(); //记录输入的filename

	//处理按钮操作
	@Override
	public void actionPerformed(ActionEvent e) {
		String arg = e.getActionCommand();
		logger.info("操作： "+arg);
		if (arg.equals("bus")) {
			jbtype_in = 1;
		} else if (arg.equals("fin")) {
			jbtype_in = 2;
		} else if (arg.equals("adpp")) {
			jbtype_in = 3;
		} else if (arg.equals("zbx")) {
			jbtype_in = 4;
		} else if (arg.equals("table")) {
			graph.setTabVisible(jCheckBox1.isSelected());
			graph.refresh();
		} else if (arg.equals("boruntime")) {
			graph.setBOVisible(jCheckBox2.isSelected());
			graph.refresh();
		} else if (arg.equals("outDated")) {
			graph.setOutDatedVisible(jCheckBoxShowOutDated.isSelected());
			graph.refresh();
			//graph.getGraphLayoutCache().reload();
		} else if (arg.equals("分析")) {
			startAnalyze();
			//graph.finder(txtJobname.getText());
			//gotoFocus();
		} else if (arg.equals("查找") && graph.finder(txtJobname.getText())) {
			// logger.debug("w="+jScrollPane1.getSize().width);
			// logger.debug("h="+jScrollPane1.getSize().height);
			gotoFocus(this.jScrollPane1, this.graph);
			// logger.debug("find true");
			// jScrollPane1.getHorizontalScrollBar().setValue(jScrollPane1.getSize().width/2);
			// jScrollPane1.setSize(1600, 1200);
			// jScrollPane1.getVerticalScrollBarPolicy();
			// jScrollPane1.getHorizontalScrollBar().setValues(800, 600, 1600,
			// 1200);
		} else if (arg.equals("帮助")) {
			showHelper();
		} else if (arg.equals("comboBoxChanged") && jComboBox1.getSelectedIndex() == 0) {
			defaultLayout();//默认布局
		} else if (arg.equals("comboBoxChanged") && jComboBox1.getSelectedIndex() == 1) {
			rLayout(1);//快速布局
		} else if (arg.equals("comboBoxChanged") && jComboBox1.getSelectedIndex() == 2) {
			rLayout(2);//有机布局
		} else if (arg.equals("comboBoxChanged") && jComboBox1.getSelectedIndex() == 3) {
			rLayout(3);//树形布局
		} else if (arg.equals("comboBoxChanged") && jComboBox1.getSelectedIndex() == 4) {
			rLayout(4);//放射布局
		} else if (arg.equals("comboBoxChanged") && jComboBox1.getSelectedIndex() == 5) {
			rLayout(5);//简洁布局
		} else if (arg.equals("comboBoxChanged") && jComboBox1.getSelectedIndex() == 6) {
			rLayout(6);//曲线布局
		} else if (arg.equals("调度")) {
			//startSchedule();
			showMonitor();
		} else if (arg.equals("刷新")) {
			//graph.refreshScheduleJobLocationList(sch.getBatchno());
		}
		/*if (arg.equals("重新排布")) {
		JGraphFacade facade = new JGraphFacade(graph); // Pass the facade the JGraph instance
		JGraphLayout layout = null;
		switch (box.getSelectedIndex()) {
		//new JGraphRadialTreeLayout();// new JGraphCompactTreeLayout();//new JGraphFastOrganicLayout(); 
		case 1:layout = new JGraphOrganicLayout(); break;   //有机
		case 2: layout = new JGraphRadialTreeLayout(); break;   //放射布局
		case 3: layout = new JGraphCompactTreeLayout(); break;  //简洁树形
		case 4: layout = new JGraphHierarchicalLayout(); break; //曲线
		default: layout = new JGraphFastOrganicLayout(); break; //快速
		}
		layout.run(facade); // Run the layout on the facade. Note that layouts do not implement the Runnable interface, to avoid confusion
		Map<?, ?> nested = facade.createNestedMap(true, true); // Obtain a map of the resulting attribute changes from the facade
		graph.getGraphLayoutCache().edit(nested); // Apply the results to the actual graph
		}*/
	}

	// 分析(血缘/影响)
	public void startAnalyze() {
		// graph.getModel().remove(c1.toArray());
		// graph.getModel().remove(c2.toArray());
		String txt = txtJobname.getText();
		String stype = buttonGroup1.getSelection().getActionCommand();
		//logger.debug("stype:" + stype);
		if (txt.equals("jobname/filename") || txt.equalsIgnoreCase("t")) {
			sampleAnalyze(this.graph);
			return;
		} else if (getNotExistsJobs(stype, txt).size() > 0) {
			JOptionPane.showMessageDialog(null, "以下job/file不存在，请检查:\n" + "" + strNotExists.toString().replaceAll(",", "\n") + "", "输入错误", 0);
			return;
		}
		user.setOptionTime(new Date());
		ArrayList<Job> c1 = graph.getJobLocationsList2_1(this.user, jbtype_in, choice1.getSelectedIndex(), txtJobname.getText());// ,
//		ArrayList<JobEdge> c2 = 
		graph.getJobRelationsList(this.user, c1);
		/*graph.c1.addAll(c1);
		graph.c2.addAll(c2);
		graph.getGraphLayoutCache().insert(c1.toArray());
		graph.getGraphLayoutCache().insert(c2.toArray());
		graph.getGraphLayoutCache().toFront(c1.toArray());*/
		ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
		cells.addAll(graph.jobs);
		cells.addAll(graph.jobRelations);
		graph.getGraphLayoutCache().insert(cells.toArray());
		graph.setTabVisible(jCheckBox1.isSelected());
		graph.setBOVisible(jCheckBox2.isSelected());
		graph.setOutDatedVisible(jCheckBoxShowOutDated.isSelected());
		graph.getGraphLayoutCache().toFront(graph.jobs.toArray()); //jobs放上层
		/*if (!jcbFile.isSelected()) {//是否显示TAB
			graph.getGraphLayoutCache().setVisible(graph.cF1.toArray(),false);
			//graph.getGraphLayoutCache().setVisible(graph.cF2.toArray(),false);
			//graph.getGraphLayoutCache().toFront(graph.cF1.toArray());
			//尝试用setVisible
			//graph.getModel().remove(graph.getDescendants(graph.cF1.toArray()));
			//graph.getModel().remove(graph.cF2.toArray());
		}
		if (!jcbBO.isSelected()) {//是否显示boruntime
			//graph.getGraphLayoutCache().insert(graph.cBO1.toArray());
			//graph.getGraphLayoutCache().insert(graph.cBO2.toArray());
			//graph.getGraphLayoutCache().toFront(graph.cBO1.toArray());
			graph.getGraphLayoutCache().setVisible(graph.cBO1.toArray(),false);
			//graph.getGraphLayoutCache().setVisible(graph.cBO2.toArray(),false);
		}*/
	}

}