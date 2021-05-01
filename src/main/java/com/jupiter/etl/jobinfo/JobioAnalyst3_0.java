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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import com.jgraph.layout.organic.JGraphFastOrganicLayout;
import com.jgraph.layout.organic.JGraphOrganicLayout;
import com.jgraph.layout.tree.JGraphCompactTreeLayout;
import com.jgraph.layout.tree.JGraphRadialTreeLayout;
import com.jgraph.layout.tree.JGraphTreeLayout;
import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.etl.jobinfo.entities.JobEdge;
import com.jupiter.etl.schedule.Monitor;
import com.jupiter.mybatis.dao.DBUnit;
import com.jupiter.mybatis.po.User;

/**
 *
 * @author  __USER__
 */
public class JobioAnalyst3_0 extends JobioBaseJFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	//DesignerGraph graph = new DesignerGraph(user,this);

	public JobioAnalyst3_0(User user) {
		this.user = user;
		this.graph = new DesignerGraph(user,this);
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
		choice1.add("血缘分析");
		choice1.add("影响分析");
		choice1.add("Both");

		//增加操作监控
		jRadioButton1.addActionListener(this);
		jRadioButton2.addActionListener(this);
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
				} else if (!txtJobname.getText().equals("jobname/filename")) {
					String txt = txtJobname.getText().replace(" ", "");
					String[] ls = txt.split(",");
					String stype = jRadioButton1.isSelected() ? "bus" : "fin";
					strNotExists.clear();
					strNotExists = new ArrayList<String>(Arrays.asList(ls));
					strNotExists.removeAll(DBUnit.checkNameEixts(stype, txt));

					/*
					//有输入时开始判断
					String txt = txtJobname.getText().replace(" ", "");
					String[] tmp = txt.split(",");
					txt = "'" + tmp[0] + "'";
					for (int i = 1; i < tmp.length; i++) {
						txt += ",'" + tmp[i] + "'";
					}
					//System.out.println("tmp = " + (new ArrayList<String>(Arrays.asList(tmp))).toString().replace(" ", "").replace("[", "").replace("]", ""));
					//System.out.println("txt = " + txt);
					jobnameList = DBUnit.checkJobnameEixts(txt.toLowerCase());
					filenameList = DBUnit.checkFilenameEixts(txt.toUpperCase());
					System.out.println("jobnameList:" + jobnameList.toString().replace(" ", "").replace("[", "").replace("]", ""));
					System.out.println("filenameList:" + filenameList.toString().replace(" ", "").replace("[", "").replace("]", ""));
					System.out.println("tmp.length = " + tmp.length + " , jobnameList.size() = " + jobnameList.size() + " , filenameList.size() = "
							+ filenameList.size());
					strNotExists.clear();
					if (tmp.length > jobnameList.size() + filenameList.size() && !jobnameList.containsAll(Arrays.asList(tmp))) {
						// ArrayList<String> strNotExists = new
						// ArrayList<String>();
						for (int i = 0; i < tmp.length; i++) {
							if (!jobnameList.contains(tmp[i]) && !filenameList.contains(tmp[i]))
								strNotExists.add(tmp[i]);
						}
					}*/
					//System.out.println("strNotExists:" + strNotExists.toString());
				}
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
	public JobioAnalyst3_0() {
		this.user = user;
		this.graph = new DesignerGraph(user,this);
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
		choice1.add("血缘分析");
		choice1.add("影响分析");
		choice1.add("Both");

		//增加操作监控
		jRadioButton1.addActionListener(this);
		jRadioButton2.addActionListener(this);
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
				} else if (!txtJobname.getText().equals("jobname/filename")) {
					String txt = txtJobname.getText().replace(" ", "");
					String[] ls = txt.split(",");
					String stype = jRadioButton1.isSelected() ? "bus" : "fin";
					strNotExists.clear();
					strNotExists = new ArrayList<String>(Arrays.asList(ls));
					strNotExists.removeAll(DBUnit.checkNameEixts(stype, txt));

					/*
					//有输入时开始判断
					String txt = txtJobname.getText().replace(" ", "");
					String[] tmp = txt.split(",");
					txt = "'" + tmp[0] + "'";
					for (int i = 1; i < tmp.length; i++) {
						txt += ",'" + tmp[i] + "'";
					}
					//System.out.println("tmp = " + (new ArrayList<String>(Arrays.asList(tmp))).toString().replace(" ", "").replace("[", "").replace("]", ""));
					//System.out.println("txt = " + txt);
					jobnameList = DBUnit.checkJobnameEixts(txt.toLowerCase());
					filenameList = DBUnit.checkFilenameEixts(txt.toUpperCase());
					System.out.println("jobnameList:" + jobnameList.toString().replace(" ", "").replace("[", "").replace("]", ""));
					System.out.println("filenameList:" + filenameList.toString().replace(" ", "").replace("[", "").replace("]", ""));
					System.out.println("tmp.length = " + tmp.length + " , jobnameList.size() = " + jobnameList.size() + " , filenameList.size() = "
							+ filenameList.size());
					strNotExists.clear();
					if (tmp.length > jobnameList.size() + filenameList.size() && !jobnameList.containsAll(Arrays.asList(tmp))) {
						// ArrayList<String> strNotExists = new
						// ArrayList<String>();
						for (int i = 0; i < tmp.length; i++) {
							if (!jobnameList.contains(tmp[i]) && !filenameList.contains(tmp[i]))
								strNotExists.add(tmp[i]);
						}
					}*/
					//System.out.println("strNotExists:" + strNotExists.toString());
				}
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
		jScrollPane1 = new javax.swing.JScrollPane(graph);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Jobio Analyst V3.0");

		jRadioButton1.setSelected(true);
		jRadioButton1.setText("bus");
		jRadioButton1.setToolTipText("\u4e1a\u52a1\u4ed3\u5e93");

		jRadioButton2.setText("fin");
		jRadioButton2.setToolTipText("\u8d22\u52a1\u6570\u636e\u4ed3\u5e93");

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

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout
						.createSequentialGroup()
						.addComponent(jRadioButton1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jRadioButton2)
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
						.addContainerGap(321, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton1).addComponent(jRadioButton2)
								.addComponent(jCheckBoxShowOutDated).addComponent(jCheckBox1))
				.addComponent(jCheckBox2)
				.addGroup(
						jPanel1Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel1)
								.addComponent(txtJobname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
				.addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(choice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

		txtJobname.getAccessibleContext().setAccessibleDescription("");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1127, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)));

		getAccessibleContext().setAccessibleName("Jobio V3.0");

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		JobioAnalyst3_0 ds = new JobioAnalyst3_0();
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
	//private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField txtJobname;
	// End of variables declaration//GEN-END:variables
	int jbtype_in = 1;
	int analyze_in = 0;
	ArrayList<String> jobnameList = new ArrayList<String>(); //记录输入的jobname
	ArrayList<String> filenameList = new ArrayList<String>(); //记录输入的filename
	List<String> strNotExists = new ArrayList<String>(); //记录输入错误(不存在的job/file)

	//处理按钮操作
	@Override
	public void actionPerformed(ActionEvent e) {
		String arg = e.getActionCommand();
		//System.out.print("arg="+arg);
		if (arg.equals("bus")) {
			jbtype_in = 1;
		} else if (arg.equals("fin")) {
			jbtype_in = 2;
			//System.out.print("jbtype_in="+jbtype_in);
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
			// System.out.println("w="+jScrollPane1.getSize().width);
			// System.out.println("h="+jScrollPane1.getSize().height);
			gotoFocus(this.jScrollPane1,this.graph);
			// System.out.println("find true");
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
		if (txtJobname.getText().equals("jobname/filename")||txtJobname.getText().equalsIgnoreCase("t")) {
			sampleAnalyze(this.graph);
			return;
		} else if (this.strNotExists.size() > 0) {
			JOptionPane.showMessageDialog(null, "以下job/file不存在，请检查:\n" + "" + strNotExists.toString().replaceAll(",", "\n") + "", "输入错误", 0);
			return;
		}
		user.setOptionTime(new Date());
		ArrayList<Job> c1 = graph.getJobLocationsList2_1(this.user, jbtype_in, choice1.getSelectedIndex(), txtJobname.getText());// ,
		ArrayList<JobEdge> c2 = graph.getJobRelationsList(this.user, c1);
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