package com.hy.superemsg.adapter;

import android.view.View;

public interface IPinnedHeaderAdapter {
	public static final int PINNED_HEADER_GONE = 0;

	public static final int PINNED_HEADER_VISIBLE = 1;

	public static final int PINNED_HEADER_PUSHED_UP = 2;

	public int getPinedHeaderState(int postion);

	public void configPinnedHeader(View header, int postion);
}