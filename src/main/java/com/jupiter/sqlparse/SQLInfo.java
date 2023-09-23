package com.jupiter.sqlparse;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author jupiter
 * the class SQLInfo, carries the information of sql to be analyzed.
 */
public class SQLInfo {
    //public String type;     //SQL类型:SELECT INSERT DELETE UPDATE MERGE LOAD IMPORT TRUNCATE
    public String sqlStr;
    //public int bracketsCount = 0;
    Comparator1 comparator = new Comparator1();
    //public Map<String,String> brackets = new LinkedHashMap<String,String>();
    //public Map<String,String> subQueries = new LinkedHashMap<String,String>();
    public Map<String, String> brackets = new TreeMap<>(comparator);
    public Map<String, String> subQueries = new TreeMap<>(comparator);
    //public Map<String,String> bracketsCounts = new HashMap<String,String>();
    //IdentityHashMap in_tbl = new IdentityHashMap<String,String>();
    //IdentityHashMap out_tbl = new IdentityHashMap<String,String>();

    /**
     * Creates a new instance of SQLInfo.
     */
    public SQLInfo(String sqlStr) {
        this.sqlStr = sqlStr;
        //this.bracketsCount = bracketsCount;
    }

    public String toString() {
        return this.sqlStr;
    }

    class Comparator1 implements java.util.Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            // 降序排序
            if (o1.equals("__main__") || o2.equals("__main__"))
                return o2.compareTo(o1);
            else
                return Integer.parseInt(o2.substring(10).replace("__", "")) - Integer.parseInt(o1.substring(10).replace("__", ""));
            //return o2.compareTo(o1);
        }
    }
}

