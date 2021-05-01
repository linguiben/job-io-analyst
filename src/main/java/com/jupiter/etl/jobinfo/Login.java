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
	
	//版本号
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
		Label lblUsername = new Label("用户名");
		final TextField txtUsername = new TextField(9);
		Label lblPassword = new Label("密码");
		final TextField txtPassword = new TextField(9);
		final InitProperty ip = new InitProperty();
		
		loginFrame.setResizable(false);
		txtUsername.setText(ip.getLoginUser());
		txtUsername.setEditable(true);
		txtPassword.setEchoChar('*');
		txtPassword.setText(ip.getLoginPassword());
		Button settingButton = new Button("设置");
		//Label lblRptDate = new Label("欢迎使用!");
		final Button loginButton = new Button("登录");
		Button closeButton = new Button("关闭");
		Button scheduleButton = new Button("运行调度");
		
		final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(calendar.DATE, -1);
		
		final User user = new User();
	
		//响应登录事件
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((txtPassword.getText()).length() == 0) {
					JOptionPane.showMessageDialog(loginFrame, "请输入密码");
					loginButton.transferFocusUpCycle();
					return;
				}
				if ((txtUsername.getText()).length() == 0) {
					JOptionPane.showMessageDialog(loginFrame, "用户名为空");
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
						com.jupiter.WriteLog.writeFile(this.getClass().getName()+":连接失败!");
						JOptionPane.showMessageDialog(loginFrame, "连接失败!");
						loginFrame.getFocusableWindowState();
						loginButton.transferFocusUpCycle();
						return;
					}else if(loginStatus == 0){
						com.jupiter.WriteLog.writeFile("用户名或者密码错误!");
						JOptionPane.showMessageDialog(loginFrame, "用户名或者密码错误!");
						loginButton.transferFocusUpCycle();
						return;
					}
					
					/*final JobioFrame jobioFrame = new JobioFrame();
					jobioFrame.init(jobioFrame);
					loginFrame.setVisible(false);*/
					//JOptionPane.showMessageDialog(frmFrame, "OK.\n" );
					{  //登录成功打开Jobio Analysis
						user.setUserID(txtUsername.getText());
						user.setPassword(txtPassword.getText());
						user.setOptionTime(new Date());
						user.setVersion(version);
//						JobioAnalyst3_0 ds = new JobioAnalyst3_0(user);
						JobioAnalyst3 jobioAnalyst = new JobioAnalyst3(user);
						int windowWidth = jobioAnalyst.getWidth(); // 获得窗口宽
						int windowHeight = jobioAnalyst.getHeight(); // 获得窗口高
						Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
						Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
						int screenWidth = screenSize.width; // 获取屏幕的宽
						int screenHeight = screenSize.height; // 获取屏幕的高
						jobioAnalyst.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 3 - windowHeight / 3); // 居中
						jobioAnalyst.setVisible(true);
						loginFrame.setVisible(false);
					}
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(loginFrame, "失败,请重试或联系管理员");
					e1.printStackTrace();
					return;
				}
			};
			
		});
		
		//响应关闭事件
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		// 打开调度窗口
		scheduleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Monitor monior = new Monitor();
				int windowWidth = monior.getWidth(); // 获得窗口宽
				int windowHeight = monior.getHeight(); // 获得窗口高
				Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
				Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
				int screenWidth = screenSize.width; // 获取屏幕的宽
				int screenHeight = screenSize.height; // 获取屏幕的高
				monior.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 3 - windowHeight / 3); // 居中
				loginFrame.setVisible(false);
				monior.setVisible(true);
			}
		});
		
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int windowWidth = loginFrame.getWidth(); // 获得窗口宽
		int windowHeight = loginFrame.getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
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
	 * 登录
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
			JOptionPane.showMessageDialog(null, "当前版本不可用。\n最新版本：V"+lastVersion);
			System.exit(0);
		}else if(lastVersion > login.version){
			int n = JOptionPane.showConfirmDialog(null, "当前版本：V"+login.version+"\n是否继续使用此版本?", "已有新版本，最新版本：V"+lastVersion, JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				// ......
			} else if (n == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		}
	}
	
}
