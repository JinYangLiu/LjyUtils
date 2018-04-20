package com.ljy.ljyutils.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewTestActivity extends BaseActivity {

    @BindView(R.id.rootLayout)
    RelativeLayout mRootLayout;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_test);
        ButterKnife.bind(mActivity);
        initWebView();
        intIntent();
    }

    private void intIntent() {
        String url=getIntent().getStringExtra("url");
        LjyLogUtil.i("intent.url:"+url);
        mWebView.loadUrl(url);
    }

    private void initWebView() {
        //避免WebView内存泄露,不在xml中定义 Webview ，而是在需要的时候在Activity中创建，
        // 并且Context使用 getApplicationgContext(),在销毁activity时移除
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mRootLayout.addView(mWebView);

        //设置settings
        WebSettings webSettings = mWebView.getSettings();
        //支持JavaScript交互
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕
        //方法1
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //方法2
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        LayoutAlgorithm是一个枚举用来控制页面的布局，有三个类型：
//        1.NARROW_COLUMNS：可能的话使所有列的宽度不超过屏幕宽度
//        2.NORMAL：正常显示不做任何渲染
//        3.SINGLE_COLUMN：把所有内容放大webview等宽的一列中

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);//4.2以后设置默认可以自动播放音频
        }
        //5.0+设置混合模式。允许https 访问http
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //设置网页字体不跟随系统字体发生改变
        mWebView.getSettings().setTextZoom(100);
        //load本地
        //mWebView.loadUrl("file:///android_asset/hellotest.html");
        //load在线
//        mWebView.loadUrl("https://club.anxin.com/m2018/fanxian");
        //如果右侧的滚动条占据的地方出现了白边
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //是否可以后退
        mWebView.canGoBack();
        //是否可以前进
        mWebView.canGoForward();
        //以当前的index为起始点前进或者后退到历史记录中指定的steps
        //如果steps为负数则为后退，正数则为前进
        //mWebView.goBackOrForward(1);
        //设置网页缩放百分比
        mWebView.setInitialScale(75);
        //当你点击页面中的链接时，页面将会在你手机默认的浏览器上打开。
        //那如果想要页面在App内中打开的话，那么就得设置setWebViewClient：
        mWebView.setWebViewClient(new WebViewClient() {

            // webView默认是不处理https请求的，页面显示空白,
            // onReceivedSslError为webView处理ssl证书设置
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();//忽略 SSL 证书错误
                // handler.cancel();//挂起连接，为默认方式
                // handler.handleMessage(null);
            }

            //打开网页时不调用系统浏览器， 而是在本WebView中显示；
            //在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

            //设定加载开始的操作,可以设定一个loading的页面，告诉用户程序在等待网络响应。
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            //设定加载结束的操作,可以关闭loading 条
            @Override
            public void onPageFinished(WebView view, String url) {
            }

            //在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            //加载页面的服务器出现错误时（如404）调用
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                switch (errorCode) {
                    //case 404: ...  break;
                    default:
                        break;
                }
            }

        });
        //设置WebChromeClient
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LjyToastUtil.toast(mContext, "网页标题: " + title);
            }

            //获得网页的加载进度并显示
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                    LjyLogUtil.i(progress);
                } else {
                    LjyLogUtil.i("加载进度 100%");
                }
            }

            //支持javascript的警告框
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("JsAlert")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }

            //支持javascript的确认框
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("JsConfirm")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
                // 返回布尔值：判断点击时确认还是取消
                // true表示点击了确认；false表示点击了取消；
                return true;
            }

            //支持javascript输入框
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                final EditText et = new EditText(mActivity);
                et.setText(defaultValue);
                new AlertDialog.Builder(mActivity)
                        .setTitle(message)
                        .setView(et)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm(et.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();

                return true;
            }

            //文件上传:

            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "选择要上传的文件"), 111);
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            // For Android > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

            // For Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "选择要上传的文件"), 111);
                return true;
            }

        });

        /*
        对于Android调用JS代码的方法有2种：
            通过WebView的loadUrl（）
            通过WebView的evaluateJavascript（）
        对于JS调用Android代码的方法有3种：
            通过WebView的addJavascriptInterface（）进行对象映射
            通过 WebViewClient 的shouldOverrideUrlLoading ()方法回调拦截 url
            通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）
            方法回调拦截JS对话框alert()、confirm()、prompt（） 消息
        * */

    }

    public void onWebViewToolBarClick(View view) {
        switch (view.getId()) {
            case R.id.btn_goBack:
                mWebView.goBack();//后退网页
                break;
            case R.id.btn_goForword:
                mWebView.goForward();//前进网页
                break;
            case R.id.btn_clear:
                //清除网页访问留下的缓存
                //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
                mWebView.clearCache(true);

                //清除当前webview访问的历史记录
                //只会webview访问历史记录里的所有记录除了当前访问记录
                mWebView.clearHistory();

                //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
                mWebView.clearFormData();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //激活WebView为活跃状态，能正常执行网页的响应
        mWebView.onResume();
        //恢复pauseTimers状态
        mWebView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
        //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
        mWebView.onPause();
        //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
        //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
        mWebView.pauseTimers();
    }

    //在 Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，
    // 然后移除 WebView，再销毁 WebView，最后置空。
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁Webview
        //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
        //但是注意：webview调用destory时,webview仍绑定在Activity上
        //这是由于自定义webview构建时传入了该Activity的context对象
        //因此需要先从父容器中移除webview,然后再销毁webview:
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mRootLayout.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

}
