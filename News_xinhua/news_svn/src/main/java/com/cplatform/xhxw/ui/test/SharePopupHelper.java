package com.cplatform.xhxw.ui.test;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.cplatform.xhxw.ui.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 分享界面工具类
 * 
 * 参数parentLayout是界面最外层view（需要是RelativeLayout）
 * 
 * Created by liang on 14-1-18.
 */
public class SharePopupHelper {

//	private static final int SHARE_WEIBO = 1;
//	private static final int SHARE_QQ = 2;
//	private static final int SHARE_WEIXIN = 3;
//	private static final int SHARE_TWEIBO = 4;
//	private static final int SHARE_WEIXIN_CIRCLE = 5;
//
//	private Activity mContext;
//	private RelativeLayout mParentLayout;
//	private String mShareFrom; // 用户友盟统计分享来自于什么界面
//    private String mStationId; // 用户友盟统计
//    private String mActivityId; // 用户友盟统计
//    
//	PopupWindow mPopMenu;
//	private TextView mWeiboView, mQQView, mWeixinView, mQQWeiboView, mWeixinCircleView;
//	private Button mCancelButton;
//
//	// share
//	UMSocialService mController;
//
//	public SharePopupHelper(Activity mContext, RelativeLayout mParentLayout) {
//		this.mContext = mContext;
//		this.mParentLayout = mParentLayout;
//
//		initPopMenu();
//	}
//
//	public void setController(UMSocialService controller) {
//		this.mController = controller;
//
//		configShare();
//		configContent();
//	}
//
//    public void setController(UMSocialService controller,String content,String imageUrl){
//        setController(controller);
//        // 设置分享内容
//        if(!TextUtils.isEmpty(content)){
//            mController.setShareContent(content);
//        }
//
//        // 设置分享图片, 参数2为图片的url地址
//        if(!TextUtils.isEmpty(imageUrl)){
//            mController.setShareMedia(new UMImage(mContext,
//                    imageUrl));
//        }
//    }
//
//    public void setController(UMSocialService controller,String content,int imageRes){
//        setController(controller);
//        // 设置分享内容
//        if(!TextUtils.isEmpty(content)){
//            mController.setShareContent(content);
//        }
//
//        // 设置分享图片, 参数2为图片的url地址
//        mController.setShareMedia(new UMImage(mContext,
//                    imageRes));
//    }
//
//	private void configShare() {
//		mController.getConfig().supportQQPlatform(mContext, DownloaderUtil.SHARE_DOWN_ADDRESS);
//		mController.getConfig().setSsoHandler(new QZoneSsoHandler(mContext));
//		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//		configWeixin();
//	}
//
//	private void configWeixin() {
//		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//		String appID = "wxcf622fb7a68c1126";
//		// 微信图文分享必须设置一个url
//		String contentUrl = DownloaderUtil.SHARE_DOWN_ADDRESS;
//		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
//		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(mContext, appID, contentUrl);
//		wxHandler.setWXTitle("听说Android客户端");
//		// 支持微信朋友圈
//		UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(mContext, appID, contentUrl);
//		circleHandler.setCircleTitle("听说Android客户端");
//	}
//
//	private void configContent() {
//		mController.setShareContent("我是听说Android分享，说点什么好呢....");
//	}
//
//	private void initPopMenu() {
//		View mLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_share_popup_view, null);
//
//		mWeiboView = (TextView) mLayout.findViewById(R.id.tv_share_weibo);
//		mWeiboView.setOnClickListener(new ShareClickListener(SHARE_WEIBO));
//
//		mQQView = (TextView) mLayout.findViewById(R.id.tv_share_qq);
//		mQQView.setOnClickListener(new ShareClickListener(SHARE_QQ));
//
//		mWeixinView = (TextView) mLayout.findViewById(R.id.tv_share_weixin);
//		mWeixinView.setOnClickListener(new ShareClickListener(SHARE_WEIXIN));
//
//		mWeixinCircleView = (TextView) mLayout.findViewById(R.id.tv_share_weixin_circle);
//		mWeixinCircleView.setOnClickListener(new ShareClickListener(SHARE_WEIXIN_CIRCLE));
//
//		mQQWeiboView = (TextView) mLayout.findViewById(R.id.tv_share_tweibo);
//		mQQWeiboView.setOnClickListener(new ShareClickListener(SHARE_TWEIBO));
//
//		mCancelButton = (Button) mLayout.findViewById(R.id.btn_cancel);
//		mCancelButton.setOnClickListener(mCancelClickListener);
//
//		mPopMenu = new PopupWindow(mLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
//				true);
//		mPopMenu.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.recommend_two_dropdown_press));
//		mPopMenu.setOutsideTouchable(false);
//	}
//
//	public void showPopup(String shareFrom,String stationId,String activityId) {
//		this.mShareFrom = shareFrom;
//		this.mStationId = stationId;
//		this.mActivityId = activityId;
//		mPopMenu.showAtLocation(mParentLayout, Gravity.BOTTOM, 0, 0);
//	}
//
//	private void dismissPopup() {
//		if (mPopMenu != null && mPopMenu.isShowing()) {
//			mPopMenu.dismiss();
//		}
//	}
//
//	class ShareClickListener implements View.OnClickListener {
//
//		private int mType;
//
//		public ShareClickListener(int type) {
//			this.mType = type;
//		}
//
//		@Override
//		public void onClick(View v) {
//			switch (mType) {
//			case SHARE_WEIBO:
//				// ToastUtil.show(mContext,"微博分享成功");
//				doShare(SHARE_MEDIA.SINA,mType);
//				break;
//			case SHARE_QQ:
//				// ToastUtil.show(mContext,"QQ分享成功");
////				doShare(SHARE_MEDIA.QZONE,mType);
//				doShare(SHARE_MEDIA.QQ,mType);
//				break;
//			case SHARE_WEIXIN:
//				// ToastUtil.show(mContext,"微信分享成功");
//				doShare(SHARE_MEDIA.WEIXIN,mType);
//				break;
//			case SHARE_TWEIBO:
//				// ToastUtil.show(mContext,"腾讯微博分享成功");
//				doShare(SHARE_MEDIA.TENCENT,mType);
//				break;
//			case SHARE_WEIXIN_CIRCLE:
//				doShare(SHARE_MEDIA.WEIXIN_CIRCLE,mType);
//				break;
//			}
//			dismissPopup();
//		}
//	}
//
//	View.OnClickListener mCancelClickListener = new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			dismissPopup();
//		}
//	};
//
//	// final UMSocialService mController =
//	// UMServiceFactory.getUMSocialService("com.umeng.share",
//	// RequestType.SOCIAL);
//	//
//	private void doShare(SHARE_MEDIA type, final int mShareType) {
//		if (mController == null)
//			return;
//
//		mController.postShare(mContext, type, new SocializeListeners.SnsPostListener() {
//			@Override
//			public void onStart() {
//				//Toast.makeText(mContext, "开始分享.", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA share_media, int eCode, SocializeEntity socializeEntity) {
//				if (eCode == 200) {
//					//Toast.makeText(mContext, "分享成功.", Toast.LENGTH_SHORT).show();
//					statisticShare(mShareType);
//				} else {
//					String eMsg = "";
//					if (eCode == -101) {
//						eMsg = "没有授权";
//					}
//					//Toast.makeText(mContext, "分享失败[" + eCode + "] " + eMsg, Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//	}
//
//	private void statisticShare(int type) {
//		HashMap<String, String> map = UMengEventId.getAnalyticsMap(mStationId, mActivityId);
//		map.put(UMengEventId.SHARE_POSITION, mShareFrom);
//		switch (type) {
//		case SHARE_WEIBO:
//			map.put(UMengEventId.SHARE_TYPE, UMengEventId.TYPE_SHARE_SINA);
//			break;
//		case SHARE_QQ:
//			map.put(UMengEventId.SHARE_TYPE, UMengEventId.TYPE_SHARE_QQ);
//			break;
//		case SHARE_WEIXIN:
//			map.put(UMengEventId.SHARE_TYPE, UMengEventId.TYPE_SHARE_WEIXIN);
//			break;
//		case SHARE_WEIXIN_CIRCLE:
//			map.put(UMengEventId.SHARE_TYPE, UMengEventId.TYPE_SHARE_FRIENDS);
//			break;
//		case SHARE_TWEIBO:
//			map.put(UMengEventId.SHARE_TYPE, UMengEventId.TYPE_SHARE_TENCENT);
//			break;
//		}
//		MobclickAgent.onEvent(mContext, UMengEventId.CLIECK_SHARE_COUNT, map);
//	}
}
