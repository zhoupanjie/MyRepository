package com.cplatform.xhxw.ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/** 下载工具类 */
public class DownloadUtils {
	private static final int CONNECT_TIMEOUT = 10000;
	private static final int DATA_TIMEOUT = 40000;
	private final static int DATA_BUFFER = 1024 * 10;

	public interface DownloadListener {
		public void downloading(int progress);

		public void downloaded();

		public boolean isCanceled();
	}

	public static long download(String urlStr, File dest, boolean append,
			DownloadListener downloadListener) throws Exception {
		int downloadProgress = 0;
		long remoteSize = 0;
		long currentSize = 0;
		long totalSize = -1;

		File parentFile = dest.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs();
		}

		if (!append || !dest.isFile()) {
			dest.delete();
		}

		if (append && dest.exists()) {
			currentSize = dest.length();
		}

		HttpGet request = new HttpGet(urlStr);

		if (currentSize > 0) {
			request.addHeader("Range", "bytes=" + currentSize + "-");
		}

		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, DATA_TIMEOUT);
		HttpClient httpClient = new DefaultHttpClient(params);

		InputStream is = null;
		FileOutputStream os = null;

		boolean isCanceled = false;

		try {
			HttpResponse response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK
					|| statusCode == HttpStatus.SC_PARTIAL_CONTENT) {

				boolean isBreak = false;
				if (statusCode == HttpStatus.SC_PARTIAL_CONTENT) {
					isBreak = true;
				}
				Header[] hs = response.getAllHeaders();
				if (hs != null) {
					for (int i = 0; i < hs.length; i++) {
						String headerName = hs[i].getName();
						if ("Accept-Ranges".equalsIgnoreCase(headerName)
								|| "Accept-Range".equalsIgnoreCase(headerName)
								|| "Content-Ranges"
										.equalsIgnoreCase(headerName)
								|| "Content-Range".equalsIgnoreCase(headerName)) {
							isBreak = true;
						}
					}
				}

				remoteSize = response.getEntity().getContentLength(); // maxo0
				if (isBreak) {
					remoteSize += currentSize;
				}

				is = response.getEntity().getContent();
				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					is = new GZIPInputStream(is);
				}
				os = new FileOutputStream(dest, isBreak);
				byte buffer[] = new byte[DATA_BUFFER];
				int readSize = 0;
				while ((readSize = is.read(buffer)) != -1) {
					os.write(buffer, 0, readSize);
					os.flush();
					totalSize += readSize;
					currentSize += readSize;
					downloadProgress = (int) (currentSize * 100 / remoteSize);
					// LogUtil.d("currentSize = " + currentSize);
					if (downloadListener != null) {
						downloadListener.downloading(downloadProgress);
					}
					if (isCanceled(downloadListener)) {
						isCanceled = true;
						break;
					}
				}
				if (totalSize < 0) {
					totalSize = 0;
				}
			} else {
				throw new Exception("网络请求失败:" + urlStr);
			}

		} catch (Exception e) {
			if (!isCanceled(downloadListener)) {
				throw e;
			}
		} finally {
			httpClient.getConnectionManager().shutdown();
			if (os != null) {
				try {
					os.close();
				} catch (Exception e2) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
				}
			}
		}

		if (!isCanceled) {
			if (totalSize < 0) {
				throw new Exception("Download file fail: " + urlStr);
			}

			if (downloadListener != null) {
				downloadListener.downloaded();
			}
		}

		return totalSize;
	}

	/**
	 * 是否被取消了
	 * 
	 * @param downloadListener
	 * @return
	 */
	private static boolean isCanceled(DownloadListener downloadListener) {
		return Thread.currentThread().isInterrupted()
				|| (downloadListener != null && downloadListener.isCanceled());
	}
}
