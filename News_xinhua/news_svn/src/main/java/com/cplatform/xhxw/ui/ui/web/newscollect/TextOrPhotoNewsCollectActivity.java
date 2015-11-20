package com.cplatform.xhxw.ui.ui.web.newscollect;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.NewsCollectUploadRequest;
import com.cplatform.xhxw.ui.http.net.NewsCollectUploadResponse;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.ActionSheet;
import com.cplatform.xhxw.ui.ui.main.saas.contribute.SelectedPhotosGridAdapter;
import com.cplatform.xhxw.ui.ui.main.saas.contribute.SelectedPhotosGridAdapter.onSelectedPhotoOperateListener;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.ImageCompressScaleUtil;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.MediaScanner;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.PhotoPickActivity;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.SelectTookenPhotoActivity;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.entities.PhotoItem;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class TextOrPhotoNewsCollectActivity extends BaseActivity implements onSelectedPhotoOperateListener {
	private static final String COLLECT_ID = "news_id";
	private static final String COLLECT_TYPE = "collect_type";
	private static final String COLLECT_STRLEN = "str_len";
	
	private static final int REQUEST_CODE_PICK_PHOTO = 10001;
	private static final int REQUEST_CODE_TAKE_PHOTO = 10002;
	private static final int REQUEST_CODE_SELECT_PHOTO = 10003;
	
	private static final int UPLOAD_WAITING_TIME = 15 * 1000;
	
	private int mNewsId;
	private int mCollectType;
	private int mStrMaxLen;
	
	private ProgressDialog mSubmitPd;
	private EditText mContentEt;
	private GridView mPhotoGv;
	private Button mSubmitBtn;
	private TextView mTextCountTv;
	
	private SelectedPhotosGridAdapter mGridAdapter;
	private ArrayList<PhotoItem> mSelectedPhotos = new ArrayList<PhotoItem>();
	private static String mTmpFile;
	private String mTmpPhotoName;
	private ArrayList<PhotoItem> mCompressedPhotosToSend;
	
	public static Intent getIntent(Context context, int levyId, int levyType, int strLen) {
		Intent intent = new Intent(context, TextOrPhotoNewsCollectActivity.class);
		intent.putExtra(COLLECT_ID, levyId);
		intent.putExtra(COLLECT_TYPE, levyType);
		intent.putExtra(COLLECT_STRLEN, strLen);
		return intent;
	}

	private Handler mHandl = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mSubmitPd.dismiss();
			if(msg.what == 0) {
				showToast(R.string.sign_news_success);
				finish();
			} else if(msg.what == 1) {
				showToast(R.string.contribute_commit_fail);
			} else if(msg.what == 2) {
				showToast(msg.getData().getString("msg"));
			} else if(msg.what == 3) {
				if(mSubmitPd != null && mSubmitPd.isShowing()) {
					mSubmitPd.dismiss();
				}
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected String getScreenName() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_textorphoto_collect);
		initViews();
	}

	private void initViews() {
		mNewsId = getIntent().getIntExtra(COLLECT_ID, 0);
		mCollectType = getIntent().getIntExtra(COLLECT_TYPE, NewsCollectUploadRequest.COLLECT_TYPE_TEXT);
		mStrMaxLen = getIntent().getIntExtra(COLLECT_STRLEN, 300);
		mContentEt = (EditText) findViewById(R.id.text_photo_collect_et);
		mPhotoGv = (GridView) findViewById(R.id.text_photo_collect_gridview);
		mSubmitBtn = (Button) findViewById(R.id.text_photo_collect_commit_btn);
		Button backBtn = (Button) findViewById(R.id.btn_back);
		mSubmitBtn.setOnClickListener(mOnclick);
		backBtn.setOnClickListener(mOnclick);
		mContentEt.addTextChangedListener(titleTextWatcher);
		if(mCollectType == NewsCollectUploadRequest.COLLECT_TYPE_TEXT) {
			mPhotoGv.setVisibility(View.GONE);
		} else {
			mPhotoGv.setVisibility(View.VISIBLE);
		}
		mTextCountTv = (TextView) findViewById(R.id.text_photo_collect_text_count_tv);
		mTextCountTv.setText("0/" + mStrMaxLen);
		
		mGridAdapter = new SelectedPhotosGridAdapter(this);
		mGridAdapter.setmMaxPhotoCount(3);
		mGridAdapter.setOnSelectedPhotoOper(this);
		mGridAdapter.setmPhotos(mSelectedPhotos, mPhotoGv.getWidth());
		mPhotoGv.setAdapter(mGridAdapter);
		
	}
	
	View.OnClickListener mOnclick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.text_photo_collect_commit_btn) {
				checkAndCommit();
			} else if(v.getId() == R.id.btn_back) {
				backToPrePage();
			}
		}
	};
	
	/**
	 * 内容字数限定监听
	 */
	TextWatcher titleTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String content = s.toString();
			try {
				if(content.trim().length() > mStrMaxLen) {
					showToast(R.string.contribue_out_of_length);
					if(content.trim().length() > mStrMaxLen) {
						content = content.substring(0, mStrMaxLen);
					}
					while(content.toString().trim().length() > mStrMaxLen) {
						content = content.substring(0, content.length());
					}
					mContentEt.setText(content);
					mContentEt.setSelection(content.length());
				}
				mTextCountTv.setText((content.trim().length() > 300 ? 300 : content.trim().length())
						+ "/" + mStrMaxLen);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onAddPhotoClick() {
		mSelectedPhotos.clear();
		mSelectedPhotos.addAll(mGridAdapter.getmPhotos());
		mSelectedPhotos.remove(mSelectedPhotos.size() - 1);
		onAddPhotoBtnClick();
	}

	@Override
	public void onRemovePhoto() {
		mSelectedPhotos.clear();
		mSelectedPhotos.addAll(mGridAdapter.getmPhotos());
		mSelectedPhotos.remove(mSelectedPhotos.size() - 1);
	}

	/**
	 * 添加照片按钮点击事件
	 */
	public void onAddPhotoBtnClick() {
        ActionSheet.Builder builder = new ActionSheet.Builder(this);
        builder.appendMenuItem(getString(R.string.photograph), null,
                new ActionSheet.GPopupMenuListener() {
                    @Override
                    public void onMenuSelected(ActionSheet.MenuItem item) {
                        takePhoto();
                    }
                });
        builder.appendMenuItem(getString(R.string.choose_from_a_local_album), null,
                new ActionSheet.GPopupMenuListener() {
                    @Override
                    public void onMenuSelected(ActionSheet.MenuItem item) {
                        pickPhoto();
                    }
                });
        builder.show();
    }
	
	/**
	 * 从相册选取照片
	 */
	protected void pickPhoto() {
		startActivityForResult(PhotoPickActivity.getPhotoPickIntent(this, mSelectedPhotos, 3), REQUEST_CODE_PICK_PHOTO);
	}

	/**
	 * 拍照
	 */
	protected void takePhoto() {
		String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                Intent intent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                mTmpPhotoName = new Date().getTime() + ".jpg";
                mTmpFile = Constants.Directorys.TAKE_PHOTO + mTmpPhotoName;
                // localTempImgDir和localTempImageFileName是自己定义的名字
                Uri u = Uri.fromFile(new File(mTmpFile));
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
            } catch (ActivityNotFoundException e) {
            }
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.i("-------", "requestCode-----" + requestCode + " --resultCode----" +
					resultCode);
		if(resultCode == RESULT_OK) {
			if(requestCode == REQUEST_CODE_PICK_PHOTO) {
				mSelectedPhotos = data.getParcelableArrayListExtra(
						PhotoPickActivity.PHOTO_PICK_PARAM_KEY);
				mGridAdapter.setmPhotos(mSelectedPhotos, mPhotoGv.getWidth());
				
			} else if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
				//对拍摄的照片调用系统媒体扫描服务，使系统将此照片添加到系统多媒体数据库
				MediaScanner.getInstance(this).scanFile(mTmpFile, "media/jpg");
				Intent it = new Intent(this, SelectTookenPhotoActivity.class);
				it.putExtra(SelectTookenPhotoActivity.PHOTO_PATH, mTmpFile);
				startActivityForResult(it, REQUEST_CODE_SELECT_PHOTO);
				
			} else if(requestCode == REQUEST_CODE_SELECT_PHOTO) {
				PhotoItem photo = data.getParcelableExtra(SelectTookenPhotoActivity.BACK_DATA);
				mGridAdapter.insertAPhoto(photo);
				mSelectedPhotos.add(photo);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 返回上一界面
	 */
	private void backToPrePage() {
		if(isContentChanged()) {
			new AlertDialog.Builder(this).setTitle(null)
				.setMessage(R.string.cancel_alert)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setNegativeButton(R.string.cancel, null).show();
		} else {
			finish();
		}
	}
	
	/**
	 * 是否修改了页面内容
	 * @return
	 */
	private boolean isContentChanged() {
		return mContentEt.getText().toString().trim().length() > 0 ||
				mGridAdapter.getmPhotos().size() > 1;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			backToPrePage();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	private void checkAndCommit() {
		if(mContentEt.getText().toString().length() <= 0) {
			showToast(R.string.contribute_content_empty_alert);
			return;
		}
		
		if(mCollectType == NewsCollectUploadRequest.COLLECT_TYPE_PHOTO 
				&& mSelectedPhotos.size() < 1) {
			showToast(R.string.news_collect_no_photo);
			return;
		}
		
		//show commit verify dialog
		new AlertDialog.Builder(this).setTitle(R.string.contribute_title)
			.setMessage(R.string.contribute_commite_verify)
			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mSubmitPd = ProgressDialog.show(TextOrPhotoNewsCollectActivity.this, 
							getString(R.string.contribute_title), 
							getString(R.string.uploading));
					commitContent();
				}
			})
			.setNegativeButton(R.string.cancel, null).show();
	}
	
	private void commitContent() {
		mHandl.sendEmptyMessageDelayed(3, UPLOAD_WAITING_TIME);
		new Thread() {

			@Override
			public void run() {
				NewsCollectUploadRequest request = composeCommitRequest();
				
				APIClient.submitCollectContent(request, new AsyncHttpResponseHandler(){

					@Override
					public void onFinish() {
						mSubmitPd.dismiss();
						deleteTmpedPhotos(mCompressedPhotosToSend);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						mSubmitPd.dismiss();
						NewsCollectUploadResponse response = null;
						try {
							response = new Gson().fromJson(content, NewsCollectUploadResponse.class);
							ResponseUtil.checkObjResponse(response);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(response == null) {
							showToast(R.string.submit_fail);
							return;
						}
						if(response.isSuccess()) {
							mHandl.sendEmptyMessage(0);
						} else {
							Message msg = new Message();
							msg.what = 2;
							Bundle data = new Bundle();
							data.putString("msg", response.getMsg());
							msg.setData(data);
							mHandl.sendMessage(msg);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						mSubmitPd.dismiss();
						mHandl.sendEmptyMessage(1);
					}
				});
			}
		}.start();
	}
	
	private NewsCollectUploadRequest composeCommitRequest() {
		NewsCollectUploadRequest request = new NewsCollectUploadRequest();
		if(mSelectedPhotos.size() < 1) {
			request.setType(NewsCollectUploadRequest.COLLECT_TYPE_TEXT);
		} else {
			request.setType(NewsCollectUploadRequest.COLLECT_TYPE_PHOTO);
			
			HashMap<String, File> pics = new HashMap<String, File>();
			mCompressedPhotosToSend = compressSelectedPhotos();
			int photoCount = mCompressedPhotosToSend.size();
			for(int i = 0; i < photoCount; i++) {
				PhotoItem pho = mCompressedPhotosToSend.get(i);
				if(pho.getFliePath() == null) continue;
				String path = pho.getFliePath();
				pics.put("pics[" + i + "]", new File(path));
			}
			request.setPics(pics);
		}
		
		request.setContent(mContentEt.getText().toString().trim());
		request.setListid(mNewsId);
		
		return request;
	}
	
	/**
	 * 压缩图片资源
	 * @return
	 */
	private ArrayList<PhotoItem> compressSelectedPhotos() {
		ArrayList<PhotoItem> result = new ArrayList<PhotoItem>();
		mSelectedPhotos = mGridAdapter.getmPhotos();
		for(PhotoItem photo : mSelectedPhotos) {
			String path = photo.getFliePath();
			if(path == null) continue;
			Bitmap compressedBit = ImageCompressScaleUtil.getCompressedImage(path);
			
			PhotoItem item = new PhotoItem(photo.getImageId(), photo.getThumbPath(), photo.getFliePath());
			String exten = path.substring(path.lastIndexOf("."));
			String tmpPath = Constants.Directorys.TEMP + new Date().getTime() + exten;
			if(ImageCompressScaleUtil.saveFile(compressedBit, tmpPath)) {
				item.setFliePath(tmpPath);
			}
			result.add(item);
		}
		return result;
	}
	
	/**
	 * 删除暂存的压缩图片
	 * @param data
	 */
	private void deleteTmpedPhotos(ArrayList<PhotoItem> data) {
		for(PhotoItem photo : data) {
			File f = new File(photo.getFliePath());
			if(f.exists()) {
				f.delete();
			}
		}
	}
}
