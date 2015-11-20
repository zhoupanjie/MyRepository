package com.cplatform.xhxw.ui.ui.main.saas.photopick;
import java.util.ArrayList;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.adapter.PhotoAdappter;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.adapter.PhotoAdappter.onSelectPhotoListener;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.entities.PhotoItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jinyz
 *
 * 照片选取页面<br>
 * 调用方法：<br>
 * 		##通过上一activity的startActivityForResult调用，可通过intent传入已选取的照片数组，
 * 		     格式为ArrayList<PhotoItem>, 参数获取key为"photos"<br>
 * 		##其它调用方式在后续需求中添加<br>
 * 
 * 数据返回：<br>
 * 		##在发起调用的页面的onActivityResult回调中获取选取的照片数组，数组格式为ArrayList<PhotoItem>
 * 		     获取方式为getExtras().getParcelableArrayList(PHOTO_PICK_PARAM_KEY)<br>
 * 
 * PhotoItem：单张照片属性类
 */
public class PhotoPickActivity extends Activity implements onSelectPhotoListener {
	public static final String PHOTO_PICK_PARAM_KEY = "photos";
	public static final String PHOTO_PICK_MAX_COUNT = "max_photos_count";
	private static final int MSG_PHOTO_INIT_DONE = 1;
	
	public static Intent getPhotoPickIntent(Context con, ArrayList<PhotoItem> selectedPhotos, 
			int maxPicsToPick) {
		Intent it = new Intent();
		it.setClass(con, PhotoPickActivity.class);
		it.putExtra(PHOTO_PICK_MAX_COUNT, maxPicsToPick);
		it.putParcelableArrayListExtra(PHOTO_PICK_PARAM_KEY, selectedPhotos);
		return it;
	}
	
	private GridView mGridView;
	private PhotoAdappter mAdapter;
	private TextView mSelectHintTv;
	private ImageView mBackBtn;
	private Button mDoneBtn;
	private ArrayList<PhotoItem> photos = new ArrayList<PhotoItem>();
	
	private ProgressDialog mProgressDialog;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_PHOTO_INIT_DONE:
				ArrayList<PhotoItem> selectedPhotoList = getIntent()
						.getExtras().getParcelableArrayList(PHOTO_PICK_PARAM_KEY);
				mAdapter.setSelectedPhotos(selectedPhotoList);
				mAdapter.notifyDataSetChanged();
				upgradePhotoSelectInfo();
				mProgressDialog.dismiss();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photoalbum_gridview);
		mSelectHintTv = (TextView)findViewById(R.id.photo_pick_tv);
		mBackBtn = (ImageView) findViewById(R.id.photo_pick_back);
		mDoneBtn = (Button) findViewById(R.id.photo_pick_done_btn);
		mGridView =(GridView)findViewById(R.id.photo_gridview);
		mAdapter = new PhotoAdappter(this, photos);
		mAdapter.setmMaxPicPhotos(getIntent().getIntExtra(PHOTO_PICK_MAX_COUNT, 6));
		mAdapter.setOnSelectLis(this);
		mGridView.setAdapter(mAdapter);
		
		mBackBtn.setOnClickListener(mOnClickListener);
		mDoneBtn.setOnClickListener(mOnClickListener);
		
		upgradePhotoSelectInfo();
		
		initPhotos();
	}
	
	OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.photo_pick_back:
				finish();
				break;
			case R.id.photo_pick_done_btn:
				Intent it = new Intent();
				it.putParcelableArrayListExtra(PHOTO_PICK_PARAM_KEY, mAdapter.getSelectedPhotos());
				setResult(RESULT_OK, it);
				finish();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 初始化所有照片
	 */
	private void initPhotos() {
		mProgressDialog = ProgressDialog.show(this, null, "请稍候...");
		new Thread() {

			@Override
			public void run() {
				photos.addAll(PhotoManagerUtil.getAllPhotosThumb(PhotoPickActivity.this));
				mHandler.sendEmptyMessage(MSG_PHOTO_INIT_DONE);
			}
		}.start();
	}

	/**
	 * 照片选择回调，用户更新选取数量
	 */
	@Override
	public void onPhotoSelected(int selectedCount) {
		upgradePhotoSelectInfo();
	}
	
	private void upgradePhotoSelectInfo() {
		mSelectHintTv.setText(mAdapter.getSelectedPhotos().size() + "/" + mAdapter.getmMaxPicPhotos());
	}
}
