package com.ljy.ljyutils.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LJY on 2018/4/12.
 */

public class ParceBean implements Parcelable {
    public String name;
    public Parce2Bean mParce2Bean;

    public ParceBean(String name, Parce2Bean parce2Bean) {
        this.name = name;
        mParce2Bean = parce2Bean;
    }

    protected ParceBean(Parcel in) {
        name = in.readString();
        mParce2Bean = in.readParcelable(Parce2Bean.class.getClassLoader());
    }

    public static final Creator<ParceBean> CREATOR = new Creator<ParceBean>() {
        @Override
        public ParceBean createFromParcel(Parcel in) {
            return new ParceBean(in);
        }

        @Override
        public ParceBean[] newArray(int size) {
            return new ParceBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(mParce2Bean, flags);
    }

    @Override
    public String toString() {
        return "ParceBean{" +
                "name='" + name + '\'' +
                ", mParce2Bean=" + mParce2Bean +
                '}';
    }
}
