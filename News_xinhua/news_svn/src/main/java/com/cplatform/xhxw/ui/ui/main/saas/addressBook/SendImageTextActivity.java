package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.model.CompanyAddImage;
import com.cplatform.xhxw.ui.model.CompanyImage;
import com.cplatform.xhxw.ui.model.UserInfo;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneItemExrta;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneItemUserInfo;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneList;
import com.cplatform.xhxw.ui.plugin.gallery.PicShowActivity;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.adapter.SendImageTextAdapter;
import com.cplatform.xhxw.ui.ui.base.view.FriendsActionSheet;
import com.cplatform.xhxw.ui.ui.base.view.NoScrollGridView;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionManager;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt;
import com.cplatform.xhxw.ui.util.AppCoomentUtil;
import com.cplatform.xhxw.ui.util.EditUtil;
import com.cplatform.xhxw.ui.util.FileUtil;
import com.cplatform.xhxw.ui.util.ImageUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.SendImageTextUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

/**
 * 社区 发布新鲜事（图文）
 */
public class SendImageTextActivity extends BaseActivity implements
		OnClickListener,
		InputViewSensitiveLinearLayout.OnInputViewListener,
		XWExpressionWidgt.onExprItemClickListener, OnItemClickListener, OnItemLongClickListener {

	private static final String TAG = SendImageTextActivity.class.getSimpleName();
	private static final int REQUEST_CODE_ALBUMGROUP = 1;
	private static final int REQUEST_CODE_CAMERA = 2;
    private static final int REQUEST_CODE_PREVIEW = 3;

	private View backBtn;
	private View sendBtn;
	private EditText mEditText;
	private Button faceBtn;

	private RadioButton companyRadio;
	private RadioButton friendRadio;
	private XWExpressionWidgt mExpressionWidgt;

	private InputViewSensitiveLinearLayout mRootContainer;

	private boolean mIsExprShow = false;
	private boolean mIsSoftKeyboardShow = false;
	private InputMethodManager mImm;
	private Handler mUiHandler = new Handler();
	private int mSoftKeyboardHeight = 0;
	private int mOriginalHeight = 0;
	private int mRootContainerBottom = 0;
	private boolean isExprAreaResized = false;
//	private List<String> selectList;
//	private List<CompanyImage> imageList;

	private NoScrollGridView mGridView;
	private SendImageTextAdapter mAdapter;

	private AsyncHttpResponseHandler mHttpResponseHandler;
	
	/** 图片处理 */
	private Uri mPhotoUri;
	
	/** 存放选中的图片文件的 路径 */
	private String filephoto = Constants.Directorys.TAKE_PHOTO + "filephoto.jpg";
	private String filephotos = Constants.Directorys.TAKE_PHOTO + "filephotos";
	
	public static Intent newIntent(Context context, List<String> list, CircleCreateType type) {
		Intent intent = new Intent(context, SendImageTextActivity.class);
		intent.putExtra("images", (Serializable) list);
        int createType = (type == CircleCreateType.friend) ? 1 : 2;
		intent.putExtra("createType", createType);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_send_image_text);

		init();

		setListener();

		initView();
		
		fileClear(filephotos);
	}

	private void init() {
		initActionBar();

		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		backBtn = findViewById(R.id.btn_back);
		sendBtn = findViewById(R.id.company_circle_send);
		faceBtn = (Button) findViewById(R.id.company_circle_face);
		mEditText = (EditText) findViewById(R.id.company_circle_edit);
		companyRadio = (RadioButton) findViewById(R.id.company_circle_radiao);
		friendRadio = (RadioButton) findViewById(R.id.friend_circle_radiao);
		mExpressionWidgt = (XWExpressionWidgt) findViewById(R.id.comment_expression_widgt);
		mRootContainer = (InputViewSensitiveLinearLayout) findViewById(R.id.layout_content);

		mGridView = (NoScrollGridView) findViewById(R.id.company_circle_gridview);
		mAdapter = new SendImageTextAdapter(this);
		mGridView.setAdapter(mAdapter);

		mSoftKeyboardHeight = PreferencesManager.getSoftKeyboardHeight(this);
        if (Constants.hasLogin() && Constants.userInfo != null && Constants.userInfo.getEnterprise() != null && Constants.userInfo.getEnterprise().getAliases() != null) {
            UserInfo.EnterpriseAlias alias = Constants.userInfo.getEnterprise().getAliases();
            companyRadio.setText(alias.getC_community_alias());
            friendRadio.setText(alias.getF_community_alias());
        }
	}

	private void setListener() {
		backBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		faceBtn.setOnClickListener(this);
		mExpressionWidgt.setmOnExprItemClickListener(this);
		mRootContainer.setOnInputViewListener(this);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);

		mEditText.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (mIsExprShow) {
						mIsExprShow = false;
						mIsSoftKeyboardShow = true;
						mExpressionWidgt.setVisibility(View.GONE);
						faceBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
					}
				}
				return false;
			}
		});
        int createType = getIntent().getIntExtra("createType", 1);
        if (createType == 1) {
            friendRadio.setChecked(true);
        } else {
            companyRadio.setChecked(true);
        }
	}

	private void initView() {
		faceBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);

		/**
		 * 测量软键盘所占区域高度
		 */
		mRootContainer.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						mRootContainer.getWindowVisibleDisplayFrame(r);
						int screenHeight = mRootContainer.getHeight();
						final int screenWidth = mRootContainer.getWidth();
						mRootContainerBottom = screenHeight;
						if (mOriginalHeight == 0) {
							mOriginalHeight = screenHeight;
						} else {
							if (screenHeight != mOriginalHeight
									&& !isExprAreaResized) {
								mSoftKeyboardHeight = Math.abs(screenHeight
										- mOriginalHeight);
                                PreferencesManager.saveSoftKeyboardHeight(SendImageTextActivity.this, mSoftKeyboardHeight);
								isExprAreaResized = true;
								mUiHandler.post(new Runnable() {
									@Override
									public void run() {
										resizeExprArea(screenWidth);
									}
								});
							}
						}
					}
				});

		if (mSoftKeyboardHeight > 0) {
			resizeExprArea(Constants.screenWidth);
		}

		mSoftKeyboardHeight = 300;
		resizeExprArea(Constants.screenWidth);

		List<String> selectList = getIntent().getStringArrayListExtra("images");
		
