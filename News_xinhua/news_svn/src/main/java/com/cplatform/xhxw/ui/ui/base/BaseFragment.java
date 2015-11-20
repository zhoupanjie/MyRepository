package com.cplatform.xhxw.ui.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.cplatform.xhxw.ui.util.MessageUtil;
import com.cplatform.xhxw.ui.R;

public abstract class BaseFragment extends Fragment {

    private MessageUtil mMsg;
    private Toast mToast;
    protected BaseActivity mAct;
    protected boolean isDestroyView;
    protected boolean isButterKnife = true;

    @Override
    public void onAttach(Activity activity) {
        if (null != activity && activity instanceof BaseActivity)
            mAct = (BaseActivity)activity;
        super.onAttach(activity);
    }

    /**
     * 显示Message信息
     */
    protected void showMessage(String msg) {
        if (isDetached()) return;
        if (null == mMsg) mMsg = new MessageUtil(getActivity());
        mMsg.showMsg(msg);
    }

    /**
     * 显示toast
     */
    protected void showToast(String msg) {
        if (isDetached()) return;
        if (null == mToast) mToast = Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT);
        mToast.setText(msg);
        mToast.show();
    }

    /**
     * 显示toast
     */
    protected void showToast(int msg) {
       showToast(getString(msg));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyView = true;
        if (isButterKnife) {
            ButterKnife.reset(this);
        }
    }

    public void initActionBar(View v) {
        View back = v.findViewById(R.id.btn_back);
        if (null != back) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
//                    boolean isRun = ActivityUtil.isRunning(mAct, HomeActivity.class);
//                    if (!isRun) mAct.startActivity(HomeActivity.class);
                    mAct.finish();
                }
            });
        }
    }

    public void startActivity(Intent intent) {
        mAct.startActivity(intent);
    }

    public void startNoAnimActivit(Intent intent) {
        mAct.startNoAnimActivity(intent);
    }

    public void startNoAnimActivityForResult(Intent intent, int requestCode) {
        mAct.startNoAnimActivityForResult(intent, requestCode);
    }

    public int getResCoclor(int id) {
        return getResources().getColor(id);
    }

    public void showLoadingView() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoadingView();
        }
    }

    public void hideLoadingView() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoadingView();
        }
    }
}
