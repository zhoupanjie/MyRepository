package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.TextViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 下载列表
 * 
 * @author CharmingLee 2015-6-18
 */
public class GameDownloadCenterActivity extends BaseActivity {
	@InjectView(R.id.lv_downcenter)
	ListView lv;

	Context con;
	DownHistoryAdapter dha;
	private DataReceiver receiverDownloadCenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloadlist);
		con = this;
		ButterKnife.inject(this);
		initActionBar();
		dha = new DownHistoryAdapter(con,
				GameManager.getAllGameDownCacheList(con));
		dha.setListView(lv);
		lv.setAdapter(dha);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		receiverDownloadCenter = new DataReceiver();
		IntentFilter filter = new IntentFilter(GameUtil.ACTION_GAME);
		registerReceiver(receiverDownloadCenter, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiverDownloadCenter);
	}

	class DownHistoryAdapter extends BaseAdapter {
		List<Game> lsGameId;
		ListView listView;

		public DownHistoryAdapter(Context context, List<Game> ls) {
			super();
			lsGameId = ls;
		}

		public ListView getListView() {
			return listView;
		}

		public void setListView(ListView listView) {
			this.listView = listView;
		}

		public List<Game> getLsGameId() {
			return lsGameId;
		}

		public void setLsGameId(List<Game> lsGameId) {
			this.lsGameId = lsGameId;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lsGameId.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return lsGameId.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		void setDownProgressShow(boolean isShow, ViewHolderGameList holder) {
			if (isShow) {
				holder.tvType.setVisibility(View.GONE);
				holder.tvDescription.setVisibility(View.VISIBLE);
				holder.pbDown.setVisibility(View.VISIBLE);
				holder.tvDownInfo.setVisibility(View.VISIBLE);
			} else {
				holder.pbDown.setVisibility(View.GONE);
				holder.tvDownInfo.setVisibility(View.GONE);
				holder.tvType.setVisibility(View.VISIBLE);
				holder.tvDescription.setVisibility(View.VISIBLE);
			}
		}

		void setTextsize(ViewHolderGameList holder, Game item) {
			TextViewUtil.setDisplayModel(con, holder.tvName, holder.tvType,
					item.isRead());
			float textSize = getTitleTextSize();
			holder.tvName.setTextSize(textSize);
			if (textSize > 16) {
				holder.tvType.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE + 2);
			} else {
				holder.tvType.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE);
			}
		}

		/**
		 * 获取新闻列表页标题文字大小
		 * 
		 * @return
		 */
		public float getTitleTextSize() {
			int textStyle = Constants.getNewsDetTextSize();
			int textSize = 18;
			switch (textStyle) {
			case NewsDetTextSize.SMALL:
				textSize = 14;
				break;
			case NewsDetTextSize.MIDDLE:
				textSize = 16;
				break;
			case NewsDetTextSize.BIG:
				textSize = 18;
				break;
			case NewsDetTextSize.SUPER_BIG:
				textSize = 22;
				break;
			}
			return textSize;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolderGameList holder;
			if (convertView == null) {
				convertView = View.inflate(con, R.layout.item_gamelist, null);
				holder = new ViewHolderGameList();
				ButterKnife.inject(holder, convertView);
				/* 得到各个控件的对象 */
				convertView.setTag(holder); // 绑定ViewHolder对象
			} else {
				holder = (ViewHolderGameList) convertView.getTag(); // 取出ViewHolder对象
			}
			final Game item = lsGameId.get(position);
			TextViewUtil.setDisplayModel(con, holder.tvName, holder.tvType,
					item.isRead());
			if (item != null) {
				setTextsize(holder, item);
				if (TextUtils.isEmpty(item.getName())) {
					holder.tvName.setText(R.string.game_text_unknown);
				} else {
					holder.tvName.setText(item.getName());
				}
				if (TextUtils.isEmpty(item.getCatname())) {
					holder.tvType.setText(R.string.game_text_unknown);
				} else {
					holder.tvType.setText(item.getCatname());
				}
				if (TextUtils.isEmpty(item.getSummary())) {
					holder.tvDescription.setText(R.string.game_text_unknown);
				} else {
					holder.tvDescription.setText(item.getSummary());
				}
				if (TextUtils.isEmpty(item.getIcon())) {
					holder.ivIcon.setImageResource(R.drawable.def_img_16_9);
				} else {
					try {
						ImageLoader.getInstance().displayImage(item.getIcon(),
								holder.ivIcon, GameUtil.dio);
					} catch (Exception e) {
						Log.e("加载图片错误", e.getMessage().toString());
					}
				}
				if (new File(Constants.Directorys.DOWNLOAD
						+ GameUtil.getFileNameByUrl(item.getDownloadurl()))
						.exists()) {
					if ((item.getStateDown() == GameUtil.GAME_DOWN_CONTINUE)) {// 下载中，恢复为暂停
						// item.setStateDown(GameUtil.GAME_DOWN_PAUSE);
					} else if ((item.getStateDown() == GameUtil.GAME_INSTALL_ING)) {// 安装中恢复为下载完成状态
						item.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
					}
				} else {
					GameManager.delGameCashByAppId(con, item.getId(),
							item.getDownloadurl());
					GameManager.removeDownTask(con, item.getId());
					// item.setStateDown(GameUtil.GAME_DOWN_UN);
				}
				if ((item.getStateDown() == GameUtil.GAME_INSTALL_COMPLETE)
						&& (false == GameUtil.isInstallByPackageName(con,
								item.getPackageName()))) {// 安装中恢复为下载完成状态
					item.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
				}
				if (item.getFileSize() != 0) {
					// holder.pbDown.setVisibility(View.VISIBLE);
					holder.pbDown.setMax(item.getFileSize());
					if (item.getComplete() != 0) {
						holder.pbDown.setProgress(item.getComplete());
					}
				}
				switch (item.getStateDown()) {
				case GameUtil.GAME_DOWN_UN:
					holder.ivDown.setText(R.string.game_down);
					setDownProgressShow(false, holder);
					break;
				case GameUtil.GAME_DOWN_WAIT:
					holder.ivDown.setText(R.string.game_down_wait);
					setDownProgressShow(false, holder);
					break;
				case GameUtil.GAME_DOWN_ING:
					holder.ivDown.setText(R.string.game_down_pause);
					holder.tvDownInfo.setText(GameUtil.toMByB(item
							.getComplete())
							+ "/"
							+ GameUtil.toMByB(item.getFileSize()));
					setDownProgressShow(true, holder);
					break;
				case GameUtil.GAME_DOWN_CONTINUE:
					holder.ivDown.setText(R.string.game_down_pause);
					holder.tvDownInfo.setText(GameUtil.toMByB(item
							.getComplete())
							+ "/"
							+ GameUtil.toMByB(item.getFileSize()));
					setDownProgressShow(true, holder);
					break;
				case GameUtil.GAME_DOWN_PAUSE:
					holder.ivDown.setText(R.string.game_down_continue);
					holder.tvDownInfo.setText(GameUtil.toMByB(item
							.getComplete())
							+ "/"
							+ GameUtil.toMByB(item.getFileSize()));
					setDownProgressShow(true, holder);
					break;
				case GameUtil.GAME_DOWN_COMPLETE:
					holder.ivDown.setText(R.string.game_down_install);
					if ((item.getFileSize() == item.getComplete())
							&& (!TextUtils.isEmpty(item.getDownTime()))) {
						holder.tvDescription.setText("下载时间:"
								+ item.getDownTime());
					}
					setDownProgressShow(false, holder);
					break;
				case GameUtil.GAME_DOWN_FAILURE:
					holder.ivDown.setText(R.string.game_down);
					holder.tvDownInfo.setText(R.string.game_text_down_failure);
					setDownProgressShow(true, holder);
					break;
				case GameUtil.GAME_INSTALL_ING:
					holder.ivDown.setText(R.string.game_down_install);
					setDownProgressShow(false, holder);
					break;
				case GameUtil.GAME_INSTALL_COMPLETE:
					holder.ivDown.setText(R.string.game_install_open);
					if ((item.getFileSize() == item.getComplete())
							&& (!TextUtils.isEmpty(item.getInstallTime()))) {
						holder.tvDescription.setText("安装时间:"
								+ item.getInstallTime());
					}
					setDownProgressShow(false, holder);
					break;
				case GameUtil.GAME_INSTALL_FAILURE:
					holder.ivDown.setText(R.string.game_down_install);
					if ((item.getFileSize() == item.getComplete())
							&& (!TextUtils.isEmpty(item.getDownTime()))) {
						holder.tvDescription.setText("下载时间:"
								+ item.getDownTime());
					}
					setDownProgressShow(false, holder);
					break;
				}
				holder.ivDown.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						setOnClickDownButton((TextView) arg0, item);
					}
				});
			}
			return convertView;
		}

		void setOnClickDownButton(TextView ivDown, Game item) {

			if (item.getStateDown() == GameUtil.GAME_DOWN_UN) {// 未下载状态
				GameManager.setStartDown(con, ivDown, item,
						R.string.game_down_pause, GameUtil.GAME_DOWN_ING);
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_ING) {
				// GameUtil.addPauseId(item.getId());
				GameManager.onPauseById(con, item.getId());
				ivDown.setText(R.string.game_down_continue);
				item.setStateDown(GameUtil.GAME_DOWN_PAUSE);
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_PAUSE) {
				// GameUtil.removePauseId(item.getId());
				GameManager.setStartDown(con, ivDown, item,
						R.string.game_down_pause, GameUtil.GAME_DOWN_CONTINUE);
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_CONTINUE) {
				// GameUtil.addPauseId(item.getId());
				GameManager.onPauseById(con, item.getId());
				ivDown.setText(R.string.game_down_continue);
				item.setStateDown(GameUtil.GAME_DOWN_PAUSE);
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_COMPLETE) {
				if(new File(Constants.Directorys.DOWNLOAD
						+ GameUtil.getFileNameByUrl(item.getDownloadurl()))
						.exists()){
					GameUtil.installAPK((Activity) con, item.getId(),
							item.getDownloadurl(), item.getPackageName(), 3);
				}else{
					item.setStateDown(GameUtil.GAME_DOWN_UN);
					ivDown.setText(R.string.game_down);
				}
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_FAILURE) {
				GameManager.setStartDown(con, ivDown, item,
						R.string.game_down_pause, GameUtil.GAME_DOWN_ING);
			} else if (item.getStateDown() == GameUtil.GAME_INSTALL_COMPLETE) {
				GameUtil.openAppByByPackageName(con, item.getPackageName());
				ivDown.setText(R.string.game_install_open);
				item.setStateDown(GameUtil.GAME_INSTALL_COMPLETE);
			} else if (item.getStateDown() == GameUtil.GAME_INSTALL_FAILURE) {
				if(new File(Constants.Directorys.DOWNLOAD
						+ GameUtil.getFileNameByUrl(item.getDownloadurl()))
						.exists()){
					GameUtil.installAPK((Activity) con, item.getId(),
							item.getDownloadurl(), item.getPackageName(), 3);
				}else{
					item.setStateDown(GameUtil.GAME_DOWN_UN);
					ivDown.setText(R.string.game_down);
				}
			}
		}

		int getPosByGameId(String gameId) {
			int Pos = -1;
			for (Game item : lsGameId) {
				if (item.getId().equals(gameId)) {
					Pos = lsGameId.indexOf(item);
				}
			}
			// if (lsGameId.contains(gameId)) {
			// Pos = lsGameId.indexOf(gameId);
			// }
			return Pos;
		}

		View getItemRootViewByIndex(int pos) {
			View vRoot = null;

			if (pos != -1) {

				int visiblePos = listView.getFirstVisiblePosition()
						- listView.getHeaderViewsCount();
				int offset = pos - visiblePos;
				// Log.e("",
				// "index="+index+"visiblePos="+visiblePos+"offset="+offset);
				// 只有在可见区域才更新
				if (false == (offset < 0)) {
					vRoot = listView.getChildAt(offset);
				}
			}
			return vRoot;
		}

		public void updateInstallResult(String GameId, String packageName) {
			int pos = getPosByGameId(GameId);
			if (pos != -1) {
				View view = getItemRootViewByIndex(pos);
				if ((view != null) && (view.getTag() != null)) {
					ViewHolderGameList holder = (ViewHolderGameList) view
							.getTag();
					setDownProgressShow(true, holder);
					if (GameUtil.isInstallByPackageName(con, packageName)) {
						Log.d("安装结果", "成功");
						setDownProgressShow(false, holder);
						holder.ivDown.setText(R.string.game_install_open);
						holder.tvDescription.setText("安装完成");
						lsGameId.get(pos).setStateDown(
								GameUtil.GAME_INSTALL_COMPLETE);
						GameManager.updateGameCacheByDown(con, GameId,
								GameUtil.GAME_TAG_INSTALL_SUCCESS, 0, 0);
					} else {
						Log.d("安装结果", "失败");
						setDownProgressShow(false, holder);
						holder.ivDown.setText(R.string.game_down_install);
						holder.tvDescription.setText("等待安装");
						lsGameId.get(pos).setStateDown(
								GameUtil.GAME_DOWN_COMPLETE);
						GameManager.updateGameCacheByDown(con, GameId,
								GameUtil.GAME_TAG_INSTALL_FAILURE, 0, 0);
					}
				}
			}
			GameUtil.removeApkByGameId(GameId);
		}

		public void updateItem(Intent i) {
			int runState = i.getIntExtra("RunState", 0);
			String GameId = i.getStringExtra("GameId");
			int FileSize = i.getIntExtra("FileSize", 0);
			int Complete = i.getIntExtra("Complete", 0);
			int pos = getPosByGameId(GameId);
			if (pos != -1) {
				View view = getItemRootViewByIndex(pos);
				if ((view != null) && (view.getTag() != null)) {
					ViewHolderGameList holder = (ViewHolderGameList) view
							.getTag();
					setDownProgressShow(true, holder);
					lsGameId.get(pos).setFileSize(FileSize);
					lsGameId.get(pos).setComplete(Complete);
					holder.pbDown.setMax(FileSize);
					holder.pbDown.setProgress(Complete);
					holder.tvDownInfo.setText(GameUtil.toMByB(Complete) + "/"
							+ GameUtil.toMByB(FileSize));
					// 下载结果判断
					switch (runState) {
					case GameUtil.GAME_TAG_DOWN_SUCCESS: {// 成功
						lsGameId.get(pos).setStateDown(GameUtil.GAME_DOWN_ING);
						holder.ivDown.setText(R.string.game_down_pause);
						int totalSize = i.getIntExtra("totalSize", 0);
						holder.tvDownInfo.setText(GameUtil.toMByB(Complete)
								+ "/" + GameUtil.toMByB(FileSize)
								+ GameUtil.getGameDownSpeed(totalSize));
						holder.pbDown.incrementProgressBy(totalSize);
						if (holder.pbDown.getProgress() == holder.pbDown
								.getMax()) {
							holder.ivDown.setText(R.string.game_down_install);
							lsGameId.get(pos).setStateDown(
									GameUtil.GAME_DOWN_COMPLETE);
							// 下载完成后清除进度条并将map中的数据清空
							setDownProgressShow(false, holder);
							holder.tvDescription.setText("下载完成");
							// GameManager.removeTask(GameId);
						}
					}
						break;
					case GameUtil.GAME_TAG_DOWN_FAILURE: {// 失败
						// if(GameUtil.isPause(GameId)){
						lsGameId.get(pos).setStateDown(GameUtil.GAME_DOWN_ING);
						holder.ivDown.setText(R.string.game_down_pause);
						// }
						int totalSize = i.getIntExtra("totalSize", 0);
						holder.pbDown.incrementProgressBy(totalSize);
						holder.ivDown.setText(R.string.game_down);
						lsGameId.get(pos).setStateDown(
								GameUtil.GAME_DOWN_FAILURE);
						setDownProgressShow(false, holder);
						// 下载完成后清除进度条并将map中的数据清空
						holder.tvDescription.setText("下载失败");
					}
						break;
					case GameUtil.GAME_TAG_DOWN_ING: {// 下载中
						lsGameId.get(pos).setStateDown(GameUtil.GAME_DOWN_ING);
						holder.ivDown.setText(R.string.game_down_pause);
					}
						break;
					case GameUtil.GAME_TAG_DOWN_PAUSE: {// 暂停
						lsGameId.get(pos)
								.setStateDown(GameUtil.GAME_DOWN_PAUSE);
						holder.ivDown.setText(R.string.game_down_continue);
						int totalSize = i.getIntExtra("totalSize", 0);
						holder.pbDown.incrementProgressBy(totalSize);
						if (holder.pbDown.getProgress() == holder.pbDown
								.getMax()) {
							holder.ivDown.setText(R.string.game_down_install);
							lsGameId.get(pos).setStateDown(
									GameUtil.GAME_DOWN_COMPLETE);
							// 下载完成后清除进度条并将map中的数据清空
							holder.tvDownInfo.setText("下载完成");
							// GameManager.removeTask(GameId);
						}
					}
						break;
					}
				} else {
					Log.e("updateView", "游戏列表下载成功hole空");
				}
			}
		}

		public class ViewHolderGameList {
			@InjectView(R.id.rl_root)
			public RelativeLayout rlRoot;
			@InjectView(R.id.iv_logo)
			public ImageView ivIcon;// 图片
			@InjectView(R.id.tv_name)
			public TextView tvName;// 应用名称
			@InjectView(R.id.tv_type)
			public TextView tvType;// 类型
			@InjectView(R.id.tv_desc)
			public TextView tvDescription;// 描述
			@InjectView(R.id.iv_download)
			public TextView ivDown;// 下载
			@InjectView(R.id.pb_down)
			public ProgressBar pbDown;
			@InjectView(R.id.tv_down_info)
			TextView tvDownInfo;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GameUtil.REQUEST_CODE_INSTALL: {// 选择图片
			List<APKInstallData> laid = GameUtil.getInstallListByUI(3);
			if (laid.isEmpty() == false) {
				for (APKInstallData aid : laid) {
					String gameId = aid.getGameId();
					// String downUrl = data.getStringExtra("DownUrl");
					// String apkPath = data.getStringExtra("APKPath");
					String packageName = aid.getPackageName();
					dha.updateInstallResult(gameId, packageName);
				}
			}
		}
			break;
		}
	}

	private class DataReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (dha != null && arg1 != null) {
				dha.updateItem(arg1);
			}
		}

	}

	@Override
	protected String getScreenName() {
		// TODO Auto-generated method stub
		return "GameDownloadCenterActivity";
	}
}
