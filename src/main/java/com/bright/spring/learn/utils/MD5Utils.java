package com.bright.spring.learn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * MD5 工具类
 *
 * @author zhengyuan
 * @since 2021/03/31
 */
public class MD5Utils {
    private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);

    private static final String[] hexDigIts = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * MD5加密
     *
     * @param origin      字符
     * @param charsetName 编码
     * @return MD5加密后的结果
     */
    public static String MD5Encode(String origin, String charsetName) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (null == charsetName || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception e) {
            logger.error("md5 encode error, origin text is {}, charset is {}", origin, charsetName);
        }
        return resultString;
    }

    /**
     * byte数组 转 16进制
     *
     * @param b byte数组
     * @return 16进制
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    /**
     * byte 转 16进制
     *
     * @param b byte字节
     * @return 16进制
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }
}
