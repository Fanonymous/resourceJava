package com.nugget.common.utils;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by VOLL on 2017/5/19.
 */
public class NuStringUtils {

    private final static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    public static boolean isBlank(String str) {


        if(StringUtils.isBlank(str)){
            return true;
        }

        if(str.equals("NULL")||str.equals("null")){
            return true;
        }

        return false;
    }

    /**
     * 去掉字符串中的某个字符
     * @param resource
     * @param ch
     * @return
     */
    public static String remove(String resource,char ch){
        StringBuffer buffer=new StringBuffer();
        int position=0;
        char currentChar;

        while(position<resource.length()){
            currentChar=resource.charAt(position++);
            if(currentChar!=ch) buffer.append(currentChar);
        }
        return buffer.toString();
    }

    /**
     * 将对象中String字段进行处理, 如果是null, 则替换成""
     *
     * @param o
     */
    public static <T> void trim(T o) {
        if (o != null) {
            try {
                Field[] fields = o.getClass().getFields();
                for (int j = 0; j < fields.length; j++) {
                    if (fields[j].getType().getName().equals("java.lang.String")
                            && fields[j].get(o) == null) {
                        fields[j].set(o, "");//设置为空字串
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 字符串超出验证
     * @param str
     * @param num
     * @return
     */
    public static boolean isOverNum(String str,int num) {
        if(num==0)return false;
        if(isBlank(str))return false;
        if (str.length()>num) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串超过20验证
     * @param str
     * @return
     */
    public static boolean isOver20(String str) {
        if(isBlank(str))return false;
        if (str.length()>20) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串超过50验证
     * @param str
     * @return
     */
    public static boolean isOver50(String str) {
        if(isBlank(str))return false;
        if (str.length()>50) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串超过500验证
     * @param str
     * @return
     */
    public static boolean isOver500(String str) {
        if(isBlank(str))return false;
        if (str.length()>500) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断字符串对象是否为空
     * @param str
     * @return 如果为空，则返回true
     */
    public static boolean isObjectEmpty(Object object){
        return object == null || object.toString().trim().length() == 0;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }



    public static void main(String[] args) {
        System.out.println(NuStringUtils.replaceBlank("just do it!"));
    }

}
