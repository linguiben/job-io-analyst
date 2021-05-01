
package com.jupiter.sqlparse;



/**
* @Description:
* @author: Jupiter.Lin
* @version: V1.0 
* @date: 2020年9月24日 上午10:27:00 
*/
public class JobInfo {
    public String stype = ""; //调度类型
    public String jobtype = ""; //类型
    public String joblocate = ""; //程序所在目录
    public String jobname = ""; //名称
    public String aliasName = null;
    
    
    
    /**
    * Creates a new instance of JobInfo.
    *
    * @param stype2
    * @param jobType2
    * @param joblocate2
    * @param jobname2
    */
    	
    public JobInfo() {
    }
            
    public JobInfo(String stype, String jobType, String joblocate, String jobname) {
        this.stype = stype;
        this.jobtype = jobType;
        this.joblocate = joblocate;
        this.jobname = jobname;
    }



    public String toString() {
        String s = null;
        s = "(" + this.stype + "," + joblocate+ "," + jobname + ")\n";
        return s;
    }
}

	