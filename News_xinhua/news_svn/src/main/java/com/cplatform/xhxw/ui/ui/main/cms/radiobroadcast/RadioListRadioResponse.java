package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

/**
 * 
 * @ClassName RadioListRadioResponse 
 * @Description TODO 广播列表响应（加载更多的信息）
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月23日 上午10:23:56 
 * @Mender zxe
 * @Modification 2015年9月23日 上午10:23:56 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class RadioListRadioResponse extends BaseResponse {

    private List<DataRadioBroadcast> data;

    public List<DataRadioBroadcast> getData() {
        return data;
    }

    public void setData(List<DataRadioBroadcast> data) {
        this.data = data;
    }
}
