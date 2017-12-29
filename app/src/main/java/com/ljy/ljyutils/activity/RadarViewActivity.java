package com.ljy.ljyutils.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.view.LjyRadarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RadarViewActivity extends AppCompatActivity {
    String[] values = new String[]{"KDA","输出","经济","生存","带线","打野"};
    String[] titles = new String[]{"12.4","54092","12670","4.5","6","12"};
    double[] percents = new double[]{0.9, 0.7,0.7,0.5,0.9,0.45};

    @BindView(R.id.jump)
    TextView jump;

    @BindView(R.id.radarView)
    LjyRadarView mRadarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_view);

        ButterKnife.bind(this);

        mRadarView.setTitles(titles);
        mRadarView.setValues(values);
        mRadarView.setPercents(percents);
        mRadarView.setColors(null);
        mRadarView.setValueColor(0xff888888);
        mRadarView.setTitleColor(0xff000000);

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RadarViewActivity.this, Radar2Activity.class);
                RadarViewActivity.this.startActivity(intent);
            }
        });
    }
}
