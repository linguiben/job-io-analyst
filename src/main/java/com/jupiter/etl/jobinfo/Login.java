package com.jupiter.etl.jobinfo;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.jupiter.etl.schedule.Monitor;
import com.jupiter.mybatis.dao.DBUnit;
import com.jupiter.mybatis.po.User;

@Controller("login")
public class Login {
	
	//�汾��
	final private double version = 3.2;
	
	@Autowired
	private DBUnit dbUnit;
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	private String pwd = "";
	public String loginInit() {
		final JFrame loginFrame = new JFrame();
		Panel pnlPanel = new Panel();
		Label lblUsername = new Label("�û���");
		final TextField txtUsername = new TextField(9);
		Label lblPassword = new Label("����");
		final TextField txtPassword = new TextField(9);
		final InitProperty ip = new InitProperty();
		
		loginFrame.setResizable(false);
		txtUsername.setText(ip.getLoginUser());
		txtUsername.setEditable(true);
		txtPassword.setEchoChar('*');
		txtPassword.setText(ip.getLoginPassword());
		Button settingButton = new Button("����");
		//Label lblRptDate = new Label("��ӭʹ��!");
		final Button loginButton = new Button("��¼");
		Button closeButton = new Button("�ر�");
		Button scheduleButton = new Button("���е���");
		
		final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(calendar.DATE, -1);
		
		final User user = new User();
	
		//��Ӧ��¼�¼�
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((txtPassword.getText()).length() == 0) {
					JOptionPane.showMessageDialog(loginFrame, "����������");
					loginButton.transferFocusUpCycle();
					return;
				}
				if ((txtUsername.getText()).length() == 0) {
					JOptionPane.showMessageDialog(loginFrame, "�û���Ϊ��");
					loginButton.transferFocusUpCycle();
					return;
				}
				//txtPassword.setColumns(16);
				pwd = txtPassword.getText();
				try {
					ip.setLoginPassword(txtPassword.getText());
					ip.setLoginUser(txtUsername.getText());
					int loginStatus = login(ip);
					if(loginStatus == -1){
						com.jupiter.WriteLog.writeFile(this.getClass().getName()+":����ʧ��!");
						JOptionPane.showMessageDialog(loginFrame, "����ʧ��!");
						loginFrame.getFocusableWindowState();
						loginButton.transferFocusUpCycle();
						return;
					}else if(loginStatus == 0){
						com.jupiter.WriteLog.writeFile("�û��������������!");
						JOptionPane.showMessageDialog(loginFrame, "�û��������������!");
						loginButton.transferFocusUpCycle();
						return;
					}
					
					/*final JobioFrame jobioFrame = new JobioFrame();
					jobioFrame.init(jobioFrame);
					loginFrame.setVisible(false);*/
					//JOptionPane.showMessageDialog(frmFrame, "OK.\n" );
					{  //��¼�ɹ���Jobio Analysis
						user.setUserID(txtUsername.getText());
						user.setPassword(txtPassword.getText());
						user.setOptionTime(new Date());
						user.setVersion(version);
//						JobioAnalyst3_0 ds = new JobioAnalyst3_0(user);
						JobioAnalyst3 jobioAnalyst = new JobioAnalyst3(user);
						int windowWidth = jobioAnalyst.getWidth(); // ��ô��ڿ�
						int windowHeight = jobioAnalyst.getHeight(); // ��ô��ڸ�
						Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
						Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
						int screenWidth = screenSize.width; // ��ȡ��Ļ�Ŀ�
						int screenHeight = screenSize.height; // ��ȡ��Ļ�ĸ�
						jobioAnalyst.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 3 - windowHeight / 3); // ����
						jobioAnalyst.setVisible(true);
						loginFrame.setVisible(false);
					}
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(loginFrame, "ʧ��,�����Ի���ϵ����Ա");
					e1.printStackTrace();
					return;
				}
			};
			
		});
		
		//��Ӧ�ر��¼�
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		// �򿪵��ȴ���
		scheduleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Monitor monior = new Monitor();
				int windowWidth = monior.getWidth(); // ��ô��ڿ�
				int windowHeight = monior.getHeight(); // ��ô��ڸ�
				Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
				Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
				int screenWidth = screenSize.width; // ��ȡ��Ļ�Ŀ�
				int screenHeight = screenSize.height; // ��ȡ��Ļ�ĸ�
				monior.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 3 - windowHeight / 3); // ����
				loginFrame.setVisible(false);
				monior.setVisible(true);
			}
		});
		
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int windowWidth = loginFrame.getWidth(); // ��ô��ڿ�
		int windowHeight = loginFrame.getHeight(); // ��ô��ڸ�
		Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
		Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
		int screenWidth = screenSize.width; // ��ȡ��Ļ�Ŀ�
		int screenHeight = screenSize.height; // ��ȡ��Ļ�ĸ�
		loginFrame.setLocation(screenWidth / 3 - windowWidth / 2, screenHeight / 3 - windowHeight / 2);

		pnlPanel.add(lblUsername);
		pnlPanel.add(txtUsername);
		pnlPanel.add(lblPassword);
		pnlPanel.add(txtPassword);
		pnlPanel.add(settingButton);
		//pnlPanel.add(lblRptDate);
		//pnlPanel.add(txtRptDate);
		pnlPanel.add(loginButton);
		pnlPanel.add(closeButton);
		pnlPanel.add(scheduleButton);
		loginFrame.add(pnlPanel);
		loginFrame.setTitle("Jobio Analyst "+version);
		loginFrame.pack();
		loginButton.requestFocus();
		loginFrame.setVisible(true);
		return pwd;
	}
	
	/**
	 * ��¼
	 */
	public int login(InitProperty ip) {
		return DBUnit.login(ip);
	}

	public static void main(String args[]) throws Exception {
		String xmlPath = "applicationContext.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
		//Login login = new Login();
		Login login = (Login) applicationContext.getBean("login");
		login.loginInit();
		login.checkVersionUsable(login);
	}

	private void checkVersionUsable(Login login) {
		DBUnit dbUnit = login.dbUnit;
        boolean isVersionUseable = dbUnit.isVersionUseable(login.version);
        double lastVersion = dbUnit.getLastVersion();
		if(!isVersionUseable){
			JOptionPane.showMessageDialog(null, "��ǰ�汾�����á�\n���°汾��V"+lastVersion);
			System.exit(0);
		}else if(lastVersion > login.version){
			int n = JOptionPane.showConfirmDialog(null, "��ǰ�汾��V"+login.version+"\n�Ƿ����ʹ�ô˰汾?", "�����°汾�����°汾��V"+lastVersion, JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				// ......
			} else if (n == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		}
	}
	
}
