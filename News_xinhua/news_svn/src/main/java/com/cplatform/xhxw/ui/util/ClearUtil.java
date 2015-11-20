package com.cplatform.xhxw.ui.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

public class ClearUtil extends AsyncTask<Void, Void, Boolean>{

	private BaseActivity context;
	private File file1;
	private File file2;
	private String fSize;
	private OnClearListener listener;
	
	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long 单位为Bit
	 * @throws Exception
	 */
	private static long getFileSize(File file) throws Exception {
		long size = 0;
		if (!file.exists()) {
			return size;
		}
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFileSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
//		return size / 1048576;
		return size;
	}

	/**
	 * 删除指定目录下文件及目录
	 * 
	 * @param deleteThisPath
	 * @param filepath
	 * @return
	 */
	private static void deleteFolderFile(String filePath, boolean deleteThisPath)
			throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);

			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFolderFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// 如果是文件，删除
					file.delete();
				} else {// 目录
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}
	
	/**
	 * 删除目录下的所有文件
	 * 
	 * @param dir
	 * @param deleteNoMediaFile
	 *            是否删除".nomedia"文件
	 */
	public static void deleteFilesOfDirectory(File dir,
			boolean deleteNoMediaFile) {
		if (dir == null || !dir.exists()) {
			return;
		}
		if (dir.isDirectory()) {
			File[] subFiles = dir.listFiles();
			if (subFiles != null) {
				for (File file : subFiles) {
					deleteFilesOfDirectory(file, deleteNoMediaFile);
				}
			}
		} else {
			if (!".nomedia".equals(dir.getName())) {
				dir.delete();
			} else if (deleteNoMediaFile) {
				dir.delete();
			}
		}
	}
	
	
	
	/**
	 * 删除缓存的数据
	 * */
	public static void deleteFolderFile(){
		ImageLoader.getInstance().clearDiscCache();
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	/**
	 * 转换文件大小, 参数的大小为Bit
	 * */
	private static String FormetFileSize(long fileSize) {
		//转换为bit
		long fileS = fileSize;
		
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS == 0) {
			return "0.00b";
		}
		
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	
	/**
	 * 参数的大小为Bit
	 * 返回的数据的单位也是Bit
	 * */
	private static long addSize(Long fileSize1, long fileSize2) {
		return fileSize1 + fileSize2;
	}
	
	
	public static String getClearSize(File file1, File file2) {
		try {
			return FormetFileSize(addSize(getFileSize(file1), getFileSize(file2)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return "0.0M";
	}

	
	public ClearUtil(BaseActivity context, File file1, File file2) {
		super();
		this.context = context;
		this.file1 = file1;
		this.file2 = file2;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (listener != null) {
			listener.onPreClear();
		}
		
		fSize = getClearSize(file1, file2);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		
		deleteFolderFile();
		deleteFilesOfDirectory(file1, false);
		deleteFilesOfDirectory(file2, false);
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		
		if (result == true) {
			if ("0".equals(fSize)) {
				Toast.makeText(context, "不需要清理了", Toast.LENGTH_LONG).show();
			}else {
//				Toast.makeText(context, "清除了" + fSize + "数据", Toast.LENGTH_LONG).show();
			}
		}
		
		if (listener != null) {
			listener.onPostClear();
		}
		
		super.onPostExecute(result);
	}
	
	public interface OnClearListener{
		public void onPreClear();
		public void onPostClear();
	}
	
	public void setClearListener(OnClearListener listener) {
		this.listener = listener;
	}
	
}
