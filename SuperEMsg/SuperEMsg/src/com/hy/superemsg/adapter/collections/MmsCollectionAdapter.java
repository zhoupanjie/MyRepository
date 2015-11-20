
package com.hy.superemsg.adapter.collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import com.hy.superemsg.R;
import com.hy.superemsg.adapter.MmsListAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.rsp.MmsContentDetail;

public class MmsCollectionAdapter extends MmsListAdapter {

    public MmsCollectionAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public void getView(int position, View convertView,
            final MmsContentDetail data) {
        super.getView(position, convertView, data);
        View btn = convertView.findViewById(R.id.item_btn);
        btn.setSelected(true);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setMessage("是否取消收藏?")
                        .setPositiveButton("确定", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBUtils.getInst().remove(DBHelper.TABLE_MMS_COLLECTION, data);
                                removeData(data);
                                Activity act = (Activity) getContext();
                                act.setResult(Activity.RESULT_OK);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", null).create().show();
            }
        });
    }
}
