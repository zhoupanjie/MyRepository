package com.cplatform.xhxw.ui.ui.main.saas.personalcenter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.http.net.SetUserInfoResponse;
import com.cplatform.xhxw.ui.http.net.saas.SaasSetUserinfoRequest;
import com.cplatform.xhxw.ui.model.SetUserInfo;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.widget.ActionSheet;
import com.cplatform.xhxw.ui.ui.settings.CropAvatarActivity;
import com.cplatform.xhxw.ui.ui.settings.UserinfoUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.FileUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 个人信息修改
 */
public class SaasUserInfoFragment extends BaseFragment implements OnClickListener {
	private static final int REQUEST_CODE_TAKE_PHOTO = 0;
	private static final int REQUEST_CODE_PICK_PHOTO = 1;
	private static final int REQUEST_CODE_CROP_IMAGE = 2;
	
	private static final int EDIT_TEXT_TELNUM = 1;
	private static final int EDIT_TEXT_NICKNAME = 2;
	private static final int EDIT_TEXT_SIGNATURE = 3;
	
	private static final int NICKNAME_LENGTH_LIMIT = 14;
	private static final int TELNUM_LENGTH_LIMIT = 15;
	private static final int SIGNATURE_LENGTH_LIMIT = 60;
	
	private RelativeLayout mAvatarLo;
	private ImageView mAvatarIV;
	private TextView mNameTV;
	private RelativeLayout mNickNameLo;
	private TextView mNickNameTV;
	private TextView mMobileNumTV;
	private RelativeLayout mTelNumLo;
	private TextView mTelNumTV;
	private TextView mMailboxTV;
	private RelativeLayout mSexSelectLo;
	private TextView mSexTV;
	private RelativeLayout mCharactorSignLo;
	private TextView mCharactorSignTV;
	private EditText mDialogET;
	private Button mSaveBtn;
	
	private int mSex = -1;
	
