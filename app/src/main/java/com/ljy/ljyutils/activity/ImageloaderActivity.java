package com.ljy.ljyutils.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyDensityUtil;
import com.ljy.util.LjyImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageloaderActivity extends BaseActivity {

    @BindView(R.id.gridView)
    GridView gridView;
    private ArrayList<String> urlList;
    private static boolean mIsGridViewIdle=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader);
        ButterKnife.bind(mActivity);
        initData();
        initView();
    }

    private void initData() {
        urlList=new ArrayList<>();
        urlList.add("https://avatars1.githubusercontent.com/u/19702574?s=460&v=4");
        urlList.add("https://avatars1.githubusercontent.com/u/25233216?s=200&v=4");
        urlList.add("https://avatars1.githubusercontent.com/u/35551?s=460&v=4");
        urlList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=426595056,3152484396&fm=27&gp=0.jpg");
        urlList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=428549871,4004515111&fm=27&gp=0.jpg");
        urlList.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4063893668,3154339931&fm=27&gp=0.jpg");
        urlList.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3305259696,4000115539&fm=27&gp=0.jpg");
        urlList.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1731289026,1820369445&fm=27&gp=0.jpg");
        urlList.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3407784717,2395060724&fm=27&gp=0.jpg");
        urlList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1689987710,1765706894&fm=27&gp=0.jpg");
        urlList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2680524837,3337493238&fm=27&gp=0.jpg");
        urlList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3562441405,3992976602&fm=27&gp=0.jpg");
        urlList.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2121028362,1128129619&fm=27&gp=0.jpg");
        urlList.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=247904163,605748106&fm=27&gp=0.jpg");
        urlList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1923406312,3955780655&fm=27&gp=0.jpg");
        urlList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2471258591,1536588225&fm=27&gp=0.jpg");
        urlList.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=754123634,3570338229&fm=27&gp=0.jpg");
        urlList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=568548490,3880388784&fm=27&gp=0.jpg");
        urlList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3612661600,2074915&fm=27&gp=0.jpg");
        urlList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1436545976,2535581228&fm=27&gp=0.jpg");
        urlList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1946374524,1572505334&fm=27&gp=0.jpg");
    }

    private void initView() {
        final ImageAdapter adapter = new ImageAdapter(mContext);
        gridView.setAdapter(adapter);
        adapter.addData(urlList);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    mIsGridViewIdle=true;
                    adapter.notifyDataSetChanged();
                }else {
                    mIsGridViewIdle=false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private static class ImageAdapter extends BaseAdapter{

        private final LayoutInflater layoutInflater;
        private final LjyImageLoader imageLoader;
        private List<String> mUrlList=new ArrayList<>();


        public ImageAdapter(Context context) {
            layoutInflater=LayoutInflater.from(context);
            imageLoader= LjyImageLoader.build(context);
        }

        public void addData(List<String> newList){
            mUrlList.addAll(newList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mUrlList.size();
        }

        @Override
        public String getItem(int position) {
            return mUrlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView= layoutInflater.inflate(R.layout.image_grid_item,parent,false);
                holder=new ViewHolder();
                holder.imageView=convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            ImageView imageView=holder.imageView;
            final String tag= (String) imageView.getTag();
            final String url=getItem(position);
            if (!url.equals(tag)){
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
            if (mIsGridViewIdle) {
                imageView.setTag(url);
                imageLoader.bindBitmap(url, imageView, 720, 1080);
            }

            return convertView;
        }

        class ViewHolder{
            ImageView imageView;
        }
    }
}
