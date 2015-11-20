package com.cplatform.xhxw.ui.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class AppCoomentUtil {
	public static Intent getIntent(Context paramContext) {
		StringBuilder localStringBuilder = new StringBuilder().append("market://details?id=");
		String str = paramContext.getPackageName();
		localStringBuilder.append(str);
		Uri localUri = Uri.parse(localStringBuilder.toString());
		return new Intent("android.intent.action.VIEW", localUri);
	}

	public static void start(Context paramContext, String paramString) {
		Uri localUri = Uri.parse(paramString);
		Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
		localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		paramContext.startActivity(localIntent);
	}

	public static boolean judge(Context paramContext, Intent paramIntent) {
		List<ResolveInfo> localList = paramContext.getPackageManager().queryIntentActivities(paramIntent,PackageManager.GET_INTENT_FILTERS);
		if ((localList != null) && (localList.size() > 0)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param activity
	 * @param uri 图片地址
	 * @param aspectX 比例宽
	 * @param aspectY 比例高
	 * @param outputX 裁剪输出宽
	 * @param outputY 裁剪输出高
	 * @param requestCode 请求码
	 */
	public static void cropImageUri(Activity activity, Uri uri, int aspectX, int aspectY, int outputX, int outputY, int requestCode){
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		intent.putExtra("crop", "true");
//		
//		intent.putExtra("aspectX", aspectX);
//		intent.putExtra("aspectY", aspectY);
//		intent.putExtra("outputX", outputX);
//		intent.putExtra("outputY", outputY);
//		
//		intent.putExtra("scale", true);// 去黑边
//		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//		
//		intent.putExtra("return-data", false);
//		
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//		intent.putExtra("noFaceDetection", true); // no face detection
//		
//		activity.startActivityForResult(intent, requestCode);
		
		String string = android.os.Build.MODEL;
		boolean aa = string.contains("Nexus");
		boolean bb = string.contains("HUAWEI");
		/**判断手机型号*/
		if (android.os.Build.MODEL.contains("Nexus")) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
	
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
	
			intent.putExtra("outputX", 150);
			intent.putExtra("outputY", 150);
			intent.putExtra("return-data", false);
			
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true); // no face detection
			 activity.startActivityForResult(intent, requestCode);
		}else {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
			intent.putExtra("outputX", outputX);
			intent.putExtra("outputY", outputY);
			
			intent.putExtra("scale", true);// 去黑边
			intent.putExtra("scaleUpIfNeeded", true);// 去黑边
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			
			intent.putExtra("return-data", false);
			
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true); // no face detection
			
			activity.startActivityForResult(intent, requestCode);
		}
	}

	/**从相册中取图片*/
	public static void pickPhoto(Activity activity, int requestCode) {
//		Intent intent = new Intent();
//		intent.setType("image/*");
//		intent.setAction(Intent.ACTION_GET_CONTENT);
//		activity.startActivityForResult(intent, requestCode);
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		activity.startActivityForResult(intent, requestCode);
	}
	
	/**拍照获取图片*/
	public static void takePhoto(Activity activity, Uri photoUri, int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
		activity.startActivityForResult(intent, requestCode);
	}
	
//	/** 选择图片后，获取图片的路径,进行剪切 */
//	public static void CutOutPictures(final Activity activity, Uri uri, final File targetFile) {
//		if (uri == null) {
//			Toast.makeText(activity, "选择图片文件不正确", Toast.LENGTH_SHORT);
//			return;
//		}
//
//		final String path = getUriPath(uri);
//		if (path == null) {
//			Toast.makeText(activity, "选择图片文件不正确", Toast.LENGTH_SHORT);
//			return;
//		}
//
//		if (ImageUtil.needRotate(activity, path)) {
//			new AsyncTask<Void, Void, Void>() {
//
//				@Override
//				protected Void doInBackground(Void... params) {
//					ImageUtil.rotatedBitmap(activity, path,
//							targetFile.getAbsolutePath());
//					return null;
//				}
//
//				@Override
//				protected void onPreExecute() {
//					super.onPreExecute();
//					// showLoading(R.string.please_wait);
//				}
//
//				@Override
//				protected void onPostExecute(Void result) {
//					super.onPostExecute(result);
//					// hideLoading();
//					// if (!isDestroyed) {
//					mPicTmpPath = cropImage(tempPhotoFile.getAbsolutePath(),
//							false);
//					// }
//				}
//			}.execute();
//		} else {
//			mPicTmpPath = cropImage(path, true);
//		}
//	}
	
	/** 获得uri中的对应的图片路径 */
	public static String getUriPath(Context context, Uri uri) {
		String picPath = null;
		String[] pojo = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, pojo, null, null, null);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			try {
				cursor.close();
			} catch (Exception e) {

			}
		}
		return picPath;
	}
}
