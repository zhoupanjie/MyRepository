package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.util.List;

import android.os.WorkSource;

import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.model.Focus;

/**
 * 新闻列表
 * Created by cy-love on 14-1-2.
 */
public class GameListResponse extends BaseResponse {

    private Conetnt data;

    public Conetnt getData() {
        return data;
    }

    public void setData(Conetnt data) {
        this.data = data;
    }

    public class Conetnt {
        private List<Game> list;
        private List<Focus> focus;
        private List<GameTypes> gametypes;
        private List<GameScrollbars> gamescrollbars;

        public List<Game> getList() {
            return list;
        }

        public void setList(List<Game> list) {
            this.list = list;
        }

        public List<Focus> getFocus() {
            return focus;
        }

        public void setFocus(List<Focus> focus) {
            this.focus = focus;
        }

		public List<GameTypes> getGametypes() {
			return gametypes;
		}

		public void setGametypes(List<GameTypes> gametypes) {
			this.gametypes = gametypes;
		}

		public List<GameScrollbars> getGamescrollbars() {
			return gamescrollbars;
		}

		public void setGamescrollbars(List<GameScrollbars> gamescrollbars) {
			this.gamescrollbars = gamescrollbars;
		}
		
    }
}
