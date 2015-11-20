package com.cplatform.xhxw.ui.util;

import java.io.File;
import java.text.DecimalFormat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HuanCunSizeUtil {

	private static HuanCunSizeUtil util;

	private static File file1;
	private static File file2;
	
	private HuanCunSizeUtil(File file1, File file2) {

		this.file1 = file1;
		this.file2 = file2;
	}

	public static HuanCunSizeUtil getInstance(File file1, File file2) {

		util = new HuanCunSizeUtil(file1, file2);
		return util;
	}

	public static String getClearSize(File file1, File file2) {
		try {
			return FormetFileSize(addSize(getFileSize(file1),
					getFileSize(file2)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0.0M";
	}
	
	public static String getClearSize() {
		try {
			return FormetFileSize(addSize(getFileSize(file1),
					getFileSize(file2)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0.0M";
	}

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
		// return size / 1048576;
		return size;
	}

	/**
	 * 参数的大小为Bit 返回的数据的单位也是Bit
	 * */
	private static long addSize(Long fileSize1, long fileSize2) {
		return fileSize1 + fileSize2;
	}

	/**
	 * 转换文件大小, 参数的大小为Bit
	 * */
	private static String FormetFileSize(long fileSize) {
		// 转换为bit
		long fileS = fileSize;

		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS == 0) {
			return "0.0M";
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
	 * 异步获取缓存文件size
	 * @param file1
	 * @param file2
	 * @param cacheHandler
	 */
	public static void getCachedFileSize(final File file1, final File file2, 
			final Handler cacheHandler) {
		new Thread(){
			@Override
			public void run() {
				String size = "0.0 M";
				try {
					size = FormetFileSize(addSize(getFileSize(file1),
							getFileSize(file2)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(cacheHandler != null) {
					Message msg = new Message();
					msg.what = 0;
					Bundle bun = new Bundle();
					bun.putString("size", size);
					msg.setData(bun);
					cacheHandler.sendMessage(msg);
				}
			}
		}.start();
	}
}
