package com.xin.lv.yang.utils.info;

import android.os.Parcel;
import android.os.Parcelable;

public class PicItem implements Parcelable {
   public String uri;
   public boolean selected;

    protected PicItem(Parcel in) {
        uri = in.readString();
        selected = in.readByte() != 0;
    }

    public PicItem(){}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PicItem> CREATOR = new Creator<PicItem>() {
        @Override
        public PicItem createFromParcel(Parcel in) {
            return new PicItem(in);
        }

        @Override
        public PicItem[] newArray(int size) {
            return new PicItem[size];
        }
    };
}
