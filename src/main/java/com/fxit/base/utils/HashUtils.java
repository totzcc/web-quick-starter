package com.fxit.base.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HashUtils {
    private static final Map<String, FileHashCache> hashCache = new HashMap<>();

    public static FileHashCache getFileHash(File file) {
        FileHashCache cache = hashCache.get(file.getAbsolutePath());
        if (cache != null && System.currentTimeMillis() - cache.getCacheTime() > 600 * 1000)
            cache = null;
        if (cache == null) {
            String sha1 = getFileSha1(file);
            String md5 = getFileMD5(file);
            cache = new FileHashCache(file.getAbsolutePath(), md5, sha1, System.currentTimeMillis(), file.length());
            hashCache.put(file.getAbsolutePath(), cache);
        }
        return cache;
    }

    private static String getFileSha1(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return getFileHash(in, digest, 40);
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return null;
    }

    private static String getFileMD5(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return getFileHash(in, digest, 32);
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return null;
    }

    private static String getFileHash(FileInputStream in, MessageDigest digest, int i2) throws IOException {
        byte[] buffer = new byte[1024 * 1024 * 10];
        int len;
        while ((len = in.read(buffer)) > 0) {
            digest.update(buffer, 0, len);
        }
        String sha1 = new BigInteger(1, digest.digest()).toString(16);
        int length = i2 - sha1.length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                sha1 = "0" + sha1;
            }
        }
        return sha1;
    }


    public static String md5(String in) {
        return hash(in, "MD5");
    }

    public static String sha512(String in) {
        return hash(in, "SHA-512");
    }

    public static String sha256(String in) {
        return hash(in, "SHA-256");
    }

    public static String sha1(String in) {
        return hash(in, "SHA");
    }

    public static String hmacSha256(String in, String key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(in.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String tmp;
        for (int n = 0; b != null && n < b.length; n++) {
            tmp = Integer.toHexString(b[n] & 0XFF);
            if (tmp.length() == 1)
                hs.append('0');
            hs.append(tmp);
        }
        return hs.toString();
    }

    private static String hash(String in, String type) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance(type);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = in.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    @Data
    @AllArgsConstructor
    public static class FileHashCache implements Serializable {
        private String absolutePath;
        private String md5;
        private String sha1;
        private Long length;
        private Long cacheTime;
    }

    public static void main(String[] args) {
        System.out.println("md5:" + md5("123456"));
        System.out.println("sha1:" + sha1("123456"));
        System.out.println("sha256:" + sha256("123456"));
        System.out.println("sha512:" + sha512("123456"));
    }
}
