package com.cplatform.xhxw.ui.location;

import android.content.Context;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.util.LogUtil;

/**
 * 定位管理类
 * Created by cy-love on 14-1-17.
 */
public class LocationClientController {

    private static final String TAG = LocationClientController.class.getSimpleName();
    private LocationClient mLocationClient = null;

    public LocationClientController(Context context) {
        mLocationClient = new LocationClient(context);
        mLocationClient.setAK(context.getString(R.string.app_key_baidu_location));

        LocationClientOption mOption = new LocationClientOption();
        mOption.setOpenGps(false);
        mOption.setAddrType("all");//返回的定位结果包含地址信息
        mOption.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        mOption.disableCache(true);//禁止启用缓存定位
        mOption.setPriority(LocationClientOption.NetWorkFirst);
        mLocationClient.setLocOption(mOption);
    }

    public void setLocationListener(BDLocationListener listener) {
        mLocationClient.registerLocationListener(listener);
    }

    public void start() {
        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    public void stop() {
        try {
            if (mLocationClient != null && mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }

    }
}
