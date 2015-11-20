package com.cplatform.xhxw.ui.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.NewsCashDB;
import com.cplatform.xhxw.ui.db.NewsDetailCashDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.db.dao.NewsCashDao;
import com.cplatform.xhxw.ui.db.dao.NewsDetailCashDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.NewsDetailRequest;
import com.cplatform.xhxw.ui.http.net.NewsListRequest;
import com.cplatform.xhxw.ui.http.net.NewsListResponse;
import com.cplatform.xhxw.ui.model.Focus;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.model.Other;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;

import java.util.List;

/**
 * 新闻缓存
 * Created by cy-love on 14-2-4.
 */
public class NewsCashService extends IntentService {

    public static final String TAG = NewsCashService.class.getSimpleName();

    /**
     * 停止缓存
     */
    public static final String ACTION_NEWS_CASH_STOP = "com.xuanwen.mobile.news.ACTION_NEWS_CASH_STOP";

    /**
     * 进度改变
     */
    public static final String ACTION_NEWS_CASH_CHANGE = "com.xuanwen.mobile.news.ACTION_NEWS_CASH_CHANGE";

    /**
     * 进度完成
     */
    public static final String ACTION_NEWS_CASH_DONE = "com.xuanwen.mobile.news.ACTION_NEWS_CASH_DONE";


    private boolean isStop;
    private Receiver mReceiver;
    private static boolean sIsStart;
    private static float sPlannedSpeed; // 当前缓存进度


    public static boolean isStart() {
        return sIsStart;
    }

    public static int getSpeed() {
        return (int) sPlannedSpeed;
    }

    public NewsCashService() {
        super("NewsCashService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(ACTION_NEWS_CASH_STOP));
        sIsStart = true;
        sPlannedSpeed = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mReceiver = null;
        sIsStart = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            LogUtil.d(TAG, "新闻缓存开始");
            String uid = Constants.hasLogin() ? Constants.getUid() : Constants.DB_DEFAULT_USER_ID;
            List<ChanneDao> list = ChanneDB.getShowChanne(this, uid);
            if (!ListUtil.isEmpty(list)) {
                float speed = 100 / list.size();
                for (ChanneDao dao : list) {
                    if (isStop) return;
                    String json = APIClient.syncNewsList(new NewsListRequest(dao.getChannelID()));
                    if (TextUtils.isEmpty(json)) {
                        continue;
                    }
                    NewsListResponse response;
                    try {
                    	ResponseUtil.checkResponse(json);
                        response = new Gson().fromJson(json, NewsListResponse.class);
                    } catch (Exception e) {
                        LogUtil.w(TAG, e);
                        sendSpeedChangeBroadcast(speed);
                        continue;
                    }
                    if (response.isSuccess()) {
                        NewsCashDao cash = new NewsCashDao();
                        cash.setSaveTime(DateUtil.getTime());
                        cash.setJson(json);
                        cash.setColumnId(dao.getChannelID());
                        NewsCashDB.saveData(this, cash);
                        NewsListResponse.Conetnt conetnt = response.getData();

                        // 计算单个进度
                        int size = 0;
                        if (!ListUtil.isEmpty(conetnt.getFocus())) {
                            size += conetnt.getFocus().size();
                        }

                        if (!ListUtil.isEmpty(conetnt.getList())) {
                            size += conetnt.getList().size();
                        }

                        if (!ListUtil.isEmpty(conetnt.getOther())) {
                            size += conetnt.getOther().size();
                        }

                        if (size == 0) {
                            sendSpeedChangeBroadcast(speed);
                            continue;
                        }
                        float itemSpe = speed / size;
                        // 计算单个进度结束

                        if (!ListUtil.isEmpty(conetnt.getFocus())) {
                            List<Focus> focus = conetnt.getFocus();
                            for (Focus item : focus) {
                                if (isStop) return;
                                saveNewsDetailCash(item.getNewsId());
                                sendSpeedChangeBroadcast(itemSpe);
                            }
                        }
                        if (!ListUtil.isEmpty(conetnt.getList())) {
                            List<New> news = conetnt.getList();
                            for (New item : news) {
                                if (isStop) return;
                                saveNewsDetailCash(item.getNewsId());
                                sendSpeedChangeBroadcast(itemSpe);
                            }
                        }
                        if (!ListUtil.isEmpty(conetnt.getOther())) {
                            List<Other> other = conetnt.getOther();
                            for (Other item : other) {
                                if (isStop) return;
                                saveNewsDetailCash(item.getNewsId());
                                sendSpeedChangeBroadcast(itemSpe);
                            }
                        }
                    } else {
                        sendSpeedChangeBroadcast(speed);
                        LogUtil.e(TAG, "新闻栏目::id=" + dao.getChannelID() + " 栏目名称::" + dao.getChannelName()
                                + " 返回数据::" + json);
                    }
                }
            }
        } finally {
            sPlannedSpeed = 100;
            sendSpeedBroadcast(ACTION_NEWS_CASH_DONE);
            LogUtil.d(TAG, "新闻缓存完成");
            StartServiceReceiver.completeWakefulIntent(intent);
        }
    }

    /**
     * 保存新闻内容
     */
    private void saveNewsDetailCash(String newsId) {
        String result = APIClient.syncNewsDetail(new NewsDetailRequest(newsId));
        if (TextUtils.isEmpty(result)) {
            LogUtil.e(TAG, "新闻id=" + newsId + "  未获取新闻详情内容");
            return;
        }
        BaseResponse res;
        try {
        	ResponseUtil.checkResponse(result);
            res = new Gson().fromJson(result, BaseResponse.class);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            return;
        }
        if (res.isSuccess()) {
            NewsDetailCashDao newsDetailCashDao = new NewsDetailCashDao();
            newsDetailCashDao.setJson(result);
            newsDetailCashDao.setSaveTime(DateUtil.getTime());
            newsDetailCashDao.setNewsId(newsId);
            NewsDetailCashDB.saveData(this, newsDetailCashDao);
        } else {
            LogUtil.e(TAG, "新闻id=" + newsId + res.getMsg());
        }
    }

    /**
     * 更改进度
     *
     * @param itemSpeed
     */
    private static void sendSpeedChangeBroadcast(float itemSpeed) {
        sPlannedSpeed += itemSpeed;
        sendSpeedBroadcast(ACTION_NEWS_CASH_CHANGE);
    }

    /**
     * 发送广播
     */
    private static void sendSpeedBroadcast(String action) {
        Intent intent = new Intent(ACTION_NEWS_CASH_CHANGE);
        intent.putExtra("speed", (int) sPlannedSpeed);
        LocalBroadcastManager.getInstance(App.getCurrentApp()).sendBroadcast(intent);
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_NEWS_CASH_STOP.equals(intent.getAction())) {
                isStop = true;
                stopSelf();
            }
        }
    }
}
