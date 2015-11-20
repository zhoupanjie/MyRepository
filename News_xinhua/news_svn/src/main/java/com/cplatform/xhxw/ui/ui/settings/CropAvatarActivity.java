package com.cplatform.xhxw.ui.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.cylib.imageCrop.CropImage;
import com.cylib.imageCrop.CropImageView;
import com.cylib.imageCrop.Messages;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.LogUtil;


/**
 * 头像裁剪
 * Created by cy-love on 14-1-18.
 */
public class CropAvatarActivity extends BaseActivity {
    private static final String TAG = CropAvatarActivity.class.getSimpleName();

    @InjectView(R.id.gl_modify_avatar_image)
    CropImageView mImageView;
    private Bitmap mBitmap;
    private CropImage mCrop;
    private String mPath = "CropImageActivity";

    private ProgressBar mProgressBar;

    public static Intent getIntent(Context context, String path) {
        Intent intent = new Intent(context, CropAvatarActivity.class);
        Bundle bun = new Bundle();
        bun.putString("path", path);
        bun.putInt("ptWidth", 1); // 宽比例
        bun.putInt("ptHeight", 1); // 高比例
        intent.putExtras(bun);
        return intent;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Messages.SHOW_PROGRESS: // 显示等待
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case Messages.REMOVE_PROGRESS: // 隐藏等待
                    mHandler.removeMessages(Messages.SHOW_PROGRESS);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    break;
            }

        }
    };

    @Override
    protected String getScreenName() {
        return "CropAvatarActivity";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        ButterKnife.inject(this);
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();

        }
        mBitmap = null;
    }

    private void init() {
        Bundle bun = getIntent().getExtras();
        if (bun == null) {
            LogUtil.e(TAG, "参数未传递！");
            this.finish();
            return;
        }
        mPath = bun.getString("path");
        int ptWidth = bun.getInt("ptWidth", 1);
        int ptHeight = bun.getInt("ptHeight", 1);
        LogUtil.i(TAG, "得到的图片的路径是 = " + mPath);
        try {
            mBitmap = createBitmap(mPath, Constants.screenWidth, Constants.screenHeight);
            if (mBitmap == null) {
                showToast("没有找到图片");
                finish();
            } else {
                resetImageView(mBitmap, ptWidth, ptHeight);
            }
        } catch (Exception e) {
            showToast("没有找到图片");
            LogUtil.e(TAG, e);
            finish();
        }
        addProgressbar();
    }

    /**
     * 设置图片
     * @param b 位图
     * @param ptWidth 宽比例
     * @param ptHeight 高比例
     */
    private void resetImageView(Bitmap b, int ptWidth, int ptHeight) {
        mImageView.clear();
        mImageView.setImageBitmap(b);
        mImageView.setImageBitmapResetBase(b, true);
        mCrop = new CropImage(this, mImageView, mHandler, ptWidth, ptHeight);
        mCrop.crop(b);
    }

    /**
     * 取消事件
     */
    @OnClick(R.id.gl_modify_avatar_cancel)
    public void onCancelAction() {
        mCrop.cropCancel();
        finish();
    }

    /**
     * 保存事件
     */
    @OnClick(R.id.gl_modify_avatar_save)
    public void onSaveAction() {
        String path = mCrop.saveToLocal(mCrop.cropAndSave(), Constants.Directorys.CACHE+"crop.jpg");
        LogUtil.i(TAG, "截取后图片的路径是 = " + path);
        Intent intent = new Intent();
        intent.putExtra("path", path);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 图像右旋转事件
     */
    @OnClick(R.id.gl_modify_avatar_rotate_right)
    public void onRotateRightAction() {
        mCrop.startRotate(90.f);
    }

    protected void addProgressbar() {
        mProgressBar = new ProgressBar(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addContentView(mProgressBar, params);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 创建位图
     * @param path 保存路径
     * @param w 位图宽
     * @param h 位图高
     * @return
     */
    private Bitmap createBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            return null;
        }
    }

}