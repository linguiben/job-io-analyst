/*
 * Monitor.java
 *
 * Created on __DATE__, __TIME__
 */

package com.jupiter.etl.schedule;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.GraphConstants;

import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.etl.jobinfo.entities.JobEdge;
import com.jupiter.mybatis.dao.DBUnit;
import com.jupiter.util.DBconnect;

/**
 *
 * @author  __USER__
 */
public class Monitor extends javax.swing.JFrame {

	private static final long serialVersionUID = 9081016140666889788L;
	public static String scheduleType = null; //公开当前调度类型
	public static String batchno; //公开当前批次号，以便调度和监控使用
	private int[] scheduleStatus = new int[5]; //任务数、成功数、失败数、未完成数
	private MonitorGraph graph;
	private Schedule sch; //调度类
	private Thread thredSch;
	public static int refreshTime = 1;//刷新频率0/1/2/3  (单位：sconde)
	public static int parallel = 2; //作业并行度
	private java.util.Timer timer;
	private Map<String, Map<Thread, Schedule>> typeMap1 = new HashMap<String, Map<Thread, Schedule>>();;
	private HashMap<String, String[]> typeMap = new HashMap<String, String[]>();
	private HashMap<String, Schedule> batchnoMap = new HashMap<String, Schedule>(); //用户停止、续跑批次
	private HashMap<Schedule, Thread> scheduleMap = new HashMap<Schedule, Thread>(); //用于防止重复启动

