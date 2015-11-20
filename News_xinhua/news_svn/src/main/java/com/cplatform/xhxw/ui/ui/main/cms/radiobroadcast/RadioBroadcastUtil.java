package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

import com.cplatform.xhxw.ui.R;
import com.google.gson.Gson;

public class RadioBroadcastUtil {
	/**
	 * Fragment 标识
	 */
	public static String TAG_FRAGMENT_BROADCASTLIST = "TAG_BROADCASTLIST";// 广播列表页
	public static String TAG_FRAGMENT_PLAY = "TAG_PLAY";// 广播播放页
	/**
	 * 播放状态
	 */
	public static final int STATE_RADIO_PLAY = 0x00;// 播放
	public static final int STATE_RADIO_PAUSE = 0x01;// 暂停
	public static final int STATE_RADIO_STOP = 0x02;// 停止
	public static final int STATE_RADIO_COMPLETION = 0x03;// 播放完成
	public static final int STATE_RADIO_PLAY_AUTO = 0x04;// 完成
	public static final int STATE_RADIO_PLAY_NONETWORK = 0x05;// 无网
	/**
	 * 播放时间
	 */
	public static final int SETTING_RADIO_TIME_NOLIMIT = 0;// 无限制
	public static final int SETTING_RADIO_TIME_MINUTE60 = 60;// 60分钟
	public static final int SETTING_RADIO_TIME_MINUTE30 = 30;// 30分钟
	public static final int SETTING_RADIO_TIME_MINUTE10 = 10;// 10分钟
	public static int SETTING_RADIO_TIME = SETTING_RADIO_TIME_NOLIMIT;// 定时停止播放时间,默认无限制
	public static long SETTING_RADIO_TIMER_END = SETTING_RADIO_TIME_NOLIMIT;// 定时结束时间
	/**
	 * 播放方式
	 */
	public static final int SETTING_RADIO_MODE_SINGLECHANNELLOOP = 0x01;// 单频道循环播放
	public static final int SETTING_RADIO_MODE_TURNCHANNELORDER = 0x02;// 转频道顺序播放
	public static int SETTING_RADIO_MODE = SETTING_RADIO_MODE_TURNCHANNELORDER;// 播放方式,默认为单频道循环播放
	/**
	 * 播放设置信息存储
	 */
	public static final String SPNAME = "RadioBroadcast";// 保存SharedPreferences名称
	public static final String VALUENAME_RADIO_MODE = "Radio_Mode";// 播放方式
	public static final String VALUENAME_RADIO_TIME = "Radio_Time";// 播放定时
	public static final String VALUENAME_RADIO_TIMER_END = "Radio_Timer_End";// 播放结束时间
	public static final String VALUENAME_RADIO_LIST = "RadioList_";
	public static boolean isShowNoNetwork = false;// 是否显示无网络对话框
	/**
	 * 播放频谱
	 */
	public static int[] ARRAY_SPECTRUM = { R.drawable.img_spectrum_1,
			R.drawable.img_spectrum_2, R.drawable.img_spectrum_3,
			R.drawable.img_spectrum_4, R.drawable.img_spectrum_5,
			R.drawable.img_spectrum_6, R.drawable.img_spectrum_7,
			R.drawable.img_spectrum_8, R.drawable.img_spectrum_9,
			R.drawable.img_spectrum_10, R.drawable.img_spectrum_11,
			R.drawable.img_spectrum_12, R.drawable.img_spectrum_13,
			R.drawable.img_spectrum_14, R.drawable.img_spectrum_15,
			R.drawable.img_spectrum_16 };
	public static int NUM_FRAME = ARRAY_SPECTRUM.length;
	public static int DURATION_SPECTRUM = 500;// 100毫秒每帧
	/**
	 * @Name getPlayTime
	 * @Description TODO
	 * @param ss
	 * @return
	 * @return String
	 * @Author zxe
	 * @Date 2015年8月31日 下午2:16:06
	 * 
	 */
	public static String getFormatPlayTime(int time) {
		int second = time / 1000;
		int h = 0;
		int d = 0;
		int s = 0;
		int temp = second % 3600;
		if (second > 3600) {
			h = second / 3600;
			if (temp != 0) {
				if (temp > 60) {
					d = temp / 60;
					if (temp % 60 != 0) {
						s = temp % 60;
					}
				} else {
					s = temp;
				}
			}
		} else {
			d = second / 60;
			if (second % 60 != 0) {
				s = second % 60;
			}
		}
		StringBuilder sb = new StringBuilder();
		if (h < 10) {
			sb.append("0" + h + ":");
		} else {
			sb.append(h + ":");
		}
		if (d < 10) {
			sb.append("0" + d + ":");
		} else {
			sb.append(d + ":");
		}
		if (s < 10) {
			sb.append("0" + s);
		} else {
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @Name setPlayTimer
	 * @Description TODO 设置播放定时
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午3:43:22
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	public static void setPlayTimer(Context con) {
		Log.d("设定播放时间", SETTING_RADIO_TIME + "分钟");
		if (SETTING_RADIO_TIME != SETTING_RADIO_TIME_NOLIMIT) {
			Calendar Cal = Calendar.getInstance();
			Cal.add(Calendar.MINUTE, SETTING_RADIO_TIME);
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd EE hh:mm:ss");
			Log.d(SETTING_RADIO_TIME + "后的时间",
					"date:" + format.format(Cal.getTime()));
			SETTING_RADIO_TIMER_END = Cal.getTimeInMillis();
		}
		SharedPreferences mySharedPreferences = con.getSharedPreferences(
				SPNAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		// 用putString的方法保存数据
		editor.putInt(VALUENAME_RADIO_TIME, SETTING_RADIO_TIME);
		editor.putLong(VALUENAME_RADIO_TIMER_END, SETTING_RADIO_TIMER_END);
		// 提交当前数据
		editor.commit();
	}

	/**
	 * 
	 * @Name isBeyondPlayTime
	 * @Description TODO 是否超出设置的播放时间
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年8月28日 下午8:44:06
	 * 
	 */
	public static boolean isBeyondPlayTime() {
		boolean isBeyond = false;
		if (SETTING_RADIO_TIMER_END != SETTING_RADIO_TIME_NOLIMIT) {
			isBeyond = System.currentTimeMillis() > SETTING_RADIO_TIMER_END;
		}
		// Log.d("是否超出设置的播放时间", isBeyond + "");
		return isBeyond;
	}

	/**
	 * 
	 * @Name saveInfoToSP
	 * @Description TODO 保存信息至SharedPreferences
	 * @param con
	 * @param valueName
	 * @param value
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月7日 上午11:23:46
	 * 
	 */
	public static void saveIntToSP(Context con, String valueName, int value) {
		SharedPreferences mySharedPreferences = con.getSharedPreferences(
				SPNAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		// 用putString的方法保存数据
		editor.putInt(valueName, value);
		// 提交当前数据
		editor.commit();
	}

	/**
	 * 
	 * @Name getRadioSettingInfo
	 * @Description TODO
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月7日 上午11:40:10
	 * 
	 */
	public static void getRadioSettingInfo(Context con) {
		SharedPreferences mySharedPreferences = con.getSharedPreferences(
				SPNAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		// 用putString的方法保存数据
		SETTING_RADIO_TIME = mySharedPreferences.getInt(VALUENAME_RADIO_TIME,
				SETTING_RADIO_TIME_NOLIMIT);
		SETTING_RADIO_TIMER_END = mySharedPreferences.getLong(
				VALUENAME_RADIO_TIMER_END, SETTING_RADIO_TIME_NOLIMIT);
		SETTING_RADIO_MODE = mySharedPreferences.getInt(VALUENAME_RADIO_MODE,
				SETTING_RADIO_MODE_TURNCHANNELORDER);
		// 提交当前数据
		editor.commit();
	}

	/**
	 * 
	 * @Name getIdByRadioModeSelect
	 * @Description TODO 获取广播设置中已选的播放方式单选id
	 * @return
	 * @return int
	 * @Author zxe
	 * @Date 2015年9月7日 下午12:00:33
	 * 
	 */
	public static int getIdByRadioModeSelect() {
		int id = R.id.rb_singlechannelloop;
		switch (SETTING_RADIO_MODE) {
		case SETTING_RADIO_MODE_SINGLECHANNELLOOP:// 单频道循环播放
			id = R.id.rb_singlechannelloop;
			break;
		case SETTING_RADIO_MODE_TURNCHANNELORDER:// 转频道顺序播放
			id = R.id.rb_turnchannelorder;
			break;
		}
		return id;
	}

	/**
	 * 
	 * @Name getIdByRadioTimerSelect
	 * @Description TODO 获取广播设置中已选的播放定时单选id
	 * @return
	 * @return int
	 * @Author zxe
	 * @Date 2015年9月7日 下午12:01:54
	 * 
	 */
	public static int getIdByRadioTimerSelect() {
		int id = R.id.rb_nolimit;
		switch (SETTING_RADIO_TIME) {
		case SETTING_RADIO_TIME_NOLIMIT:
			id = R.id.rb_nolimit;
			break;
		case SETTING_RADIO_TIME_MINUTE60:
			id = R.id.rb_minute60;
			break;
		case SETTING_RADIO_TIME_MINUTE30:
			id = R.id.rb_minute30;
			break;
		case SETTING_RADIO_TIME_MINUTE10:
			id = R.id.rb_minute10;
			break;
		// case SETTING_RADIO_TIME_MINUTE10:
		// id=R.id.rb_custom;
		// break;
		}
		return id;
	}

	/**
	 * 
	 * @Name showNoNetworkDialog
	 * @Description TODO 网络提示框
	 * @param con
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月21日 下午2:38:33
	 * 
	 */
	public static void showNoNetworkDialog(Context con) {
		isShowNoNetwork = true;
		new AlertDialog.Builder(con)
				.setMessage(R.string.text_no_network)
				.setTitle(R.string.text_play_tips)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	/**
	 * 
	 * @Name showTipInformationDialog
	 * @Description TODO 提示消息框
	 * @param con
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月21日 下午2:39:34
	 * 
	 */
	public static void showTipInformationDialog(Context con, int titleId,
			int messageId) {
		isShowNoNetwork = true;
		new AlertDialog.Builder(con)
				.setTitle(titleId)
				.setMessage(messageId)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	/**
	 * 
	 * @Name saveRadioList
	 * @Description TODO 存储广播列表
	 * @param listGame
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月22日 上午11:03:02
	 * 
	 */
	public static void saveRadioList(Context con, String content, String catid) {
		SharedPreferences mySharedPreferences = con.getSharedPreferences(
				SPNAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		// 用putString的方法保存数据
		editor.putString(VALUENAME_RADIO_LIST + catid, content);
		// 提交当前数据
		editor.commit();
	}

	public static void getRadioList(Context con, String catid) {
		SharedPreferences mySharedPreferences = con.getSharedPreferences(
				SPNAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		// 用putString的方法保存数据
		String content = mySharedPreferences.getString(VALUENAME_RADIO_LIST
				+ catid, "");
		// 提交当前数据
		editor.commit();

		RadioListResponse rlr = new Gson().fromJson(content,
				RadioListResponse.class);
		List<DataRadioBroadcast> listGame = null;
		if (rlr != null) {
			listGame = rlr.getData().getList();

		} else {
			RadioListRadioResponse rlrr = new Gson().fromJson(content,
					RadioListRadioResponse.class);
			if (rlrr != null) {
				listGame = rlrr.getData();
			}
		}
		if (listGame != null) {
			RadioBroadcastFragment.player.setListData(listGame);
		}

		// RadioListRadioResponse response_ = new Gson().fromJson(content,
		// RadioListRadioResponse.class);
		// List<DataRadioBroadcast> listGame=response_.getData();

	}
}
