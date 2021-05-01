package com.jupiter.sqlparse;

import java.lang.reflect.Method;

/**
 * @Description: ö�ٹ�����
 * @author: Jupiter.Lin
 * @version: V1.0
 * @date: 2021��1��29�� ����3:48:20
 */
public class EnumUtils {

    /**
     * �ж���ֵ�Ƿ�����ö�����ֵ
     * 
     * @param clzz
     *            ö���� Enum
     * @param key
     * @return
     */
    public static boolean isInclude(Class<?> clzz, String key) {
    /*
     * throws InvocationTargetException, IllegalAccessException,
     * NoSuchMethodException
     */ 
        boolean include = false;
        if (clzz.isEnum()) {
            Object[] enumConstants = clzz.getEnumConstants();
            try {
                Method getKey = clzz.getMethod("getKey");
                for (Object enumConstant : enumConstants) {
                    if (getKey.invoke(enumConstant).equals(key.toUpperCase())) {
                        include = true;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return include;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(isInclude(AnotherSQLKeyWord.class, "select"));
        // System.out.println(ArrayList(AnotherSQLKeyWord.values()));
    }
}