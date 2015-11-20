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
import butterknife.Bind;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.SMessageChatDao;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * S信适配器
 * Created by cy-love on 14-8-7.
 */
public class SMessageChatAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_LEFT = 0;
    private static final int VIEW_TYPE_RIGHT = 1;
    private LayoutInflater mInflater;
    private Set<String> mSending;

    private HashMap<String, String> mLogo;
    private View.OnClickListener mBodyOnClickLis;
    private View.OnLongClickListener mBodyOnLongClickLis;
    private View.OnClickListener mAvatarOnClickLis;

    public SMessageChatAdapter(Context context, Cursor c, int flags, View.OnClickListener listener, View.OnLongClickListener bodyOption, View.OnClickListener avatarOnClickLis) {
        super(context, c, flags);
        mBodyOnClickLis = listener;
        mBodyOnLongClickLis = bodyOption;
        mAvatarOnClickLis = avatarOnClickLis;
        mLogo = new HashMap<String, String>();
        mSending = new HashSet<String>();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void putSending(String id) {
        mSending.add(id);
    }

    public void removeSending(String id) {
        mSending.remove(id);
    }

    public boolean checkSending(String id) {
        return mSending.contains(id);
    }

    /**
     * 添加头像
     *
     * @param uid  用户id
     * @param logo 头像
     */
    public void putLogo(String uid, String logo) {
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(logo)) {
            return;
        }
        mLogo.put(uid, logo);
    }

    /**
     * 清除头像
     *
     * @param uid 用户id
     */
    public void removeLogo(String uid) {
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        mLogo.remove(uid);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        int iIsSelf = cursor.getColumnIndex(SMessageChatDao.ISSELF);
        String isSelf = cursor.getString(iIsSelf);
        if (!("1".equals(isSelf))) {
            View view = mInflater.inflate(R.layout.view_s_message_chat_item_left, null);
            ViewHelperLeft helper = new ViewHelperLeft(view);
            helper.body.setOnLongClickListener(mBodyOnLongClickLis);
            helper.avatar.setOnClickListener(mAvatarOnClickLis);
            view.setTag(helper);
            return view;
        }
        View view = mInflater.inflate(R.layout.view_s_message_chat_item_right, null);
        ViewHelperRight helper = new ViewHelperRight(view);
        helper.body.setOnClickListener(mBodyOnClickLis);
        helper.body.setOnLongClickListener(mBodyOnLongClickLis);
        view.setTag(helper);
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (mCursor.moveToPosition(position)) {
            int iIsSelf = mCursor.getColumnIndex(SMessageChatDao.ISSELF);
            String isSelf = mCursor.getString(iIsSelf);
            if ("1".equals(isSelf)) {
                return VIEW_TYPE_RIGHT;
            }
        }
        return VIEW_TYPE_LEFT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int iUserId = cursor.getColumnIndex(SMessageChatDao.USER_ID);
        int iInfoId = cursor.getColumnIndex(SMessageChatDao.INFO_ID);
        int iMsg = cursor.getColumnIndex(SMessageChatDao.MSG);
        int iMsgType = cursor.getColumnIndex(SMessageChatDao.MSG_TYPE);
        int iStatus = cursor.getColumnIndex(SMessageChatDao.STATUS);
        int iTime = cursor.getColumnIndex(SMessageChatDao.TIME);
        String msg = cursor.getString(iMsg);
        String userId = cursor.getString(iUserId);
        int msgType = cursor.getInt(iMsgType);
        int status = cursor.getInt(iStatus);
        long time = cursor.getLong(iTime);
        String infoId = cursor.getString(iInfoId);
        int posi = cursor.getPosition();
        int timeVis = getTimeVisibility(cursor, time) ? View.VISIBLE : View.GONE;
        Object obj = view.getTag();
        if (obj instanceof ViewHelperLeft) { // 左侧
            ViewHelperLeft helper = (ViewHelperLeft) obj;
            helper.msg.setText(XWExpressionUtil.generateSpanComment(
                    context, msg,
                    (int) helper.msg.getTextSize()));
            helper.body.setTag(posi);
            helper.pic.setVisibility(View.GONE);
            helper.avatar.setTag(posi);
            helper.date.setText(DateUtil.getSMessageChatFormmatString(time));
            helper.date.setVisibility(timeVis);
            ImageLoader.getInstance().displayImage(mLogo.get(userId), helper.avatar, DisplayImageOptionsUtil.avatarSaasImagesOptions);
        } else { // 右侧布局
            ViewHelperRight helper = (ViewHelperRight) obj;
            helper.body.setTag(posi);
            helper.msg.setText(XWExpressionUtil.generateSpanComment(
                    context, msg,
                    (int) helper.msg.getTextSize()));
            setMsgStatus(helper.progressBar, helper.error, infoId, status);
            helper.pic.setVisibility(View.GONE);

            helper.time.setVisibility(timeVis);
            helper.time.setText(DateUtil.getSMessageChatFormmatString(time));
            String avatarUrl = Constants.userInfo != null ? Constants.userInfo.getLogo() : null;
            ImageLoader.getInstance().displayImage(avatarUrl, helper.avatar, DisplayImageOptionsUtil.avatarSaasImagesOptions);
        }
    }

    /**
     * 获得时间控件是否显示
     */
    private boolean getTimeVisibility(Cursor cursor, long time) {
        int position = cursor.getPosition();
        if (position == 0) {
            return true;
        }

        if (cursor.moveToPosition(position - 1)) {
            int iTime = cursor.getColumnIndex(SMessageChatDao.TIME);
            long oldTime = cursor.getLong(iTime);
            if ((time - oldTime) < 60*5) { // 小于五分钟不显示时间
                return false;
            }
        }
        return true;
    }

    /**
     * 设置消息状态
     *
     * @param imageView
     * @param status
     */
    private void setMsgStatus(View progressBar, ImageView imageView, String id, int status) {
        switch (status) {
            case SMessageChatDao.MSG_STATUS_ERROR:
                if (mSending.contains(id)) {
                    progressBar.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
                break;
            default:
                imageView.setVisibility(View.GONE);
                break;
        }
    }

    static class ViewHelperLeft {
        @Bind(R.id.formclient_row_date)
        TextView date;
        @Bind(R.id.avatar)
        ImageView avatar;
        @Bind(R.id.formclient_row_msg)
        TextView msg;
        @Bind(R.id.iv_pic)
        ImageView pic;
        @Bind(R.id.body_layout)
        View body;

        public ViewHelperLeft(View v) {
            ButterKnife.bind(this, v);
        }
    }

    static class ViewHelperRight {
        @Bind(R.id.formclient_row_date)
        TextView time;
        @Bind(R.id.avatar)
        ImageView avatar;
        @Bind(R.id.formclient_row_msg)
        TextView msg;
        @Bind(R.id.iv_pic)
        ImageView pic;
        @Bind(R.id.send_error_icon)
        ImageView error;
        @Bind(R.id.progressBar)
        View progressBar;
        @Bind(R.id.body_layout)
        View body;

        public ViewHelperRight(View v) {
            ButterKnife.bind(this, v);
        }
    }
}