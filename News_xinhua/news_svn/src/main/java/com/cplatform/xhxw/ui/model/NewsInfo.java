package com.cplatform.xhxw.ui.model;

/**
 * Created by cy-love on 14-1-2.
 */
public class NewsInfo {

    private String id;
    private String friendTime;
    private String source;
    private String title;
    private int type;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendTime() {
        return friendTime;
    }

    public void setFriendTime(String friendTime) {
        this.friendTime = friendTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
