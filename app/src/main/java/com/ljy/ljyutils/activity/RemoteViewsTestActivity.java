package com.ljy.ljyutils.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemoteViewsTestActivity extends BaseActivity {
    @BindView(R.id.remoteViewContent)
    LinearLayout remoteViewContent;

    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RemoteViews remoteViews=intent.getParcelableExtra(RemoteViewsActivity.EXTRA_REMOTE_VIEWS);
            LjyLogUtil.i("BroadcastReceiver.onReceive");
            LjyLogUtil.i("remoteViews==null:"+(remoteViews==null));
            if (remoteViews!=null){
                updateUI(remoteViews);
            }
        }
    };

    private void updateUI(RemoteViews remoteViews) {
        //1.同一个应用的不同进程中
        View view=remoteViews.apply(mContext,remoteViewContent);
        remoteViewContent.addView(view);
        //2.如果是在不同的应用中,id可能是不同的
//        int layoutId=getResources().getIdentifier("layout_notification","layout",getPackageName());
//        View view1=getLayoutInflater().inflate(layoutId,remoteViewContent,false);
//        remoteViews.reapply(mContext,view1);
//        remoteViewContent.addView(view1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_views_test);

        ButterKnife.bind(mActivity);

        IntentFilter filter=new IntentFilter(RemoteViewsActivity.REMOTE_ACTION);
        registerReceiver(broadcastReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
