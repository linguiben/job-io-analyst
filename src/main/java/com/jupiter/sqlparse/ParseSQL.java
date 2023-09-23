package com.jupiter.sqlparse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jupiter.WriteLog;

public class ParseSQL {

	//private static ComboPooledDataSource ds = new ComboPooledDataSource();
	private List<String> anotherKeyWords = Arrays.asList("WHERE","GROUP","ORDER","UNION","INTERSECT","EXCEPT","MINUS");//另外一句SQL
	private List<String> SQLKeyWords = Arrays.asList("SELECT","AS","WITH","WHERE","ON","GROUP","ORDER","UNION","INTERSECT","EXCEPT","MINUS","LEFT","RIGHT","FULL","CROSS","JOIN",",","(",")");
	private List<String> subSQLAlias = Arrays.asList("__brackets__","__subquery__","/dev/null","dev/null");
	private String substr = "__(brackets|subquery)\\d+__";
	private HashSet<JobInputOutput> jobinoSet = new HashSet<JobInputOutput>();
	
	/**
	 * 解析SQLS(以;分隔)
	 */
	public void parseSQLs(String sqls, String serverName,JobInfo job) {
	    jobinoSet.clear();
        if (sqls == null || sqls.equals(""))
            return;
        sqls = SQLStrFormat.removeAnnotation(sqls);
        sqls = sqls.replaceAll("';'", "''");
        String[] sql = sqls.split(";");
        for (int i = 0; i < sql.length; i++) {
            parseSQL(sql[i],serverName,job);
        }
	}
	
	public void parseSQLs(String sqls) {
		parseSQLs(sqls,"",new JobInfo());
	}
	
	/**
	 * @param ssql 解析单句SQL
	 * @param serverName sername
	 * @param job  etl job
	 */
	public void parseSQL(String ssql, String serverName,JobInfo job){ //String stype,String joblocate, String jobname, 
		String sql = SQLStrFormat.format(ssql);
		String SQLType = SQLStrFormat.getSQLType(sql);
		if(SQLType.equalsIgnoreCase("SELECT"))
			parseSelectS(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("INSERT"))
			parseInsert(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("DELETE"))
			parseDelete(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("UPDATE"))
			parseUpdate(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("TRUNCATE")||SQLType.equalsIgnoreCase("ALTER")||SQLType.equalsIgnoreCase("RUNSTATS")||SQLType.equalsIgnoreCase("REFRESH"))
			parseAlter(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("MERGE"))
			parseMerge(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("CALL")||SQLType.equalsIgnoreCase("EXEC")||SQLType.equalsIgnoreCase("EXECUTE"))
			parseCall(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("CREATE")||SQLType.equalsIgnoreCase("DROP"))
			parseCreate(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("WITH"))
			parseWith(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("EXPORT"))
			parseExport(sql,serverName,job);
		else if(SQLType.equalsIgnoreCase("IMPORT") || SQLType.equalsIgnoreCase("LOAD"))
			parseImport(sql, serverName,job);
		else if(SQLType.equalsIgnoreCase("WRITEFILE"))
            parseWriteFile(ssql, serverName,job);
		else if(SQLType.equalsIgnoreCase("READFILE"))
            parseReadFile(ssql, serverName,job);
		else if(SQLType.equalsIgnoreCase("")||SQLType.equalsIgnoreCase("COMMIT")||SQLType.equalsIgnoreCase("ROLLBACK")||SQLType.equalsIgnoreCase("END"))
            ;
        else if (SQLType.equalsIgnoreCase("BEGIN"))
            parseSQL(sql.substring(sql.indexOf(" ")), serverName,job);
		else //JOptionPane.showMessageDialog(null, SQLType +" : "+sql+"\n","不支持:",0);
			WriteLog.writeFile(ParseJobio.getLogFile(),"不支持: "+sql);
	}
	
	public void parseSQL(String sql){
		parseSQL(sql,"",new JobInfo());
	}

    private void parseWith(String sql, String serverName,JobInfo job) {
	    parseWith(sql,new ArrayList<String>(),serverName,job);
	}

