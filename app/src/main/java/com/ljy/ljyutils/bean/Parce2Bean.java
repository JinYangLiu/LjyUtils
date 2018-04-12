package com.ljy.ljyutils.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LJY on 2018/4/12.
 */

public class Parce2Bean implements Parcelable {
    private String info;

    public Parce2Bean(String info) {
        this.info = info;
    }

    protected Parce2Bean(Parcel in) {
        info = in.readString();
    }

    public static final Creator<Parce2Bean> CREATOR = new Creator<Parce2Bean>() {
        @Override
        public Parce2Bean createFromParcel(Parcel in) {
            return new Parce2Bean(in);
        }

        @Override
        public Parce2Bean[] newArray(int size) {
            return new Parce2Bean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
    }

    @Override
    public String toString() {
        return "Parce2Bean{" +
                "info='" + info + '\'' +
                '}';
    }
}
