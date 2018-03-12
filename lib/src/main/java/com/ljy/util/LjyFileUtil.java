package com.ljy.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ljy.bean.DownloadBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

/**
 * Created by Mr.LJY on 2018/1/23.
 */

public class LjyFileUtil {

    public static boolean writeResponseBodyToDisk(ResponseBody responseBody, DownloadBean bean) {
        RandomAccessFile randomAccessFile = null;
        FileChannel channelOut = null;
        InputStream inputStream = null;
        try {
            long allLength = 0 == bean.getTotal() ? responseBody.contentLength() :
                    bean.getProgress() + responseBody.contentLength();

            inputStream = responseBody.byteStream();
            randomAccessFile = new RandomAccessFile(bean.getSaveFile(), "rwd");
            channelOut = randomAccessFile.getChannel();
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                    bean.getProgress(), allLength - bean.getProgress());
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (channelOut != null) {
                try {
                    channelOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * file转byte[]
     *
     * @param file
     * @return
     */
    public static byte[] getBytesFromFile(File file) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return buffer;
    }

    /**
     * InputStream 转 File
     *
     * @param is
     * @param file
     */
    public static boolean writeBytesToFile(InputStream is, File file) {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
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

    /**
     * 删除文件
     *
     * @param file 文件
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
                cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

}
