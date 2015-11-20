package com.cplatform.xhxw.ui.patch;

import android.content.Context;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.util.ListUtil;

import java.util.List;

/**
 * 版本修复补丁
 * Created by cy-love on 14-3-6.
 */
public class Patch {

    private static final int v4_0_3 = 22;

    public static synchronized void mainThreadCheckVersion(Context context, long versionCode) {
        if(versionCode < v4_0_3) { // 小于4.0.3版本，修复初始化时为设置排序顺序bug
            updateChannelOrderForV4_0_2(context, Constants.DB_DEFAULT_USER_ID);
            if(Constants.hasLogin()) {
                updateChannelOrderForV4_0_2(context, Constants.getUid());
            }
        }
    }

    private static void updateChannelOrderForV4_0_2(Context context, String userId) {
        List<ChanneDao> list = ChanneDB.getShowChannelNoOrder(context, userId);
        if (ListUtil.isEmpty(list)) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ChanneDao item = list.get(i);
            if(Constants.RECOM_COLUMU.equals(item.getChannelID())) {
                item.setListorder(0);
            } else {
                item.setListorder(i+1);
            }
            item.setDirty(1);
            ChanneDB.updateChanneById(context, item);
        }
    }
}
