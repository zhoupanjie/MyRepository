package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class ActivityCheckResponse extends BaseResponse {
	private ActivityData data;

	public ActivityData getData() {
		return data;
	}

	public void setData(ActivityData data) {
		this.data = data;
	}
	
	public class ActivityData {
		private int exist;
		private String activity_url;
		private long begin_time;
		private long end_time;
		
		public int getExist() {
			return exist;
		}
		public void setExist(int exist) {
			this.exist = exist;
		}
		public String getActivity_url() {
			return activity_url;
		}
		public void setActivity_url(String activity_url) {
			this.activity_url = activity_url;
		}
		public long getBegin_time() {
			return begin_time;
		}
		public void setBegin_time(long begin_time) {
			this.begin_time = begin_time;
		}
		public long getEnd_time() {
			return end_time;
		}
		public void setEnd_time(long end_time) {
			this.end_time = end_time;
		}
	}
}