	private List<String> parseWith(String sql,List<String> alias,  String serverName,JobInfo job) {
        //HashSet<String> wi = new HashSet<String>();
        //List<String> alias = new ArrayList<String>();
        //ArrayList<String> sqls = SQLStrFormat.getSubSQLStr(sql);
        /*
         * String stype = job.stype; String joblocate = job.joblocate; String jobname =
         * job.jobname;
         */
        Map<String,String> sqls = SQLStrFormat.getSubSQLStr(sql,1);
        Set<String> ks = sqls.keySet();
        String isql = sqls.get("__main__");
        ks.remove("__main__");
        alias.addAll(ks);
        alias.addAll(subSQLAlias);
        //if (sqls.size() > 0) {
            List<String> strs = SQLStrFormat.split(isql);
            Iterator<String> it = strs.iterator();
            while (it.hasNext()) {
                String str = it.next();
                if (str.equalsIgnoreCase("WITH")) {
                    it.remove();
                    str = it.next();
                    alias.add(str);
                } else if (str.equalsIgnoreCase(",")) {
                    it.remove();
                    str = it.next();
                    alias.add(str);
                } else if (str.equalsIgnoreCase("SELECT")) {
                    parseSingleSelect(strs, alias, serverName, job);
                    break;
                }
                it.remove();
            }
        //}

        for (String i : ks) {
            // System.out.println(sqls.get(i));
            parseSingleSelect(SQLStrFormat.split(sqls.get(i)), alias,serverName,job);
        }
        return alias;
    }
	
	public void setJobinoSet(HashSet<JobInputOutput> jobinoSet) {
		this.jobinoSet = jobinoSet;
	}

	/**
	 * 解析SELECT,含子查询
	 * @param serverName 
	 * @param jobname 
	 * @param stype 
	 * @param args
	 */
	public void parseSelectS(String str, String stype, String jobname, String serverName){
	    parseSelectS(str,serverName,new JobInfo(stype,"","",jobname));
	}
	public void parseSelectS(String sqlStr, String serverName,JobInfo job){
        /*
         * String stype = job.stype; String joblocate = job.joblocate; String jobname =
         * job.jobname;
         */
		Map<String,String> sqls = SQLStrFormat.getSubSQLStr(sqlStr,1);
		Set<String> keySet = sqls.keySet();
		List<String> alias = new ArrayList<String>();
		alias.addAll(keySet);
		alias.addAll(subSQLAlias);
		for(String key : keySet){
			//parseSelect(SQLStrFormat.split(sqls.get(key)),ss,stype,jobname,serverName);
		    String sql = sqls.get(key);
		    List<String> strs = SQLStrFormat.split(sql);
		    String str1 = strs.get(0);
		    if(str1.equalsIgnoreCase("WITH")) {
		        parseWith(sql,alias,serverName,job);
		    }else {
		        parseSingleSelect(strs,alias,serverName,job);  
		    }
		}
	}
	
	public void parseSelectS(String str){
		parseSelectS(str,"","","");
	}
	

	/**
	 * @param stype 增加subSQLName aliasName
	 */
	public void parseSelect(ArrayList<String> l,List<String> wi, String stype, String jobname, String serverName) {
	    parseSingleSelect(l,wi,serverName,new JobInfo(stype,"","",jobname));
	}

