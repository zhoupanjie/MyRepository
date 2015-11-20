package com.cplatform.xhxw.ui.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.cplatform.xhxw.ui.ui.base.view.OnMoreListener;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheet;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheetEn;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheetGame;
import com.cplatform.xhxw.ui.ui.base.view.ShareVideoActionSheet;
import com.cplatform.xhxw.ui.ui.web.RedenvelopesCallBack;
import com.umeng.socialize.media.UMImage;

/**
 * 分享 Created by cy-love on 14-1-20.
 */
public class ShareUtil {

	public static boolean isShow = true;

	public static RedenvelopesCallBack callback;// 红包的回调对象

	/**
	 * 分享类型 0:新闻 1:应用
	 */
	public static int share_type = 0;

	public static void sendPhotoIntent(Context context, String selectTitle,
			String subject, String photoPath, String title, String content) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		// intent.setType("text/plain");
		intent.setType("image/png");
		// 添加图片
		File f = new File(photoPath);
		Uri uri = Uri.fromFile(f);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.putExtra(Intent.EXTRA_TITLE, title);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, selectTitle));
	}

	/**
	 * 分享
	 * 
	 * @param context
	 * @param more
	 *            更多接口
	 * @param selectTitle
	 *            分享标题
	 * @param subject
	 *            分享标题
	 * @param title
	 *            分享标题
	 * @param content
	 *            分享内容
	 * @param clickUrl
	 * @param isShowCollect
	 *            是否显示收藏按钮
	 * @param isShowShare
	 *            是否显示分享布局
	 * @param isShowMore
	 *            是否显示更多布局
	 */
	public static ShareActionSheet sendTextIntent(Activity context,
			OnMoreListener more, String selectTitle, String subject,
			String title, String content, String clickUrl,
			boolean isShowCollect, boolean isShowShare, boolean isShowMore,
			String shareImageUrl, boolean isShouldCombineUrl) {
		ShareActionSheet actionSheet = ShareActionSheet.getInstance(context,
				more, title, content, clickUrl, isShowCollect, isShowShare,
				isShowMore, shareImageUrl, isShouldCombineUrl, share_type,
				callback);
		// 是否按照默认方式显示
		if (isShow) {
			actionSheet.show();
		}
		// 回复成默认类型:窗口显示
		isShow = true;
		// 回复成默认类型:分享新闻
		share_type = 0;
		// 清除回调对象
		callback = null;
		return actionSheet;
	}

	public static ShareVideoActionSheet sendVideoTextIntent(Activity context,
			String title, String content, String clickUrl,
			String shareImageUrl, boolean isShouldCombineUrl) {
		ShareVideoActionSheet actionSheet = ShareVideoActionSheet.getInstance(
				context, title, content, clickUrl, shareImageUrl,
				isShouldCombineUrl, share_type);
		// 是否按照默认方式显示
		if (isShow) {
			actionSheet.show();
		}
		// 回复成默认类型:窗口显示
		isShow = true;
		// 回复成默认类型:分享新闻
		share_type = 0;
		// 清除回调对象
		callback = null;
		return actionSheet;
	}

	public static ShareActionSheetGame sendGameTextIntent(Activity context,
			OnMoreListener more, String selectTitle, String subject,
			String title, String content, String clickUrl,
			boolean isShowCollect, boolean isShowShare, boolean isShowMore,
			String shareImageUrl, boolean isShouldCombineUrl, String iconUrl) {

		ShareActionSheetGame actionSheet = ShareActionSheetGame.getInstance(
				context, more, title, content, clickUrl, isShowCollect,
				isShowShare, isShowMore, shareImageUrl, isShouldCombineUrl,
				share_type, callback, new UMImage(context, iconUrl));
		// 是否按照默认方式显示
		if (isShow) {
			actionSheet.show();
		}
		// 回复成默认类型:窗口显示
		isShow = true;
		// 回复成默认类型:分享新闻
		share_type = 0;
		// 清除回调对象
		callback = null;
		return actionSheet;
	}

	/**
	 * 分享
	 * 
	 * @param context
	 * @param more
	 *            更多接口
	 * @param selectTitle
	 *            分享标题
	 * @param subject
	 *            分享标题
	 * @param title
	 *            分享标题
	 * @param content
	 *            分享内容
	 * @param clickUrl
	 * @param isShowCollect
	 *            是否显示收藏按钮
	 * @param isShowShare
	 *            是否显示分享布局
	 * @param isShowMore
	 *            是否显示更多布局
	 */
	public static ShareActionSheetEn sendTextIntentEn(Activity context,
			OnMoreListener more, String selectTitle, String subject,
			String title, String content, String clickUrl,
			boolean isShowCollect, boolean isShowShare, boolean isShowMore,
			String shareImageUrl, boolean isShouldCombineUrl) {
		/*
		 * Intent intent=new Intent(Intent.ACTION_SEND);
		 * intent.setType("text/plain"); intent.putExtra(Intent.EXTRA_SUBJECT,
		 * subject); intent.putExtra(Intent.EXTRA_TEXT, content);
		 * intent.putExtra(Intent.EXTRA_TITLE, title);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * context.startActivity(Intent.createChooser(intent, selectTitle));
		 */

		// ShareNewActivity.goShareNewActivity(context);
		// title 【历史脱口秀】奇葩皇帝一箩筐
		// content
		// 【【历史脱口秀】奇葩皇帝一箩筐】http://xw.feedss.com/show/index?newsid=6117（来自新华炫闻客户端）
		// content
		ShareActionSheetEn actionSheet = ShareActionSheetEn.getInstance(
				context, more, title, content, clickUrl, isShowCollect,
				isShowShare, isShowMore, shareImageUrl, isShouldCombineUrl,
				share_type, callback);
		// 是否按照默认方式显示
		if (isShow) {
			actionSheet.show();
		}
		// 回复成默认类型:窗口显示
		isShow = true;
		// 回复成默认类型:分享新闻
		share_type = 0;
		// 清除回调对象
		callback = null;
		return actionSheet;
	}
}
