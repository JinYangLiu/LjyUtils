package net.bither.util;

import android.graphics.Bitmap;

/**
 * JNI图片压缩工具类
 */
public class NativeUtil {

	/**
	 * 调用底层 bitherlibjni.c中的方法
	 */
	public static native String compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes,
											   boolean optimize);
	/**
	 * 加载lib下两个so文件
	 */
	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("bitherjni");
	}

}