package com.jupiter.sqlparse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jupiter.Lin
 * @desc TODO
 * @date 2023-09-22 23:48
 */
class SQLStrFormatTest {

    String sql = null;

    @BeforeEach
    void setUp() {
        sql = "select * from a inner join b where a.xxx = b.xxx group by a.bigDui having count(1) > '地球'";
    }

    @Test
    void format() {
        String formatSql = SQLStrFormat.format(sql, 1);
        System.out.println(formatSql);
        Assertions.assertEquals(6, formatSql.lines().count());
    }

}