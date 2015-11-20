package com.cplatform.xhxw.ui.service;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.GetVersionInfoRequest;
import com.cplatform.xhxw.ui.http.net.GetVersionInfoResponse;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.Engine;
import com.cplatform.xhxw.ui.util.InstallAppTask;
import com.cplatform.xhxw.ui.util.InstallAppTask.OnLoadListener;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VersionUpdateService extends Service implements OnLoadListener{

	private int type = 0;
	private String url;
	private InstallAppTask task;
	
	/**
	 * 
	 * @param context
	 *            上下文
	 */
	public static void checkVersion(Context context) {
		Intent intent = new Intent(context, VersionUpdateService.class);
//		Bundle bundle = new Bundle();
//		intent.putExtras(bundle);
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

		getVersionInfo();
		
		return startId;
	}

	/** 获取版本属性 */
	private AsyncHttpResponseHandler getHandler;

	private void getVersionInfo() {

		GetVersionInfoRequest request = new GetVersionInfoRequest();
		request.setDevice_type("android_phone");
		request.setVersion_no(Engine.getVersionCode(this));

		APIClient.getVersionInfo(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				getHandler = null;
			}

			@Override
			protected void onPreExecute() {
				if (getHandler != null)
					getHandler.cancle();
					getHandler = this;
				// mDefView.setStatus(DefaultView.Status.loading);
			}

			@Override
			public void onSuccess(String content) {
				final GetVersionInfoResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							GetVersionInfoResponse.class);
				} catch (Exception e) {
//					showToast(R.string.data_format_error);
					return;
				}
				
				if (response != null) {
					if (response.isSuccess()) {
						if (response.getData() != null) {
							if (Engine.getVersionCode(getApplicationContext()) < Integer
									.valueOf(response.getData().getVersion_no())) {
								
								url = response.getData().getDown_url();
								
								if ("1".equals(response.getData().getUpdate_type())) {//强制升级
									/*Intent intent = new Intent(getApplicationContext(), VersionAlertActivity.class);  
									intent.putExtra("url", response.getData().getDown_url());
									intent.putExtra("type", response.getData().getUpdate_type());
									intent.putExtra("message", response.getData().getUpdate_info());
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
									startActivity(intent); */
									
//									showMustDialog(response.getData().getDown_url());
									showMustDialog1(response.getData().getDown_url());
								}else {//普通升级
									/*Intent intent = new Intent(getApplicationContext(), VersionAlertActivity.class);  
									intent.putExtra("url", response.getData().getDown_url());
									intent.putExtra("type", response.getData().getUpdate_type());
									intent.putExtra("message", response.getData().getUpdate_info());
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
									startActivity(intent); */
									
//									showNomalDialog(response.getData().getDown_url());
									showNomalDialog1(response.getData().getDown_url());
							} 
						}
					} 					
				}
				stopSelf();
			}
			}
			@Override
			public void onFailure(Throwable error, String content) {
				// mDefView.setStatus(DefaultView.Status.error);
			}
		});
	}

	@Override
	public void onFail() {
		task = null;
		if (type == 1) {
			showMustDialog(url);
		}
	}

	@Override
	public void onSecuses() {
		
	}

	/**
	 * 强制升级 
	 * */
	private void showMustDialog(final String url){
		
		type = 1;
		
		View view =View.inflate(VersionUpdateService.this, R.layout.version_update_must, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(VersionUpdateService.this);
		
		builder.setView(view);
		
		final AlertDialog dialog = builder.create();
		//按返回键  不可取消对话框外部，不可消失
		dialog.setCancelable(false);
		
		//点击
		dialog.setCanceledOnTouchOutside(false);
		
		//系统中关机对话框就是这个属性 
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG); 
		//窗口可以获得焦点，响应操作 
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
		//窗口不可以获得焦点，点击时响应窗口后面的界面点击事件 
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);  
		
		dialog.show(); 

		TextView ok = (TextView) view.findViewById(R.id.dialog_text_ok);
		TextView exit = (TextView)view.findViewById(R.id.dialog_text_exit);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (task == null) {
					task = new InstallAppTask(VersionUpdateService.this,
							getString(R.string.version_update_string),
							url);
				}
				 
				task.setOnLoadListener(VersionUpdateService.this);
				task.execute();
				dialog.dismiss();
			}
		});
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				exitApp();
			}
		});
	}
	 
	/**
	 * 普通升级 
	 * */
	private void showNomalDialog(final String url){
		
		type = 2;
		
		View view =View.inflate(VersionUpdateService.this, R.layout.version_update_must, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(VersionUpdateService.this);
		
		builder.setView(view);
		
		final AlertDialog dialog = builder.create();
		//按返回键  不可取消对话框外部，不可消失
		dialog.setCancelable(false);
		
		//点击
		dialog.setCanceledOnTouchOutside(false);
		
		//系统中关机对话框就是这个属性 
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG); 
		//窗口可以获得焦点，响应操作 
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
		//窗口不可以获得焦点，点击时响应窗口后面的界面点击事件 
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);  
		
		dialog.show(); 

		LinearLayout ok = (LinearLayout) view.findViewById(R.id.dialog_ok_linear);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (task == null) {
					task = new InstallAppTask(VersionUpdateService.this,
							getString(R.string.version_update_string),
							url);
				}
				task.setOnLoadListener(VersionUpdateService.this);
				task.execute();
				dialog.dismiss();
			}
		});
	}
	
	private void showMustDialog1(final String url){
		type = 1;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setTitle("版本更新"); 
		builder.setMessage("发现新版本，马上更新？") 
	       .setCancelable(false) 
	       .setPositiveButton("更新", new DialogInterface.OnClickListener() { 
	           public void onClick(DialogInterface dialog, int id) { 
	        	   dialog.cancel(); 
	        	   if (CommonUtils.isFastDoubleClick()) {
						return;
					}
	        	   if (task == null) {
						task = new InstallAppTask(VersionUpdateService.this,
								getString(R.string.version_update_string),
								url);
					}
					 
					task.setOnLoadListener(VersionUpdateService.this);
					task.execute();
	           } 
	       }); 
		
	   AlertDialog alert = builder.create(); 
	   alert.setIcon(android.R.drawable.ic_dialog_info);
		//系统中关机对话框就是这个属性 
//		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG); 
		//窗口可以获得焦点，响应操作 
	    alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
		//窗口不可以获得焦点，点击时响应窗口后面的界面点击事件 
//		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);  
	    alert.setCanceledOnTouchOutside(false);
	   alert.show();
	}
	
   private void showNomalDialog1(final String url){
		
		type = 2;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setTitle("版本更新"); 
		builder.setMessage("发现新版本，马上更新？") 
	       .setCancelable(false) 
	       .setPositiveButton("更新", new DialogInterface.OnClickListener() { 
	           public void onClick(DialogInterface dialog, int id) { 
	        	   dialog.cancel(); 
	        	   if (CommonUtils.isFastDoubleClick()) {
						return;
					}
	        	   if (task == null) {
						task = new InstallAppTask(VersionUpdateService.this,
								getString(R.string.version_update_string),
								url);
					}
					 
					task.setOnLoadListener(VersionUpdateService.this);
					task.execute();
	           } 
	       }) 
	       .setNegativeButton("取消", new DialogInterface.OnClickListener() { 
	           public void onClick(DialogInterface dialog, int id) { 
	                dialog.cancel(); 
	           } 
	       }); 
		
	   AlertDialog alert = builder.create(); 
	   alert.setIcon(android.R.drawable.ic_dialog_info);
		//系统中关机对话框就是这个属性 
//		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG); 
		//窗口可以获得焦点，响应操作 
	    alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
		//窗口不可以获得焦点，点击时响应窗口后面的界面点击事件 
//		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);  
	    alert.setCanceledOnTouchOutside(false);
	    alert.show();
	}
   
   private void exitApp() {
	   Intent intent = new Intent(Intent.ACTION_MAIN);  
       intent.addCategory(Intent.CATEGORY_HOME);  
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
       startActivity(intent);  
       android.os.Process.killProcess(Process.myPid());
   }
}
