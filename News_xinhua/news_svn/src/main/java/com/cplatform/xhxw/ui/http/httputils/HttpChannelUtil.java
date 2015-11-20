package com.cplatform.xhxw.ui.http.httputils;

import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.ChannelListResponse;
import com.cplatform.xhxw.ui.model.Channe;
import com.cplatform.xhxw.ui.model.UserInfo;
import com.cplatform.xhxw.ui.util.Actions;
import com.google.gson.Gson;

public class HttpChannelUtil {

	/**
	 * 获取企业的新闻频道列表
	 * @param ui
	 * @return
	 */
	public static void fetchEnterpriseChannels(final Context con, final UserInfo ui) {
		new Thread(){

			@Override
			public void run() {
				BaseRequest br = new BaseRequest();
		    	br.setSaasRequest(true);
		    	String result = APIClient.syncChannelList(br);
		    	try {
		    		ResponseUtil.checkResponse(result);
			    	ChannelListResponse response = new Gson().fromJson(result, 
			    			ChannelListResponse.class);
			    	if(response.isSuccess()) {
			    		if(!isChannelSameToServer(con, ui, response.getData())) {
			    			ChanneDB.delChannelByEnterpriseId(con, ui.getEnterprise().getId());
			    			for(Channe ch : response.getData()) {
			    				ChanneDB.saveChannel(con, composeChanneDaoForEnterPrise(ch, ui));
			    			}
			    		}
			    		Intent intent = new Intent();
		                intent.setAction(Actions.ACTION_ENTERPRISE_CHANNEL_CHANGED);
		                con.sendBroadcast(intent);
			    	}
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	
			}
		}.start();
    }
	
	/**
	 * 判断从服务器获取到的频道列表是否和本地一致(id, name, order 都一致)
	 * @param con
	 * @param ui
	 * @param channelsInServer
	 * @return
	 */
	public static boolean isChannelSameToServer(Context con, UserInfo ui, List<Channe> channelsInServer) {
		List<ChanneDao> channelsInLocal = ChanneDB.getChannelsByEnterpriseId(con, ui.getEnterprise().getId());
		if(channelsInLocal == null || channelsInServer == null) {
			return false;
		}
		if(channelsInLocal.size() != channelsInServer.size()) {
			return false;
		}
		
		boolean isSame = true;
		for(Channe channel : channelsInServer) {
			boolean isExistLocal = false;
			for(ChanneDao chanD : channelsInLocal) {
				if(channel.getChannelid().equals(chanD.getChannelID()) &&
						channel.getChannelname().equals(chanD.getChannelName()) &&
						channel.getListorder() == chanD.getListorder() &&
						channel.getType() == chanD.getChannelType()) {
					isExistLocal = true;
					channelsInLocal.remove(chanD);
					break;
				}
			}
			if(isExistLocal) {
				continue;
			} else {
				isSame = false;
				break;
			}
		}
		return isSame;
	}
	
	/**
	 * Channe对象转ChanneDao对象
	 * @param channe
	 * @param ui
	 * @return
	 */
	public static ChanneDao composeChanneDaoForEnterPrise(Channe channe, UserInfo ui) {
		ChanneDao channDao = new ChanneDao();
		if(channe == null) {
			return null;
		}
		
		channDao.setChannelID(channe.getChannelid());
		channDao.setChannelName(channe.getChannelname());
		channDao.setDirty(0);
		channDao.setEnterpriseId(Integer.valueOf(ui.getEnterprise().getId()));
		channDao.setListorder(channe.getListorder());
		channDao.setOperatetime(System.currentTimeMillis());
		channDao.setShow(channe.getIsdisplay());
		channDao.setSyncTime(System.currentTimeMillis());
		channDao.setUserId(ui.getUserId());
		channDao.setChannelType(channe.getType());
		channDao.setChannelCreateType("" + channe.getChanneltype());
		
		return channDao;
	}
}
