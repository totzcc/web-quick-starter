package com.fxit.base.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    public static String md5(String text, String key) {
        return DigestUtils.md5Hex(text + key);
    }

    public static String md5(String text) {
        return md5(text, "");
    }

    public static boolean verify(String text, String key, String md5) {
        String md5Text = md5(text, key);
        return md5Text.equalsIgnoreCase(md5);
    }

    public static void main(String[] args) {
        System.out.println(md5("123", ""));
    }
}