package com.hy.superemsg.rsp;

import android.content.ContentValues;

public abstract class AbsContentDetail {
	public abstract ContentValues toContentValues();

	public abstract String getId();

	private boolean isCollected;

	public void setCollected(boolean collect) {
		isCollected = collect;
	}

	public boolean isCollected() {
		return isCollected;
	}
}
