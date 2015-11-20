package com.hy.superemsg.rsp;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsCategory extends Category {
	public int contenttype;
	public static final int TYPE_NEWS = 1;
	public static final int TYPE_PIC = 2;

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(contenttype);
	}

	public NewsCategory() {
	}

	public NewsCategory(Parcel source) {
		super(source);
		contenttype = source.readInt();
	}

	public static final Parcelable.Creator<NewsCategory> CREATOR = new Parcelable.Creator<NewsCategory>() {

		@Override
		public NewsCategory createFromParcel(Parcel source) {
			return new NewsCategory(source);
		}

		@Override
		public NewsCategory[] newArray(int size) {
			return new NewsCategory[] {};
		}

	};
}
