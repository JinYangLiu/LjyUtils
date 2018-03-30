package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * RxJava: 基于事件流的链式调用
 */
public class RxJavaTestActivity extends BaseActivity {

    @BindView(R.id.textViewShowRxInfo)
    TextView mTextViewShow;

    @BindView(R.id.form)
    LinearLayout form;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.ageTextView)
    TextView ageTextView;
    @BindView(R.id.jobTextView)
    TextView jobTextView;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.form2)
    LinearLayout form2;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.ed)
    EditText ed;

    @BindView(R.id.btnFangDou)
    Button btnFangDou;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_test);
        ButterKnife.bind(mActivity);
    }

    public void onRxJavaBtnClick(View view) {
        mTextViewShow.append("----------" + ((Button) view).getText() + "----------\n");
        LjyLogUtil.setAppendLogMsg(true);
        boolean isShowText = false;
        switch (view.getId()) {
            case R.id.btnSimple:
                //1.基本使用
                testSimple();
                isShowText = true;
                break;
            case R.id.btnMap:
                //2.map变换操作符
                //对 被观察者发送的每1个事件都通过 指定的函数 处理，从而变换成另外一种事件
                testMap();
                break;
            case R.id.btnFlatMap:
                //3.FlatMap变换操作符
                //将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送
                //注：新合并生成的事件序列顺序是无序的，即 与旧序列发送事件的顺序无关
                testFlatMap();
                break;
            case R.id.btnConcatMap:
                //4.ConcatMap变换操作符
                //类似FlatMap（）操作符,拆分 & 重新合并生成的事件序列 的顺序 = 被观察者旧序列生产的顺序
                testConcatMap();
                break;
            case R.id.btnBuffer:
                //4.Buffer变换操作符
                //定期从 被观察者（Obervable）需要发送的事件中 获取一定数量的事件 & 放到缓存区中，最终发送
                testBuffer();
                isShowText = true;
                break;
            case R.id.btnRegisterLogin:
                //进行嵌套网络请求：即在第1个网络请求成功后，继续再进行一次网络请求
                //如 先进行 用户注册 的网络请求, 待注册成功后回再继续发送 用户登录 的网络请求
//                testRegisterLoginNormal();
                testRegisterLogin();
                break;
            case R.id.btnConcat:
                //5.Concat
                //组合多个被观察者一起发送数据，合并后 按发送顺序串行执行
                testConcat();
                break;
            case R.id.btnMerge:
                //6.merge
                //组合多个被观察者一起发送数据，合并后 按时间线并行执行
                testMerge();
                break;
            case R.id.btnZip:
                //7.zip
                //合并 多个被观察者（Observable）发送的事件，生成一个新的事件序列（即组合过后的事件序列），并最终发送
                testZip();
                break;
            case R.id.btnReduce:
                //8.Reduce
                //把被观察者需要发送的事件聚合成1个事件 & 发送
                testReduce();
                isShowText = true;
                break;
            case R.id.btnCollect:
                //9.Collect
                //将被观察者Observable发送的数据事件收集到一个数据结构里
                testCollect();
                isShowText = true;
                break;
            case R.id.btnStartWith:
                //10.startWith
                //在一个被观察者发送事件前，追加发送一些数据 / 一个新的被观察者
                testStartWith();
                isShowText = true;
                break;
            case R.id.btnCount:
                //10.count
                //统计被观察者发送事件的数量
                testCount();
                isShowText = true;
                break;
            case R.id.btnCache:
                //10.Cache
                //从磁盘 / 内存缓存中 获取缓存数据
                testCache();
                isShowText = true;
                break;
            case R.id.btnLianHe:
                //联合判断
                testLianHe();
                break;
            case R.id.btnDo:
                //11.do系列操作符的使用
                //在事件发送 & 接收的整个生命周期过程中进行操作,如发送事件前的初始化、发送事件后的回调请求等
                testDo();
                isShowText = true;
                break;
            case R.id.btnRepeat:
                //12.repeatWhen实现网络请求轮询
                testRepeat();
                break;
            case R.id.btnRetryWhen:
                //13.retryWhen实现网络出错重连
                testRetryWhen();
                break;
            case R.id.btnFilter:
                //14.Filter 过滤操作符
                //通过设置指定的过滤条件，当且仅当该事件满足条件,才发送
                testFilter();
                isShowText = true;
                break;
            case R.id.btnOfType:
                //15.ofType 过滤操作符
                //过滤 特定数据类型的数据
                testOfType();
                isShowText = true;
                break;
            case R.id.btnSkip:
                //15.skip 过滤操作符
                //跳过某个事件
                testSkip();
                isShowText = true;
                break;
            case R.id.btnDistinct:
                //16.Distinct 过滤操作符
                //过滤事件序列中重复的事件 / 连续重复的事件
                testDistinct();
                isShowText = true;
                break;
            case R.id.btnTake:
                //17.take 过滤操作符
                //通过设置指定的事件数量，仅发送特定数量的事件
                testTake();
                isShowText = true;
                break;
            case R.id.btnThrottle:
                //18.throttleFirst/throttleLast
                //在某段时间内，只发送该段时间内第1次事件 / 最后1次事件
                //可以用于去抖,防止一段时间内多次触发
                //或者输入框的监听
                testThrottle();
                break;
            case R.id.btnElement:
                //18.Element
                testElement();
                isShowText = true;
                break;
            case R.id.btnDebounce:
                //联想搜索优化
                testDebounce();
                break;
            case R.id.btnSteadyShot:
                //功能防抖
                testSteadyShot();
                break;
            case R.id.btnAll:
                //19.all 操作符
                //判断发送的每项数据是否都满足 设置的函数条件
                //若满足，返回 true；否则，返回 false
                testAll();
                isShowText = true;
                break;
            case R.id.btnTakeWhile:
                //20.takeWhile 操作符
                //判断发送的每项数据是否满足 设置函数条件
                // 若发送的数据满足该条件，则发送该项数据；否则不发送
                testTakeWhile();
                isShowText = true;
                break;
            case R.id.btnSkipWhile:
                //21.skipWhile 操作符
                //判断发送的每项数据是否满足 设置函数条件
                // 直到该判断条件 = false时，才开始发送Observable的数据
                testSkipWhile();
                isShowText = true;
                break;
            case R.id.btnTakeUntil:
                //22.takeUntil 操作符
                //执行到某个条件时，停止发送事件
                testTakeUntil();
                isShowText = true;
                break;
            case R.id.btnSkipUntil:
                //23.skipUntil 操作符
                //等到 skipUntil（） 传入的Observable开始发送数据，（原始）第1个Observable的数据才开始发送数据
                testSkipUntil();
                isShowText = true;
                break;
            case R.id.btnSequenceEqual:
                //24.SequenceEqual 操作符
                //判定两个Observables需要发送的数据是否相同
                testSequenceEqual();
                isShowText = true;
                break;
            case R.id.btnContains:
                //25.Contains 操作符
                //判断发送的数据中是否包含指定数据
                testContains();
                isShowText = true;
                break;
            case R.id.btnIsEmpty:
                //26.IsEmpty 操作符
                //判断发送的数据是否为空
                testIsEmpty();
                isShowText = true;
                break;
            case R.id.btnAmb:
                //27.amb 操作符
                //当需要发送多个 Observable时，只发送 先发送数据的Observable的数据，而其余 Observable则被丢弃。
                testAmb();
                break;
            case R.id.btnDefaultIfEmpty:
                //27.DefaultIfEmpty 操作符
                //在不发送任何有效事件（ Next事件）、仅发送了 Complete 事件的前提下，发送一个默认值
                testDefaultIfEmpty();
                isShowText = true;
                break;
            case R.id.btnBackPressure:
                //背压策略
                //观察者 & 被观察者 之间存在2种订阅关系：同步 & 异步
                //对于异步订阅关系，存在 被观察者发送事件速度 与观察者接收事件速度 不匹配的情况
                //问题: 被观察者 发送事件速度太快，而观察者 来不及接收所有事件，从而导致观察者
                // 无法及时响应 / 处理所有发送过来事件的问题，最终导致缓存区溢出、事件丢失 & OOM
                testBackPressure();
                break;
            default:
                break;

        }
        if (isShowText)
            mTextViewShow.append(LjyLogUtil.getAllLogMsg());
        LjyLogUtil.setAppendLogMsg(false);
    }

    private void testBackPressure() {
        //出现发送 & 接收事件严重不匹配的问题
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; ; i++) {
                            try {
                                LjyLogUtil.i("发送了事件:" + i);
//                                LjyToastUtil.toast(mContext,"发送了事件:" + i );
                                Thread.sleep(100);
                                // 发送事件速度：10ms / 个
                                subscriber.onNext(i);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("onCompleted");
//                        mTextViewShow.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("onError:" + e.getMessage());
//                        mTextViewShow.append("onError:" + e.getMessage() + "\n");
                    }

                    @Override
                    public void onNext(Integer num) {
                        //接收事件5s/个
                        try {
                            Thread.sleep(5000);
                            LjyLogUtil.i("onNext接收到了事件:" + num);
//                            mTextViewShow.append("onNext接收到了事件:" + num + "\n");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });


    }

    private void testDefaultIfEmpty() {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        //不发送任何有效事件onNext、仅发送了 Complete
                        subscriber.onCompleted();
                    }
                })
                .defaultIfEmpty("我是默认值哦")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("对Complete事件作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("对Error事件作出响应:" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        LjyLogUtil.i("onNext接收到了事件:" + s);
                    }
                });
    }

    private void testAmb() {
        Observable.amb(
                Observable.just(1, 2, 3).delay(1, TimeUnit.SECONDS),
                Observable.just(4, 5, 6))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("发送数据是:" + integer);
                        mTextViewShow.append("发送数据是:" + integer + "\n");
                    }
                });
    }

    private void testIsEmpty() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
