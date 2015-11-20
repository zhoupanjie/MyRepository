package com.hy.superemsg.data;

import java.util.Comparator;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.hy.superemsg.utils.CommonUtils;

public class Contact implements Parcelable {
	public String id;
	public String name;
	public String phone;
	public String pinyin;
	public boolean isSelected = false;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(phone);
		dest.writeString(pinyin);
	}

	public Contact() {

	}

	public Contact(Parcel source) {
		id = source.readString();
		name = source.readString();
		phone = source.readString();
		pinyin = source.readString();
	}

	public char getContactPointer() {
		if (isPointer) {
			return name.charAt(0);
		}
		return pinyin.charAt(0);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source) {
			return new Contact(source);
		}

		@Override
		public Object[] newArray(int size) {
			return new Contact[] {};
		}

	};
	public boolean isPointer = false;

	public static Contact createFakeContact(char pointer) {
		Contact ret = new Contact();
		ret.isPointer = true;
		ret.name = Character.toString(pointer);
		return ret;
	}

	public static Comparator<Contact> pinyinASCComparator = new Comparator<Contact>() {

		@Override
		public int compare(Contact c1, Contact c2) {
			char char1 = c1.pinyin.charAt(0);
			char char2 = c2.pinyin.charAt(0);
			return char1 - char2;
		}
	};

	public static int getCount(List<? extends Contact> contacts) {
		int ret = 0;
		if (CommonUtils.isNotEmpty(contacts)) {
			for (Contact contact : contacts) {
				if (!contact.isPointer) {
					ret += 1;
				}
			}
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o.getClass().equals(Contact.class)) {
			Contact another = (Contact) o;
			if (!isPointer) {
				return id.equals(another.id);
			} else {
				return name.equals(another.name);
			}
		}
		return false;
	}

	public String getContactVisibleContent() {
		String ret = null;
		if (!isPointer) {
			ret = name;
			if (ret == null) {
				ret = phone;
			}
		}
		return ret;
	}

}
