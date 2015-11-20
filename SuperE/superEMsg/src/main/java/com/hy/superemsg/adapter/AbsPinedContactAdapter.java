package com.hy.superemsg.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.components.PinnedHeaderListView;
import com.hy.superemsg.data.Contact;
import com.hy.superemsg.utils.CommonUtils;

public abstract class AbsPinedContactAdapter extends AbsCommonAdapter<Contact>
		implements IPinnedHeaderAdapter, OnScrollListener {
	public static final char[] POINTERS = { '#', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	public AbsPinedContactAdapter(Context ctx, int layout) {
		super(ctx, layout);
	}

	@Override
	public boolean isEnabled(int position) {
		if (checkIfNoData()) {
			return false;
		}
		Contact contact = getItem(position);
		if (contact.isPointer) {
			return false;
		}
		return super.isEnabled(position);
	}

	private boolean checkIfNoData() {
		return getCount() == 0;
	}

	public int getPositionByContact(Contact contact) {
		for (int i = 0; i < getCount(); i++) {
			Contact item = getItem(i);
			if (!contact.isPointer) {
				if (item.isPointer)
					continue;
				if (item.equals(contact)) {
					return i;
				}
			} else {
				if (!item.isPointer)
					continue;
				if (contact.name.equals(item.name)) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public void configPinnedHeader(View header, int postion) {
		if (checkIfNoData()) {
			return;
		}
		if (header != null) {
			TextView tx = (TextView) header.findViewById(R.id.item_text);
			tx.setText(getItem(postion).getContactPointer() + "");
		}
	}

	private int getPointerFromPosition(int position) {
		if (checkIfNoData()) {
			return 0;
		}
		Contact contact = getItem(position);
		int index = Arrays.binarySearch(POINTERS, contact.getContactPointer());
		return index > -1 ? index : -index - 2;
	}

	private int getPinnedPostionFromSection(int section) {
		if (checkIfNoData()) {
			return 0;
		}
		char pointer = POINTERS[section];
		for (int i = 0; i < getCount(); i++) {
			if (getItem(i).getContactPointer() == pointer) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view)
					.configPinnedHeaderView(firstVisibleItem);
		}
	}

	public void setDatas(List<Contact> datas, pointerExpressListener listener) {
		if (CommonUtils.isNotEmpty(datas)) {
			mergePointers(datas, POINTERS);
		}
		if (listener != null) {
			List<Contact> pointerList = new ArrayList<Contact>();
			for (Contact contact : datas) {
				if (contact.isPointer) {
					pointerList.add(contact);
				}
			}
			listener.onPointerChanged(pointerList);
		}
		super.setDatas(datas);
	}


	public interface pointerExpressListener {
		void onPointerChanged(List<Contact> datas);
	}

	private void mergePointers(List<Contact> datas, char[] pointers) {
		for (int i = 0; i < POINTERS.length; i++) {
			for (int j = 0; j < datas.size(); j++) {
				if (datas.get(j).getContactPointer() == pointers[i]) {
					datas.add(j, Contact.createFakeContact(pointers[i]));
					break;
				}
			}
		}
	}

	@Override
	public int getPinedHeaderState(int postion) {

		if (postion < 0 || postion > getCount())
			return IPinnedHeaderAdapter.PINNED_HEADER_GONE;
		int pointerIndex = getPointerFromPosition(postion);
		int currentSectionLastPostion = 0;
		if (pointerIndex < POINTERS.length - 1) {
			currentSectionLastPostion = getPinnedPostionFromSection(pointerIndex + 1) - 1;
		} else {
			currentSectionLastPostion = getCount() - 1;
		}
		if (postion == currentSectionLastPostion) {
			return IPinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
		}
		return IPinnedHeaderAdapter.PINNED_HEADER_VISIBLE;

	}

}
