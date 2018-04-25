package com.ljy.ljyutils.stub;

import android.os.RemoteException;

import com.ljy.ljyutils.ICompute;

/**
 * Created by LJY on 2018/4/25.
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
