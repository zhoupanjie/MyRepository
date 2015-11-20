package com.cplatform.xhxw.ui.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.*;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;

import java.util.List;

/**
 * html 工具类
 * Created by cy-love on 13-12-29.
 */
public class HtmlUtil {

    private static final String TAG = HtmlUtil.class.getSimpleName();
    private static int DEFAULT_WIDTH = DensityUtil.getWebViewWidth(App.CONTEXT);
    
    private static final String HTML_DEFAULT_LOADING_IMG = "file:///android_asset/def_img.png";

    /**
     * 添加视频点击控件
     *
     * @param index 当前id
     * @param left  距左
     * @param top   距上
     * @return
     */
    public static String getVideo(String url, int index, int left, int top, int width, int height) {
        StringBuilder str = new StringBuilder();
        int _width = DEFAULT_WIDTH - 32;
        int _height = getMediaHeight(height, width);
        int btnLeft = (_width - 80) / 2;
        int btnTop = (_height - 80) / 2;
        str.append("<div style=\"position:relative;\">")
        		.append("<center><img src='")
                .append(url).append("' ")
                .append("width='").append(_width).append("' height='").append(_height).append("' ")
                .append("style='position:relative; background:url(").append(HTML_DEFAULT_LOADING_IMG)
                .append(") center center no-repeat;background-size:cover'/></center>\n")
                .append("<center><img src='")
                .append("big_play.png")
                .append("' style='position:absolute;width:80px;height:80px;left:")
                .append(btnLeft).append("px;top:")
                .append(btnTop).append("px;' onClick='playVideo(0)'/></center></div>");
        return str.toString();
    }

    /**
     * 获得img控件
     *
     * @param index
     * @param uri
     * @param width
     * @param height
     * @return
     */
    public static String getImg(int index, String uri, int width, int height, String summary) {
    	height = getMediaHeight(height, width);
        
        String format1 = "<center><img src=\"%s\" width=\"%d\" height=\"%d\" "
        		+ "style=\"background:url(%s) center center no-repeat;background-size:cover\" "
        		+ "onClick='showImg(%d)'></center>"
        		+ "<p class=\"desc\">%s</p>";
        String str1 = String.format(format1, uri, DEFAULT_WIDTH - 32, height,
        		HTML_DEFAULT_LOADING_IMG, index, summary);
        return str1;
    }

    public static String getCenterStyle() {
        return "style='margin-left:auto;margin-right:auto;text-align:center;'";
    }

    /**
     * 处理html标记符串
     * 视频：<!--VIDEO#0-->
     * 图片：<!--IMG#0-->
     */
    public static String doHtmlContent(String src, List<Pics> pics, List<Videos> videos, List<Audios> audios) {
        StringBuffer buf = new StringBuffer();
        src = (src == null) ? "" : src;
        buf.append(src);
        String startTag = "<!--";
        String endTag = "-->";
        // 替换图片
        if (pics != null) {
            int size = pics.size();

            for (int i = 0; i < size; i++) {
                Pics pic = pics.get(i);
                int width = getWidthIntValue(pic.getWidth());
                int height = getHeightIntValue(pic.getHeight());
                String str = getImg(i, pic.getUrl(), width, height, pic.getSummary());
                replaceTag(buf, startTag, endTag, String.format("IMG#%d", i), str);
            }

        }

        // 替换视频
        if (videos != null) {
            int size = videos.size();

            for (int i = 0; i < size; i++) {
                replaceTag(buf, startTag, endTag, String.format("VIDEO#%d", i), "");
            }

        }
        return buf.toString();
    }
    
    /**
     * 获取新闻中所有视频信息，并生成HTML标签，用以放在新闻头部
     */
    public static String getVidiosString(List<Videos> videos) {
    	if (videos != null) {
    		StringBuilder sb = new StringBuilder();
            int size = videos.size();

            for (int i = 0; i < size; i++) {
                Videos video = videos.get(i);

                String str = getVideo(video.getThumbnail(), i, 0, 0,
                        getWidthIntValue(video.getWidth()), getHeightIntValue(video.getHeight()));
                sb.append(str).append("\n");
            }
            return sb.toString();
        }
    	return "";
    }

    private static int getMediaHeight(int height, int width) {
        if (width ==0) {
            return 0;
        }
        return DEFAULT_WIDTH * height / width;
    }

