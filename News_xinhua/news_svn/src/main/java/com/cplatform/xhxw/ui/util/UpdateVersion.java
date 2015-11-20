package com.cplatform.xhxw.ui.util;

import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.service.VersionUpdateService;

import android.content.Context;

public class UpdateVersion {

	private static UpdateVersion instance;
	private PreferencesManager manager;
	private Context context;
	
	public static UpdateVersion getInstance(Context context) {
        if (null == instance) instance = new UpdateVersion(context.getApplicationContext());
        return instance;
    }

    private UpdateVersion(Context context) {
        this.context = context;
        /**可以在实例化对象的时候去，初始化一些数据*/
    	manager = PreferencesManager.getInstance(context);
    }
    
    public void isUpdate(){ 
    	/**获得本地的版本号*/
    	int nativeCode = Engine.getVersionCode(context);
    	
    	/**获得保存的版本信息*/
    	int code = manager.getVersionCode();
    	int type = manager.getUpdateType();
    	long time = manager.getUpdateTime();
    	
    	VersionUpdateService.checkVersion(context);
    	
    	/**
    	 * nativeCode > code    本地版本号  > 保存的版本号
    	 * 
    	 * 这种情况只针对于，第一次运行的情况
    	 *  
    	 * 因为第一次运行时， share还没有保存过信息，
    	 * 
    	 * 针对于这种情况，直接联网
    	 * */
    	/*if (nativeCode > code) {
			*//**
			 * 开启“联网服务”，访问接口，在根据返回的信息，再和本地进行比较
			 * 
			 * 进一步判断，是否需要开启 “更新服务”*//*
    		
    		VersionUpdateService.checkVersion(context);
    		
    		return;
		}*/
    	
    	/**
    	 * nativeCode <= code    本地版本号  <= 保存的版本号
    	 * 
    	 * 这种情况针对于以前更新过项目，因为share里面保存的code已经有“数据”了
    	 *  
    	 * nativeCode < code
    	 * 以前应该更新却没有更新，
    	 * 
    	 * nativeCode = code
    	 * 上次更新了
    	 * */
    	/*if (nativeCode <= code) {
    		if (type == 1) {//是否是强制更新类型
        		if (isTime()) {
        			*//**
        			 * 开启“联网服务”，访问接口，在根据返回的信息，再和本地进行比较
        			 * 
        			 * 进一步判断，是否需要开启 “更新服务”*//*
        			VersionUpdateService.checkVersion(context);
				}
        		return;
    		}
		}*/
    }
    
    /**
     * 获取此时的时间和上次更新的时间差
     * 
     * 如果有一周的时间，
     * 
     * 返回true，否则返回false
     * */ 
    
    private boolean isTime(){
    	long nowTime = System.currentTimeMillis();//得到的是现在的毫秒数
    	if (nowTime >= changeTime(7)) {
			return true;
		}
		return false;
    }
    
    /**
     * 时间转换
     * 将"规定的天数"的时间转换成毫秒
     * 
     * */
    private long changeTime(int day) {
    	int hour = day * 24;//小时
    	int second = hour * 60; //分钟
		return second * 60 * 1000;
	}
}
