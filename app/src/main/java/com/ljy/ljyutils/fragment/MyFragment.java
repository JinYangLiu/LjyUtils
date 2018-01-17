package com.ljy.ljyutils.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.util.LjyLogUtil;

/**
 * Created by ljy on 2018/1/17.
 */

public class MyFragment extends Fragment {
    private TextView textView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LjyLogUtil.i("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LjyLogUtil.i("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LjyLogUtil.i("onCreateView");
        View view=inflater.inflate(R.layout.layout_fragment_my,container,false);
        textView=view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LjyLogUtil.i("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LjyLogUtil.i("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LjyLogUtil.i("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LjyLogUtil.i("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LjyLogUtil.i("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LjyLogUtil.i("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LjyLogUtil.i("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LjyLogUtil.i("onDetach");
    }

    public void setTextInfo(String textInfo){
        textView.setText(textInfo);
    }

    public void setTextBackgroundColor(int textBackgroundColor){
        textView.setBackgroundColor(textBackgroundColor);
    }
}
