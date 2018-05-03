package com.ljy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;

import net.bither.util.NativeUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Mr.LJY on 2017/12/26.
 */

public class LjyBitmapUtil {
    /**
     * 1.质量压缩
     *
     * @param bitmap     原图片
     * @param targetPath 要保存的指定目录
     * @param quality    0~100，要压缩到百分之几
     */
    public static void compressQuality(Bitmap bitmap, String targetPath, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(new File(targetPath));
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap compressQuality(Bitmap bitmap, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] compressBytes = baos.toByteArray();
        return BitmapFactory.decodeByteArray(compressBytes, 0, compressBytes.length);
    }

    /**
     * 2.尺寸压缩
     *
     * @param bitmap     原图片
     * @param targetPath 要保存的指定目录
     * @param ratio      要压缩到几分之一
     */
    public static void compressSize(Bitmap bitmap, String targetPath, int ratio) {
        Bitmap compressBitmap = compressSize(bitmap, ratio);
        compressQuality(compressBitmap, targetPath, 100);
    }

    public static Bitmap compressSize(Bitmap bitmap, int ratio) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio);
        canvas.drawBitmap(bitmap, null, rect, null);
        return result;

    }

    /**
     * 3.采样率压缩
     *
     * @param filePath     原图片
     * @param targetPath   要保存的指定目录
     * @param inSampleSize 数值越高，图片像素越低
     */
    public static void compressSample(String filePath, String targetPath, int inSampleSize) {
        Bitmap compressBitmap = compressSample(filePath, inSampleSize);
        compressQuality(compressBitmap, targetPath, 100);
    }

    public static Bitmap compressSample(String filePath, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(filePath, options);


    }

    /**
     * 4.哈夫曼压缩
     *
     * @param bit        原图片
     * @param targetPath 要保存的指定目录
     * @param quality    0~100，要压缩到百分之几
     * @param optimize   是否采用哈弗曼表数据计算 品质相差5-10倍
     */
    public static void compressHuffman(Bitmap bit, String targetPath, int quality, boolean optimize) {
        try {
            NativeUtil.compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality, targetPath.getBytes("utf-8"), optimize);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 5.混合终极方法（尺寸、质量、JNI压缩）
     *
     * @param image      bitmap对象
     * @param targetPath 要保存的指定目录
     * @param maxSizeKB  将指定的bitmap压缩到小于maxSizeKB,maxSizeKB最小为30KB
     * @Description: 通过JNI图片压缩把Bitmap保存到指定目录
     */
    public static void compressMix(Bitmap image, String targetPath, int maxSizeKB) {
        maxSizeKB = maxSizeKB > 30 ? maxSizeKB : 30;
        //step1：尺寸压缩
        // 获取尺寸压缩倍数
        int ratio = getRatioSize(image.getWidth(), image.getHeight());
        // 压缩原Bitmap到按对应尺寸压缩的bitmap
        Bitmap resultBitmap = compressSize(image, ratio);
        //step2：质量压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        if (resultBitmap==null)
            return;
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        // 循环判断如果压缩后图片是否大于最大值,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSizeKB) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            quality -= 10;
            if (quality < 10) {
                quality = 10;
                break;
            }
            // 这里压缩options%，把压缩后的数据存放到baos中
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        //step3：哈夫曼压缩
        // JNI调用哈夫曼压缩并保存图片到SD卡 这个关键
        compressHuffman(resultBitmap, targetPath, quality, true);
        // 释放Bitmap
        if (resultBitmap != null && !resultBitmap.isRecycled()) {
            resultBitmap.recycle();
            resultBitmap = null;
        }
    }

    /**
     * 计算缩放比
     *
     * @param currentWidth  当前图片宽度
     * @param currentHeight 当前图片高度
     * @param targetWidth   目标图片宽度
     * @param targetHeight  目标片高度
     * @return int 缩放比
     */
    private static int getRatioSize(int currentWidth, int currentHeight, int targetWidth, int targetHeight) {
        // 图片最大分辨率
        int imageWidth = targetWidth;
        int imageHeight = targetHeight;
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (currentWidth > currentHeight && currentWidth > imageWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = currentWidth / imageWidth;
        } else if (currentWidth < currentHeight && currentHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = currentHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }

    /**
     * 计算缩放比例：缩放到1080*1920
     *
     * @param currentWidth
     * @param currentHeight
     * @return
     */
    private static int getRatioSize(int currentWidth, int currentHeight) {
        return getRatioSize(currentWidth, currentHeight, 1080, 1920);
    }


    /**
     * 通过文件路径读获取Bitmap防止OOM以及解决图片旋转问题
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapFromFile(String filePath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //只读边,不读内容,如果将这个值置为true，那么在解码的时候将不会返回bitmap，只会返回这个bitmap的尺寸。
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, newOpts);
        //表示这个Bitmap的宽和高
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 获取尺寸压缩倍数,这个值是一个int，当它小于1的时候，将会被当做1处理，如果大于1，那么就会按照比例（1 / inSampleSize）缩小bitmap的宽和高、降低分辨率，大于1时这个值将会被处置为2的倍数。例如，width=100，height=100，inSampleSize=2，那么就会将bitmap处理为，width=50，height=50，宽高降为1 / 2，像素数降为1 / 4。
        newOpts.inSampleSize = getRatioSize(w, h);
        newOpts.inJustDecodeBounds = false;//读取所有内容
        //默认值是ARGB_8888，在这个模式下，一个像素点占用4bytes空间，一般对透明度不做要求的话，一般采用RGB_565模式，这个模式下一个像素点占用2bytes。
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        //这个值和透明度通道有关，默认值是true，如果设置为true，则返回的bitmap的颜色通道上会预先附加上透明度通道。
        newOpts.inPremultiplied = false;
        //这个值和抖动解码有关，默认值为false，表示不采用抖动解码,如果采用抖动解码，那么就会在这些色带上采用随机噪声色来填充，目的是让这张图显示效果更好，色带不那么明显
        newOpts.inDither = false;
        //设置这个Bitmap是否可以被缩放，默认值是true，表示可以被缩放
        newOpts.inScaled = true;
        //inPurgeable和inInputShareable：这两个值一般是一起使用，设置为true时，前者表示空间不够是否可以被释放，后者表示是否可以共享引用。这两个值在Android5.0后被弃用。
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        //这个值表示是否在解码时图片有更高的品质，仅用于JPEG格式。如果设置为true，则图片会有更高的品质，但是会解码速度会很慢。
        newOpts.inPreferQualityOverSpeed = false;
        newOpts.inTempStorage = new byte[32 * 1024];
        Bitmap bitmap = null;
        File file = new File(filePath);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fs != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, newOpts);
                //旋转图片
                int photoDegree = readPictureDegree(filePath);
                if (photoDegree != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(photoDegree);
                    // 创建新的图片
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 给图片添加水印
     *
     * @param targetBitmap   目标图片
     * @param watermarkText  水印文字
     * @param watermarkColor 水印颜色
     */
    public static void addWatermark(Bitmap targetBitmap, String watermarkText, int watermarkColor, String savePath) {
        Bitmap result = addWatermark(targetBitmap, watermarkText, watermarkColor);
        compressQuality(result, savePath, 100);
    }

    public static Bitmap addWatermark(Bitmap targetBitmap, String watermarkText, int watermarkColor) {
        if (TextUtils.isEmpty(watermarkText))
            return targetBitmap;
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 0; i < 9; i++) {
            stringBuffer.append(watermarkText);
            if (i!=8)
                stringBuffer.append("        ");
        }
        watermarkText=stringBuffer.toString();
        int width = targetBitmap.getWidth(), hight = targetBitmap.getHeight();
        Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); //建立一个空的BItMap
        Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些

        Rect src = new Rect(0, 0, targetBitmap.getWidth(), targetBitmap.getHeight());//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);//创建一个指定的新矩形的坐标
        canvas.save();
        canvas.drawBitmap(targetBitmap, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint
        canvas.restore();
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        float textSize = 20;
        if (width > 1440 || hight > 2560) {
            textSize = 80;
        } else if (width > 1080 || hight > 1920) {
            textSize = 60;
        } else if (width > 720 || hight > 1280) {
            textSize = 40;
        }
        textPaint.setTextSize(textSize);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setColor(watermarkColor);//采用的颜色
        Rect textBounds = new Rect();
        photoPaint.getTextBounds(watermarkText, 0, String.valueOf(watermarkText).length(), textBounds);
        float textHeight = textBounds.height();
        for (float j = -canvas.getWidth(); j < canvas.getHeight(); ) {
            canvas.save();
            canvas.translate(0, j);
            canvas.rotate(30);
            canvas.drawText(watermarkText, textHeight, textHeight, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
            canvas.restore();
            j = j + textHeight * 30;
        }
        return icon;
    }

    /**
     * bitmap转base64
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = "";
        ByteArrayOutputStream bos = null;
        try {
            if (null != bitmap) {
                bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);// 将bitmap放入字节数组流中

                bos.flush();// 将bos流缓存在内存中的数据全部输出，清空缓存
                bos.close();

                byte[] bitmapByte = bos.toByteArray();
                result = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * base64转bitmap
     */
    public static Bitmap base64ToBitmap(String base64String) {
        byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * 保存bitmap为File文件（无损）
     *
     * @param bitmap
     * @param targetPath
     */
    public static void bitmapToFile(Bitmap bitmap, String targetPath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(targetPath));
            fos.write(baos.toByteArray());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static Bitmap rotateBidmap( Bitmap bitmap, float degree) {
        Matrix matrix=new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }
}
