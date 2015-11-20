package com.cplatform.xhxw.ui.ui.detailpage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.model.Ad;
import com.cplatform.xhxw.ui.model.DetailHotNews;
import com.cplatform.xhxw.ui.model.NewPic;
import com.cplatform.xhxw.ui.model.NewsDetail;
import com.cplatform.xhxw.ui.model.PicShow;
import com.cplatform.xhxw.ui.model.Pics;
import com.cplatform.xhxw.ui.model.Relation;
import com.cplatform.xhxw.ui.model.TopRelation;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheet;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.ui.picShow.PicShowActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.util.ActivityUtil;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.HtmlUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.wbtech.ums.UmsAgent;

public class JSInterface {
	private NewsDetail mNewsDetail;

	private Context mCon;
	private Activity mAct;
	private boolean mIsEnterpsie;
	private onJsOperListener mJsOperListener;

	public JSInterface(Context con, Activity act, boolean isEnterprise) {
		this.mCon = con;
		this.mAct = act;
		this.mIsEnterpsie = isEnterprise;
	}

	/**
	 * 获得标题
	 */
	@JavascriptInterface
	public String getTitle() {
		if (mNewsDetail != null && !TextUtils.isEmpty(mNewsDetail.getTitle())) {
			return HtmlUtil.getTitle(mNewsDetail.getTitle());
		}
		return "";
	}

	/**
	 * 获得正文内容
	 */
	@JavascriptInterface
	public String getBody() {
		if (mNewsDetail != null) {
			String str = HtmlUtil.doHtmlContent(mNewsDetail.getContent(),
					mNewsDetail.getPics(), mNewsDetail.getVideos(),
					mNewsDetail.getAudios());
			return str;
		}
		return "";
	}
	
	@JavascriptInterface
	public String getTopAd() {
		if (mNewsDetail != null) {
			String str = HtmlUtil.getAd(mCon, mNewsDetail.getAd().getTop(), 1);
			return str;
		}
		return "";
	}
	
	@JavascriptInterface
	public String getSharesLay() {
		if (mNewsDetail != null) {
			String str = HtmlUtil.getShareLoCss();
			return str;
		}
		return "";
	}
	
	@JavascriptInterface
	public void startShare(int i) {
		if(mNewsDetail != null) {
			if(mJsOperListener != null) {
				mJsOperListener.onStartShare(i);
			}
		}
	}

	@JavascriptInterface
	public String getVideos() {
		if (mNewsDetail != null) {
			String vidios = HtmlUtil.getVidiosString(mNewsDetail.getVideos());
			return vidios;
		}
		return "";
	}

	/**
	 * 获得推广内容
	 */
	@JavascriptInterface
	public String getAdvertisements() {
		if (mNewsDetail != null) {
			String str = HtmlUtil.getAd(mCon, mNewsDetail.getAd().getBottom(), 2);
			return str;
		}
		return "";
	}

	/**
	 * 播放视频
	 * 
	 * @param i
	 *            位置
	 */
	@JavascriptInterface
	public void playVideo(int i) {
		String url = mNewsDetail.getVideos().get(i).getUrl();
		String newsId = mNewsDetail.getId();
		mCon.startActivity(VideoPlayerActivity.getIntent(mCon, newsId, url));
		// if(mOnVideoPlayListener != null) {
		// mOnVideoPlayListener.onVideoPlay(url);
		// }
	}

	/**
	 * 显示img
	 * 
	 * @param i
	 *            位置
	 */
	@JavascriptInterface
	public void showImg(int i) {
		if (CommonUtils.isFastDoubleClick()) {
			return;
		}
		PicShow picShow = new PicShow(new String());
		picShow.setCommentCount(mNewsDetail.getCommentcount());
		picShow.setTitle(mNewsDetail.getTitle());
		picShow.setNewsId(mNewsDetail.getId());
		picShow.setComment(mNewsDetail.getIscomment().equals("0") ? true
				: false);
		List<NewPic> pisc = new ArrayList<NewPic>();
		List<Pics> list = mNewsDetail.getPics();
		for (Pics item : list) {
			NewPic tmp = new NewPic();
			tmp.setThumbnail(item.getUrl());
			pisc.add(tmp);
		}
		picShow.setPics(pisc);
		mCon.startActivity(PicShowActivity.getIntent(mCon, picShow, i,
				mNewsDetail.getId(), false, mIsEnterpsie,
				mNewsDetail.getTitle()));
	}

