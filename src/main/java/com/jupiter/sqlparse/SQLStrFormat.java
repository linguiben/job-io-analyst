package com.jupiter.sqlparse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @version : v1.0
 * @author  : Jupiter.Lin
 * @specify : 
 * @createDate: Mar 22, 2019
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jupiter.WriteLog;
//import com.sun.scenario.effect.Bloom;

public class SQLStrFormat {

    private static List<String> lineKey = Arrays.asList("FROM","JOIN","WHERE","AND","GROUP","ORDER","UNION","INTERSECT","EXCEPT","MINUS");
    private static List<String> comma = Arrays.asList(",");
    private static List<String> joinMode = Arrays.asList("INNER","LEFT","RIGHT","CORSS");
    
	public static void main(String[] args) {
		
		//String str = "with #a as(select * from t1,t2)select * from dbo.t,#a where exists(select 1 as s from (select 10 from dual))";
	    
	    String filename = "sample.sql";
	    SQLStrFormat p = new SQLStrFormat();
	    InputStream in =  p.getClass().getClassLoader().getResourceAsStream(filename);
        StringBuilder rs = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String str = null;
        try {
            while((str = br.readLine())!=null)
                rs.append("\n"+str);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            WriteLog.writeFile(ParseJobio.getLogFile(),e.toString());
        }
        //System.out.println(rs.toString());
        
        
        /*Map<String, String> SQLStrs = SQLStrFormat.getSubSQLStr(rs.toString(), 1);
        Set<String> ks = SQLStrs.keySet();
        System.out.println("__main__: " + SQLStrs.get("__main__"));
        ks.remove("__main__");
        for (String key : ks) {
            System.out.println(key + ": " + SQLStrs.get(key));
        }*/
         
        
        /*���Ի�ȡ�Ӳ�ѯ
         * ArrayList<String> SQLStrs1 = SQLStrFormat.getSubSQLStr(rs.toString());
         * System.out.println(SQLStrs1.toString());
         */
        
        String sql = rs.toString();
        sql = SQLStrFormat.removeAnnotation(sql);
        String sql_format = SQLStrFormat.format(sql);
        System.out.println(sql_format);
        SQLInfo sqlInfo = SQLStrFormat.getSubSQLStr(sql_format);
        Map<String, String> subQeryies = sqlInfo.subQueries;
        for(String key : subQeryies.keySet()) {
            System.out.println(key+" : " + subQeryies.get(key));
        }
        
        //��ԭ�Ӳ�ѯ
        String mainSQL = subQeryies.get("__main__");
        subQeryies.remove("__main__");
        mainSQL = SQLStrFormat.format1(mainSQL,1);
        for(String key : subQeryies.keySet()) {
            String subquery = subQeryies.get(key);
            mainSQL = mainSQL.replace(key,subquery);
        }
        //��ԭ����
        Map<String, String> brackets = sqlInfo.brackets;
        for(String key : brackets.keySet()) {
            String bracket = brackets.get(key);
            mainSQL = mainSQL.replace(key,bracket);
        }
        //mainSQL = mainSQL.replaceAll("( )?,( )?", ",").replaceAll("( )?\\.( )?", ".").replaceAll("( )?\\(( )?","(").replaceAll("( )?\\)( )?",")").replaceAll(" \n", "\n");
        mainSQL = mainSQL.replaceAll(" *(?=,|\\.|\\(|\\)|\n)", "").replaceAll("(?<=,|\\.|\\(|\\)|\n) *", "");
        System.out.println(mainSQL);
        
		
	}

