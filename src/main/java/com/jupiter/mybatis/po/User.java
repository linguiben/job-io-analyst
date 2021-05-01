package com.jupiter.mybatis.po;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

	private int id = 0;
	private String username="";
	private String password="";
	private Date optionTime;
	private String remark="";
	private double version;
	
	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public User(String username,String password) {
		this.username = username;
		this.password = password;
	}
	
	public User() {
		
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getUserID() {
		return username;
	}

	public void setUserID(String userID) {
		this.username = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getOptionTime() {
		return optionTime;
	}

	public void setOptionTime(Date optionTime) {
		this.optionTime = optionTime;
	}

	
    @Override
    public String toString() { // 为了方便查看结果，重写了toString方法
        return ("Jobio_User:[ID=" + id + ",USERNAME=" + username) + (remark=="" ? "," : ",REMARK=" + remark +"]");
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		User user = new User();
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		//Date date = df.format(new Date());
		user.setOptionTime(new Date());
		System.out.println("UserID:" + user.id);
		System.out.println("optionTime:" + df.format(user.optionTime));
	}

}
