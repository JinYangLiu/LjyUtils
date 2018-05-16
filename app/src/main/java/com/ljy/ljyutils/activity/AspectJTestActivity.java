package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.test.DebugTool;
import com.ljy.util.LjyLogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AspectJTestActivity extends BaseActivity {
    @BindView(R.id.textViewShow)
    TextView mTextViewShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspect_jtest);
        ButterKnife.bind(mActivity);
    }

    public void onBtnClick(View view) {
        mTextViewShow.append("----------" + ((Button) view).getText() + "----------\n");
        LjyLogUtil.setAppendLogMsg(true);
        switch (view.getId()) {
            case R.id.btnTestBefore:
                methodTest001(view);
                break;
            case R.id.btnTestAfter:
                methodTest002(view);
                break;
            case R.id.btnTestAroundExecution:
                methodTest003(view);
                break;
            case R.id.btnTestDebugTool:
               String result= methodTest004(view);
               LjyLogUtil.i("result:"+result);
                break;
            case R.id.btnTestAroundCall:
                methodTest005();
                break;
            case R.id.btnTestWithinCode0:
                testAOP1();
                break;
            case R.id.btnTestWithinCode1:
                testAOP2();
                break;
            case R.id.btnTestAfterThrowing:
                testAfterThrowing();
                break;
            case R.id.btnToast:
//                Toast.makeText(mContext, "btnToast click", Toast.LENGTH_SHORT).show();
                //本来是想通过AspectJ将Toast替换为自己的windowToast(TYPE_TOAST),详见AspectTest.toastShow
                //但是Android 7.1.1出现了异常token null is not valid,所以也是不行了
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (Settings.canDrawOverlays(this)) {
//                        LjyLogUtil.i("hasPermission");
//                        LjyToastUtil.windowToast(AspectJTestActivity.this, "windowToast", 2000);
//                    } else {
//                        LjyLogUtil.i("requestPermission");
//                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                                Uri.parse("package:" + getPackageName()));
//                        startActivityForResult(intent,10);
//                    }
//                }else{
//                    LjyToastUtil.windowToast(AspectJTestActivity.this, "windowToast", 2000);
//                }
                /**
                 * 替换Toast的可行方案
                 为了继续能让用户在禁掉通知权限的情况下，也能看到通知以及屏蔽上述Toast带来的Crash，我们经过调研、分析并尝试了以下几种方案。
                 1.在7.1.1以上系统中继续使用WindowManager方式，只不过需要把type改为TYPE_PHONE等悬浮窗权限。
                 但是TYPE_PHONE是需要申请运行时权限的
                 2.使用Dialog、DialogFragment、PopupWindow等弹窗控件来实现一个通知。
                 3.按照Snackbar的实现方式，找到一个可以添加布局的父布局，采用addView的方式添加通知。
                 */
                //使用SnackBar代替
//                LjyToastUtil.showSnackBar(mTextViewShow,"btnToast click");
                break;
            default:
                break;
        }
        mTextViewShow.append(LjyLogUtil.getAllLogMsg());
        LjyLogUtil.setAppendLogMsg(false);
    }

    private void testAfterThrowing() {
        LjyLogUtil.i("testAfterThrowing:start");
        View view = null;
//        try {
        view.animate();
//        }catch (Exception e){
//            LjyLogUtil.i("Exception:view == null");
//        }
        LjyLogUtil.i("testAfterThrowing:end");
    }

    private void testAOP1() {
        LjyLogUtil.i("testAOP1:start");
        methodTest006();
        LjyLogUtil.i("testAOP1:end");
    }

    private void testAOP2() {
        LjyLogUtil.i("testAOP2:start");
        methodTest006();
        LjyLogUtil.i("testAOP2:end");
    }

    /**
     * testAOP1() 和 testAOP2() 都调用了 methodTest006()方法，
     * 但是，现在想在testAOP2()方法调用 methodTest006()方法的时候，才切入代码，
     * 那么这个时候，就需要使用到Pointcut和withincode组合的方式，来精确定位切入点
     */
    private void methodTest006() {
        LjyLogUtil.i("methodTest006 to do...");
    }

    private void methodTest005() {
        LjyLogUtil.i("methodTest005 to do...");
    }

    @DebugTool
    private String methodTest004(View view) {
        LjyLogUtil.i(Thread.currentThread().getName()+"_methodTest004 to do...");
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LjyLogUtil.i(Thread.currentThread().getName()+"_methodTest004 to do end");
        return "耗时操作的结果";
    }

    private void methodTest003(View view) {
        LjyLogUtil.i("methodTest003 to do...");
    }

    private void methodTest002(View view) {
        LjyLogUtil.i("methodTest002 to do...");
    }

    private void methodTest001(View view) {
        LjyLogUtil.i("methodTest001 to do...");
    }
}
