
package com.hy.superemsg.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.ContactsChooserActivity;
import com.hy.superemsg.activity.RingsCollectionsActivity;
import com.hy.superemsg.data.Contact;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.ReqRingAddRing;
import com.hy.superemsg.req.ReqRingCallOut;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RingContentDetail;
import com.hy.superemsg.rsp.RspRingAddRing;
import com.hy.superemsg.rsp.RspRingCallOut;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

public class RingListAdapter extends AbsCommonAdapter<RingContentDetail> {

    public RingListAdapter(Context ctx) {
        super(ctx, R.layout.item_ringlist);
    }

    @Override
    public void getView(final int position, View convertView,
            final RingContentDetail data) {
        TextView tv1 = (TextView) convertView.findViewById(R.id.item_text);
        TextView tv2 = (TextView) convertView.findViewById(R.id.item_text1);
        TextView tv3 = (TextView) convertView.findViewById(R.id.item_text2);
        TextView tv4 = (TextView) convertView.findViewById(R.id.item_text3);
        convertView.findViewById(R.id.item_btn).setSelected(data.isCollected());
        tv1.setText(data.ringname);
        tv2.setText("歌手:" + data.ringsinger);
        tv3.setText(data.ringusecount + "人已使用");
        tv4.setText("资费:" + data.ringprice + "元");
        convertView.findViewById(R.id.item_btn).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        data.setCollected(!data.isCollected());
                        if (data.isCollected()) {
                            DBUtils.getInst().insert(
                                    DBHelper.TABLE_RING_COLLECTION, null, data);
                        } else {
                            DBUtils.getInst().remove(
                                    DBHelper.TABLE_RING_COLLECTION, data);
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

                                            }
                                        }).create().show();
                    }
                });
        convertView.findViewById(R.id.item_btn2).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // download ring
                        HttpUtils.getInst().excuteTask(
                                new ReqRingAddRing(SuperEMsgApplication.account.phonenum,
                                        data.ringid, data.ringname), new AsynHttpCallback() {

                                    @Override
                                    public void onSuccess(BaseRspApi rsp) {
                                        RspRingAddRing ring = (RspRingAddRing) rsp;
                                        if (ring.resultcode == 0) {
                                            SuperEMsgApplication.toast(ring.resultdesc);
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        if (error != null) {
                                            SuperEMsgApplication.toast(error);
                                        } else {
                                            SuperEMsgApplication.toast("设为铃音失败");
                                        }
                                    }
                                });
                    }
                });
        convertView.findViewById(R.id.item_btn3).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // send ring
                        Activity act = (Activity) getContext();
                        Intent i = new Intent(act,
                                ContactsChooserActivity.class);
                        i.putExtra(SuperEMsgApplication.EXTRA_SEND_CONTENT,
                                data);
                        act.startActivity(i);
                    }
                });
        convertView.findViewById(R.id.item_image).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final RingContentDetail ring = data;
                new AlertDialog.Builder(getContext())
                        .setMessage(
                                "您将收到来电进行铃音试听?").setPositiveButton("试听", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpUtils.getInst().excuteTask(
                                        new ReqRingCallOut(SuperEMsgApplication.account.phonenum,
                                                SuperEMsgApplication.account.phonenum,
                                                ring.ringid,
                                                ring.ringname), new AsynHttpCallback() {

                                            @Override
                                            public void onSuccess(BaseRspApi rsp) {
                                                RspRingCallOut callout = (RspRingCallOut) rsp;
                                                SuperEMsgApplication
                                                        .toast(callout.resultdesc);

                                            }

                                            @Override
                                            public void onError(String error) {
                                                SuperEMsgApplication
                                                        .toast(error);

                                            }
                                        });
                                dialog.dismiss();
                            }
                        }).setNegativeButton("拒绝", null).create().show();
            }
        });
    }
}
