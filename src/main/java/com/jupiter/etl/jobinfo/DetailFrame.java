/*
 * DetailFrame.java
 *
 * Created on __DATE__, __TIME__
 */

package com.jupiter.etl.jobinfo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jupiter.etl.schedule.DefaultJob;
import com.jupiter.mybatis.dao.DBUnit;

/**
 *
 * @author  __USER__
 */
public class DetailFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6370177964293294268L;
	private String jobname;
	private DefaultJob jobDetail;

	/** Creates new form DetailFrame */
	public DetailFrame() {
		initComponents();
	}

	public DetailFrame(String jobname, String jobtype) {
		this.jobname = jobname;
		jobDetail = DBUnit.getJobDetail(jobname, jobtype);
		initComponents();
		this.addWindowFocusListener(new WindowAdapter() {
			public void windowLostFocus(WindowEvent e) {
				//e.getWindow().requestFocus(); 
			}
		});
		if (jobtype.equals("TAB")||jobtype.equals("BlankJob")) {
			inputLLabel1.setText("Tabname:");
			inputLLabel.setText("JobOut:");
			outputLabel.setText("InToJob:");
		}
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		detailTextArea = new javax.swing.JTextArea();
		inputLLabel = new javax.swing.JLabel();
		outputLabel = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		inputTextArea = new javax.swing.JTextArea();
		detailLabel = new javax.swing.JLabel();
		joblocateTextField = new javax.swing.JTextField();
		jobnameTextField = new javax.swing.JTextField();
		inputLLabel1 = new javax.swing.JLabel();
		outputLabel1 = new javax.swing.JLabel();
		jScrollPane3 = new javax.swing.JScrollPane();
		outputTextArea = new javax.swing.JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("input/output detail");

		detailTextArea.setColumns(20);
		detailTextArea.setRows(5);
		String[] str = jobDetail.getDetailList().toArray(new String[] {});
		for (int i = 0; i < str.length; i++) {
			detailTextArea.append(str[i] + "\n");
		}
		jScrollPane1.setViewportView(detailTextArea);

		inputLLabel.setText("Input:");

		outputLabel.setText("Output:");

		inputTextArea.setColumns(20);
		inputTextArea.setRows(5);
		//inputTextArea.append(jobDetail.getInputList().toString());
		str = jobDetail.getInputList().toArray(new String[] {});
		for (int i = 0; i < str.length; i++) {
			inputTextArea.append(str[i] + "\n");
		}
		jScrollPane2.setViewportView(inputTextArea);

		detailLabel.setText("Detail:");

		joblocateTextField.setText(jobDetail.getJoblocate());

		jobnameTextField.setText(this.jobname);
		jobnameTextField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jobnameTextFieldActionPerformed(evt);
			}
		});

		inputLLabel1.setText("Jobname:");

		outputLabel1.setText("Locate:");

		outputTextArea.setColumns(20);
		outputTextArea.setRows(5);
		str = jobDetail.getOutputList().toArray(new String[] {});
		for (int i = 0; i < str.length; i++) {
			outputTextArea.append(str[i] + "\n");
		}
		jScrollPane3.setViewportView(outputTextArea);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
												.addComponent(detailLabel)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.TRAILING)
																												.addGroup(
																														javax.swing.GroupLayout.Alignment.LEADING,
																														layout.createSequentialGroup()
																																.addComponent(inputLLabel1)
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																.addComponent(
																																		jobnameTextField,
																																		javax.swing.GroupLayout.DEFAULT_SIZE,
																																		190, Short.MAX_VALUE))
																												.addComponent(jScrollPane2,
																														javax.swing.GroupLayout.DEFAULT_SIZE,
																														251, Short.MAX_VALUE))
																								.addGap(18, 18, 18))
																				.addGroup(
																						layout.createSequentialGroup().addComponent(inputLLabel)
																								.addGap(233, 233, 233)))
																.addGroup(
																		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(outputLabel1)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(joblocateTextField,
																										javax.swing.GroupLayout.DEFAULT_SIZE, 214,
																										Short.MAX_VALUE))
																				.addComponent(outputLabel)
																				.addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 260,
																						Short.MAX_VALUE)).addContainerGap()))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(inputLLabel1)
										.addComponent(jobnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(outputLabel1)
										.addComponent(joblocateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(inputLLabel).addComponent(outputLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
										.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)).addGap(4, 4, 4)
						.addComponent(detailLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE).addContainerGap()));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void jobnameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * @param args the command line arguments
	 */
	/*public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new DetailFrame().setVisible(true);
			}
		});
	}*/

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JLabel detailLabel;
	private javax.swing.JTextArea detailTextArea;
	private javax.swing.JLabel inputLLabel;
	private javax.swing.JLabel inputLLabel1;
	private javax.swing.JTextArea inputTextArea;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JTextField joblocateTextField;
	private javax.swing.JTextField jobnameTextField;
	private javax.swing.JLabel outputLabel;
	private javax.swing.JLabel outputLabel1;
	private javax.swing.JTextArea outputTextArea;
	// End of variables declaration//GEN-END:variables

}