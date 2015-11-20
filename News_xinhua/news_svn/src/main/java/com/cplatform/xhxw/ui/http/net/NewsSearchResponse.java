package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.Search;

import java.util.List;

/**
 * 新闻搜索解析
 * Created by cy-love on 14-1-25.
 */
public class NewsSearchResponse extends BaseResponse {

    private Info data;

    public Info getData() {
        return data;
    }

    public void setData(Info data) {
        this.data = data;
    }

    public List<Search> getList() {
        if (data != null) {
            return data.getList();
        }
        return null;
    }

    public class Info {

        private String count;
        private String pn;
        private String offset;
        private List<Search> list;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getPn() {
            return pn;
        }

        public void setPn(String pn) {
            this.pn = pn;
        }

        public String getOffset() {
            return offset;
        }

        public void setOffset(String offset) {
            this.offset = offset;
        }

        public List<Search> getList() {
            return list;
        }

        public void setList(List<Search> list) {
            this.list = list;
        }
    }
}
