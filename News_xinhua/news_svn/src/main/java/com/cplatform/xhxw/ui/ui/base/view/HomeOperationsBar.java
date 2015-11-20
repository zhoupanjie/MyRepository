package com.cplatform.xhxw.ui.ui.base.view;

import android.annotation.TargetApi;
import android.os.Build;

import com.cplatform.xhxw.ui.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeOperationsBar extends LinearLayout implements View.OnClickListener {
	public static final int BOTTOM_BUTTON_TYPE_NEWS = 1;
	public static final int BOTTOM_BUTTON_TYPE_BROADCAST = 2;
	public static final int BOTTOM_BUTTON_TYPE_VIDEO = 3;
	public static final int BOTTOM_BUTTON_TYPE_PERSONAL = 4;
	
	private Context mCon;
	
	private LinearLayout mNewsLo;
	private ImageView mNewsIv;
	private TextView mNewsTv;
	private LinearLayout mSMsgLo;
	private ImageView mEnterpriseLogoIv;
	private TextView menterpriseNameTv;
	private LinearLayout mBBSLo;
	private ImageView mAppIv;
	private TextView mAppeTv;
	private LinearLayout mPersonalCenterLo;
	private ImageView mPersonalIv;
	private TextView mPersonalTv;
	private TextView mMsgCountTv;
    private TextView mMsgEnterprise;
    private TextView mMsgHomeOperApp;
	private onHomeBottomBarClickListener mOnHomeBottomBarClickLs;
	
	private String mEnterpriseName = "企业";
	private int mSelectedType = BOTTOM_BUTTON_TYPE_NEWS;
	
	private LayoutParams mNormalLP;
	private LayoutParams mSelectedLP;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public HomeOperationsBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mCon = context;
		init();
	}

	public HomeOperationsBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mCon = context;
		init();
	}

	public HomeOperationsBar(Context context) {
		super(context);
		this.mCon = context;
		init();
	}
	
	public void upgradeBottomBtnName() {
	}

	private void init() {
		LayoutInflater lif = (LayoutInflater) mCon.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lif.inflate(R.layout.home_operation_bar, null);
		mNewsLo = (LinearLayout) view.findViewById(R.id.home_oper_xuanwen);
		mNewsIv = (ImageView) view.findViewById(R.id.home_oper_xuanwen_iv);
		mNewsTv = (TextView) view.findViewById(R.id.home_oper_xuanwen_tv);
		mSMsgLo = (LinearLayout) view.findViewById(R.id.home_oper_enterprise);
		mEnterpriseLogoIv = (ImageView) view.findViewById(R.id.enterprise_iv);
		menterpriseNameTv = (TextView) view.findViewById(R.id.enterprise_tv);
		mBBSLo = (LinearLayout) view.findViewById(R.id.home_oper_app_center);
		mAppIv = (ImageView) view.findViewById(R.id.home_oper_app_iv);
		mAppeTv = (TextView) view.findViewById(R.id.home_oper_app_tv);
		mPersonalCenterLo = (LinearLayout) view.findViewById(R.id.home_oper_personnal_center);
		mPersonalIv = (ImageView) view.findViewById(R.id.home_oper_personal_iv);
		mPersonalTv = (TextView) view.findViewById(R.id.home_oper_personal_tv);
		mMsgCountTv = (TextView) view.findViewById(R.id.home_oper_personal_msgcount_iv);
        mMsgEnterprise = (TextView) view.findViewById(R.id.tv_enterprise_msg);
        mMsgHomeOperApp = (TextView) view.findViewById(R.id.tv_home_oper_app_msg);
		mNewsLo.setOnClickListener(this);
		mSMsgLo.setOnClickListener(this);
		mBBSLo.setOnClickListener(this);
		mPersonalCenterLo.setOnClickListener(this);
		LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		addView(view, lllp);
		
		mNormalLP = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		mNormalLP.topMargin = 12;
		mSelectedLP = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		
		setSelectedBtnBackground(mSelectedType);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_oper_xuanwen:
			mSelectedType = BOTTOM_BUTTON_TYPE_NEWS;
			break;
		case R.id.home_oper_enterprise:
			mSelectedType = BOTTOM_BUTTON_TYPE_BROADCAST;
			break;
		case R.id.home_oper_app_center:
			mSelectedType = BOTTOM_BUTTON_TYPE_VIDEO;
			break;
		case R.id.home_oper_personnal_center:
			mSelectedType = BOTTOM_BUTTON_TYPE_PERSONAL;
			break;
		}
		mOnHomeBottomBarClickLs.onHomeBottomBarClick(mSelectedType);
		setSelectedBtnBackground(mSelectedType);
	}
	
	public void setSelectedItem(int selectedType) {
		mSelectedType = selectedType;
		setSelectedBtnBackground(selectedType);
	}
	
	/**
	 * 设置选中导航按钮的选中效果
	 * @param selectedBtnType
	 */
	private void setSelectedBtnBackground(int selectedBtnType) {
//		mNewsLo.setLayoutParams(mNormalLP);
//		mNewsLo.setBackgroundResource(R.drawable.saas_bottom_bar_bg);
		mNewsIv.setBackgroundResource(R.drawable.saas_bottom_enterpise_bg);
		mNewsTv.setTextColor(Color.GRAY);
		
//		mSMsgLo.setLayoutParams(mNormalLP);
//		mSMsgLo.setBackgroundResource(R.drawable.saas_bottom_bar_bg);
		mEnterpriseLogoIv.setBackgroundResource(R.drawable.saas_bottom_smsg_bg);
		menterpriseNameTv.setTextColor(Color.GRAY);
		
//		mBBSLo.setLayoutParams(mNormalLP);
//		mBBSLo.setBackgroundResource(R.drawable.saas_bottom_bar_bg);
		mAppIv.setBackgroundResource(R.drawable.saas_bottom_bbs_bg);
		mAppeTv.setTextColor(Color.rgb(141, 141, 141));
		
//		mPersonalCenterLo.setLayoutParams(mNormalLP);
//		mPersonalCenterLo.setBackgroundResource(R.drawable.saas_bottom_bar_bg);
		mPersonalIv.setBackgroundResource(R.drawable.saas_bottom_personal_bg);
		mPersonalTv.setTextColor(Color.GRAY);
		int selectedColor = Color.rgb(204, 0, 0);
		switch (selectedBtnType) {
		case BOTTOM_BUTTON_TYPE_NEWS:
//			mNewsLo.setLayoutParams(mSelectedLP);
//			mNewsLo.setBackgroundResource(R.drawable.saas_bottom_focus_bg);
			mNewsIv.setBackgroundResource(R.drawable.saas_bottom_enterpise_bg_focus);
			mNewsTv.setTextColor(selectedColor);
			break;
		case BOTTOM_BUTTON_TYPE_BROADCAST:
//			mSMsgLo.setLayoutParams(mSelectedLP);
//			mSMsgLo.setBackgroundResource(R.drawable.saas_bottom_focus_bg);
			mEnterpriseLogoIv.setBackgroundResource(R.drawable.saas_bottom_smsg_focus_bg);
			menterpriseNameTv.setTextColor(selectedColor);
			break;
		case BOTTOM_BUTTON_TYPE_VIDEO:
//			mBBSLo.setLayoutParams(mSelectedLP);
//			mBBSLo.setBackgroundResource(R.drawable.saas_bottom_focus_bg);
			mAppIv.setBackgroundResource(R.drawable.saas_bottom_bbs_bg_focus);
			mAppeTv.setTextColor(selectedColor);
			break;
		case BOTTOM_BUTTON_TYPE_PERSONAL:
//			mPersonalCenterLo.setLayoutParams(mSelectedLP);
//			mPersonalCenterLo.setBackgroundResource(R.drawable.saas_bottom_focus_bg);
			mPersonalIv.setBackgroundResource(R.drawable.saas_bottom_personal_bg_focus);
			mPersonalTv.setTextColor(selectedColor);
			break;
		}
	}
	
	public void setmEnterpriseLogo(String path) {
		ImageLoader.getInstance().displayImage(path, mEnterpriseLogoIv);
	}

	public void setMenterpriseName(String enterPriseName) {
		this.mEnterpriseName = enterPriseName;
		mNewsTv.setText(mEnterpriseName);
		setSelectedBtnBackground(mSelectedType);
	}
	
	public onHomeBottomBarClickListener getmOnHomeBottomBarClickLs() {
		return mOnHomeBottomBarClickLs;
	}

	public void setmOnHomeBottomBarClickLs(onHomeBottomBarClickListener mOnHomeBottomBarClickLs) {
		this.mOnHomeBottomBarClickLs = mOnHomeBottomBarClickLs;
	}
	
	public void setMsgCount(int count) {
		if(count > 0) {
			mMsgCountTv.setVisibility(View.VISIBLE);
			mMsgCountTv.setText("" + (count > 99 ? 99 : count));
		} else {
			mMsgCountTv.setVisibility(View.GONE);
		}
		
	}

    /**
     * 设置S信未读
     */
    public void setMsgEnterpriseCount(int count) {
        if(count > 0) {
            mMsgEnterprise.setVisibility(View.VISIBLE);
            mMsgEnterprise.setText("" + (count > 99 ? 99 : count));
        } else {
            mMsgEnterprise.setVisibility(View.GONE);
        }

    }

    /**
     * 设置社区未读
     */
    public void setMsgHomeOperAppCount(int count) {
        if(count > 0) {
            mMsgHomeOperApp.setVisibility(View.VISIBLE);
            mMsgHomeOperApp.setText("" + (count > 99 ? 99 : count));
        } else {
            mMsgHomeOperApp.setVisibility(View.GONE);
        }

    }

	public interface onHomeBottomBarClickListener{
		void onHomeBottomBarClick(int buttonType);
	}
}
