package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;
import java.util.List;

public class PersonalMoodsData implements Serializable {

	private String showtime;// ": "今天",//展示名称
	private List<PersonalMoods> data;// "

	public String getShowtime() {
		return showtime;
	}

	public void setShowtime(String showtime) {
		this.showtime = showtime;
	}

	public List<PersonalMoods> getData() {
		return data;
	}

	public void setData(List<PersonalMoods> data) {
		this.data = data;
	}

}
