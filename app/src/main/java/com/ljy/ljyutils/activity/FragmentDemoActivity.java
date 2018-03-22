package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.fragment.MyFragment;
import com.ljy.ljyutils.fragment.MyFragment2;
import com.ljy.ljyutils.fragment.MyFragment3;
import com.ljy.util.LjyLogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDemoActivity extends BaseActivity {


    //用于动态添加fragment的frameLayout
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<Fragment> fragmentList2 = new ArrayList<>();
    private List<String> fragmentTitleList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LjyLogUtil.i("onCreate");
        setContentView(R.layout.activity_fragment_demo);
        ButterKnife.bind(mActivity);
        initFragment();
        initViewPager();
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(2);//左边可以预加载并缓存2个页面，右边也可以预加载并缓存2个页面
        FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList2.get(i);
            }

            @Override
            public int getCount() {
                return fragmentList2.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentTitleList.get(position);
            }

            //java.lang.NullPointerException: Attempt to invoke virtual method 'android.os.Handler android.support.v4.app.FragmentHostCallback.getHandler()' on a null object reference
            @Override
            public void finishUpdate(ViewGroup container) {
                try{
                    super.finishUpdate(container);
                } catch (NullPointerException nullPointerException){
                    LjyLogUtil.e("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
                }
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);//已经废弃的方法，是因为没有必要设置，setupWithViewPager已经足够
    }

    private void initFragment() {
        fragmentList.add(new MyFragment());
        fragmentList.add(new MyFragment2());
        fragmentList.add(new MyFragment3());

        fragmentList2.add(new MyFragment());
        fragmentList2.add(new MyFragment2());
        fragmentList2.add(new MyFragment3());

        fragmentTitleList.add("板块_001");
        fragmentTitleList.add("板块_002");
        fragmentTitleList.add("板块_003");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LjyLogUtil.i("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LjyLogUtil.i("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LjyLogUtil.i("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LjyLogUtil.i("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LjyLogUtil.i("onDestroy");
    }

    public void onFragmentBtnClick(View view) {

        int currentCount = 0;
        switch (view.getId()) {
            case R.id.btn_111:
                currentCount = 0;
                break;
            case R.id.btn_222:
                currentCount = 1;
                break;
            case R.id.btn_333:
                currentCount = 2;
                break;
            default:
                break;
        }
        //通过FragmentManager+Fragment实现
        switchFragment( currentCount);
        //通过ViewPager+FragmentPagerAdapter+Fragment实现
        viewPager.setCurrentItem(currentCount);


    }

    private void switchFragment( int currentCount) {
        //创建FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启fragment事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //1.使用replace，就是将一个创建并添加，，并且将之前的移除并销毁
//        fragmentTransaction.replace(R.id.frameLayout, fragmentList.get(currentCount));

        //2.通过hide，show，若没有添加就添加，若添加了就直接切换显示/隐藏
        if (fragmentList.size() >= currentCount) {
            //添加/显示
            if (fragmentList.get(currentCount).isAdded())
                fragmentTransaction.show(fragmentList.get(currentCount));
            else
                fragmentTransaction.add(R.id.frameLayout, fragmentList.get(currentCount));
            //隐藏
            for (int i = 0; i < fragmentList.size(); i++) {
                if (i != currentCount && fragmentList.get(i).isAdded() && !fragmentList.get(i).isHidden()) {
                    fragmentTransaction.hide(fragmentList.get(i));
                }
            }
        }

        //最后一定要记得commit
        fragmentTransaction.commit();
    }

    /** 打开到销毁这个activity所执行生命周期方法的顺序如下：
     *
     * (FragmentDemoActivity.java:14)
     * ---->onCreate
     * (MyFragment.java:22)
     * ---->onAttach
     * (MyFragment.java:28)
     * ---->onCreate
     * (MyFragment.java:33)
     * ---->onCreateView
     * (MyFragment.java:41)
     * ---->onActivityCreated
     * (FragmentDemoActivity.java:21)
     * ---->onStart
     * (MyFragment.java:47)
     * ---->onStart
     * (FragmentDemoActivity.java:27)
     * ---->onResume
     * (MyFragment.java:53)
     * ---->onResume
     * (MyFragment.java:59)
     * ---->onPause
     * (FragmentDemoActivity.java:33)
     * ---->onPause
     * (MyFragment.java:65)
     * ---->onStop
     * (FragmentDemoActivity.java:39)
     * ---->onStop
     * (MyFragment.java:71)
     * ---->onDestroyView
     * (MyFragment.java:77)
     * ---->onDestroy
     * (MyFragment.java:83)
     * ---->onDetach
     * (FragmentDemoActivity.java:45)
     * ---->onDestroy
     */
}