    public void parseSelect(ArrayList<String> strs, List<String> wi, String serverName,JobInfo job) {
        /*
         * String stype = job.stype; String jobtype = job.jobtype; String joblocate =
         * job.joblocate; String jobname = job.jobname;
         */
        boolean from = false; // 是否找到from
        Iterator<String> iter = strs.iterator();
        String str = "";
        String ino = "in", schema = "", inofile = "";
        while (iter.hasNext()) {
            // 遇到重置标记
            if (anotherKeyWords.contains(str.toUpperCase())) {
                from = false;
            }
            // 1.分析from后面的值
            if (!from) {
                str = iter.next();
                if (str.equalsIgnoreCase("FROM")) {
                    from = true;
                    str = iter.hasNext() ? iter.next() : "";
                    String t = iter.hasNext() ? iter.next() : "";
                    schema = "";
                    while (t.equals(".")) {
                        if (!schema.isEmpty())
                            schema += ".";
                        schema += str;
                        str = iter.hasNext() ? iter.next() : "";
                        if(!str.equals("."))
                            t = iter.hasNext() ? iter.next() : "";
                    }
                    inofile = str;
                    str = t;
                    /*
                     * if (t.equals(".")) { schema = str; inofile = iter.hasNext() ? iter.next() :
                     * ""; str = iter.hasNext() ? iter.next() : ""; } else { schema = ""; inofile =
                     * str; str = t; }
                     */
                    if (!wi.contains(inofile)) {
                        str = str.equalsIgnoreCase("AS") ? (iter.hasNext() ? iter.next() : "") : str;
                        String alias = SQLKeyWords.contains(str) ? null : str;
                        jobinoSet.add(
                                new JobInputOutput(job,ino, serverName, schema, inofile, "", alias));
                    }
                    // System.out.println("tabnamef: " + tb);
                }
            } else {
                // 2.分析join
                if (str.equalsIgnoreCase("JOIN")) {
                    str = iter.hasNext() ? iter.next() : "";
                    String t = iter.hasNext() ? iter.next() : "";
                    schema = "";
                    if (t.equalsIgnoreCase(".")) {
                        schema = str;
                        inofile = iter.next();
                        str = iter.hasNext() ? iter.next() : "";
                    } else {
                        schema = "";
                        inofile = str;
                        str = t;
                    }
                    if (!wi.contains(inofile)) {
                        str = str.equalsIgnoreCase("AS") ? (iter.hasNext() ? iter.next() : "") : str;
                        String alias = SQLKeyWords.contains(str) ? null : str;
                        jobinoSet.add(
                                new JobInputOutput(job, ino, serverName, schema, inofile, "", alias));
                    }
                    // System.out.println("tabnamej: " + str);
                    int left = 0, right = 0;
                    while (iter.hasNext()) {
                        str = iter.hasNext() ? iter.next() : "";
                        // System.out.println(str1);
                        if (anotherKeyWords.contains(str.toUpperCase()) || str.equalsIgnoreCase("JOIN")) {
                            break;
                        } else if (str.equals("(")) {
                            left++;
                        } else if (str.equals(")")) {
                            right++;
                        } else if (str.equals(",") && left == right) {
                            break;
                        }
                    }
                } else if (str.equalsIgnoreCase(",")) {
                    // 3.分析笛卡儿积
                    str = iter.hasNext() ? iter.next() : "";
                    String t = iter.hasNext() ? iter.next() : "";
                    schema = "";
                    while (t.equals(".")) {
                        if (!schema.isEmpty())
                            schema += ".";
                        schema += str;
                        str = iter.hasNext() ? iter.next() : "";
                        if(!str.equals("."))
                            t = iter.hasNext() ? iter.next() : "";
                    }
                    inofile = str;
                    str = t;
                    if (!wi.contains(inofile)) {
                        str = str.equalsIgnoreCase("AS") ? (iter.hasNext() ? iter.next() : "") : str;
                        String alias = SQLKeyWords.contains(str) ? null : str;
                        jobinoSet.add(
                                new JobInputOutput(job, ino, serverName, schema, inofile, "", alias));
                    }
                    // System.out.println("tabname,: " + str);
                } else {
                    str = iter.hasNext() ? iter.next() : "";
                }
            }
        }
    }
	
	public void parseSelect(ArrayList<String> l, String stype, String jobname, String serverName) {
		//List<String> wi = Arrays.asList("__brackets__","__subquery__");
		parseSelect(l,subSQLAlias,stype,jobname,serverName);
	}
	
	public void parseSelect(ArrayList<String> l) {
		parseSelect(l,"","","");
	}

	/**
     * @param args 解析单个SELECT (不含子查询)
     * @param serverName 
     * @param jobname 
     * @param stype 
     */
	public void parseSingleSelect(List<String> strs, List<String> aliasNames, String serverName, JobInfo job) {
        boolean select = false; // 是否找到select
        boolean from = false; // 是否找到from
        Iterator<String> iter = strs.iterator();
        @SuppressWarnings("unused")
        String str = "",ino = "in", schema = "", inofile = "";
        while (iter.hasNext()) {
            str = iter.hasNext() ? iter.next() : "";
            if (anotherKeyWords.contains(str.toUpperCase())) {
                select = false;// 遇到重置标记
                from = false;
            } else if (str.equalsIgnoreCase("SELECT")) {
                select = true;// 发现select
            } else if (select && str.equalsIgnoreCase("FROM")) {
                from = true;// 发现from
                inofile = iter.hasNext() ? iter.next() : "";
                if (!inofile.matches(substr) & !aliasNames.contains(inofile))
                    jobinoSet.add(new JobInputOutput(job, ino,serverName, inofile));
            } else if (select & from &&(str.equalsIgnoreCase("JOIN") || str.equals(","))) {
                    // 分析join 和 ","
                    inofile = iter.hasNext() ? iter.next() : "";
                    if (!inofile.matches(substr) & !aliasNames.contains(inofile))
                        jobinoSet.add(new JobInputOutput(job, ino,serverName, inofile));
            }
            /* 只考虑from的方式：
            // 1.未找到From，先找From
            if (!from) {
                if (str.equalsIgnoreCase("FROM")) {
                    from = true;
                    inofile = iter.hasNext() ? iter.next() : "";
                    if (!inofile.matches(substr))
                        jobinoSet.add(new JobInputOutput(job, ino, inofile));
                }
                // 2.已找到From，分析join 和 ","
            } else if (str.equalsIgnoreCase("JOIN") || str.equals(",")) {
                inofile = iter.hasNext() ? iter.next() : "";
                if (!inofile.matches(substr))
                    jobinoSet.add(new JobInputOutput(job, ino, inofile));
            }*/
        }
    }
	
