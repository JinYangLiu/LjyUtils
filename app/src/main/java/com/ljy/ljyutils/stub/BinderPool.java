package com.ljy.ljyutils.stub;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.ljy.ljyutils.IBinderPool;
import com.ljy.ljyutils.service.BinderPoolService;
import com.ljy.util.LjyLogUtil;

import java.util.concurrent.CountDownLatch;

/**
 * Created by LJY on 2018/4/25.
 */

public class BinderPool {
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;
    //volatile变量,一种稍弱的同步机制，用来确保将变量的更新操作通知到其他线程。
    private static volatile BinderPool sInstance;
    private Context mContext;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    public BinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPool.class) {
                if (sInstance == null) {
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    private synchronized void connectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

    private IBinderPool mBinderPool;
    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        public IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                LjyLogUtil.i("binder died.");
                mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
                mBinderPool = null;
                connectBinderPoolService();
            }
        };

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //ignored
        }
    };

    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }
}