//		if (getIntent().getStringExtra("imagePath") != null && !TextUtils.isEmpty(getIntent().getStringExtra("imagePath"))) {
//			CompanyImage companyImage = new CompanyImage();
//			companyImage.setFileUrl(getIntent().getStringExtra("imagePath"));
//			imageList.add(companyImage);
//		}
		if (selectList != null && selectList.size() > 0) {
			for (int i = 0; i < selectList.size(); i++) {
				CompanyImage companyImage = new CompanyImage();
				companyImage.setFileUrl(selectList.get(i));
				mAdapter.getData().add(companyImage);
			}
		}
		if (mAdapter.getData().size() < SendImageTextUtil.COUNT) {
			mAdapter.getData().add(new CompanyAddImage());
		}

		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			showExitDialog();
			break;
		case R.id.company_circle_send:
			sendCompanyCircle();
			break;
		case R.id.company_circle_face:
			if (mIsExprShow) {
				controlKeyboardOrExpr(true, true);
			} else {
				controlKeyboardOrExpr(false, true);
			}
			break;
		default:
			break;
		}
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EditUtil.hideMethod(this, mEditText);
        if (mAdapter.getItem(position) instanceof CompanyAddImage) {
            // 添加新的图片
            showPhotoActionSheet(SendImageTextActivity.this);
            return;
        }
        
        List<String> imgs = new ArrayList<String>();