	/**
	 * 相关新闻
	 */
	@JavascriptInterface
	public void startRelation(int i) {
		if (mNewsDetail != null) {
			Relation item = mNewsDetail.getRelation().get(i);
			ActivityUtil.goToNewsDetailPageByNewstype(mCon,
					StringUtil.parseIntegerFromString(item.getNewsType()),
					item.getId(), null, mIsEnterpsie, item.getTitle());
		}
	}
	
	@JavascriptInterface
	public void startHotsRelation(int i) {
		if (mNewsDetail != null) {
			DetailHotNews item = mNewsDetail.getHotnews().get(i);
			ActivityUtil.goToNewsDetailPageByNewstype(mCon,
					StringUtil.parseIntegerFromString(item.getNewsType()),
					item.getId(), null, mIsEnterpsie, item.getTitle());
		}
	}

	/**
	 * 顶部相关新闻
	 */
	@JavascriptInterface
	public void startTopRelation(int i) {
		if (mNewsDetail != null) {
			TopRelation item = mNewsDetail.getTopRelation().get(i);
			String id=item.getId();
			if(id== null){
//				mCon.startActivity(WebViewActivity.getIntentByURL(mCon,
//						item.getUrl(), item.getTitle()));
				mCon.startActivity(WebViewActivity.getIntentByURL(mCon,
						item.getUrl(), ""));
			} else {
				ActivityUtil.goToNewsDetailPageByNewstype(mCon,
						StringUtil.parseIntegerFromString(item.getNewsType()),
						item.getId(), null, mIsEnterpsie, item.getTitle());
			}
		}
	}

	/**
	 * 
	 * @param i
	 * @param isTop, 1:top ; 2:bottom
	 */
	@JavascriptInterface
	public void startAdvertisements(int i, int isTop) {
		if (mNewsDetail != null) {
			Ad ad = mNewsDetail.getAd().getBottom().get(i);
			mCon.startActivity(WebViewActivity.getIntentByURL(mCon,
					ad.getShorturl(), ad.getTitle()));
			UmsAgent.onEvent(mCon, StatisticalKey.xwad_detail_banner,
					new String[] { StatisticalKey.key_newsid,StatisticalKey.key_title  },
					new String[] { ad.getId(),ad.getTitle() });
		}
	}

	/**
	 * 正文字体大小 0极大 1大 2中 3小
	 */
	@JavascriptInterface
	public int getTextSize() {
		return Constants.getNewsDetTextSize();
	}

	/**
	 * 来源
	 */
	@JavascriptInterface
	public String getSource() {
		if (mNewsDetail != null) {
			// 判断是否为中文版，否则返回空
			if (PreferencesManager.getLanguageInfo(mCon).equals(
					LanguageUtil.LANGUAGE_CH)) {
				String original = HtmlUtil.getSource(mCon,
						mNewsDetail.getSource(), mNewsDetail.getFriendTime());
				if (mNewsDetail.getReadCount() > 0) {
					original += "&nbsp;&nbsp;&nbsp;&nbsp;"
							+ mNewsDetail.getReadCount() + "阅读";
				}
				return original;
			} else {
				return "";
			}

		}
		return "";
	}

	/**
	 * 相关新闻
	 */
	@JavascriptInterface
	public String getRelation() {
		if (mNewsDetail != null && !ListUtil.isEmpty(mNewsDetail.getRelation())) {
			return HtmlUtil.getRelation(mCon, mNewsDetail.getRelation());
		}
		return "";
	}
	
	/**
	 * 热点相关新闻
	 */
	@JavascriptInterface
	public String getHotsRelation() {
		if (mNewsDetail != null && !ListUtil.isEmpty(mNewsDetail.getHotnews())) {
			boolean isShowTit = mNewsDetail.getRelation() == null || mNewsDetail.getRelation().size() < 1;
			return HtmlUtil.getHotsRelation(mCon, mNewsDetail.getHotnews(), isShowTit);
		}
		return "";
	}

	/**
	 * 顶部相关新闻
	 */
	@JavascriptInterface
	public String getTopRelation() {
		if (mNewsDetail != null
				&& !ListUtil.isEmpty(mNewsDetail.getTopRelation())) {
			return HtmlUtil.getTopRelation(mCon, mNewsDetail.getTopRelation());
		}
		return "";
	}

	public NewsDetail getmNewsDetail() {
		return mNewsDetail;
	}

	public void setmNewsDetail(NewsDetail mNewsDetail) {
		this.mNewsDetail = mNewsDetail;
	}

	public void setJsOperListener(onJsOperListener mJsListener) {
		this.mJsOperListener = mJsListener;
	}

	public interface onJsOperListener {
		void onVideoPlay(String url);
		void onStartShare(int shareType);
	}
}
