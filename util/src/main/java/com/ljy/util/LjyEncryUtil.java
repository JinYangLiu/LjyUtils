package com.ljy.util;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Mr.LJY on 2017/12/26.
 *
 * 各种数据加密的工具类
 */

public class LjyEncryUtil {
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
        byte[] bytes = encodeCaesar(value.getBytes(), key);
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
        return new String(bytes);
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
     * @param value   原始数据
     * @param key   密钥（最少16位）
     * @param isHex 加密结果格式：true为16进制字符串，false为base64字符串
     * @return
     */
    public static String encodeAES(String value, String key, boolean isHex) {
        byte[] bytes = encodeAES(value.getBytes(), key);
        return isHex?LjyStringUtil.byte2hex(bytes):LjyStringUtil.byte2base64(bytes);
    }

    static String e = "9238513401340235";
    public static byte[] encodeAES(byte[] value, String key) {
        if (key == null)
            return null;
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher;// "算法/模式/补码方式"0102030405060708
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(e.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(value);
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * AES解密算法
     *
     * @param value   加密数据
     * @param key   密钥（最少16位）
     * @param isHex 加密结果格式：true为16进制字符串，false为base64字符串
     * @return
     */
    public static String decodeAES(String value, String key, boolean isHex) {
        byte[] bytes=decodeAES(isHex?LjyStringUtil.hex2byte(value):LjyStringUtil.base642byte(value),key);
        return new String(bytes);
    }

    public static byte[] decodeAES(byte[] value, String key) {
        try {
            // 判断Key是否正确
            if (key == null)
                return null;
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(e.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(value);
        } catch (Exception ex) {
            return null;
        }
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
        byte[] bytes=encodeDES(value.getBytes(),key,ivStr);
        return LjyStringUtil.byte2hex(bytes);
    }

    public static byte[] encodeDES(byte[] value, String key, String ivStr) {
        try {
            byte[] ivs = ivStr.getBytes();//StringToByte(ivStr, "UTF-8");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(ivs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            return cipher.doFinal(value);
        } catch (Exception e1) {
            return null;
        }

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
        byte[] bytes=decodeDES(LjyStringUtil.hex2byte(value),key,ivStr);
        return new String(bytes);
    }

    public static byte[] decodeDES(byte[] value, String key, String ivStr) {
        try {
            byte[] ivs = ivStr.getBytes();//StringToByte(ivStr, "UTF-8");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("utf-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(ivs);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return cipher.doFinal(value);
        } catch (Exception e1) {
            return null;
        }
    }

    //生成密钥对
    public static KeyPair getRsaKey(int keyLength) {
        KeyPairGenerator keyPairGenerator ;
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
        byte[] bytes=encodeRSA(value.getBytes(),publicKey);
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
        byte[] bytes=decodeRSA(LjyStringUtil.hex2byte(value),privateKey);
        return new String(bytes);
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
        byte[] bytes=getMD5(value.getBytes());
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

    public static String SHA_256 = "SHA-256";
    public static String SHA_512 = "SHA-512";

    /**
     * 获取哈希（默认SHA512）
     *
     * @param value  需要加密的字符
     * @param encName 选择加密方式。
     * @return 加密后的字符串
     */
    public static String getSHA(String value, String encName) {
        byte[] bytes=getSHA(value.getBytes(),encName);
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

    /**
     * 用字符串生成二维码,需要zxing.jar
     *
     * @param value
     * @return
     * @throws
     */
    public static Bitmap getQrCode(String value, int width, int height, boolean isAlpha) {
        try {
            //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            BitMatrix matrix = new MultiFormatWriter().encode(value,
                    BarcodeFormat.QR_CODE, width, height);
            //二维矩阵转为一维像素数组, 也就是一直横着排了
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        if (isAlpha) {
                            pixels[y * width + x] = 0x00ffffff;
                        } else {
                            pixels[y * width + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //通过像素数组生成bitmap,具体参考api
//            int[] pixels, int offset, int stride, int x, int y, int width, int height
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }




}
