package com.ljy.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mr.LJY on 2017/12/26.
 */

public class LjyBitmapUtil {
    /**
     * 质量压缩
     *
     * @param bitmap
     * @param quality
     * @param file
     */
    public static void compressQuality(Bitmap bitmap, int quality, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 尺寸压缩
     *
     * @param bitmap
     * @param file
     */
    public static void compressSize(Bitmap bitmap, File file) {
        int ratio = 8;//尺寸压缩比例
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio);
        canvas.drawBitmap(bitmap, null, rect, null);

        compressQuality(result, 100, file);
    }

    /**
     * 采样率压缩
     *
     * @param filePath
     * @param file
     */
    public static void compressSample(String filePath, File file) {
        int inSampleSize = 8;//采样率设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        compressQuality(bitmap, 100, file);
    }





    /**
     * 通过文件路径读获取Bitmap防止OOM以及解决图片旋转问题
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapFromFile(String filePath){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //只读边,不读内容,如果将这个值置为true，那么在解码的时候将不会返回bitmap，只会返回这个bitmap的尺寸。
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, newOpts);
        //表示这个Bitmap的宽和高
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 获取尺寸压缩倍数,这个值是一个int，当它小于1的时候，将会被当做1处理，如果大于1，那么就会按照比例（1 / inSampleSize）缩小bitmap的宽和高、降低分辨率，大于1时这个值将会被处置为2的倍数。例如，width=100，height=100，inSampleSize=2，那么就会将bitmap处理为，width=50，height=50，宽高降为1 / 2，像素数降为1 / 4。
        newOpts.inSampleSize = getRatioSize(w,h);
        newOpts.inJustDecodeBounds = false;//读取所有内容
        //默认值是ARGB_8888，在这个模式下，一个像素点占用4bytes空间，一般对透明度不做要求的话，一般采用RGB_565模式，这个模式下一个像素点占用2bytes。
        newOpts.inPreferredConfig= Bitmap.Config.RGB_565;
        //这个值和透明度通道有关，默认值是true，如果设置为true，则返回的bitmap的颜色通道上会预先附加上透明度通道。
        newOpts.inPremultiplied = false;
        //这个值和抖动解码有关，默认值为false，表示不采用抖动解码,如果采用抖动解码，那么就会在这些色带上采用随机噪声色来填充，目的是让这张图显示效果更好，色带不那么明显
        newOpts.inDither = false;
        //设置这个Bitmap是否可以被缩放，默认值是true，表示可以被缩放
        newOpts.inScaled = true;
        //inPurgeable和inInputShareable：这两个值一般是一起使用，设置为true时，前者表示空间不够是否可以被释放，后者表示是否可以共享引用。这两个值在Android5.0后被弃用。
        newOpts.inPurgeable=true;
        newOpts.inInputShareable=true;
        //这个值表示是否在解码时图片有更高的品质，仅用于JPEG格式。如果设置为true，则图片会有更高的品质，但是会解码速度会很慢。
        newOpts.inPreferQualityOverSpeed=false;
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
            if(fs!=null){
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(),null,newOpts);
                //旋转图片
                int photoDegree = readPictureDegree(filePath);
                if(photoDegree != 0){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(photoDegree);
                    // 创建新的图片
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(fs!=null) {
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
     * 计算缩放比
     * @param bitWidth 当前图片宽度
     * @param bitHeight 当前图片高度
     * @return int 缩放比
     */
    private static int getRatioSize(int bitWidth, int bitHeight) {
        // 图片最大分辨率
        int imageWidth = 1080;
        int imageHeight = 1920;
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageWidth;
        } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }

    /**
     *
     * 读取图片属性：旋转的角度
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
