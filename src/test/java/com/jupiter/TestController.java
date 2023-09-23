package com.jupiter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jupiter.mybatis.dao.UserController;

public class TestController {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        String xmlPath = "applicationContext.xml";
        applicationContext = new ClassPathXmlApplicationContext(xmlPath);
        UserController uc = (UserController) applicationContext.getBean("userController");
        uc.test();
    }
}