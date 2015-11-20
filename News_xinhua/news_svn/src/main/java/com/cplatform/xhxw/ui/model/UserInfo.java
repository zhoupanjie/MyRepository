package com.cplatform.xhxw.ui.model;

import android.text.TextUtils;

/**
 * 用户信息
 * Created by cy-love on 14-1-15.
 */
public class UserInfo {
	public static final int ACCOUNT_TYPE_XUNWEN = 0;
	public static final int ACCOUNT_TYPE_SINAWEIBO = 1;
	public static final int ACCOUNT_TYPE_RENREN = 2;
	public static final int ACCOUNT_TYPE_QQ = 3;
	public static final int ACCOUNT_TYPE_TENCENTWEIBO = 4;
	public static final int ACCOUNT_TYPE_WEIXIN = 5;

    private String userId;
    private String userToken;
    private String state;
    private String nickName;
    private String logo;
    /**
     * 1 为企业用户
     * 0 为普通用户
     */
    private String type;
    private EnterPriseInfo enterprise;
    private String bindmobile;
    private int sex;
    private long birthday;
    private int career;
    private int blood;
    
    private int accountType = ACCOUNT_TYPE_XUNWEN;

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

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isToken() {
        return !TextUtils.isEmpty(userToken);
    }
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public EnterPriseInfo getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterPriseInfo enterprise) {
		this.enterprise = enterprise;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public String getBindmobile() {
		return bindmobile;
	}

	public void setBindmobile(String bindmobile) {
		this.bindmobile = bindmobile;
	}

	public class EnterPriseInfo {
    	private String id;
    	private String name;
    	private String simplename;
    	private String logo;
    	private String staff_name;
    	private String staff_phone;
    	private String staff_landline;
    	private String staff_signature;
    	private String staff_email;
    	private EnterpriseAlias aliases;
    	
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLogo() {
			return logo;
		}
		public void setLogo(String logo) {
			this.logo = logo;
		}
		public String getSimplename() {
			return simplename;
		}
		public void setSimplename(String simplename) {
			this.simplename = simplename;
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
		public String getStaff_email() {
			return staff_email;
		}
		public void setStaff_email(String staff_email) {
			this.staff_email = staff_email;
		}
		public String getStaff_signature() {
			return staff_signature;
		}
		public void setStaff_signature(String staff_signature) {
			this.staff_signature = staff_signature;
		}
		public EnterpriseAlias getAliases() {
			return aliases;
		}
		public void setAliases(EnterpriseAlias aliases) {
			this.aliases = aliases;
		}
    }
	
	public class EnterpriseAlias {
		private String im_alias;
		private String community_alias;
		private String f_community_alias;
		private String c_community_alias;
		
		public String getIm_alias() {
			if(im_alias == null || im_alias.trim().length() <= 0) {
				im_alias = "S信";
			}
			return im_alias;
		}
		public void setIm_alias(String im_alias) {
			this.im_alias = im_alias;
		}
		public String getCommunity_alias() {
			if(community_alias == null || community_alias.trim().length() <= 0) {
				community_alias = "社区";
			}
			return community_alias;
		}
		public void setCommunity_alias(String community_alias) {
			this.community_alias = community_alias;
		}
		public String getF_community_alias() {
			if(f_community_alias == null || f_community_alias.trim().length() <= 0) {
				f_community_alias = "朋友圈";
			}
			return f_community_alias;
		}
		public void setF_community_alias(String f_community_alias) {
			this.f_community_alias = f_community_alias;
		}
		public String getC_community_alias() {
			if(c_community_alias == null || c_community_alias.trim().length() <= 0) {
				c_community_alias = "公司圈";
			}
			return c_community_alias;
		}
		public void setC_community_alias(String c_community_alias) {
			this.c_community_alias = c_community_alias;
		}
	}
}
