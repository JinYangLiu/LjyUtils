package com.ljy.ljyutils.bean;

import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.LJY on 2017/8/9.
 */

public class SwipeCardBean {
    private int position;
    private String name;
    private String url;
    private int cardColor;
    public static String imgUrl="http://img5.imgtn.bdimg.com/it/u=2629022734,2518818737&fm=26&gp=0.jpg";
    public static String[] urls = {"http://imgsrc.baidu.com/forum/w%3D580/sign=cd4ee2fafad3572c66e29cd4ba126352/359205ce36d3d539719fbad03287e950342ab0f8.jpg",
            "http://img4.imgtn.bdimg.com/it/u=1555471406,472717268&fm=26&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2547432359,3031103221&fm=26&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3124759563,1050396013&fm=26&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=167162227,3470253227&fm=26&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=605643313,2678653208&fm=26&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3244593858,3958579744&fm=26&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1082905169,3799258946&fm=26&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2382549967,1575870957&fm=26&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2234681052,4089284475&fm=11&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1508930897,1782742021&fm=11&gp=0.jpg",
            "http://mmbiz.qpic.cn/mmbiz_jpg/iciccsvt6Tiawr9ftibLsY9icR1iaS7urOHUqjdf8AtdWjZErIGTuUHSAI3gzpPODcBb0GnFBUiaEz7lasckBZbp6Y5mg/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1",
            "http://img5.imgtn.bdimg.com/it/u=163980870,3978538475&fm=11&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=733312119,1115252904&fm=26&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1303781219,1093366621&fm=11&gp=0.jpg",
            "http://mmbiz.qpic.cn/mmbiz_jpg/iciccsvt6Tiawr9ftibLsY9icR1iaS7urOHUqjiaaILTWUicBX6Jicys1OicDJZRWjp9T5UKKfLhS6sy3gXNE3Y60FNnKGJA/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1"
    };

    public static List<SwipeCardBean> initData() {
        List<SwipeCardBean> list = new ArrayList<>();
        final RandomColor randomColor=new RandomColor();
        for (int i = 0; i < 5; i++) {
            list.add(new SwipeCardBean(i+1, "权游__" + (i+1), urls[i],randomColor.randomColor()));
        }
        return list;
    }
    public static List<SwipeCardBean> initData(int startCount,int maxCount) {
        List<SwipeCardBean> list = new ArrayList<>();
        final RandomColor randomColor=new RandomColor();
        for (int i = startCount; i < maxCount; i++) {
            String imgUrl=i<urls.length? urls[i]:urls[0];
            list.add(new SwipeCardBean(i+1, "权游__" + (i+1),imgUrl,randomColor.randomColor()));
        }
        return list;
    }

    public SwipeCardBean(int position, String name, String url, int cardColor) {
        this.position = position;
        this.name = name;
        this.url = url;
        this.cardColor = cardColor;
    }

    public int getPosition() {
        return position;
    }


    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCardColor() {
        return cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }
}
