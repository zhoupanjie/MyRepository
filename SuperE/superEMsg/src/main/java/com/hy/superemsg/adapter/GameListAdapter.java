package com.hy.superemsg.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.GameCollectionsActivity;
import com.hy.superemsg.activity.GameDetailActivity;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.rsp.GameContentDetail;
import com.hy.superemsg.utils.ImageUtils;
import com.hy.superemsg.utils.ShareUtils;

public class GameListAdapter extends AbsCommonAdapter<GameContentDetail> {

	public GameListAdapter(Context ctx) {
		super(ctx, R.layout.item_game);
	}

	@Override
	public void getView(int position, View convertView,
			final GameContentDetail data) {
		TextView name = (TextView) convertView.findViewById(R.id.item_text);
		TextView cate = (TextView) convertView.findViewById(R.id.item_text1);
		TextView download = (TextView) convertView
				.findViewById(R.id.item_text2);
		ImageView iv = (ImageView) convertView.findViewById(R.id.item_image);
		ImageUtils.Image.loadImage(data.gameiconurl, iv);
		name.setText(data.gamename);
		cate.setText(data.gamestyle);
		download.setText("下载:" + data.gamedownload);
		convertView.setTag(data);
		convertView.findViewById(R.id.item_btn).setSelected(data.isCollected());
		convertView.findViewById(R.id.item_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						data.setCollected(!data.isCollected());
						if (data.isCollected()) {
							DBUtils.getInst().insert(
									DBHelper.TABLE_GAME_COLLECTION, null, data);
						} else {
							DBUtils.getInst().remove(
									DBHelper.TABLE_GAME_COLLECTION, data);
						}
						notifyDataSetChanged();

					}
				});
		convertView.findViewById(R.id.item_btn1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// share
						new AlertDialog.Builder(getContext())
								.setItems(R.array.menu_sms_share,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												if (which == 0) {
													// weixin
													ShareUtils.shareByWeixin(
															getContext(),
															data.gameintroduce);
												} else if (which == 1) {
													// weibo
													ShareUtils.shareByWeibo(
															getContext(), data);
												}

											}
										}).create().show();
					}
				});
		convertView.findViewById(R.id.item_btn2).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Activity act = (Activity) getContext();
						Uri uri = Uri.parse(data.gamefileurl);
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						act.startActivity(it);
					}
				});
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GameContentDetail game = (GameContentDetail) v.getTag();
				Activity act = (Activity) getContext();
				Intent i = new Intent(act, GameDetailActivity.class);
				i.putExtra(SuperEMsgApplication.EXTRA_GAME_DETAIL, game);
				act.startActivity(i);
			}
		});
	}

}
