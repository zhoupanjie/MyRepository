package com.hy.superemsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.hy.superemsg.data.Account;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.GameContentDetail;
import com.hy.superemsg.rsp.HolidayContentDetail;
import com.hy.superemsg.rsp.MmsContentDetail;
import com.hy.superemsg.rsp.RingContentDetail;
import com.hy.superemsg.rsp.SmsContentDetail;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.MobileInfo;

public class SuperEMsgApplication extends Application {
	public static final String EXTRA_MMS_DETAIL = "extra_mms_detail";
	public static final String EXTRA_SEND_CONTENT = "extra_sms_detail";
	public static final String EXTRA_GAME_DETAIL = "extra_game_detail";
	public static final String EXTRA_BOOK_DETAIL = "extra_book_detail";
	public static final String EXTRA_DRAMA_DETAIL = "extra_drama_detail";
	public static final String EXTRA_NEWS_DETAIL = "extra_news_detail";
	public static final String EXTRA_PIC_DETAIL = "extra_pic_detail";
	public static final String EXTRA_WEB_URL = "extra_web_url";
	public static final String EXTRA_SEND_NAMES = "extra_send_names";
	public static final String EXTRA_SEND_VOICEWISH = "extra_send_voicewish";
	public static final String EXTRA_SEND_SMSID = "extra_send_smsid";
	public static final String EXTRA_FROM_RING_TO_VALIDATE = "extra_from_ring_to_validate";
	public static final int REQUEST_LIST_TO_COLLECTION = 0x1001;
	public static final int REQUEST_SEND_SMS = 0x1002;
	public static final int REQUEST_VALIDATE_FROM_MAIN = 0x1003;
	public static final int REQUEST_VALIDATE_FROM_SMS = 0x1004;
	public static final int REQUEST_LIST_TO_MMS_DETAIL = 0x1005;
	public static final int PAGE_SIZE = 10;
	public static Account account;
	public static String festivalRemind = null;
	public static Context _context = null;

	@Override
	public void onCreate() {
		super.onCreate();
		DBUtils.initInstance(this);
		checkAccount();
		HttpUtils.getInst().initContext(getApplicationContext());
		_context = this;
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				loadDataFromDB();
				return null;
			}
		}.execute();

	}

	private void loadDataFromDB() {
		List<Category> smsCates = DBUtils.getInst().queryCategory(
				DBHelper.TABLE_SMS);
		List<Category> mmsCates = DBUtils.getInst().queryCategory(
				DBHelper.TABLE_MMS);
		List<Category> ringCates = DBUtils.getInst().queryCategory(
				DBHelper.TABLE_RING);
		List<Category> gameCates = DBUtils.getInst().queryCategory(
				DBHelper.TABLE_GAME);
		if (CommonUtils.isNotEmpty(smsCates)) {
			for (Category cate : smsCates) {
				List<SmsContentDetail> sms = DBUtils.getInst().querySms(false,
						cate.categoryid);
				cached_sms.put(cate, sms);
			}
		}
		if (CommonUtils.isNotEmpty(mmsCates)) {
			for (Category cate : mmsCates) {
				cached_mms.put(cate,
						DBUtils.getInst().queryMms(false, cate.categoryid));
			}
		}
		if (CommonUtils.isNotEmpty(ringCates)) {
			for (Category cate : ringCates) {
				cached_ring.put(cate,
						DBUtils.getInst().queryRing(false, cate.categoryid));
			}
		}
		if (CommonUtils.isNotEmpty(gameCates)) {
			for (Category cate : gameCates) {
				cached_game.put(cate,
						DBUtils.getInst().queryGame(false, cate.categoryid));
			}
		}
	}

	public final static String SHARED_PREFERENCES = "shared_preference";
	public final static String SAVED_SIM_SN = "saved_sim_sn";
	public final static String SAVED_PHONE = "saved_phone";
	public final static String SAVED_OPERATOR = "saved_operator";
	public final static String SAVED_SIM_LOCATION = "saved_sim_location";

	public static void toast(String msg) {
		if (_context != null && !TextUtils.isEmpty(msg)) {
			android.widget.Toast.makeText(_context, msg, Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void checkAccount() {
		if (MobileInfo.getSimState(this) == TelephonyManager.SIM_STATE_READY) {
			String simsn = MobileInfo.getSIMSN(this);
			String savedSimsn = getSharedPreferences(SHARED_PREFERENCES,
					Context.MODE_WORLD_READABLE).getString(SAVED_SIM_SN, null);
			if (savedSimsn != null && simsn.equals(savedSimsn)) {
				String savedPhone = getSharedPreferences(SHARED_PREFERENCES,
						Context.MODE_WORLD_READABLE).getString(SAVED_PHONE,
						null);
				int savedOperator = getSharedPreferences(SHARED_PREFERENCES,
						Context.MODE_WORLD_READABLE).getInt(SAVED_OPERATOR, 0);
				String savedSimLocation = getSharedPreferences(
						SHARED_PREFERENCES, Context.MODE_WORLD_READABLE)
						.getString(SAVED_SIM_LOCATION, null);
				account = new Account();
				account.operator = savedOperator;
				account.phonenum = savedPhone;
				account.province = savedSimLocation;
				account.sim = savedSimsn;

			} else {
				if (CommonUtils.isNotEmpty(cached_ring)) {
					cached_ring.clear();
				}
			}
		}
	}

	public void saveAccount() {
		if (account != null) {
			getSharedPreferences(SHARED_PREFERENCES,
					Context.MODE_WORLD_READABLE).edit()
					.putString(SAVED_SIM_SN, MobileInfo.getSIMSN(this))
					.putString(SAVED_PHONE, account.phonenum)
					.putInt(SAVED_OPERATOR, account.operator)
					.putString(SAVED_SIM_LOCATION, account.province).commit();
		}
	}

	public static Map<Category, List<SmsContentDetail>> cached_sms = new HashMap<Category, List<SmsContentDetail>>();
	public static Map<Category, List<MmsContentDetail>> cached_mms = new HashMap<Category, List<MmsContentDetail>>();
	public static Map<Category, List<RingContentDetail>> cached_ring = new HashMap<Category, List<RingContentDetail>>();;
	public static Map<Category, List<GameContentDetail>> cached_game = new HashMap<Category, List<GameContentDetail>>();;
	public static Map<Category, List<HolidayContentDetail>> cached_holiday = new HashMap<Category, List<HolidayContentDetail>>();;

	
}
