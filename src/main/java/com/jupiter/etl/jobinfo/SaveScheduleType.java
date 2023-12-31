/*
 * SaveScheduleType.java
 *
 * Created on __DATE__, __TIME__
 */

package com.jupiter.etl.jobinfo;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import com.jupiter.mybatis.dao.DBUnit;

/**
 *
 * @author  __USER__
 */
public class SaveScheduleType extends javax.swing.JDialog {
	/** A return status code - returned if Cancel button has been pressed */
	public static final int RET_CANCEL = 0;
	/** A return status code - returned if OK button has been pressed */
	public static final int RET_OK = 1;

	private DesignerGraph graph;

	/** Creates new form SaveScheduleType */
	public SaveScheduleType(java.awt.Frame parent, boolean modal, DesignerGraph graph) {
		super(parent, modal);
		System.out.println("i am :" + graph.getClass().toString());
		this.graph = graph;
		//graph;
		initComponents();
	}

	/** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
	public int getReturnStatus() {
		return returnStatus;
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
		okButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();
		jRadioButton1 = new javax.swing.JRadioButton();
		jRadioButton2 = new javax.swing.JRadioButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		jList1 = new javax.swing.JList();
		jTextField1 = new javax.swing.JTextField();
		jProgressBar1 = new javax.swing.JProgressBar();

		setTitle("Save All Jobs");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});

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

		buttonGroup1.add(jRadioButton1);
		jRadioButton1.setText("\u65b0\u589e\u8c03\u5ea6\u7c7b\u578b");

		buttonGroup1.add(jRadioButton2);
		jRadioButton2.setSelected(true);
		jRadioButton2.setText("\u8986\u76d6\u539f\u6709\u8c03\u5ea6\u7c7b\u578b");

		jList1.setModel(new javax.swing.AbstractListModel() {
			//加载调度类型
			ArrayList<String> batchnos = DBUnit.getScheduleType();
			String[] strings = batchnos.toArray(new String[batchnos.size()]);

			//String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		jScrollPane1.setViewportView(jList1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(120, 120, 120)
																.addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addGroup(
																		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165,
																						javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton2))))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(cancelButton)
												.addGroup(
														layout.createSequentialGroup()
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addGroup(
																		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(jRadioButton1)
																				.addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 177,
																						Short.MAX_VALUE)))
												.addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)).addContainerGap()));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { cancelButton, okButton });

		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton2).addComponent(jRadioButton1))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(
												layout.createSequentialGroup()
														.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
														.addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)).addGap(48, 48, 48)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(okButton).addComponent(cancelButton))
						.addContainerGap()));

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
	
	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		doClose(RET_OK);
	}//GEN-LAST:event_okButtonActionPerformed

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		doClose(RET_CANCEL);
	}//GEN-LAST:event_cancelButtonActionPerformed

	/** Closes the dialog */
	private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
		doClose(RET_CANCEL);
	}//GEN-LAST:event_closeDialog

	private void doClose(int retStatus) {
		if (retStatus == RET_OK) {
			//保存调度的作业
			String schuduleType;
			if (jRadioButton2.isSelected())
				schuduleType = jList1.getSelectedValue().toString();
			else
				schuduleType = jTextField1.getText();
			System.out.println("save scheduletype:" + schuduleType);
			System.out.println("grpah:" + graph.toString());
			graph.saveAllJobs(schuduleType);
		}
		returnStatus = retStatus;
		setVisible(false);
		dispose();

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				SaveScheduleType dialog = new SaveScheduleType(new javax.swing.JFrame(), true, null);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JButton cancelButton;
	private javax.swing.JList jList1;
	private javax.swing.JProgressBar jProgressBar1;
	private javax.swing.JRadioButton jRadioButton1;
	private javax.swing.JRadioButton jRadioButton2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JButton okButton;
	// End of variables declaration//GEN-END:variables

	private int returnStatus = RET_CANCEL;
}