package com.cplatform.xhxw.ui.ui.base.view;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class CheckDrawPrizeAlertDialog extends AlertDialog {

	private Context con;
	private String mShareUrl;
	private Button mCloseBtn;
	private Button mPlayBtn;
	private RelativeLayout mContentView;
	
	public CheckDrawPrizeAlertDialog(Context context, String shareurl) {
		super(context);
		this.con = context;
		this.mShareUrl = shareurl;
		initViews();
	}

	private void initViews() {
		LayoutInflater lif = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentView = (RelativeLayout) lif.inflate(R.layout.dialog_draw_prize, null);
		mCloseBtn = (Button) mContentView.findViewById(R.id.dialog_draw_prize_close);
		mPlayBtn = (Button) mContentView.findViewById(R.id.dialog_draw_prize_play_btn);
		mCloseBtn.setOnClickListener(mOnclick);
		mPlayBtn.setOnClickListener(mOnclick);
		setTitle(null);
		setCancelable(true);
	}
	
	@Override
	public void show() {
		super.show();
		setContentView(mContentView);
	}

	android.view.View.OnClickListener mOnclick = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.dialog_draw_prize_close) {
				dismiss();
			} else if(v.getId() == R.id.dialog_draw_prize_play_btn) {
				con.startActivity(WebViewActivity.getDrawPrizeIntent(con, mShareUrl, null));
				dismiss();
			}
		}
	};
}
