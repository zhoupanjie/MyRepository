package com.cplatform.xhxw.ui.ui.main.cms.game;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

/**
 * 新闻详情接口
 * Created by cy-love on 14-1-8.
 */
public class GameDetailResponse extends BaseResponse {

    private GameDetail data;

    public GameDetail getData() {
        return data;
    }

    public void setData(GameDetail data) {
        this.data = data;
    }
}