    private void parseCreate(String sql,  String serverName,JobInfo job) {
        /*
         * String stype = job.stype; String joblocate = job.joblocate; String jobname =
         * job.jobname;
         */
		List<String> strs = SQLStrFormat.split(sql);
		Iterator<String> it = strs.iterator();
		String str = "", ino = "out", schema = "", inofile = "";
		str = it.next();
		if (str.equalsIgnoreCase("CREATE")||str.equalsIgnoreCase("DROP")) {
			while (it.hasNext()) {
				str = it.next();
				if(str.equalsIgnoreCase("TABLE")) {
					str = it.next();
					String t = it.hasNext() ? it.next() : "";
					if (t.equalsIgnoreCase(".")) {
						schema = str;
						inofile = it.next();
					}else{
						schema = "";
						inofile = str;
					}
					str = t;
					/*if (it.hasNext() && (it.next()).equals(".")) {
						schema = str;
						inofile = (String) it.next();
						str = schema + "." + inofile;
					}else{
						schema = "";
						inofile = str;
					}*/
					//System.out.println(str);
					if (!inofile.matches(substr))
						jobinoSet.add(new JobInputOutput(job,"out", serverName,schema, inofile));
					break;
				}else if(str.equalsIgnoreCase("INDEX")) {
					while (it.hasNext()) {
						str =  it.next();
						if (str.equalsIgnoreCase("ON")) {
							str =  it.next();
							String t = it.hasNext() ? it.next() : "";
							if (t.equalsIgnoreCase(".")) {
								schema = str;
								inofile = it.next();
							}else{
								schema = "";
								inofile = str;
							}
							str = t;
							if (!inofile.equals("__brackets__") && !inofile.equals("__subquery__"))
								jobinoSet.add(new JobInputOutput(job, ino, serverName, schema, inofile));
						}
					}
				}
			}
		}
	}


    private void parseCall(String sql, String stype, String jobname, String serverName) {
        parseCall(sql, serverName,new JobInfo(stype, "", "",jobname));
    }
	private void parseCall(String sql,  String serverName,JobInfo job) {
		// sql = "CALL SYSPROC.ADMIN_CMD('import from /dev/null of del replace
		// into #v_jb_eser_schema#.GiveQuaNum_dwn');";
		List<String> strs = SQLStrFormat.split(sql);
		Iterator<String> it = strs.iterator();
		String str = "", schema = "";
		str = it.next();
		//.join(strs,"");
		// exec
		if (str.equalsIgnoreCase("EXECUTE")) {
		    it.remove();
		    str = it.hasNext() ? it.next() : "";
		    if(str.equalsIgnoreCase("IMMEDIATE")) {//EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CHECK_UL';
		        it.remove();
		        str = it.hasNext() ? it.next() : "";
		        if(str.substring(0, 1).equals("'")) {
		            str = str.substring(1,str.length()-1);
		        }
		        else if(str.substring(0, 1).equals("(")) {//EXECUTE IMMEDIATE (xxxx);
		        }
		        while(it.hasNext())
		            str += " " + it.next();
		        parseAlter(str, serverName,job);
		    }
		    else
		        jobinoSet.add(new JobInputOutput(job, "out", serverName, schema, sql.replace(" . ", ".").replace(" ( ", "(").replace(" ' ", "'")));
		    
			return;
		}
		if (str.equalsIgnoreCase("EXEC")) {
            jobinoSet.add(new JobInputOutput(job, "out", serverName, schema, sql.replace(" . ", ".").replace(" ( ", "(").replace(" ' ", "'")));
            return;
        }
		// call
		if (str.equalsIgnoreCase("CALL")) {
			str = it.next();
		}
		if (str.equalsIgnoreCase("SYSPROC")) {
			str = it.hasNext() ? it.next() : "";
			if (str.equals("."))
				str = it.hasNext() ? it.next() : "";
			if (str.equalsIgnoreCase("ADMIN_CMD")) {
				sql = sql.substring(sql.indexOf("'") + 1, sql.lastIndexOf("'"));
				parseSQL(sql, serverName,job);
			}
		}else{
		    boolean isSQLStatement = false;
		    while(str!="" && !isSQLStatement){
		        str = it.hasNext() ? it.next() : "";
		        isSQLStatement = SQLPattern.isSQLStatement(str);
		    }
		    //System.out.println(str);
		    if(isSQLStatement) {
		        str = str.substring(str.indexOf("'") + 1, str.lastIndexOf("'"));
		        parseSQL(str,serverName,job);
		        return;
		    }
			jobinoSet.add(new JobInputOutput(job, "out", serverName, schema, sql.replace(" . ", ".").replace(" ( ", "(").replace(" ' ", "'")));
		}
			/*else
			while (it.hasNext()) {
				str = it.next();
				if (str.equalsIgnoreCase("INTO") || str.equalsIgnoreCase("TABLE")) {
					str = it.next();
					if (it.hasNext() && (it.next()).equals(".")) {
						schema = str;
						inofile = it.next();
						str = schema + "." + inofile;
					} else {
						schema = "";
						inofile = str;
					}
					// System.out.println(str);
					if (!subSQLAlias.contains(inofile))
						jobinoSet.add(new Jobino(stype, jobname, ino, serverName, schema, inofile));
					break;
				}
			}*/
	}

