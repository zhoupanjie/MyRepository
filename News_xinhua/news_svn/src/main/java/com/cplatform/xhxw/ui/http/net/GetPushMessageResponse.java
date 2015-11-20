package com.cplatform.xhxw.ui.http.net;

import android.content.Context;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.Message;
import com.cplatform.xhxw.ui.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * push message 解析
 * Created by cy-love on 14-1-23.
 */
public class GetPushMessageResponse extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Message> getDatas(Context context) {
        return data.getDatas(context);
    }

    private class Data {
        private List<Message> today; //今天
        private List<Message> yesterday; // 昨天
        private List<Message> history; // 历史

        public List<Message> getToday() {
            return today;
        }

        public void setToday(List<Message> today) {
            this.today = today;
        }

        public List<Message> getYesterday() {
            return yesterday;
        }

        public void setYesterday(List<Message> yesterday) {
            this.yesterday = yesterday;
        }

        public List<Message> getHistory() {
            return history;
        }

        public void setHistory(List<Message> history) {
            this.history = history;
        }

        public List<Message> getDatas(Context context) {
            List<Message> data = new ArrayList<Message>();
            if (!ListUtil.isEmpty(today)) {
                for (Message item : today) {
                    item.setTime(context.getString(R.string.today));
                    data.add(item);
                }
            }
            if (!ListUtil.isEmpty(yesterday)) {
                for (Message item : yesterday) {
                    item.setTime(context.getString(R.string.yesterday));
                    data.add(item);
                }
            }
            if (!ListUtil.isEmpty(history)) {
                for (Message item : history) {
                    item.setTime(context.getString(R.string.history));
                    data.add(item);
                }
            }
            return data;
        }
    }

}
