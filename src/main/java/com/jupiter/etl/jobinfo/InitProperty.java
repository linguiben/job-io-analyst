package com.jupiter.etl.jobinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.regex.Pattern;


public class InitProperty {
	private int loginStatus = 0;
	public int getRptdtdate() {
		return loginStatus;
	}

	public void setRptdtdate(int rptdtdate) {
		this.loginStatus = rptdtdate;
	}

	
	private static String db_host;
	private static String db_dbname;
	private static String db_user;
	public static String getDBUser() {
		return db_user;
	}

	public static void setDBUser(String db_user) {
		InitProperty.db_user = db_user;
	}


	private static String db_password;
	private static String login_user;
	private static String login_password;
	
	public String getLoginUser() {
		return login_user;
	}

	public void setLoginUser(String gUser) {
		login_user = gUser;
	}

	public static String g_port;
	public static String g_dbtype;
	public static String driverName = "";
	public static String url = "";
	/*
	 * �ж��Ƿ�Ϊ������������double��float
	 * 
	 * @param str ������ַ���
	 * 
	 * @return �Ǹ���������true,���򷵻�false
	 */
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * ���Ƶ����ļ�
	 * 
	 * @param oldPath
	 *            String ԭ�ļ�·�� �磺c:/fqf.txt
	 * @param newPath
	 *            String ���ƺ�·�� �磺f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // �ļ�����ʱ
				InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // �ֽ��� �ļ���С
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}




	public String getDBPassword() {
		return this.db_password;
	}

	/**
	 * ���췽������ʼ�������ļ�
	 * 
	 * @throws Exception
	 */
	public InitProperty() {
		try {
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("db.properties");
		Properties p = new Properties();

			p.load(inputStream);
//			db_host = p.getProperty("SERVERNAME");
//			db_dbname = p.getProperty("DBNAME");
//			db_user = p.getProperty("DBUSER");
//			db_password = p.getProperty("DBPASSWORD");
			g_port = p.getProperty("PORT");
			g_dbtype = p.getProperty("DBTYPE");
			
			login_user = p.getProperty("JobioUser");
			login_password = p.getProperty("JobioPassword");
			setDBPassword(p.getProperty("DBPASSWORD"));
			
		} catch (IOException e1) {
			e1.printStackTrace();
			com.jupiter.WriteLog.writeFile("��ȡ�����ļ�����,�˳�����");
			System.exit(0);
		}
//		if (g_dbtype.toLowerCase().equals("db2")) {
//			driverName = "com.ibm.db2.jcc.DB2Driver";
//			url = "jdbc:" + g_dbtype + "://" + db_host + ":" + g_port + "/"
//					+ db_dbname;
//		} else if (g_dbtype.toLowerCase().equals("oracle")) {
//			driverName = "oracle.jdbc.driver.OracleDriver";
//			url = "jdbc:" + g_dbtype + "://" + db_host + ":" + g_port + "/"
//					+ db_dbname;
//		} else if (g_dbtype.toLowerCase().equals("sqlserver")) {
//			driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//			url = "jdbc:sqlserver://" + db_host + ":" + g_port
//					+ ";DatabaseName=" + db_dbname;
//		}else{
//			com.jupiter.WriteLog.writeFile("�����ļ����ݿ���Ϣ����!�޷�ʶ����������ݿ⣡");
//			System.exit(0);
//		}
	}

	/**
	 * �޸������ļ�
	 * @param args
	 * @throws Exception
	 */
	public void setProperty(String key,String value){
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/db.properties");
			Properties p = new Properties();
			p.load(inputStream);
			p.put(key, value);
			FileOutputStream output = new FileOutputStream("d:/db.properties");
			p.store(output, null);
			inputStream.close();
			output.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			com.jupiter.WriteLog.writeFile("�޷�����");
			System.exit(0);
		}
	}
	
	public static void main(String args[]) throws Exception {
		InitProperty ip = new InitProperty();
		System.out.println("url:" + ip.url);
		System.out.println("dbuser:" + ip.db_user);
		System.out.println("dbpassword:" + ip.db_password);
		ip.setProperty("test", "ccc");
	}

	public void setDBPassword(String db_pwd) {
		InitProperty.db_password = db_pwd;
	}
	
	public void setLoginPassword(String db_pwd) {
		InitProperty.login_password = db_pwd;
	}

	public String getLoginPassword() {
		return login_password;
	}

}