	/**ɾ��ע��**/
	public static String removeAnnotation(String sql){
		// ɾ������ע��
		//sql = Pattern.compile("--.*").matcher(sql).replaceAll("");
		// ɾ��ע�Ϳ�
		//sql = Pattern.compile("/\\*((?!/\\*|\\*/).)*\\*/",Pattern.DOTALL).matcher(sql).replaceAll("");
		
		char[] ch = sql.toCharArray();
		String str = ""; 
		int len = ch.length,sqm = 1,dqm = 1;//�����š�˫��������
		anno : for(int i=0;i<len;i++){
			char s = ch[i];
			if(s=='\''){
				sqm *= -1;
			}else if(s=='"'){
				dqm *= -1;
			}else if(s=='-' && sqm+dqm==2 && i<len-1){
				if(ch[i+1]=='-'){
					while(i<len && ch[i]!=10 && ch[i]!=13){
						//ch[i++] = ' ';
						i++;
					}
					if(i>=len-1)
						break anno;
				}
			} else if (s == '/' && sqm + dqm == 2 && i < len - 1) {
				if (ch[i + 1] == '*') {
					i++;
					while (i < len-1 ) {
						//ch[i++] = ' ';
						if(!(ch[i] == '*' && ch[i + 1] == '/')){
							i++;							
						}else{
							i++;
							continue anno;
						}
					}
					//ch[i++] = ' ';
					//ch[i] = ' ';
				}
			}
			str += String.valueOf(ch[i]);
		}
		 //sql = String.valueOf(ch);
		 //System.out.println(str);
		 return str;
	}
	
	public static ArrayList<String> split(String str) {
	    //ȥ��"."���ߵ� �� "]"��ߵĿհ�,[ ``���ÿ���
	    str = str.replaceAll("\\s*(?=\\]|\\.)", "").replaceAll("(?<=\\.)\\s*", "");
	    
	    int length = str.length();
	    if(str.substring(0,1).equals("(")&&str.substring(length-1).equals(")"))
	        str = str.substring(1, length-1);

		/* ������ʽ���ָ��� */
		String regEx = "\\s+|,|;|\\(|\\)|'([^']|''|\\s)*'"; // '([^']|''|\\s)*' '{1,2}
		regEx += "|(?<=select)(\\*|`\\w+`|\\[\\w+\\]|\"\\w*\"|'\\w*')"
		        + "|(\\*|`\\w+`|\\[\\w+\\]|\"\\w*\"|'\\w*')(?=from|,|left|right|cross|join)"
		        + "|(?<=from|,|join)(`\\w+`|\\[\\w+\\])"; //select*from[]
		//regEx += "|#\\w+\\.\\w+#|\\d+\\.\\d+|\\.";  //. ���ų�#.# ��С����
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		/* ���վ��ӽ������ָ���� */
		String[] words = p.split(str);

		// ת��ArrayList
		// ArrayList<String> ws = new ArrayList<String>(Arrays.asList(words));
		ArrayList<String> ws = new ArrayList<String>();

		/* ����ָ��� */
		if (words.length > 0) {
			int count = 0;
			while (count < words.length) {
				// System.out.println(words[count]);
				String w = words[count].trim();
				if (!w.isEmpty())
					ws.add(w);
				if (m.find()) {
					String mg = m.group().trim();
					if (!mg.isEmpty()) {
						// ws.add(2 * count + 1, m.group());
						// System.out.println(m.group());
						ws.add(mg);
					}
				}
				count++;
			}
			while (m.find()) {
				String mg = m.group().trim();
				if (!mg.isEmpty()) {
					ws.add(mg);
				}
			}
		}

		//�ϲ��ַ��� 'x''x'
		/*String s = null,t=null;
		for(int i=0;i<ws.size();i++){
			s = ws.get(i);
			if(s.equals("'")&&i<ws.size()-1){
				int j = i;
				i ++;
				t = ws.get(i);
				s += t;
				ws.set(i, "");
				while(!t.equals("'")&&i<ws.size()-1){
					i++;
					t = ws.get(i);
                    s += t.equals("'") ? t : "" + t;
					ws.set(i, "");
				}
				ws.set(j, s);
			}
		}*/
		/* ������ */
		 //System.out.println(ws);
		return ws;
	}
	
	public static String format(String sql){
		return format1(sql,0);
	}
	
