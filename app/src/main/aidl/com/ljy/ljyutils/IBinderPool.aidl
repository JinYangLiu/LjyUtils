package com.ljy.ljyutils;
//为Binder连接池创建的
interface IBinderPool{
    IBinder queryBinder(int binderCode);
}