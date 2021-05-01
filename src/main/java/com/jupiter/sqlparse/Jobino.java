package com.jupiter.sqlparse;

public class Jobino {
	public String stype = ""; //调度类型
	public String joblocate = ""; //程序所在目录
	public String jobname = ""; //名称
	public String jobtype = ""; //类型
	public String serverName = ""; //SND
	public String ino = ""; //INO
	public String schema = ""; //schema
	public String inofile = ""; //inofile
	public String subSQLName = null;
	public String aliasName = null;

	public Jobino(String stype, String jobname, String ino, String serverName, String schema, String inofile) {
		this.stype = stype;
		this.jobname = jobname;
		this.ino = ino;
		this.serverName = serverName;
		this.schema = schema;
		this.inofile = inofile;
		checkProperties();
	}

	public Jobino(String stype, String jobname, String ino, String serverName, String schema, String inofile,String subSQLName,String aliasName) {
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
	
	public Jobino(String ino, String schema, String inofile) {
		super();
		this.ino = ino;
		this.schema = schema;
		this.inofile = inofile;
		checkProperties();	
	}

	public void checkProperties(){
		if(this.stype==null)this.jobtype="";
		if(this.jobname==null)this.jobname="";
		if(this.ino==null)this.ino="";
		if(this.serverName==null)this.serverName="";
		if(this.schema==null)this.schema="";
		if(this.inofile==null)this.inofile="";
		if(this.schema.contains("/")||this.inofile.contains("/")){
			if(this.schema.contains("#")||this.inofile.contains("#")){
				String t = (schema=="" ? "" : schema+"." )+ inofile;
				this.schema = t.substring(0,t.lastIndexOf("/"));
				this.inofile = t.substring(t.lastIndexOf("/"));
			}else{
				String t = (schema=="" ? "" : schema+"." )+ inofile;
				this.schema = t.substring(0,t.lastIndexOf("/")+1);
				this.inofile = t.substring(t.lastIndexOf("/")+1);
			}
		}
	}
	
	public String toString() {
		String s = null;
		s = "(" + this.stype + "," + jobname + "," + ino + "," + serverName + "," + schema + "," + inofile + ")\n";
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
		Jobino p = (Jobino) obj;
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
