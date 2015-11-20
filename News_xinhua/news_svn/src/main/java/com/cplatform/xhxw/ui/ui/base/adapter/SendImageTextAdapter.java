package com.cplatform.xhxw.ui.ui.base.adapter;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.CompanyAddImage;
import com.cplatform.xhxw.ui.model.CompanyImage;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SendImageTextAdapter extends BaseAdapter<CompanyImage>{

	public SendImageTextAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final CompanyImage item = getItem(position);
		ViewHodler hodler = null;
		if (view == null) {
			hodler = new ViewHodler();
			view = LayoutInflater.from(mContext).inflate(R.layout.activity_send_image_text_item, null);
			
			hodler.imageView = (ImageView) view.findViewById(R.id.send_image_text_item_image);
			
			view.setTag(hodler);
		}else {
			hodler = (ViewHodler) view.getTag();
		}
		
		setShape(hodler.imageView);
		
		if (!(item instanceof CompanyAddImage)) {
			ImageLoader.getInstance().displayImage(
					Uri.fromFile(new File(item.getFileUrl())).toString(),
					hodler.imageView,
					DisplayImageOptionsUtil.avatarFriendsInfoImagesOptions);
		} else {
			ImageLoader.getInstance().cancelDisplayTask(hodler.imageView);
			hodler.imageView.setImageBitmap(null);
			hodler.imageView.setBackgroundResource(R.drawable.publish_message_add_image_bg);
		}
		
		return view;
	}
	
	private class ViewHodler {
		ImageView imageView;
	}
	
	private void setShape(ImageView imageView) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
				.getLayoutParams();
		params.width = (Constants.screenWidth - 2 * mContext.getResources()
				.getDimensionPixelOffset(R.dimen.company_gridview_margin) - 3 * mContext.getResources()
				.getDimensionPixelOffset(R.dimen.company_gridview_distance)) / 4;
		params.height = params.width;
		imageView.setLayoutParams(params);
	}

}
