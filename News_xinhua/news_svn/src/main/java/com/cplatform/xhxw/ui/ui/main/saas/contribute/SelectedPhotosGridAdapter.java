package com.cplatform.xhxw.ui.ui.main.saas.contribute;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.CompanyAddImage;
import com.cplatform.xhxw.ui.plugin.gallery.PicShowActivity;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.adapter.PhotoAdappter;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.entities.PhotoItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;

public class SelectedPhotosGridAdapter extends BaseAdapter {

	private ArrayList<PhotoItem> mPhotos = new ArrayList<PhotoItem>();
	private Context mCon;
	private onSelectedPhotoOperateListener onSelectedPhotoOper;
	private PhotoItem mAddPhotoItem = new PhotoItem("", null, null);
	private int mViewWidth;
	private int mMaxPhotoCount = 6;

	private DisplayImageOptions opts = new DisplayImageOptions.Builder()
			.displayer(new RoundedBitmapDisplayer(20))
			.displayer(new FadeInBitmapDisplayer(100))
			.bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).cacheOnDisc()
			.build();

	public SelectedPhotosGridAdapter(Context mCon) {
		super();
		this.mCon = mCon;
	}

	@Override
	public int getCount() {
		return mPhotos.size();
	}

	@Override
	public PhotoItem getItem(int position) {
		return mPhotos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		mViewWidth = (mViewWidth == 0) ? (Constants.screenWidth - 30)
				: mViewWidth;
		convertView = LayoutInflater.from(mCon).inflate(
				R.layout.photoalbum_gridview_item, null);
		convertView.setLayoutParams(new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, (mViewWidth - 20)
						/ ContributeActivity.PHOTO_GRID_COLOUMNS));

		ImageView photoIv = (ImageView) convertView
				.findViewById(R.id.photo_img_view);
		ImageView photoDeleteIcon = (ImageView) convertView
				.findViewById(R.id.photo_select);
		final PhotoItem item = getItem(position);
		if (item.getFliePath() == null) {
			photoDeleteIcon.setVisibility(View.GONE);
			photoIv.setScaleType(ScaleType.FIT_XY);
			photoIv.setImageResource(R.drawable.saas_add_photo_selector);
			photoIv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onSelectedPhotoOper != null) {
						onSelectedPhotoOper.onAddPhotoClick();
					}
				}
			});
		} else {
//			photoDeleteIcon.setVisibility(View.VISIBLE);
			photoDeleteIcon.setImageResource(R.drawable.button_clear);
			photoDeleteIcon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removePhotoAlert(item);
				}
			});
			final List<String> imgs = new ArrayList<String>();
			for (int i = 0; i < getCount(); i++) {
				String path = getItem(i).getFliePath();
				if (path != null)
					imgs.add(path);
			}
			photoIv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseActivity a = (BaseActivity) mCon;
					a.startActivityForResult(PicShowActivity.newIntent(mCon,
							imgs, position, false), 11);
				}
			});

			photoIv.setScaleType(ScaleType.FIT_XY);
			if (item.getThumbPath() != null
					&& new File(item.getThumbPath()).exists()) {
				ImageLoader.getInstance().displayImage(
						"file://" + item.getThumbPath(), photoIv, opts);
			} else {
				ImageLoader.getInstance().displayImage(
						"file://" + item.getFliePath(), photoIv, opts);
			}
		}

		return convertView;
	}

	/**
	 * 移除照片提示
	 */
	private void removePhotoAlert(final PhotoItem photo) {
		new AlertDialog.Builder(mCon)
				.setTitle("移除照片")
				.setMessage("确认移除所选照片？")
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								removeAPhoto(photo);
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	/**
	 * 移除照片逻辑
	 * 
	 * @param photo
	 */
	private void removeAPhoto(PhotoItem photo) {
		for (PhotoItem item : mPhotos) {
			if ((item.getFliePath() != null && item.getFliePath().equals(
					photo.getFliePath()))
					|| (item.getThumbPath() != null && item.getThumbPath()
							.equals(photo.getThumbPath()))) {
				mPhotos.remove(item);
				if (onSelectedPhotoOper != null) {
					onSelectedPhotoOper.onRemovePhoto();
				}
				break;
			}
		}

		if (mPhotos.size() == mMaxPhotoCount - 1) {
			if (!mPhotos.contains(mAddPhotoItem)) {
				mPhotos.add(mAddPhotoItem);
			}
		}
		notifyDataSetChanged();
	}

	public interface onSelectedPhotoOperateListener {
		void onAddPhotoClick();

		void onRemovePhoto();
	}

	public ArrayList<PhotoItem> getmPhotos() {
		return mPhotos;
	}

	/**
	 * 初始化所有数据
	 * 
	 * @param photos
	 * @param viewWidth
	 */
	public void setmPhotos(ArrayList<PhotoItem> photos, int viewWidth) {
		this.mPhotos.clear();
		this.mPhotos.addAll(photos);
		if (this.mPhotos.size() < mMaxPhotoCount) {
			this.mPhotos.add(mAddPhotoItem);
		}
		this.mViewWidth = viewWidth;
		notifyDataSetChanged();
	}

	/**
	 * 插入新的照片
	 * 
	 * @param photo
	 */
	public void insertAPhoto(PhotoItem photo) {
		this.mPhotos.add(this.mPhotos.size() - 1, photo);
		notifyDataSetChanged();
	}

	public onSelectedPhotoOperateListener getOnSelectedPhotoOper() {
		return onSelectedPhotoOper;
	}

	public void setOnSelectedPhotoOper(
			onSelectedPhotoOperateListener onSelectedPhotoOper) {
		this.onSelectedPhotoOper = onSelectedPhotoOper;
	}

	public int getmMaxPhotoCount() {
		return mMaxPhotoCount;
	}

	public void setmMaxPhotoCount(int mMaxPhotoCount) {
		this.mMaxPhotoCount = mMaxPhotoCount;
	}
}
