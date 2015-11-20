package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameTypes;

/**
 * 
 * @ClassName RadioListResponse 
 * @Description TODO 广播列表响应（返回的全播信息）
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月23日 上午10:24:48 
 * @Mender zxe
 * @Modification 2015年9月23日 上午10:24:48 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class RadioListResponse extends BaseResponse {
	private Conetnt data;

	public Conetnt getData() {
		return data;
	}

	public void setData(Conetnt data) {
		this.data = data;
	}

	public class Conetnt {
		private List<DataRadioBroadcast> list;
		private List<GameTypes> audiotypes;
		private String catid;
		private String catname;
		private AdvertismentsResponseRadio ad;

		public List<DataRadioBroadcast> getList() {
			return list;
		}

		public void setList(List<DataRadioBroadcast> list) {
			this.list = list;
		}

		public List<GameTypes> getAudiotypes() {
			return audiotypes;
		}

		public void setAudiotypes(List<GameTypes> audiotypes) {
			this.audiotypes = audiotypes;
		}

		public String getCatid() {
			return catid;
		}

		public void setCatid(String catid) {
			this.catid = catid;
		}

		public String getCatname() {
			return catname;
		}

		public void setCatname(String catname) {
			this.catname = catname;
		}

		public AdvertismentsResponseRadio getAd() {
			return ad;
		}

		public void setAd(AdvertismentsResponseRadio ad) {
			this.ad = ad;
		}
	}
}
