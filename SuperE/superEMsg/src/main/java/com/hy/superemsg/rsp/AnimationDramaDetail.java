package com.hy.superemsg.rsp;

import com.hy.superemsg.db.DBColumns;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class AnimationDramaDetail extends AbsContentDetail implements
		Parcelable {
	public String dramaname;
	public String dramafilepicurl;
	public int dramapiccount;
	public String dramaId;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(dramaId);
		dest.writeString(dramaname);
		dest.writeString(dramafilepicurl);
		dest.writeInt(dramapiccount);
	}

	public AnimationDramaDetail() {
	}

	public AnimationDramaDetail(Parcel source) {
		dramaId = source.readString();
		dramaname = source.readString();
		dramafilepicurl = source.readString();
		dramapiccount = source.readInt();
	}

	public static final Parcelable.Creator<AnimationDramaDetail> CREATOR = new Parcelable.Creator<AnimationDramaDetail>() {

		@Override
		public AnimationDramaDetail createFromParcel(Parcel source) {
			return new AnimationDramaDetail(source);
		}

		@Override
		public AnimationDramaDetail[] newArray(int size) {
			return new AnimationDramaDetail[] {};
		}

	};

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(DBColumns.ID, dramaId);
		values.put(DBColumns.CartoonDramaColumns.DRAMA_NAME, dramaname);
		values.put(DBColumns.CartoonDramaColumns.DRAMA_FILE_URL,
				dramafilepicurl);
		values.put(DBColumns.CartoonDramaColumns.DRAMA_PIC_COUNT, dramapiccount);
		return values;
	}

	@Override
	public String getId() {
		return dramaId;
	}

}
