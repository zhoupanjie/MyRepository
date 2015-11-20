package com.cplatform.xhxw.ui.http;

/**
 * Created by cy-love on 14-1-7.
 */
public class SyncHttpClient extends com.loopj.android.http.SyncHttpClient {

    @Override
    public String onRequestFailed(Throwable error, String content) {
        return null;
    }

}
