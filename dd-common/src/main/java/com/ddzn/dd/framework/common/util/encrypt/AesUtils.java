
package com.ddzn.dd.framework.common.util.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * AES-128 ECB加密.<br>
 *
 * <pre>
 * 字符集:UTF-8
 * 算法模式:ECB
 * 数据块:128位
 * 补码方式:PKCS5Padding
 * 加密结果编码方式:Base64
 * </pre>
 *
 * @author
 */
public class AesUtils {

    private static final String AES = "AES";
    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";

    /**
     * 加密成16进制字符串
     *
     * @param content  明文内容
     * @param password 密码（如手机号后四位）
     * @return 加密后16进制字符串
     * @throws Exception 加密失败抛出异常
     */
    public static String encryptToHex(String content, String password) throws Exception {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        String md5Password = DigestUtils.md5Hex(password);
        byte[] result = encrypt(data, md5Password);
        return Hex.encodeHexString(result);
    }

    /**
     * 解密16进制字符串
     *
     * @param hex      加密的16进制字符串
     * @param password 密码（如手机号后四位）
     * @return 解密后的明文
     * @throws Exception 解密失败抛出异常
     */
    public static String decryptFromHex(String hex, String password) throws Exception {
        byte[] data = Hex.decodeHex(hex.toCharArray());
        String md5Password = DigestUtils.md5Hex(password);
        byte[] contentData = decrypt(data, md5Password);
        return new String(contentData, StandardCharsets.UTF_8);
    }

    /**
     * AES加密
     */
    private static byte[] encrypt(byte[] data, String key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(key));
        return cipher.doFinal(data);
    }

    /**
     * AES解密
     */
    private static byte[] decrypt(byte[] data, String key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(key));
        return cipher.doFinal(data);
    }

    /**
     * 获取 AES SecretKeySpec（取前16字节作为key）
     */
    private static SecretKeySpec getSecretKeySpec(String key) {
        byte[] keyBytes = key.substring(0, 16).getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, AES);
    }

}
