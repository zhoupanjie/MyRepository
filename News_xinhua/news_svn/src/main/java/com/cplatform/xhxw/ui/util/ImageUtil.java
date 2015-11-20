package com.cplatform.xhxw.ui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.widget.Toast;

public class ImageUtil {

	/**
	 * 判断图片是否需要翻转（正方向的不用翻转）
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean needRotate(String filePath) {
		try {
			ExifInterface exif = new ExifInterface(filePath);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				return true;
			case ExifInterface.ORIENTATION_ROTATE_270:
				return true;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return true;
			}

			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取翻转到正方向的的图片
	 * 
	 * @param filePath
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static void rotatedBitmap(Context context, String filePath,
			String dstFilePath) {
		try {

			File file = new File(dstFilePath);
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			ExifInterface exif = new ExifInterface(filePath);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			Matrix matrix = null;

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				matrix = new Matrix();
				matrix.postRotate(90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				matrix = new Matrix();
				matrix.postRotate(270);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				matrix = new Matrix();
				matrix.postRotate(180);
				break;
			}

			Bitmap bmp = getSmallBitmap(context,
					Uri.fromFile(new File(filePath)));
			if (matrix != null) {
				Bitmap bmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), matrix, true);
				bmp.recycle();

				FileOutputStream fos = new FileOutputStream(dstFilePath);
				bmp2.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
				bmp2.recycle();
			} else {
				FileOutputStream fos = new FileOutputStream(dstFilePath);
				FileInputStream fis = new FileInputStream(filePath);

				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = fis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				fis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
	    double w = options.outWidth;  
	    double h = options.outHeight;  
	  
	    int lowerBound = (maxNumOfPixels == -1) ? 1 :  
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
	    int upperBound = (minSideLength == -1) ? 128 :  
	            (int) Math.min(Math.floor(w / minSideLength),  
	            Math.floor(h / minSideLength));  
	  
	    if (upperBound < lowerBound) {  
	        // return the larger one when there is no overlapping zone.  
	        return lowerBound;  
	    }  
	  
	    if ((maxNumOfPixels == -1) &&  
	            (minSideLength == -1)) {  
	        return 1;  
	    } else if (minSideLength == -1) {  
	        return lowerBound;  
	    } else {  
	        return upperBound;  
	    }  
	}  
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

//		final int height = options.outHeight;
//		final int width = options.outWidth;
//		int inSampleSize = 1;
//
//		if (height > reqHeight || width > reqWidth) {
//			final int heightRatio = Math.round((float) height
//					/ (float) reqHeight);
//			final int widthRatio = Math.round((float) width / (float) reqWidth);
//			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//		}
//		return inSampleSize;
		
		int initialSize = computeInitialSampleSize(options, reqWidth, reqHeight);  
		  
	    int roundedSize;  
	    if (initialSize <= 8 ) {  
	        roundedSize = 1;  
	        while (roundedSize < initialSize) {  
	            roundedSize <<= 1;  
	        }  
	    } else {  
//	        roundedSize = (initialSize + 7) / 8 * 8; 
	    	roundedSize = (initialSize + 7) / 2;  
	    }  
	  
	    return roundedSize;  

	}

	public static Bitmap getSmallBitmap(Context context, Uri uri) {
//		return getBitmap(context, uri, 800, 1280);
		return getBitmap(context, uri, -1, 128*128);
	}

	public static Bitmap getBitmap(Context context, Uri uri, int maxWidth,
			int maxHeight) {
		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			InputStream is = context.getContentResolver().openInputStream(uri);
			BitmapFactory.decodeStream(is, null, options);
			is.close();

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;

			is = context.getContentResolver().openInputStream(uri);
			Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
			is.close();
			return bmp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	第一：我们先看下质量压缩方法：
	public static void compressImage(Bitmap image, String dstFilePath) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024 > 80) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        image.recycle();
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(dstFilePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.recycle();
    }  
	
//	第二：图片按比例大小压缩方法（根据路径获取图片并压缩）：
	public static void getimage(Context context, String srcPath, String dstFilePath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        if (bitmap != null) {
        	 compressImage(bitmap, dstFilePath);//压缩好比例大小后再进行质量压缩  
		}else {
			Toast.makeText(context, "图片文件选取不正确", Toast.LENGTH_SHORT).show();
			return;
		}
       
    }  
}
