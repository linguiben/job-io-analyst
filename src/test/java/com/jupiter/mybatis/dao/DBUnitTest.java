package com.jupiter.mybatis.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Jupiter.Lin
 * @desc TODO
 * @date 2023-09-24 01:22
 */
class DBUnitTest {

    ApplicationContext applicationContext;
    DBUnit dbUnit;

    @BeforeEach
    void setUp(){
        String xmlPath = "applicationContext.xml";
        applicationContext = new ClassPathXmlApplicationContext(xmlPath);
        dbUnit = applicationContext.getBean(DBUnit.class);
    }

    @Test
    void checkUser() {
    }

    @Test
    void checkExistsJobname() {
    }

    @Test
    void checkFilenameEixts() {
    }

    @Test
    void checkNameEixts() {
    }

    @Test
    void getLastVersion() {
    }

    @Test
    void isVersionUseable() {
		System.out.println(dbUnit.getLastVersion());
        assertFalse(dbUnit.isVersionUseable(0.0));
    }
}