	private AlertDialog mEditDialog;
	private String mTmpFile;
	private boolean isAvatarChange = false;
	private String mPhotoFilePath;
	private int mCurrentEditText = EDIT_TEXT_NICKNAME;
	private int mCurrentEditLengthLimit = NICKNAME_LENGTH_LIMIT;
    private AsyncHttpResponseHandler mHttpHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.saas_fragment_userinfo, container, false);
		initViews(rootView);
		return rootView;
	}
	
	private void initViews(View rootView) {
		mAvatarLo = (RelativeLayout) rootView.findViewById(R.id.saas_userinfo_ly_avatar);
		mAvatarIV = (ImageView) rootView.findViewById(R.id.saas_userinfo_iv_avatar);
		mNameTV = (TextView) rootView.findViewById(R.id.saas_userinfo_name_tv);
		mNickNameLo = (RelativeLayout) rootView.findViewById(R.id.saas_userinfo_nickname_lo);
		mNickNameTV = (TextView) rootView.findViewById(R.id.saas_userinfo_nickname_tv);
		mMobileNumTV = (TextView) rootView.findViewById(R.id.saas_userinfo_mobilenum_tv);
		mTelNumLo = (RelativeLayout) rootView.findViewById(R.id.saas_userinfo_telnum_lo);
		mTelNumTV = (TextView) rootView.findViewById(R.id.saas_userinfo_telnum_tv);
		mMailboxTV = (TextView) rootView.findViewById(R.id.saas_userinfo_mailbox_tv);
		mSexSelectLo = (RelativeLayout) rootView.findViewById(R.id.saas_userinfo_sex_lo);
		mSexTV = (TextView) rootView.findViewById(R.id.saas_userinfo_sex_tv);
		mCharactorSignLo = (RelativeLayout) rootView.findViewById(R.id.saas_userinfo_persign_lo);
		mCharactorSignTV = (TextView) rootView.findViewById(R.id.saas_userinfo_persign_tv);
		mSaveBtn = (Button) rootView.findViewById(R.id.saas_userinfo_btn_save);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvents();
		displayUserinfo();
	}

	private void initEvents() {
		mAvatarLo.setOnClickListener(this);
		mNickNameLo.setOnClickListener(this);
		mTelNumLo.setOnClickListener(this);
		mSexSelectLo.setOnClickListener(this);
		mCharactorSignLo.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.saas_userinfo_ly_avatar:
			onAvatarAction();
			break;
		case R.id.saas_userinfo_nickname_lo:
			mCurrentEditText = EDIT_TEXT_NICKNAME;
			mCurrentEditLengthLimit = NICKNAME_LENGTH_LIMIT;
			showNicknameEditDialog(mNickNameTV);
			break;
		case R.id.saas_userinfo_telnum_lo:
			mCurrentEditText = EDIT_TEXT_TELNUM;
			mCurrentEditLengthLimit = TELNUM_LENGTH_LIMIT;
			showNicknameEditDialog(mTelNumTV);
			break;
		case R.id.saas_userinfo_sex_lo:
			showXingbieChooseDialog();
			break;
		case R.id.saas_userinfo_persign_lo:
			mCurrentEditText = EDIT_TEXT_SIGNATURE;
			mCurrentEditLengthLimit = SIGNATURE_LENGTH_LIMIT;
			showNicknameEditDialog(mCharactorSignTV);
			break;
		case R.id.saas_userinfo_btn_save:
			onSaveAction();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 显示用户信息
	 */
	private void displayUserinfo() {
		ImageLoader.getInstance().displayImage(Constants.userInfo.getLogo(), mAvatarIV,
				DisplayImageOptionsUtil.avatarSaasImagesOptions);
		mNameTV.setText(Constants.userInfo.getEnterprise().getStaff_name());
		mNickNameTV.setText(Constants.userInfo.getNickName());
		mMobileNumTV.setText(Constants.userInfo.getEnterprise().getStaff_phone());
		mTelNumTV.setText(Constants.userInfo.getEnterprise().getStaff_landline());
		mMailboxTV.setText(Constants.userInfo.getEnterprise().getStaff_email());
		mSexTV.setText(UserinfoUtil.getSexBykey(Constants.userInfo.getSex()));
		mCharactorSignTV.setText(Constants.userInfo.getEnterprise().getStaff_signature());
	}
	
	/**
  	 * 性别选择
  	 */
  	private void showXingbieChooseDialog() {
  		final String[] items = new String[]{"保密", "男", "女"};
  		int checkedItem = 0;
  		String displayedContent = mSexTV.getText().toString().trim();
  		if(displayedContent.equals(items[1])) {
  			checkedItem = 1;
  		} else if(displayedContent.equals(items[2])) {
  			checkedItem = 2;
  		}
  		new AlertDialog.Builder(mAct).setTitle("请选择您的性别")
  			.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mSex = which;
					mSexTV.setText(items[which]);
					dialog.dismiss();
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.show();
  	}
  	
  	/**
  	 * 弹出文字编辑框
  	 */
  	private void showNicknameEditDialog(TextView targetView) {
  		View v = initEditVIews(targetView);
  		mDialogET.setText(targetView.getText().toString().trim());
  		if(mCurrentEditText == EDIT_TEXT_TELNUM) {
  			mDialogET.setKeyListener(mNumberKeyLis);
  		}
  		Builder editBuilder = new AlertDialog.Builder(mAct).setTitle("编辑内容")
  			.setView(v);
  		mEditDialog = editBuilder.show();
  	}
  	
  	/**
	 * 文字编辑框字数限定监听
	 */
	TextWatcher editTextWatcher = new TextWatcher() {
		
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
				if(content.getBytes("GBK").length > mCurrentEditLengthLimit) {
					showToast(R.string.contribue_out_of_length);
					if(content.length() > mCurrentEditLengthLimit) {
						content = content.substring(0, mCurrentEditLengthLimit - 1);
					}
					while(content.toString().getBytes("GBK").length > mCurrentEditLengthLimit) {
						content = content.substring(0, content.length() - 1);
					}
					mDialogET.setText(content);
					mDialogET.setSelection(content.length());
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	};
  	
  	NumberKeyListener mNumberKeyLis = new NumberKeyListener() {
		
		@Override
		public int getInputType() {
			return android.text.InputType.TYPE_CLASS_PHONE;
		}
		
		@Override
		protected char[] getAcceptedChars() {
			String acceptedCharacters = "1234567890.*+-%/";
			char[] numberChars = acceptedCharacters.toCharArray();
			return numberChars;
		}
	};
  	
  	private View initEditVIews(final TextView targetView) {
  		LayoutInflater lif = (LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  		View dialog = lif.inflate(R.layout.dialog_edittext_lo, null);
  		mDialogET = (EditText) dialog.findViewById(R.id.dialog_edittext_et);
  		mDialogET.setSingleLine(false);
  		mDialogET.addTextChangedListener(editTextWatcher);
  		Button confirmBtn = (Button) dialog.findViewById(R.id.dialog_edittext_confirm_btn);
  		confirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = mDialogET.getText().toString().trim();
				if (getCount(content) <= 0) {
					showToast("内容不能为空");
				} else {
					targetView.setText(content);
					mEditDialog.dismiss();
				}
			}
		});
  		Button cancelBtn = (Button) dialog.findViewById(R.id.dialog_edittext_cancel_btn);
  		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditDialog.dismiss();
			}
		});
  		return dialog;
  	}
  	
  	/**
     * 判断字符串的字符个数
     * 
     * 字母，数字各按一个字符，汉字按两个
     * */
    private int getCount(String string) {
    	int count = 0;
    	
		if (TextUtils.isEmpty(string.trim())) {
			return 0;
		}
		
		for (int i = 0; i < string.length(); i++) {
			if (isHanZi(string.substring(i, i+1))) {
				count = count + 2; 
			}else {
				count++;
			}
		}
    	return count;
	}
    
    //判断字符是否是汉字
  	private boolean isHanZi(String string) {
  		Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
  		Matcher m = p_str.matcher(string);
  		if(m.find()&&m.group(0).equals(string)){
  		   return true;
  		}
  		return false;
  	}
  	
  	public void onAvatarAction() {
        ActionSheet.Builder builder = new ActionSheet.Builder(mAct);
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
     * 拍照
     */
    private void takePhoto() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                Intent intent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                mTmpFile = Constants.Directorys.TEMP + "/" + (new Date().getTime()) + ".jpg";
                // localTempImgDir和localTempImageFileName是自己定义的名字
                Uri u = Uri.fromFile(new File(mTmpFile));
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
            } catch (ActivityNotFoundException e) {
            }
        }
    }
    
    /**
     * 选择相册
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO: // 照片
            {
                startActivityForResult(CropAvatarActivity.getIntent(mAct, mTmpFile), REQUEST_CODE_CROP_IMAGE);
            }
            break;
            case REQUEST_CODE_PICK_PHOTO: {
                if (data != null) {
                    Uri uri = data.getData();
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        Cursor cursor = mAct.getContentResolver().query(uri,
                                new String[]{MediaStore.Images.Media.DATA},
                                null, null, null);
                        if (null == cursor) {
                            showToast("图片没找到");
                            return;
                        }
                        cursor.moveToFirst();
                        String path = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                        LogUtil.i("", "path=" + path);

                        startActivityForResult(CropAvatarActivity.getIntent(mAct, path), REQUEST_CODE_CROP_IMAGE);
                    } else {
                        LogUtil.i("", "path=" + uri.getPath());
                        startActivityForResult(CropAvatarActivity.getIntent(mAct, uri.getPath()), REQUEST_CODE_CROP_IMAGE);
                    }
                }
            }
            break;
            case REQUEST_CODE_CROP_IMAGE: // 裁剪完成
            {
                if (data != null) {
                	isAvatarChange = true;
                	mPhotoFilePath = data.getStringExtra("path");
                    ImageLoader.getInstance().displayImage("file://" + mPhotoFilePath, mAvatarIV,
                    		DisplayImageOptionsUtil.avatarSaasImagesOptions);
                }
            }
            break;
        }
    }
    
    /**
     * 保存修改
     */
    public void onSaveAction() {
        if (!userInfoChanged() && !isAvatarChange ) {
            showToast(getString(R.string.not_change_userinfo));
            return;
        }
        
        SaasSetUserinfoRequest request = new SaasSetUserinfoRequest();
        request.setSaasRequest(true);
        if (!TextUtils.isEmpty(mPhotoFilePath)) {
            try {
                String logo = FileUtil.file2Base64(mPhotoFilePath);
                request.setLogo(logo);
            } catch (Exception e) {
                LogUtil.e("", e);
            }
        }
        
        String nick = mNickNameTV.getText().toString().trim();
        if (!nick.equals(Constants.userInfo.getNickName())) {
            request.setNickname(nick);
        }
        
        String telNum = mTelNumTV.getText().toString().trim();
        if (!telNum.equals(Constants.userInfo.getEnterprise().getStaff_landline())) {
            request.setStaff_landline(telNum);
        }
        
        String siganature = mCharactorSignTV.getText().toString().trim();
        if (!siganature.equals(Constants.userInfo.getEnterprise().getStaff_signature())) {
            request.setStaff_signature(siganature);
        }
        
        if((mSex != -1) && (Constants.userInfo.getSex() != mSex)) {
        	request.setSex("" + mSex);
        } else {
        	request.setSex("");
        }
        
        executeSave(request);
    }

    private boolean userInfoChanged() {
		return !mNickNameTV.getText().toString().trim().equals(Constants.userInfo.getNickName())
				|| !mTelNumTV.getText().toString().trim().equals(Constants.userInfo.getEnterprise().getStaff_landline())
				|| !mCharactorSignTV.getText().toString().trim().equals(Constants.userInfo.getEnterprise().getStaff_signature())
				|| mSex > -1;
	}

	private void executeSave(final SaasSetUserinfoRequest request) {
        APIClient.setSAASUserInfo(request, new AsyncHttpResponseHandler() {
            @Override
            protected void onPreExecute() {
                showLoadingView();
                HttpHanderUtil.cancel(mHttpHandler);
                mHttpHandler = this;
            }

            @Override
            public void onFailure(Throwable error, String content) {
                showToast(R.string.load_server_failure);
            }

            @Override
            public void onFinish() {
                hideLoadingView();
                mHttpHandler = null;
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                LogUtil.d(content);
                SetUserInfoResponse response;
                try {
                    response = new Gson().fromJson(content, SetUserInfoResponse.class);
                    ResponseUtil.checkObjResponse(response);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    LogUtil.w("", "返回数据::" + content);
                    LogUtil.w("", e);
                    return;
                }
                if (response.isSuccess()) {
                    showToast("修改成功！");
                    SetUserInfo info = response.getData();
                    if (info != null) {
                        Constants.userInfo.setNickName(info.getNickName());
                        Constants.userInfo.setLogo(info.getLogo());
                        Constants.userInfo.setBirthday(info.getBirthday());
                        Constants.userInfo.setSex(info.getSex());
                        Constants.userInfo.setCareer(info.getCareer());
                        Constants.userInfo.setBlood(info.getBlood());
                        Constants.userInfo.getEnterprise().setStaff_name(info.getStaff_name());
                        Constants.userInfo.getEnterprise().setStaff_phone(info.getStaff_phone());
                        Constants.userInfo.getEnterprise().setStaff_landline(info.getStaff_landline());
                        Constants.userInfo.getEnterprise().setStaff_email(info.getStaff_email());
                        Constants.userInfo.getEnterprise().setStaff_signature(info.getStaff_signature());
                        
                        Constants.saveUserInfo();
                        if (!TextUtils.isEmpty(request.getLogo())) {
                            ImageLoader.getInstance().clearDiscCache();
                            ImageLoader.getInstance().clearMemoryCache();
                        }
                    }
                    mAct.finish();
                } else {
                    showToast(response.getMsg());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        HttpHanderUtil.cancel(mHttpHandler);
        super.onDestroyView();
    }
}