    private void parseImport(String sql, String serverName,JobInfo job) {
		List<String> strs = SQLStrFormat.split(sql);
		Iterator<String> it = strs.iterator();
		String str = "", inofile = "";//, ino = "out"
		while (it.hasNext()) {
			str = it.hasNext() ? it.next() : "";
			if (str.equalsIgnoreCase("FROM")) {
				str = it.hasNext() ? it.next() : "";
				while(!str.equalsIgnoreCase("OF")){
					inofile += str;
					str = it.hasNext() ? it.next() : "";
				}
				str = it.hasNext() ? it.next() : "";
				if(str.equalsIgnoreCase("CURSOR")){
					this.parseSQL(sql.substring(sql.indexOf("("), sql.lastIndexOf(")")),serverName,job);
				}else{
					if(!subSQLAlias.contains(inofile))
						jobinoSet.add(new JobInputOutput(job, "in", serverName, inofile));
				}
				// System.out.println(str);
			}else if (str.equalsIgnoreCase("INTO") || str.equalsIgnoreCase("TABLE")) {
			    inofile = it.hasNext() ? it.next() : "";
				// System.out.println(str);
				if(!subSQLAlias.contains(inofile))
					jobinoSet.add(new JobInputOutput(job, "out", serverName, inofile));
				break;
			}
		}
	}

    private void parseExport(String sql,String serverName, JobInfo job) {
		List<String> strs = SQLStrFormat.split(sql);
		Iterator<String> it = strs.iterator();
		String str = "", ino = "out", inofile = "";
		while (it.hasNext()) {
			str = it.next();
			if (str.equalsIgnoreCase("TO")) {
				inofile = it.next();
				String t = it.hasNext() ? it.next() : "";
				while(it.hasNext() && !t.equalsIgnoreCase("OF")){
					inofile +=  t ;
					t = it.hasNext() ? it.next() : "";
				}
				// System.out.println(str);
				if(!subSQLAlias.contains(inofile))
					jobinoSet.add(new JobInputOutput(job, ino, serverName, inofile));
				sql = sql.substring(sql.toUpperCase().indexOf("SELECT"));
				parseSelectS(sql,serverName,job);
				break;
			}
		}
		
	}

	@SuppressWarnings("unused")
	private void parseCall(String sql) {
		parseCall(sql,"","","");
	}

    private void parseMerge(String sql, String stype, String jobname, String serverName) {
        parseMerge(sql, serverName,new JobInfo(stype, "","", jobname));
    }
	private void parseMerge(String sql, String serverName,JobInfo job) {
		Map<String, String> sqls = SQLStrFormat.getSubSQLStr(sql, 1);
		Set<String> ks = sqls.keySet();
		List<String> ss = new ArrayList<String>();
		ss.addAll(ks);
		ss.addAll(subSQLAlias);
		String mainSql = sqls.get("__main__");
		ks.remove("__main__");
		List<String> strs = SQLStrFormat.split(mainSql);
		Iterator<String> it = strs.iterator();
		String str = "", ino = "out", inofile = "";
		while (it.hasNext()) {
			str = it.next();
			//merge into 
			if (str.equalsIgnoreCase("MERGE")) {
				str = it.hasNext() ? it.next() : "";
				if (str.equalsIgnoreCase("INTO")) {
				    inofile = it.hasNext() ? it.next() : "";
					if (!inofile.matches(substr))
						jobinoSet.add(new JobInputOutput(job, ino, serverName, inofile));
				}
			}
			//using table
			if (str.equalsIgnoreCase("USING")) {
			    inofile = it.hasNext() ? it.next() : "";
				if(!ss.contains(inofile))
					jobinoSet.add(new JobInputOutput(job, "in", serverName, inofile));
				break;
			}
		}
		//using subquery
		for (String key : ks) {
			// System.out.println(key + ": " + sqls.get(key));
		    parseSingleSelect(SQLStrFormat.split(sqls.get(key)),ss,serverName,job);
		}
	}
	
