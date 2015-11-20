package com.cplatform.xhxw.ui.receiver;

import android.content.Context;
import android.text.TextUtils;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.feedss.push.sdk.PushManager;
import com.feedss.push.sdk.PushMessageReceiver;

/**
 * 众品push
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 *
 * 返回值中的code，解释如下： 200 - Success 400 －未知错误 401 身份验证失败 402 缺少参数 403 参数值不正确 500
 * 服务器内部错误 506 服务器操作失败
 *
 */
public class FeedssPushMessageReceiver extends PushMessageReceiver {

    private static final String TAG = FeedssPushMessageReceiver.class.getSimpleName();
    private static final PushMessageUtil.PushChannelType PUSH_CHANNEL_TYPE = PushMessageUtil.PushChannelType.zhongpin;

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回
     * resultCode －200 绑定成功
     * deviceToken -服务器端分配给当前设备的唯一设备号
     */
    @Override
    public void onBind(Context context, int resultCode, String appkey, String deviceToken) {
        LogUtil.i(TAG, "onBind:" + resultCode + "," + appkey + "," + deviceToken);
        if (resultCode != 200) {
            LogUtil.e(TAG, "onBind: 绑定失败! resultCode:" + resultCode);
            return;
        }
        String pushChannel = PushManager.getInstance().getPushChannel();
        PushManager.getInstance().setNotificationVibrateEnabled(App.CONTEXT, false);
        PushMessageUtil.bindServer(context, Constants.getUid(), deviceToken, null, pushChannel);
    }

    /**
     * 接收透传消息的函数。
     */
    @Override
    public void onMessage(Context context, String message, String ext) {
        LogUtil.i(TAG, "众品onMessage:" + message + "," + ext);
        PushMessageUtil.message(context, message, ext, PUSH_CHANNEL_TYPE);
    }

    /**
     * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
     */
    @Override
    public void onNotificationClicked(Context context, String title, String description, String ext) {
        LogUtil.i(TAG, "onNotificationClicked:" + title + "," + description + "," + ext);
        PushMessageUtil.notificationClicked(context, title, description, ext, PUSH_CHANNEL_TYPE);
    }

    /**
     * resultCode 200代表解绑定成功
     * 该方法将会在客户端调用PushManager.getInstance().stopWork()解绑后回调
     *
     */
    @Override
    public void onUnbind(Context context, int resultCode) {
        LogUtil.i(TAG, "onUnbind:" + resultCode);
        if (resultCode != 200) {
            return;
        }
        String deviceToken = App.getPreferenceManager().getPushDeviceToken();
        // push类型为空
        if (TextUtils.isEmpty(deviceToken)) {
            LogUtil.e(TAG, "onUnbind: deviceToken is null!");
            return;
        }
        PushMessageUtil.unbindServer(context, Constants.getUid(), deviceToken);
    }
}
