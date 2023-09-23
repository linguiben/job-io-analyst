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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParseSelectSQL {

	private static final ComboPooledDataSource dataSource = new ComboPooledDataSource();

	/** 遇到这些关键字则判断为一句新的SQL，或者subQuery */
	private final List<String> resetKeyList = Arrays.asList("SELECT","WHERE","GROUP","HAVING","ORDER","UNION",
			"INTERSECT",
			"EXCEPT","MINUS");

	/**
	 * @param sqlWordList 解析格SQL式化后的单词，
	 * @return 返回 tableList of the sql input & output
	 */
	public List<String> parse(List<String> sqlWordList) {
		List<String> tableList= new ArrayList<>(); // 存储找到的input table
		Iterator<String> it = sqlWordList.iterator();
		String word = ""; // 逐字分析
		boolean from = false; // 记录是否遇到FROM关键字
		while (it.hasNext()) {
			// 遇到重置标记 (遇到这些关键字，则后面一定会出现FROM关键字)
			if (resetKeyList.contains(word.toUpperCase())) {
				from = false;
			}

			if (!from) {
				// 1.遇到FROM开始分析
				word = it.next();
				if (word.equalsIgnoreCase("FROM")) {
					from = true;
					String tab = it.next();
					word = it.next();
					if (word.equalsIgnoreCase(".")) {
						tab = tab + "." + it.next();
					}
					log.debug("tableName: {}",tab);
					tableList.add(tab);
				}
			} else {
				if (word.equalsIgnoreCase("JOIN")) {
					// 2.分析join
					word = it.next();
					if (it.next().equalsIgnoreCase(".")) {
						word = word + "." + it.next();
					}
					tableList.add(word);
					log.debug("tableName: {}",word);
					int left = 0, right = 0;
					while (it.hasNext()) {
						word = it.next();
						// System.out.println(str1);
						if(resetKeyList.contains(word.toUpperCase()) || word.equalsIgnoreCase("JOIN")){
							break;
						}else if (word.equals("(")) {
							left++;
						} else if (word.equals(")")) {
							right++;
						} else if (word.equals(",") && left == right) {
							break;
						}
					}
				} else if (word.equalsIgnoreCase(",")) {
					// 3.分析笛卡儿积
					word = it.next();
					if (it.next().equalsIgnoreCase(".")) {
						word = word + "." + it.next();
					}
					tableList.add(word);
					//System.out.println("tabname,: " + str);
				}else{
					word = it.next();
				}
			}
		}
		return tableList;
	}
	
	/**
	 * @param str analyze the Select SQL
	 *        return the input and output tables
	 */
	public List<String> parseSelect(String str){
		List<String> inoTables= new ArrayList<>();
		Map<String,String> sqls = SQLStrFormat.getSubSQLStr(str,1);
		for (String sql : sqls.values()) {
			List<String> sqlSplits = SQLStrFormat.split(sql);
			List<String> tabList = parse(sqlSplits);
			inoTables.addAll(tabList);
		}
		/*for(String i : sqls.keySet()){
			tbs.addAll(parse(SQLStrFormat.split(sqls.get(i))));
		}*/
		return inoTables;
	}

	// TODO: to be remove
	public List<String> getStrList(String sql){
		List<String> l = new ArrayList<String>();
		try {
			Connection conn = dataSource.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				log.debug("sql:{}",sql);
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
