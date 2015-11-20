package com.hy.superemsg.rsp;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
	public String categoryid;
	public String categoryname;

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Category) {
			Category another = (Category) o;
			if (categoryid.equals(another.categoryid)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(categoryid);
		dest.writeString(categoryname);
	}

	public Category() {
	}

	public Category(Parcel source) {
		categoryid = source.readString();
		categoryname = source.readString();
	}

	public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {

		@Override
		public Category createFromParcel(Parcel source) {
			return new Category(source);
		}

		@Override
		public Category[] newArray(int size) {
			return new Category[] {};
		}

	};
}
