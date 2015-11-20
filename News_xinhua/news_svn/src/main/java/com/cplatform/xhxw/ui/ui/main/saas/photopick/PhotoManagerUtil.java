package com.cplatform.xhxw.ui.ui.main.saas.photopick;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;

import com.cplatform.xhxw.ui.ui.main.saas.photopick.entities.PhotoItem;

public class PhotoManagerUtil {

	/**
	 * 获取所有图片的id，文件路径以及其缩略图路径
	 * @param con
	 * @return
	 */
	public static final ArrayList<PhotoItem> getAllPhotosThumb(Context con) {
		ArrayList<PhotoItem> photos = new ArrayList<PhotoItem>();
		ContentResolver cr = con.getContentResolver();
        String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
                Thumbnails.DATA };
        
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
                Thumbnails.KIND + "=" + Thumbnails.MINI_KIND, null, 
                Thumbnails.IMAGE_ID + " DESC");
        //获取照片id和缩略图的映射
        HashMap<String, String> photoIdVSThumbMap = new HashMap<String, String>();
        if (cursor != null && cursor.moveToFirst()) {
            String imageId;
            String thumbPath;
            int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);
            do {
            	imageId = cursor.getString(image_idColumn);
                thumbPath = cursor.getString(dataColumn);
                photoIdVSThumbMap.put(imageId, thumbPath);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        //建立照片id，文件路径以及缩略图的映射
        cursor = MediaStore.Images.Media.query(con.getContentResolver(),
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA},
				null, MediaStore.Images.Media.DATE_ADDED + " DESC");
		while (cursor != null && cursor.moveToNext()) {
			String id = cursor.getString(0);
			String path = cursor.getString(1);
			String thumbPath = photoIdVSThumbMap.get(id);
			PhotoItem photo = new PhotoItem();
            photo.setImageId(id);
            photo.setFliePath(path);
            photo.setThumbPath(thumbPath);
            photos.add(photo);
		}
		if(cursor != null) {
			cursor.close();
		}
        
        return photos;
	}
	
	/**
	 * 获取图片原路径
	 * @param con
	 * @param imageId
	 * @return
	 */
	public static final String getImagePathById(Context con, int imageId) {
		Cursor cursor = MediaStore.Images.Media.query(con.getContentResolver(), 
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, 
				MediaStore.Images.Media._ID + "=" + imageId, null);
		if(cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		cursor.close();
		return null;
	}
	
	/**
	 * 根据图片路径获取图片_ID
	 * @param con
	 * @param imagePath
	 * @return
	 */
	public static final String getImageIdByPath(Context con, String imagePath) {
		Cursor cursor = MediaStore.Images.Media.query(con.getContentResolver(), 
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				new String[]{MediaStore.Images.Media._ID}, 
				MediaStore.Images.Media.DATA + "='" + imagePath + "'", null);
		if(cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		cursor.close();
		return null;
	}
	
	/**
	 * 根据图片_ID获取图片缩略图路径
	 * @param con
	 * @param imageId
	 * @return
	 */
	public static final String getImageThumbById(Context con, String imageId) {
		Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(con.getContentResolver(), 
				Long.valueOf(imageId), Thumbnails.MINI_KIND, 
				new String[]{MediaStore.Images.Thumbnails.DATA});
		
		if(cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		cursor.close();
		return null;
	}
}
