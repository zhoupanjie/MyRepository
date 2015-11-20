package com.cplatform.xhxw.ui.location;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.model.LocationPoint;

public class LocationUtil {
	private static final int FETCH_POSITION_INTERVAL = 60 * 1000 * 10;//获取位置信息的间隔时间
	
	public static LocationPoint getAPosition(final Context context, final LocationClientController locationClient) {
		long lastFetchTime = PreferencesManager.getLastPositionTime(context);
		if(lastFetchTime != -1 && (lastFetchTime - System.currentTimeMillis() < FETCH_POSITION_INTERVAL)) {
			return PreferencesManager.getPositionInfo(context);
		}
		locationClient.start();
        locationClient.setLocationListener(new MyLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				LocationPoint point = new LocationPoint();
				for(int i = 0; i < 5; i++) {
					double longitude = arg0.getLongitude();
					double latitude = arg0.getLatitude();
					point.setLongitude(longitude);
					point.setLatitude(latitude);
					if(longitude != 0L && latitude != 0L) {
						PreferencesManager.savePositionTime(context, System.currentTimeMillis());
						PreferencesManager.savePositionInfo(context, point);
						break;
					}
				}
				locationClient.stop();
			}

			@Override
			public void onReceivePoi(BDLocation arg0) {
			}

			@Override
			public void onMyLocation(BDLocation location) {
			}
		});
        
		return getExistedLocation(context);
	}
	
	private static LocationPoint getExistedLocation(Context con) {
		LocationPoint point = PreferencesManager.getPositionInfo(con);
		if(point.getLongitude() != -1L) {
			return point;
		}
		return null;
	}
}
