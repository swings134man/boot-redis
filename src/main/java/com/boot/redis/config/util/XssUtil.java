package com.boot.redis.config.util;

/**
 * @info : XssUtil
 */
public class XssUtil {
    public static String replaceXss(String value) {
        String returnVal = value;
        returnVal = returnVal.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        returnVal = returnVal.replaceAll("eval\\((.*)\\)", "");
        returnVal = returnVal.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        returnVal = returnVal.replaceAll("script", "");
        returnVal = returnVal.replaceAll("iframe", "");
        returnVal = returnVal.replaceAll("embed", "");

        return returnVal;
    }
}
