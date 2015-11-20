package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;
/**
 * 
 * @ClassName InterfaceRadioPlayControl 
 * @Description TODO 播放控制接口
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月15日 下午8:10:14 
 * @Mender zxe
 * @Modification 2015年9月15日 下午8:10:14 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public interface InterfaceRadioPlayControl {
	/**
	 * 
	 * @Name isPlaying 
	 * @Description TODO 是否播放
	 * @return 
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年9月23日 上午10:01:52
	*
	 */
	boolean isPlaying();
	/**
	 * 
	 * @Name onRadioPause 
	 * @Description TODO  广播暂停
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 上午10:02:03
	*
	 */
	void onRadioPause();
	/**
	 * 
	 * @Name onRadioResume 
	 * @Description TODO 广播重播 
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 上午10:02:16
	*
	 */
	void onRadioResume();
	/**
	 * 
	 * @Name setNextPlay 
	 * @Description TODO  播放下一条
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 上午10:02:30
	*
	 */
	void setNextPlay();
	/**
	 * 
	 * @Name setLastPlay 
	 * @Description TODO  上一条
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 上午10:03:00
	*
	 */
	void setLastPlay();
	/**
	 * 
	 * @Name firstLoadData 
	 * @Description TODO  广播列表第一次加载
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 上午10:03:11
	*
	 */
	void firstLoadData();
}
