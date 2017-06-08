package com.hengda.smart.common.autono;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * 将SSID转成AutoNo
 */
public class DESHelper {

    private byte[] desKey;

    public DESHelper(String desKey) {
        this.desKey = desKey.getBytes();
    }

    /**
     * DES加密
     *
     * @param plainText
     * @param mikey
     * @return
     * @throws Exception
     */
    public static byte[] desEncrypt(byte[] plainText, String mikey) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = mikey.getBytes();
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        byte data[] = plainText;
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;
    }


    /**
     * DES解密
     *
     * @param encryptText
     * @param mikey
     * @return
     * @throws Exception
     */
    public static byte[] desDecrypt(byte[] encryptText, String mikey) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = mikey.getBytes();
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        byte encryptedData[] = encryptText;
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return decryptedData;
    }


    /**
     * BASE64加密
     *
     * @param input
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String input, String key) throws Exception {
        byte[] bytes = desEncrypt(input.getBytes(), key);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    /**
     * BASE64解密
     *
     * @param input
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String input, String key) throws Exception {
        byte[] bytes = Base64.decode(input, Base64.DEFAULT);
        return new String(desDecrypt(bytes, key));
    }


    /**
     * 根据WiFi BSSID生成密钥
     *
     * @param bssid
     * @return
     */
    public static String genKeyByBSSID(String bssid) {
        String newString = bssid.substring(bssid.length() - 8, bssid.length() - 2);
        char[] lastString = bssid.substring(bssid.length() - 2, bssid.length()).toCharArray();
        if ((int) lastString[1] != '0') {
            int ascTep2 = (int) lastString[1];
            lastString[1] = (char) (ascTep2 - 1);
        } else {
            int ascTep1 = (int) lastString[0];
            lastString[1] = 'F';
            lastString[0] = (char) (ascTep1 - 1);
        }
        String yy = new String(lastString);
        return newString + yy;
    }

}