    private static int getWidthIntValue(String value) {
        try {
        	int width = StringUtil.parseIntegerFromString(value);
            return (width == 0) ? DEFAULT_WIDTH : width;
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return DEFAULT_WIDTH;
    }

    private static int getHeightIntValue(String value) {
        try {
        	int height = StringUtil.parseIntegerFromString(value);
            return (height == 0) ? 9 * DEFAULT_WIDTH / 16 : height;
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return 9 * DEFAULT_WIDTH / 16;
    }

    /**
     * 标记替换
     *
     * @param src      源数据
     * @param startTag 开始标签 : <!--
     * @param endTag   结束标签  -->
     * @param tag      需要替换 IMG#0 | AUDIO#0 | VIDEO#0
     * @param string   替换的数据 <img>...</>
     */
    public static void replaceTag(StringBuffer src, String startTag, String endTag, String tag, String string) {
        int starTagLen = startTag.length();
        int endTagLen = endTag.length();
        int startIndex = 0;

        while ((startIndex = src.indexOf(startTag, startIndex)) != -1) {
            int endIndex = src.indexOf(endTag, startIndex);
            if (endIndex == -1) {
                break;
            }
            String tagStr = src.subSequence(startIndex + starTagLen, endIndex).toString().trim();
            if (!TextUtils.isEmpty(tagStr) && tagStr.equals(tag)) {
                src.replace(startIndex, endIndex + endTagLen, string);
            }

            startIndex = endIndex;
        }
    }

    /**
     * 获得相关新闻
     */
    public static String getRelation(Context context, List<Relation> list) {
        StringBuffer buffer = new StringBuffer();
        if(PreferencesManager.getLanguageInfo(App.CONTEXT).equals(
    				LanguageUtil.LANGUAGE_EN)) {
        	buffer.append("<div class=\"tit\"><h3>Relations</h3></div>");
        } else {
        	buffer.append(String.format("<div class=\"tit\"><h3>%s</h3></div>", context.getString(R.string.related_news)));
        }
        buffer.append("<div class=\"cnt\">");
        buffer.append("<ul>");
        for (int i = 0; i < list.size(); i++) {
            buffer.append(getRelation(i, list.get(i).getTitle()));
        }
        buffer.append("</ul>");
        buffer.append("</div>");
        return buffer.toString();
    }
    
    /**
     * 组合热点相关新闻html
     * @param con
     * @param list
     * @return
     */
    public static String getHotsRelation(Context con, List<DetailHotNews> list, boolean isShowTit) {
    	StringBuffer buffer = new StringBuffer();
    	if(isShowTit) {
    		if(PreferencesManager.getLanguageInfo(App.CONTEXT).equals(
    				LanguageUtil.LANGUAGE_EN)) {
        		buffer.append("<div class=\"tit\"><h3>Relations</h3></div>");
    	    } else {
    	    	buffer.append(String.format("<div class=\"tit\"><h3>%s</h3></div>", con.getString(R.string.related_news)));
    	    }
    	}
    	
    	buffer.append("<div class=\"cnt\">");
    	for(int i = 0; i < list.size(); i++) {
    		buffer.append(getHotsRelationItem(i, list.get(i)));
    	}
    	buffer.append("</div>");
    	return buffer.toString();
    }
    
    private static String getHotsRelationItem(int index, DetailHotNews data) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("<a onclick=\"javascript:startHotsRelation(").append(index).append(")\"; href=\"#\">");
    	buffer.append("<div class=\"item\">");
    	buffer.append("<div class=\"pic\">");
    	buffer.append("<img src=\"").append(data.getThumbnail()).append("\"");
    	buffer.append(" width=\"80\" height=\"80\"");
    	buffer.append("style=\"background:url(").append(HTML_DEFAULT_LOADING_IMG);
    	buffer.append(") center center no-repeat;background-size:cover\"> </div>");
    	buffer.append("<div class=\"con\"><h2>").append(data.getTitle());
    	buffer.append("</h2>");
//    	if(!TextUtils.isEmpty(data.getSource())) {
//    		if(PreferencesManager.getLanguageInfo(App.CONTEXT).equals(
//    				LanguageUtil.LANGUAGE_EN)) {
//    			buffer.append("<h3>Source:").append(data.getSource()).append("</h3>");
//    		} else {
//    			buffer.append("<h3>来源：").append(data.getSource()).append("</h3>");
//    		}
//    	}
    	buffer.append("</div></div>");
    	
    	return buffer.toString();
    }
    
    /**
     * 获得顶部相关新闻
     */
    public static String getTopRelation(Context context, List<TopRelation> list) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("<ul>");
    	for (int i = 0; i < list.size(); i++) {
            buffer.append(getTopRelation(i, list.get(i).getTitle()));
        }
    	buffer.append("</ul>");
    	buffer.append("<hr width=98% size=0.2 color=#bbbcbc style=\"FILTER: alpha(opacity=100,finishopacity=0)\"> ");
    	return buffer.toString();
    }

    private static String getRelation(int index, String title) {
        return String.format("<li><a onclick=\"javascript:startRelation(%d)\"; href=\"#\">%s</a></li>",
                index, title);
    }
    private static String getTopRelation(int index, String title) {
    	return String.format("<li><a onclick=\"javascript:startTopRelation(%d);return false;\" mon=\"ct=0&amp;a=2&amp;c=internet&amp;pn=8\" target=\"_blank\">%s</a></li>",
    			index, title);
    }

