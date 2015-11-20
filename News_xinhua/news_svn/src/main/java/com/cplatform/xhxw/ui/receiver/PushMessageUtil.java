package com.cplatform.xhxw.ui.receiver;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.PushCancelRequest;
import com.cplatform.xhxw.ui.http.net.PushOnlineRequest;
import com.cplatform.xhxw.ui.http.responseType.NewsType;
import com.cplatform.xhxw.ui.model.FeedssPushMessage;
import com.cplatform.xhxw.ui.model.PushInfoTmp;
import com.cplatform.xhxw.ui.model.PushMessage;
import com.cplatform.xhxw.ui.model.PushNotification;
import com.cplatform.xhxw.ui.ui.detailpage.NewsPageActivity;
import com.cplatform.xhxw.ui.ui.detailpage.VideoPlayerActivity;
import com.cplatform.xhxw.ui.ui.picShow.PicShowActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.ui.web.newscollect.NewsCollectWebActivity;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NotificationUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * push工具类 Created by cy-love on 14-4-9.
 */
public class PushMessageUtil {

	private static final String TAG = PushMessageUtil.class.getSimpleName();

	public enum PushChannelType {
		baidu, zhongpin
	}

	/**
	 * 绑定服务器
	 *
	 * @param context
	 *            上下文
	 * @param userId
	 *            用户id(无用户id采用设备id)
	 * @param deviceToken
	 *            设备唯一标示（push绑定返回）
	 * @param channelId
	 *            百度push参数
	 * @param push_channel
	 *            push平台
	 */
	public static void bindServer(Context context, final String userId,
			final String deviceToken, final String channelId,
			final String push_channel) {
		PushInfoTmp tmp = new PushInfoTmp();
		tmp.setUserId(userId);
		tmp.setDeviceToken(deviceToken);
		tmp.setChannelId(channelId);
		tmp.setPush_channel(push_channel);
		Constants.sPushInfoTmp = tmp;
		APIClient.pushOnline(new PushOnlineRequest(context, userId,
				deviceToken, channelId, push_channel),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						BaseResponse response;
						try {
							response = new Gson().fromJson(content,
									BaseResponse.class);
							ResponseUtil.checkObjResponse(response);
						} catch (Exception e) {
							LogUtil.w(TAG, e);
							return;
						}
						if (response.isSuccess()) {
							LogUtil.d(TAG, "服务器绑定push成功！ userId=" + userId
									+ "  deviceToken=" + deviceToken
									+ "  channelId=" + channelId);
							App.getPreferenceManager().setPushDeviceToken(
									deviceToken);
						} else {
							LogUtil.e(TAG, "服务器绑定push失败！msg:" + content);
						}
					}
				});
	}

	/**
	 * 解绑
	 *
	 * @param context
	 *            上下文
	 * @param userId
	 *            用户id(无用户id采用设备id)
	 * @param deviceToken
	 *            设备唯一标示（push绑定返回
	 */
	public static void unbindServer(Context context, final String userId,
			final String deviceToken) {
		APIClient.pushCancel(
				new PushCancelRequest(context, userId, deviceToken),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						BaseResponse response;
						try {
							response = new Gson().fromJson(content,
									BaseResponse.class);
							ResponseUtil.checkObjResponse(response);
						} catch (Exception e) {
							LogUtil.w(TAG, e);
							return;
						}
						if (response.isSuccess()) {
							LogUtil.d(TAG, "服务器解绑push成功！ userId=" + userId
									+ "  deviceToken=" + deviceToken);
							App.getPreferenceManager().setPushDeviceToken(null);
						} else {
							LogUtil.e(TAG,
									"服务器解绑push失败！ msg:" + response.getMsg());
						}
					}
				});
	}

	/**
	 * 接收透传消息的函数。
	 *
	 * @param context
	 *            上下文
	 * @param message
	 *            推送的消息
	 * @param customContentString
	 *            自定义内容,为空或者json字符串
	 */
	public static void message(Context context, String message,
			String customContentString, PushChannelType pushType) {
		String messageString = "透传消息 message=\"" + message
				+ "\" customContentString=" + customContentString;
		LogUtil.d(TAG, messageString);
		switch (pushType) {
		case zhongpin:
			parsingOfFeedssPush(context, message, customContentString);
			break;
		case baidu:
			parsingOfBaiduPush(context, message);
			break;
		}
	}

	/** 解析 feedss push数据 */
	private static void parsingOfFeedssPush(Context context, String message,
			String customContentString) {
		FeedssPushMessage json;
		try {
			json = new Gson().fromJson(customContentString,
					FeedssPushMessage.class);
			ResponseUtil.checkObjResponse(json);
		} catch (Exception e) {
			LogUtil.e(TAG, "json解析错误::" + customContentString);
			return;
		}
		if (json == null) {
			LogUtil.e(TAG, "json解析错误::" + customContentString);
			return;
		}
		switch (json.getMsgType()) {
		case 1: // 新闻消息
			if (!App.getPreferenceManager().isOpenPush()) {
				return;
			}
			boolean isEnterprise = (json.getSiteId() == null || json
					.getSiteId().length() == 0) ? false : true;
			showNotifyNews(context, json.getNewsType(), json.getNewsId(),
					isEnterprise, message);
			break;
		}
	}

	/** 解析百度push数据 */
	private static void parsingOfBaiduPush(Context context, String message) {
		if (!App.getPreferenceManager().isOpenPush()) {
			return;
		}
		PushMessage json;
		try {
			json = new Gson().fromJson(message, PushMessage.class);
			ResponseUtil.checkObjResponse(json);
		} catch (Exception e) {
			LogUtil.e(TAG, "json解析错误::" + message);
			return;
		}
		boolean isEnterprise = (json.getSiteId() == null || json.getSiteId()
				.length() == 0) ? false : true;
		showNotifyNews(context, json.getNewsType(), json.getNewsId(),
				isEnterprise, json.getDescription());
	}

	/**
	 * 通知栏显示新闻
	 * 
	 * @param context
	 * @param newsType
	 *            类型
	 * @param newsId
	 *            新闻id
	 * @param isEnterprise
	 * @param text
	 *            内容
	 */
	private static void showNotifyNews(Context context, int newsType,
			String newsId, boolean isEnterprise, String text) {

		Intent intent = getStartActivityIntent(context, newsType, newsId,
				isEnterprise);
		intent.putExtra("isPush", true);

		App.getPreferenceManager().messageNewCountAdd();
		LocalBroadcastManager.getInstance(context).sendBroadcast(
				new Intent(Actions.ACTION_MESSAGE_NEW_COUNT_CHANGE));

		// if (!ActivityUtil.isApplicationBroughtToBackground(context)) {
		// showDialog(context, text, intent, pushType);
		// } else {
		//
		// }

		int messageId = NotificationUtil.getNewMessageId();
		PendingIntent pendingIntent = PendingIntent.getActivity(App.CONTEXT,
				messageId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationUtil.notifyStatus(context, messageId,
				context.getString(R.string.app_name), text, pendingIntent,
				R.drawable.ic_notification_icon);

		UmsAgent.init(context, HttpClientConfig.UMSAGENT_BASE_URL, 1);
		UmsAgent.onEvent(context, StatisticalKey.push_receive,
				new String[] { StatisticalKey.key_newsid },
				new String[] { newsId });
	}

	private static boolean isShowAlert;

	private static synchronized void showDialog(final Context context,
			String title, final Intent intent, final PushChannelType pushType) {
		if (isShowAlert) {
			return;
		}
		isShowAlert = true;
		AlertDialog.Builder builder;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			builder = new AlertDialog.Builder(context);
		} else {
			builder = new AlertDialog.Builder(context,
					AlertDialog.THEME_HOLO_LIGHT);
		}
		builder.setTitle(R.string.news_push)
				.setMessage(title)
				.setPositiveButton(R.string.view_details,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								pushDetailEvent(context, pushType);
								App.CONTEXT.startActivity(intent);
							}
						}).setNegativeButton(R.string.cancel, null);
		AlertDialog alert = builder.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				isShowAlert = false;
			}
		});
		alert.show();
	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
	 *
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */
	public static void notificationClicked(Context context, String title,
			String description, String customContentString, PushChannelType type) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		LogUtil.d(TAG, notifyString);
		PushNotification json;
		try {
			json = new Gson().fromJson(customContentString,
					PushNotification.class);
			ResponseUtil.checkObjResponse(json);
		} catch (Exception e) {
			LogUtil.e(TAG, "json解析错误::" + customContentString);
			return;
		}
		pushOpenEvent(context, type);
		boolean isEnterprise = (json.getSiteId() == null || json.getSiteId()
				.length() == 0) ? false : true;
		Intent intent = getStartActivityIntent(context, json.getNewsType(),
				json.getNewsId(), isEnterprise);

		UmsAgent.init(context, HttpClientConfig.UMSAGENT_BASE_URL, 1);
		UmsAgent.onEvent(context, StatisticalKey.push_detail,
				new String[] { StatisticalKey.key_newsid },
				new String[] { json.getNewsId() });
		context.getApplicationContext().startActivity(intent);

	}

	/**
	 * 获得新闻类型对应的Activity
	 *
	 * @param context
	 * @param newsType
	 *            新闻类型
	 * @param newsId
	 *            新闻id
	 */
	private static Intent getStartActivityIntent(Context context, int newsType,
			String newsId, boolean isEnterprise) {
		Intent intent;
		switch (newsType) {
		case NewsType.VIDEO:
			intent = VideoPlayerActivity.getIntent(
					context.getApplicationContext(), newsId);
			break;
		case NewsType.ATLAS:
			intent = PicShowActivity.getIntent(context.getApplicationContext(),
					newsId, true, isEnterprise, "");
			break;
		case NewsType.LIVE:
			intent = WebViewActivity.getLiveNewsIntent(context, newsId, "",
					true);
			break;
		case NewsType.COLLECT:
			intent = NewsCollectWebActivity.getCollectIntent(context, newsId,
					"", true);
			break;
		case NewsType.DRAW_PRIZE:
			intent = WebViewActivity.getDrawPrizeIntent(context, null, newsId);
			break;
		default:
			intent = NewsPageActivity.getIntent(
					context.getApplicationContext(), newsId, isEnterprise);
			break;
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	/**
	 * 后台运行push被打开统计
	 *
	 * @param pushType
	 *            平台类型
	 */
	private static void pushOpenEvent(Context context, PushChannelType pushType) {
		switch (pushType) {
		case baidu:
			MobclickAgent.onEvent(context, StatisticalKey.push_open);
			break;
		case zhongpin:
			MobclickAgent.onEvent(context, StatisticalKey.push_open_zp);
			break;
		}
	}

	/**
	 * 运行中push被打开事件统计
	 *
	 * @param pushType
	 *            平台类型
	 */
	private static void pushDetailEvent(Context context,
			PushChannelType pushType) {
		switch (pushType) {
		case baidu:
			MobclickAgent.onEvent(context, StatisticalKey.push_detail);
			break;
		case zhongpin:
			MobclickAgent.onEvent(context, StatisticalKey.push_detail_zp);
			break;
		}
	}
}
