package com.cplatform.xhxw.ui.util;

import com.cplatform.xhxw.ui.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MessageUtil {

    private Context mContext;
    private Toast mMesageToast;
    private TextView mMsg;

    public MessageUtil(Context context) {
        this.mContext = context;
    }

    /**
     * 显示Message消息
     *
     * @param msg
     */
    public void showMsg(String msg) {
        if (mMesageToast == null) {
            mMesageToast = new Toast(mContext);
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.framework_messege, null);
            mMesageToast.setView(view);
            mMesageToast.setDuration(Toast.LENGTH_SHORT);
            mMesageToast.setGravity(Gravity.CENTER, 0, 0);
            mMsg = (TextView) view.findViewById(R.id.framework_messege_tv);
        }
        mMsg.setText(msg);
        mMesageToast.show();
    }
}
