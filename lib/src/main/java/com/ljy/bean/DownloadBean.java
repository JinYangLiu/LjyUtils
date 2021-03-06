package com.ljy.bean;

import com.ljy.util.LjyRetrofitUtil;

import java.io.File;

import io.reactivex.disposables.Disposable;


/**
 * Created by Mr.LJY on 2018/1/23.
 */

public class DownloadBean {
    private String loadUrll;
    private File saveFile;
    private long progress;
    private long total=0;
    private boolean done;
    private LjyRetrofitUtil.ApiService mApiService;
    private Disposable mDisposable;

    public DownloadBean(String loadUrll, File fileName) {
        this.loadUrll = loadUrll;
        this.saveFile = fileName;
    }

    public Disposable getDisposable() {
        return mDisposable;
    }

    public void setDisposable(Disposable disposable) {
        this.mDisposable = disposable;
    }

    public LjyRetrofitUtil.ApiService getApiService() {
        return mApiService;
    }

    public void setApiService(LjyRetrofitUtil.ApiService apiService) {
        mApiService = apiService;
    }

    public String getLoadUrll() {
        return loadUrll;
    }

    public void setLoadUrll(String loadUrll) {
        this.loadUrll = loadUrll;
    }

    public File getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
