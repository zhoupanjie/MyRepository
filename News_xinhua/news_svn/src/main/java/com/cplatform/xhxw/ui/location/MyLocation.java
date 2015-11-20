package com.cplatform.xhxw.ui.location;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * 获得当前城市位置
 * Created by cy-love on 14-1-17.
 */
public class MyLocation {

    private LocationClientController mLocationController;
    private MyLocationListener mListener;

    public MyLocation(Context context, MyLocationListener listener) {
        mListener = listener;
        mLocationController = new LocationClientController(context.getApplicationContext());
        mLocationController.setLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    return;
                }
//                String code = bdLocation.getCityCode();
                if (mListener != null) {
                    mListener.onMyLocation(bdLocation);
                }
                // 定位成功，关闭定位
                mLocationController.stop();
                mLocationController = null;
                mListener = null;
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
            }
        });
        mLocationController.start();
    }



}
