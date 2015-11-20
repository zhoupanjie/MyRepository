package com.cplatform.xhxw.ui.service;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.util.InstallAppTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class VersionDownLoadService extends Service{

	/**
	 * 
	 * @param context
	 *            上下文
	 */
	public static void loadVersion(Context context, String url) {
		Intent intent = new Intent(context, VersionDownLoadService.class);
		intent.putExtra("url", url);
		context.startService(intent);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		new InstallAppTask(
                App.getCurrentApp(),
				getString(R.string.version_update_string),
				intent.getStringExtra("url"))
				.execute();
		
		return startId;
	}
	
}
