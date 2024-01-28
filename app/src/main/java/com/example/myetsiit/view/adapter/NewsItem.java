package com.example.myetsiit.view.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class NewsItem implements Parcelable {

    private int imageResId;
    private String title;

    private String pieDeFoto;

    public NewsItem(int imageResId, String title, String pieDeFoto) {
        this.imageResId = imageResId;
        this.title = title;
        this.pieDeFoto = pieDeFoto;
    }

    protected NewsItem(Parcel in) {
        imageResId = in.readInt();
        title = in.readString();
        pieDeFoto = in.readString();
    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getPieDeFoto() {
        return pieDeFoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(imageResId);
        dest.writeString(title);
        dest.writeString(pieDeFoto);
    }
}
