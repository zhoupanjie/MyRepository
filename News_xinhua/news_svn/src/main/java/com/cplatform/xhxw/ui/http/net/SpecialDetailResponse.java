package com.cplatform.xhxw.ui.http.net;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.cplatform.xhxw.ui.model.SpecialDataInfo;
import com.cplatform.xhxw.ui.model.SpecialDetail;
import com.cplatform.xhxw.ui.util.ListUtil;

/**
 * 专题新闻详情 Created by cy-love on 14-1-10.
 */
public class SpecialDetailResponse extends BaseResponse {

	private SpecialData data;

	public SpecialData getData() {
		return data;
	}

	public void setData(SpecialData data) {
		this.data = data;
	}

	public class SpecialData {
		private SpecialDataInfo special;
		private List<SpecialDetail> list;

		public List<SpecialDetail> getList() {
			if (!ListUtil.isEmpty(list)) {
				Collections.sort(list, new Comparator<SpecialDetail>() {
					@Override
					public int compare(SpecialDetail lhs, SpecialDetail rhs) {
						return lhs.getOrders() - rhs.getOrders();
					}
				});
			}
			return list;
		}

		public SpecialDataInfo getSpecial() {
			return special;
		}

		public void setSpecial(SpecialDataInfo special) {
			this.special = special;
		}

		public void setList(List<SpecialDetail> list) {
			this.list = list;
		}

	}

}
