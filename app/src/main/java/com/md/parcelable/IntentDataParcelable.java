package com.md.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class IntentDataParcelable implements Parcelable {

    public int mInt;
    public String mStr;
    public ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(mInt);
        dest.writeString(mStr);
        dest.writeList(mList);

    }

    public static final Creator<IntentDataParcelable> CREATOR
            = new Creator<IntentDataParcelable>() {

        @Override
        public IntentDataParcelable[] newArray(int size) {
            // TODO Auto-generated method stub
            return new IntentDataParcelable[size];
        }

        @Override
        public IntentDataParcelable createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new IntentDataParcelable(source);
        }
    };

    private IntentDataParcelable(Parcel source) {
        mInt = source.readInt();
        mStr = source.readString();
        mList = source.readArrayList(ArrayList.class.getClassLoader());
    }

    public IntentDataParcelable() {

    }
}