	/**
	 * @param f:0-���з��� ��0-���з���
	 */
	public static String format1(String sql,int f){
	    List<String> lineKeys = new ArrayList<String>();
	    lineKeys.addAll(lineKey);
	    //lineKeys.addAll(joinMode);
		String str = "";
		ArrayList<String> ws = SQLStrFormat.split(sql);
		if(f==0){
		    for(int i=0;i<ws.size();i++){
                str += (ws.get(i) + " ");
            }
        } else {
            // boolean anotherSQL = false;//�Ƿ���һ��SQL
            for (int i = 0; i < ws.size(); i++) {
                String card = ws.get(i);
                //��ΪSQL�Ͼ䣬���Զ���
                if (EnumUtils.isInclude(AnotherSQLKeyWord.class, card.toUpperCase()))
                    lineKeys.removeAll(comma);
                //��ΪFROM���ж϶���
                else if (card.toUpperCase().equals("FROM"))
                    lineKeys.addAll(comma);
                // ������inner left right corss,����join,���򲻺���
                else if (joinMode.contains(card.toUpperCase()) && i < ws.size()-2) {
                    str += "\n" + card + " " + ws.get(++i) + " ";
                    card = ws.get(++i);
                }
                if (lineKeys.contains(card.toUpperCase()))
                    card = "\n" + card;
                str += card + " ";
            }
        }
		
		return str;
	}
	
	/**
	 * @param ��ȡSQL����
	 */
	public static String getSQLType(String sql) {
		String t = "";
		Pattern p = Pattern.compile("\\w{3,}");
		Matcher m = p.matcher(sql);
		if (m.find())
			t = m.group();
		return t;
	}
	
    /**
     * @param ����SQL��ֳɶ����SQL
     */
    public static Map<String, String> getSubSQLStr(String sql, int i) {
       
        SQLInfo sqlInfo = getSubSQLStr(sql);
        return sqlInfo.subQueries;
        
        /*
        //1.�����Ӳ�ѯ�ﲻ�� (with as ..)
        sql = replaceAllBrackets(sql);
        Matcher m = p.matcher(sql);
        while (m.find()) {
            //System.out.println(m.group());
            SQLStrs.put("__subquery" + id + "__", m.group());
            sql = m.replaceFirst("__subquery" + id + "__");
            //System.out.println(sql);
            sql = replaceAllBrackets(sql);
            m = p.matcher(sql);
            id++;
        }
        
        //2. ���� ( with t as __subquery1__ select * from t )
        Matcher m1 = p1.matcher(sql);
        if(m1.find()) {
            SQLStrs.put("__subquery" + id + "__", m1.group());
            sql = m1.replaceFirst("__subquery" + id + "__");
            sql = replaceAllBrackets(sql);
            //System.out.println(sql);
            id++;
        }
        
        //3.�ٴ���һ���Ӳ�ѯ
        m = p.matcher(sql);
        while (m.find()) {
            // System.out.println(m.group());
            SQLStrs.put("__subquery" + id + "__", m.group());
            sql = m.replaceFirst("__subquery" + id + "__");
            //System.out.println(sql);
            sql = replaceAllBrackets(sql);
            m = p.matcher(sql);
            id++;
        }
        
        //4.ʣ�µĲ���Ϊ����ѯ
        SQLStrs.put("__main__", sql);
        return SQLStrs;
        */
    }
	
