package com.cplatform.xhxw.ui.model;

public class SetUserInfo {

    private String logo;
    private String nickName;
    private int sex;
    private long birthday;
    private int career;
    private int blood;
    private String staff_name;
    private String staff_phone;
    private String staff_landline;
    private String staff_signature;
    private String staff_email;

    public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		this.blood = blood;
	}

	public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

	public String getStaff_name() {
		return staff_name;
	}

	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}

	public String getStaff_phone() {
		return staff_phone;
	}

	public void setStaff_phone(String staff_phone) {
		this.staff_phone = staff_phone;
	}

	public String getStaff_landline() {
		return staff_landline;
	}

	public void setStaff_landline(String staff_landline) {
		this.staff_landline = staff_landline;
	}

	public String getStaff_signature() {
		return staff_signature;
	}

	public void setStaff_signature(String staff_signature) {
		this.staff_signature = staff_signature;
	}

	public String getStaff_email() {
		return staff_email;
	}

	public void setStaff_email(String staff_email) {
		this.staff_email = staff_email;
	}
}
