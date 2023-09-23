package com.jupiter.sqlparse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Jupiter.Lin
 * @desc 测试类
 * @date 2023-09-22 23:34
 */
class ParseSelectSQLTest {

    String sql = null;
    ParseSelectSQL p;

    @BeforeEach
    void setUp() {
        p = new ParseSelectSQL();
        sql = "SELECT * FROM #v_jb_ods_schema# . EUAAPKY T1 LEFT JOIN #v_jb_ods_schema# . EUABPKY T2 ON T1 . APPLNUM " +
                "= T2 . APPLNUM , etl_policy_dimn e LEFT JOIN #v_jb_ods_schema# . UI_BRANCH T4 ON T1 . AGNTCOY = T4 . CHDRCOY LEFT JOIN #v_jb_ods_schema# . EUADPF T5 ON T1 . APPLNUM=T5 . APPLNUM AND T5 . ROLEFLAG=''2'' LEFT JOIN #v_jb_ods_schema# . EUADPF T6 ON T1 . APPLNUM=T6 . APPLNUM AND T6 . ROLEFLAG=''1'' , etl_job c WHERE T1 . HPRRCVDT BETWEEN INT ( DATE ( SUBSTR ( CHAR   , 1 , 4 ) ||''-''||SUBSTR ( CHAR   , 5 , 2 ) ||''-01'' ) -1 MONTHS ) AND #v_jb_enddate# AND T2 . CRTABLE <> ''VSB9'' AND T2 . CRTABLE <> ''VSBG'' AND T2 . PRMSW IN  " +
                "union all " +
                "select * from dbo.abcd ,dbo.efdt,dikaerji join dbo.qwerwsds a where asdw =sedwaed and sdwea <> 232 " +
                "union all " +
                "select xxxx from dbo.txxxx join tyyyy on id = name join (select a.b,c.d,e.f,g.h from dbo.subtable b) on coalesce(a.b , c.d,xxx)=0 and max(id)= min(name)";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void parse() {
    }

    @Test
    void parseSelect() {
        System.out.println(p.parseSelect(sql));
    }

    @Test
    void testParseFullJoinSql(){
        // sql = "select * from t1 a full join t2 b on a.id  = b.id ";
        sql = "select max(\"from\".name) as having from t as \"select\" inner join a as \"join \" on \"select\".id =" +
                "\"join\".id group by \"join\".id having count(1) > 0\n";
        System.out.println(p.parseSelect(sql));
    }

    @Test
    void getStrList() {
    }
}