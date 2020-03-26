package com.nugget.common.utils;

/**
 * Redis所有Keys
 *
 * @author hb
 * @email 1247898199@qq.com
 * @date 2019-01-09
 */
public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }
}