	@SuppressWarnings("unused")
	private void parseMerge(String sql) {
		parseMerge(sql,"","","");
	}

	public void parseInsert(String sql, String stype, String jobname, String serverName) {
	    parseInsert(sql, serverName,new JobInfo(stype,"","",jobname));
	}
	public void parseInsert(String sql,String serverName,JobInfo job) {
		// sql = "delete from(select * from a where exists(select 1 from b))";
		String str = "", ino = "out", inofile = "";
		Map<String, String> sqls = SQLStrFormat.getSubSQLStr(sql, 1);
		ArrayList<String> ss = new ArrayList<String>();
		Set<String> ks = sqls.keySet();
		ss.addAll(ks);
		ss.addAll(subSQLAlias);
		String isql = sqls.get("__main__");
		ks.remove("__main__");
		// System.out.println("isql: " + isql);
		List<String> strs = SQLStrFormat.split(isql);
		Iterator<String> it = strs.iterator();
		//str = it.hasNext()?it.next():"";
		while (it.hasNext()) {
		        str = it.next();
		        it.remove();
			 if (str.equalsIgnoreCase("INTO")) {
				inofile = it.hasNext() ? it.next() : "";
				it.remove();			
                /*
                 * while(t.equals(".")) { if (!schema.isEmpty()) schema += "."; schema += str;
                 * str = it.hasNext() ? it.next() : ""; it.remove(); if(!str.equals(".")) { //t
                 * = it.hasNext() ? it.next() : ""; //it.remove(); inofile = str; break; } }
                 */
				if (!inofile.matches(substr))
					jobinoSet.add(new JobInputOutput(job, ino, serverName, inofile));
			}else if (str.equalsIgnoreCase("WITH")) {
			    str = it.hasNext() ? it.next() : "";
			    it.remove();
                ss.add(str);
			    while(it.hasNext() && !str.equalsIgnoreCase("SELECT")) {
			        str = it.hasNext() ? it.next() : "";
			        it.remove();
			       if (str.equalsIgnoreCase(",")) { 
	                    str = it.hasNext() ? it.next() : "";
	                    it.remove();
	                    ss.add(str);
	                }
			    }
			    strs.add(0, "select");
			    parseSingleSelect(strs,ss,serverName,job);
                break;
			}else if (str.equalsIgnoreCase("SELECT")) {
				// insert into ...select ...from...
			    strs.add(0, "select");
			    parseSingleSelect(strs,ss,serverName,job);
				break;
			}
		}
		for (String key : ks) {
			// System.out.println(key + ": " + sqls.get(key));
		    parseSingleSelect(SQLStrFormat.split(sqls.get(key)),ss,serverName,job);
		}
	}
	
	public void parseInsert(String sql) {
		parseInsert(sql,"","","");
	}
	private void parseAlter(String sql, String serverName,JobInfo job) {
		List<String> strs = SQLStrFormat.split(sql);
		parseAlter(strs, serverName,job);
	}
	
	private void parseAlter(List<String> strs, String serverName,JobInfo job) {
	    Iterator<String> it = strs.iterator();
        String str = "", ino = "out", inofile = "";
        str = it.hasNext() ? it.next() : "";
        if (str.equalsIgnoreCase("ALTER")||str.equalsIgnoreCase("TRUNCATE")||str.equalsIgnoreCase("RUNSTATS")) {
            while (it.hasNext()) {
                str = it.hasNext() ? it.next() : "";
                if(str.equalsIgnoreCase("TABLE")) {
                    str = it.hasNext() ? it.next() : "";
                    inofile = str;
                    //System.out.println(str);
                    if (!inofile.matches(substr))
                        jobinoSet.add(new JobInputOutput(job,ino, serverName, inofile));
                    break;
                }
            }
        }
	}
	
	/**
     * @param args 解析ReadFile
     */
    private void parseReadFile(String sql,  String serverName,JobInfo job) {
		List<String> strs = SQLStrFormat.split(sql);
        Iterator<String> it = strs.iterator();
        String str = "", ino = "in", schema = "", inofile = "";
        str = (String) it.next();
        if (str.equalsIgnoreCase("READFILE")) {
            while (it.hasNext()) {
                str = it.next();
                if (str.equalsIgnoreCase("FROM")) {
                    /*
                     * int index1 = sql.lastIndexOf("/"); int index2 = sql.lastIndexOf("\\"); int
                     * index = index1 > index2 ? index1 : index2; if(index <= 0) { inofile =
                     * sql.substring(14).trim(); }else { schema = sql.substring(14, index +
                     * 1).trim(); inofile = sql.substring(index + 1).trim(); }
                     */
                    inofile = sql.substring(14).replaceAll("\\\\", "/").trim();
                    if (!inofile.matches(substr) && !inofile.isEmpty())
                        jobinoSet.add(new JobInputOutput(job, ino, serverName, schema, inofile));
                    break;
                }
            }
        }
    }
    
