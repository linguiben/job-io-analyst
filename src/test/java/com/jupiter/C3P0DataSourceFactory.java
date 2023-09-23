package com.jupiter;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
* @Description:
* @author: Jupiter.Lin
* @version: V1.0 
* @date: 2021年4月25日 下午7:12:02
*/
public class C3P0DataSourceFactory extends UnpooledDataSourceFactory {

    static Logger logger = Logger.getLogger(C3P0DataSourceFactory.class);
    
    public C3P0DataSourceFactory() {
        this.dataSource = new ComboPooledDataSource();
    }
    
    public static void main(String[] args) {
        SqlSessionFactory factory = null;
        SqlSession sqlSession = null;
        String resource = "mybatis-configC3P0.xml";
        InputStream is;
        try {
            is = Resources.getResourceAsStream(resource);
            factory = new SqlSessionFactoryBuilder().build(is);
             sqlSession = factory.openSession(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(factory);
        //Role role = null;
        //2.用 Mapper 接口发送 SQL
        //RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
        //role = roleMapper.getRoleById(1L); //返回2条则报错
        logger.info("");
        
        sqlSession.close();
    }
}