//                        subscriber.onNext(1);
                        subscriber.onCompleted();
                    }
                })
                .isEmpty()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        LjyLogUtil.i("发送数据是否为空:" + aBoolean);
                    }
                });
    }

    private void testContains() {
        Observable.just(1, 2, 3, 4, 5)
                .contains(3)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        LjyLogUtil.i("1,2,3,4,5 是否包含3: " + aBoolean);
                    }
                });
    }

    private void testSequenceEqual() {
        Observable
                .sequenceEqual(
                        Observable.just(1, 2, 3),
                        Observable.just(1, 2, 3))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        LjyLogUtil.i("1,2,3 与 1,2,3 是否相同:" + aBoolean);
                    }
                });
    }

    private void testSkipUntil() {
        // （原始）第1个Observable：每隔1s发送1个数据 = 从0开始，每次递增1
        Observable.interval(1, TimeUnit.SECONDS)
                //  第2个Observable：延迟5s后开始发送1个Long型数据
                .skipUntil(Observable.timer(5, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("onCompleted");
                        mTextViewShow.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("onError:" + e.getLocalizedMessage());
                        mTextViewShow.append("onError:" + e.getLocalizedMessage() + "\n");
                    }

                    @Override
                    public void onNext(Long num) {
                        LjyLogUtil.i("onNext:" + num);
                        mTextViewShow.append("onNext:" + num + "\n");

                    }
                });
    }

    private void testTakeUntil() {
        LjyLogUtil.i("1, 2, 3, 4, 5, 6, 7:");
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .takeUntil(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        // 当发送的数据满足==5时，就停止发送Observable的数据
                        //(满足条件就不要后面的了)
                        return integer == 5;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("发送了数据:" + integer);
                    }
                });
        //该判断条件也可以是Observable，即 等到 takeUntil传入的Observable开始发送数据，
        // （原始）第1个Observable的数据停止发送数据
        Observable.interval(1, TimeUnit.SECONDS)
                // 第2个Observable：延迟5s后开始发送1个Long型数据
                .takeUntil(Observable.timer(5, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("onCompleted");
                        mTextViewShow.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("onError:" + e.getLocalizedMessage());
                        mTextViewShow.append("onError:" + e.getLocalizedMessage() + "\n");
                    }

                    @Override
                    public void onNext(Long num) {
                        LjyLogUtil.i("onNext:" + num);
                        mTextViewShow.append("onNext:" + num + "\n");

                    }
                });
    }

    private void testSkipWhile() {
        LjyLogUtil.i("1, 2, 3, 4, 5, 6, 7:");
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .skipWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        // 直到判断条件不成立 = false = 发射的数据≥5，才开始发送数据
                        //(就是当满足条件时过滤掉事件)
                        return integer < 5;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("发送了数据:" + integer);
                    }
                });
    }

    private void testTakeWhile() {
        LjyLogUtil.i("1, 2, 3, 4, 5, 6, 7:");
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .takeWhile(new Func1<Integer, Boolean>() {
                    // 当发送的数据满足<5时，才发送Observable的数据
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 5;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("发送了数据:" + integer);
                    }
                });
    }

    private void testAll() {
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .all(new Func1<Integer, Boolean>() {
                    // 该函数用于判断Observable发送的数据是否都满足integer<5
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 5;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        LjyLogUtil.i("1,2,3,4,5,6,7是否都小于5:" + aBoolean);
                    }
                });
    }

    private void testSteadyShot() {
        mTextViewShow.append("两秒内只能一次:\n");
        btnFangDou.setVisibility(View.VISIBLE);
        RxView.clicks(btnFangDou)
                .throttleFirst(2, TimeUnit.SECONDS)////两秒内只能发送第一次事件
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("onCompleted");
                        mTextViewShow.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("onError:" + e.getLocalizedMessage());
                        mTextViewShow.append("onError:" + e.getLocalizedMessage() + "\n");
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        LjyLogUtil.i("onNext:发送了网络请求");
                        mTextViewShow.append("onNext:发送了网络请求\n");

                    }
                });

    }

    private void testDebounce() {
        form2.setVisibility(View.VISIBLE);
        RxTextView
                .textChanges(ed)
                .debounce(1, TimeUnit.SECONDS)
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        tv.setText("发送给服务器去查询的字符 = " + charSequence.toString());
                    }
                });

    }

    private void testElement() {
        //firstElement
        Observable
                .just(1, 2, 3, 4, 5)
                .first()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("获取到的第一个事件是： " + integer);
                    }
                });
        Observable
                .just(1, 2, 3, 4, 5)
                .last()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("获取到的最后1个事件是： " + integer);
                    }
                });
        Observable
                .just(1, 2, 3, 4, 5)
                .elementAt(3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("elementAt获取到的事件是： " + integer);
                    }
                });
        Observable
                .just(1, 2, 3, 4, 5)
                .elementAtOrDefault(9, 100)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("elementAtOrDefault获取到的事件是： " + integer);
                    }
                });
    }

    private void testThrottle() {
        //在某段时间内，只发送该段时间内第1次事件
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 1; i <= 20; i++) {
                            subscriber.onNext(i);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        subscriber.onCompleted();
                    }
                })
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("throttleFirst_onCompleted");
                        mTextViewShow.append("throttleFirst_onCompleted" + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer item) {
                        LjyLogUtil.i("throttleFirst_获取到的元素是： " + item);
                        mTextViewShow.append("throttleFirst_获取到的元素是： " + item + "\n");
                    }
                });
        LjyLogUtil.i("-----------------------------");
        mTextViewShow.append("-----------------------------\n");
        //在某段时间内，只发送该段时间内最后1次事件
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 1; i <= 20; i++) {
                            subscriber.onNext(i);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        subscriber.onCompleted();
                    }
                })
                .throttleLast(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("throttleLast_onCompleted");
                        mTextViewShow.append("throttleLast_onCompleted" + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer item) {
                        LjyLogUtil.i("throttleLast_获取到的元素是： " + item);
                        mTextViewShow.append("throttleLast_获取到的元素是： " + item + "\n");
                    }
                });
        //发送数据事件时，若2次发送事件的间隔＜指定时间，就会丢弃前一次的数据，
        // 直到指定时间内都没有新数据发射时才会发送后一次的数据
        LjyLogUtil.i("-----------------------------");
        mTextViewShow.append("-----------------------------\n");
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 1; i <= 20; i++) {
                            subscriber.onNext(i);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        subscriber.onCompleted();
                    }
                })
                .throttleWithTimeout(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("throttleWithTimeout_onCompleted");
                        mTextViewShow.append("throttleWithTimeout_onCompleted" + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer item) {
                        LjyLogUtil.i("throttleWithTimeout_获取到的元素是： " + item);
                        mTextViewShow.append("throttleWithTimeout_获取到的元素是： " + item + "\n");
                    }
                });
        LjyLogUtil.i("-----------------------------");
        mTextViewShow.append("-----------------------------\n");
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 1; i <= 20; i++) {
                            subscriber.onNext(i);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        subscriber.onCompleted();
                    }
                })
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("debounce_onCompleted");
                        mTextViewShow.append("debounce_onCompleted" + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer item) {
                        LjyLogUtil.i("debounce_获取到的元素是： " + item);
                        mTextViewShow.append("debounce_获取到的元素是： " + item + "\n");
                    }
                });
    }

    private void testTake() {
        //接收前n个
        Observable
                .just(1, 2, 3, 4, 5)
                .take(2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("对Complete事件作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("对Error事件作出响应:" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LjyLogUtil.i("take过滤后接收到了事件" + integer);
                    }
                });
        LjyLogUtil.i("-----------------------------");
        //接收后n个
        Observable
                .just(1, 2, 3, 4, 5)
                .takeLast(2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("对Complete事件作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("对Error事件作出响应:" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LjyLogUtil.i("takeLast过滤后接收到了事件" + integer);
                    }
                });

    }

    private void testDistinct() {
        //使用1：过滤事件序列中重复的事件
        LjyLogUtil.i("过滤事件序列中重复的事件:1,2,3,1,2,4,1,2,5");
        Observable
                .just(1, 2, 3, 1, 2, 4, 1, 2, 5)
                .distinct()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("1_获取到的事件元素是： " + integer);
                    }
                });
        //使用2：过滤事件序列中 连续重复的事件
        LjyLogUtil.i("过滤事件序列中 连续重复的事件:1,2,2,2,3,3,3,4,5");
        Observable
                .just(1, 2, 2, 2, 3, 3, 3, 4, 5)
                .distinctUntilChanged()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("2_获取到的事件元素是： " + integer);
                    }
                });
    }

    private void testSkip() {
        Integer[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //使用1：根据顺序跳过数据项
        LjyLogUtil.i("根据顺序跳过数据项:");
        Observable
                .from(arr)
                .skip(3)// 跳过正序的前n项
                .skipLast(2)// 跳过正序的后n项
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("1_获取到的整型事件元素是： " + integer);
                    }
                });
        //使用2：根据时间跳过数据项
        LjyLogUtil.i("根据顺序跳过数据项:");
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 1; i <= 10; i++) {
                            subscriber.onNext(i);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        subscriber.onCompleted();
                    }
                })
                .skip(3, TimeUnit.SECONDS)//跳过第ns发送的数据
                .skipLast(3, TimeUnit.SECONDS)//跳过最后ns发送的数据
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("2_onCompleted");
                        mTextViewShow.append("2_onCompleted" + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer item) {
                        LjyLogUtil.i("2_获取到的元素是： " + item);
                        mTextViewShow.append("2_获取到的元素是： " + item + "\n");
                    }
                });

    }

    private void testOfType() {
        Observable
                .just(1, "abc", 4, "hello world", 9)
                .ofType(String.class)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LjyLogUtil.i("获取到的字符串事件元素是: " + s);
                    }
                });

    }

    private void testFilter() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(1);
                        subscriber.onNext(2);
                        subscriber.onNext(3);
                        subscriber.onNext(4);
                        subscriber.onNext(5);
                    }
                })
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 2 && integer < 5;//过滤条件
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("对Complete事件作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("对Error事件作出响应:" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LjyLogUtil.i("接收到了事件" + integer);
                    }
                });
    }

    //可重试次数
    int maxConnectCount = 5;
    //当前已重试次数
    int currentRetryCount = 0;
    //重试等待时间
    int waitRetryTime = 0;

    private void testRetryWhen() {
        //1.创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        //2.创建网络请求接口的实例
        final GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        //3.observable封装
        //创建一个请求:中译英
        String word = "你好世界";
        LjyLogUtil.i("中译英:" + word);
        Observable<Translation1> observable1 = request.getEnglish(word);
        //4.发送网络请求 & 通过retryWhen进行重试
        mTextViewShow.append("网络翻译 ##" + word + "## , 出错重连 :\n");
        observable1
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                //输出异常信息
                                LjyLogUtil.i(throwable.getMessage());
                                mTextViewShow.append(throwable.getMessage() + "\n");
                                if (throwable instanceof IOException) {
                                    LjyLogUtil.i("属于IO异常,需要重试");
                                    mTextViewShow.append("属于IO异常,需要重试" + "\n");
                                    if (currentRetryCount < maxConnectCount) {
                                        currentRetryCount++;
                                        LjyLogUtil.i("重试次数:" + currentRetryCount);
                                        mTextViewShow.append("重试次数:" + currentRetryCount + "\n");
                                        /*
                                         * 需求2：实现重试
                                         * 通过返回的Observable发送的事件 = Next事件，从而使得retryWhen（）重订阅，最终实现重试功能
                                         *
                                         * 需求3：延迟1段时间再重试
                                         * 采用delay操作符 = 延迟一段时间发送，以实现重试间隔设置
                                         *
                                         * 需求4：遇到的异常越多，时间越长
                                         * 在delay操作符的等待时间内设置 = 每重试1次，增多延迟重试时间1s
                                         */
                                        // 设置等待时间
                                        waitRetryTime = 1000 + currentRetryCount * 1000;
                                        LjyLogUtil.i("等待时间=" + waitRetryTime);
                                        mTextViewShow.append("等待时间=" + waitRetryTime + "\n");
                                        return Observable.just(1).delay(waitRetryTime, TimeUnit.MILLISECONDS);
                                    } else {
                                        return Observable.error(new Throwable("重试次数已超过最大次数 = "
                                                + currentRetryCount + "，即 不再重试"));
                                    }
                                } else {
                                    return Observable.error(new Throwable("发生了非网络异常（非I/O异常）"));
                                }
                            }
                        });
                    }
                })
                .subscribe(new Observer<Translation1>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("onCompleted");
                        mTextViewShow.append("onCompleted" + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("onError:" + e.getLocalizedMessage());
                        mTextViewShow.append("onError:" + e.getLocalizedMessage() + "\n");
                    }

                    @Override
                    public void onNext(Translation1 translation1) {
                        LjyLogUtil.i("onNext:" + translation1.content.out);
                        mTextViewShow.append("onNext:翻译结果" + translation1.content.out + "\n");

                    }
                });

    }


    private int temp;

    private void testRepeat() {
        temp = 0;
        //1.创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        //2.创建网络请求接口的实例
        final GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        //3.observable封装
        //创建一个请求:中译英
        String word = "你好世界";
        LjyLogUtil.i("中译英:" + word);
        Observable<Translation1> observable1 = request.getEnglish(word);
        //4.发送网络请求 & 通过repeatWhen()进行轮询
        mTextViewShow.append("轮询翻译 ##" + word + "## :\n");
        observable1
//                .repeat(5)
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.flatMap(new Func1<Void, Observable<?>>() {
                            @Override
                            public Observable<?> call(Void aVoid) {
                                // 加入判断条件：当轮询次数 = 5次后，就停止轮询
                                if (++temp > 5) {
                                    return Observable.error(new Throwable("轮询结束"));
                                }
                                return Observable.just(1).delay(2, TimeUnit.SECONDS);
//                                return Observable.timer(3,TimeUnit.SECONDS);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translation1>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("onCompleted");
                        mTextViewShow.append("onCompleted" + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("onError:" + e.getLocalizedMessage());
                        mTextViewShow.append("onError:" + e.getLocalizedMessage() + "\n");
                    }

                    @Override
                    public void onNext(Translation1 translation1) {
                        LjyLogUtil.i("onNext:" + translation1.content.out);
                        mTextViewShow.append("onNext:翻译结果" + translation1.content.out + "\n");

                    }
                });

    }

    private void testDo() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(1);
                        subscriber.onNext(2);
                        subscriber.onNext(3);
                        subscriber.onError(new Throwable("发生了错误哦~~"));
                    }
                })
                .doOnEach(new Action1<Notification<? super Integer>>() {
                    //1.当Observable每发送1次数据事件就会调用1次
                    @Override
                    public void call(Notification<? super Integer> notification) {
                        LjyLogUtil.i("doOnEach:" + notification.getValue());
                    }
                })
                .doOnNext(new Action1<Integer>() {
                    //2.执行Next事件前调用
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("doOnNext:" + integer);
                    }
                })
                .doOnCompleted(new Action0() {
                    //3.Observable正常发送事件完毕后调用
                    @Override
                    public void call() {
                        LjyLogUtil.i("doOnCompleted");
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    //4.Observable发送错误事件时调用
                    @Override
                    public void call(Throwable throwable) {
                        LjyLogUtil.i("doOnError:" + throwable.getLocalizedMessage());
                    }
                })
                .doOnSubscribe(new Action0() {
                    //5.观察者订阅时调用
                    @Override
                    public void call() {
                        LjyLogUtil.i("doOnSubscribe");
                    }
                })
                .doAfterTerminate(new Action0() {
                    //6.Observable发送事件完毕后调用，无论正常发送完毕/异常终止
                    @Override
                    public void call() {
                        LjyLogUtil.i("doAfterTerminate");
                    }
                })
                .finallyDo(new Action0() {
                    //最后执行
                    @Override
                    public void call() {
                        LjyLogUtil.i("finallyDo");
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("对Complete事件作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("对Error事件作出响应:" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LjyLogUtil.i("接收到了事件" + integer);
                    }
                });


    }

    private void testLianHe() {
        form.setVisibility(View.VISIBLE);
        Observable<CharSequence> nameObservable = RxTextView.textChanges(nameTextView).skip(1);
        Observable<CharSequence> ageObservable = RxTextView.textChanges(ageTextView).skip(1);
        Observable<CharSequence> jobObservable = RxTextView.textChanges(jobTextView).skip(1);
        Observable
                .combineLatest(nameObservable, ageObservable, jobObservable, new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
                        //规定表单信息输入不能为空
                        //1.name
//                boolean isNameValid=!TextUtils.isEmpty(nameTextView.getText());
                        // 除了设置为空，也可设置长度限制
                        boolean isNameValid = !TextUtils.isEmpty(nameTextView.getText()) && (nameTextView.getText().toString().length() > 2 && nameTextView.getText().toString().length() < 9);
                        //2.age
                        boolean isAgeValid = !TextUtils.isEmpty(ageTextView.getText());
                        //3.job
                        boolean isJobValid = !TextUtils.isEmpty(jobTextView.getText());
                        //返回信息 = 联合判断，即3个信息同时已填写，"提交按钮"才可点击
                        return isNameValid && isAgeValid && isJobValid;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        LjyLogUtil.i("提交按钮是否可点击: " + aBoolean);
                        btnSubmit.setEnabled(aBoolean);
                    }
                });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = String.format("提交了form: name=%s,age=%s,job=%s", nameTextView.getText(), ageTextView.getText(), jobTextView.getText());
                LjyLogUtil.i(info);
                LjyToastUtil.toast(mContext, info);
            }
        });
    }

    private void testCache() {
        // 该2变量用于模拟内存缓存 & 磁盘缓存中的数据
        final String memoryCache = getMemoryCache();
        final String diskCache = getDiskCache();
        //设置第1个Observable：检查内存缓存是否有该数据的缓存
        Observable<String> memoryObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //先判断内存中有无数据
                if (memoryCache != null) {
                    subscriber.onNext(memoryCache);
                } else {
                    // 若无该数据，则直接发送结束事件
                    subscriber.onCompleted();
                }
            }
        });
        //设置第2个Observable：检查磁盘缓存是否有该数据的缓存
        Observable<String> diskObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (diskCache != null) {
                    subscriber.onNext(diskCache);
                } else {
                    subscriber.onCompleted();
                }
            }
        });
        //设置第3个Observable：通过网络获取数据
        Observable<String> networkObservable = Observable.just("从网络中获取的数据");
        //通过concat() 和 firstElement()操作符实现缓存功能
        Observable.concat(memoryObservable, diskObservable, networkObservable)
                .first()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LjyLogUtil.i("最终获取的数据来源 =  " + s);
                    }
                });

    }

    private String getDiskCache() {
        return "从磁盘缓存中获取的数据";
//        return null;
    }

    private String getMemoryCache() {
//        return "从内存缓存中获取的数据";
        return null;
    }

    private void testCount() {
        Observable.just(1, 2, 3, 4)
                .count()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("发送的事件数量 =  " + integer);
                    }
                });
    }

    private void testStartWith() {
        Observable.just(4, 5, 6)
                .startWith(0)
                .startWith(1, 2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        LjyLogUtil.i("得到的数据是： " + integer);
                    }
                });
    }

    private void testCollect() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .collect(new Func0<ArrayList<String>>() {
                    @Override
                    public ArrayList<String> call() {
                        return new ArrayList<>();
                    }
                }, new Action2<ArrayList<String>, Integer>() {
                    @Override
                    public void call(ArrayList<String> list, Integer integer) {
                        list.add("数据00" + integer);
                    }
                })
                .subscribe(new Action1<ArrayList<String>>() {
                    @Override
                    public void call(ArrayList<String> list) {
                        LjyLogUtil.i("得到的数据是： " + list.toString());
                    }
                });
