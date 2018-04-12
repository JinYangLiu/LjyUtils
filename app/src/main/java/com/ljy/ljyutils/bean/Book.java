package com.ljy.ljyutils.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LJY on 2018/4/12.
 */

public class Book implements Parcelable {
    public String name;
    public int count;

    public Book() {
    }

    public Book(String name, int count) {
        this.name = name;
        this.count = count;
    }

    protected Book(Parcel in) {
        name = in.readString();
        count = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(count);
    }

    public void readFromParcel(Parcel in){
        name = in.readString();
        count = in.readInt();
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
