package com.ljy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.ljy.lib.R;
import com.ljy.util.disklrucache.LjyDiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ljy on 2018/5/17.
 */

public class LjyImageLoader {
    private static final String TAG = "LjyImageLoader";
    //磁盘缓存可用空间大小（50M）
    private final long DISK_CACHE_SIZE = 1024 * 1024 * 50;
    //当前设备cpu核数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //线程池核心线程数
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    //线程池最大线程数
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    //闲置线程回收的超时时长
    private static final long KEEP_ALIVE_SECONDS = 30;
    private int TAG_KEY_URI = R.id.imageloader_uri;
    private  boolean mIsDiskLruCacheCreated=false;
    private int DISK_CACHE_INDEX=0;
    //线程工厂
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        //AtomicInteger是使用非阻塞算法实现并发控制,线程安全的进行加减操作
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "LjyImageLoader#" + mCount.getAndIncrement());
        }
    };
    //线程池
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), sThreadFactory);
    //获取UI线程的Handler
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            imageView.setImageBitmap(result.bitmap);
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if (uri != null && uri.equals(result.uri)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Log.w(TAG, "set image bitmap,but url has changed, ignored!");
            }
        }
    };
    private Context mContext;
    private LruCache<String, Bitmap> mMemoryCache;
    private LjyDiskLruCache mDiskLruCache;
    private int MESSAGE_POST_RESULT=1;
    private int IO_BUFFER_SIZE=1024*8;

    public LjyImageLoader(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        File diskCacheDir = LjyFileUtil.getDiskCacheDir(mContext, "bitmap");
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = LjyDiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static LjyImageLoader build(Context context){
        return new LjyImageLoader(context);
    }

    public void bindBitmap(final String uri,final ImageView imageView,
                           final int reqWidth,final int reqHeight){
        imageView.setTag(TAG_KEY_URI,uri);
        Bitmap bitmap= loadBitmapFromMemCache(uri);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        Runnable loadBitmapTask=new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1=loadBitmap(uri,reqWidth,reqHeight);
                if (bitmap1!=null){
                    LoaderResult result=new LoaderResult(imageView,uri,bitmap1);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result)
                            .sendToTarget();

                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    private Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        //1。内存中取
        Bitmap bitmap= loadBitmapFromMemCache(uri);
        if (bitmap!=null){
            return bitmap;
        }

        try {
            //2。磁盘中取
            bitmap=loadBitmapFromDiskCache(uri,reqWidth,reqHeight);
            if (bitmap!=null){
                return bitmap;
            }
            //3。网络中取
            bitmap=loadBitmapFromHttp(uri,reqWidth,reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap==null&&!mIsDiskLruCacheCreated){
            bitmap=downLoadBitmapFromUrl(uri);
        }
        return bitmap;

    }

    private void addBitmapToMemoryCache(String key ,Bitmap bitmap){
        if (getBitmapFromMemCache(key) ==null){
            mMemoryCache.put(key,bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private Bitmap loadBitmapFromMemCache(String uri) {
        return getBitmapFromMemCache(LjyEncryptUtil.getMD5(uri));
    }

    private Bitmap loadBitmapFromDiskCache(String uri, int reqWidth, int reqHeight) throws IOException {

        if (Looper.myLooper()==Looper.getMainLooper())
            Log.w(TAG, "load bitmap from UI thread, it's not recommended!");

        if (mDiskLruCache==null)
            return null;

        Bitmap bitmap=null;
        String key=LjyEncryptUtil.getMD5(uri);
        LjyDiskLruCache.Snapshot snapshot=mDiskLruCache.get(key);
        if (snapshot!=null){
            FileInputStream fileInputStream= (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            bitmap = LjyBitmapUtil.compressInSampleSize(fileInputStream.getFD(), reqWidth, reqHeight);
            if (bitmap!=null){
                addBitmapToMemoryCache(key,bitmap);
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) throws IOException {

        if (Looper.myLooper()==Looper.getMainLooper())
            throw new RuntimeException("can not visit network from UI Thread");

        if (mDiskLruCache==null)
            return null;

        String key=LjyEncryptUtil.getMD5(url);
        LjyDiskLruCache.Editor editor=mDiskLruCache.edit(key);
        if (editor!=null){
            OutputStream outputStream=editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url,outputStream))
                editor.commit();
            else
                editor.abort();
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url,reqWidth,reqHeight);
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection=null;
        BufferedOutputStream out=null;
        BufferedInputStream in=null;
        try {
            final URL url=new URL(urlString);
            urlConnection= (HttpURLConnection) url.openConnection();
            in=new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            out=new BufferedOutputStream(outputStream,IO_BUFFER_SIZE);
            int b;
            while ((b=in.read())!=-1){
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG,"Error in downloadUrlToStream: "+e);
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            LjySystemUtil.clostStream(out);
            LjySystemUtil.clostStream(in);
        }
        return false;
    }

    private Bitmap downLoadBitmapFromUrl(String urlString) {
        Bitmap bitmap=null;
        HttpURLConnection urlConnection=null;
        BufferedInputStream in=null;
        try {
            URL url=new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in =new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            bitmap=BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            Log.e(TAG,"Error in downLoadBitmapFromUrl: "+e);
        }finally {
            if (urlConnection!=null)
                urlConnection.disconnect();
            LjySystemUtil.clostStream(in);
        }
        return bitmap;
    }


    private long getUsableSpace(File path) {
        return LjyFileUtil.getFreeBytes(path.getAbsolutePath());
//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD) {
//            return path.getUsableSpace();
//        }
//        final StatFs statFs=new StatFs(path.getPath());
//        return statFs.getBlockSize()*statFs.getAvailableBlocks();
    }


    private static class LoaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }

}
