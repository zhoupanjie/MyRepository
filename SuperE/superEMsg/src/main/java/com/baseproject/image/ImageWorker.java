/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baseproject.image;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.hy.superemsg.BuildConfig;
import com.hy.superemsg.utils.UIUtils;

/**
 * This class wraps up completing some arbitrary long running work when loading
 * a bitmap to an ImageView. It handles things like using a memory and disk
 * cache, running the work in a background thread and setting a placeholder
 * image.
 */
public abstract class ImageWorker {
	private static final String TAG = "ImageWorker";
	private static final int FADE_IN_TIME = 200;
	/**默认图片压缩比 */
	private static int INSAMPLESIZE = 1; 
	
	private ImageCache mImageCache;
	private Bitmap mLoadingBitmap;
	private boolean mFadeInBitmap = true;
	private boolean mExitTasksEarly = false;

	protected Context mContext;
	protected ImageWorkerAdapter mImageWorkerAdapter;
 
	protected ImageWorker(Context context) {
		mContext = context;
	}

	/**
	 * 设置默认的图片采样比
	 * @param inSampleSize
	 */
	public static  void setInsampleSize(int inSampleSize) {
		INSAMPLESIZE = inSampleSize;
	}
	
	/**
	 * 获取图片采样比
	 * @return
	 */
	public static int getInsampleSize() {
		return INSAMPLESIZE;
	}
	/**
	 * Load an image specified by the data parameter into an ImageView (override
	 * {@link ImageWorker#processBitmap(Object)} to define the processing
	 * logic). A memory and disk cache will be used if an {@link ImageCache} has
	 * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
	 * image is found in the memory cache, it is set immediately, otherwise an
	 * {@link AsyncTask} will be created to asynchronously load the bitmap.
	 * 
	 * @param data
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void loadImage(Object data, ImageView imageView) {
		Bitmap bitmap = null;

		if (mImageCache != null) {
			bitmap = mImageCache.getBitmapFromMemCache(String.valueOf(data));
		}

		if (bitmap != null) {
			// Bitmap found in memory cache
			if (null != imageView) {
				imageView.setImageBitmap(bitmap);
			}
		} else if (cancelPotentialWork(data, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(),
					mLoadingBitmap, task);
			if (null != imageView) {
				imageView.setImageDrawable(asyncDrawable);
			}
			task.execute(data);
		}
	}

	public void loadImage(Object data, ImageView imageView, ImageCallback imageCallback) {
		Bitmap bitmap = null;

		if (mImageCache != null) {
			bitmap = mImageCache.getBitmapFromMemCache(String.valueOf(data));
		}

		if (bitmap != null) {
			// Bitmap found in memory cache
			if (null != imageView) {
				imageView.setImageBitmap(bitmap);
			}
			if (imageCallback != null) {
				imageCallback.imageLoaded(bitmap, String.valueOf(data));
			}
		} else if (cancelPotentialWork(data, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView, imageCallback);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(),
					mLoadingBitmap, task);
			if (null != imageView) {
				imageView.setImageDrawable(asyncDrawable);
			}
			task.execute(data);
		}
	}

	/**
	 * 批量下载图片使用，底层使用AsyncTask
	 * @param data  图片下载url
	 * @param imageCallback  回调接口
	 */
//	public void loadImage(Object data, ImageCallback imageCallback) {
//		Bitmap bitmap = null;
//
//		if (mImageCache != null) {
//			bitmap = mImageCache.getBitmapFromMemCache(String.valueOf(data));
//		}
//
//		if (bitmap != null) {
//			// Bitmap found in memory cache
//			
//			if (imageCallback != null) {
//				imageCallback.imageLoaded(bitmap, String.valueOf(data));
//			}
//		} else {
//			final BitmapTask task = new BitmapTask(imageCallback);
//		
//			task.execute(data);
//		}
//	}
	/**
	 * Load an image specified from a set adapter into an ImageView (override
	 * {@link ImageWorker#processBitmap(Object)} to define the processing
	 * logic). A memory and disk cache will be used if an {@link ImageCache} has
	 * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
	 * image is found in the memory cache, it is set immediately, otherwise an
	 * {@link AsyncTask} will be created to asynchronously load the bitmap.
	 * {@link ImageWorker#setAdapter(ImageWorkerAdapter)} must be called before
	 * using this method.
	 * 
	 * @param data
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void loadImage(int num, ImageView imageView) {
		if (mImageWorkerAdapter != null) {
			loadImage(mImageWorkerAdapter.getItem(num), imageView);
		} else {
			throw new NullPointerException("Data not set, must call setAdapter() first.");
		}
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param bitmap
	 */
	public void setLoadingImage(Bitmap bitmap) {
		mLoadingBitmap = bitmap;
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param resId
	 */
	public void setLoadingImage(int resId) {
		mLoadingBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
	}

	/**
	 * Set the {@link ImageCache} object to use with this ImageWorker.
	 * 
	 * @param cacheCallback
	 */
	public void setImageCache(ImageCache cacheCallback) {
		mImageCache = cacheCallback;
	}

	public ImageCache getImageCache() {
		return mImageCache;
	}

	/**
	 * If set to true, the image will fade-in once it has been loaded by the
	 * background thread.
	 * 
	 * @param fadeIn
	 */
	public void setImageFadeIn(boolean fadeIn) {
		mFadeInBitmap = fadeIn;
	}

	public void setExitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
	}

