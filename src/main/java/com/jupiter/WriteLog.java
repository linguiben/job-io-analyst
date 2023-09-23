package com.jupiter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class WriteLog {

    static FileOutputStream fis = null;

    static String filename = "JobioAnalystV3.2.log";

    static boolean isFirst = true;

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

    static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

    static String fileDate = "";

    /**
     * 默认是一个文件
     *
     * @param Str logging
     * @throws IOException todo
     */
    public static void writeFile(String Str) {

        System.out.println((sdf.format(new Date()) + " --- " + Str + "\r\n"));
        String fileDatetemp = sdf1.format(new Date());
        boolean flag = fileDatetemp.equalsIgnoreCase(fileDate);
        if (!flag) {
            try {
                fileDate = fileDatetemp;
                fis = new FileOutputStream(System.getProperties().getProperty("user.home") + File.separator + filename,
                        true);
                // fw=new java.io.FileWriter(filename, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // pw.println(new String(Str.getBytes("utf-8"),"gbk"));
        try {
            fis.write((sdf.format(new Date()) + " --- " + Str + "\r\n").getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            fis.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void writeFile(String fileName, String Str) {
        FileOutputStream fos = null;
        //new File(fileName).deleteOnExit();
        try {
            fos = new FileOutputStream(fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println((sdf.format(new Date()) + " --- " + Str + "\r\n"));
        String fileDatetemp = sdf1.format(new Date());
        boolean flag = fileDatetemp.equalsIgnoreCase(fileDate);
        if (!flag) {
            try {
                fileDate = fileDatetemp;
                fos = new FileOutputStream(fileName, true);
                // fw=new java.io.FileWriter(filename, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // pw.println(new String(Str.getBytes("utf-8"),"gbk"));
        try {
            fos.write((sdf.format(new Date()) + " --- " + Str + "\r\n").getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void writeCSV(String fileName, String Str) {
        FileOutputStream fos = null;
        //new File(fileName).deleteOnExit();
        try {
            fos = new FileOutputStream(fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.write((Str + "\r\n").getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        writeFile("program starting....");
    }
}