package com.cplatform.xhxw.ui.model;

/**
 * 栏目
 */
public class Channe {

	private String channelid; // 数据id
	private String channelname; // 栏目名称
	private long listorder; // 栏目顺序
    private int isdisplay; // 0 显示 1 隐藏
    private int type; //企业栏目是否为push消息栏目（企业账户使用）
    private int isadd;//关键词搜索栏目是否已在本地添加
    private int channeltype;//0:固定栏目 1：订阅频道 2：关键词栏目
    private int newscount;//栏目下相关新闻数目

    public int getIsadd() {
		return isadd;
	}

	public void setIsadd(int isadd) {
		this.isadd = isadd;
	}

	public int getChanneltype() {
		return channeltype;
	}

	public void setChanneltype(int channeltype) {
		this.channeltype = channeltype;
	}

	public int getNewscount() {
		return newscount;
	}

	public void setNewscount(int newscount) {
		this.newscount = newscount;
	}

	public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

    public long getListorder() {
        return listorder;
    }

    public void setListorder(long listorder) {
        this.listorder = listorder;
    }

    public int getIsdisplay() {
        return isdisplay;
    }

    public void setIsdisplay(int isdisplay) {
        this.isdisplay = isdisplay;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
