package com.cplatform.xhxw.ui.ui.base.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.CheckDrawPrizeRequest;
import com.cplatform.xhxw.ui.http.net.CheckDrawPrizeResponse;
import com.cplatform.xhxw.ui.http.net.CheckDrawPrizeResponse.DrawPrizeData;
import com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize;
import com.cplatform.xhxw.ui.ui.base.ScreenManager;
import com.cplatform.xhxw.ui.ui.web.RedenvelopesCallBack;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.OauthKey;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.wbtech.ums.UmsAgent;
/**
 * 
 * @ClassName ShareActionSheet 
 * @Description TODO 中文分享窗口
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年5月13日 下午1:53:56 
 * @Mender zxe
 * @Modification 2015年5月13日 下午1:53:56 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class ShareActionSheetGame extends PopupWindow {

	private static final String TAG = ShareActionSheetGame.class.getSimpleName();

	public static final int REQUEST_CODE_SMS = 10101;
	public static final int REQUEST_CODE_EMAIL = 10102;

	private Activity context;
	private boolean isDismissed = false;
	private static ViewGroup layoutContent;
	private static String title;

	private static UMSocialService mController;
	private static OnMoreListener more_lis;

	private static RelativeLayout detail_more_function_lo;
	private static TextView detail_more_fun_night_model_tv;
	private static ImageView mSmallTextBtn;
	private static ImageView mMiddleTextBtn;
	private static ImageView mBigTextBtn;
	private static ImageView mDisplayModelIv;

	private static boolean isShowCollect;
	private static boolean isShowShare;
	private static boolean isShowMore;

	private static LinearLayout collect_layout;
	private static ImageView collect_image;
	private static TextView collect_tv;

	private static String mShareImageUrl;

	private static boolean isTop;
	// private String mContent; // 分享的内容
	private static int share_type;

	private static RedenvelopesCallBack callback;
	private boolean isShare = false;
	private static UMediaObject umo=null;
	private ShareActionSheetGame(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
	}

	public static ShareActionSheetGame getInstance(final Activity context,
			OnMoreListener more, final String title, final String content,
			final String clickUrl, boolean showCollect, boolean showShare,
			boolean showMore, String imageUrl,
			final boolean isShouldCombineUrl, int share,
			RedenvelopesCallBack redenvelopesCallBack,UMediaObject uo) {
		umo=uo;
		LinearLayout contentView = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.action_sheet, null);
		more_lis = more;
		isShowCollect = showCollect;
		isShowShare = showShare;
		isShowMore = showMore;
		mShareImageUrl = imageUrl;
		share_type = share;
		callback = redenvelopesCallBack;

		if (!isShowMore && isShowShare) {
			contentView.setGravity(Gravity.BOTTOM);
		} else {
			contentView.setGravity(Gravity.TOP);
		}

		if (callback != null) {
			contentView.setGravity(Gravity.TOP);
		}

		final ShareActionSheetGame actionSheet = new ShareActionSheetGame(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, true);

		actionSheet.context = context;
		actionSheet.title = title;
		actionSheet.layoutContent = (ViewGroup) contentView
				.findViewById(R.id.layout_content);
		int layout_id = 0;
		if (isShowCollect) {
			layout_id = R.layout.action_share_has_collect;
		} else {
			layout_id = R.layout.action_share_no_collect;
		}
		View collect_layout = LayoutInflater.from(context).inflate(layout_id,
				null);
		layoutContent.addView(collect_layout, 0);
		if (!isShowShare) {
			layoutContent.getChildAt(0).setVisibility(View.GONE);
		}
		contentView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					actionSheet.dismiss();
				}
				return true;
			}
		});
		actionSheet.layoutContent
				.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						return true;
					}
				});

		initMoreFuncPopup(contentView);
		changeDisplayModel();

		actionSheet.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		actionSheet.setAnimationStyle(R.style.action_sheet_no_animation);

		/** 分享 */
		actionSheet.mController = UMServiceFactory.getUMSocialService(
				"com.umeng.share");

		// 人人
		contentView.findViewById(R.id.share_renren).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// mController.setAppWebSite(SHARE_MEDIA.RENREN,
						// "http://www.umeng.com/social");
						mController.getConfig().supportAppPlatform(context,
								SHARE_MEDIA.RENREN, content, true);
						actionSheet.doShare(context, SHARE_MEDIA.RENREN,
								content, clickUrl,umo);
						actionSheet.dismiss();
					}
				});

		// 新浪
		contentView.findViewById(R.id.share_sina).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmsShare(context, "新浪");
						if (checkNetWork(context, actionSheet) == false) {
							return;
						}
						mController.getConfig().setSsoHandler(
								new SinaSsoHandler());// 新浪
						String weiboContent = "【" + content + "】" + clickUrl
								+ "（来自新华炫闻客户端）";
						if (!isShouldCombineUrl) {
							weiboContent = "【" + content + "】（来自新华炫闻客户端）";
						}
						actionSheet.doShare(context, SHARE_MEDIA.SINA,
								weiboContent, clickUrl,umo);
						actionSheet.dismiss();
					}
				});

		// QQ
		contentView.findViewById(R.id.share_qq).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmsShare(context, "QQ");
						configQQQZonePlatform(context);
						actionSheet.doShare(context, SHARE_MEDIA.QQ, content,
								clickUrl,umo);
						actionSheet.dismiss();
					}
				});

		// 微信
		contentView.findViewById(R.id.share_weixin).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmsShare(context, "微信");
						if (checkNetWork(context, actionSheet) == false) {
							return;
						}
						configWeixin(context, clickUrl);
						actionSheet.doShare(context, SHARE_MEDIA.WEIXIN,
								content, clickUrl,umo);
						actionSheet.dismiss();
					}
				});

		// 朋友圈
		contentView.findViewById(R.id.share_friends).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmsShare(context, "朋友圈");
						if (checkNetWork(context, actionSheet) == false) {
							return;
						}
						configWeixin(context, clickUrl);
						actionSheet.doShare(context, SHARE_MEDIA.WEIXIN_CIRCLE,
								content, clickUrl,umo);
						actionSheet.dismiss();
					}
				});

		// 微博
		contentView.findViewById(R.id.share_qq_weibo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmsShare(context, "微博");
						if (checkNetWork(context, actionSheet) == false) {
							return;
						}
						mController.getConfig().setSsoHandler(
								new TencentWBSsoHandler());// 腾讯微博
						String weiboContent = "【" + content + "】" + clickUrl
								+ "（来自新华炫闻客户端）";
						if (!isShouldCombineUrl) {
							weiboContent = "【" + content + "】（来自新华炫闻客户端）";
						}
						actionSheet.doShare(context, SHARE_MEDIA.TENCENT,
								weiboContent, clickUrl,umo);
						actionSheet.dismiss();
					}
				});

		// 短信
		contentView.findViewById(R.id.share_message).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmsShare(context, "短信");
						if (checkNetWork(context, actionSheet) == false) {
							return;
						}

						String smsContent = content + clickUrl;
						if (!isShouldCombineUrl) {
							smsContent = content;
						}

						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setType("vnd.android-dir/mms-sms");
						// intent.putExtra("address", cbFromPhone.getTel());
						intent.putExtra("sms_body", smsContent);
						/* startActivityForResult(intent, 1); */
						// context.startActivity(intent);
						context.startActivityForResult(intent, REQUEST_CODE_SMS);
						PreferencesManager.saveDrawPrizeUrl(App.CONTEXT,
								clickUrl);
						actionSheet.dismiss();
					}
				});

		// 邮件
		contentView.findViewById(R.id.share_email).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmsShare(context, "邮件");
						if (checkNetWork(context, actionSheet) == false) {
							return;
						}
						String mailContent = content + clickUrl;
						if (!isShouldCombineUrl) {
							mailContent = content;
						}
						Uri uri = Uri.parse("mailto:");
						Intent intent = new Intent(
								android.content.Intent.ACTION_SENDTO, uri);
						intent.putExtra(Intent.EXTRA_TEXT, mailContent);
						context.startActivityForResult(intent,
								REQUEST_CODE_EMAIL);
						PreferencesManager.saveDrawPrizeUrl(App.CONTEXT,
								clickUrl);
						actionSheet.dismiss();
					}
				});

		// 复制链接
		contentView.findViewById(R.id.share_copy_link).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmsShare(context, "复制链接");
						// 得到剪贴板管理器
						ClipboardManager cmb = (ClipboardManager) context
								.getSystemService(Context.CLIPBOARD_SERVICE);
						cmb.setText(clickUrl);
						Toast.makeText(context.getApplicationContext(),
								"链接已复制", Toast.LENGTH_SHORT).show();
						actionSheet.dismiss();
					}
				});

		return actionSheet;
	}

	public static void UmsShare(Context c, String plat) {
		if (share_type == 1) {
			// 分享应用统计
			UmsAgent.onEvent(c, StatisticalKey.share_app,
					new String[] { StatisticalKey.key_plat },
					new String[] { plat });
		} else if (share_type == 0) {
			// 分享新闻统计
			UmsAgent.onEvent(c, StatisticalKey.share_news, new String[] {
					StatisticalKey.key_channelid, StatisticalKey.key_newsid,
					StatisticalKey.key_plat }, new String[] { App.channel_id,
					App.news_id, plat });
		}
	}

	public void setAnimation() {
		Animation animation = null;
		if (isTop) {
			animation = AnimationUtils.loadAnimation(context,
					R.anim.actionsheet_top_in);
		} else {
			animation = AnimationUtils.loadAnimation(context,
					R.anim.actionsheet_bottom_in);
		}
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		layoutContent.startAnimation(animation);
	}

	/**
	 * 显示
	 */
	public void show() {
		showAtLocation((context).getWindow().getDecorView(), Gravity.BOTTOM, 0,
				0);
		isTop = false;
		setAnimation();
	}

	@Override
	public void showAsDropDown(View anchor) {
		isTop = true;
		setAnimation();
		super.showAsDropDown(anchor);
	}

	@Override
	public void dismiss() {
		if (isDismissed) {
			return;
		}
		isDismissed = true;
		Animation animation = null;
		if (isTop) {
			animation = AnimationUtils.loadAnimation(context,
					R.anim.actionsheet_top_out);
		} else {
			animation = AnimationUtils.loadAnimation(context,
					R.anim.actionsheet_bottom_out);
		}
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				layoutContent.setVisibility(View.INVISIBLE);
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						try {
							superDismiss();
						} catch (Exception e) {

						}
					}
				});
			}
		});
		if (callback != null) {
			if (isShare) {
				callback.share();
			}
			callback = null;
		}
		layoutContent.startAnimation(animation);
		more_lis = null;
		context = null;
	}

	/**
	 * 隐藏
	 */
	public void superDismiss() {
		super.dismiss();
	}

	/**
	 * 配置微信分享
	 * 
	 * @param context
	 * @param url
	 */
	private static void configWeixin(Activity context, String url) {
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context,
				OauthKey.KEY_WEIXIN_ID, OauthKey.KEY_WEIXIN_SECRET);
		wxHandler.setTargetUrl(url);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context,
				OauthKey.KEY_WEIXIN_ID, OauthKey.KEY_WEIXIN_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.setTargetUrl(url);
		wxCircleHandler.addToSocialSDK();
	}

	private static void configQQQZonePlatform(Activity context) {
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context,
				OauthKey.KEY_QQ_ID, OauthKey.KEY_QQ_SECRET);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
				OauthKey.KEY_QQ_ID, OauthKey.KEY_QQ_SECRET);
		qZoneSsoHandler.addToSocialSDK();
	}


	/**
	 * 
	 * 
	 * @param context
	 * @param type
	 *            
	 * @param content
	 *            
	 */
	/**
	 * 
	 * @Name doShare 
	 * @Description TODO 分享
	 * @param context
	 * @param type     平台类型
	 * @param content  分享内容
	 * @param url
	 * @param uo      //网络图片new UMImage(getActivity(), "http://www.baidu.com/img/bdlogo.png")//本地图片new UMImage(getActivity(), R.drawable.ic_share)
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月6日 下午2:26:41
	*
	 */
	private void doShare(Activity context, SHARE_MEDIA type, String content,
			final String url,UMediaObject uo) {
		if (mController == null)
			return;
		if (mShareImageUrl == null) {
			mController
					.setShareMedia(uo);
		} else {
			mController.setShareMedia(new UMImage(context, mShareImageUrl));
		}
		mController.setShareContent(content);

		try {
			isShare = true;
			mController.postShare(context, type,
					new SocializeListeners.SnsPostListener() {
						@Override
						public void onStart() {
							LogUtil.d(TAG, "开始分享");
						}

						@Override
						public void onComplete(SHARE_MEDIA share_media,
								int eCode, SocializeEntity socializeEntity) {
							LogUtil.d(TAG, "开始状态::" + (eCode == 200) + " code:"
									+ eCode);
							if (eCode == 200) {
								checkDrawPrize(url);
							}
						}
					});
		} catch (Exception e) {
			LogUtil.e(TAG, "分享失败::" + e);
		}

	}

	public static void checkDrawPrize(String url) {
		CheckDrawPrizeRequest request = new CheckDrawPrizeRequest();
		request.setUrl(url);
		APIClient.checkDrawPrizeCount(request, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, String content) {
				CheckDrawPrizeResponse response = null;
				try {
					response = new Gson().fromJson(content,
							CheckDrawPrizeResponse.class);
					ResponseUtil.checkObjResponse(response);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (response != null && response.isSuccess()) {
					DrawPrizeData data = response.getData();
					dealWithDrawPrizeData(data);
				}
			}
		});
	}

	public static void dealWithDrawPrizeData(DrawPrizeData data) {
		if (data.getCount() > 0) {
			if (ScreenManager.getScreenManager().isAnyActivityInForeGround()) {
				Activity shownActivity = ScreenManager.getScreenManager()
						.getShownActivity();
				if (shownActivity != null) {
					String className = shownActivity.getClass().getName();
					try {
						className = className.substring(className
								.lastIndexOf(".") + 1);
						LogUtil.e(TAG, "------" + className);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (className != null
							&& className.equals("WebViewActivity")
							&& WebViewActivity.isDrawPrize) {
						Intent it = new Intent();
						it.setAction(WebViewActivity.ACTION_SHARE_DONE);
						shownActivity.sendBroadcast(it);
					} else {
						new CheckDrawPrizeAlertDialog(shownActivity,
								data.getUrl()).show();
					}
				}
			} else {
				PreferencesManager.setDrawPrizeAlert(true);
				PreferencesManager.saveDrawPrizeUrl(App.CONTEXT, data.getUrl());
			}
		}
	}

	/**
	 * 更多功能弹出框初始化
	 */
	private static void initMoreFuncPopup(View contentView) {
		detail_more_function_lo = (RelativeLayout) contentView
				.findViewById(R.id.detail_more_function_lo);
		if (isShowMore) {
			detail_more_function_lo.setVisibility(View.VISIBLE);
		} else {
			detail_more_function_lo.setVisibility(View.GONE);
		}
		mSmallTextBtn = (ImageView) contentView
				.findViewById(R.id.text_size_small);
		mMiddleTextBtn = (ImageView) contentView
				.findViewById(R.id.text_size_middle);
		mBigTextBtn = (ImageView) contentView.findViewById(R.id.text_size_big);
		mDisplayModelIv = (ImageView) contentView
				.findViewById(R.id.detail_more_fun_night_model_iv);
		detail_more_fun_night_model_tv = (TextView) contentView
				.findViewById(R.id.detail_more_fun_night_model_tv);
		
		if (isShowMore) {
			mSmallTextBtn.setOnClickListener(mOnClickListener);
			mMiddleTextBtn.setOnClickListener(mOnClickListener);
			mBigTextBtn.setOnClickListener(mOnClickListener);
			mDisplayModelIv.setOnClickListener(mOnClickListener);
		}
		if (isShowCollect) {
			collect_layout = (LinearLayout) contentView
					.findViewById(R.id.btn_more_func);
			collect_image = (ImageView) collect_layout
					.findViewById(R.id.imageView1);
			collect_tv = (TextView) collect_layout.findViewById(R.id.textView1);
			collect_layout.setOnClickListener(mOnClickListener);
		}
		changeTextSize();
		initDisplayModel();
	}

	private static void initDisplayModel() {
		int model = App.getDispalyModel();
		if (model == Constants.DISPLAY_MODEL_DAY) {
			mDisplayModelIv.setBackgroundResource(R.drawable.detail_day);
			detail_more_fun_night_model_tv.setText(R.string.day_model);
		} else {
			mDisplayModelIv.setBackgroundResource(R.drawable.detail_night);
			detail_more_fun_night_model_tv.setText(R.string.night_model);
		}
	}

	public static void changeTextSize() {
		if (isShowMore) {
			int textSize = Constants.getNewsDetTextSize();
			mSmallTextBtn.setImageResource(R.drawable.font_text_bg);
			mMiddleTextBtn.setImageResource(R.drawable.font_text_bg);
			mBigTextBtn.setImageResource(R.drawable.font_text_bg);
			switch (textSize) {
			case NewsDetTextSize.SMALL:
				mSmallTextBtn.setImageResource(R.drawable.font_text_bg_focus);
				break;
			case NewsDetTextSize.MIDDLE:
				mMiddleTextBtn.setImageResource(R.drawable.font_text_bg_focus);
				break;
			case NewsDetTextSize.BIG:
				mBigTextBtn.setImageResource(R.drawable.font_text_bg_focus);
				break;
			}
		}
	}

	private static OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (more_lis != null) {
				if (v.getId() == R.id.detail_comment_editt) {
					// 评论编辑框点击
					more_lis.onEditTextClick();
				} else if (v.getId() == R.id.detail_comment_sendbtn) {
					// 评论提交或者进入评论页
					more_lis.onClickCommentBtn();
				} else if (v.getId() == R.id.detail_comment_expression_btn) {
					// 表情区域收起显示
					more_lis.onClickExprBtn();
				} else if (v.getId() == R.id.text_size_big) {
					// 选择大文字
					more_lis.changeTextSize(NewsDetTextSize.BIG);
					changeTextSize();
				} else if (v.getId() == R.id.text_size_middle) {
					// 选择中文字
					more_lis.changeTextSize(NewsDetTextSize.MIDDLE);
					changeTextSize();
				} else if (v.getId() == R.id.text_size_small) {
					// 选择小文字
					more_lis.changeTextSize(NewsDetTextSize.SMALL);
					changeTextSize();
				} else if (v.getId() == R.id.detail_more_fun_night_model_iv) {
					// 日间、夜间模式切换
					more_lis.changeDisplayModel();
					changeDisplayModel();
				} else if (v.getId() == R.id.detail_sign_btn) {
					// 圈阅新闻
					more_lis.showSignNewsAlert();
				} else if (v.getId() == R.id.btn_more_func) {
					// 收藏
					more_lis.collect();
				}

			}
		}
	};

	public static void changeCollectType(boolean isCollect) {
		if (collect_image != null) {
			if (isCollect) {
				collect_image
						.setImageResource(R.drawable.btn_new_start_success);
				collect_tv.setText("取消收藏");
			} else {
				collect_image.setImageResource(R.drawable.icon_star);
				collect_tv.setText("收藏");
			}
		}
	}

	public static void changeDisplayModel() {
		if (isShowMore) {
			int model = App.getDispalyModel();
			model = (model == Constants.DISPLAY_MODEL_DAY ? Constants.DISPLAY_MODEL_NIGHT
					: Constants.DISPLAY_MODEL_DAY);
			if (model == Constants.DISPLAY_MODEL_DAY) {
				mDisplayModelIv.setBackgroundResource(R.drawable.detail_day);
				detail_more_fun_night_model_tv.setText(R.string.day_model);
			} else {
				mDisplayModelIv.setBackgroundResource(R.drawable.detail_night);
				detail_more_fun_night_model_tv.setText(R.string.night_model);
			}
		}
	}

	/**
	 * 检查网络状态
	 */
	private static boolean checkNetWork(Activity context,
			ShareActionSheetGame actionSheet) {
		if (!DeviceConfig.isNetworkAvailable(context)) {
			Toast.makeText(context, "您的网络不可用,请检查网络连接...", Toast.LENGTH_SHORT)
					.show();
			actionSheet.dismiss();
			return false;
		}
		return true;
	}
}
