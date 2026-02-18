
package com.ddzn.dd.framework.common.util.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * des 加解密工具类
 *
 * @author
 */
@Slf4j
public class DesUtils {

    private static String DES_ALGORITHM = "DES";
    private static String ALGORITHM_MODE = "/CBC/PKCS5Padding";

    public static String encryptByDES(String content, String key) {
        String result = "";

        try {
            Key desKey = desKeyGenerator(key);
            new IvParameterSpec(key.getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM + ALGORITHM_MODE);
            cipher.init(1, desKey, new IvParameterSpec(getIV()));
            byte[] byteResult = cipher.doFinal(content.getBytes("UTF-8"));
            result = Base64.encodeBase64String(byteResult);
        } catch (Exception var7) {
            log.error("encryptByDES error=" + var7.getMessage(), var7);
        }

        return result;
    }

    public static String decryptByDES(String content, String key) {
        String result = "";

        try {
            Key desKey = desKeyGenerator(key);
            new IvParameterSpec(key.getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM + ALGORITHM_MODE);
            cipher.init(2, desKey, new IvParameterSpec(getIV()));
            byte[] byteResult = cipher.doFinal(Base64.decodeBase64(content.getBytes("UTF-8")));
            result = new String(byteResult, "UTF-8");
        } catch (Exception var7) {
            log.error("decryptByDES error=" + var7.getMessage(), var7);
        }

        return result;
    }

    public static byte[] getIV() throws Exception {
        return "asdfivh7".getBytes("UTF-8");
    }

    private static SecretKey desKeyGenerator(String key) throws Exception {
        DESKeySpec desKey = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
        return keyFactory.generateSecret(desKey);
    }
}