	/** Creates new form Monitor */
	public Monitor() {
		// 设置图形界面外观
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// JGraph对象
		graph = new MonitorGraph();
		graph.setEditable(false);
		graph.setMonitor(this);
		// 创建调度实例
		//sch = new Schedule();
		// graph.removeMouseListener(graph.getMouseListeners());
		/*
		 * MouseListener[] ls = this.getMouseListeners(); for (MouseListener l :
		 * ls) { graph.removeMouseListener(l); }
		 */

		initComponents();

		// 程序初始化刷新调度类型
		refreshScheduleType(null);
		/*ArrayList<String> batchnos = DBUnit.getScheduleType();
		for (int i = 0; i < batchnos.size(); i++) {
			JMenuItem jMenuItemN = new javax.swing.JMenuItem(batchnos.get(i));
			jMenuItemN.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					addScheduleType(evt.getActionCommand());
				}
			});
			loadScheduTypeMenu.add(jMenuItemN);
		}*/

	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		aboutjDialog = new javax.swing.JDialog();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		settingDialog = new javax.swing.JDialog();
		parallelTextField = new javax.swing.JFormattedTextField();
		jLabel1 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		okButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane(graph);
		jMenuBar1 = new javax.swing.JMenuBar();
		loadScheduTypeMenu = new javax.swing.JMenu();
		refreshScheduTypeMenuItem = new javax.swing.JMenuItem();
		loadBatchnoMenu = new javax.swing.JMenu();
		refreshBatchnoMenuItem = new javax.swing.JMenuItem();
		jMenu3 = new javax.swing.JMenu();
		startMenuItem = new javax.swing.JMenuItem();
		jMenuItem5 = new javax.swing.JMenuItem();
		jMenuItem4 = new javax.swing.JMenuItem();
		jMenuItem12 = new javax.swing.JMenuItem();
		jMenu2 = new javax.swing.JMenu();
		jMenuItem6 = new javax.swing.JMenuItem();
		refreshBatchnoMenuItem1 = new javax.swing.JMenuItem();
		jMenuItem8 = new javax.swing.JMenuItem();
		jMenuItem9 = new javax.swing.JMenuItem();
		jMenu5 = new javax.swing.JMenu();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenuItem13 = new javax.swing.JMenuItem();
		jMenuItem3 = new javax.swing.JMenuItem();
		jMenu1 = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		settingMenuItem = new javax.swing.JMenuItem();
		jMenu4 = new javax.swing.JMenu();
		jMenuItem10 = new javax.swing.JMenuItem();
		jMenuItem7 = new javax.swing.JMenuItem();

		aboutjDialog.setTitle("\u8bf4\u660e");
		aboutjDialog.setBounds(new java.awt.Rectangle(0, 0, 0, 0));
		aboutjDialog.setMinimumSize(new java.awt.Dimension(480, 300));
		aboutjDialog.setModal(true);
		aboutjDialog.setResizable(false);

		jTextArea1.setColumns(20);
		jTextArea1.setEditable(false);
		jTextArea1.setRows(5);
		jTextArea1
				.setText("\n\u6b22\u8fce\u4f7f\u7528\ufe36\u3123\u6728\u6728\u30fe\u738b\u5b50\u7684\u8c03\u5ea6\u7cfb\u7edf!\n\u7248\u672c\u53f7:v1.8\n\n1.\u52a0\u8f7d\u4f5c\u4e1a\n2.\u7f16\u6392\u5e76\u4fdd\u5b58(\u53f3\u952esave all)\n3.\u8fd0\u884c-\u542f\u52a8\n\n\u66f4\u65b0\u65e5\u5fd7\uff1a\n1.20170804:\n  1)\u53f3\u952e\u83dc\u5355\u589e\u52a0\u5237\u65b0\n\n                                                  Jupiter.Lin\n                                                    2017.08\n                                                 linguiben@163.com");
		jScrollPane2.setViewportView(jTextArea1);

		javax.swing.GroupLayout aboutjDialogLayout = new javax.swing.GroupLayout(aboutjDialog.getContentPane());
		aboutjDialog.getContentPane().setLayout(aboutjDialogLayout);
		aboutjDialogLayout.setHorizontalGroup(aboutjDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2,
				javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE));
		aboutjDialogLayout.setVerticalGroup(aboutjDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2,
				javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE));

		aboutjDialog.getAccessibleContext().setAccessibleDescription("hello");

		settingDialog.setTitle("\u53c2\u6570\u8bbe\u7f6e");
		settingDialog.setMinimumSize(new java.awt.Dimension(320, 240));
		settingDialog.setModal(true);
		settingDialog.setResizable(false);

		parallelTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(
				new java.text.DecimalFormat("#"))));
		parallelTextField.setText("2");

		jLabel1.setText("\u5e76\u884c\u4f5c\u4e1a\u6570");

		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout.createSequentialGroup().addGap(94, 94, 94)
						.addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addContainerGap(85, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel1Layout
								.createSequentialGroup()
								.addContainerGap(159, Short.MAX_VALUE)
								.addGroup(
										jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(okButton)
												.addComponent(cancelButton)).addContainerGap()));

		javax.swing.GroupLayout settingDialogLayout = new javax.swing.GroupLayout(settingDialog.getContentPane());
		settingDialog.getContentPane().setLayout(settingDialogLayout);
		settingDialogLayout.setHorizontalGroup(settingDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				settingDialogLayout
						.createSequentialGroup()
						.addGroup(
								settingDialogLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(
												settingDialogLayout
														.createSequentialGroup()
														.addGap(18, 18, 18)
														.addComponent(jLabel1)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(parallelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGroup(
												settingDialogLayout
														.createSequentialGroup()
														.addContainerGap()
														.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))).addContainerGap()));
		settingDialogLayout.setVerticalGroup(settingDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				settingDialogLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								settingDialogLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1)
										.addComponent(parallelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));

		parallelTextField.setText(String.valueOf(Integer.parseInt(parallelTextField.getText()) % 20));

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Jobio v2.0 Monitor");
		setLocationByPlatform(true);
		setName("Monitor");

		jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));
		jScrollPane1.setAlignmentX(0.0F);
		jScrollPane1.setAlignmentY(0.0F);
		jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 0));
		jScrollPane1.getVerticalScrollBar().setUnitIncrement(20);
		jScrollPane1.getHorizontalScrollBar().setUnitIncrement(20);

		loadScheduTypeMenu.setText("\u52a0\u8f7d");

		refreshScheduTypeMenuItem.setText("\u5237\u65b0\u8c03\u5ea6\u7c7b\u578b");
		refreshScheduTypeMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshScheduleType(evt);
			}
		});
		loadScheduTypeMenu.add(refreshScheduTypeMenuItem);

		jMenuBar1.add(loadScheduTypeMenu);

		loadBatchnoMenu.setText("\u6279\u6b21");

		refreshBatchnoMenuItem.setText("\u5237\u65b0\u6279\u6b21\u53f7");
		refreshBatchnoMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshBatchnoMenuItemActionPerformed(evt);
				refreshBatchnoMenuActionPerformed(evt);
			}
		});
		loadBatchnoMenu.add(refreshBatchnoMenuItem);

		jMenuBar1.add(loadBatchnoMenu);

		jMenu3.setText("\u8fd0\u884c");

		startMenuItem.setText("\u542f\u52a8\u65b0\u7684\u6279\u6b21");
		startMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startSchedule(evt);
			}
		});
		jMenu3.add(startMenuItem);

		jMenuItem5.setText("\u505c\u6b62\u5f53\u524d\u6279\u6b21");
		jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				stopSchedule(evt);
			}
		});
		jMenu3.add(jMenuItem5);

		jMenuItem4.setText("\u6682\u505c/\u7ee7\u7eed");
		jMenu3.add(jMenuItem4);

		jMenuItem12.setText("\u7eed\u8dd1\u5f53\u524d\u6279\u6b21");
		jMenu3.add(jMenuItem12);

		jMenuBar1.add(jMenu3);

		jMenu2.setText("\u67e5\u770b");

		jMenuItem6.setText("\u7acb\u5373\u5237\u65b0");
		jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshScheduleStatus(evt);
			}
		});
		jMenu2.add(jMenuItem6);

		refreshBatchnoMenuItem1.setText("\u5237\u65b0\u9891\u7387:1\u5206\u949f");
		refreshBatchnoMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshBatchnoMenuItem1ActionPerformed(evt);
			}
		});
		jMenu2.add(refreshBatchnoMenuItem1);

		jMenuItem8.setText("\u5237\u65b0\u9891\u7387:3\u5206\u949f");
		jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem8ActionPerformed(evt);
			}
		});
		jMenu2.add(jMenuItem8);

		jMenuItem9.setText("\u5237\u65b0\u9891\u7387:5\u5206\u949f");
		jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem9ActionPerformed(evt);
			}
		});
		jMenu2.add(jMenuItem9);

		jMenuBar1.add(jMenu2);

		jMenu5.setText("\u6392\u7248");

		jMenuItem2.setText("\u6811\u5f62\u6392\u7248");
		jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem2ActionPerformed(evt);
			}
		});
		jMenu5.add(jMenuItem2);

		jMenuItem13.setText("\u6709\u673a\u6392\u7248");
		jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem13ActionPerformed(evt);
			}
		});
		jMenu5.add(jMenuItem13);

		jMenuItem3.setText("\u7b80\u6d01\u6392\u7248");
		jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem3ActionPerformed(evt);
			}
		});
		jMenu5.add(jMenuItem3);

		jMenuBar1.add(jMenu5);

		jMenu1.setText("\u8bbe\u7f6e");

		jMenuItem1.setText("\u670d\u52a1\u5668");
		jMenu1.add(jMenuItem1);

		settingMenuItem.setText("\u8c03\u5ea6\u53c2\u6570");
		settingMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				settingMenuItemActionPerformed(evt);
			}
		});
		jMenu1.add(settingMenuItem);

		jMenuBar1.add(jMenu1);

		jMenu4.setText("\u5e2e\u52a9");

		jMenuItem10.setText("\u5173\u4e8e");
		jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showAboutDailog(evt);
			}
		});
		jMenu4.add(jMenuItem10);

		jMenuItem7.setText("\u66f4\u65b0");
		jMenu4.add(jMenuItem7);

		jMenuBar1.add(jMenu4);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1,
				javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1,
				javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void settingMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		settingDialog.setLocationRelativeTo(this);
		settingDialog.setVisible(true);
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//GEN-FIRST:event_cancelButtonActionPerformed
	}

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Monitor.parallel = Integer.parseInt(parallelTextField.getText());
	}

	private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
		graph.rLayout(5); //简洁排版
	}

	private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {
		graph.rLayout(1);
	}

	private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
		graph.rLayout(3);
	}

	private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {
		// 定时器
		newTimer(5); //5分钟刷新
	}

	private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {
		newTimer(3); //3分钟刷新
	}

	private void refreshBatchnoMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
		newTimer(1); //5分钟刷新

	}

	//
	private void refreshBatchnoMenuActionPerformed(java.awt.event.ActionEvent evt) {

	}

	private void showAboutDailog(java.awt.event.ActionEvent evt) {
		//AboutjDialog aboutjDialog = new AboutjDialog(null,true);
		aboutjDialog.setLocationRelativeTo(this);
		aboutjDialog.setVisible(true);
	}

	private void refreshScheduleStatus(java.awt.event.ActionEvent evt) {
		graph.refreshScheduleJobLocationList(Monitor.batchno);
	}

	//刷新调度类型
	private void refreshScheduleType(java.awt.event.ActionEvent evt) {
		loadScheduTypeMenu.removeAll();
		loadScheduTypeMenu.add(refreshScheduTypeMenuItem);
		ArrayList<String> batchnos = DBUnit.getScheduleType();
		for (int i = 0; i < batchnos.size(); i++) {
			JMenuItem jMenuItemN = new javax.swing.JMenuItem(batchnos.get(i));
			jMenuItemN.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					//加载调度调度类型和作业
					addScheduleType(evt.getActionCommand());
					//加载调度类型的同时刷新批次号
					refreshBatchnoMenuItemActionPerformed(evt);
				}
			});
			loadScheduTypeMenu.add(jMenuItemN); //增加munu选择
		}
		//刷新调度类型时无需刷新批次号,待选择调度类型并加载后再刷新
		//refreshBatchnoMenuItemActionPerformed(evt);
		//Monitor.batchno = null;
	}

	//加载调度类型的作业
	// 加载调度的作业
	public void addScheduleType(String scheduleType) {
		if (timer != null)
			timer.cancel(); //取消定时刷新
		//记下调度类型和批次,续跑调度时传给调度线程
		//System.out.println("当前监控的类型0000000：" + Monitor.scheduleType);
		Monitor.scheduleType = scheduleType;
		Monitor.batchno = null;
		//System.out.println("当前监控的类型11111：" + Monitor.scheduleType);
		// 清空所有list,移除所有cell
		graph.getGraphLayoutCache().remove(graph.jobs.toArray());
		graph.getGraphLayoutCache().remove(graph.jobRelations.toArray());
		graph.jobs.clear();
		graph.jobRelations.clear();
		graph.boruntimes.clear();
		graph.boruntimesEdgs.clear();
		graph.files.clear();
		graph.filesEdgs.clear();
		//graph.getJobRelationsList(graph.getScheduleJobLocationList(actionCommand));
		ArrayList<Job> jobsList = graph.getScheduleJobLocationList(scheduleType); //根据调度类型获取调度作业
		List<JobEdge> relationsList = graph.getScheduleJobRelationsList(scheduleType,jobsList); //根据调度作业获取作业依赖关系
		//graph.getScheduleJobRelationsList(scheduleType, graph.getScheduleJobLocationList(scheduleType));
		//显示调度信息:
		GraphConstants.setValue(graph.getInfoCell().getAttributes(), "调度类型:" + Monitor.scheduleType + " 批次号:" + Monitor.batchno);
		graph.getGraphLayoutCache().insert(jobsList.toArray()); //ArrayList可能包含null的元素，转成数组去除null
		graph.getGraphLayoutCache().insert(relationsList.toArray());
		//graph.getGraphLayoutCache().setVisible(graph.getEndCell(), false);
		graph.getGraphLayoutCache().toFront(jobsList.toArray());
		graph.getGraphLayoutCache().insert(graph.getInfoCell());
	}

	//刷新批次号
	private void refreshBatchnoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		//选择批次号同时,更新graph和
		loadBatchnoMenu.removeAll();
		loadBatchnoMenu.add(refreshBatchnoMenuItem);
		ArrayList<String> batchnos = DBUnit.getBatchno(Monitor.scheduleType);
		for (int i = 0; i < batchnos.size(); i++) {
			JMenuItem jMenuItemN = new javax.swing.JMenuItem(batchnos.get(i));
			jMenuItemN.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					//addScheduleType(evt.getActionCommand());\
					if (timer != null)
						timer.cancel(); //取消定时刷新
					addScheduleType(Monitor.scheduleType);
					Monitor.batchno = evt.getActionCommand(); //选择批次号
					graph.refreshScheduleJobLocationList(batchno);
					//显示调度信息:
					scheduleStatus = DBUnit.getScheduleStatus(Monitor.scheduleType, Monitor.batchno);
					GraphConstants.setValue(graph.getInfoCell().getAttributes(), "调度类型:" + Monitor.scheduleType + " 批次号:" + Monitor.batchno + "   任务数:"
							+ scheduleStatus[0] + " 成功数:" + scheduleStatus[1] + " 失败数:" + scheduleStatus[2] + " 未完成数:" + scheduleStatus[3] + "  ");
					graph.getGraphLayoutCache().insert(graph.getInfoCell());
				}
			});
			loadBatchnoMenu.add(jMenuItemN);
		}
	}

	//创建自动刷新定时器
	public void newTimer(int refreshTime) {
		if (sch == null)
			return; //无调度时不刷新
		if (timer != null)
			timer.cancel();
		Monitor.refreshTime = refreshTime;
		timer = new Timer(true);
		graph.setTimer(timer);
		timer.schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				scheduleStatus = sch.getScheduleStatus();
				GraphConstants.setValue(graph.getInfoCell().getAttributes(), "调度类型:" + Monitor.scheduleType + " 批次号:" + Monitor.batchno + "   任务数:"
						+ scheduleStatus[0] + " 成功数:" + scheduleStatus[1] + " 失败数:" + scheduleStatus[2] + " 未完成数:" + scheduleStatus[3] + "  ");
				graph.refreshScheduleJobLocationList(Monitor.batchno);
				//scheduleStatus = DBUnit.getScheduleStatus(Monitor.scheduleType, Monitor.batchno);
				//graph.getGraphLayoutCache().update();
				//graph.getModel().beginUpdate();
				//graph.getModel().endUpdate();
				//graph.getGraphLayoutCache().insert(graph.getInfoCell());
				//graph.getGraphLayoutCache().setVisible(graph.getInfoCell(), false);
				//graph.getGraphLayoutCache().setVisible(graph.getInfoCell(), true);
				//graph.getModel().beginUpdate();
				//graph.getInfoCell().

				//graph.getGraphLayoutCache().update(graph.defaultCellViewFactorynew.createView(graph.getModel(), graph.getInfoCell()));
				//graph.getGraphLayoutCache().refresh((new DefaultCellViewFactory()).createView(graph.getModel(), graph.getInfoCell()), true);
				//graph.getGraphLayoutCache().re
				//System.out.println("timer5");
			}
		}, 0, refreshTime * 1000);
	}

	//启动调度
	public void startSchedule(java.awt.event.ActionEvent evt) {
		if (Monitor.scheduleType == null) {
			JOptionPane.showMessageDialog(null, "请选择业务类型" + "", "调度类型为空", 0);
			return;
		}
		System.out.println("启动的调度类型2：" + Monitor.scheduleType);
		Map<Thread, Schedule> map = new HashMap<Thread, Schedule>();
		if (thredSch != null && thredSch.isAlive()) {
			JOptionPane.showMessageDialog(null, "请勿重复调度," + "批次号:" + Monitor.batchno, "调度正在运行", 0);
		} else {
			addScheduleType(scheduleType);
			//启动调度线程
			SimpleDateFormat dfstr = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
			batchno = dfstr.format(new Date()); //生成调度批次号
			GraphConstants.setValue(graph.getInfoCell().getAttributes(), "调度类型:" + Monitor.scheduleType + " 批次号:" + Monitor.batchno);
			graph.getGraphLayoutCache().insert(graph.getInfoCell());
			// 创建调度实例
			sch = new Schedule();
			sch.setScheduleType(Monitor.scheduleType);
			sch.setBatchno(Monitor.batchno);
			sch.setSTOP(false); //置为非停止状态
			thredSch = new Thread(sch);

			map.put(thredSch, sch);
			typeMap1.put(Monitor.scheduleType, map);
			thredSch.start();
			newTimer(1); //默认1分钟刷新定时器
		}
	}

	//停止调度
	public void stopSchedule(java.awt.event.ActionEvent evt) {
		//1.找出Schele和Thread和
		if (thredSch == null)
			JOptionPane.showMessageDialog(null, "无运行中的调度", "停止调度", 0);
		else if (!thredSch.isAlive())
			JOptionPane.showMessageDialog(null, "已停止," + "批次号:" + Monitor.batchno, "停止调度", 0);
		else
			sch.stop();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		Monitor monior = new Monitor();
		int windowWidth = monior.getWidth(); // 获得窗口宽
		int windowHeight = monior.getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		monior.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 3 - windowHeight / 3); // 居中
		monior.setVisible(true);
		/*java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Monitor().setVisible(true);
			}
		});*/
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		Monitor.batchno = batchno;
	}

	public java.util.Timer getTimer() {
		return timer;
	}

	public void setTimer(java.util.Timer timer) {
		this.timer = timer;
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JDialog aboutjDialog;
	private javax.swing.JButton cancelButton;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JMenu jMenu3;
	private javax.swing.JMenu jMenu4;
	private javax.swing.JMenu jMenu5;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JMenuItem jMenuItem10;
	private javax.swing.JMenuItem jMenuItem12;
	private javax.swing.JMenuItem jMenuItem13;
	private javax.swing.JMenuItem jMenuItem2;
	private javax.swing.JMenuItem jMenuItem3;
	private javax.swing.JMenuItem jMenuItem4;
	private javax.swing.JMenuItem jMenuItem5;
	private javax.swing.JMenuItem jMenuItem6;
	private javax.swing.JMenuItem jMenuItem7;
	private javax.swing.JMenuItem jMenuItem8;
	private javax.swing.JMenuItem jMenuItem9;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JMenu loadBatchnoMenu;
	private javax.swing.JMenu loadScheduTypeMenu;
	private javax.swing.JButton okButton;
	private javax.swing.JFormattedTextField parallelTextField;
	private javax.swing.JMenuItem refreshBatchnoMenuItem;
	private javax.swing.JMenuItem refreshBatchnoMenuItem1;
	private javax.swing.JMenuItem refreshScheduTypeMenuItem;
	private javax.swing.JDialog settingDialog;
	private javax.swing.JMenuItem settingMenuItem;
	private javax.swing.JMenuItem startMenuItem;
	// End of variables declaration//GEN-END:variables
}