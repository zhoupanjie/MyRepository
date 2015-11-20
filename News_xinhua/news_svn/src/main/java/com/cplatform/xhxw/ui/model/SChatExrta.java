package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * S信息对话列表最后一条未读内容附件内容
 */
public class SChatExrta implements Serializable {

    private String thumb; // 图片缩略图
    private String file; // 语音文件地址或者图片原图

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