    /**
     * @param args 解析WriteFile
     */
    private void parseWriteFile(String sql,  String serverName,JobInfo job) {
		List<String> strs = SQLStrFormat.split(sql);
        Iterator<String> it = strs.iterator();
        String str = "", ino = "out", schema = "", inofile = "";
        str = (String) it.next();
        if (str.equalsIgnoreCase("WRITEFILE")) {
            while (it.hasNext()) {
                str = it.next();
                if (str.equalsIgnoreCase("INTO")) {
                    /*
                     * int index1 = sql.lastIndexOf("/"); int index2 = sql.lastIndexOf("\\"); int
                     * index = index1 > index2 ? index1 : index2; if(index <= 0) { inofile =
                     * sql.substring(15).trim(); }else { schema = sql.substring(15, index +
                     * 1).trim(); inofile = sql.substring(index + 1).trim(); }
                     */
                    inofile = sql.substring(15).replaceAll("\\\\", "/").trim();
                    if (!inofile.matches(substr) && !inofile.isEmpty())
                        jobinoSet.add(new JobInputOutput(job, ino, serverName, schema, inofile));
                    break;
                }
            }
        }
    }
	
	@SuppressWarnings("unused")
	private void parseAlter(String sql) {
		parseAlter(sql,"",new JobInfo());
	}

	/**
	 * @param args 解析Delete SQL
	 */
	public void parseDelete(String sql, String stype, String jobname, String serverName) {
	    parseDelete(sql,serverName,new JobInfo(stype,"","",jobname));
	}
	public void parseDelete(String sql, String serverName,JobInfo job) {
		//sql = "DELETE FROM Z FROM DBO.ETL_POLICY_DIMN Z INNER JOIN DBO.ETL_POLICY_EFFECTIVE_DIMN_MIDD B ON Z.COMPANYCODE = B.COMPANYCODE";
		//delete from A from (select * from test3)C,dbo.test1 B,dbo.test2 A where a.id = b.id and c.id = a.id;
		//delete <from> A from(select * from test1)A;
		//boolean delete = false; // 是否找到delete
		String str = "", ino = "out", schema = "", inofile = "";
		Map<String, String> sqls = SQLStrFormat.getSubSQLStr(sql, 1);
		ArrayList<String> ss = new ArrayList<String>();
		Set<String> ks = sqls.keySet();
		String k = "__main__";
		String deleteAliasTableName = null;
		while (k != null) {
			String isql = sqls.get(k);
			ks.remove(k);
			// System.out.println("isql: " + isql);
			List<String> strs = SQLStrFormat.split(isql);
			Iterator<String> it = strs.iterator();
			// String str ="", ino = "out", schema = "", inofile = "";
			while (it.hasNext()) {
				str = it.hasNext() ? it.next() : "";
				it.remove();
				if (str.equalsIgnoreCase("DELETE") || str.equalsIgnoreCase("FROM")) {
				    ss.addAll(ks);
                    ss.addAll(subSQLAlias);
					//search for delete from tab | delete tab | select * from tab
					if (str.equalsIgnoreCase("DELETE")){
						str = it.hasNext() ? it.next() : "";
						it.remove();
						// 1.delete tabname(oracle)
						if (str.equalsIgnoreCase("FROM")){
							str = it.hasNext() ? it.next() : "";
							it.remove();
						}
					}else{
						// 2.delete from tabname
						str = it.hasNext() ? it.next() : "";
						it.remove();
					}
					//search for tabname
					String t1 = it.hasNext() ? it.next() : "";
					if(t1.equalsIgnoreCase("FROM")){
						//delete from A FROM dbo.test1 B,dbo.test2 A where a.id = b.id;
						deleteAliasTableName = str; //(1)记下删除目标表的别名
						strs.add(0, "*");
						strs.add(0, "select");
						ParseSQL p1 = new ParseSQL();						
						p1.parseSingleSelect(strs,ss, serverName,job);
						for(JobInputOutput j : p1.getJobinoSet()){
							if(deleteAliasTableName.equals(j.aliasName))
								//(2)标记delete的目标表
								j.ino = "out";
						}
						this.jobinoSet.addAll(p1.getJobinoSet());
						//System.out.println("delete from A FROM dbo.test1 B,dbo.test2 A where a.id = b.id;");
						k = null;
						break;
						/* delete from A from (select * from tab)A join ....
						 * if (t2.length() > 10 && t2.substring(0, 10).equals("__subquery")) {
							//delete <from> A FROM (select * from dbo.tab1)A join<,> B
						}else{
							
						}*/
					}
					else {
    					inofile = str;
                        str = t1;
					}
					String t = (schema + inofile);
					if (t.matches(substr)) {
						// 遇到delete from(select * from tab)，继续搜索子查询
						k = schema + inofile;
						// System.out.println("k: " + k);
						break;
					} else {
						//if (!inofile.equals("__brackets__") && !inofile.equals("__subquery__"))
						jobinoSet.add(new JobInputOutput(job, ino, serverName, inofile));
						ino = "in";
						k = null;
						break;
					}
				}
			}
		}
		//子查询
		for (String key : ks) {
			// System.out.println(key + ": " + sqls.get(key));
			parseSingleSelect(SQLStrFormat.split(sqls.get(key)),ss,serverName,job);
		}

	}
	
