package com.cplatform.xhxw.ui.ui.main.saas.photopick;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.entities.PhotoItem;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 拍摄照片保存后，询问用户是否选择使用此照片页面<br>
 * 调用方法：<br>
 * Intent it = new Intent(this, SelectTookenPhotoActivity.class);<br>
 * it.putExtra(SelectTookenPhotoActivity.PHOTO_PATH, photosavepath);<br>
 * startActivityForResult(it, requestcode);<br>
 * <br>
 * 返回数据：PhotoItem对象<br>
 * 使用getParcelableExtra(SelectTookenPhotoActivity.BACK_DATA)获取
 */
public class SelectTookenPhotoActivity extends BaseActivity implements OnClickListener {
	public static final String PHOTO_PATH = "photo_path";
	public static final String BACK_DATA = "data";
	
	private CheckBox mCheckBox;
	private Button mDoneBtn;
	private ImageView mImageView;
	private String mPhotoPath;

	private DisplayImageOptions opts = new DisplayImageOptions.Builder()
									.displayer(new RoundedBitmapDisplayer(20))
									.displayer(new FadeInBitmapDisplayer(100))
									.bitmapConfig(Bitmap.Config.RGB_565)
									.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
									.cacheOnDisc().build();
	
	@Override
	protected String getScreenName() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tooken_photo);
		initView();
	}

	private void initView() {
		mCheckBox = (CheckBox) findViewById(R.id.select_tooken_photo_cb);
		mDoneBtn = (Button) findViewById(R.id.select_tooken_photo_done_btn);
		mImageView = (ImageView) findViewById(R.id.select_tooken_photo_iv);
		mDoneBtn.setOnClickListener(this);
		
		mCheckBox.setChecked(true);
		mPhotoPath = getIntent().getStringExtra(PHOTO_PATH);
		ImageLoader.getInstance().displayImage("file://" + mPhotoPath, mImageView, opts);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.select_tooken_photo_done_btn) {
			if(mCheckBox.isChecked()) {
				Intent it = initBackData();
				if(it != null) {
					setResult(RESULT_OK, initBackData());
				}
			}
			finish();
		}
	}
	
	private Intent initBackData() {
		PhotoItem data = new PhotoItem();
		String imageId = PhotoManagerUtil.getImageIdByPath(this, mPhotoPath);
		data.setImageId(imageId == null ? "" : imageId);
		if(imageId != null) {
			String imageThum = PhotoManagerUtil.getImageThumbById(this, imageId);
			data.setThumbPath(imageThum);
		} else {
			data.setThumbPath("");
		}
		
		data.setFliePath(mPhotoPath);
		
		Intent it = new Intent();
		it.putExtra(BACK_DATA, data);
		return it;
	}
}
