package com.hy.superemsg.rsp;

import com.hy.superemsg.db.DBColumns;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class SmsContentDetail extends AbsContentDetail implements Parcelable {
	public String smsid;
	public String smscontent;

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(DBColumns.ID, smsid);
		values.put(DBColumns.SmsColumns.SMS_CONTENT, smscontent);
		return values;
	}

	@Override
	public String getId() {
		return smsid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(smsid);
		dest.writeString(smscontent);
	}

	public SmsContentDetail() {
	}

	public SmsContentDetail(Parcel source) {
		smsid = source.readString();
		smscontent = source.readString();
	}

	public static final Parcelable.Creator<SmsContentDetail> CREATOR = new Parcelable.Creator<SmsContentDetail>() {

		@Override
		public SmsContentDetail createFromParcel(Parcel source) {
			return new SmsContentDetail(source);
		}

		@Override
		public SmsContentDetail[] newArray(int size) {
			return new SmsContentDetail[] {};
		}

	};

}
