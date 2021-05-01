
package com.jupiter.sqlparse;



/**
* @Description:
* @author: Jupiter.Lin
* @version: V1.0 
* @date: 2020��9��24�� ����10:27:00 
*/
public class JobInfo {
    public String stype = ""; //��������
    public String jobtype = ""; //����
    public String joblocate = ""; //��������Ŀ¼
    public String jobname = ""; //����
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

	