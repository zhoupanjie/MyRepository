package com.cplatform.xhxw.ui.ui.main.saas.contribute;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.net.ContributeRequest;
import com.cplatform.xhxw.ui.model.CompanyAddImage;
import com.cplatform.xhxw.ui.model.CompanyImage;
import com.cplatform.xhxw.ui.plugin.gallery.PicShowActivity;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.ActionSheet;
import com.cplatform.xhxw.ui.ui.main.saas.EnterpriseMainFragment;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.CircleCreateType;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.CompanyCircleAlbumGroupActivity;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.SendImageTextActivity;
import com.cplatform.xhxw.ui.ui.main.saas.contribute.SelectedPhotosGridAdapter.onSelectedPhotoOperateListener;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.ImageCompressScaleUtil;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.MediaScanner;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.PhotoPickActivity;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.SelectTookenPhotoActivity;
import com.cplatform.xhxw.ui.ui.main.saas.photopick.entities.PhotoItem;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.EditUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.SendImageTextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 投稿
 */
public class ContributeActivity extends BaseActivity implements
		onSelectedPhotoOperateListener, OnClickListener {
	public static final int PHOTO_GRID_COLOUMNS = 3;

	private static final int REQUEST_CODE_PICK_PHOTO = 10001;
	private static final int REQUEST_CODE_TAKE_PHOTO = 10002;
	private static final int REQUEST_CODE_SELECT_PHOTO = 10003;

	private static final int TITLE_BYTE_COUNT = 27;

	private ImageView mTitleCommitBtn;
	private TextView mChannelTv;
	private ImageView mChannelDropDownIv;
	private EditText mTitleET;
	private EditText mShareLinkET;
	private GridView mPhotoGrid;
	private EditText mContentET;
	private Button mBottomCommitBtn;

	private SelectedPhotosGridAdapter mGridAdapter;
	private ArrayList<PhotoItem> mSelectedPhotos = new ArrayList<PhotoItem>();
	private static String mTmpFile;
	private String mTmpPhotoName;

	private String mCurrentChannelName;
	private String[] mAllChannelIds;
	private String[] mAllChannelNames;

	private JSONArray mSelectedPhotosWidth = new JSONArray();
	private JSONArray mSelectedPhotosHeight = new JSONArray();
	private ArrayList<PhotoItem> mCompressedPhotosToSend;

	private ProgressDialog mCommitProDialog;

	private Handler mHandl = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				Toast.makeText(ContributeActivity.this,
						R.string.contribute_commit_success, Toast.LENGTH_LONG)
						.show();
			} else if (msg.what == 1) {
				Toast.makeText(ContributeActivity.this,
						R.string.contribute_commit_fail, Toast.LENGTH_LONG)
						.show();
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
		setContentView(R.layout.activity_contribute);

		initViews();
		initData();
	}

	private void initViews() {
		View mBackBtn = findViewById(R.id.contribute_back_btn);
		mTitleCommitBtn = (ImageView) findViewById(R.id.contribute_commit_btn);
		mChannelTv = (TextView) findViewById(R.id.contribute_channel_switch_tv);
		mChannelDropDownIv = (ImageView) findViewById(R.id.contribute_channel_switch_iv);
		mTitleET = (EditText) findViewById(R.id.contribute_title_et);
		mShareLinkET = (EditText) findViewById(R.id.contribute_share_link_et);
		mPhotoGrid = (GridView) findViewById(R.id.contribute_photo_gv);
		mContentET = (EditText) findViewById(R.id.contribute_content_et);
		mBottomCommitBtn = (Button) findViewById(R.id.contribute_bottom_commit_btn);

		mBackBtn.setOnClickListener(this);
		mTitleCommitBtn.setOnClickListener(this);
		mChannelDropDownIv.setOnClickListener(this);
		mChannelTv.setOnClickListener(this);
		mBottomCommitBtn.setOnClickListener(this);
		mTitleET.addTextChangedListener(titleTextWatcher);

		mGridAdapter = new SelectedPhotosGridAdapter(this);
		mGridAdapter.setmMaxPhotoCount(6);
		mGridAdapter.setOnSelectedPhotoOper(this);
		mGridAdapter.setmPhotos(mSelectedPhotos, mPhotoGrid.getWidth());
		mPhotoGrid.setAdapter(mGridAdapter);
		initChannels();
	}

	private void initData() {
		mCurrentChannelName = getIntent().getStringExtra("channel_name");
		if (mCurrentChannelName.length() < 1) {
			if (mAllChannelIds.length > 0) {
				mChannelTv.setText(mAllChannelNames[0]);
			}
		} else {
			mChannelTv.setText(mCurrentChannelName);
		}
	}

	@Override
	public void onAddPhotoClick() {
		mSelectedPhotos.clear();
		mSelectedPhotos.addAll(mGridAdapter.getmPhotos());
		mSelectedPhotos.remove(mSelectedPhotos.size() - 1);
		onAddPhotoBtnClick();
	}

	@Override
	public void onRemovePhoto() {
		resizeGridHeight();
	}

	/**
	 * 标题字数限定监听
	 */
	TextWatcher titleTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String content = s.toString();
			try {
				if (content.getBytes("GBK").length > TITLE_BYTE_COUNT) {
					showToast(R.string.contribue_out_of_length);
					if (content.length() > TITLE_BYTE_COUNT) {
						content = content.substring(0, TITLE_BYTE_COUNT - 1);
					}
					while (content.toString().getBytes("GBK").length > TITLE_BYTE_COUNT) {
						content = content.substring(0, content.length() - 1);
					}
					mTitleET.setText(content);
					mTitleET.setSelection(content.length());
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	};

	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// // EditUtil.hideMethod(this, mEditText);
	// if (mGridAdapter.getItem(position) instanceof CompanyAddImage) {
	// // 添加新的图片
	// showPhotoActionSheet(SendImageTextActivity.this);
	// return;
	// }
	//
	// List<String> imgs = new ArrayList<String>();
	// // for (CompanyImage img : imageList) {
	// // if (imgs instanceof CompanyAddImage) {
	// // continue;
	// // }
	// // imgs.add(img.getFileUrl());
	// // }
	//
	// for (int i = 0; i < mAdapter.getData().size(); i++) {
	// if (mAdapter.getData().get(i) instanceof CompanyAddImage) {
	// continue;
	// }
	// imgs.add(mAdapter.getData().get(i).getFileUrl());
	// }
	// startActivityForResult(PicShowActivity.newIntent(this, imgs, position,
	// true), REQUEST_CODE_PREVIEW);
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.i("-------", "requestCode-----" + requestCode
				+ " --resultCode----" + resultCode);
		if (resultCode == RESULT_OK) {
			if (requestCode == EnterpriseMainFragment.REQUEST_CODE_COMPANT_ALBUMGROUP) {

				/** 相册传过来的数据 */
				if (data != null) {
					List<String> result = (List<String>) data
							.getSerializableExtra("result");
					if (!ListUtil.isEmpty(result)) {
						int size = result.size();
						for (int i = 0; i < size; i++) {
							PhotoItem item = new PhotoItem(null, null,
									result.get(i));
							mSelectedPhotos.add(item);
						}
						result.clear();
					}
				}
				// mSelectedPhotos = data.getParcelableArrayListExtra(
				// PhotoPickActivity.PHOTO_PICK_PARAM_KEY);
				mGridAdapter.setmPhotos(mSelectedPhotos, mPhotoGrid.getWidth());
				resizeGridHeight();
			} else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
				// 对拍摄的照片调用系统媒体扫描服务，使系统将此照片添加到系统多媒体数据库
				MediaScanner.getInstance(this).scanFile(mTmpFile, "media/jpg");
				Intent it = new Intent(this, SelectTookenPhotoActivity.class);
				it.putExtra(SelectTookenPhotoActivity.PHOTO_PATH, mTmpFile);
				startActivityForResult(it, REQUEST_CODE_SELECT_PHOTO);

			} else if (requestCode == REQUEST_CODE_SELECT_PHOTO) {
				PhotoItem photo = data
						.getParcelableExtra(SelectTookenPhotoActivity.BACK_DATA);
				mGridAdapter.insertAPhoto(photo);

			} else if (requestCode == 11) {
				/** 相册传过来的数据 */
				mSelectedPhotos.clear();
				if (data != null) {
					List<String> result = (List<String>) data
							.getSerializableExtra("result");
					if (!ListUtil.isEmpty(result)) {
						int size = result.size();
						for (int i = 0; i < size; i++) {
							PhotoItem item = new PhotoItem(null, null,
									result.get(i));
							mSelectedPhotos.add(item);
						}
						result.clear();
					}
				}
				mGridAdapter.setmPhotos(mSelectedPhotos, mPhotoGrid.getWidth());
				resizeGridHeight();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 根据选中照片数量重置GridView高度
	 */
	private void resizeGridHeight() {
		if (mGridAdapter.getmPhotos().size() > PHOTO_GRID_COLOUMNS) {
			mPhotoGrid.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, (mPhotoGrid.getWidth() - 10)
							/ PHOTO_GRID_COLOUMNS * 2 + 20));
		} else {
			mPhotoGrid.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, (mPhotoGrid.getWidth() - 10)
							/ PHOTO_GRID_COLOUMNS));
		}
		mPhotoGrid.setVerticalSpacing(10);
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
		builder.appendMenuItem(getString(R.string.choose_from_a_local_album),
				null, new ActionSheet.GPopupMenuListener() {
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
		// startActivityForResult(PhotoPickActivity.getPhotoPickIntent(this,
		// mSelectedPhotos, 6), REQUEST_CODE_PICK_PHOTO);
		startActivityForResult(
				CompanyCircleAlbumGroupActivity.newIntent(this, null),
				EnterpriseMainFragment.REQUEST_CODE_COMPANT_ALBUMGROUP);
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
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.contribute_back_btn:
			backToPrePage();
			break;
		case R.id.contribute_bottom_commit_btn:
		case R.id.contribute_commit_btn:
			if (isNecessaryFieldEmpty()) {
				showToast(R.string.contribute_content_empty_alert);
				return;
			}
			// show commit verify dialog
			new AlertDialog.Builder(this)
					.setTitle(R.string.commit_dialog_title)
					.setMessage(R.string.contribute_commite_verify)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mCommitProDialog = ProgressDialog
											.show(ContributeActivity.this,
													getString(R.string.commit_dialog_title),
													getString(R.string.commit_dialog_msg));
									commitContent();
								}
							}).setNegativeButton(R.string.cancel, null).show();
			break;
		case R.id.contribute_channel_switch_tv:
		case R.id.contribute_channel_switch_iv:
			onChannelSwiticherClick();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToPrePage();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 提交投稿
	 */
	private void commitContent() {
		new Thread() {

			@Override
			public void run() {
				ContributeRequest request = composeCommitRequest();

				APIClient.commitContributeContent(request,
						new AsyncHttpResponseHandler() {

							@Override
							public void onFinish() {
								mCommitProDialog.dismiss();
								deleteTmpedPhotos(mCompressedPhotosToSend);
							}

							@Override
							public void onSuccess(int statusCode, String content) {
								mHandl.sendEmptyMessage(0);
								finish();
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								mHandl.sendEmptyMessage(1);
							}
						});
			}

		}.start();
	}

	/**
	 * 生成请求参数
	 * 
	 * @return
	 */
	private ContributeRequest composeCommitRequest() {
		ContributeRequest request = new ContributeRequest();
		request.setChannelid(getChannelIdByname());
		request.setTitle(mTitleET.getText().toString().trim());
		request.setSaasRequest(true);
		request.setDescription(mContentET.getText().toString().trim());

		HashMap<String, File> pics = new HashMap<String, File>();
		mCompressedPhotosToSend = compressSelectedPhotos();
		int photoCount = mCompressedPhotosToSend.size();
		JSONArray fileNames = new JSONArray();
		for (int i = 0; i < photoCount; i++) {
			PhotoItem pho = mCompressedPhotosToSend.get(i);
			if (pho.getFliePath() == null)
				continue;
			String path = pho.getFliePath();
			String[] peices = path.split(File.separator);
			fileNames.put(peices[peices.length - 1]);
			pics.put("file" + i, new File(path));
		}
		request.setPicsToUpload(pics);
		request.setFileName(fileNames.toString());
		request.setWidth(mSelectedPhotosWidth.toString());
		request.setHeight(mSelectedPhotosHeight.toString());
		request.setFileCount(mCompressedPhotosToSend.size());

		return request;
	}

	/**
	 * 压缩图片资源
	 * 
	 * @return
	 */
	private ArrayList<PhotoItem> compressSelectedPhotos() {
		ArrayList<PhotoItem> result = new ArrayList<PhotoItem>();
		mSelectedPhotos = mGridAdapter.getmPhotos();
		clearJsonArray(mSelectedPhotosWidth);
		clearJsonArray(mSelectedPhotosHeight);
		for (PhotoItem photo : mSelectedPhotos) {
			String path = photo.getFliePath();
			if (path == null)
				continue;
			Bitmap compressedBit = ImageCompressScaleUtil
					.getCompressedImage(path);
			mSelectedPhotosWidth.put("" + compressedBit.getWidth());
			mSelectedPhotosHeight.put("" + compressedBit.getHeight());

			PhotoItem item = new PhotoItem(photo.getImageId(),
					photo.getThumbPath(), photo.getFliePath());
			String exten = path.substring(path.lastIndexOf("."));
			String tmpPath = Constants.Directorys.TEMP + new Date().getTime()
					+ exten;
			if (ImageCompressScaleUtil.saveFile(compressedBit, tmpPath)) {
				item.setFliePath(tmpPath);
			}
			result.add(item);
		}

		return result;
	}

	/**
	 * 清空JsonArray内容
	 * 
	 * @param ja
	 */
	private void clearJsonArray(JSONArray ja) {
		ja = null;
		ja = new JSONArray();
	}

	/**
	 * 删除暂存的压缩图片
	 * 
	 * @param data
	 */
	private void deleteTmpedPhotos(ArrayList<PhotoItem> data) {
		for (PhotoItem photo : data) {
			File f = new File(photo.getFliePath());
			if (f.exists()) {
				f.delete();
			}
		}
	}

	/**
	 * 是否修改了页面内容
	 * 
	 * @return
	 */
	private boolean isContentChanged() {
		return mTitleET.getText().toString().trim().length() > 0
				|| mShareLinkET.getText().toString().trim().length() > 0
				|| mGridAdapter.getmPhotos().size() > 1
				|| mContentET.getText().toString().trim().length() > 0;
	}

	/**
	 * 是否有必填项漏填
	 * 
	 * @return
	 */
	private boolean isNecessaryFieldEmpty() {
		return mTitleET.getText().toString().trim().length() == 0
				|| mContentET.getText().toString().trim().length() == 0;
	}

	/**
	 * 返回上一界面
	 */
	private void backToPrePage() {
		if (isContentChanged()) {
			new AlertDialog.Builder(this)
					.setTitle(null)
					.setMessage(R.string.cancel_alert)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).setNegativeButton(R.string.cancel, null).show();
		} else {
			finish();
		}
	}

	/**
	 * 频道选择
	 */
	public void onChannelSwiticherClick() {
		if (CommonUtils.isFastDoubleClick())
			return;

		int selectedItem = 0;
		for (int i = 0; i < mAllChannelNames.length; i++) {
			if (mAllChannelNames[i].equals(mCurrentChannelName)) {
				selectedItem = i;
				break;
			}
		}

		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.contribute_choose_channel))
				.setSingleChoiceItems(mAllChannelNames, selectedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								mCurrentChannelName = mAllChannelNames[which];
								mChannelTv.setText(mCurrentChannelName);
								dialog.dismiss();
							}

						}).setNegativeButton(R.string.cancel, null).show();
	}

	private void initChannels() {
		List<ChanneDao> chnnes = ChanneDB.getChannelsByEnterpriseId(this,
				Constants.getEnterpriseId());
		mAllChannelIds = new String[chnnes.size()];
		mAllChannelNames = new String[chnnes.size()];
		for (int i = 0; i < chnnes.size(); i++) {
			ChanneDao channel = chnnes.get(i);
			mAllChannelIds[i] = channel.getChannelID();
			mAllChannelNames[i] = channel.getChannelName();
		}
	}

	private String getChannelIdByname() {
		for (int i = 0; i < mAllChannelNames.length; i++) {
			if (mAllChannelNames[i].equals(mCurrentChannelName)) {
				return mAllChannelIds[i];
			}
		}
		return null;
	}
}
