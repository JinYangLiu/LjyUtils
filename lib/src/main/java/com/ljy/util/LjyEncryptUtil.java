package com.ljy.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Mr.LJY on 2017/12/26.
 * <p>
 * 各种数据加密的工具类
 */

public class LjyEncryptUtil {
    /**
     * 恺撒加密
     *
     * @param value 数据源（需要加密的数据）
     * @param key   秘钥，即偏移量
     * @return 返回加密后的数据
     * <p>
     * //字符转换成ASCII 码值
     * int ascii = array[i];
     * //字符偏移，例如a->b
     * ascii = ascii + key;
     * //ASCII 码值转换为char
     * char newChar = (char) ascii;
     * //替换原有字符
     * array[i] = newChar;
     * <p>
     * //以上4 行代码可以简写为一行
     * //array[i] = (char) (array[i] + key);
     */
    public static String encodeCaesar(String value, int key) {
        byte[] bytes = new byte[0];
        try {
            bytes = encodeCaesar(value.getBytes("utf-8"), key);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return LjyStringUtil.byte2hex(bytes);
    }

    public static byte[] encodeCaesar(byte[] value, int key) {
        for (int i = 0; i < value.length; ++i) {
            value[i] = (byte) (value[i] + key);
        }
        return value;
    }

    /**
     * 凯撒解密
     *
     * @param value 数据源（被加密后的数据）
     * @param key   秘钥，即偏移量
     * @return 返回解密后的数据
     */
    public static String decodeCaesar(String value, int key) {
        byte[] bytes = decodeCaesar(LjyStringUtil.hex2byte(value), key);
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static byte[] decodeCaesar(byte[] value, int key) {
        for (int i = 0; i < value.length; ++i) {
            value[i] = (byte) (value[i] - key);
        }
        return value;
    }


    public static String getAESKey() {
        return LjyStringUtil.getRandomStr(8);
    }

    public static String getDESIV() {
        return LjyStringUtil.getRandomStr(4);
    }

    /**
     * AES加密算法
     *
     * @param value 原始数据
     * @param key   密钥（最少16位）
     * @param isHex 加密结果格式：true为16进制字符串，false为base64字符串
     * @return
     */
    public static String encodeAES(String value, String key, boolean isHex) {
        byte[] bytes = new byte[0];
        try {
            bytes = encodeAES(value.getBytes("utf-8"), key);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return isHex ? LjyStringUtil.byte2hex(bytes) : LjyStringUtil.byte2base64(bytes);
    }

    static String e = "9238513401340235";

    public static byte[] encodeAES(byte[] value, String key) {
        if (key == null)
            return null;

        try {
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"0102030405060708
            IvParameterSpec iv = new IvParameterSpec(e.getBytes("utf-8"));// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(value);
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e1) {
            e1.printStackTrace();
        } catch (BadPaddingException e1) {
            e1.printStackTrace();
        } catch (InvalidKeyException e1) {
            e1.printStackTrace();
        } catch (IllegalBlockSizeException e1) {
            e1.printStackTrace();
        } catch (InvalidAlgorithmParameterException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密算法
     *
     * @param value 加密数据
     * @param key   密钥（最少16位）
     * @param isHex 加密结果格式：true为16进制字符串，false为base64字符串
     * @return
     */
    public static String decodeAES(String value, String key, boolean isHex) {
        byte[] bytes = decodeAES(isHex ? LjyStringUtil.hex2byte(value) : LjyStringUtil.base642byte(value), key);
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static byte[] decodeAES(byte[] value, String key) {
        // 判断Key是否正确
        if (key == null)
            return null;
        try {
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(e.getBytes("utf-8"));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(value);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (InvalidKeyException e1) {
            e1.printStackTrace();
        } catch (InvalidAlgorithmParameterException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e1) {
            e1.printStackTrace();
        } catch (BadPaddingException e1) {
            e1.printStackTrace();
        } catch (IllegalBlockSizeException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * DES加密
     *
     * @param value
     * @param key
     * @param ivStr
     * @return
     */
    public static String encodeDES(String value, String key, String ivStr) {
        byte[] bytes = new byte[0];
        try {
            bytes = encodeDES(value.getBytes("utf-8"), key, ivStr);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return LjyStringUtil.byte2hex(bytes);
    }

    public static byte[] encodeDES(byte[] value, String key, String ivStr) {
        try {
            byte[] ivs = ivStr.getBytes("utf-8");//StringToByte(ivStr, "UTF-8");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("utf-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(ivs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            return cipher.doFinal(value);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (InvalidKeyException e1) {
            e1.printStackTrace();
        } catch (InvalidAlgorithmParameterException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e1) {
            e1.printStackTrace();
        } catch (BadPaddingException e1) {
            e1.printStackTrace();
        } catch (InvalidKeySpecException e1) {
            e1.printStackTrace();
        } catch (IllegalBlockSizeException e1) {
            e1.printStackTrace();
        }
        return null;

    }

    /**
     * DES解密
     *
     * @param value
     * @param key
     * @param ivStr
     * @return
     */
    public static String decodeDES(String value, String key, String ivStr) {
        byte[] bytes = decodeDES(LjyStringUtil.hex2byte(value), key, ivStr);
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static byte[] decodeDES(byte[] value, String key, String ivStr) {

        byte[] ivs = new byte[0];//StringToByte(ivStr, "UTF-8");
        try {
            ivs = ivStr.getBytes("utf-8");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("utf-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(ivs);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return cipher.doFinal(value);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (InvalidKeyException e1) {
            e1.printStackTrace();
        } catch (InvalidAlgorithmParameterException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e1) {
            e1.printStackTrace();
        } catch (BadPaddingException e1) {
            e1.printStackTrace();
        } catch (InvalidKeySpecException e1) {
            e1.printStackTrace();
        } catch (IllegalBlockSizeException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    //生成密钥对
    public static KeyPair getRsaKey(int keyLength) {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keyLength <= 0 ? 1024 : keyLength);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e1) {
            return null;
        }
    }

    //公钥加密
    public static String encodeRSA(String value, PublicKey publicKey) {
        byte[] bytes = new byte[0];
        try {
            bytes = encodeRSA(value.getBytes("utf-8"), publicKey);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return LjyStringUtil.byte2hex(bytes);
    }

    public static byte[] encodeRSA(byte[] value, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");//java默认"RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(value);
        } catch (Exception e1) {
            return null;
        }
    }

    //私钥解密
    public static String decodeRSA(String value, PrivateKey privateKey) {
        byte[] bytes = decodeRSA(LjyStringUtil.hex2byte(value), privateKey);
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static byte[] decodeRSA(byte[] value, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(value);
        } catch (Exception e1) {
            return null;
        }
    }


    /**
     * 获取MD5
     *
     * @param value
     * @return
     */
    public static String getMD5(String value) {
        byte[] bytes = new byte[0];
        try {
            bytes = getMD5(value.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return LjyStringUtil.byte2hex(bytes);
    }

    public static byte[] getMD5(byte[] value) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(value);
            return md5.digest();//加密
        } catch (NoSuchAlgorithmException e1) {
            return null;
        }
    }

    public static final String SHA_256 = "SHA-256";
    public static final String SHA_512 = "SHA-512";

    /**
     * 获取哈希（默认SHA512）
     *
     * @param value   需要加密的字符
     * @param encName 选择加密方式。
     * @return 加密后的字符串
     */
    public static String getSHA(String value, String encName) {
        byte[] bytes = new byte[0];
        try {
            bytes = getSHA(value.getBytes("utf-8"), encName);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return LjyStringUtil.byte2hex(bytes);
    }

    public static byte[] getSHA(byte[] value, String encName) {
        try {
            if (encName == null) {
                encName = SHA_512;
            }
            MessageDigest md = MessageDigest.getInstance(encName);
            md.update(value);
            return md.digest();
        } catch (Exception e) {
            return null;
        }
    }



}
