/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.loopj.android.http;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Used to intercept and handle the responses from requests made using
 * {@link AsyncHttpClient}. The {@link #onSuccess(String)} method is designed to
 * be anonymously overridden with your own response handling code.
 * <p>
 * Additionally, you can override the {@link #onFailure(Throwable, String)},
 * {@link #onStart()}, and {@link #onFinish()} methods as required.
 * <p>
 * For example:
 * <p>
 * 
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.get(&quot;http://www.google.com&quot;, new AsyncHttpResponseHandler() {
 * 	&#064;Override
 * 	public void onStart() {
 * 		// Initiated the request
 * 	}
 * 
 * 	&#064;Override
 * 	public void onSuccess(String response) {
 * 		// Successfully got a response
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFailure(Throwable e, String response) {
 * 		// Response failed :(
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFinish() {
 * 		// Completed the request (either success or failure)
 * 	}
 * });
 * </pre>
 */
public class AsyncHttpResponseHandler {
	private static final String TAG = AsyncHttpResponseHandler.class
			.getSimpleName();
	protected static final int SUCCESS_MESSAGE = 0;
	protected static final int FAILURE_MESSAGE = 1;
	protected static final int START_MESSAGE = 2;
	protected static final int FINISH_MESSAGE = 3;
	protected static final int CANCELED_MESSAGE = 4;

	private Handler handler;
	
	private static String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 是否已被取消
	 */
	protected boolean isCancled = false;

	/**
	 * 是否执行成功
	 */
	protected boolean isSurcess = false;

	/**
	 * 在构造方法里执行（在构造方法的线程执行）
	 */
	protected void onPreExecute() {

	}

	/**
	 * 被取消以后调用（调用cancel返回会导致onCanceled被调用）
	 */
	protected void onCanceled() {

	}

	/**
	 * 取消以后，onCanceled会被调用，其他的方法就不会被调用（例如：onStart、onFinish、onSuccess、onFailure等）<br/>
	 * 取消以后，不会中断线程的执行，只是不再调用onCanceled之外的返回方法
	 */
	public void cancle() {
		isCancled = true;
		sendCanceledMessage();
		handler = null;
	}

	public boolean isCancled() {
		return isCancled;
	}

	/**
	 * Creates a new AsyncHttpResponseHandler
	 */
	public AsyncHttpResponseHandler() {
		// Set up a handler to post events back to the correct thread if
		// possible
		if (Looper.myLooper() != null) {
			handler = new MyHandler() {
				public void handleMessage(Message msg) {
					AsyncHttpResponseHandler.this.handleMessage(msg);
				}
			};
		}
		onPreExecute();
	}

	private static class MyHandler extends Handler {

		MyHandler() {
		}

		public void handleMessage(Message msg) {
			this.handleMessage(msg);
		}
	}

	//
	// Callbacks to be overridden, typically anonymously
	//

	/**
	 * Fired when the request is started, override to handle in your own code
	 */
	public void onStart() {
	}

	/**
	 * Fired in all cases when the request is finished, after both success and
	 * failure, override to handle in your own code
	 */
	public void onFinish() {
	}

	/**
	 * Fired when a request returns successfully, override to handle in your own
	 * code
	 * 
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(String content) {
	}

	/**
	 * Fired when a request returns successfully, override to handle in your own
	 * code
	 * 
	 * @param statusCode
	 *            the status code of the response
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(int statusCode, String content) {
		onSuccess(content);
	}

	/**
	 * Fired when a request fails to complete, override to handle in your own
	 * code
	 * 
	 * @param error
	 *            the underlying cause of the failure
	 * @deprecated use {@link #onFailure(Throwable, String)}
	 */
	public void onFailure(Throwable error) {
	}

	/**
	 * Fired when a request fails to complete, override to handle in your own
	 * code
	 * 
	 * @param error
	 *            the underlying cause of the failure
	 * @param content
	 *            the response body, if any
	 */
	public void onFailure(Throwable error, String content) {
		// By default, call the deprecated onFailure(Throwable) for
		// compatibility
		onFailure(error);
	}

	//
	// Pre-processing of messages (executes in background threadpool thread)
	//

	protected void sendSuccessMessage(int statusCode, String responseBody) {
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[] {
				new Integer(statusCode), responseBody }));
	}

	protected void sendFailureMessage(Throwable e, String responseBody) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e,
				responseBody }));
	}

	protected void sendFailureMessage(Throwable e, byte[] responseBody) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e,
				responseBody }));
	}

	protected void sendStartMessage() {
		sendMessage(obtainMessage(START_MESSAGE, null));
	}

	protected void sendFinishMessage() {
		sendMessage(obtainMessage(FINISH_MESSAGE, null));
	}

	protected void sendCanceledMessage() {
		sendMessage(obtainMessage(CANCELED_MESSAGE, null));
	}

	//
	// Pre-processing of messages (in original calling thread, typically the UI
	// thread)
	//

	protected void handleSuccessMessage(int statusCode, String responseBody) {
		isSurcess = true;
		onSuccess(statusCode, responseBody);
	}

	protected void handleFailureMessage(Throwable e, String responseBody) {
		onFailure(e, responseBody);
	}

	// Methods which emulate android's Handler and Message methods
	protected void handleMessage(Message msg) {
		if (isCancled) {
			return;
		}
		Object[] response;

		switch (msg.what) {
		case SUCCESS_MESSAGE:
			response = (Object[]) msg.obj;
			Log.d("zxe_"+this.getClass().getName()+"获取"+url+"请求响应数据", "success:" + response[1]);
			AsyncHttpUtil.log(TAG, "success::" + response[1]);
			// Log.d("SUCCESS_MESSAGE", "success::"+response[1]);
			handleSuccessMessage(((Integer) response[0]).intValue(),
					(String) response[1]);
			break;
		case FAILURE_MESSAGE:
			response = (Object[]) msg.obj;
			Log.d("zxe_"+this.getClass().getName()+"获取"+url+"请求响应数据", "failure:" + response[1]);
			AsyncHttpUtil.log(TAG, "failure:" + response[1]);
			handleFailureMessage((Throwable) response[0], (String) response[1]);
			break;
		case START_MESSAGE:
			onStart();
			break;
		case FINISH_MESSAGE:
			onFinish();
			break;
		case CANCELED_MESSAGE:
			onCanceled();
			break;
		}
	}

	protected void sendMessage(Message msg) {
		if (handler != null) {
			handler.sendMessage(msg);
		} else {
			handleMessage(msg);
		}
	}

	protected Message obtainMessage(int responseMessage, Object response) {
		Message msg = null;
		if (handler != null) {
			msg = this.handler.obtainMessage(responseMessage, response);
		} else {
			msg = Message.obtain();
			msg.what = responseMessage;
			msg.obj = response;
		}
		return msg;
	}

	// Interface to AsyncHttpRequest
	void sendResponseMessage(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		String responseBody = null;
		try {
			HttpEntity entity = null;
			HttpEntity temp = response.getEntity();
			if (temp != null) {
				entity = new BufferedHttpEntity(temp);
				responseBody = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException e) {
			sendFailureMessage(e, (String) null);
		}

		if (status.getStatusCode() >= 300 || responseBody == null) {
			sendFailureMessage(new HttpResponseException(
					status.getStatusCode(), status.getReasonPhrase()),
					responseBody);
		} else {
			sendSuccessMessage(status.getStatusCode(), responseBody);
		}
	}
}