//                .collect(new Func0<ArrayList<Integer>>() {
//                    @Override
//                    public ArrayList<Integer> call() {
//                        return new ArrayList<>();
//                    }
//                }, new Action2<ArrayList<Integer>, Integer>() {
//                    @Override
//                    public void call(ArrayList<Integer> list, Integer integer) {
//                        list.add(integer);
//                    }
//                })
//                .subscribe(new Action1<ArrayList<Integer>>() {
//                    @Override
//                    public void call(ArrayList<Integer> list) {
//                        LjyLogUtil.i("得到的数据是： " + list.toString());
//                    }
//                });
    }

    private void testReduce() {
        LjyLogUtil.i("1,2,3,4:");
        Observable.just(1, 2, 3, 4)
                .reduce(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer * integer2;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LjyLogUtil.i("结果:" + integer);
                    }
                });
    }

    private void testZip() {
        //创建第一个被观察者
        Observable<Integer> observable1 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Thread.sleep(1000);
                    subscriber.onNext(1);
                    Thread.sleep(1000);
                    subscriber.onNext(2);
                    Thread.sleep(1000);
                    subscriber.onNext(3);
                    Thread.sleep(1000);
                    subscriber.onNext(4);
                } catch (InterruptedException e) {
                }
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io());//设置被观察者1在工作线程1中工作
        //创建第二个被观察者
        Observable<String> observable2 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(500);
                    subscriber.onNext("A");
                    Thread.sleep(500);
                    subscriber.onNext("B");
                    Thread.sleep(500);
                    subscriber.onNext("C");
                    Thread.sleep(500);
                    subscriber.onNext("D");
                } catch (InterruptedException e) {
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread());//设置被观察者2在工作线程2中工作
        //假设不作线程控制，则该两个被观察者会在同一个线程中工作，即发送事件存在先后顺序，而不是同时发送
        Observable
