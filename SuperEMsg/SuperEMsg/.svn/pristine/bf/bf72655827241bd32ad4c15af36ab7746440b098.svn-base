
package com.hy.superemsg.rsp;

import com.hy.superemsg.db.DBColumns;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class MmsContentDetail extends AbsContentDetail implements Parcelable {
    public String mmsid;
    public String mmsname;
    public String mmscontent;
    public String mmspicurl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mmsid);
        dest.writeString(mmsname);
        dest.writeString(mmscontent);
        dest.writeString(mmspicurl);
    }

    public MmsContentDetail() {

    }

    public MmsContentDetail(Parcel source) {
        mmsid = source.readString();
        mmsname = source.readString();
        mmscontent = source.readString();
        mmspicurl = source.readString();
    }

    public static final Parcelable.Creator<MmsContentDetail> CREATOR = new Parcelable.Creator<MmsContentDetail>() {

        @Override
        public MmsContentDetail createFromParcel(Parcel source) {
            return new MmsContentDetail(source);
        }

        @Override
        public MmsContentDetail[] newArray(int size) {
            return new MmsContentDetail[] {};
        }

    };

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DBColumns.ID, mmsid);
        values.put(DBColumns.MmsColumns.MMS_NAME, mmsname);
        values.put(DBColumns.MmsColumns.MMS_CONTENT, mmscontent);
        values.put(DBColumns.MmsColumns.MMS_PIC_URL, mmspicurl);
        return values;
    }

    @Override
    public String getId() {
        return mmsid;
    }
}
