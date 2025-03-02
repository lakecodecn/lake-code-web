package cn.lakecode.web.utils;

import com.alibaba.fastjson2.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DataUtils {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final String INIT_VECTOR = "1122334455667788";

    /**
     * 解密数据
     *
     * @param data   T
     * @param aesKey key
     * @param <T>    object
     * @return String
     */
    public static <T> String encrypt(T data, String aesKey) {
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParams = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);
            String content = JSONObject.toJSONString(data);
            byte[] encryptedData = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("encrypt error", e);
        }
    }


    /**
     * 加密
     *
     * @param content 内容
     * @param aesKey  key
     * @param <T>     object
     * @return T
     */
    public static <T> T decrypt(String content, String aesKey, Class<T> clazz) {
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParams = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);

            byte[] originalData = cipher.doFinal(Base64.getDecoder().decode(content));
            String s = new String(originalData, StandardCharsets.UTF_8);
            return JSONObject.parseObject(s, clazz);
        } catch (Exception e) {
            throw new RuntimeException("decrypt error", e);
        }
    }

}
