package com.ljy.ljyutils;
// * AIDL的使用,之前都是一个AIDL文件对应一个Service,但是当业务规模扩大,存在10个,100个AIDL文件时呢,
// * 若仍然是一对一的,创建如此之多的service明显是不好的方案, 那么就需要将多个AIDL放在同一个service中管理
// * 故而又创建了此AIDL文件以做演示多AIDL情况

interface ISecurityCenter{
    String encrypt(String content);
    String decrypt(String password);
}