package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cy-love on 14-6-7.
 */
public class CompanyCircleAlbumPreviewAdapter extends BaseAdapter {

	private Context context;
	private List<AlbumGroup> dateList;
	private List<String> selectList;
	private OnAlbumPreviewListener mListener;
	private LayoutInflater mInflater;
	private int selectCount;
    private int mImgWidth;

	public CompanyCircleAlbumPreviewAdapter(Context context) {
		this.context = context;
		dateList = new ArrayList<AlbumGroup>();
		selectList = new ArrayList<String>();
		mInflater = LayoutInflater.from(context);
        mImgWidth = Constants.screenWidth / 4;
	}

	@Override
	public int getCount() {
		return dateList.size();
	}

	@Override
	public AlbumGroup getItem(int position) {
		return dateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHelper helper;
		final AlbumGroup item = dateList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.fragment_company_circle_album_preview_item, null);
			helper = new ViewHelper();

			helper.album = (ImageView) convertView.findViewById(R.id.iv_album);
			helper.check = (ImageView) convertView.findViewById(R.id.media_cbx);

			convertView.setTag(helper);
		} else {
			helper = (ViewHelper) convertView.getTag();
		}
        Picasso.with(context).load(new File(item.getUrl())).centerCrop().resize(mImgWidth, mImgWidth).error(R.drawable.def_img_4_3).into(helper.album);
		if (selectList.contains(item.getUrl())) {
			helper.check.setBackgroundResource(R.drawable.send_message_checked);
		}else {
			helper.check.setBackgroundResource(R.drawable.send_message_nochecked);
		}
//		helper.check.setChecked(item.isChecked());
//		helper.check.setTag(position);
//		helper.check.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				int tag = (Integer) view.getTag();
//				if (selectList.contains(dateList.get(tag).getUrl())) {
//					selectList.remove(dateList.get(tag).getUrl());
//					selectCount--;
//					((CompoundButton) view).setChecked(false);
//					dateList.get(tag).setChecked(false);
//				} else {
//					if (selectCount < 6) {
//						selectList.add(dateList.get(tag).getUrl());
//						selectCount++;
//						((CompoundButton) view).setChecked(true);
//						dateList.get(tag).setChecked(true);
//					} else {
//						((CompoundButton) view).setChecked(false);
//						dateList.get(tag).setChecked(false);
//						Toast.makeText(context, "图片达到上限了", Toast.LENGTH_SHORT)
//								.show();
//					}
//				}
//
//				if (mListener != null) {
//					mListener.updateSelectCount(selectCount);
//				}
//			}
//		});

		return convertView;
	}

	static class ViewHelper {
		ImageView album;
		ImageView check;
	}

	private View.OnClickListener checkClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String url = (String) v.getTag();
			setSelect(url);
		}
	};

	private void setSelect(String url) {// /storage/sdcard0/adroid/bg/diy_new_1/f138c287b0f3f1e43a49d0c021ad67ca.jpg
		if (selectList.contains(url)) {
			selectList.remove(url);
			selectCount--;
		} else {
			if (selectCount < 6) {
				selectList.add(url);
				selectCount++;
			} else {
				Toast.makeText(context, R.string.select_picture_reached_the_limit, Toast.LENGTH_SHORT).show();
			}
		}
		if (mListener != null) {
			mListener.updateSelectCount(selectCount);
		}
	}

	public interface OnAlbumPreviewListener {
		public void updateSelectCount(int count);
	}

	public void setOnAlbumPreviewListener(OnAlbumPreviewListener listener) {
		this.mListener = listener;
	}

	public List<AlbumGroup> getDateList() {
		return dateList;
	}

	public void setDateList(List<AlbumGroup> dateList) {
		this.dateList = dateList;
	}

	public List<String> getSelectList() {
		return selectList;
	}

	public void setSelectList(List<String> selectList) {
		this.selectList = selectList;
	}

	public int getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(int selectCount) {
		this.selectCount = selectCount;
	}

}
