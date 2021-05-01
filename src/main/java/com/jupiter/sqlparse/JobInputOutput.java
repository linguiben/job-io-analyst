package com.jupiter.sqlparse;

public class JobInputOutput extends JobInfo{
	public String serverName = ""; //SND
	public String ino = ""; //INO
	public String schema = ""; //schema
	public String inofile = ""; //inofile
	public String subSQLName = null;

	public JobInputOutput(String stype, String jobname, String ino, String serverName, String schema, String inofile) {
		this.stype = stype;
		this.jobname = jobname;
		this.ino = ino;
		this.serverName = serverName;
		this.schema = schema;
		this.inofile = inofile;
		checkProperties();
	}
	
	public JobInputOutput(JobInfo job, String ino, String serverName, String schema, String inofile) {
        this.stype = job.stype;
        this.jobtype = job.jobtype;
        this.joblocate = job.joblocate;
        this.jobname = job.jobname;
        this.ino = ino;
        this.serverName = serverName;
        this.schema = schema;
        this.inofile = inofile;
        checkProperties();
    }

	public JobInputOutput(String stype, String jobname, String ino, String serverName, String schema, String inofile,String subSQLName,String aliasName) {
		this.stype = stype;
		this.jobname = jobname;
		this.ino = ino;
		this.serverName = serverName;
		this.schema = schema;
		this.inofile = inofile;
		this.subSQLName = subSQLName;
		this.aliasName = aliasName;
		checkProperties();
	}
	
	public JobInputOutput(JobInfo job, String ino, String serverName, String schema, String inofile,String subSQLName,String aliasName) {
        this.stype = job.stype;
        this.jobtype = job.jobtype;
        this.joblocate = job.joblocate;
        this.jobname = job.jobname;
        this.ino = ino;
        this.serverName = serverName;
        this.schema = schema;
        this.inofile = inofile;
        this.subSQLName = subSQLName;
        this.aliasName = aliasName;
        checkProperties();
    }
	
	public JobInputOutput(String ino, String schema, String inofile) {
		super();
		this.ino = ino;
		this.schema = schema;
		this.inofile = inofile;
		checkProperties();	
	}

	//dblink.zbxods.dbo.tabname ， 未考虑 schema.tab@citicpru.xxx.xxx
	public JobInputOutput (JobInfo job, String ino,String serverName, String inofile) {
	    this.stype = job.stype;
        this.jobtype = job.jobtype;
        this.joblocate = job.joblocate;
        this.jobname = job.jobname;
        this.ino = ino;
        String[] strs = inofile.split("\\.");
        if(strs.length == 4) {
            this.serverName = (serverName == "" ? "" : serverName+".") + strs[0]+"."+strs[1];
            this.schema = strs[2];
            this.inofile = strs[3];
        }else if(strs.length == 3) {
            this.serverName = (serverName == "" ? "" : serverName+".") + strs[0];
            this.schema = strs[1];
            this.inofile = strs[2];
        }else if(strs.length == 2) {
            this.schema = strs[0];
            this.inofile = strs[1];
        }else if(strs.length == 1) {
            this.inofile = strs[0];
        }
        checkProperties();
    }
	
	public void checkProperties(){
		if(this.stype==null)this.jobtype="";
		if(this.jobname==null)this.jobname="";
		if(this.ino==null)this.ino="";
		if(this.serverName==null)this.serverName="";
		if(this.schema==null)this.schema="";
		if(this.inofile==null)this.inofile="";
		//处理文件类型
        if (this.schema.contains("/") || this.inofile.contains("/")) {
            if (this.schema.contains("#") || this.inofile.contains("#")) {
                String t = (schema == "" ? "" : schema + ".") + inofile;
                this.schema = t.substring(0, t.lastIndexOf("/"));
                this.inofile = t.substring(t.lastIndexOf("/"));
            } else {
                String t = (schema == "" ? "" : schema + ".") + inofile;
                this.schema = t.substring(0, t.lastIndexOf("/") + 1);
                this.inofile = t.substring(t.lastIndexOf("/") + 1);
            }
        } //处理dblink,例如：sdata.crtable_mapp@dbl_finods.prd.xincheng.china jb_e02_grp_fin_m_fact_inc
        else if (this.schema.contains("@") || this.inofile.contains("@")) {
            if (this.schema.contains("#") || this.inofile.contains("#")) {
                String t = (schema == "" ? "" : schema + ".") + inofile;
                this.schema = t.substring(0, t.indexOf("."));
                this.inofile = t.substring(t.indexOf(".")+1);
            } else {
                String t = (schema == "" ? "" : schema + ".") + inofile;
                this.schema = t.substring(0, t.indexOf("."));
                this.inofile = t.substring(t.indexOf(".") + 1);
            }
        }
	}
	
	public String toString() {
		String s = null;
		s = "(" + this.stype + "," + joblocate+ "," + jobname + "," + ino + "," + serverName + "," + schema + "," + inofile + ")\n";
		//s = "(" + this.stype + "," + jobname + "," + ino + "," + serverName + "," + schema + (schema==""?"":".") + inofile + "," + aliasName + ")\n";
		return s;
	}

	@Override
	public int hashCode() {
        return this.inofile.toUpperCase().hashCode();
    }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobInputOutput p = (JobInputOutput) obj;
		if (!this.ino.equalsIgnoreCase(p.ino))
			return false;
		if (!this.schema.equalsIgnoreCase(p.schema))
			return false;
		if (!this.stype.equalsIgnoreCase(p.stype))
			return false;
		if (!this.joblocate.equalsIgnoreCase(p.joblocate))
			return false;
		if (!this.jobname.equalsIgnoreCase(p.jobname))
			return false;
		if (!this.jobtype.equalsIgnoreCase(p.jobtype))
			return false;
		if (!this.serverName.equalsIgnoreCase(p.serverName))
			return false;
		/*if (!this.inofile.equalsIgnoreCase(p.inofile))
			return false;*/
		return true;
	}
}
