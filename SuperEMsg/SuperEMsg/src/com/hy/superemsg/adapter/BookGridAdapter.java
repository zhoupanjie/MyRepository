
package com.hy.superemsg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.BookDetailActivity;
import com.hy.superemsg.rsp.AnimationContentDetail;
import com.hy.superemsg.utils.ImageUtils;

public class BookGridAdapter extends AbsCommonAdapter<AnimationContentDetail> {

    public BookGridAdapter(Context ctx) {
        super(ctx, R.layout.item_grid);
    }

    @Override
    public void getView(int position, View convertView, AnimationContentDetail data) {
        ImageView iv = (ImageView) convertView.findViewById(R.id.item_image);
        ImageUtils.Image.loadImage(data.amcoverpicurl, iv);
        TextView tv = (TextView) convertView.findViewById(R.id.item_text);
        tv.setText(data.amname);
        convertView.setTag(data);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Activity act = (Activity) getContext();
                Intent i = new Intent(act, BookDetailActivity.class);
                i.putExtra(SuperEMsgApplication.EXTRA_BOOK_DETAIL,
                        (Parcelable) v.getTag());
                act.startActivity(i);
            }
        });
    }
}
