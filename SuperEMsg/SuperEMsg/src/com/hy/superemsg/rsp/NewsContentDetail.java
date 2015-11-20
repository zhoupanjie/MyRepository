package com.hy.superemsg.rsp;

import java.util.ArrayList;
import java.util.List;

import com.hy.superemsg.db.DBColumns;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.MobileInfo;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class NewsContentDetail extends AbsContentDetail implements Parcelable {
	public String newsid;
	public String newstitle;
	public String newsleads;
	public String newsfocuspicurl;
	public String newspicurl;
	public String newscontent;
	public List<Image> img;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(newsid);
		dest.writeString(newstitle);
		dest.writeString(newsleads);
		dest.writeString(newsfocuspicurl);
		dest.writeString(newspicurl);
		dest.writeString(newscontent);
		dest.writeTypedList(img);
	}

	public NewsContentDetail() {
	}

	public NewsContentDetail(Parcel source) {
		newsid = source.readString();
		newstitle = source.readString();
		newsleads = source.readString();
		newsfocuspicurl = source.readString();
		newspicurl = source.readString();
		newscontent = source.readString();
		img = new ArrayList<Image>();
		source.readTypedList(img, Image.CREATOR);
	}

	public static final Parcelable.Creator<NewsContentDetail> CREATOR = new Parcelable.Creator<NewsContentDetail>() {

		@Override
		public NewsContentDetail createFromParcel(Parcel source) {
			return new NewsContentDetail(source);
		}

		@Override
		public NewsContentDetail[] newArray(int size) {
			return new NewsContentDetail[] {};
		}

	};

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(DBColumns.NewsColumns.NEWS_TITLE, newstitle);
		values.put(DBColumns.NewsColumns.NEWS_LEADS, newsleads);
		values.put(DBColumns.NewsColumns.NEWS_CONTENT, newscontent);
		values.put(DBColumns.NewsColumns.NEWS_FOCUS_PIC_URL, newsfocuspicurl);
		return values;
	}

	@Override
	public String getId() {
		return newsid;
	}

	public void replaceImgRefWithRealImgSrc() {
		if (!TextUtils.isEmpty(newscontent)) {
			if (CommonUtils.isNotEmpty(img)) {
				for (Image i : img) {
					String ref = i.ref;
					if (!TextUtils.isEmpty(ref)) {
						String realSrc = String
								.format("<img src=\"%1$s\" alt=\"%2$s\" width=\"%3$s\" />",
										new Object[] { i.src, i.alt, "100%" });
						newscontent = newscontent.replace(ref, realSrc);
					}
				}
			}
		}
	}

	public void replaceImgExplicitPixel() {
		newscontent = newscontent.replace("width=\"320\"", "width=\"100%\"");
	}
}
