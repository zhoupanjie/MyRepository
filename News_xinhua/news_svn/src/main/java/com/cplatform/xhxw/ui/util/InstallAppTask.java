package com.cplatform.xhxw.ui.util;

import java.io.File;
import java.io.IOException;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class InstallAppTask extends AsyncTask<Void, Integer, Boolean> {

	private Context mContext;
	private File mCacheFile;
	private ProgressDialog mDialog;
	private String mUrl;
    private OnLoadListener listener;
    
    public void setOnLoadListener(OnLoadListener listener){
    	this.listener = listener;
    }
    
	/**
	 * 是否存储在内部存储
	 */
	private boolean isInInnerSpace;

	public InstallAppTask(Context context, String title, String url) {
		mContext = context;
		mUrl = url;

		String string = Constants.Directorys.TEMP + "upldate_temp";///storage/sdcard0/yskj_news/temp/upldate_temp
		String string2 = context.getCacheDir().getAbsolutePath();///data/data/com.xuanwen.mobile.news/cache
		
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			
			mCacheFile = new File(Constants.Directorys.TEMP + "upldate_temp");
			isInInnerSpace = false;
		} else {
			mCacheFile = new File(context.getCacheDir(), "temp");
			isInInnerSpace = true;
		}

		mDialog = new ProgressDialog(context);
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setIcon(android.R.drawable.ic_dialog_info);
		mDialog.setIndeterminateDrawable(mContext.getResources().getDrawable(R.drawable.update_dialog_bg));
		mDialog.setProgress(0);
		mDialog.setMax(100);
		mDialog.setTitle(title);
		mDialog.setMessage("正在下载...");
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		//系统中关机对话框就是这个属性 
//		mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG); 
		//窗口可以获得焦点，响应操作 
		mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
		//窗口不可以获得焦点，点击时响应窗口后面的界面点击事件 
//		mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);  
		mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				cancel(true);
				Toast.makeText(mContext, "已取消下载", Toast.LENGTH_SHORT).show();
			}
		});
		
		Window window = mDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
//		lp.alpha = 0.5f;// 透明度
//		lp.dimAmount = 1.0f;// 黑暗度
		window.setAttributes(lp);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			DownloadUtils.download(mUrl, mCacheFile, false,
					new DownloadUtils.DownloadListener() {

						@Override
						public void downloading(int progress) {
							publishProgress(progress);
						}

						@Override
						public void downloaded() {

						}

						@Override
						public boolean isCanceled() {
							return false;
						}
					});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		mDialog.dismiss();
		if (result != null && result) {
			if (listener != null) {
				listener.onSecuses();
			}
			install(mCacheFile.getAbsolutePath());
		} else {
			showAlertDialog("提示", "下载失败");
			if (listener != null) {
				listener.onFail();
			}
		}
	}

	private void showAlertDialog(String title, String message) {
		/*AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(false)
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if (listener != null) {
					listener.onFail();
				}
			}
		});
		builder.show();*/
//		new MyDialog(mContext, title, message, "确定", "取消", new MyDialog.MyOnClickListener() {
//
//			@Override
//			public void myOnClick(View view) {
//				switch (view.getId()) {
//				case R.id.dialog_ok:
//					if (listener != null) {
//						listener.onFail();
//					}
//					break;
//				default:
//					break;
//				}
//			}
//		}).show();
		
		Toast.makeText(mContext, "更新失败", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		int progress = values[0];
		mDialog.setProgress(progress);
	}

	/**
	 * 
	 * @param context
	 *            上下文环境
	 * @param cachePath
	 *            安装文件路径
	 */
	private void install(String cachePath) {
		//
		// 修改apk权限
		if (isInInnerSpace) {
			chmod("777", cachePath);
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + cachePath),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	/**
	 * 获取权限
	 * 
	 * @param permission
	 *            权限
	 * @param path
	 *            路径
	 */
	private void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public interface OnLoadListener{
		public void onFail();
		public void onSecuses();
		
	}
}
