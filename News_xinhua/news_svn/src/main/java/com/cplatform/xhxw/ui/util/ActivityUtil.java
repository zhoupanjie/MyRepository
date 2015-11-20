package com.cplatform.xhxw.ui.util;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.cplatform.xhxw.ui.http.responseType.NewsType;
import com.cplatform.xhxw.ui.model.PicShow;
import com.cplatform.xhxw.ui.ui.detailpage.NewsPageActivity;
import com.cplatform.xhxw.ui.ui.picShow.PicShowActivity;
import com.cplatform.xhxw.ui.ui.specialTopic.SpecialTopicActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.ui.web.newscollect.NewsCollectWebActivity;

/**
 * Created by cy-love on 13-12-29.
 */
public class ActivityUtil {

	/**
	 * 判断程序是否允许在后台
	 * 
	 * @param context
	 * @return true 表示后台，false表示前台
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Android获取一个用于打开图片文件的intent
	 * 
	 * @param filePath
	 * @return
	 */
	public static Intent getImageFileIntent(String filePath) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(filePath));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	/**
	 * 消息推送，我的评论以及搜索界面点击新闻时根据newstype跳转到相应详情页面
	 * 
	 * @param con
	 * @param newsType
	 * @param newsId
	 *            && item.getShowType() == ShowType.BIG_VIDEO
	 */
	public static void goToNewsDetailPageByNewstype(Context con, int newsType,
			String newsId, String videoUrl, boolean isEnterprise, String title) {

		switch (newsType) {
		case NewsType.SPECIAL_TOPIC:
			con.startActivity(SpecialTopicActivity.getIntent(con, newsId, null));
			break;
		case NewsType.ATLAS: {
			PicShow picShow = new PicShow(new String());
			picShow.setPics(null);
			con.startActivity(PicShowActivity.getIntent(con, picShow, 0,
					newsId, true, isEnterprise, title));
		}
			break;
		case NewsType.LIVE:
			con.startActivity(WebViewActivity.getLiveNewsIntent(con, newsId,
					title, true));
			break;
		case NewsType.COLLECT:
			con.startActivity(NewsCollectWebActivity.getCollectIntent(con,
					newsId, title, true));
			break;
		case NewsType.DRAW_PRIZE:
			con.startActivity(WebViewActivity.getDrawPrizeIntent(con, null,
					newsId));
			break;
		default:
			con.startActivity(NewsPageActivity.getIntent(con, newsId,
					isEnterprise));
			break;
		}
	}
}
