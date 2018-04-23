package com.ljy.ljyutils.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjySystemUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by LJY on 2018/4/23.
 */

public class TcpServerService extends Service {
    public static final int port = 8688;
    private boolean mIsServiceDestoryed = false;
    private String[] mDefinedMessages = new String[]{
            "你好",
            "哈喽",
            "扎喜德勒",
            "萨瓦迪卡",
            "库尼希瓦",
            "本竹",
    };

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class TcpServer implements Runnable {


        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                LjyLogUtil.e("establish tcp server failed,port:" + port);
                e.printStackTrace();
                return;
            }
            while (!mIsServiceDestoryed) {
                try {
                    //接收客户端请求
                    final Socket client = serverSocket.accept();
                    LjyLogUtil.i("accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void responseClient(Socket client) throws IOException {
        //用于接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //用于想客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来撩~~");
        while (!mIsServiceDestoryed) {
            String str = in.readLine();
            LjyLogUtil.i("msg from client:" + str);
            if (str == null) {
                //客户端断开连接
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
            LjyLogUtil.i("send:" + msg);
        }
        LjyLogUtil.i("client quit");
        //关闭流
        LjySystemUtil.clostStream(in);
        LjySystemUtil.clostStream(out);
    }
}
