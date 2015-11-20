package com.cplatform.xhxw.ui.http;

import android.text.TextUtils;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.http.net.AddChannelRequest;
import com.cplatform.xhxw.ui.http.net.AttentionRequest;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.BindMobileRequest;
import com.cplatform.xhxw.ui.http.net.ChangePasswordRequest;
import com.cplatform.xhxw.ui.http.net.ChannelSearchAddRequest;
import com.cplatform.xhxw.ui.http.net.ChannelSearchCancelRequest;
import com.cplatform.xhxw.ui.http.net.ChannelSearchRequest;
import com.cplatform.xhxw.ui.http.net.CheckAccountStatusRequest;
import com.cplatform.xhxw.ui.http.net.CheckDrawPrizeRequest;
import com.cplatform.xhxw.ui.http.net.ClearHistoryMessageRequest;
import com.cplatform.xhxw.ui.http.net.CodeRequest;
import com.cplatform.xhxw.ui.http.net.CommentDeleteRequest;
import com.cplatform.xhxw.ui.http.net.CommentPraiseRequest;
import com.cplatform.xhxw.ui.http.net.CommentReplyMeRequest;
import com.cplatform.xhxw.ui.http.net.CommentRequest;
import com.cplatform.xhxw.ui.http.net.CompanyFreshsMoodInfoRequest;
import com.cplatform.xhxw.ui.http.net.CompanyMessageRequest;
import com.cplatform.xhxw.ui.http.net.ContributeRequest;
import com.cplatform.xhxw.ui.http.net.CyanCommentRequest;
import com.cplatform.xhxw.ui.http.net.DelChannelRequest;
import com.cplatform.xhxw.ui.http.net.DeleteCompanyFreshsMoodCommentRequest;
import com.cplatform.xhxw.ui.http.net.DeleteCompanyFreshsMoodRequest;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsFreshsMoodCommentRequest;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsFreshsMoodRequest;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsRequest;
import com.cplatform.xhxw.ui.http.net.FeedBackRequest;
import com.cplatform.xhxw.ui.http.net.FindEmailPasswordRequest;
import com.cplatform.xhxw.ui.http.net.FriendsCompareRequest;
import com.cplatform.xhxw.ui.http.net.FriendsFreshsMoodInfoRequest;
import com.cplatform.xhxw.ui.http.net.GetMoreReplyRequest;
import com.cplatform.xhxw.ui.http.net.GetPushMessageRequest;
import com.cplatform.xhxw.ui.http.net.GetSChatRequest;
import com.cplatform.xhxw.ui.http.net.GetSysMsgListRequest;
import com.cplatform.xhxw.ui.http.net.GetUserChannelRequest;
import com.cplatform.xhxw.ui.http.net.GetUserInfoRequest;
import com.cplatform.xhxw.ui.http.net.GetVersionInfoRequest;
import com.cplatform.xhxw.ui.http.net.GuideUploadRequest;
import com.cplatform.xhxw.ui.http.net.LoginRequest;
import com.cplatform.xhxw.ui.http.net.MsgDetailRequest;
import com.cplatform.xhxw.ui.http.net.MyCommentRequest;
import com.cplatform.xhxw.ui.http.net.NewSestRequest;
import com.cplatform.xhxw.ui.http.net.NewsCollectUploadRequest;
import com.cplatform.xhxw.ui.http.net.NewsDetailRequest;
import com.cplatform.xhxw.ui.http.net.NewsListRequest;
import com.cplatform.xhxw.ui.http.net.NewsSearchRequest;
import com.cplatform.xhxw.ui.http.net.PhoneResetPasswordRequest;
import com.cplatform.xhxw.ui.http.net.PushCancelRequest;
import com.cplatform.xhxw.ui.http.net.PushOnlineRequest;
import com.cplatform.xhxw.ui.http.net.RecommendImagesRequest;
import com.cplatform.xhxw.ui.http.net.RedEnvelopesCodeRequest;
import com.cplatform.xhxw.ui.http.net.RegisterRequest;
import com.cplatform.xhxw.ui.http.net.SChatSendRequest;
import com.cplatform.xhxw.ui.http.net.SendCommentOrReplyPersonRequest;
import com.cplatform.xhxw.ui.http.net.SetUserInfoRequest;
import com.cplatform.xhxw.ui.http.net.SpecialDetailRequest;
import com.cplatform.xhxw.ui.http.net.StartResponse;
import com.cplatform.xhxw.ui.http.net.UserOpenRequest;
import com.cplatform.xhxw.ui.http.net.saas.ChannelDragRequest;
import com.cplatform.xhxw.ui.http.net.saas.CompanyZoneCommentSubRequest;
import com.cplatform.xhxw.ui.http.net.saas.CompanyZoneListRequest;
import com.cplatform.xhxw.ui.http.net.saas.CompanyZoneParisaSubRequest;
import com.cplatform.xhxw.ui.http.net.saas.FriendZoneCommentSubRequest;
import com.cplatform.xhxw.ui.http.net.saas.FriendZoneListRequest;
import com.cplatform.xhxw.ui.http.net.saas.FriendZoneParisaSubRequest;
import com.cplatform.xhxw.ui.http.net.saas.SaasGetContributeListRequest;
import com.cplatform.xhxw.ui.http.net.saas.SaasNewsSignRequest;
import com.cplatform.xhxw.ui.http.net.saas.SaasSetUserinfoRequest;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameDetailRequest;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameListRequest;
import com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RadioListRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.FriendsInfoRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.FriendsMessageRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.HistoryMessageRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.NewFriendsRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.PersonalMoodsRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.ReceiveNewFriendsRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.SearchResultRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.SendCompanyCircleRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.SendFriendsVerifyRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.UpdateFriendRemarkRequest;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class APIClient {

	private static final String TAG = APIClient.class.getSimpleName();

	/**
	 * 同步请求，内部检查devid
	 * 
	 * @param url
	 * @param request
	 * @return
	 */
	private static String syncPostHttp(String url, BaseRequest request) {
		if (checkSyncDevId()) {
			return SyncHttpManager.post(url, request);
		}
		return null;
	}

	private static void asyncPostHttp(final String url,
			final BaseRequest request,
			final AsyncHttpResponseHandler responseHandler) {
		if ((TextUtils.isEmpty(Constants.getDevId()))) {
			asyncStart(new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, String content) {
					StartResponse response;
					try {
						ResponseUtil.checkResponse(content);
						response = new Gson().fromJson(content,
								StartResponse.class);
					} catch (Exception e) {
						LogUtil.e(TAG, "获得设备id失败" + e);
						responseHandler.onFailure(e, "获得设备id失败");
						return;
					}
					if (response.isSuccess()) {
						String devId = response.getData().getUserId();
						App.getPreferenceManager().setDevId(devId);
						if (!responseHandler.isCancled()) {
							AsyncHttpManager
									.post(url, request, responseHandler);
						}
					} else {
						if (!responseHandler.isCancled()) {
							responseHandler.onSuccess(statusCode, content);
						}
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					if (!responseHandler.isCancled()) {
						responseHandler.onFailure(error, content);
					}
				}
			});
		} else {
			AsyncHttpManager.post(url, request, responseHandler);
		}
	}

    private static void asyncGetHttp(final String url,
                                      final BaseRequest request,
                                      final AsyncHttpResponseHandler responseHandler) {
        if ((TextUtils.isEmpty(Constants.getDevId()))) {
            asyncStart(new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, String content) {
                    StartResponse response;
                    try {
                        ResponseUtil.checkResponse(content);
                        response = new Gson().fromJson(content,
                                StartResponse.class);
                    } catch (Exception e) {
                        LogUtil.e(TAG, "获得设备id失败" + e);
                        responseHandler.onFailure(e, "获得设备id失败");
                        return;
                    }
                    if (response.isSuccess()) {
                        String devId = response.getData().getUserId();
                        App.getPreferenceManager().setDevId(devId);
                        if (!responseHandler.isCancled()) {
                            AsyncHttpManager
                                    .get(url, request, responseHandler);
                        }
                    } else {
                        if (!responseHandler.isCancled()) {
                            responseHandler.onSuccess(statusCode, content);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    if (!responseHandler.isCancled()) {
                        responseHandler.onFailure(error, content);
                    }
                }
            });
        } else {
            AsyncHttpManager.get(url, request, responseHandler);
        }
    }

	/**
	 * 检查devid
	 * 
	 * @return
	 */
	public static boolean checkSyncDevId() {
		if (TextUtils.isEmpty(Constants.getDevId())) {
			String devRes = syncStart();
			if (TextUtils.isEmpty(devRes)) {
				return false;
			}
			StartResponse response;
			try {
				ResponseUtil.checkResponse(devRes);
				response = new Gson().fromJson(devRes, StartResponse.class);
			} catch (Exception e) {
				LogUtil.e(TAG, "获得设备id失败::" + e);
				return false;
			}
			if (!response.isSuccess()) {
				return false;
			}
			String devId = response.getData().getUserId();
			App.getPreferenceManager().setDevId(devId);
		}
		return true;
	}

	/**
	 * 获得UID
	 */
	public static String syncStart() {
		return SyncHttpManager.post("/start", new BaseRequest());
	}

	/**
	 * 获得UID
	 */
	public static void asyncStart(AsyncHttpResponseHandler responseHandler) {
		AsyncHttpManager.post("/start", new BaseRequest(), responseHandler);
	}

	/**
	 * 获取频道列表
	 */
	public static String syncChannelList(BaseRequest br) {
		return syncPostHttp("/channel/list", br);
	}
	
	/**
	 * 异步获取频道列表
	 * @param br
	 * @param responseHandler
	 */
	public static void asynChannelList(BaseRequest br, AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/channel/list", br, responseHandler);
	}

	/**
	 * 同步用户栏目
	 */
	public static String syncUserchannel(GetUserChannelRequest request) {
		return syncPostHttp("/channel/userchannel", request);
	}

	/**
	 * 栏目添加
	 */
	public static String syncAddChannel(AddChannelRequest response) {
		return syncPostHttp("/channel/addchannel", response);
	}
	
	/**
	 * 异步添加栏目
	 * @param request
	 * @param resHandler
	 */
	public static void asyncAddChannel(AddChannelRequest request, 
			AsyncHttpResponseHandler resHandler) {
		asyncPostHttp("/channel/addchannel", request, resHandler);
	}

	/**
	 * 栏目删除
	 */
	public static String syncDelChannel(DelChannelRequest response) {
		return syncPostHttp("/channel/delchannel", response);
	}
	
	/**
	 * 异步删除栏目
	 * @param request
	 * @param resHandler
	 */
	public static void asyncDelChannel(DelChannelRequest request, 
			AsyncHttpResponseHandler resHandler) {
		asyncPostHttp("/channel/delchannel", request, resHandler);
	}
	
	/**
	 * 异步提交栏目排序
	 * @param request
	 * @param resHandler
	 */
	public static void orderChannelDrag(ChannelDragRequest request, 
			AsyncHttpResponseHandler resHandler) {
		asyncPostHttp("/channel/channeldrag", request, resHandler);
	}
	
	/**
	 * 上传引导页选项
	 * @param request
	 * @param resHandler
	 */
	public static void guideUpload(GuideUploadRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/user/setuserportrait", request, resHandler);
    }
	
	/**
	 * 根据关键词搜索网络频道
	 * @param request
	 * @param resHandler
	 */
	public static void searchChannelsByKeyword(ChannelSearchRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/channel/dictsearch", request, resHandler);
    }
	
	/**
	 * 订阅搜索出的栏目
	 * @param request
	 * @param resHandler
	 */
	public static void subscribeSearchedChannel(ChannelSearchAddRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/channel/dictadd", request, resHandler);
    }
	
	/**
	 * 取消订阅栏目
	 * @param request
	 * @param resHandler
	 */
	public static void cancelSubChannel(ChannelSearchCancelRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/channel/dictsub", request, resHandler);
    }
	
	/**
	 * 获取搜索关键词栏目下的新闻列表
	 * @param request
	 * @param resHandler
	 */
	public static void getSearchedChannelNewsList(NewsListRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/channel/dictlist", request, resHandler);
    }

	/**
	 * 新闻列表
	 */
	public static void newsList(NewsListRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/news/list", request, responseHandler);
	}
	
	/**
	 * 视频列表
	 */
	public static void videoList(NewsListRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/news/video", request, responseHandler);
	}
	/**
	 * 新闻列表
	 */
	public static void gameList(GameListRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/game/list", request, responseHandler);
	}
	
	/**
	 * 
	 * @Name getRadioList 
	 * @Description TODO 获取广播列表
	 * @param request
	 * @param responseHandler 
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月24日 上午9:37:30
	*
	 */
	public static void radioList(RadioListRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/audio/list", request, responseHandler);
	}
	/**
	 * 新闻列表
	 */
	public static String syncNewsList(NewsListRequest request) {
		return syncPostHttp("/news/list", request);
	}

	/**
	 * 新闻列表
	 */
	public static void newSest(NewSestRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/news/newsest", request, responseHandler);
	}

	/**
	 * 专题详情
	 */
	public static void specialDetail(SpecialDetailRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/special/detail", request, responseHandler);
	}

	/**
	 * 新闻详情
	 * 
	 * @param request
	 * @param responseHandler
	 */
	public static void newsDetail(NewsDetailRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/news/detail", request, responseHandler);
	}
	/**
	 * 
	 * @Name gameDetail 
	 * @Description TODO 游戏详情
	 * @param request
	 * @param responseHandler 
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月26日 下午5:09:23
	*
	 */
	public static void gameDetail(GameDetailRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/game/detail", request, responseHandler);
	}
	/**
	 * 
	 * @Name gameShared 
	 * @Description TODO 游戏分享
	 * @param request
	 * @param responseHandler 
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月27日 下午3:26:21
	*
	 */
	public static void gameShared(GameDetailRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/game/shared", request, responseHandler);
	}
	/**
	 * 
	 * @Name gameStat 
	 * @Description TODO 游戏统计
	 * @param request
	 * @param responseHandler 
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月27日 下午3:26:49
	*
	 */
	public static void gameStat(GameDetailRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/game/stat", request, responseHandler);
	}
	/**
	 * 新闻详情
	 * 
	 * @param request
	 */
	public static String syncNewsDetail(NewsDetailRequest request) {
		return syncPostHttp("/news/detail", request);
	}

	/**
	 * 推荐图片集
	 */
	public static void recommendImages(RecommendImagesRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/news/imagelist", new BaseRequest(), responseHandler);
	}

	/**
	 * 第三方登录
	 */
	public static void userOpen(UserOpenRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/open", request, responseHandler);
	}

	/**
	 * 普通--登录
	 */
	public static void login(LoginRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/loginentrance", request, responseHandler);
	}

	/**
	 * 邮箱、手机--注册
	 */
	public static void register(RegisterRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/register", request, responseHandler);
	}

	/**
	 * 注册--获取验证码
	 */
	public static void getCode(CodeRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/getphonevaliedcode", request, responseHandler);
	}
	
	/**
	 * 红包快捷注册--获取验证码
	 */
	public static void getRedEnvelopesCode(RedEnvelopesCodeRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/getbindmobilecode", request, responseHandler);
	}
	
	/**
	 * 绑定手机号码
	 */
	public static void bindMobile(BindMobileRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/bindmobile", request, responseHandler);
	}

	/**
	 * 找回密码--获取验证码
	 */
	public static void getFindCode(CodeRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/getphonevaliedreset", request, responseHandler);
	}

	/**
	 * 手机重置密码
	 */
	public static void phoneResetPassword(PhoneResetPasswordRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/phoneresetpassword", request, responseHandler);
	}

	/**
	 * 获取新闻评论列表
	 */
	public static void getComment(CommentRequest request,
			AsyncHttpResponseHandler responseHandler) {

		asyncPostHttp("/comment/lista", request, responseHandler);
	}
	
	/**
	 * SAAS获取新闻评论列表
	 */
	public static void getSAASComment(CommentRequest request,
			AsyncHttpResponseHandler responseHandler) {

		asyncPostHttp("/comment/list", request, responseHandler);
	}

	/**
	 * 评论新闻 或者 回复评论列表中的某个人
	 */
	public static void sendCommentOrReplyPerson(
			SendCommentOrReplyPersonRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/comment/reply", request, responseHandler);
	}

	/**
	 * 邮箱找回密码
	 */
	public static void findEmailPassword(FindEmailPasswordRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/findemailpassword", request, responseHandler);
	}

	/**
	 * push绑定
	 */
	public static void pushOnline(PushOnlineRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/push/online", request, responseHandler);
	}

	/**
	 * push解绑
	 */
	public static void pushCancel(PushCancelRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/push/cancel", request, responseHandler);
	}

	/**
	 * 评论--赞或者取消赞
	 */
	public static void setPraise(CommentPraiseRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/comment/praise", request, responseHandler);
	}

	/**
	 * 评论--删除
	 */
	public static void setDelete(CommentDeleteRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/comment/delete", request, responseHandler);
	}

	/**
	 * 获取用户属性
	 */
	public static void getUserInfo(GetUserInfoRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/getuserinfo", request, responseHandler);
	}

	/**
	 * 获取用户属性
	 */
	public static void setUserInfo(SetUserInfoRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/setuserinfo", request, responseHandler);
	}
	
	/**
	 * 设置SAAS用户属性
	 */
	public static void setSAASUserInfo(SaasSetUserinfoRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/user/setuserinfo", request, responseHandler);
	}

	/**
	 * 意见反馈
	 */
	public static void feedBack(FeedBackRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/app/feedback", request, responseHandler);
	}

	/**
	 * 我的评论
	 */
	public static void myComment(MyCommentRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/comment/my", request, responseHandler);
	}

	/**
	 * 获得push message
	 */
	public static void getPushMessage(GetPushMessageRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/push/getmessage", request, responseHandler);
	}

	/**
	 * 新闻搜索
	 */
	public static void newsSearch(NewsSearchRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/news/search", request, responseHandler);
	}

	/**
	 * 获取项目版本信息
	 */
	public static void getVersionInfo(GetVersionInfoRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/version", request, responseHandler);
	}

    /**
     * 角标广告
     */
    public static void adSuperscript(AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/ad/superscript", new BaseRequest(), responseHandler);
    }
    
    /**
     * 获取loading页广告
     */
    public static void getLoadingAdvertisements(BaseRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/ad/start", request, responseHandler);
    }
    
    /**
     * 获取角标区广告
     */
    public static void getFlagAdvertisement(BaseRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/ad/superscript", request, responseHandler);
    }
    
    /**
     * 获取热门搜索词
     * @param request
     * @param resHandler
     */
    public static void getHotSearchWords(BaseRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/tag/searchcloud", request, resHandler);
    }
    
    /**
     * 获取当前缓存账户的账户状态
     * @param request
     * @param resHandler
     */
    public static void getAccountStatus(CheckAccountStatusRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/user/enterprisecheck", request, resHandler);
    }
    
    /**
     * 获取回复我的评论列表
     * @param request
     * @param resHandler
     */
    public static void getCommentReplyMe(CommentReplyMeRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/comment/backme", request, resHandler);
    }
    
    /**
     * 获取更多评论
     * @param request
     * @param resHandler
     */
    public static void getMoreReply(GetMoreReplyRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/comment/getbackmore", request, resHandler);
    }
    
    /**
     * 获取系统消息
     * @param request
     * @param resHandler
     */
    public static void getSysMsg(GetSysMsgListRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/usermsg/list", request, resHandler);
    }
    
    /**
     * 获取系统消息详情
     * @param request
     * @param resHandler
     */
    public static void getSysMsgDetail(MsgDetailRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/usermsg/detail", request, resHandler);
    }
    
    /**
     * 获取用户属性设置项列表
     * @param request
     * @param resHandler
     */
    public static void getUserSettingProperties(BaseRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/user/getsysproperty", request, resHandler);
    }
    
    /**
     * 获取是否有新系统消息或评论回复
     * @param request
     * @param resHandler
     */
    public static void getIsHaveNewMsg(BaseRequest request, AsyncHttpResponseHandler resHandler) {
    	asyncPostHttp("/usermsg/newusermsg", request, resHandler);
    }

    /**
     * 获得联系人
     */
    public static String syncContactList() {
        BaseRequest request = new BaseRequest();
        request.setDevRequest(true);
        return syncPostHttp("/addressbook/contactlist", request);
    }
    
    /**
	 * 获取推荐的朋友列表
	 */
	public static void getNewFriends(NewFriendsRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/friends/recommend", request, responseHandler);
	}
	
	/**
	 * 接受新的朋友
	 */
	public static void receiveNewFriends(ReceiveNewFriendsRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/friends/reply", request, responseHandler);
	}
	
	/**
	 * 向朋友发送验证信息
	 */
	public static void sendFriendsVerify(SendFriendsVerifyRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/friends/apply", request, responseHandler);
	}
	
	/**
	 * 获取搜索结果
	 */
	public static void getSearchResult(SearchResultRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/friends/search", request, responseHandler);
	}
	
	/**
	 * 向朋友发送验证信息
	 */
	public static void getFriendsInfo(FriendsInfoRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/addressbook/contactdetail", request, responseHandler);
	}
	
	/**
	 * 更新好友备注
	 */
	public static void updateFriendRemark(UpdateFriendRemarkRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/addressbook/contactcomment", request, responseHandler);
	}
	
	/**
	 * 关注
	 */
	public static void attention(AttentionRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/friends/attention", request, responseHandler);
	}
	
	/**
	 * 删除好友
	 */
	public static void deleteFriends(DeleteFriendsRequest request,
			AsyncHttpResponseHandler responseHandler) {
		asyncPostHttp("/addressbook/contactdelete", request, responseHandler);
	}
	
    /**
     * 上传通讯录
     */
    public static String syncFriendsCompare(FriendsCompareRequest request) {
        return syncPostHttp("/friends/compare", request);
    }

    /**
     * 提交投稿内容
     * @param request
     * @param resHandler
     */
    public static void commitContributeContent(ContributeRequest request,
                                               AsyncHttpResponseHandler resHandler) {
        asyncPostHttp("/user/contribute", request, resHandler);
    }
    
    /**
     * 好友聊天列表
     * @param request
     * @param responseHandler
     */
    public static void getSChat(GetSChatRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/schat/getchat", request, responseHandler);
    }
    
    /**
     * 获得S信未读消息列表
     */
    public static String getSyncNotreadList() {
        BaseRequest request = new BaseRequest();
        request.setDevRequest(true);
        return syncPostHttp("/schat/getnotreadlist", request);
    }

    /**
     * 发送S信
     */
    public static void sChatSend(SChatSendRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncGetHttp("/schat/send", request, responseHandler);
    }
    
    /**
     * 获取投稿列表
     */
    public static void saasGetContributeList(SaasGetContributeListRequest request, 
    		AsyncHttpResponseHandler responseHandler) {
    	asyncPostHttp("/user/contributeList", request, responseHandler);
    }
    
    /**
     * 获取新消息数目
     */
    public static void saasGetMsgCount(BaseRequest request, 
    		AsyncHttpResponseHandler responseHandler) {
    	asyncPostHttp("/msg/msgCount", request, responseHandler);
    }

    /**
     * 获得公司圈请求地址
     */
    public static String getCompanyZoneUrl() {
        return HttpClientConfig.getAbsoluteUrl("/html/companyzone", false, true);
    }

    /**
     * 公司圈评论
     */
    public static void companyZoneCommentSub(CompanyZoneCommentSubRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/companyzone/commentsub", request, responseHandler);
    }

    /**
     * 好友圈评论
     */
    public static void friendZoneCommentSub(FriendZoneCommentSubRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/friendzone/commentsub", request, responseHandler);
    }
    
    /**
     * 公司圈发布新鲜事
     */
    public static void sendCompanyCircle(SendCompanyCircleRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/new/send", request, responseHandler);
    }
    
    /**
     * 圈阅新闻
     */
    public static void saasSignNews(SaasNewsSignRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/user/sign", request, responseHandler);
    }
    
    /**
     * 获取我的圈阅列表
     */
    public static void saasGetSignNewsList(SaasGetContributeListRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/user/signList", request, responseHandler);
    }

    /**
     * 公司圈列表数据
     * @param request
     * @param responseHandler
     */
    public static void companyZoneList(CompanyZoneListRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/companyzone/list", request, responseHandler);
    }

    /**
     * 朋友圈
     * @param request
     * @param responseHandler
     */
    public static void friendZoneList(FriendZoneListRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/friendzone/list", request, responseHandler);
    }
    
    /**
     * 朋友圈---新的消息
     */
    public static void getFriendsMessage(FriendsMessageRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/friendzone/msglist", request, responseHandler);
    }
    
    /**
     * 公司圈---新的消息
     */
    public static void getCompanyMessage(CompanyMessageRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/companyzone/msglist", request, responseHandler);
    }
    /**
     * 获得未读统计
     * @param responseHandler
     */
    public static void sumGet(AsyncHttpResponseHandler responseHandler) {
        BaseRequest request = new BaseRequest();
        request.setDevRequest(true);
        asyncPostHttp("/sum/get", request, responseHandler);
    }
    
    /**
     * 公司圈---新的消息
     */
    public static void getPersonalMoods(PersonalMoodsRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/new/list", request, responseHandler);
    }

    /**
     * 公司圈点赞
     */
    public static void companyZoneParisaSub(CompanyZoneParisaSubRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/companyzone/parisasub", request, responseHandler);
    }

    /**
     * 朋友圈点赞
     */
    public static void friendZoneParisaSub(FriendZoneParisaSubRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/friendzone/parisasub", request, responseHandler);
    }

    /**
     * 公司圈点赞
     */
    public static void companyZoneParisa(CompanyZoneParisaSubRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/companyzone/parisasub", request, responseHandler);
    }
    
    /**
     * 公司圈新鲜事详情
     */
    public static String getCompanyFreshInfoUrl() {
        return HttpClientConfig.getAbsoluteUrl("/html/companymsgdetail", false, true);
    }
    
    /**
     * 朋友圈新鲜事详情
     */
    public static String getFriendsFreshInfoUrl() {
        return HttpClientConfig.getAbsoluteUrl("/zone/friendsdetail", false, true);
    }
    
    /**
     * 获取历史消息列表
     */
    public static void getHistoryMessage(HistoryMessageRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/new/hisrotypush", request, responseHandler);
    }
    
    /**
     * 清除历史消息列表
     */
    public static void clearHistoryMessage(ClearHistoryMessageRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/new/cleanmsg", request, responseHandler);
    }
    
    /**
     * 获取当前活动信息
     */
    public static void fetchActivityInfo(BaseRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/activity/check", request, responseHandler);
    }
    
    /**
     * 修改密码
     */
    public static void changePassword(ChangePasswordRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/user/resetpwd", request, responseHandler);
    }
    
    /**
     * 提交征集内容
     */
    public static void submitCollectContent(NewsCollectUploadRequest request, 
    		AsyncHttpResponseHandler responseHandler) {
    	asyncPostHttp("/levy/submit", request, responseHandler);
    }
    
    
    /**
     * 获取朋友圈新鲜事详情 --- 本地效果
     */
    public static void getFriendsFreshsMoodInfo(FriendsFreshsMoodInfoRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/friendzone/detail", request, responseHandler);
    }
    
    /**
     * 获取公司圈新鲜事详情 --- 本地效果
     */
    public static void getCompanyFreshsMoodInfo(CompanyFreshsMoodInfoRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/companyzone/detail", request, responseHandler);
    }
    
    /**
     * 删除朋友圈新鲜事 --- 本地效果
     */
    public static void deleteFriendsFreshsMood(DeleteFriendsFreshsMoodRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/friendzone/delete", request, responseHandler);
    }
    
    /**
     * 删除公司圈新鲜事 --- 本地效果
     */
    public static void deleteCompanyFreshsMood(DeleteCompanyFreshsMoodRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/companyzone/delete", request, responseHandler);
    }
    
    /**
     * 删除朋友圈新鲜事评论 --- 本地效果
     */
    public static void deleteFriendsFreshsComment(DeleteFriendsFreshsMoodCommentRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/friendzone/deldiscuss", request, responseHandler);
    }
    /**
     * 删除公司圈新鲜事评论 --- 本地效果
     */
    public static void deleteCompanyFreshsComment(DeleteCompanyFreshsMoodCommentRequest request, AsyncHttpResponseHandler responseHandler) {
        asyncPostHttp("/companyzone/deldiscuss", request, responseHandler);
    }
    
    /**
     * 检查抽奖剩余次数
     * @param request
     * @param hanlder
     */
    public static void checkDrawPrizeCount(CheckDrawPrizeRequest request,
    		AsyncHttpResponseHandler hanlder) {
    	asyncPostHttp("/lottery/api?action=shareurl", request, hanlder);
    }
    
    /**
     * 获取畅言我的评论
     * @param request
     * @param handler
     */
    public static void CyanGetMyComment(CyanCommentRequest request,
    		AsyncHttpResponseHandler handler) {
    	asyncPostHttp("/changyan/my", request, handler);
    }
    
    /**
     * 获取畅言回复我的评论
     * @param request
     * @param handler
     */
    public static void CyanGetReplyMeComment(CyanCommentRequest request,
    		AsyncHttpResponseHandler handler) {
    	asyncPostHttp("/changyan/backme", request, handler);
    }
}
