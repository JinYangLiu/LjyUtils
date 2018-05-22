package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LayoutBetterActivity extends BaseActivity {


    @BindView(R.id.include_1)
    View include1;
    @BindView(R.id.include_2)
    LinearLayout include2;

    LinearLayout viewImport;

    @BindView(R.id.viewStub)
    ViewStub viewStub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_better);
        ButterKnife.bind(mActivity);
        ImageView imageView1 = include1.findViewById(R.id.imageView);
        imageView1.setImageResource(R.drawable.girl);
        ImageView imageView2 = include2.findViewById(R.id.imageView);
        imageView2.setImageResource(R.drawable.cat);
    }

    public void btnClick(View view) {
        if (view.getId() == R.id.button_viewStub) {
            //只能inflate一次，否则报错 IllegalStateException: ViewStub must have a non-null ViewGroup viewParent
            if (viewImport == null) {
                viewImport = (LinearLayout) viewStub.inflate();
            }else {
                viewImport.setVisibility(viewImport.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
            }

        }
    }
}
