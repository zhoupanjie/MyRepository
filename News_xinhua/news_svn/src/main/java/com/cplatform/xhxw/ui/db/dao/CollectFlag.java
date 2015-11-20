package com.cplatform.xhxw.ui.db.dao;

/**
 * 收藏类型
 * Created by cy-love on 14-1-25.
 */
public class CollectFlag {
    
    /**
     * 普通用户图片新闻
     */
    public static final int COLLECT_NEWS_TYPE_NORMAL_PICS = 2;
    
    /**
     * 普通用户普通新闻
     */
	public static final int COLLECT_NEWS_TYPE_NORMAL_NORM = 1;
	
	/**
	 * 企业用户图片新闻 1000000 + 企业ID
	 */
	public static final int COLLECT_NEWS_TYPE_ENTERPRISE_PICS = 1000000;
	
	/**
	 * 企业用户普通新闻 2000000 + 企业ID
	 */
	public static final int COLLECT_NEWS_TYPE_ENTERPRISE_NORM = 2000000;
}
