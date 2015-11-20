package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.AdvertismentsResponse;
import com.cplatform.xhxw.ui.model.Focus;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.model.Other;
import com.cplatform.xhxw.ui.model.Thumbnail;
import com.cplatform.xhxw.ui.model.Today;

import java.util.List;

/**
 * 新闻列表
 * Created by cy-love on 14-1-2.
 */
public class NewsListResponse extends BaseResponse {

    private Conetnt data;

    public Conetnt getData() {
        return data;
    }

    public void setData(Conetnt data) {
        this.data = data;
    }

    public class Conetnt {
        private List<New> list;
        private List<Focus> focus;
        private List<Other> other;
        private List<Today> today;
        private Thumbnail thumbnail;
        private AdvertismentsResponse ad;

        public List<New> getList() {
            return list;
        }

        public void setList(List<New> list) {
            this.list = list;
        }

        public List<Focus> getFocus() {
            return focus;
        }

        public void setFocus(List<Focus> focus) {
            this.focus = focus;
        }

        public List<Other> getOther() {
            return other;
        }

        public void setOther(List<Other> other) {
            this.other = other;
        }

        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(Thumbnail thumbnail) {
            this.thumbnail = thumbnail;
        }

		public AdvertismentsResponse getAd() {
			return ad;
		}

		public void setAd(AdvertismentsResponse ad) {
			this.ad = ad;
		}

		public List<Today> getToday() {
			return today;
		}

		public void setToday(List<Today> today) {
			this.today = today;
		}
		
    }
}
