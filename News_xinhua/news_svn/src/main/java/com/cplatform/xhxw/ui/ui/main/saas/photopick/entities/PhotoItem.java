package com.cplatform.xhxw.ui.ui.main.saas.photopick.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoItem implements Parcelable {
	/**
	 * 原照片id
	 */
	private String imageId;
	/**
	 * 照片缩略图文件路径， 有时为空
	 */
	private String thumbPath;
	
	/**
	 * 照片原文件路径，不为空
	 */
	private String fliePath;
	
	public PhotoItem() {
	}
	
	public PhotoItem(String imageId, String thumbPath, String filePath) {
		super();
		this.imageId = imageId;
		this.thumbPath = thumbPath;
		this.fliePath = filePath;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	@Override
	public String toString() {
		return "PhotoItem [imageId=" + imageId + ", thumbPath=" + thumbPath + "]";
	}

	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imageId);
		dest.writeString(thumbPath);
		dest.writeString(fliePath);
	}
	
	public String getFliePath() {
		return fliePath;
	}

	public void setFliePath(String fliePaht) {
		this.fliePath = fliePaht;
	}

	public static final Parcelable.Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
		
		@Override
		public PhotoItem[] newArray(int size) {
			return new PhotoItem[size];
		}
		
		@Override
		public PhotoItem createFromParcel(Parcel source) {
			PhotoItem item = new PhotoItem();
			item.imageId = source.readString();
			item.thumbPath = source.readString();
			item.fliePath = source.readString();
			return item;
		}
	};
}
