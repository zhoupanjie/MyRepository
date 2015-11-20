package com.cplatform.xhxw.ui.ui.main.saas.photopick.adapter;
import java.io.File;
import java.util.ArrayList;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.entities.PhotoItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PhotoAdappter extends BaseAdapter {
	private int mMaxPicPhotos = 6;
	
	private Context context;
	private ArrayList<PhotoItem> mAllPhotos;
	private ArrayList<PhotoItem> selectedPhotos = new ArrayList<PhotoItem>();
	private onSelectPhotoListener onSelectLis;
	
	private DisplayImageOptions opts = new DisplayImageOptions.Builder()
		.displayer(new RoundedBitmapDisplayer(20))
		.displayer(new FadeInBitmapDisplayer(100))
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.cacheOnDisc().build();
	
	public PhotoAdappter(Context context, ArrayList<PhotoItem> photos) {
		this.context = context;
		this.mAllPhotos = photos;
	}

	@Override
	public int getCount() {
		return mAllPhotos.size();
	}

	@Override
	public PhotoItem getItem(int position) {
		return mAllPhotos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView == null){
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.photoalbum_gridview_item, null);
			vh.photo = (ImageView) convertView.findViewById(R.id.photo_img_view);
			vh.selector = (ImageView) convertView.findViewById(R.id.photo_select);
			convertView.setLayoutParams(new AbsListView.LayoutParams(
					AbsListView.LayoutParams.MATCH_PARENT, Constants.screenWidth / 3));
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		final PhotoItem item = getItem(position);
		vh.photo.setImageResource(R.drawable.def_img_4_3);
		
		if(item.getThumbPath() != null && new File(item.getThumbPath()).exists()) {
			ImageLoader.getInstance().displayImage("file://" + item.getThumbPath(), 
					vh.photo, opts);
		} else {
			ImageLoader.getInstance().displayImage("file://" + item.getFliePath(), 
					vh.photo, opts);
		}
		
		vh.selector.setImageResource(isPhotoSelected(item) ? R.drawable.ic_select_pressed : 
			R.drawable.ic_select_normal);
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isPhotoSelected(item)) {
					if(unSelectPhoto(item)) {
						((ViewHolder)v.getTag()).selector.setImageResource(R.drawable.ic_select_normal);
					}
				} else {
					if(selectPhoto(item)) {
						((ViewHolder)v.getTag()).selector.setImageResource(R.drawable.ic_select_pressed);
					}
				}
				if(onSelectLis != null) {
					onSelectLis.onPhotoSelected(selectedPhotos.size());
				}
			}
		});
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView photo;
		ImageView selector;
	}
	
	private boolean isPhotoSelected(PhotoItem photo) {
		for(PhotoItem item : selectedPhotos) {
			if(item.getImageId().equals(photo.getImageId())) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean selectPhoto(PhotoItem item) {
		if(selectedPhotos.size() >= mMaxPicPhotos) {
			return false;
		}
		if(!isPhotoSelected(item)) {
			selectedPhotos.add(item);
			return true;
		}
		return false;
	}
	
	private boolean unSelectPhoto(PhotoItem item) {
		for(PhotoItem data : selectedPhotos) {
			if(item.getImageId().equals(data.getImageId())) {
				selectedPhotos.remove(data);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<PhotoItem> getSelectedPhotos() {
		return selectedPhotos;
	}
	
	public void setSelectedPhotos( ArrayList<PhotoItem> selected) {
		selectedPhotos.clear();
		selectedPhotos.addAll(selected);
		notifyDataSetChanged();
	}
	
	public onSelectPhotoListener getOnSelectLis() {
		return onSelectLis;
	}

	public void setOnSelectLis(onSelectPhotoListener onSelectLis) {
		this.onSelectLis = onSelectLis;
	}

	public int getmMaxPicPhotos() {
		return mMaxPicPhotos;
	}

	public void setmMaxPicPhotos(int mMaxPicPhotos) {
		this.mMaxPicPhotos = mMaxPicPhotos;
	}

	public interface onSelectPhotoListener{
		void onPhotoSelected(int selectedCount);
	}
}
