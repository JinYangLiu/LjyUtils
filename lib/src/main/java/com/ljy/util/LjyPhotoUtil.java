package com.ljy.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.ljy.lib.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;

/**
 * Created by Mr.LJY on 2018/1/5.
 * 选取图片和拍照
 */
public class LjyPhotoUtil {

    //调用系统相机的Code
    private static final int REQUEST_TAKE_PHOTO_CODE = 1001;
    //拍照裁剪的Code
    private static final int REQUEST_TAKE_PHOTO_CROP_CODE = 1003;
    //调用系统图库的Code
    private static final int REQUEST_TAKE_PICTURE_CODE = 1002;
    //调用系统图库裁剪Code
    private static final int REQUEST_TAKE_PICTURE_CROP_CODE = 1004;
    //裁剪的Code
    private static final int REQUEST_TAKE_CROP_CODE = 1005;
    //截取图片的高度
    private static final int REQUEST_HEIGHT = 400;
    //截取图片的宽度
    private static final int REQUEST_WIDTH = 400;
    //用来存储照片的URL
    private Uri photoURL;
    //调用照片的Activity
    private Activity activity;
    //回调函数
    private CameraResult cameraResult;
    private Uri fileURL;

    public LjyPhotoUtil(Activity activity, CameraResult cameraResult) {
        this.activity = activity;
        this.cameraResult = cameraResult;
    }

    /**
     * 拍照并保存到指定path
     * @param path
     */
    public void doCamera(String path) {
        doCamera(path2Uri(path));
    }

    public void doCamera(Uri uri) {
        activity.startActivityForResult(startTakePhoto(uri), REQUEST_TAKE_PHOTO_CODE);
    }

    /**
     * 拍照后裁剪并保存到指定path
     */
    public void doCameraAndCut(String path) {
        doCameraAndCut( path2Uri(path));
    }

    public void doCameraAndCut(Uri uri) {
        activity.startActivityForResult(startTakePhoto(uri), REQUEST_TAKE_PHOTO_CROP_CODE);
    }

    /**
     * 从系统相册选取单张图片
     */
    public void getPicture() {
        activity.startActivityForResult(startTakePicture(), REQUEST_TAKE_PICTURE_CODE);
    }

    /**
     * 从系统相册选取单张图片后裁剪并保存到指定path
     * @param path
     */
    public void getPictureAndCut(String path) {
        fileURL = Uri.fromFile(new File(path));
        activity.startActivityForResult(startTakePicture(), REQUEST_TAKE_PICTURE_CROP_CODE);
    }

    public void getPictures(Activity mActivity,int requestCode){
        //Matisse，来自知乎的PhotoPicker
        Matisse.from(mActivity)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Dracula)
                .countable(false)//是否显示所选的图片是第几个（右上角标），false则以对号标识选中
                .maxSelectable(9)
                .capture(false)//是否可以拍照，如果允许需要另行配置，具体可查看Matisse相关文章
                .imageEngine(new GlideEngine())
                .forResult(requestCode);
    }


    //在Activity.onActivityResult中调用
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //调用相机
                case REQUEST_TAKE_PHOTO_CODE:
                    cameraResult.onSuccess(fileURL.getPath());
                    break;
                //调用相机,裁剪
                case REQUEST_TAKE_PHOTO_CROP_CODE:
                    activity.startActivityForResult(takeCropPicture(photoURL, REQUEST_HEIGHT, REQUEST_WIDTH), REQUEST_TAKE_CROP_CODE);
                    break;
                //选择系统图库
                case REQUEST_TAKE_PICTURE_CODE:
                    //获取系统返回的照片的Uri
                    photoURL = intent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    //从系统表中查询指定Uri对应的照片
                    Cursor cursor = activity.getContentResolver().query(photoURL, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);  //获取照片路径
                    cursor.close();
                    if (!TextUtils.isEmpty(picturePath)) {
                        cameraResult.onSuccess(picturePath);
                    } else {
                        cameraResult.onFail("文件没找到");
                    }
                    break;
                //选择系统图库.裁剪
                case REQUEST_TAKE_PICTURE_CROP_CODE:
                    photoURL = intent.getData();
                    activity.startActivityForResult(takeCropPicture(photoURL, REQUEST_HEIGHT, REQUEST_WIDTH), REQUEST_TAKE_CROP_CODE);
                    break;
                //裁剪之后的回调
                case REQUEST_TAKE_CROP_CODE:
                    String path = fileURL!=null?fileURL.getPath():"";
                    String path2 = getPic2Uri(photoURL, activity);
                    cameraResult.onSuccess(TextUtils.isEmpty(path)?path2:path);
                    break;
                default:
                    break;
            }
        }
    }

    //在Activity.onRestoreInstanceState 中调用
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        photoURL = savedInstanceState.getParcelable("photoURL");
    }

    //在Activity.onSaveInstanceState 中调用
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("photoURL", photoURL);
    }

    //通过URI得到图片路径
    private static String getPic2Uri(Uri contentUri, Context context) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            String filePath = "";
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(proj[0]);
                cursor.moveToFirst();
                filePath = cursor.getString(columnIndex);
                // 4.0以上的版本会自动关闭 (4.0--14; 4.0.3--15)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    cursor.close();
                }
            }
            return filePath;
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    private Uri path2Uri(String path) {
//        Uri uri= Uri.fromFile(new File(path));
        // 下面三句，解决 Android N 调用相册crash- FileUriExposedException
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, path);
        Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        photoURL = uri;
         fileURL = Uri.fromFile(new File(path));
        return uri;
    }


    //调用系统照相机，对Intent参数进行封装
    private Intent startTakePhoto(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//将拍取的照片保存到指定URI
        return intent;
    }

    //调用系统图库,对Intent参数进行封装
    private Intent startTakePicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");//从所有图片中进行选择
        return intent;
    }

    //调用系统裁剪图片，对Intent参数进行封装
    private Intent takeCropPicture(Uri uri, int with, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", with);
        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);//黑边

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileURL);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        return intent;
    }


    //回调实例
    public interface CameraResult {
        //成功回调
        void onSuccess(String filePath);

        //失败
        void onFail(String message);
    }
}