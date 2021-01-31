package com.lagou.utils;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 3:34
 */
public class Uuid {
    public static String getUid() {
        String uuid = java.util.UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
