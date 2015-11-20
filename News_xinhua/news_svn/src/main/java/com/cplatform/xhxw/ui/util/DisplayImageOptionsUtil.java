package com.cplatform.xhxw.ui.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.cplatform.xhxw.ui.R;

/**
 * 图像显示
 * Created by cy-love on 13-12-25.
 */
public class DisplayImageOptionsUtil {
    /**列表新闻图片默认*/
    public static final DisplayImageOptions listNewImgOptions;
    /**列表新闻滚动图片*/
    public static final DisplayImageOptions newsSliderImgOptions;
    /** 列表新闻横向图集展示 */
    public static final DisplayImageOptions newsMultiHorImgOptions;
    /** 专题推荐图 */
    public static final DisplayImageOptions SpecialTopicRecoOptions;
    /** 推荐图片集展示 */
    public static final DisplayImageOptions recommendImagesOptions;
    /** 头像 */
    public static final DisplayImageOptions avatarImagesOptions;
    /** 小头像 */
    public static final DisplayImageOptions avatarSmallImagesOptions;

    /** saas默认头像 */
    public static final DisplayImageOptions avatarSaasImagesOptions;
    /** 好友详情 */
    public static final DisplayImageOptions avatarFriendsInfoImagesOptions;
    /** 好友列表 */
    public static final DisplayImageOptions avatarFriendsListImagesOptions;
    
    public static final DisplayImageOptions flagDisplayOpts;
    
    static {
        listNewImgOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.def_img_4_3)
                .showImageForEmptyUri(R.drawable.def_img_4_3)
                .showImageOnFail(R.drawable.def_img_4_3).cacheInMemory()
                .cacheOnDisc().build();

        newsSliderImgOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.def_img_16_9)
                .showImageForEmptyUri(R.drawable.def_img_16_9)
                .showImageOnFail(R.drawable.def_img_16_9).cacheInMemory()
                .cacheOnDisc().build();
        newsMultiHorImgOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.def_img_4_3)
                .showImageForEmptyUri(R.drawable.def_img_4_3)
                .showImageOnFail(R.drawable.def_img_4_3).cacheInMemory()
                .cacheOnDisc().build();
        SpecialTopicRecoOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.def_img_4_3)
                .showImageForEmptyUri(R.drawable.def_img_4_3)
                .showImageOnFail(R.drawable.def_img_4_3).cacheInMemory()
                .cacheOnDisc().build();
        recommendImagesOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.def_img_4_3)
                .showImageForEmptyUri(R.drawable.def_img_4_3)
                .showImageOnFail(R.drawable.def_img_4_3).cacheInMemory()
                .cacheOnDisc().build();
        avatarImagesOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_avatar_bg)
                .showImageForEmptyUri(R.drawable.ic_avatar_bg)
                .showImageOnFail(R.drawable.ic_avatar_bg).cacheInMemory()
                .cacheOnDisc().build();
        avatarSmallImagesOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_avatar_bg)
                .showImageForEmptyUri(R.drawable.ic_avatar_bg)
                .showImageOnFail(R.drawable.ic_avatar_bg).cacheInMemory()
                .cacheOnDisc().build();

        avatarSaasImagesOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.s_msg_user_def)
                .displayer(new RoundedBitmapDisplayer(40))
                .showImageForEmptyUri(R.drawable.s_msg_user_def)
                .showImageOnFail(R.drawable.s_msg_user_def).cacheInMemory()
                .cacheOnDisc().build();
        
        avatarFriendsInfoImagesOptions = new DisplayImageOptions.Builder()
	        .showStubImage(R.drawable.s_msg_user_def)
	        .showImageForEmptyUri(R.drawable.s_msg_user_def)
	        .showImageOnFail(R.drawable.s_msg_user_def).cacheInMemory()
	        .cacheOnDisc().build();
        
        avatarFriendsListImagesOptions = new DisplayImageOptions.Builder()
	        .showStubImage(R.drawable.frriends_header)
	        .showImageForEmptyUri(R.drawable.frriends_header)
	        .showImageOnFail(R.drawable.frriends_header).cacheInMemory()
	        .cacheOnDisc().build();
        
        flagDisplayOpts = new DisplayImageOptions.Builder()
        	.showStubImage(R.drawable.flag)
        	.showImageForEmptyUri(R.drawable.flag)
        	.showImageOnFail(R.drawable.flag).cacheOnDisc()
        	.cacheInMemory().build();
    }
}
