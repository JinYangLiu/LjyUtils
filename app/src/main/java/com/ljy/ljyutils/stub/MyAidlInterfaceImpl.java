package com.ljy.ljyutils.stub;

import android.os.RemoteException;

import com.ljy.ljyutils.IMyAidlInterface;
import com.ljy.ljyutils.bean.Book;
import com.ljy.util.LjyLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJY on 2018/4/25.
 */

public class MyAidlInterfaceImpl extends IMyAidlInterface.Stub {
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

}
