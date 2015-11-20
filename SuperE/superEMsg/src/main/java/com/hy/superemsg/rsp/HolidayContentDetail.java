package com.hy.superemsg.rsp;

import com.hy.superemsg.db.DBColumns;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class HolidayContentDetail extends AbsContentDetail implements
		Parcelable {
	public String smsid;
	public String smscontent;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(smsid);
		dest.writeString(smscontent);
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(DBColumns.ID, smsid);
		values.put(DBColumns.HolidayColumns.HOLIDAY_CONTENT, smscontent);
		return values;
	}

	@Override
	public String getId() {
		return smsid;
	}
	public HolidayContentDetail() {
	}

	public HolidayContentDetail(Parcel source) {
		smsid = source.readString();
		smscontent = source.readString();
	}

	public static final Parcelable.Creator<HolidayContentDetail> CREATOR = new Parcelable.Creator<HolidayContentDetail>() {

		@Override
		public HolidayContentDetail createFromParcel(Parcel source) {
			return new HolidayContentDetail(source);
		}

		@Override
		public HolidayContentDetail[] newArray(int size) {
			return new HolidayContentDetail[] {};
		}

	};

}
