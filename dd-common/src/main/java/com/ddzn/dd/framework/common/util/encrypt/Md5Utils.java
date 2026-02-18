
package com.ddzn.dd.framework.common.util.encrypt;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * @author
 */
@Slf4j
public class Md5Utils {

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 对字符串做(32位小写)MD5
     *
     * @param origin 需要处理的字符串
     * @return 处理后的字符串。
     */
    public static String encode(String origin) {
        return encode(origin, "UTF-8");
    }


    public static String encode(String origin, String charset) {
        String result = null;
        try {
            result = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = new String(encodeHex((charset == null || "".equals(charset)) ? md.digest(result.getBytes()) : md.digest(result.getBytes(charset))));
        } catch (Exception e) {
            log.error("md5 encode exception:{}", e.getMessage(), e);
        }
        return result;
    }

    private static char[] encodeHex(byte[] bytes) {
        char[] chars = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }
}
