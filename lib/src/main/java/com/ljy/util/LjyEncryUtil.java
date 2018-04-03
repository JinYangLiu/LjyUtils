package com.ljy.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
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
            return new String(bytes,"utf-8");
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
     * @param value   原始数据
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
        return isHex?LjyStringUtil.byte2hex(bytes):LjyStringUtil.byte2base64(bytes);
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
     * @param value   加密数据
     * @param key   密钥（最少16位）
     * @param isHex 加密结果格式：true为16进制字符串，false为base64字符串
     * @return
     */
    public static String decodeAES(String value, String key, boolean isHex) {
        byte[] bytes=decodeAES(isHex?LjyStringUtil.hex2byte(value):LjyStringUtil.base642byte(value),key);
        try {
            return new String(bytes,"utf-8");
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
        byte[] bytes= new byte[0];
        try {
            bytes = encodeDES(value.getBytes("utf-8"),key,ivStr);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return LjyStringUtil.byte2hex(bytes);
    }

    public static byte[] encodeDES(byte[] value, String key, String ivStr) {
        try {
            byte[] ivs  = ivStr.getBytes("utf-8");//StringToByte(ivStr, "UTF-8");
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
        byte[] bytes=decodeDES(LjyStringUtil.hex2byte(value),key,ivStr);
        try {
            return new String(bytes,"utf-8");
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
        byte[] bytes= new byte[0];
        try {
            bytes = encodeRSA(value.getBytes("utf-8"),publicKey);
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
        byte[] bytes=decodeRSA(LjyStringUtil.hex2byte(value),privateKey);
        try {
            return new String(bytes,"utf-8");
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
        byte[] bytes= new byte[0];
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
     * @param value  需要加密的字符
     * @param encName 选择加密方式。
     * @return 加密后的字符串
     */
    public static String getSHA(String value, String encName) {
        byte[] bytes= new byte[0];
        try {
            bytes = getSHA(value.getBytes("utf-8"),encName);
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

    /**
     * 创建二维码
     *
     * @param content   content
     * @param widthPix  widthPix
     * @param heightPix heightPix
     * @param logoBm    logoBm
     * @return 二维码
     */
    public static Bitmap createQRCode(String content, int widthPix, int heightPix, Bitmap logoBm) {
        try {
            if (content == null || "".equals(content)) {
                return null;
            }
            // 配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
                    heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }




}