	/**
	 * Subclasses should override this to define any processing or work that
	 * must happen to produce the final bitmap. This will be executed in a
	 * background thread and be long running. For example, you could resize a
	 * large bitmap here, or pull down an image from the network.
	 * 
	 * @param data
	 *            The data to identify which image to process, as provided by
	 *            {@link ImageWorker#loadImage(Object, ImageView)}
	 * @return The processed bitmap
	 */
	protected abstract Bitmap processBitmap(Object data);

	public static void cancelWork(ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null) {
			bitmapWorkerTask.cancel(true);
			if (BuildConfig.DEBUG) {
				final Object bitmapData = bitmapWorkerTask.data;
				Log.d(TAG, "cancelWork - cancelled work for " + bitmapData);
			}
		}
	}

	/**
	 * Returns true if the current work has been canceled or if there was no
	 * work in progress on this image view. Returns false if the work in
	 * progress deals with the same data. The work is not stopped in that case.
	 */
	public static boolean cancelPotentialWork(Object data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final Object bitmapData = bitmapWorkerTask.data;
			if (bitmapData == null || !bitmapData.equals(data)) {
				bitmapWorkerTask.cancel(true);
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "cancelPotentialWork - cancelled work for " + data);
				}
			} else {
				// The same work is already in progress.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active work task (if any) associated with
	 *         this imageView. null if there is no such task.
	 */
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * The actual AsyncTask that will asynchronously process the image.
	 */
	private class BitmapWorkerTask extends ImageAsyncTask<Object, Void, Bitmap> {
		private Object data;
		private final WeakReference<ImageView> imageViewReference;
		private ImageCallback imageCallback;

		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		public BitmapWorkerTask(ImageView imageView, ImageCallback imageCallback) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			this.imageCallback = imageCallback;
		}

		/**
		 * Background processing.
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			data = params[0];
			final String dataString = String.valueOf(data);
			Bitmap bitmap = null;

			// If the image cache is available and this task has not been
			// cancelled by another
			// thread and the ImageView that was originally bound to this task
			// is still bound back
			// to this task and our "exit early" flag is not set then try and
			// fetch the bitmap from
			// the cache
			if (mImageCache != null && !isCancelled() && getAttachedImageView() != null
					&& !mExitTasksEarly) {
				bitmap = mImageCache.getBitmapFromDiskCache(dataString);
			}

			// If the bitmap was not found in the cache and this task has not
			// been cancelled by
			// another thread and the ImageView that was originally bound to
			// this task is still
			// bound back to this task and our "exit early" flag is not set,
			// then call the main
			// process method (as implemented by a subclass)
			if (bitmap == null && !isCancelled() && getAttachedImageView() != null
					&& !mExitTasksEarly) {
				bitmap = processBitmap(params[0]);
			}

			// If the bitmap was processed and the image cache is available,
			// then add the processed
			// bitmap to the cache for future use. Note we don't check if the
			// task was cancelled
			// here, if it was, and the thread is still running, we may as well
			// add the processed
			// bitmap to our cache as it might be used again in the future
			if (null != mImageCache) {
				if (null != bitmap && !bitmap.isRecycled()) {
					mImageCache.addBitmapToCache(dataString, bitmap);
				} else if (null != mImageCache.getDiskCache()) {
					mImageCache.getDiskCache().removeFile(dataString);
				}
			}

			return bitmap;
		}

		/**
		 * Once the image is processed, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// if cancel was called on this task or the "exit early" flag is set
			// then we're done
			if (isCancelled() || mExitTasksEarly) {
				bitmap = null;
			}

			final ImageView imageView = getAttachedImageView();
			if (bitmap != null && imageView != null) {
				setImageBitmap(imageView, bitmap);
			}
			if (null != imageCallback) {
				imageCallback.imageLoaded(bitmap, String.valueOf(data));
			}
		}

		/**
		 * Returns the ImageView associated with this task as long as the
		 * ImageView's task still points to this task as well. Returns null
		 * otherwise.
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();
			final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

	/**
	 * 带回调的下载线程
	 * @author liubing
	 *
	 */
//	private class BitmapTask extends AsyncTask<Object, Void, Bitmap> {
//		private Object data;
//		private ImageCallback imageCallback = null;
//
//		public BitmapTask(ImageCallback imageCallback) {
//			this.imageCallback = imageCallback;
//		}
//
//		/**
//		 * Background processing.
//		 */
//		@Override
//		protected Bitmap doInBackground(Object... params) {
//			data = params[0];
//			final String dataString = String.valueOf(data);
//			Bitmap bitmap = null;
//
//			// If the image cache is available and this task has not been
//			// cancelled by another
//			// thread and the ImageView that was originally bound to this task
//			// is still bound back
//			// to this task and our "exit early" flag is not set then try and
//			// fetch the bitmap from
//			// the cache
//			if (mImageCache != null && !isCancelled()
//					&& !mExitTasksEarly) {
//				bitmap = mImageCache.getBitmapFromDiskCache(dataString);
//			}
//
//			// If the bitmap was not found in the cache and this task has not
//			// been cancelled by
//			// another thread and the ImageView that was originally bound to
//			// this task is still
//			// bound back to this task and our "exit early" flag is not set,
//			// then call the main
//			// process method (as implemented by a subclass)
//			if (bitmap == null && !isCancelled() && !mExitTasksEarly) {
//				bitmap = processBitmap(params[0]);
//			}
//
//			// If the bitmap was processed and the image cache is available,
//			// then add the processed
//			// bitmap to the cache for future use. Note we don't check if the
//			// task was cancelled
//			// here, if it was, and the thread is still running, we may as well
//			// add the processed
//			// bitmap to our cache as it might be used again in the future
//			if (null != mImageCache) {
//				if (null != bitmap) {
//					mImageCache.addBitmapToCache(dataString, bitmap);
//				} else if (null != mImageCache.getDiskCache()) {
//					mImageCache.getDiskCache().removeFile(dataString);
//				}
//			}
//
//			return bitmap;
//		}
//
//		/**
//		 * Once the image is processed, associates it to the imageView
//		 */
//		@Override
//		protected void onPostExecute(Bitmap bitmap) {
//			// if cancel was called on this task or the "exit early" flag is set
//			// then we're done
//			if (isCancelled() || mExitTasksEarly) {
//				bitmap = null;
//			}
//			if (null != imageCallback) {
//				imageCallback.imageLoaded(bitmap, String.valueOf(data));
//			}
//		}
//
//	}

	/**
	 * A custom Drawable that will be attached to the imageView while the work
	 * is in progress. Contains a reference to the actual worker task, so that
	 * it can be stopped if a new binding is required, and makes sure that only
	 * the last started worker process can bind its result, independently of the
	 * finish order.
	 */
	private static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);

			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	/**
	 * Called when the processing is complete and the final bitmap should be set
	 * on the ImageView.
	 * 
	 * @param imageView
	 * @param bitmap
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
		if (mFadeInBitmap) {
			// Transition drawable with a transparent drwabale and the final
			// bitmap
			TransitionDrawable td = new TransitionDrawable(new Drawable[] {
					new ColorDrawable(android.R.color.transparent),
					new BitmapDrawable(mContext.getResources(), bitmap) });
			final WeakReference<TransitionDrawable> tdReference = new WeakReference<TransitionDrawable>(
					td);
			td = null;
			// Set background to loading bitmap
			/**
			 * modify：by liubing
			 * 说明：如果不设置默认loading图的话，不再使用背景设置
			 */
			if (mLoadingBitmap != null) {
				final BitmapDrawable bd = new BitmapDrawable(mContext.getResources(), mLoadingBitmap);
				if (UIUtils.hasJellyBean()) {
					imageView.setBackground(bd);
				} else {
					imageView.setBackgroundDrawable(bd);

				}
			}
			
			if (null != tdReference.get()) {
				imageView.setImageDrawable(tdReference.get());
				tdReference.get().startTransition(FADE_IN_TIME);
			}
		} else {
			imageView.setImageBitmap(bitmap);
		}

	}

	/**
	 * Set the simple adapter which holds the backing data.
	 * 
	 * @param adapter
	 */
	public void setAdapter(ImageWorkerAdapter adapter) {
		mImageWorkerAdapter = adapter;
	}

	/**
	 * Get the current adapter.
	 * 
	 * @return
	 */
	public ImageWorkerAdapter getAdapter() {
		return mImageWorkerAdapter;
	}

	/**
	 * A very simple adapter for use with ImageWorker class and subclasses.
	 */
	public static abstract class ImageWorkerAdapter {
		public abstract Object getItem(int num);

		public abstract int getSize();
	}
}
