package com.ljy.ljyutils.service;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.ljy.ljyutils.IMyAidlInterface;
import com.ljy.ljyutils.bean.Book;
import com.ljy.util.LjyLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.LJY on 2018/1/16.
 *
 * 远程服务：
 * 1. 与本地服务最大的区别是：远程Service与调用者不在同一个进程里（即远程Service是运行在另外一个进程）；而本地服务则是与调用者运行在同一个进程里
 */

public class MyAidlNormalService extends MyNormalService {

    IMyAidlInterface.Stub mStub=new IMyAidlInterface.Stub() {
        private ArrayList<Book> mBooks=new ArrayList<>();
        @Override
        public void callByService() throws RemoteException {
            LjyLogUtil.i("调用了callByService，客户端通过AIDL与远程后台成功通信");
            mBooks.add(new Book("红楼梦",800));
        }

        @Override
        public void addBookIn(Book bean) throws RemoteException {
            mBooks.add(bean);
        }

        @Override
        public void addBookout(Book bean) throws RemoteException {
            mBooks.add(bean);
        }

        @Override
        public void addBookInout(Book bean) throws RemoteException {
            mBooks.add(bean);
        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            return mBooks;
        }


    };

    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }
}
