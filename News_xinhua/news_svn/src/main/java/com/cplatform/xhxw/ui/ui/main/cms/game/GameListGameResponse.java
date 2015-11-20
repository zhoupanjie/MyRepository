package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

/**
 * 新闻列表 新闻解析
 * Created by cy-love on 14-1-2.
 */
public class GameListGameResponse extends BaseResponse {

    private List<Game> data;

    public List<Game> getData() {
        return data;
    }

    public void setData(List<Game> data) {
        this.data = data;
    }
}
