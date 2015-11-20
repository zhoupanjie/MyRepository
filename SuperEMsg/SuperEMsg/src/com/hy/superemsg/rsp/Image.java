package com.hy.superemsg.rsp;

import android.os.Parcel;
import android.os.Parcelable;


public class Image implements Parcelable {
	public String ref;
	public String alt;
	public String src;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ref);
		dest.writeString(alt);
		dest.writeString(src);
	}
	public Image() {
	}

	public Image(Parcel source) {
		ref = source.readString();
		alt = source.readString();
		src = source.readString();
	}

	public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {

		@Override
		public Image createFromParcel(Parcel source) {
			return new Image(source);
		}

		@Override
		public Image[] newArray(int size) {
			return new Image[] {};
		}

	};
}
