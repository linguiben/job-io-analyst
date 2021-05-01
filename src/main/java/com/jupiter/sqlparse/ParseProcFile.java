
package com.jupiter.sqlparse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jupiter.WriteLog;
import com.jupiter.mybatis.dao.DBUtil;

import oracle.sql.CLOB;

/**
* @Description:
* @author: Jupiter.Lin
* @version: V1.0 
* @date: 2020��9��23�� ����5:34:05 
*/
public class ParseProcFile {
  
    Logger logger = Logger.getLogger("src.com.SQLParse.ParseProcFile");
    private static String logFile = "jobino.log";
    //private static String path = "D:\\Jupiter\\workPlace\\001.����\\012.dsjob_analysis\\10.proc";
    
    /**��ȡ·��������sql�ļ�
     * stype.dbname.schema.procname.txt
    * @stype ϵͳ����
    * @dbname ���ݿ�����
    * @schema ��schema
    * @procname 
    * @author CHN0007482
    * @since 2020-10-10
     */
    public List<File> getFiles(String path){
        File root = new File(path);
        List<File> files = new ArrayList<File>();
        if(!root.isDirectory()){
            files.add(root);
        }else{
            File[] subFiles = root.listFiles();
            for(File f : subFiles){
                files.addAll(getFiles(f.getAbsolutePath()));
            }
        }
        return files;
    }
    
        /**����sql�ļ� ��д�� etl_jobino_tmp
        * parse-Desc:
        * @param files     
        * void 
        * @author CHN0007482
        * @since 2020-10-10
         */
    private void parse(List<File> files) {
        WriteLog.writeFile(logFile, files.toString());
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String fileName = file.getName();
            logger.info("��ʼ����: " + fileName);
            List<String> strs = Arrays.asList(fileName.split("\\."));
            ListIterator<String> it = strs.listIterator();
            while (it.hasNext())//����
                it.next();
            String fix = it.hasPrevious() ? it.previous() : "";
            if (!fix.equalsIgnoreCase("SQL")) {
                return;// ��sql�ļ�,�˳�
            }
            String jobname = it.hasPrevious() ? it.previous() : "";
            String jobtype = it.hasPrevious() ? it.previous() : "";
            //String schema = it.hasPrevious() ? it.previous() : "";
            String locate = it.hasPrevious() ? it.previous() : "";
            String stype = it.hasPrevious() ? it.previous() : "";
            String sql = "";
            try {
                InputStreamReader in = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader br = new BufferedReader(in);
                String line = br.readLine();
                while (line != null) {
                    sql += line + "\n";
                    line = br.readLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ParseSQL parseSQL = new ParseSQL();
            parseSQL.parseSQLs(sql, "", new JobInfo(stype, jobtype, locate, jobname));
            HashSet<JobInputOutput> jobinoSet = new HashSet<JobInputOutput>();
            jobinoSet.addAll(parseSQL.getJobinoSet());
            // WriteLog.writeFile(logFile, "jobinoSet:" + jobinoSet.toString());
            ParseJobio.writeJobIo(jobinoSet);
            // System.out.println(sql);
        }
    }
    
     
     /**
      * Oracle��Clobת��String
      * @param clob
      * @return
      */
     public String oracleClobToString(CLOB clob){
         try {
             return (clob == null ? null : clob.getSubString(1, (int)clob.length()));
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return null;
     }
     
     /**�����ݿ��е���SQL�ļ�
     * exportSQLFile-Desc:
     * @param dbname
     * @param path     
     * void 
     * @author CHN0007482
     * @since 2020-10-10
      */
    public void exportSQLFile(String dbname, String path) {
        String schema = "UDA";
        String sql = "select OBJECT_TYPE,OBJECT_NAME from user_objects where OBJECT_TYPE in('FUNCTION','PROCEDURE','VIEW')";
        List<Map<String,Object>> list = DBUtil.getInstance().excute(dbname, sql);
        for(int i=0;i<list.size();i++) {
            Map<?, ?> map = list.get(i);
            String OBJECT_NAME = (String) map.get("OBJECT_NAME");
            String OBJECT_TYPE = (String) map.get("OBJECT_TYPE");
            List<Map<String, Object>> list2 = DBUtil.getInstance().excute("adpp", "select dbms_metadata.get_ddl('"+OBJECT_TYPE+"','"+OBJECT_NAME+"','"+schema+"') as ddl from dual");
            Map<?, ?> map2 = list2.get(0);
            String ddl = oracleClobToString((CLOB)map2.get("DDL"));
            //stype.dbname.schema.procname.sql
            String fileName = "ADPP."+dbname.toUpperCase()+"."+schema+"."+OBJECT_TYPE+"."+OBJECT_NAME+".SQL";
            //System.out.println("DDL: " + ddl);
            WriteLog.writeCSV(path+"\\"+fileName, ddl);
        }
    }
    
    /**
     * main-Desc:
     * @param args     
     * void 
     * @author CHN0007482
     * @since 2020-09-23
     */
     public static void main(String[] args) {
         ParseProcFile p = new ParseProcFile();
//         //p.exportSQLFile("adpp", path); //����SQL�ļ�,��Ҫ���ֶ����
//         List<File> files = p.getFiles(path);  //��ȡsql�ļ�
//         p.parse(files);//����sql�ļ�
         p.logger.info("aaa");
     }
}

	