    /**
     * 获得热门评论
     */
    public static String getHotComments(Context context, List<HotComments> list) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("<p>%s</p>", context.getString(R.string.hot_comments)));
        for (int i = 0; i < list.size(); i++) {
            buffer.append(getHotComments(i, list.get(i)));
        }
        return buffer.toString();
    }

    public static String getHotComments(int index, HotComments item) {
        String name = TextUtils.isEmpty(item.getRnickName()) ? item.getSnickName() : item.getRnickName();
        return String.format("<p><a onclick=\"javascript:startHotComments(%d);return false;\" href=\"#\">%s:%s</a></p>",
                index, name, item.getContent());
    }

    /**
     * 获得标题
     *
     * @param title 标题内容
     * @return <h1>标题内容</h1>
     */
    public static String getTitle(String title) {
        return String.format("%s", title);
    }

    /**
     * 获得来源
     *
     * @param source
     * @return
     */
    public static String getSource(Context context, String source, String time) {
        if (source == null) source = "";
        if (time == null) time = "";
        if(source.equals("") && time.equals("")) {
        	return "";
        } else {
        	return String.format("%s:%s&nbsp;&nbsp;&nbsp;&nbsp;%s", context.getString(R.string.source), source, time);
        }
    }

    /**
     * 获得文字形式广告
     * @param context
     * @param ads
     * @param isTop 1-top; 2-bottom
     * @return
     */
    public static String getAd(Context context, List<Ad> ads, int isTop) {
        if (ListUtil.isEmpty(ads)) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for(Ad ad : ads) {
        	if(ad.getTypeid().equals("1") || ad.getTypeid().equals("")) {
        		buffer.append(getItemAd(i, ad));
        	}
            i++;
        }
        buffer.append(getImgAds(context, ads, isTop));
        return buffer.toString();
    }
    
    public static String getShareLoCss() {
    	StringBuffer buffer = new StringBuffer();
    	if(PreferencesManager.getLanguageInfo(App.CONTEXT).equals(
				LanguageUtil.LANGUAGE_EN)) {
    		buffer.append("<div class=\"tit\"><em></em><span>Share To</span></div>");
    	} else {
    		buffer.append("<div class=\"tit\"><em></em><span>分享到</span></div>");
    	}
    	buffer.append("<div class=\"cnt clearfix\">");
    	buffer.append("<ul>");
    	buffer.append("<li><a href=\"javascript:startShare(1)\" class=\"icon_weibo\">分享到微博</li>");
    	buffer.append("<li><a href=\"javascript:startShare(2)\" class=\"icon_weixin\">分享到微信</li>");
    	buffer.append("<li><a href=\"javascript:startShare(3)\" class=\"icon_qq\">分享到QQ</li>");
    	buffer.append("<li><a href=\"javascript:startShare(4)\" class=\"icon_quan\">分享到朋友圈</li>");
    	buffer.append("</ul>");
    	buffer.append("</div>");
    	return buffer.toString();
    }

    private static String getItemAd(int i, Ad ad) {
        String format = "<div class=\"promotion\" onclick=\"javascript:startAdvertisements(%d);return false;\" href=\"#\" rel=\"nofollow\" target=\"_blank\" title=\"%s\">%s</div>";
        return String.format(format, i, ad.getTitle(), ad.getTitle());
    }
    
    /**
     * 获取图片形式的广告
     * @param con
     * @param ads
     * @param isTop 1-top; 2-bottom
     * @return
     */
    public static String getImgAds(Context con, List<Ad> ads, int isTop) {
    	if (ListUtil.isEmpty(ads)) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for(Ad ad : ads) {
        	if(ad.getTypeid().equals("2")) {
        		buffer.append(getImgAdItem(i, ad, isTop));
        	}
            i++;
        }
        return buffer.toString();
    }
    
    private static String getImgAdItem(int index, Ad ad, int isTop) {
    	String formatS = "<img width=\"%d\" height=\"%d\" src=\"%s\"; " +
    			"style=\"background:url(%s) center center no-repeat;background-size:cover\" " +
    			"onclick=\"javascript:startAdvertisements(%d, %d);\">";
    	return String.format(formatS, DEFAULT_WIDTH, getAdImgHeight(ad), 
    			ad.getAndroidimg(), HTML_DEFAULT_LOADING_IMG, index, isTop);
    }
    
    private static int getAdImgHeight(Ad ad) {
    	int height = 200;
    	int oriWid = StringUtil.parseIntegerFromString(ad.getWidth());
    	int oriHei = StringUtil.parseIntegerFromString(ad.getHeight());
    	if(oriWid > 0 && oriHei > 0) {
    		height = (int) ((DEFAULT_WIDTH * oriHei) / (oriWid * 1.0f));
    	}
    	return height;
    }
}
