package com.jupiter.sqlparse;

import java.lang.reflect.Method;

/**
 * @Description: 枚举工具类
 * @author: Jupiter.Lin
 * @version: V1.0
 * @date: 2021年1月29日 下午3:48:20
 */
public class EnumUtils {

    /**
     * 判断数值是否属于枚举类的值
     * 
     * @param clzz
     *            枚举类 Enum
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