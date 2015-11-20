
package com.hy.superemsg.rsp;

import com.hy.superemsg.db.DBColumns;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class RingContentDetail extends AbsContentDetail implements Parcelable {
    public String ringid;
    public String ringname;
    public String ringsinger;
    public String ringprice;
    public int ringusecount;

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DBColumns.ID, ringid);
        values.put(DBColumns.RingColumns.RING_NAME, ringname);
        values.put(DBColumns.RingColumns.RING_SINGER, ringsinger);
        values.put(DBColumns.RingColumns.RING_PRICE, ringprice);
        values.put(DBColumns.RingColumns.RING_USE_COUNT, ringusecount);
        return values;
    }

    @Override
    public String getId() {
        return ringid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ringid);
        dest.writeString(ringname);
        dest.writeString(ringsinger);
        dest.writeString(ringprice);
        dest.writeInt(ringusecount);
    }

    public RingContentDetail() {
    }

    public RingContentDetail(Parcel source) {
        ringid = source.readString();
        ringname = source.readString();
        ringsinger = source.readString();
        ringprice = source.readString();
        ringusecount = source.readInt();
    }

    public static final Parcelable.Creator<RingContentDetail> CREATOR = new Parcelable.Creator<RingContentDetail>() {

        @Override
        public RingContentDetail createFromParcel(Parcel source) {
            return new RingContentDetail(source);
        }

        @Override
        public RingContentDetail[] newArray(int size) {
            return new RingContentDetail[] {};
        }

    };

}
