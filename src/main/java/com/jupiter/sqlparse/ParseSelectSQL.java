package com.jupiter.sqlparse;

import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ParseSelectSQL {

	private static ComboPooledDataSource ds = new ComboPooledDataSource();
	private List<String> end = (List<String>) Arrays.asList("SELECT","WHERE","GROUP","ORDER","UNION","INTERSECT","EXCEPT","MINUS");
	/**
	 * @param args 解析格SQL式化后的单词
	 */
	public ArrayList<String> parse(ArrayList<String> l) {
		ArrayList<String> tbl= new ArrayList<String>();
		boolean from = false; // 是否找到from
		Iterator<String> iter = l.iterator();
		String str = "";
		while (iter.hasNext()) {
			
			// 遇到重置标记
			if (end.contains(str.toUpperCase())) {
				from = false;
			}

			if (!from) {
				// 1.分析from后面的值
				str = (String) iter.next();
				if (str.equalsIgnoreCase("FROM")) {
					from = true;
					String tb = (String) iter.next();
					str = (String) iter.next();
					if (str.equalsIgnoreCase(".")) {
						tb = tb + "." + iter.next();
					}
					tbl.add(tb);
					//System.out.println("tabnamef: " + tb);
				}
			} else {
				if (str.equalsIgnoreCase("JOIN")) {
					// 2.分析join
					str = (String) iter.next();
					if (((String) iter.next()).equalsIgnoreCase(".")) {
						str = str + "." + iter.next();
					}
					tbl.add(str);
					//System.out.println("tabnamej: " + str);
					int left = 0, right = 0;
					while (iter.hasNext()) {
						str = (String) iter.next();
						// System.out.println(str1);
						if(end.contains(str.toUpperCase()) || str.equalsIgnoreCase("JOIN")){
							break;
						}else if (str.equals("(")) {
							left++;
						} else if (str.equals(")")) {
							right++;
						} else if (str.equals(",") && left == right) {
							break;
						}
					}
				} else if (str.equalsIgnoreCase(",")) {
					// 3.分析笛卡儿积
					str = (String) iter.next();
					if (((String) iter.next()).equalsIgnoreCase(".")) {
						str = str + "." + iter.next();
					}
					tbl.add(str);
					//System.out.println("tabname,: " + str);
				}else{
					str = (String) iter.next();
				}
			}
		}
		return tbl;
	}
	
	/**
	 * @param args 解析Slect SQL
	 */
	public ArrayList<String> parseSelect(String str){
		ArrayList<String> tbs= new ArrayList<String>();
		/*ArrayList<String> sqls = SQLStrFormat.getSubSQLStr(str);
		for(int i=0;i<sqls.size();i++){
			tbs.addAll(parse(SQLStrFormat.split(sqls.get(i))));
		}*/
		Map<String,String> sqls = SQLStrFormat.getSubSQLStr(str,1);
		for(String i : sqls.keySet()){
			tbs.addAll(parse(SQLStrFormat.split(sqls.get(i))));
		}
		return tbs;		
	}
	
	public ArrayList<String> getStrList(String sql){
		ArrayList<String> l = new ArrayList<String>();
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				//System.out.println(rs.getString("STR"));
				l.add(Integer.parseInt(rs.getString("ID"))-1,rs.getString("STR"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	
	public static void main(String[] args) {
		String str = "SELECT * FROM #v_jb_ods_schema# . EUAAPKY T1 LEFT JOIN #v_jb_ods_schema# . EUABPKY T2 ON T1 . APPLNUM = T2 . APPLNUM , etl_policy_dimn e LEFT JOIN #v_jb_ods_schema# . UI_BRANCH T4 ON T1 . AGNTCOY = T4 . CHDRCOY LEFT JOIN #v_jb_ods_schema# . EUADPF T5 ON T1 . APPLNUM=T5 . APPLNUM AND T5 . ROLEFLAG=''2'' LEFT JOIN #v_jb_ods_schema# . EUADPF T6 ON T1 . APPLNUM=T6 . APPLNUM AND T6 . ROLEFLAG=''1'' , etl_job c WHERE T1 . HPRRCVDT BETWEEN INT ( DATE ( SUBSTR ( CHAR   , 1 , 4 ) ||''-''||SUBSTR ( CHAR   , 5 , 2 ) ||''-01'' ) -1 MONTHS ) AND #v_jb_enddate# AND T2 . CRTABLE <> ''VSB9'' AND T2 . CRTABLE <> ''VSBG'' AND T2 . PRMSW IN  " +
				"union all " +
				"select * from dbo.abcd ,dbo.efdt,dikaerji join dbo.qwerwsds a where asdw =sedwaed and sdwea <> 232 " +
				"union all " +
				"select xxxx from dbo.txxxx join tyyyy on id = name join (select a.b,c.d,e.f,g.h from dbo.subtable b) on coalesce(a.b , c.d,xxx)=0 and max(id)= min(name)";
		ParseSelectSQL p = new ParseSelectSQL();
		//p.parse(p.getStrList("select ID,STR from table(dbo.split_sqlstr('"+str+"',1))"));
		//p.parse(SQLStrFormat.split(str));
		System.out.println(p.parseSelect(str));
	}

}
