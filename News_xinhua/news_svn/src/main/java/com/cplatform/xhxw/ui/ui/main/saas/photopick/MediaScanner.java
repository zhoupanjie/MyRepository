package com.cplatform.xhxw.ui.ui.main.saas.photopick;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * 多媒体扫描服务调起类，用于立即调起系统多媒体扫描服务对特定多媒体文件<br>
 * 或文件夹进行扫描，以便将对象文件或文件夹数据记录添加到系统多媒体数据库<br>
 * <br>
 * 获取该类对象：MediaScanner.getInstance(Context)<br>
 */
public class MediaScanner {

	private MediaScannerConnection mediaScanConn = null;

	private MusicSannerClient client = null;

	private String filePath = null;
	
	private String fileType = null;
	
	private String[] filePaths = null;
	
	private static MediaScanner instance;
    /**
     * 然后调用MediaScanner.scanFile("/sdcard/2.mp3");
     * */
	private MediaScanner(Context context) {
        //创建MusicSannerClient
		if (client == null) {

			client = new MusicSannerClient();
		}

		if (mediaScanConn == null) {

			mediaScanConn = new MediaScannerConnection(context, client);
		}
	}
	
	public static MediaScanner getInstance(Context con) {
		if(instance == null) {
			instance = new MediaScanner(con);
		}
		return instance;
	}

	class MusicSannerClient implements
			MediaScannerConnection.MediaScannerConnectionClient {

		public void onMediaScannerConnected() {
			
			if(filePath != null){
				
				mediaScanConn.scanFile(filePath, fileType);
			}
			
			if(filePaths != null){
				
				for(String file: filePaths){
					
					mediaScanConn.scanFile(file, fileType);
				}
			}
			
			filePath = null;
			
			fileType = null;
			
			filePaths = null;
		}

		public void onScanCompleted(String path, Uri uri) {
			// TODO Auto-generated method stub
			mediaScanConn.disconnect();
		}

	}
	
    /**
     * 扫描文件标签信息
     * @param filePath 文件路径 eg:/sdcard/MediaPlayer/dahai.mp3
     * @param fileType 文件类型 eg: audio/mp3  media/*  application/ogg
     * */
	public void scanFile(String filepath,String fileType) {

		this.filePath = filepath;
		
		this.fileType = fileType;
        //连接之后调用MusicSannerClient的onMediaScannerConnected()方法
		mediaScanConn.connect();
	}
    /**
     * @param filePaths 文件路径
     * @param fileType 文件类型
     * */
	public void scanFile(String[] filePaths,String fileType){
		
		this.filePaths = filePaths;
		
		this.fileType = fileType;
		
		mediaScanConn.connect();
		
	}
	
	public String getFilePath() {

		return filePath;
	}

	public void setFilePath(String filePath) {

		this.filePath = filePath;
	}

	public String getFileType() {
		
		return fileType;
	}

	public void setFileType(String fileType) {
		
		this.fileType = fileType;
	}

	
}