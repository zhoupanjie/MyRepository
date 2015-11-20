package com.cplatform.xhxw.ui.http.net;

/**
 * 设置用户信息
 */
public class SetUserInfoRequest extends BaseRequest{

	private String logo;//	string	否	头像，如果没有值不作处理base64_encode()
	private String nickname;//	string	否	昵称，如果没有不作处理
	private String sex;
	private String birthday;
	private String career;
	private String blood;
	
	//	如果以上两个参数都没有则失败	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public String getBlood() {
		return blood;
	}
	public void setBlood(String blood) {
		this.blood = blood;
	}
}
