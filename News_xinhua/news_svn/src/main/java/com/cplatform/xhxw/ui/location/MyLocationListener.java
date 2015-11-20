package com.cplatform.xhxw.ui.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * Created by cy-love on 14-1-17.
 */
public interface MyLocationListener extends BDLocationListener {

    public void onMyLocation(BDLocation location);
}
