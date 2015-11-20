package com.hy.superemsg.rsp;

import java.util.ArrayList;
import java.util.List;

import com.hy.superemsg.db.DBColumns;
import com.hy.superemsg.utils.CommonUtils;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class AnimationContentDetail extends AbsContentDetail implements
		Parcelable {
	public String amid;
	public String amname;
	public String amauthor;
	public String amintroduce;
	public String amcoverpicurl;
	public List<AnimationDramaDetail> amdramalist;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(amid);
		dest.writeString(amname);
		dest.writeString(amauthor);
		dest.writeString(amintroduce);
		dest.writeString(amcoverpicurl);
		dest.writeTypedList(amdramalist);
	}

	public AnimationContentDetail() {
	}

	public AnimationContentDetail(Parcel source) {
		amid = source.readString();
		amname = source.readString();
		amauthor = source.readString();
		amintroduce = source.readString();
		amcoverpicurl = source.readString();
		amdramalist = new ArrayList<AnimationDramaDetail>();
		source.readTypedList(amdramalist, AnimationDramaDetail.CREATOR);
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(DBColumns.CartoonColumns.CARTOON_NAME, amname);
		values.put(DBColumns.CartoonColumns.CARTOON_AUTHOR, amauthor);
		values.put(DBColumns.CartoonColumns.CARTOON_INTRODUCE, amintroduce);
		values.put(DBColumns.CartoonColumns.CARTOON_COVER_PIC_URL,
				amcoverpicurl);
		return values;
	}

	@Override
	public String getId() {
		return amid;
	}

	public static final Parcelable.Creator<AnimationContentDetail> CREATOR = new Parcelable.Creator<AnimationContentDetail>() {

		@Override
		public AnimationContentDetail createFromParcel(Parcel source) {
			return new AnimationContentDetail(source);
		}

		@Override
		public AnimationContentDetail[] newArray(int size) {
			return new AnimationContentDetail[] {};
		}

	};
}
