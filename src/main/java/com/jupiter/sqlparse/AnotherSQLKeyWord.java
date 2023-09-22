
package com.jupiter.sqlparse;



/**
* @Description:
* @author: Jupiter.Lin
* @version: V1.0 
* @date: 2021年1月29日 下午3:38:52 
*/
public enum AnotherSQLKeyWord {
    
    //SELECT,WHERE,GROUP,ORDER,UNION,INTERSECT,EXCEPT,MINUS;
    SELECT("SELECT")
    ,WHERE("WHERE")
    ,GROUP("GROUP")
    ,ORDER("ORDER")
    ,UNION("UNION")
    ,INTERSECT("INTERSECT")
    ,EXCEPT("EXCEPT")
    ,MINUS("MINUS");
    
    private String key;
   // private String value;
   
    AnotherSQLKeyWord(String key) {
        this.key = key;
    }

    
    public String getKey() {
        return key;
    }

    
    public void setKey(String key) {
        this.key = key;
    }
    

    //SELECT,WHERE,GROUP,ORDER,UNION,INTERSECT,EXCEPT,MINUS;
}

	