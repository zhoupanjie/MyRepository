package com.cplatform.xhxw.ui.ui.main.saas.photopick;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

/**
 * 图片压缩工具类
 * 来源于网络
 */
public class ImageCompressScaleUtil {

	/**
	 * 质量压缩，将图片大小压缩至400K以下
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 400) { // 循环判断如果压缩后图片是否大于400kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 分辨率压缩，将图片粉笔啊率压缩至720×1280以下
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getCompressedImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		
		float hh = 1280f;// 这里设置高度为1280f
		float ww = 720f;// 这里设置宽度为720f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}
	
	/**
	 * 保存图片到指定路径
	 * @param bit
	 * @param dstPath
	 */
	public static final boolean saveFile(Bitmap bit, String dstPath) {
		File f = new File(dstPath);
		if(f.exists()) {
			f.delete();
		}
		
		try {
			CompressFormat cFortmat = null;
			String exten = dstPath.substring(dstPath.lastIndexOf(".") + 1);
			if(exten.toLowerCase().equals("jpg") || exten.toLowerCase().equals("jpeg")) {
				cFortmat = Bitmap.CompressFormat.JPEG;
			} else {
				cFortmat = Bitmap.CompressFormat.PNG;
			}
			FileOutputStream fos = new FileOutputStream(f);
			bit.compress(cFortmat, 100, fos);
			fos.flush();
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
