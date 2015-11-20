
package com.hy.superemsg.viewpager;

import com.hy.superemsg.viewpager.fragments.KeyEventListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

public abstract class AbsFragment extends Fragment implements KeyEventListener {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        excuteTask();
    }

    /**
     * place to inflate your children
     */
    protected abstract void initUI();

    /**
     * place to get data async
     */
    protected abstract void excuteTask();

    /**
     * called when user navigates to this item
     */
    protected abstract void resetUI();

    @Override
    public boolean OnKeyEvent(int keyCode, KeyEvent event) {
        return false;
    }

}
