
package com.hy.superemsg.components;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.widget.Gallery;

public class GalleryEx extends Gallery {
    private boolean startAutoScroll;
    private final static int TIME_1_SECOND = 1000;
    private int stopScrollTime;

    public boolean isAutoScrolling() {
        return startAutoScroll;
    }

    private void refreshStopTime() {
        stopScrollTime = TIME_1_SECOND * 5;
    }

    public void stopAutoScroll() {
        startAutoScroll = false;
        if (this.getAdapter() == null || this.getAdapter().getCount() == 0)
            return;
        if (this.getAdapter().getCount() > 0) {
            this.setSelection(0);
        }
    }

    public void startAutoScroll() {
        this.startAutoScroll = true;
        refreshStopTime();
        if (this.getAdapter() == null || this.getAdapter().getCount() == 0)
            return;
        if (this.getAdapter().getCount() > 0) {
            this.setSelection(0);
        }
        new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... params) {

                while (startAutoScroll) {
                    try {
                        if (stopScrollTime > 0) {
                            stopScrollTime -= TIME_1_SECOND;
                        } else {
                            refreshStopTime();
                            publishProgress(null);
                        }
                        Thread.sleep(TIME_1_SECOND);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object result) {

            }

            @Override
            protected void onProgressUpdate(Object... values) {
                Gallery mGallery = GalleryEx.this;
                int pos = mGallery.getSelectedItemPosition();
                if (mGallery.getAdapter() != null
                        && mGallery.getAdapter().getCount() > 1) {
                    if (pos >= mGallery.getAdapter().getCount() - 1) {
                        mGallery.setSelection(0, true);
                    } else {
                        int keyCode;
                        keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
                        onKeyDown(keyCode, null);
                    }
                }
            }

        }.execute();

    }

    public GalleryEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        startAutoScroll = false;
    }

    @Override
    public void playSoundEffect(int soundConstant) {
        // XXX a more gracefull resolution needed.
        if (soundConstant == SoundEffectConstants.NAVIGATION_RIGHT
                || soundConstant == SoundEffectConstants.NAVIGATION_LEFT) {
            // do nothing.
        } else {
            super.playSoundEffect(soundConstant);
        }
    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        this.requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        int keyCode;
        if (isScrollingLeft(e1, e2)) {
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        if (actionListener != null) {
            if (this.getSelectedItemPosition() == this.getAdapter().getCount() - 1) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    actionListener.onFinalDrawableOver();
                    return true;
                }
            }
        }
        onKeyDown(keyCode, null);
        if (isAutoScrolling()) {
            this.refreshStopTime();
        }
        return true;
    }

    private ActionAfterLastDrawable actionListener;

    public void setActionAfterFinalFling(ActionAfterLastDrawable action) {
        actionListener = action;
    }

    public interface ActionAfterLastDrawable {
        public void onFinalDrawableOver();
    }
}
