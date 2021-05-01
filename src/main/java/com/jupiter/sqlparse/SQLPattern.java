package com.jupiter.sqlparse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLPattern {

	/**
	 * @param args
	 */
	 //String tmp = "CALL SYSPROC.ADMIN_CMD('import from /dev/null of del replace into #v_jb_eser_schema#.GiveQuaNum_dwn')";
	 final String TrancateTable = "CALL\\s+SYSPROC(\\s)?\\.(\\s)?ADMIN_CMD(\\s)?\\(.*/dev/null.*replace(\\s)?into.*";
	 final static String SQLStatement = "'(\\s+)?(INSERT|DELETE|UPDATE|SELECT|EXPORT|IMPORT|LOAD|TRUNCATE|MERGE)\\s+.*'"; 
	
	public String db2TruncateTable(String sql) throws Exception{
		String t = sql.trim()+";";
		if(isDB2TruncateSQL(t)){
			t = t.substring(t.toUpperCase().indexOf("INTO")+4,t.toUpperCase().indexOf(");")-1);
		return t.replaceAll("'","").replaceAll(" ",	"");
		}
		throw new Exception("not db2 truncate sql: \n" + sql);
	}
	
	public boolean isDB2TruncateSQL(String sql){
		
	    // 编译正则表达式,忽略大小写
	    Pattern pattern = Pattern.compile(TrancateTable, Pattern.CASE_INSENSITIVE);  //
	    Matcher matcher = pattern.matcher(sql.trim());
	    return matcher.matches();
	}
	
    public static boolean isSQLStatement(String sql){
            // 编译正则表达式,忽略大小写
            Pattern pattern = Pattern.compile(SQLStatement, Pattern.CASE_INSENSITIVE);  //
            Matcher matcher = pattern.matcher(sql.trim());
            return matcher.matches();
     }

	
	public static void main(String[] args) {
		try {
			//System.out.println(s.db2TruncateTable("call sysproc .ADMIN_CMD('import from /dev/null of del replace into #v_jb_eser_schema#.GiveQuaNum_dwn2')"));
	        System.out.println(SQLPattern.isSQLStatement("' merge into  '"));
		} catch (Exception e) {
			System.out.println("not support!");
			e.printStackTrace();
		}
	    //System.out.println(Pattern.matches(s.regEx, s.TrancateTable));
	}

}
