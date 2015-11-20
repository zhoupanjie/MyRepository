package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.util.TextViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GameAdapter extends BaseAdapter<Game> {
	private Context con;
	ListView listView;

	public GameAdapter(Context context) {
		super(context);
		con = context;
	}

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public void close() {
		clearData();
		mData = null;
		mContext = null;
		mInflater = null;
	}

	/**
	 * 获取新闻列表页标题文字大小
	 * 
	 * @return
	 */
	public static float getTitleTextSize() {
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

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderGameList holder;
		if (convertView == null) {
			convertView = View.inflate(con, R.layout.item_gamelist, null);
			holder = new ViewHolderGameList();
			ButterKnife.bind(holder, convertView);
			/* 得到各个控件的对象 */
			convertView.setTag(holder); // 绑定ViewHolder对象
		} else {
			holder = (ViewHolderGameList) convertView.getTag(); // 取出ViewHolder对象
		}
		final Game item = getItem(position);
		TextViewUtil.setDisplayModel(con, holder.tvName, holder.tvType,
				item.isRead());
		if (item != null) {
			setTextsize(holder, item);
			setItemData(holder, item);
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

	void setItemData(ViewHolderGameList holder, Game item) {
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
				if ((!TextUtils.isEmpty(item.getIcon()))
						&& holder.ivIcon != null) {
					ImageLoader.getInstance().displayImage(item.getIcon(),
							holder.ivIcon, GameUtil.dio);
				}
			} catch (Exception e) {
				Log.e("加载图片错误", e.getMessage().toString());
			}
		}
		item = GameManager.setGameByDownHistory(con, item);
		if (item.getFileSize() != 0) {
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
			holder.tvDownInfo.setText(GameUtil.toMByB(item.getComplete()) + "/"
					+ GameUtil.toMByB(item.getFileSize()));
			setDownProgressShow(true, holder);
			break;
		case GameUtil.GAME_DOWN_CONTINUE:
			holder.ivDown.setText(R.string.game_down_pause);
			holder.tvDownInfo.setText(GameUtil.toMByB(item.getComplete()) + "/"
					+ GameUtil.toMByB(item.getFileSize()));
			setDownProgressShow(true, holder);
			break;
		case GameUtil.GAME_DOWN_PAUSE:
			holder.ivDown.setText(R.string.game_down_continue);
			holder.tvDownInfo.setText(GameUtil.toMByB(item.getComplete()) + "/"
					+ GameUtil.toMByB(item.getFileSize()));
			setDownProgressShow(true, holder);
			break;
		case GameUtil.GAME_DOWN_COMPLETE:
			holder.ivDown.setText(R.string.game_down_install);
			if ((item.getFileSize() == item.getComplete())
					&& (!TextUtils.isEmpty(item.getDownTime()))) {
				holder.tvDescription.setText("下载时间:" + item.getDownTime());
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
				holder.tvDescription.setText("安装时间:" + item.getInstallTime());
			}
			setDownProgressShow(false, holder);
			break;
		case GameUtil.GAME_INSTALL_FAILURE:
			holder.ivDown.setText(R.string.game_down_install);
			if ((item.getFileSize() == item.getComplete())
					&& (!TextUtils.isEmpty(item.getDownTime()))) {
				holder.tvDescription.setText("下载时间:" + item.getDownTime());
			}
			setDownProgressShow(false, holder);
			break;
		}
	}

	void setOnClickDownButton(TextView ivDown, Game item) {

		if (item.getStateDown() == GameUtil.GAME_DOWN_UN) {// 未下载状态
			// GameUtil.removePauseId(item.getId());
			GameManager.setStartDown(con, ivDown, item,
					R.string.game_down_pause, GameUtil.GAME_DOWN_ING);
			// ivDown.setText();
			// item.setStateDown();
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
						item.getDownloadurl(), item.getPackageName(), 1);
			}else{
				item.setStateDown(GameUtil.GAME_DOWN_UN);
				ivDown.setText(R.string.game_down);
			}
			
		} else if (item.getStateDown() == GameUtil.GAME_DOWN_FAILURE) {
			// GameUtil.removePauseId(item.getId());
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
						item.getDownloadurl(), item.getPackageName(), 1);
			}else{
				item.setStateDown(GameUtil.GAME_DOWN_UN);
				ivDown.setText(R.string.game_down);
			}
		}
	}

	public void updateInstallResult(String GameId, String packageName) {
		int pos = getPosByGameId(GameId);
		if (pos != -1) {
			View view = getItemRootViewByIndex(pos);
			if ((view != null) && (view.getTag() != null)) {
				ViewHolderGameList holder = (ViewHolderGameList) view.getTag();
				setDownProgressShow(false, holder);
				if (GameUtil.isInstallByPackageName(con, packageName)) {
					Log.d("安装结果", "成功");
					holder.ivDown.setText(R.string.game_install_open);
					holder.tvDescription.setText("安装完成");
					getData().get(pos).setStateDown(
							GameUtil.GAME_INSTALL_COMPLETE);
					GameManager.updateGameCacheByDown(con, GameId, GameUtil.GAME_TAG_INSTALL_SUCCESS, 0, 0);
				} else {
					Log.d("安装结果", "失败");

					holder.ivDown.setText(R.string.game_down_install);
					holder.tvDescription.setText("等待安装");
					getData().get(pos)
							.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
					GameManager.updateGameCacheByDown(con, GameId, GameUtil.GAME_TAG_INSTALL_FAILURE, 0, 0);
				}
			}
		}
		GameUtil.removeApkByGameId(GameId);
	}

	public void updateItem(Intent i) {
		Log.d("GameAdapter下载更新", "开始");
		if (i != null) {

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
					holder.pbDown.setMax(FileSize);
					holder.pbDown.setProgress(Complete);
					// 下载结果判断
					switch (runState) {
					case GameUtil.GAME_TAG_DOWN_SUCCESS: {// 成功
						getData().get(pos).setFileSize(FileSize);
						getData().get(pos).setComplete(Complete);
						getData().get(pos).setStateDown(GameUtil.GAME_DOWN_ING);
						holder.ivDown.setText(R.string.game_down_pause);
						int totalSize = i.getIntExtra("totalSize", 0);
						holder.pbDown.incrementProgressBy(totalSize);
						 holder.tvDownInfo.setText(GameUtil.toMByB(Complete) +
						 "/"
						 + GameUtil.toMByB(FileSize)+GameUtil.getGameDownSpeed(totalSize));
//						holder.tvDownInfo.setText(GameUtil.toMByB(Complete)
//								+ "/" + GameUtil.toMByB(FileSize));
						if (holder.pbDown.getProgress() == holder.pbDown
								.getMax()) {
							holder.ivDown.setText(R.string.game_down_install);
							getData().get(pos).setStateDown(
									GameUtil.GAME_DOWN_COMPLETE);
							setDownProgressShow(false, holder);
							// 下载完成后清除进度条并将map中的数据清空
							holder.tvDescription.setText("下载完成");
							// GameManager.removeTask(GameId);
						} else {

						}
					}
						break;
					case GameUtil.GAME_TAG_DOWN_FAILURE: {// 失败
						getData().get(pos).setFileSize(FileSize);
						getData().get(pos).setComplete(Complete);
						// if (GameUtil.isPause(GameId)) {
						 getData().get(pos).setStateDown(GameUtil.GAME_DOWN_ING);
						 holder.ivDown.setText(R.string.game_down_pause);
						// }
						int totalSize = i.getIntExtra("totalSize", 0);
						holder.pbDown.incrementProgressBy(totalSize);
//						 holder.tvDownInfo.setText(GameUtil.toMByB(Complete) +
//						 "/"
//						 + GameUtil.toMByB(FileSize)+"     "+
//						 GameUtil.toMByB(totalSize));
						holder.tvDownInfo.setText(GameUtil.toMByB(Complete)
								+ "/" + GameUtil.toMByB(FileSize));
						holder.ivDown.setText(R.string.game_down);
						getData().get(pos).setStateDown(
								GameUtil.GAME_DOWN_FAILURE);
						setDownProgressShow(false, holder);
						// 下载完成后清除进度条并将map中的数据清空
						holder.tvDescription.setText("下载失败");
					}
						break;
					case GameUtil.GAME_TAG_DOWN_ING: {// 下载中
						getData().get(pos).setFileSize(FileSize);
						getData().get(pos).setComplete(Complete);
						getData().get(pos).setStateDown(GameUtil.GAME_DOWN_ING);
						holder.ivDown.setText(R.string.game_down_pause);
						holder.tvDownInfo.setText(GameUtil.toMByB(Complete)
								+ "/" + GameUtil.toMByB(FileSize));
					}
						break;
					case GameUtil.GAME_TAG_DOWN_PAUSE: {// 暂停
						getData().get(pos).setFileSize(FileSize);
						getData().get(pos).setComplete(Complete);
						getData().get(pos).setStateDown(
								GameUtil.GAME_DOWN_PAUSE);
						holder.ivDown.setText(R.string.game_down_continue);
						int totalSize = i.getIntExtra("totalSize", 0);
						holder.pbDown.incrementProgressBy(totalSize);
//						 holder.tvDownInfo.setText(GameUtil.toMByB(Complete) +
//						 "/"
//						 + GameUtil.toMByB(FileSize)+"     "+
//						 GameUtil.toMByB(totalSize));
						holder.tvDownInfo.setText(GameUtil.toMByB(Complete)
								+ "/" + GameUtil.toMByB(FileSize));
						if (holder.pbDown.getProgress() == holder.pbDown
								.getMax()) {
							holder.ivDown.setText(R.string.game_down_install);
							getData().get(pos).setStateDown(
									GameUtil.GAME_DOWN_COMPLETE);
							// 下载完成后清除进度条并将map中的数据清空
							holder.tvDownInfo.setText("下载完成");
						}
					}
						break;
					}
				} else {
					Log.d("updateView", "游戏列表下载成功hole空");
				}
			}

		}
		Log.d("GameAdapter下载更新", "结束");
	}

	int getPosByGameId(String gameId) {
		int Pos = -1;
		for (Game item : getData()) {
			if (item.getId().equals(gameId)) {
				Pos = getData().indexOf(item);
				break;
			}
		}
		return Pos;
	}

	View getItemRootViewByIndex(int pos) {
		View vRoot = null;

		if (pos != -1) {

			int visiblePos = listView.getFirstVisiblePosition()
					- listView.getHeaderViewsCount();
			int offset = pos - visiblePos;
			// Log.d("",
			// "index="+index+"visiblePos="+visiblePos+"offset="+offset);
			// 只有在可见区域才更新
			if (false == (offset < 0)) {
				vRoot = listView.getChildAt(offset);
			}
		}
		return vRoot;
	}

	public class ViewHolderGameList {
		@Bind(R.id.rl_root)
		public RelativeLayout rlRoot;
		@Bind(R.id.iv_logo)
		public ImageView ivIcon;// 图片
		@Bind(R.id.tv_name)
		public TextView tvName;// 应用名称
		@Bind(R.id.tv_type)
		public TextView tvType;// 类型
		@Bind(R.id.tv_desc)
		public TextView tvDescription;// 描述
		@Bind(R.id.iv_download)
		public TextView ivDown;// 下载
		@Bind(R.id.pb_down)
		public ProgressBar pbDown;
		@Bind(R.id.tv_down_info)
		TextView tvDownInfo;
	}

}
