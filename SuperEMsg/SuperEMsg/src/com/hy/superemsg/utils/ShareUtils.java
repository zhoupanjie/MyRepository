package com.hy.superemsg.utils;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.text.TextUtils;

import com.hy.superemsg.rsp.AnimationContentDetail;
import com.hy.superemsg.rsp.GameContentDetail;
import com.hy.superemsg.rsp.NewsContentDetail;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

public class ShareUtils {
	public static void shareByWeibo(final Context ctx, final String content) {
		final String access_token = "access_token";
		final String expires_in = "expires_in";
		Weibo weibo = Weibo.getInstance("2708325179",
				"http://weibo.com/u/1764115503");
		weibo.authorize(ctx, new WeiboAuthListener() {

			@Override
			public void onWeiboException(WeiboException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(WeiboDialogError arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Bundle values) {
				Oauth2AccessToken token = new Oauth2AccessToken(values
						.getString(access_token), values.getString(expires_in));
				StatusesAPI api = new StatusesAPI(token);
				try {
					/*
					 * 发布文字微博
					 */
					api.update(content, "0", "0", new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(WeiboException arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onComplete(String arg0) {
							String result = arg0;
						}
					});
				} catch (Exception e) {
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void shareByWeibo(Context ctx, NewsContentDetail news) {
		// 【标题】+链接
		shareByWeibo(ctx, "【" + news.newstitle + "】" + news.newscontent);
	}

	public static void shareByWeibo(Context ctx, AnimationContentDetail cartoon) {
		// 萌妹正太御姐！二次元，超次元！#超级E信#好多好多的动漫免费看！我只转发不说话！详情→链接
		shareByWeibo(ctx, "萌妹正太御姐！二次元，超次元！#超级E信#好多好多的动漫免费看！我只转发不说话！详情→"
				+ cartoon.amcoverpicurl);
	}

	public static void shareByWeibo(Context ctx, GameContentDetail game) {
		// 我刚刚在#超级E信#下载了新游戏“游戏名称”，妈妈再也不担心我无聊了！小伙伴们快来一起玩吧：链接
		shareByWeibo(ctx, "我刚刚在#超级E信#下载了新游戏\"" + game.gamename
				+ "\",妈妈再也不担心我无聊了！小伙伴们快来一起玩吧：" + game.gamefileurl);
	}

	private static final String WX_APP_ID = "";

	public static void shareByWeixin(Context ctx, String text) {
		IWXAPI api = WXAPIFactory.createWXAPI(ctx, WX_APP_ID, false);
		api.registerApp(WX_APP_ID);
		if (text == null || text.length() == 0) {
			return;
		}

		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;

		// 调用api接口发送数据到微信
		api.sendReq(req);
	}

	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	public static void shareByWeibo(final Context context, final String text,
			final Bitmap bmp) {
		final String access_token = "access_token";
		final String expires_in = "expires_in";
		Weibo weibo = Weibo.getInstance("2708325179",
				"http://weibo.com/u/1764115503");
		weibo.authorize(context, new WeiboAuthListener() {

			@Override
			public void onWeiboException(WeiboException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(WeiboDialogError arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Bundle values) {
				Oauth2AccessToken token = new Oauth2AccessToken(values
						.getString(access_token), values.getString(expires_in));
				StatusesAPI api = new StatusesAPI(token);
				try {
					/*
					 * 发布图片微博
					 */
					bmp.compress(CompressFormat.JPEG, 100, context
							.openFileOutput("tempImage", Context.MODE_PRIVATE));
					String path = context.getFilesDir() + "/tempImage";
					String content = text;
					if (TextUtils.isEmpty(text)) {
						content = "空彩信";
					}
					api.upload(content, path, "0", "0", new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub
							System.out.println(arg0);
						}

						@Override
						public void onError(WeiboException arg0) {
							// TODO Auto-generated method stub
							System.out.println(arg0);

						}

						@Override
						public void onComplete(String arg0) {
							// TODO Auto-generated method stub
							System.out.println(arg0);
						}
					});
				} catch (Exception e) {
					System.out.println(e);
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});
	}

}
