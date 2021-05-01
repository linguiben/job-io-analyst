/*
 * EditJobProperty.java
 *
 * Created on __DATE__, __TIME__
 */

package com.jupiter.etl.jobinfo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphConstants;

import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.etl.schedule.Monitor;

/**
 *
 * @author  __USER__
 */
public class EditJobProperty extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4642134879536849579L;
	/** A return status code - returned if Cancel button has been pressed */
	public static final int RET_CANCEL = 0;
	/** A return status code - returned if OK button has been pressed */
	public static final int RET_OK = 1;
	private int returnStatus = RET_CANCEL;
	private String jobname;
	private Job job;
	private DesignerGraph graph;

	/** Creates new form EditJobProperty */
	public EditJobProperty(java.awt.Frame parent, String jobname, boolean modal, Job job, DesignerGraph graph) {
		super(parent, jobname, modal);
		this.jobname = jobname;
		this.job = job;
		this.graph = graph;
		initComponents();

		jComboBox1.setSelectedItem(job.jobtype);
		jCheckBox2.setSelected(job.isValid == 1 ? true : false);
		jCheckBox1.setSelected(job.isSchedule == 0 ? false : true);
		jComboBox2.setSelectedItem(job.on_fail_action != null && job.on_fail_action.equals("skip:") ? "skip" : "stop");
		jFormattedTextField2.setText(job.cost);
		jFormattedTextField1.setText(Integer.toString(job.groupID));
		jTextField5.setText(job.groupName);
		jTextArea1.setText(job.memo);
		jTextArea2.setText(job.params);
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jProgressBar1 = new javax.swing.JProgressBar();
		jLabel1 = new javax.swing.JLabel();
		jComboBox1 = new javax.swing.JComboBox();
		jCheckBox1 = new javax.swing.JCheckBox();
		jCheckBox2 = new javax.swing.JCheckBox();
		jLabel2 = new javax.swing.JLabel();
		jComboBox2 = new javax.swing.JComboBox();
		jLabel3 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jTextField5 = new javax.swing.JTextField();
		jLabel9 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		jLabel10 = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTextArea2 = new javax.swing.JTextArea();
		jFormattedTextField1 = new javax.swing.JFormattedTextField();
		jFormattedTextField2 = new javax.swing.JFormattedTextField();
		jobnameTextField = new javax.swing.JFormattedTextField();
		okButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();
		jobnameTextField1 = new javax.swing.JFormattedTextField();
		jLabel4 = new javax.swing.JLabel();

		setTitle("Edit Job Property");
		setBounds(100, 100, 100, 100);
		setLocationByPlatform(true);
		setModal(true);
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				save(evt);
			}
		});
		addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				formKeyPressed(evt);
			}

			public void keyTyped(java.awt.event.KeyEvent evt) {
				formKeyTyped(evt);
			}
		});

		jLabel1.setText("jobtype");

		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dsjob", "bojob", "shell", "BlankJob", "TAB", "FILE", "ktljob" }));

		jCheckBox1.setSelected(true);
		jCheckBox1.setText("isSchedule");

		jCheckBox2.setSelected(true);
		jCheckBox2.setText("isValid");

		jLabel2.setText("on_fail_action");

		jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "stop", "skip" }));

		jLabel3.setText("avg cost");

		jLabel7.setText("groupID");

		jLabel8.setText("groupName");

		jLabel9.setText("menmo");

		jTextArea1.setColumns(20);
		jTextArea1.setRows(2);
		jScrollPane1.setViewportView(jTextArea1);

		jLabel10.setText("params with out server&user/password");

		jTextArea2.setColumns(20);
		jTextArea2.setRows(5);
		jScrollPane2.setViewportView(jTextArea2);

		jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(
				"###"))));
		jFormattedTextField1.setMaximumSize(new java.awt.Dimension(9999, 9999));

		jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(
				new java.text.SimpleDateFormat("HH:mm:ss"))));
		jFormattedTextField2.setText("00:00:00");

		jobnameTextField.setText(this.jobname);

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

		jobnameTextField1.setEditable(false);
		jobnameTextField1.setText(Monitor.scheduleType);

		jLabel4.setText("\u8c03\u5ea6(\u4e1a\u52a1)\u7c7b\u578b");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addGroup(
																		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
																				.addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
																				.addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addGroup(
																														layout.createSequentialGroup()
																																.addGroup(
																																		layout.createParallelGroup(
																																				javax.swing.GroupLayout.Alignment.TRAILING)
																																				.addComponent(
																																						jLabel9,
																																						javax.swing.GroupLayout.Alignment.LEADING)
																																				.addGroup(
																																						javax.swing.GroupLayout.Alignment.LEADING,
																																						layout.createSequentialGroup()
																																								.addGroup(
																																										layout.createParallelGroup(
																																												javax.swing.GroupLayout.Alignment.LEADING)
																																												.addComponent(
																																														jLabel1)
																																												.addComponent(
																																														jLabel3))
																																								.addGap(7,
																																										7,
																																										7)
																																								.addGroup(
																																										layout.createParallelGroup(
																																												javax.swing.GroupLayout.Alignment.LEADING)
																																												.addComponent(
																																														jFormattedTextField2,
																																														javax.swing.GroupLayout.DEFAULT_SIZE,
																																														81,
																																														Short.MAX_VALUE)
																																												.addComponent(
																																														jComboBox1,
																																														javax.swing.GroupLayout.PREFERRED_SIZE,
																																														javax.swing.GroupLayout.DEFAULT_SIZE,
																																														javax.swing.GroupLayout.PREFERRED_SIZE))))
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																																.addComponent(
																																		jCheckBox2,
																																		javax.swing.GroupLayout.DEFAULT_SIZE,
																																		66, Short.MAX_VALUE))
																												.addComponent(jobnameTextField,
																														javax.swing.GroupLayout.PREFERRED_SIZE,
																														201,
																														javax.swing.GroupLayout.PREFERRED_SIZE))
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.TRAILING)
																												.addGroup(
																														layout.createSequentialGroup()
																																.addGroup(
																																		layout.createParallelGroup(
																																				javax.swing.GroupLayout.Alignment.LEADING)
																																				.addComponent(
																																						jCheckBox1,
																																						javax.swing.GroupLayout.Alignment.TRAILING,
																																						javax.swing.GroupLayout.DEFAULT_SIZE,
																																						93,
																																						Short.MAX_VALUE)
																																				.addGroup(
																																						layout.createSequentialGroup()
																																								.addComponent(
																																										jLabel7)
																																								.addPreferredGap(
																																										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																																										6,
																																										Short.MAX_VALUE)
																																								.addComponent(
																																										jFormattedTextField1,
																																										javax.swing.GroupLayout.PREFERRED_SIZE,
																																										38,
																																										javax.swing.GroupLayout.PREFERRED_SIZE)))
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																																.addGroup(
																																		layout.createParallelGroup(
																																				javax.swing.GroupLayout.Alignment.LEADING,
																																				false)
																																				.addGroup(
																																						layout.createSequentialGroup()
																																								.addComponent(
																																										jLabel2)
																																								.addPreferredGap(
																																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																								.addComponent(
																																										jComboBox2,
																																										javax.swing.GroupLayout.PREFERRED_SIZE,
																																										javax.swing.GroupLayout.DEFAULT_SIZE,
																																										javax.swing.GroupLayout.PREFERRED_SIZE))
																																				.addGroup(
																																						layout.createSequentialGroup()
																																								.addComponent(
																																										jLabel8)
																																								.addPreferredGap(
																																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																								.addComponent(
																																										jTextField5))))
																												.addGroup(
																														layout.createSequentialGroup()
																																.addComponent(jLabel4)
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																.addComponent(
																																		jobnameTextField1,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		128,
																																		javax.swing.GroupLayout.PREFERRED_SIZE))))
																				.addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(159, 159, 159)
																.addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton)))
								.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jobnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jobnameTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1)
										.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jCheckBox2)
										.addComponent(jCheckBox1)
										.addComponent(jLabel2)
										.addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel3)
										.addComponent(jLabel7)
										.addComponent(jLabel8)
										.addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(12, 12, 12).addComponent(jLabel9)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel10)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(okButton).addComponent(cancelButton))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	@Override
	protected JRootPane createRootPane() {
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				escapeKeyProc();
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

		return rootPane;
	}

	/** 
	 * 处理ESCAPE按键。子类可以重新覆盖该方法，实现自己的处理方式。 
	 */
	protected void escapeKeyProc() {
		doClose(RET_CANCEL);
	}

	private void formKeyPressed(java.awt.event.KeyEvent evt) {
		System.out.println("esc");
		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			doClose(RET_CANCEL);
		} else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			doClose(RET_OK);
		}
	}

	private void formKeyTyped(java.awt.event.KeyEvent evt) {
		/*if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			doClose(RET_CANCEL);
		} else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			doClose(RET_OK);
		}*/
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//GEN-FIRST:event_cancelButtonActionPerformed
		doClose(RET_CANCEL);
	}

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//GEN-FIRST:event_okButtonActionPerformed
		doClose(RET_OK);
	}

	private void doClose(int retStatus) {
		if (retStatus == RET_OK) {
			saveThis(null);
		}
		returnStatus = retStatus;
		setVisible(false);
		dispose();

	}

	private void save(java.awt.event.WindowEvent evt) {
		//找到Job,修改其属性
		//System.out.println("close");
	}

	private void saveThis(java.awt.event.WindowEvent evt) {
		//System.out.print("saveThis");
		//System.out.println(job.getAttributes());
		GraphConstants.setValue(job.getAttributes(), jobnameTextField.getText());//修改名称
		//GraphConstants.setAutoSize(job.getAttributes(), isShowing());
		//System.out.println(job.getAttributes());
		//修改属性
		job.jobname = jobnameTextField.getText();
		job.jobtype = jComboBox1.getSelectedItem().toString();
		job.isValid = jCheckBox2.isSelected() ? 1 : 0;
		job.isSchedule = jCheckBox1.isSelected() ? 1 : 0;
		job.on_fail_action = jComboBox2.getSelectedItem() == null ? null : jComboBox2.getSelectedItem().toString();
		job.cost = jFormattedTextField2.getText();
		try {
			job.groupID = Integer.parseInt(jFormattedTextField1.getText());
		} catch (NumberFormatException e) {
			job.groupID = 0;
		}
		job.groupName = jTextField5.getText();
		job.memo = jTextArea1.getText();
		job.params = jTextArea2.getText();
		//System.out.println(new JGraph().convertValueToString(job)+"==" + "type:" + job.jobtype + "id:" + job.groupID + " parmas:" + job.params);
		job.setColor();
		//GraphConstants.setBackground(job.getAttributes(),Color.lightGray);
		graph.getGraphLayoutCache().editCell(job, job.getAttributes());
		//graph.getGraphLayoutCache().reload();
		//graph.refresh();
	}

	/**
	 * @param args the command line arguments
	 */
	/*public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				EditJobProperty dialog = new EditJobProperty(new javax.swing.JFrame(), "Edit Job", true, null);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}*/

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JButton cancelButton;
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JCheckBox jCheckBox2;
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JComboBox jComboBox2;
	private javax.swing.JFormattedTextField jFormattedTextField1;
	private javax.swing.JFormattedTextField jFormattedTextField2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JProgressBar jProgressBar1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JTextArea jTextArea2;
	private javax.swing.JTextField jTextField5;
	private javax.swing.JFormattedTextField jobnameTextField;
	private javax.swing.JFormattedTextField jobnameTextField1;
	private javax.swing.JButton okButton;
	// End of variables declaration//GEN-END:variables

}