	/**
	 * @param ����SQL��ֳɶ����SQL
	 */
	public static SQLInfo getSubSQLStr(String sql){
	  //HashMap<String, String> SQLStrs = new HashMap<String, String>();
        int id = 1;
        // 1.SQLSTR��ʽ��
        sql = SQLStrFormat.format(sql);
        // System.out.println("source: "+sql);
        // 2.��ȡ�Ӳ�ѯ
        // ������ʽ   ((?!(\\(|\\))).)  --�����ŵķǿո��ַ�
        String regex0 = "select((?!(\\(|\\))).)+ from((?!(\\(|\\))).)+";
        String regex1 = "\\w+( __brackets\\d+__)? as __subquery\\d+__";
        String regexSingle = "\\( " + regex0 + "\\)";
        // ���� ( with t as __subquery1__ select * from t )
        String regexWith = "\\( with "+ regex1+"( , "+regex1+")?" +" "+ regex0 +"\\)";
        // String regex = "\\( select((?!( select | from )).)+ from((?!( select | from |\\))).)+\\)";
        // String regex1 = "\\( select((?!( select | from )).)+ from((?!( select | from )).)+\\)"; //ƥ�䵥���Ӳ�ѯ
        // ��ȡ�Ӳ�ѯ
        Pattern p = Pattern.compile(regexSingle, Pattern.CASE_INSENSITIVE);
        Pattern p1 = Pattern.compile(regexWith, Pattern.CASE_INSENSITIVE);
        
        SQLInfo sqlInfo = new SQLInfo(sql);
        Map<String, String> subQueries = sqlInfo.subQueries;
        //1.�����Ӳ�ѯ�ﲻ�� (with as ..)
        sqlInfo = replaceAllBrackets(sqlInfo);
        Matcher m = p.matcher(sqlInfo.sqlStr);
        while (m.find()) {
            //System.out.println(m.group());
            subQueries.put("__subquery" + id + "__", m.group());
            sqlInfo.sqlStr = m.replaceFirst("__subquery" + id + "__");
            //System.out.println(sql);
            sqlInfo = replaceAllBrackets(sqlInfo);
            m = p.matcher(sqlInfo.sqlStr);
            id++;
        }
        
        //2. ����(ֻ��oracle֧��) ( with t as __subquery1__ select * from t )
        Matcher m1 = p1.matcher(sqlInfo.sqlStr);
        if(m1.find()) {
            subQueries.put("__subquery" + id + "__", m1.group());
            sqlInfo.sqlStr = m1.replaceFirst("__subquery" + id + "__");
            sqlInfo = replaceAllBrackets(sqlInfo);
            //System.out.println(sql);
            id++;
        }
        
        //3.�ٴ���һ���Ӳ�ѯ
        m = p.matcher(sqlInfo.sqlStr);
        while (m.find()) {
            // System.out.println(m.group());
            subQueries.put("__subquery" + id + "__", m.group());
            sqlInfo.sqlStr = m.replaceFirst("__subquery" + id + "__");
            //System.out.println(sql);
            sqlInfo = replaceAllBrackets(sqlInfo);
            m = p.matcher(sqlInfo.sqlStr);
            id++;
        }
        
        //4.ʣ�µĲ���Ϊ����ѯ
        subQueries.put("__main__", sqlInfo.sqlStr);
        return sqlInfo;
	}
	
	/**
	 * @param �����������滻
	 */
	public static String replaceAllBrackets(String sql) {
		String brackets = "\\(((?!( select | from |\\(|\\))).)+\\)"; // ƥ��һ������
		Pattern pb = Pattern.compile(brackets,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher mb = pb.matcher(sql);
		while (mb.find()) {
			sql = mb.replaceAll(" __brackets__ ");
			 mb = pb.matcher(sql);
		}
		return sql;
	}
	
	public static SQLInfo replaceAllBrackets(SQLInfo sqlInfo) {
        String expression = "\\(((?!( select | from |\\(|\\))).)+\\)"; // ƥ��һ������
        String sqlStr = sqlInfo.sqlStr;
        Map<String, String> brackets = sqlInfo.brackets;
        int bracketsCount = brackets.size();
        Pattern pb = Pattern.compile(expression,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher mb = pb.matcher(sqlStr);
        while (mb.find()) {
            bracketsCount ++;
            brackets.put("__brackets"+bracketsCount+"__", mb.group());
            sqlStr = mb.replaceFirst("__brackets"  + bracketsCount + "__");
             mb = pb.matcher(sqlStr);
        }
        sqlInfo.sqlStr = sqlStr;
        return sqlInfo;
    }

	
}
