package com.cplatform.xhxw.ui.ui.main.saas;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * S信适配器
 * Created by cy-love on 14-8-7.
 */
public class SMessageAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public SMessageAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.view_s_message_item, null);
        ViewHelper helper = new ViewHelper(view);
        view.setTag(helper);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int iPhoto = cursor.getColumnIndex(SMessageDao.LOGO);
        int iName = cursor.getColumnIndex(SMessageDao.NAME);
        int iNickName = cursor.getColumnIndex(SMessageDao.NICK_NAME);
        int iComment = cursor.getColumnIndex(SMessageDao.COMMENT);
        int iLastMsg = cursor.getColumnIndex(SMessageDao.LAST_MSG);
        int iUnreadCount = cursor.getColumnIndex(SMessageDao.UNREAD_COUNT);
        int iCtime = cursor.getColumnIndex(SMessageDao.CTIME);
        String photo = cursor.getString(iPhoto);
        String name = cursor.getString(iName);
        String nickName = cursor.getString(iNickName);
        String comment = cursor.getString(iComment);
        String lastMsg = cursor.getString(iLastMsg);
        int unreadCount = cursor.getInt(iUnreadCount);
        long updateTime = cursor.getLong(iCtime);
        ViewHelper helper = (ViewHelper) view.getTag();
        String nameStr = SelectNameUtil.getName(comment, nickName, name);
        helper.name.setText(nameStr);
        if (!TextUtils.isEmpty(lastMsg)) {
            helper.lastMsg.setText(XWExpressionUtil.generateSpanComment(
                    context, lastMsg,
                    (int) helper.lastMsg.getTextSize()));
        } else {
            helper.lastMsg.setText(null);
        }

        helper.time.setText(DateUtil.getXHAPPDetailFormmatString(updateTime * 1000));
        if (unreadCount == 0) {
            helper.num.setVisibility(View.GONE);
        } else {
            helper.num.setVisibility(View.VISIBLE);
            String count = (unreadCount > 99) ? String.valueOf("99+") : String.valueOf(unreadCount);
            helper.num.setText(count);
        }
        ImageLoader.getInstance().displayImage(photo, helper.photo, DisplayImageOptionsUtil.avatarSaasImagesOptions);

    }

    static class ViewHelper {
        @InjectView(R.id.iv_logo)
        ImageView photo;
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.tv_time)
        TextView time;
        @InjectView(R.id.tv_last_msg)
        TextView lastMsg;
        @InjectView(R.id.tv_num)
        TextView num;

        public ViewHelper(View v) {
            ButterKnife.inject(this, v);
        }
    }
}