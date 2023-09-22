package com.jupiter.sqlparse;

import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ParseSingleSelect {

    private static List<String> anotherKeyWords = Arrays.asList("WHERE","GROUP","ORDER","UNION","INTERSECT","EXCEPT","MINUS");//另外一句SQL
    private static List<String> SQLKeyWords = Arrays.asList("SELECT","WHERE","ON","GROUP","ORDER","UNION","INTERSECT","EXCEPT","MINUS","LEFT","RIGHT","FULL","CROSS","JOIN",",","(",")");
    private List<String> subSQLAlias = Arrays.asList("__brackets__","__subquery__","/dev/null","dev/null");
    private static String substr = "__(brackets|subquery)\\d+__";
    //private HashSet<JobInputOutput> jobinoSet;
    
    
    public static HashSet<JobInputOutput> parseSingleSelect(ArrayList<String> strs, HashSet<JobInputOutput> jobinoSet, List<String> subqueryNames, String serverName, JobInfo job) {
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
                if (!inofile.matches(substr) & !subqueryNames.contains(inofile))
                    jobinoSet.add(new JobInputOutput(job, ino,serverName, inofile));
            } else if (select & from &&(str.equalsIgnoreCase("JOIN") || str.equals(","))) {
                    // 分析join 和 ","
                    inofile = iter.hasNext() ? iter.next() : "";
                    if (!inofile.matches(substr) & !subqueryNames.contains(inofile))
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
        return jobinoSet;
    }
		
	
	public static void main(String[] args) {
		String str = "SELECT a,b,c,\"join\" FROM #v_jb_ods_schema# . EUAAPKY@dblink T1 LEFT JOIN dblink . [zbxods ].#v_jb_ods_schema# . EUABPKY T2 ON T1 . APPLNUM = T2 . APPLNUM , etl_policy_dimn e LEFT JOIN #v_jb_ods_schema# . UI_BRANCH T4 ON T1 . AGNTCOY = T4 . CHDRCOY LEFT JOIN #v_jb_ods_schema# . EUADPF T5 ON T1 . APPLNUM=T5 . APPLNUM AND T5 . ROLEFLAG='''2''' LEFT JOIN #v_jb_ods_schema# . EUADPF T6 ON T1 . APPLNUM=T6 . APPLNUM AND T6 . ROLEFLAG='''1''' , etl_job c " +
				"union all " +
				"select 1,* from dbo.tab1 ,dbo.tab2,tab3 join dbo.tab4 as a where max(asdw,10) =sedwaed and sdwea <> 232 and exists(select a,b,c from dbo.tabexists where a.b = c.d)" +
				"EXCEPT " +
				"select xxxx from sqlserverlink.zbxods.dbo.txxxx join tyyyy on id = name join (select * from dual) on coalesce(id,0) =0 and max(name1,name2) = min(name3,name4)";
		//System.out.println(new JobInputOutput(new JobInfo(),"","dblink.zbxods.dbo.tabname"));
		Map<String, String> sqlstrs = SQLStrFormat.getSubSQLStr(str,0);
		String mainSQL = sqlstrs.get("__main__");
		//System.out.println(mainSQL);
		for(String key : sqlstrs.keySet()) {
		  System.out.println(key + " : " + sqlstrs.get(key));  
		}
		System.out.println(
		        ParseSingleSelect.parseSingleSelect(SQLStrFormat.split(mainSQL), new HashSet<JobInputOutput>(), new ArrayList<String>(), null, new JobInfo())
		);
	}

}
