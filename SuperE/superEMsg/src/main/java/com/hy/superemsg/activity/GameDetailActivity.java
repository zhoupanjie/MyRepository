package com.hy.superemsg.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.req.ReqGameDetailQuery;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.GameContentDetail;
import com.hy.superemsg.rsp.RspGameDetailQuery;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.utils.ImageUtils;

public class GameDetailActivity extends Activity {
	private GameContentDetail game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_detail);
		game = this.getIntent().getParcelableExtra(
				SuperEMsgApplication.EXTRA_GAME_DETAIL);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(game.gamename);
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		HttpUtils.getInst().excuteTask(new ReqGameDetailQuery(game.gameid),
				new AsynHttpCallback() {

					@Override
					public void onSuccess(BaseRspApi rsp) {
						RspGameDetailQuery detail = (RspGameDetailQuery) rsp;
						game = detail.gamedetail;
						setData();
					}

					@Override
					public void onError(String error) {
						SuperEMsgApplication.toast(error);
					}
				});
	}

	private int downloadCount;

	private void setData() {
		ImageView icon = (ImageView) this.findViewById(R.id.item_image);
		ImageUtils.Image.loadImage(game.gameiconurl, icon);
		TextView name = (TextView) this.findViewById(R.id.item_text);
		name.setText(game.gamename);
		TextView cate = (TextView) this.findViewById(R.id.item_text1);
		cate.setText(game.gamestyle);
		final TextView download = (TextView) this.findViewById(R.id.item_text2);
		downloadCount = game.gamedownload;
		download.setText("下载:" + downloadCount);
		TextView version = (TextView) this.findViewById(R.id.item_text3);
		version.setText("版本:" + game.gameversion);
		TextView size = (TextView) this.findViewById(R.id.item_text4);
		size.setText("大小:" + game.gamefilesize + "M");
		TextView desc = (TextView) this.findViewById(R.id.item_text5);
		desc.setText(game.gameintroduce);
		Gallery gallery = (Gallery) this.findViewById(R.id.game_screenshot);
		ImageAdapter adapter = new ImageAdapter(GameDetailActivity.this);
		adapter.setDatas(game.gamescreenshotslist);
		gallery.setAdapter(adapter);
		findViewById(R.id.item_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						downloadCount+=1;
						download.setText("下载:" + downloadCount);
						Uri uri = Uri.parse(game.gamefileurl);
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					}
				});

	}

	private class ImageAdapter extends AbsCommonAdapter<String> {

		public ImageAdapter(Context ctx) {
			super(ctx, R.layout.item_screen_shots);
		}

		@Override
		public void getView(int position, View convertView, String data) {
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.item_image);
			ImageUtils.Image.loadImage(data, iv);
		}

	}
}
