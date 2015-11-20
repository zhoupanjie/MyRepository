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

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

public class AsyncHttpRequest implements Runnable {
	private AbstractHttpClient client;
	private HttpContext context;
	private HttpUriRequest request;
	private boolean isBinaryRequest;
	private int executionCount;
	private static HashMap<Object, AsyncHttpResponseHandler> handler_map;
	private Object key;

	static {
		handler_map = new HashMap<Object, AsyncHttpResponseHandler>(5);
	}

	public AsyncHttpRequest(AbstractHttpClient client, HttpContext context,
			HttpUriRequest request, AsyncHttpResponseHandler responseHandler) {
		this.client = client;
		this.context = context;
		this.request = request;
		if (responseHandler instanceof BinaryHttpResponseHandler) {
			this.isBinaryRequest = true;
		}
		key = System.currentTimeMillis();
		handler_map.put(key, responseHandler);
	}

	public void run() {
		AsyncHttpResponseHandler responseHandler = handler_map.get(key);
		try {
			if (responseHandler != null) {
				responseHandler.sendStartMessage();
			}

			makeRequestWithRetries();

			if (responseHandler != null) {
				responseHandler.sendFinishMessage();
			}
		} catch (IOException e) {
			if (responseHandler != null) {
				responseHandler.sendFinishMessage();
				if (this.isBinaryRequest) {
					responseHandler.sendFailureMessage(e, (byte[]) null);
				} else {
					responseHandler.sendFailureMessage(e, (String) null);
				}
			}
		}
	}

	private void makeRequest() throws IOException {
		if (!Thread.currentThread().isInterrupted()) {
			try {
				HttpResponse response = client.execute(request, context);
				if (!Thread.currentThread().isInterrupted()) {
					AsyncHttpResponseHandler responseHandler = handler_map
							.get(key);
					responseHandler.setUrl(request.getURI().toString());
					if (responseHandler != null) {
						responseHandler.sendResponseMessage(response);
					}
				} else {
					// TODO: should raise InterruptedException? this block is
					// reached whenever the request is cancelled before its
					// response is received
				}
			} catch (IOException e) {
				if (!Thread.currentThread().isInterrupted()) {
					throw e;
				}
			}
		}
	}

	private void makeRequestWithRetries() throws ConnectException {
		// This is an additional layer of retry logic lifted from droid-fu
		// See:
		// https://github.com/kaeppler/droid-fu/blob/master/src/main/java/com/github/droidfu/http/BetterHttpRequestBase.java
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = client
				.getHttpRequestRetryHandler();
		AsyncHttpResponseHandler responseHandler = null;
		while (retry) {
			try {
				responseHandler = handler_map.get(key);
				makeRequest();
				return;
			} catch (UnknownHostException e) {
				if (responseHandler != null) {
					responseHandler.sendFailureMessage(e, "can't resolve host");
				}
				return;
			} catch (SocketException e) {
				// Added to detect host unreachable
				if (responseHandler != null) {
					responseHandler.sendFailureMessage(e, "can't resolve host");
				}
				return;
			} catch (SocketTimeoutException e) {
				if (responseHandler != null) {
					responseHandler.sendFailureMessage(e, "socket time out");
				}
				return;
			} catch (IOException e) {
				cause = e;
				retry = retryHandler.retryRequest(cause, ++executionCount,
						context);
			} catch (NullPointerException e) {
				// there's a bug in HttpClient 4.0.x that on some occasions
				// causes
				// DefaultRequestExecutor to throw an NPE, see
				// http://code.google.com/p/android/issues/detail?id=5255
				cause = new IOException("NPE in HttpClient" + e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount,
						context);
			} finally {
				handler_map.remove(key);
			}
		}

		// no retries left, crap out with exception
		ConnectException ex = new ConnectException();
		ex.initCause(cause);
		throw ex;
	}
}
