package com.jupiter.test;

import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jupiter.mybatis.dao.DBUnit;

public class TestEtlJobIno {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        String xmlPath = "applicationContext.xml";
        applicationContext = new ClassPathXmlApplicationContext(xmlPath);
        DBUnit dbUnit = (DBUnit) applicationContext.getBean("dbUtil");
        List<String> jobnames = dbUnit.CheckExistsJobname("'cdata_down_1212','ams_down'");
        
        for(String jobname : jobnames) {
        	System.out.println("job : " + jobname);
        }
        JOptionPane.showMessageDialog(null, "okok");
    }
}