//        for (CompanyImage img : imageList) {
//            if (imgs instanceof CompanyAddImage) {
//                continue;
//            }
//            imgs.add(img.getFileUrl());
//        }
        
        for (int i = 0; i < mAdapter.getData().size(); i++) {
        	 if (mAdapter.getData().get(i) instanceof CompanyAddImage) {
               continue;
           }
           imgs.add(mAdapter.getData().get(i).getFileUrl());
		}
        startActivityForResult(PicShowActivity.newIntent(this, imgs, position, true), REQUEST_CODE_PREVIEW);
    }

    /** 发布新鲜事 */
	private void sendCompanyCircle() {

		sendBtn.setClickable(false);
		EditUtil.hideMethod(this, mEditText);
		
		fileClear(filephotos);
		
		if (mAdapter.getCount() == 1 && mAdapter.getItem(0) instanceof CompanyAddImage) {
			showToast("请选择发布的图片");
			sendBtn.setClickable(true);
			return;
		}
		
		showLoadingView("正在发布...");
		if (getMapImages(filephotos).size() == 0) {
			/**把图片依次copy出来，把这张图片压缩，放在一个新的文件夹里面 */
			for (int i = 0; i < mAdapter.getCount(); i++) {
//				copyImage(mAdapter.getItem(i).getFileUrl(), Constants.Directorys.TAKE_PHOTO);
				if (!(mAdapter.getItem(i) instanceof CompanyAddImage)) {
					copyImageTofile(mAdapter.getItem(i).getFileUrl(), filephoto);
					try {
						ImageUtil.getimage(SendImageTextActivity.this, filephoto, createFiles(filephotos));
					} catch (Exception e) {
					
					}
	    		}
			}
		}
		
		SendCompanyCircleRequest request = new SendCompanyCircleRequest();
		if (companyRadio.isChecked()) {// 1朋友圈，2公司圈
			request.setZonetype("2");
		} else {
			request.setZonetype("1");
		}

		request.setConntenttype("1");
		request.setContent(mEditText.getText().toString().trim());
		
		FileListToUpload fileListToUpload = getMapImages(filephotos);
//		if (getMapImages(filephotos) != null && getMapImages(filephotos).size() > 0) {
//			File[] file = new File[getMapImages(filephotos).size()];
//			for (int i = 0; i < getMapImages(filephotos).size(); i++) {
//				file[i] = getMapImages(filephotos).get(i);
//			}
//			request.setUpfile(file);
//	}
		if (fileListToUpload!= null && fileListToUpload.size() > 0) {
//			request.setUpfile(fileListToUpload);
//			request.setUpfile(fileListToUpload.get(0));
			
			HashMap<String, File> pics = new HashMap<String, File>();
			int s = pics.size();
			for(int i = 0; i < fileListToUpload.size(); i++) {
				pics.put("upfile[" + i + "]", fileListToUpload.get(i));
			}
			request.setUpfile(pics);
		}else {
			showToast("请添加图片后再进行发布");
		}
		request.setDevRequest(true);

		APIClient.sendCompanyCircle(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null) {
					mHttpResponseHandler.cancle();
				}
				mHttpResponseHandler = this;
//				showLoadingView();
			}

			@Override
			public void onSuccess(String content) {
				SendCompanyCircleResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							SendCompanyCircleResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					if (response != null) {
						showToast("发布成功");
						
						Intent intent = new Intent();
						CompanyZoneList companyZoneList = new CompanyZoneList();
						if (response.getData() != null) {
							CompanyZoneItemUserInfo userInfo = new CompanyZoneItemUserInfo();
							userInfo.setName(Constants.userInfo.getNickName());
							userInfo.setLogo(Constants.userInfo.getLogo());
                            userInfo.setUserid(Constants.userInfo.getUserId());
							companyZoneList.setUserinfo(userInfo);
							companyZoneList.setContent(mEditText.getText().toString().trim());
							companyZoneList.setInfoid(response.getData().getInfoid());
							companyZoneList.setFtime("1秒前");
                            companyZoneList.setIscomment("1"); // 设置为可评论
							List<CompanyZoneItemExrta> list = new ArrayList<CompanyZoneItemExrta>();
                            if (companyZoneList.getExrta() == null) {
                                companyZoneList.setExrta(new ArrayList<CompanyZoneItemExrta>());
                            }
							if (response.getData().getExrta() != null && response.getData().getExrta().size() > 0) {
								for (int i = 0; i < response.getData().getExrta().size(); i++) {
									CompanyZoneItemExrta companyZoneItemExrta= new CompanyZoneItemExrta();
									companyZoneItemExrta.setFile(response.getData().getExrta().get(i).getFile());
									companyZoneItemExrta.setThumb(response.getData().getExrta().get(i).getThumb());
									list.add(companyZoneItemExrta);
								}
								companyZoneList.setExrta(list);
							}
						}
						intent.putExtra("data", companyZoneList);
						intent.putExtra("isFriends", friendRadio.isChecked());
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				} else {
					showToast(response.getMsg());
					sendBtn.setClickable(true);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				showToast(R.string.load_server_failure);
				sendBtn.setClickable(true);
			}
		});
	}

	@Override
	public void onExprItemClick(String exprInfo, boolean isDelete) {
		SpannableString ss;
		int textSize = (int) mEditText.getTextSize();
		if (isDelete) {
			HashMap<String, Integer> mExprFilenameIdData = XWExpressionManager
					.getInstance().getmExprInfoIdValuesCN(this);
			ss = XWExpressionUtil.generateSpanComment(getApplicationContext(),
					XWExpressionUtil.deleteOneWord(mEditText.getText()
							.toString(), mExprFilenameIdData), textSize);
		} else {
			String content = mEditText.getText() + exprInfo;
			ss = XWExpressionUtil.generateSpanComment(getApplicationContext(),
					content, textSize);
		}
		mEditText.setText(ss);
		mEditText.setSelection(ss.length());
	}

	@Override
	public void onShowInputView() {

	}

	@Override
	public void onHideInputView() {

	}

	/**
	 * 控制表情区和软键盘的显示
	 * 
	 * @param isShowKeyboard
	 * @param isSwitch
	 */
	private void controlKeyboardOrExpr(boolean isShowKeyboard, boolean isSwitch) {
		if (isSwitch) {
			if (!isShowKeyboard) {
				mIsExprShow = true;
				mIsSoftKeyboardShow = false;
				mImm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
				mUiHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mExpressionWidgt.setVisibility(View.VISIBLE);
					}
				}, 100);
				faceBtn.setBackgroundResource(R.drawable.selector_s_chat_keyboard);
			} else {
				mIsExprShow = false;
				mIsSoftKeyboardShow = true;
				mExpressionWidgt.setVisibility(View.GONE);
				faceBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} else {
			if (isShowKeyboard) {
				mIsExprShow = false;
				mIsSoftKeyboardShow = true;
				mExpressionWidgt.setVisibility(View.GONE);
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			} else {
				if (mIsSoftKeyboardShow) {
					mIsSoftKeyboardShow = false;
					mImm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
				} else {
					mIsExprShow = false;
					mExpressionWidgt.setVisibility(View.GONE);
				}
			}
			faceBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
		}
	}

	private void resizeExprArea(int viewWidth) {
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, mSoftKeyboardHeight);
		rlp.addRule(RelativeLayout.BELOW, R.id.comment_editt_linear);
		mExpressionWidgt.setLayoutParams(rlp);
		mExpressionWidgt.setKeyboardSize(viewWidth, mSoftKeyboardHeight);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (mIsExprShow || mIsSoftKeyboardShow) {
				int rootTop = 0;
				if (mIsExprShow) {
					rootTop = mRootContainerBottom - mSoftKeyboardHeight;
					// -mCommentOperateLo.getHeight();
				} else {
					rootTop = mRootContainerBottom;
					// - mCommentOperateLo.getHeight();
				}
				if (ev.getY() < rootTop) {
					controlKeyboardOrExpr(false, false);
					return true;
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 拍照或者从相册获取图片
	 */
	private void showPhotoActionSheet(final Activity activity) {
		FriendsActionSheet actionSheet = FriendsActionSheet
				.getInstance(activity);
		actionSheet.setItems(new String[] {
				getString(R.string.company_circle_camera),
				getString(R.string.company_circle_phone),
				getString(R.string.cancel) });
		actionSheet.setListener(new FriendsActionSheet.IListener() {

			@Override
			public void onItemClicked(int index, String item) {
				switch (index) {
				case 0: // 拍照
					takePhoto();
					break;
				case 1: // 相册
					List<String> imgs = new ArrayList<String>();
					for (int i = 0; i < mAdapter.getData().size(); i++) {
			        	 if (mAdapter.getData().get(i) instanceof CompanyAddImage) {
			               continue;
			           }
			           imgs.add(mAdapter.getData().get(i).getFileUrl());
					}
					startActivityForResult(CompanyCircleAlbumGroupActivity
							.newIntent(SendImageTextActivity.this, imgs), REQUEST_CODE_ALBUMGROUP);
					break;
				}
			}
		});
		actionSheet.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case REQUEST_CODE_ALBUMGROUP:
			if (data != null) {
				List<String> result = (List<String>) data
						.getSerializableExtra("result");
				if (!ListUtil.isEmpty(result)) {
					mAdapter.clearData();
					for (int i = 0; i < result.size(); i++) {
						CompanyImage companyImage = new CompanyImage();
						companyImage.setFileUrl(result.get(i));
						mAdapter.getData().add(companyImage);
					}
					
					if (mAdapter.getData().size() < SendImageTextUtil.COUNT) {
						mAdapter.getData().add(new CompanyAddImage());
					}
					
					mAdapter.notifyDataSetChanged();
				}
			}
			break;
		case REQUEST_CODE_CAMERA:
			CutOutPictures(mPhotoUri);
			break;
        case REQUEST_CODE_PREVIEW:
        {
        	if (data != null) {
				List<String> result = (List<String>) data
						.getSerializableExtra("result");
                mAdapter.clearData();
				if (!ListUtil.isEmpty(result)) {
					for (int i = 0; i < result.size(); i++) {
						CompanyImage companyImage = new CompanyImage();
						companyImage.setFileUrl(result.get(i));
						mAdapter.getData().add(companyImage);
					}
				}
                if (mAdapter.getData().size() < SendImageTextUtil.COUNT) {
                    mAdapter.getData().add(new CompanyAddImage());
                }

                mAdapter.notifyDataSetChanged();
        	}
        }
            break;
		default:
			break;
		}
	}

	/** 拍照 */
	private void takePhoto() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			ContentResolver contentResolver = getContentResolver();
			mPhotoUri = contentResolver.insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new ContentValues());
			AppCoomentUtil.takePhoto(this, mPhotoUri, REQUEST_CODE_CAMERA);
		} else {
			showToast("请安装sd卡");
		}
	}

	/**
	 * 选择图片后， 判断图片是否需要旋转--针对于三星手机
	 */
	private void CutOutPictures(Uri uri) {
		if (uri == null) {
			showToast("选择图片文件不正确");
			return;
		}

		final String path = AppCoomentUtil.getUriPath(this, uri);///storage/sdcard0/DCIM/Camera/1408475049993.jpg
		if (path == null) {
			showToast("选择图片文件不正确");
			return;
		}

		// if (ImageUtil.needRotate(GoodsAddActivity.this, path)) {
		// new AsyncTask<Void, Void, Void>() {
		//
		// @Override
		// protected Void doInBackground(Void... params) {
		// ImageUtil.rotatedBitmap(GoodsAddActivity.this, path,
		// tempPhotoFile.getAbsolutePath());
		// return null;
		// }
		//
		// @Override
		// protected void onPreExecute() {
		// super.onPreExecute();
		// // showLoading(R.string.please_wait);
		// }
		//
		// @Override
		// protected void onPostExecute(Void result) {
		// super.onPostExecute(result);
		// // hideLoading();
		// // if (!isDestroyed) {
		// mPicTmpPath = cropImage(tempPhotoFile.getAbsolutePath(),
		// false);
		// // }
		// }
		// }.execute();
		// } else {
		// mPicTmpPath = cropImage(path, true);
		// }

		CompanyImage companyImage = new CompanyImage();
		companyImage.setFileUrl(path);
		mAdapter.getData().add(mAdapter.getData().size() - 1, companyImage);
		mAdapter.notifyDataSetChanged();
	}

	/**发布的时候把所有的图片都Copy出来， 
	 *  放到一个新的文件夹内，
	 *  再将文件夹内的图片进行压缩， 最后上传*/
	private void copyImageTofiles(String srcPath, String desPath) {
		File filephoto = new File(desPath+ String.valueOf(System.currentTimeMillis()) + ".jpg");
		if (!filephoto.exists()) {
			try {
				filephoto.createNewFile();
			} catch (IOException e) {
				showToast("图片文件创建成功");
			}
		}
		
		FileUtil.copy(new File(srcPath), filephoto);
	}
	
	private void copyImageTofile(String srcPath, String desPath) {
		File filephoto = new File(desPath);
		if (!filephoto.exists()) {
			try {
				filephoto.createNewFile();
			} catch (IOException e) {
				showToast("图片文件创建成功");
			}
		}
		
		FileUtil.copy(new File(srcPath), filephoto);
	}
	
	/**创建文件夹， 并返回文件夹的路径*/
	private String createFiles(String gen) {
		File newPhotoFiles = new File(gen);
		if (!newPhotoFiles.exists()) {
			newPhotoFiles.mkdir();
		}
		
			File newPhotoFile = new File(gen + "/"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg");
			if (!newPhotoFile.exists()) {
				try {
					newPhotoFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return newPhotoFile.getAbsolutePath();
	}
	
	/** 删除文件夹内的图片文件 */
	private void fileClear(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						files[i].delete();
					}
				}
			}
		} else {
			file.mkdir();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
//		fileClear(Constants.Directorys.TAKE_PHOTO);
		fileClear(filephotos);
	}
	
	/** 将选中的图片从文件夹中选出来 */
	public FileListToUpload getMapImages(String filePath) {

		FileListToUpload listaFiles = new FileListToUpload();
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						if (!files[i].isDirectory()) {
							listaFiles.add(files[i]);
						}
					}
				}
				return listaFiles;
			}
		}
		return null;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position,
			long arg3) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setItems(new String[]{getString(R.string.delete)}, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case 0:
//                    	if (!(mAdapter.getItem(position) instanceof CompanyAddImage)) {
//                    		mAdapter.getData().remove(position);
//                		}
//                    break;
//                }
//            }
//        });
//        builder.create().show();

    return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**退出提示对话框*/
	private void showExitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确定要退出吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				SendImageTextActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", null);
		
		builder.show();
	}
}
