package com.nugget.common.validator;

import com.nugget.common.exception.RRException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 * @author hb
 * @email 1247898199@qq.com
 * @date 2019-01-09
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message);
        }
    }
    public static void validString(String param ,int length, String message) {
        if(param.length()>length){
            throw new RRException(message);
        }

    }

    public static void validInt(int param ,int length, String message) {
        if(param>length){
            throw new RRException(message);
        }

    }
}
