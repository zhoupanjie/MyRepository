package com.cplatform.xhxw.ui.ui.main.cms.channelsearch;

import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.net.ChannelSearchAddRequest;
import com.cplatform.xhxw.ui.http.net.ChannelSearchCancelRequest;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SearchChannelUtil {

	public static void dictAKeywordChannel(ChanneDao channel) {
		ChannelSearchAddRequest request = new ChannelSearchAddRequest();
		request.setChannelid(StringUtil.parseIntegerFromString(channel.getChannelID()));
		request.setChannelname(channel.getChannelName());
		request.setType(ChanneDao.CHANNEL_TYPE_KEY_WORDS);
		APIClient.subscribeSearchedChannel(request, new AsyncHttpResponseHandler(){
			
		});
	}
	
	public static void cancelDictChannel(ChanneDao channel) {
		ChannelSearchCancelRequest request = new ChannelSearchCancelRequest();
		request.setChannelid(StringUtil.parseIntegerFromString(channel.getChannelID()));
		request.setOperatetime("" + 0);
		APIClient.cancelSubChannel(request, new AsyncHttpResponseHandler(){
			
		});
	}
}
