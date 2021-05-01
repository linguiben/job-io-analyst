
package com.jupiter.mybatis.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
* @Description:
* @author: Jupiter.Lin
* @version: V1.0 
* @date: 2020年10月10日 上午11:30:20 
*/
public class DBUtil {
    
    private static DBUtil dbutil;
    private ComboPooledDataSource dsAdpp;
    
    
    private DBUtil(){}; //禁止外部创建实例
    public static DBUtil getInstance() {
        if(dbutil == null) {
            dbutil = new DBUtil();
        }
        return dbutil;
    }
    
    //执行SQL，返回List<Map<String, Object>>
    public List<Map<String, Object>> excute(String dbname,String sql) {
        //String sql = "select sysdate from dual ";
        switch (dbname.toLowerCase()) {
        case "adpp":
            if (dsAdpp == null) {//创建c3p0连接池
                dsAdpp = new ComboPooledDataSource("adpp"); 
            }
            break;
        case "db2":
            break;
        case "mysql":
            break;
        default:
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = null;
        try {
            conn = dsAdpp.getConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            list = convertList(rs);
        } catch (SQLException e) {
            		e.printStackTrace();
        }finally{
            try {
                //rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                		e.printStackTrace();
            }
        }
        return list;
    }
    
    //ResultSet转List
    public List<Map<String, Object>> convertList(ResultSet rs) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rowData = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
        }
    }
        return list;
    }
    
    //test
    public static void main(String[] args) throws SQLException {
        DBUtil dbUtil = DBUtil.getInstance();
        List<Map<String, Object>> list = dbUtil.excute("adpp", "select sysdate,systimestamp,100 aa from dual");
        System.out.println(list);
        Map<String, Object> map = list.get(0);
        System.out.println(map.get("SYSDATE"));
    }
}

	