/**
 * 
 */
package com.jupiter.mybatis.dao;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.jupiter.mybatis.mapper.UserMapper;
import com.jupiter.mybatis.po.User;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @Description:
 * @author: Jupiter
 * @date @time
 */
public class C3P0DataSourceFactory extends UnpooledDataSourceFactory {

    Logger logger = Logger.getLogger(C3P0DataSourceFactory.class);
    
    public C3P0DataSourceFactory() {
        this.dataSource = new ComboPooledDataSource();
    }
    
    public static void main(String[] args) {
        SqlSessionFactory factory = null;
        SqlSession sqlSession = null;
        String resource = "mybatis-config.xml";
        InputStream is;
        try {
            is = Resources.getResourceAsStream(resource);
            factory = new SqlSessionFactoryBuilder().build(is);
             sqlSession = factory.openSession(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(factory);
        User user = null;
        //2.用 Mapper 接口发送 SQL
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        user = userMapper.selectUserById(1);
        System.out.println(user);
        
        sqlSession.close();
    }
}
