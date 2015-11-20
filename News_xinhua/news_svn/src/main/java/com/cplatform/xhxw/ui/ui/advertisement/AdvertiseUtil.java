package com.cplatform.xhxw.ui.ui.advertisement;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.Toast;

import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.AdvertisementDB;
import com.cplatform.xhxw.ui.db.dao.AdvertisementDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.GetAdvertisementResponse;
import com.cplatform.xhxw.ui.model.Ad;
import com.cplatform.xhxw.ui.model.AdvertismentsResponse;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AdvertiseUtil {

	/**
	 * 获取loading页广告
	 * 
	 * @param context
	 */
	public static void startFetchLoadingAdver(final Context context) {
		APIClient.getLoadingAdvertisements(new BaseRequest(),
				new AsyncHttpResponseHandler() {
					AsyncHttpResponseHandler getHandler;
					Context mCon = context.getApplicationContext();
					@Override
					public void onFinish() {
						getHandler = null;
						AdvertisementDB.deleteDirtAdver(context);
					}

					@Override
					protected void onPreExecute() {
						if (getHandler != null)
							getHandler.cancle();
						getHandler = this;
					}

					@Override
					public void onSuccess(String content) {
						GetAdvertisementResponse response;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content, GetAdvertisementResponse.class);
						} catch (Exception e) {
							e.printStackTrace();
							showToast(mCon, mCon
									.getString(R.string.data_format_error));
							return;
						}
						if (response != null) {
							if (response.isSuccess()) {
								AdvertismentsResponse adver = response.getData();
								DealWithLoadingAdverResponse(mCon, adver.getTop(), true);
								DealWithLoadingAdverResponse(mCon, adver.getContent(), false);
							} else {
								showToast(mCon, response.getMsg());
							}
						} else {
							AdvertisementDB.deleteAdverByShowPosition(mCon, AdvertisementDao.ADVER_SHOW_POSITION_LOADING);
							AdvertisementDB.deleteAdverByShowPosition(mCon, AdvertisementDao.ADVER_SHOW_POSITION_BOTTOM);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// mDefView.setStatus(DefaultView.Status.error);
					}
				});
	}
	
	public static void DealWithLoadingAdverResponse(Context con, List<Ad> list, boolean isTopAdver) {
		if(list == null || list.size() <= 0) {
			AdvertisementDB.setDirtAdverByShowPosition(con, 
					isTopAdver ? AdvertisementDao.ADVER_SHOW_POSITION_LOADING : 
						AdvertisementDao.ADVER_SHOW_POSITION_BOTTOM);
			return;
		}
		AdvertisementDao ad = switchAdToAdDao(list.get(0), isTopAdver);
		if(AdvertisementDB.isAdverExist(con, ad)) {
			if(!isAdverImgExist(con, ad)) {
				getAdverImgFromServer(con, ad, isTopAdver);
			}
			return;
		} else {
			AdvertisementDB.setDirtAdverByShowPosition(con, 
					isTopAdver ? AdvertisementDao.ADVER_SHOW_POSITION_LOADING : 
						AdvertisementDao.ADVER_SHOW_POSITION_BOTTOM);
			AdvertisementDB.saveAdvertisements(con, ad);
			getAdverImgFromServer(con, ad, isTopAdver);
		}
	}

	/**
	 * 转换广告为广告数据库对象
	 * 
	 * @param data
	 * @return
	 */
	public static AdvertisementDao switchAdToAdDao(Ad data, boolean isAdvaerTop) {
		AdvertisementDao adverDao = new AdvertisementDao();
		adverDao.setAdverId(data.getId());
		adverDao.setAdverEndTime(Long.valueOf(data.getEnd()));
		adverDao.setAdverTitle(data.getTitle());
		adverDao.setAdverTypeId(Integer.valueOf(data.getTypeid()));
		adverDao.setAdverUrl(data.getUrl());
		adverDao.setAdverShortUrl(data.getShorturl());
		adverDao.setAdverImg(data.getAndroidimg());
		adverDao.setAdverRank(Integer.valueOf(data.getRank()));
		if(isAdvaerTop) {
			adverDao.setAdverShowPosition(AdvertisementDao.ADVER_SHOW_POSITION_LOADING);
		} else {
			adverDao.setAdverShowPosition(AdvertisementDao.ADVER_SHOW_POSITION_BOTTOM);
		}
		return adverDao;
	}

	/**
	 * 显示toast
	 */
	public static void showToast(Context context, String msg) {
		Toast mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
		mToast.setText(msg);
		mToast.show();
	}

	/**
	 * 联网下载文件
	 * 
	 * @param context
	 *            上下文
	 * @param isTopAdver 
	 * @param dao
	 *            任务
	 * @param downDir
	 *            下载目录
	 * @param fileName
	 *            保存文件名称
	 * @return 文件
	 * @throws Exception
	 */
	public static void getAdverImgFromServer(final Context context,
			final AdvertisementDao adver, final boolean isTopAdver) {
		new Thread() {

			@Override
			public void run() {
				HttpURLConnection conn;
				File file = null;
				File tmpDownFile = null;
				InputStream is = null;
				FileOutputStream fos = null;
				BufferedInputStream bis = null;
				PreferencesManager.setAdverCacheStatus(context, false, isTopAdver);
				boolean isToDeleteTmpFinally = true;
				try {
					conn = (HttpURLConnection) new URL(adver.getAdverImg())
							.openConnection();
					conn.setConnectTimeout(5000);
					// 获取到文件的大小
					int max = conn.getContentLength();
					is = conn.getInputStream();
					file = generateAdverImgSaveFile(context, adver);
					tmpDownFile = new File(file.getAbsolutePath() + ".bak");
					if(tmpDownFile.exists()) {
						isToDeleteTmpFinally = false;
						return;
					}
					tmpDownFile.createNewFile();
					fos = new FileOutputStream(tmpDownFile);
					bis = new BufferedInputStream(is);
					byte[] buffer = new byte[1024];
					int len;
					int total = 0;
					while ((len = bis.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						total += len;
					}

					if (total == max) {
						tmpDownFile.renameTo(file);
					}
					PreferencesManager.setAdverCacheStatus(context, true, isTopAdver);
				} catch (MalformedURLException e) {
					LogUtil.w(getName(), e);
				} catch (IOException e) {
					LogUtil.w(getName(), e);
				} finally {
					if(tmpDownFile != null && isToDeleteTmpFinally) {
						tmpDownFile.delete();
					}
					try {
						fos.close();
						bis.close();
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				super.run();
			}

		}.start();
	}

	/**
	 * 生成广告图片缓存路径
	 * 
	 * @param context
	 * @param adver
	 * @return
	 */
	public static File generateAdverImgSaveFile(Context context,
			AdvertisementDao adver) {
		File saveDir = context.getCacheDir();
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		String[] imgUrlPeices = adver.getAdverImg().split("/");
		String saveFileName = adver.getAdverId() + "_" + imgUrlPeices[imgUrlPeices.length - 1];
		File result = new File(saveDir, saveFileName);
		return result;
	}

	/**
	 * 删除缓存的广告图片
	 * 
	 * @param context
	 * @param adver
	 */
	public static void deleteCachedAdverImg(Context context,
			AdvertisementDao adver) {
		File cachedImagFile = generateAdverImgSaveFile(context, adver);
		if (cachedImagFile.exists()) {
			cachedImagFile.delete();
		}
	}

	/**
	 * 检查广告是否存在缓存图片
	 * 
	 * @param context
	 * @param adver
	 * @return
	 */
	public static boolean isAdverImgExist(Context context,
			AdvertisementDao adver) {
		return generateAdverImgSaveFile(context, adver).exists();
	}
	
	public static Bitmap scaleImgSize(File imgPath) {
		double maxSize = 1024 * 1024;
		double fileSize = imgPath.length();
		int scale = 1;
		double fileSizeAfterScale = fileSize;
		int scaleTime = 1;
		while(fileSizeAfterScale > maxSize) {
			scale = (int) Math.pow(2, scaleTime);
			fileSizeAfterScale = fileSize * ((1.0 / scale) * (1.0 / scale));
			scaleTime++;
		}
		BitmapFactory.Options opt = new Options();
		opt.inJustDecodeBounds = false;
		opt.inSampleSize = scale;
		Bitmap btp = BitmapFactory.decodeFile(imgPath.getAbsolutePath(), opt);
		return btp;
	}
}
