package com.cplatform.xhxw.ui.ui.settings;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.GetUserSettingPropertiesResponse;
import com.cplatform.xhxw.ui.http.net.SetUserInfoRequest;
import com.cplatform.xhxw.ui.http.net.SetUserInfoResponse;
import com.cplatform.xhxw.ui.model.CareerOrBloodOptions;
import com.cplatform.xhxw.ui.model.KeyValueParams;
import com.cplatform.xhxw.ui.model.SetUserInfo;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.ActionSheet;
import com.cplatform.xhxw.ui.ui.base.widget.RoundedImageView;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.FileUtil;
import com.cplatform.xhxw.ui.util.LogUtil;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户信息修改
 * Created by cy-love on 14-1-17.
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private static final int REQUEST_CODE_TAKE_PHOTO = 1; // 拍照
    private static final int REQUEST_CODE_PICK_PHOTO = 2; // 选择图片
    private static final int REQUEST_CODE_CROP_IMAGE = 3; // 裁剪图片

    @InjectView(R.id.iv_avatar)
    RoundedImageView mAvatar;
    private String mTmpFile;
    private String mPhotoFilePath;
    private AsyncHttpResponseHandler mHttpHandler;
    
    private RelativeLayout mNickNameLo;
    private TextView mNickNameTv;
    private RelativeLayout mBindPhoneLo;
    private TextView mBindPhoneTv;
    private RelativeLayout mXinbBieLo;
    private TextView mXinbBieTv;
    private RelativeLayout mBirthdayLo;
    private TextView mBirthdayTv;
    private RelativeLayout mCareerLo;
    private TextView mCareerTv;
    private RelativeLayout mBloodLo;
    private TextView mBloodTv;
    private EditText mNickEditEt;
    private TextView mAvatarEdit;
    
    private ProgressDialog mProgressDialog;
    private AlertDialog mNickEditDialog;
    
    private Map<String, Integer> mOptionsValueKeyParams = new HashMap<String, Integer>();
    private int mTryFetchOptionsListCount  = 0;
    private int mCareerKey = -1;
    private int mBloodKey = -1;
    private int mSex = -1;
    private boolean isAvatarChange = false;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "UserInfoActivity";
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.inject(this);
        initActionBar();
        init();
        UserinfoUtil.getUserSettingProperties();
    }

    private void init() {
        ImageLoader.getInstance().displayImage(Constants.userInfo.getLogo(), mAvatar,
                DisplayImageOptionsUtil.avatarImagesOptions);
        
        mNickNameLo = (RelativeLayout) findViewById(R.id.user_info_nickname_lo);
        mNickNameTv = (TextView) findViewById(R.id.userinfo_nickname_tv);
        mBindPhoneLo = (RelativeLayout) findViewById(R.id.user_info_bind_phone_lo);
        mBindPhoneTv = (TextView) findViewById(R.id.userinfo_bind_phone_tv);
        mXinbBieLo = (RelativeLayout) findViewById(R.id.user_info_xingbie_lo);
        mXinbBieTv = (TextView) findViewById(R.id.userinfo_xingbie_tv);
        mBirthdayLo = (RelativeLayout) findViewById(R.id.user_info_birthday_lo);
        mBirthdayTv = (TextView) findViewById(R.id.userinfo_birthday_tv);
        mCareerLo = (RelativeLayout) findViewById(R.id.user_info_profession_lo);
        mCareerTv = (TextView) findViewById(R.id.userinfo_profession_tv);
        mBloodLo = (RelativeLayout) findViewById(R.id.user_info_bloodtype_lo);
        mBloodTv = (TextView) findViewById(R.id.userinfo_bloodtype_tv);
        mAvatarEdit = (TextView) findViewById(R.id.user_info_avatar_edit_tv);
        
        mNickNameLo.setOnClickListener(this);
        mBindPhoneLo.setOnClickListener(this);
        mXinbBieLo.setOnClickListener(this);
        mBirthdayLo.setOnClickListener(this);
        mCareerLo.setOnClickListener(this);
        mBloodLo.setOnClickListener(this);
        mAvatarEdit.setOnClickListener(this);
    }
    
	@Override
	protected void onResume() {
		mNickNameTv.setText(Constants.userInfo.getNickName());
        mXinbBieTv.setText(UserinfoUtil.getSexBykey(Constants.userInfo.getSex()));
        mBirthdayTv.setText(DateUtil.getFormattedBirthday(Constants.userInfo.getBirthday()));
        mCareerTv.setText(UserinfoUtil.getCareerOrBloodByKey(Constants.userInfo.getCareer(), true));
        mBloodTv.setText(UserinfoUtil.getCareerOrBloodByKey(Constants.userInfo.getBlood(), false));
        mBindPhoneTv.setText(Constants.userInfo.getBindmobile());
		super.onResume();
	}

    @OnClick(R.id.iv_avatar)
    public void onAvatarAction() {
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

    @OnClick(R.id.btn_save)
    public void onSaveAction() {
        if (!userInfoChanged() && !isAvatarChange) {
            showToast(getString(R.string.not_change_userinfo));
            return;
        }
        
        SetUserInfoRequest request = new SetUserInfoRequest();
        if (!TextUtils.isEmpty(mPhotoFilePath)) {
            try {
                String logo = FileUtil.file2Base64(mPhotoFilePath);
                request.setLogo(logo);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        
        String nick = mNickNameTv.getText().toString();
        if (!nick.equals(Constants.userInfo.getNickName())) {
            request.setNickname(nick);
        }
        
        if((mSex != -1) && (Constants.userInfo.getSex() != mSex)) {
        	request.setSex(String.valueOf(mSex));
        } else {
        	request.setSex("");
        }
        
        if((mCareerKey != -1) && (Constants.userInfo.getCareer() != mCareerKey)) {
        	request.setCareer(String.valueOf(mCareerKey));
        } else {
        	request.setCareer("");
        }
        
        if((mBloodKey != -1) && (Constants.userInfo.getBlood() != mBloodKey)) {
        	request.setBlood(String.valueOf(mBloodKey));
        } else {
        	request.setBlood("");
        }
        
        String date = mBirthdayTv.getText().toString().trim();
        if(!mBirthdayTv.getText().toString().trim().
				equals(DateUtil.getFormattedBirthday(Constants.userInfo.getBirthday()))) {
        	request.setBirthday(String.valueOf(DateUtil.getTimeFromDate(date)));
        } else {
        	request.setBirthday("");
        }
        
        executeSave(request);
    }

    private void executeSave(final SetUserInfoRequest request) {
        APIClient.setUserInfo(request, new AsyncHttpResponseHandler() {
            @Override
            protected void onPreExecute() {
                if (mHttpHandler != null) {
                    mHttpHandler.cancle();
                }
                mHttpHandler = this;
                showLoadingView();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                showToast(R.string.load_server_failure);
            }

            @Override
            public void onFinish() {
                mHttpHandler = null;
                hideLoadingView();
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                SetUserInfoResponse response;
                try {
                	ResponseUtil.checkResponse(content);
                    response = new Gson().fromJson(content, SetUserInfoResponse.class);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    LogUtil.w(TAG, "返回数据::" + content);
                    LogUtil.w(TAG, e);
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
                        
                        Constants.saveUserInfo();
                        if (!TextUtils.isEmpty(request.getLogo())) {
                            ImageLoader.getInstance().clearDiscCache();
                            ImageLoader.getInstance().clearMemoryCache();
                        }
                    }
                    finish();
                } else {
                    showToast(response.getMsg());
                }
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO: // 照片
            {
                startActivityForResult(CropAvatarActivity.getIntent(this, mTmpFile), REQUEST_CODE_CROP_IMAGE);
            }
            break;
            case REQUEST_CODE_PICK_PHOTO: {
                if (data != null) {
                    Uri uri = data.getData();
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        Cursor cursor = getContentResolver().query(uri,
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
                        LogUtil.i(TAG, "path=" + path);

                        startActivityForResult(CropAvatarActivity.getIntent(this, path), REQUEST_CODE_CROP_IMAGE);
                    } else {
                        LogUtil.i(TAG, "path=" + uri.getPath());
                        startActivityForResult(CropAvatarActivity.getIntent(this, uri.getPath()), REQUEST_CODE_CROP_IMAGE);
                    }
                }
            }
            break;
            case REQUEST_CODE_CROP_IMAGE: // 裁剪完成
            {
                if (data != null) {
                	isAvatarChange = true;
                    mAvatar.setImageBitmap(null);
                    mPhotoFilePath = data.getStringExtra("path");
                    Bitmap bm = BitmapFactory.decodeFile(mPhotoFilePath);
                    mAvatar.setImageBitmap(bm);
                }
            }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mHttpHandler != null) {
            mHttpHandler.cancle();
            mHttpHandler = null;
        }
        super.onDestroy();
    }
    
    @Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(isAvatarChange || userInfoChanged()) {
				showQuitConfirmDialog();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
    
    private void showQuitConfirmDialog() {
    	new AlertDialog.Builder(this).setTitle("离开确认")
    		.setMessage("修改内容尚未提交，是否确认离开？")
    		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				}
			})
			.setNegativeButton(R.string.cancel, null).show();
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
  //判断字符是否是数字
  	public boolean isNum(String msg){
  		if(java.lang.Character.isDigit(msg.charAt(0))){
  			return true;
  		}
  		return false;
  	}
  	//判断字符是否是字母
  	public boolean getLetter(String str){  
          return str.matches("^[A-Za-z]+$");  
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
  	
  	/**
  	 * 生日选择器
  	 */
  	private void showDatePickerDialog() {
  		int tmpY = 2000;
  		int tmpM = 0;
  		int tmpD = 1;
  		String currentDate = mBirthdayTv.getText().toString().trim();
  		String[] peice = currentDate.split("-");
  		if(peice.length == 3) {
  			tmpY = Integer.valueOf(peice[0]);
  			tmpM = Integer.valueOf(peice[1]) - 1;
  			tmpD = Integer.valueOf(peice[2]);
  		}
  		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mBirthdayTv.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
		}, tmpY, tmpM, tmpD).show();
  	}
  	
  	/**
  	 * 编辑昵称
  	 */
  	private void showNicknameEditDialog() {
  		View v = initEditVIews();
  		mNickEditEt.setText(mNickNameTv.getText().toString().trim());
  		Builder editBuilder = new AlertDialog.Builder(this).setTitle("请填写昵称")
  			.setView(v);
  		mNickEditDialog = editBuilder.show();
  	}
  	
  	private View initEditVIews() {
  		LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  		View dialog = lif.inflate(R.layout.dialog_edittext_lo, null);
  		mNickEditEt = (EditText) dialog.findViewById(R.id.dialog_edittext_et);
  		Button confirmBtn = (Button) dialog.findViewById(R.id.dialog_edittext_confirm_btn);
  		confirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = mNickEditEt.getText().toString().trim();
				if (getCount(content) > 14) {
					showToast(R.string.name_lenther);
				} else if (getCount(content) <= 0) {
					showToast("昵称不能为空");
				} else {
					mNickNameTv.setText(content);
					mNickEditDialog.dismiss();
				}
			}
		});
  		Button cancelBtn = (Button) dialog.findViewById(R.id.dialog_edittext_cancel_btn);
  		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mNickEditDialog.dismiss();
			}
		});
  		return dialog;
  	}
  	
  	/**
  	 * 性别选择
  	 */
  	private void showXingbieChooseDialog() {
  		final String[] items = new String[]{"保密", "男", "女"};
  		int checkedItem = 0;
  		String displayedContent = mXinbBieTv.getText().toString().trim();
  		if(displayedContent.equals(items[1])) {
  			checkedItem = 1;
  		} else if(displayedContent.equals(items[2])) {
  			checkedItem = 2;
  		}
  		new AlertDialog.Builder(this).setTitle("请选择您的性别")
  			.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mSex = which;
					mXinbBieTv.setText(items[which]);
					dialog.dismiss();
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.show();
  	}
  	
  	/**
  	 * 弹出职业/血型选择弹出框
  	 * @param isCareer
  	 */
  	private void showChooseAlertDialog(final boolean isCareer) {
  		final String[] items = getCareerOrBloodList(isCareer);
  		if(items == null) {
  			if(mTryFetchOptionsListCount > 2) {
  				mTryFetchOptionsListCount = 0;
  				return;
  			}
  			getUserSettingProperties(isCareer);
  			mProgressDialog = ProgressDialog.show(this, null, "初始化列表，请稍候...");
  			return;
  		}
  		String title = "请选择您的职业";
  		if(!isCareer) {
  			title = "请选择您的血型";
  		}
  		int checkedItem = getDefaultCheckItem(items, isCareer);
  		new AlertDialog.Builder(this).setTitle(title)
  			.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(isCareer) {
						mCareerKey = mOptionsValueKeyParams.get(items[which]);
						mCareerTv.setText(items[which]);
					} else {
						mBloodKey = mOptionsValueKeyParams.get(items[which]);
						mBloodTv.setText(items[which]);
					}
					dialog.dismiss();
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.show();
  	}
  	
  	/**
  	 * 获取选项列表（职业或者血型）
  	 * @param isCareer
  	 * @return
  	 */
  	private String[] getCareerOrBloodList(boolean isCareer) {
  		String content = null;
  		if(isCareer) {
  			content = PreferencesManager.getCareerList(App.CONTEXT);
  		} else {
  			content = PreferencesManager.getBloodList(App.CONTEXT);
  		}
  		if(content == null) {
  			return null;
  		}
  		
  		mOptionsValueKeyParams.clear();
  		String[] items = content.split(",");
  		String[] result = new String[items.length];
  		for(int i = 0; i < items.length; i++) {
  			String item = items[i];
  			String[] keyValue = item.split(UserinfoUtil.KEY_VALUE_SPERATOR);
  			result[i] = keyValue[1];
  			mOptionsValueKeyParams.put(keyValue[1], Integer.valueOf(keyValue[0]));
  		}
  		return result;
  	}
  	
  	private int getDefaultCheckItem(String[] list, boolean isCareer) {
  		String defaultDisString = null;
  		if(isCareer) {
  			defaultDisString = mCareerTv.getText().toString().trim();
  		} else {
  			defaultDisString = mBloodTv.getText().toString().trim();
  		}
  		
  		for(int i = 0; i < list.length; i++) {
  			String item = list[i];
  			if(item.equals(defaultDisString)) {
  				return i;
  			}
  		}
  		
  		return 0;
  	}
  	
  	/**
  	 * 从后台获取选项列表
  	 * @param isCareer
  	 */
  	private void getUserSettingProperties(final boolean isCareer) {
  		mTryFetchOptionsListCount++;
		APIClient.getUserSettingProperties(new BaseRequest(), 
				new AsyncHttpResponseHandler(){

					@Override
					public void onFinish() {
						mProgressDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						showToast("获取列表失败，请检查网络后重试！");
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						GetUserSettingPropertiesResponse response;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content, GetUserSettingPropertiesResponse.class);
						} catch (Exception e) {
							return;
						}
						
						if(response.isSuccess()) {
							CareerOrBloodOptions data = response.getData();
							List<KeyValueParams> mCareers = data.getCareer();
							List<KeyValueParams> mBlood = data.getBlood();
							UserinfoUtil.saveUserProperties(mCareers, true);
							UserinfoUtil.saveUserProperties(mBlood, false);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									showChooseAlertDialog(isCareer);
								}
							});
						}
					}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.user_info_nickname_lo:
			showNicknameEditDialog();
			break;
		case R.id.user_info_bind_phone_lo:
			startActivity(BindPhoneActivity.getIntent(this));
			break;
		case R.id.user_info_xingbie_lo:
			showXingbieChooseDialog();
			break;
		case R.id.user_info_birthday_lo:
			showDatePickerDialog();
			break;
		case R.id.user_info_profession_lo:
			showChooseAlertDialog(true);
			break;
		case R.id.user_info_bloodtype_lo:
			showChooseAlertDialog(false);
			break;
		case R.id.user_info_avatar_edit_tv:
			onAvatarAction();
			break;

		default:
			break;
		}
	}
	
	private boolean userInfoChanged() {
		if(!Constants.userInfo.getNickName().equals(mNickNameTv.getText().toString().trim())) {
			return true;
		}
		if(mSex != -1 || mCareerKey != -1 || mBloodKey != -1) {
			return true;
		}
		if(!mBirthdayTv.getText().toString().trim().
				equals(DateUtil.getFormattedBirthday(Constants.userInfo.getBirthday()))){
			return true;
		}
		return false;
	}
}