//                .combineLatestDelayError(observable1, observable2, new Func2<Integer, String, String>() {
//                .combineLatest(observable1, observable2, new Func2<Integer, String, String>() {
                .zip(observable1, observable2, new Func2<Integer, String, String>() {
                    @Override
                    public String call(Integer integer, String s) {
                        return integer + "__" + s;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("onCompleted");
                        mTextViewShow.append("onCompleted\n");

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        LjyLogUtil.i("zip onNext: " + s);
                        mTextViewShow.append("zip onNext: " + s + "\n");
                    }
                });
    }

    private void testMerge() {
        LjyLogUtil.i("testMerge--->");
        Observable
//                .merge(Observable.interval(0, 3, TimeUnit.SECONDS),
//                        Observable.interval(2, 3, TimeUnit.SECONDS))
                .merge(Observable.just(1, 2, 3),
                        Observable.just(4, 5, 6),
                        Observable.just(7, 8, 9).delay(2000, TimeUnit.MILLISECONDS),
                        Observable.just(10, 11, 12),
                        Observable.just(13, 14, 15))
//                .mergeDelayError(Observable.create(new Observable.OnSubscribe<Integer>() {
//                            @Override
//                            public void call(Subscriber<? super Integer> subscriber) {
//                                subscriber.onNext(1);
//                                subscriber.onNext(2);
//                                subscriber.onNext(3);
//                                subscriber.onError(new NullPointerException());
//                                subscriber.onCompleted();
//                            }
//
//                        }),
//                        Observable.just(4, 5, 6),
//                        Observable.just(7, 8, 9),
//                        Observable.just(10, 11, 12),
//                        Observable.just(13, 14, 15))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("对Complete事件作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("对Error事件作出响应");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LjyLogUtil.i("merge接收到了事件:" + integer);
                        mTextViewShow.append("merge接收到了事件:" + integer + "\n");
                    }
                });
    }

    private void testConcat() {
        LjyLogUtil.i("testConcat--->");
        Observable
                .concat(Observable.just(1, 2, 3),
                        Observable.just(4, 5, 6),
                        Observable.just(7, 8, 9).delay(2000, TimeUnit.MILLISECONDS),
                        Observable.just(10, 11, 12),
                        Observable.just(13, 14, 15))
//                .concatDelayError(Observable.create(new Observable.OnSubscribe<Integer>() {
//                            @Override
//                            public void call(Subscriber<? super Integer> subscriber) {
//                                subscriber.onNext(1);
//                                subscriber.onNext(2);
//                                subscriber.onNext(3);
//                                subscriber.onError(new NullPointerException());
//                                subscriber.onCompleted();
//                            }
//
//                        }),
//                        Observable.just(4, 5, 6),
//                        Observable.just(7, 8, 9),
//                        Observable.just(10, 11, 12),
//                        Observable.just(13, 14, 15))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("对Complete事件作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("对Error事件作出响应");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LjyLogUtil.i("concat接收到了事件:" + integer);
                        mTextViewShow.append("merge接收到了事件:" + integer + "\n");
                    }
                });

    }

    private void testRegisterLogin() {
        //1.创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        //2.创建网络请求接口的实例
        final GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        //3.observable封装
        //创建第一个请求:中译英|
        LjyLogUtil.i("中译英:还要");
        Observable<Translation1> observable1 = request.getEnglish("还要");
        observable1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Translation1>() {
                    @Override
                    public void call(Translation1 translation1) {
                        LjyLogUtil.i("第1次网络请求成功");
                        // 对第1次网络请求返回的结果进行操作 = 显示翻译结果
                        LjyLogUtil.i(translation1.toString());
                        translation1.show();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Translation1, Observable<Translation2>>() {
                    @Override
                    public Observable<Translation2> call(Translation1 translation1) {
                        //将网络请求1转换成网络请求2，即发送网络请求2
                        //创建第一个请求:英译中
                        LjyLogUtil.i("将第一个翻译的结果反译:");
                        return request.getChinese(translation1.content.out);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Translation2>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("登录失败:" + e.getLocalizedMessage());
                        mTextViewShow.append(LjyLogUtil.getAllLogMsg());
                        LjyLogUtil.setAppendLogMsg(false);
                    }

                    @Override
                    public void onNext(Translation2 translation2) {
                        LjyLogUtil.i("第2次网络请求成功");
                        // 对第2次网络请求返回的结果进行操作 = 显示翻译结果
                        LjyLogUtil.i(translation2.toString());
                        translation2.show();
                        mTextViewShow.append(LjyLogUtil.getAllLogMsg());
                        LjyLogUtil.setAppendLogMsg(false);
                    }
                });
//                .subscribe(new Action1<Translation2>() {
//                    @Override
//                    public void call(Translation2 translation2) {
//
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//
//                    }
//                });
    }

    //为了演示是2个网络请求，所以对应设置2个接收服务器的数据类
//  解析中译英的结果
    public class Translation1 {
        private int status;
        private content content;

        private class content {
            private String from;
            private String to;
            private String vendor;
            private String out;
            private int errNo;

            @Override
            public String toString() {
                return "content{" +
                        "from='" + from + '\'' +
                        ", to='" + to + '\'' +
                        ", vendor='" + vendor + '\'' +
                        ", out='" + out + '\'' +
                        ", errNo=" + errNo +
                        '}';
            }
        }

        public void show() {
            LjyLogUtil.i("翻译结果 = " + content.out);
        }

        @Override
        public String toString() {
            return "Translation1{" +
                    "status=" + status +
                    ", content=" + content +
                    '}';
        }
    }

    //解析英译中的结果
    public class Translation2 {
        private int status;
        private content content;

        private class content {
            private String ph_en;
            private String ph_am;
            private String ph_en_mp3;
            private String ph_am_mp3;
            private String ph_tts_mp3;
            private String[] word_mean;

            @Override
            public String toString() {
                return "content{" +
                        "ph_en='" + ph_en + '\'' +
                        ", ph_am='" + ph_am + '\'' +
                        ", ph_en_mp3='" + ph_en_mp3 + '\'' +
                        ", ph_am_mp3='" + ph_am_mp3 + '\'' +
                        ", ph_tts_mp3='" + ph_tts_mp3 + '\'' +
                        ", word_mean=" + Arrays.toString(word_mean) +
                        '}';
            }
        }

        //定义 输出返回数据 的方法
        public void show() {
            LjyLogUtil.i("翻译结果 = ");
            for (String item : content.word_mean) {
                LjyLogUtil.i("--" + item);
            }
        }

        @Override
        public String toString() {
            return "Translation2{" +
                    "status=" + status +
                    ", content=" + content +
                    '}';
        }
    }

    public interface GetRequest_Interface {
        // 网络请求1
        @GET("ajax.php?a=fy&f=auto&t=auto")
        Observable<Translation1> getEnglish(@Query("w") String word);

        // 网络请求2
        @GET("ajax.php?a=fy&f=auto&t=auto")
        Observable<Translation2> getChinese(@Query("w") String word);
    }

    private void testRegisterLoginNormal() {
//        // 发送注册网络请求的函数方法
//        private void register() {
//            api.register(new RegisterRequest())
//                    .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
//                    .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
//                    .subscribe(new Consumer<RegisterResponse>() {
//                        @Override
//                        public void accept(RegisterResponse registerResponse) throws Exception {
//                            Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                            login();   //注册成功, 调用登录的方法
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            Toast.makeText(MainActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//
//
//// 发送登录网络请求的函数方法
//        private void login() {
//            api.login(new LoginRequest())
//                    .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
//                    .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
//                    .subscribe(new Consumer<LoginResponse>() {
//                        @Override
//                        public void accept(LoginResponse loginResponse) throws Exception {
//                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
    }

    private void testBuffer() {
        Observable.just(1, 2, 3, 4, 5)
//                .buffer(3, 1)//设置缓存区大小 & 步长
                .buffer(2, 2)
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("对Complete事件作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LjyLogUtil.i("对Error事件作出响应");
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        LjyLogUtil.i(" 缓存区里的事件数量 = " + integers.size());
                        for (Integer value : integers) {
                            LjyLogUtil.i(" 事件 = " + value);
                        }
                    }
                });
    }

    private void testConcatMap() {
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
            }
        });
        observable
                .concatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        final List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add("concatMap:我是事件" + integer + "拆分后的子事件_" + i);
                        }
                        return Observable.from(list).delay(800, TimeUnit.MILLISECONDS);
                    }
                })
                //subscribeOn()改变调用它之前代码的线程,observeOn()改变调用它之后代码的线程
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LjyLogUtil.i(s);
                        mTextViewShow.append(s + "\n");
                    }
                });
    }

    private void testFlatMap() {
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
            }
        });
        observable
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        final List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add("flatMap:我是事件" + integer + "拆分后的子事件_" + i);
                        }
                        return Observable.from(list).delay(800, TimeUnit.MILLISECONDS);
                    }
                })
                //subscribeOn()改变调用它之前代码的线程,observeOn()改变调用它之后代码的线程
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LjyLogUtil.i(s);
                        mTextViewShow.append(s + "\n");
                    }
                });
    }

    private void testMap() {
        Observable.create(new Observable.OnSubscribe<Integer>() {

            // 1. 被观察者发送事件 = 参数为整型 = 1、2、3
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
            }
        }).map(new Func1<Integer, String>() {
            //使用Map变换操作符中的Function函数对被观察者发送的事件进行统一变换：整型变换成字符串类型
            @Override
            public String call(Integer integer) {
                return "map:将事件" + integer + "的参数从 整型" + integer + " 变换成 字符串类型" + integer;
            }
        }).subscribe(new Action1<String>() {
            //3. 观察者接收事件时，是接收到变换后的事件 = 字符串类型
            @Override
            public void call(String s) {
                LjyLogUtil.i(s);
                mTextViewShow.append(s + "\n");
            }
        });

    }

    private void testSimple() {
        //创建被观察者:
        //方式1:完整创建
        Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                // 注：建议发送事件前检查观察者的isUnsubscribed状态，以便在没有观察者时，让Observable停止发射数据
                if (!subscriber.isUnsubscribed()) {
                    //定义需要发送的事件,通知观察者
                    subscriber.onNext("observable1_1");
                    subscriber.onNext("observable1_2");
                    subscriber.onNext("observable1_3");
                }
                subscriber.onCompleted();
            }
        });
        //方式2:快速创建, 直接将传入的参数依次发送出来
        Observable observable2 = Observable.just("observable2_1", "observable2_2", "observable2_3");
        //方式3:将传入的数组/Iterable 拆分成具体对象后，依次发送出来
        String[] nums = {"observable3_1", "observable3_2", "observable3_3"};
        Observable observable3 = Observable.from(nums);
        //方式4:延迟指定事件,延迟指定时间后，发送1个数值0（Long类型）
        Observable.timer(3, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
                LjyLogUtil.i("观察者Observer Observable.timer onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                LjyLogUtil.i("观察者Observer Observable.timer onNext:" + aLong);
            }
        });
        //方式5:每隔指定时间 就发送事件
        // 参数说明：
        // 参数1 = 第1次延迟时间；
        // 参数2 = 间隔时间数字；
        // 参数3 = 时间单位；
        Observable.interval(3, 1, TimeUnit.SECONDS)
                .doOnNext(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        LjyLogUtil.i("doOnNext: 第 " + aLong + " 次轮询");
                        //此中可做网络请求等操作

                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        LjyLogUtil.i("观察者Observer Observable.interval onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        LjyLogUtil.i("观察者Observer Observable.interval onNext:" + aLong);
                    }
                });
        //方式6:连续发送 1个事件序列，可指定范围
        // 参数说明：
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        // 注：若设置为负数，则会抛出异常
        Observable.range(3, 10).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                LjyLogUtil.i("观察者Observer Observable.range onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer value) {
                LjyLogUtil.i("观察者Observer Observable.range onNext:" + value);
            }
        });

        //创建观察者:
        //方式1: Observer接口
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                LjyLogUtil.i("观察者Observer onNext:" + s);
            }

            @Override
            public void onCompleted() {
                LjyLogUtil.i("观察者Observer onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LjyLogUtil.i("观察者Observer onError");
            }
        };
        //方式2: Subscriber抽象类
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LjyLogUtil.i("观察者Subscriber onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LjyLogUtil.i("观察者Subscriber onError");
            }

            @Override
            public void onNext(String s) {
                LjyLogUtil.i("观察者Subscriber onNext:" + s);
            }

        };

        //订阅:
        observable1.subscribe(observer);
        observable2.subscribe(observer);
        observable3.subscribe(observer);
        observable1.subscribe(subscriber);
        observable2.subscribe(subscriber);
        observable3.subscribe(subscriber);
    }


}