	public void parseUpdate(String sql, String stype, String jobname, String serverName) {
	    parseUpdate(sql, serverName,new JobInfo(stype, "","", jobname));
	}
	public void parseUpdate(String sql,String serverName,JobInfo job) {
		// sql = "delete from(select * from a where exists(select 1 from b))";
		String str ="", ino = "out", inofile = "";
		Map<String, String> sqls = SQLStrFormat.getSubSQLStr(sql,1);
		Set<String> ks = sqls.keySet();		
		List<String> ss = new ArrayList<String>();
		ss.addAll(ks);
		ss.addAll(subSQLAlias);
		String k = "__main__";
		while (k != null) {
			String isql = sqls.get(k);
			ks.remove(k);
			//System.out.println("isql: " + isql);
			List<String> strs = SQLStrFormat.split(isql);
			Iterator<String> it = strs.iterator();
			//String str ="", ino = "out", schema = "", inofile = "";
			while (it.hasNext()) {
				str = it.next();
				if(str.equalsIgnoreCase("INSERT")){
					continue;
				}else if(str.equalsIgnoreCase("DELETE") ||str.equalsIgnoreCase("FROM") || str.equalsIgnoreCase("UPDATE")  || str.equalsIgnoreCase("INTO")){
					if (str.equalsIgnoreCase("DELETE")){
						str = it.hasNext() ? it.next() : "";
						if (str.equalsIgnoreCase("FROM"))
							str = it.hasNext() ? it.next() : "";
					}else
					    inofile = it.hasNext() ? it.next() : "";
                    str = it.hasNext() ? it.next() : "";
					if (inofile.matches(substr)) {
						k = inofile;
						//System.out.println("k: " + k);
						break;
					} else {
						if (!inofile.matches(substr))
						    jobinoSet.add(new JobInputOutput(job,ino,serverName, inofile));
						ino = "in";
						k = null;
					}
				}
			}
		}
		for (String key : ks) {
			//System.out.println(key + ": " + sqls.get(key));
		    parseSingleSelect(SQLStrFormat.split(sqls.get(key)),ss,serverName,job);
		}
	}

	public HashSet<JobInputOutput> getJobinoSet() {
		return jobinoSet;
	}
	
	public String printJobino(){
		String s = "";
		Iterator<JobInputOutput> it = jobinoSet.iterator();
		while(it.hasNext()){
			s += it.next().toString();		
		}
		return s;		
	}
	
	public static void main(String[] args) {
		String filename = "sample.sql";
		ParseSQL p = new ParseSQL();
		InputStream in =  p.getClass().getClassLoader().getResourceAsStream(filename);
		StringBuilder rs = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String s = null;
		try {
			while((s = br.readLine())!=null)
				rs.append("\n"+s);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			WriteLog.writeFile(ParseJobio.getLogFile(),e.toString());
		}
		String sql = rs.toString();
		sql = SQLStrFormat.removeAnnotation(sql);
		//System.out.println(sql);
		p.parseSQLs(rs.toString());
		System.out.println(p.printJobino());
		/*String sql = "select OUTPUT_SQL SQL from table(db2inst1.dsxml_JobioSQL('bus_20190328','jb_circ_before_down2_d_inc'))t where STAGEID = 'V57S1'";
		ArrayList<String> strs = p.getJobSqls(sql);
		p.parseSQLs(strs.get(0));*/
		
        /*
         * String sql_format = SQLStrFormat.format(sql); Map<String,String> sqls_Map =
         * SQLStrFormat.getSubSQLStr(sql_format,1); for(String key : sqls_Map.keySet())
         * { System.out.println(key+" : " + sqls_Map.get(key)); }
         */
	}

}
