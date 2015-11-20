package com.cplatform.xhxw.ui.plugin.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.FriendsActionSheet;
import com.cplatform.xhxw.ui.ui.picShow.PicItemView;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 图集查看
 * Created by cy-love on 13-12-31.
 */
public class PicShowActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PicShowActivity.class.getSimpleName();
    public static final String KEY_INDEX = "index";
    public static final String KEY_DATA = "data";
    public static final String KEY_IS_SHOW_OPTION = "isShowOption";
    private List<String> mData;
    private int mSelectIndex;
    private TextView mNum;
    private PicPagerAdapter mAdapter;
    ViewPager vp;

    /**
     * 预览图片
     *
     * @param context
     * @param url          地址
     * @param isShowOption true显示删除按钮
     * @return
     */
    public static Intent newIntent(Context context, String url, boolean isShowOption) {
        List<String> list = new ArrayList<String>();
        list.add(url);
        return newIntent(context, list, 0, isShowOption);
    }

    /**
     * 预览图片
     *
     * @param context
     * @param data         图片集合
     * @param index        显示索引
     * @param isShowOption true显示删除按钮
     * @return
     */
    public static Intent newIntent(Context context, List<String> data, int index, boolean isShowOption) {
        Intent intent = new Intent(context, PicShowActivity.class);
        Bundle bundle = new Bundle();
        String str = new Gson().toJson(data);
        bundle.putString(KEY_DATA, str);
        bundle.putInt(KEY_INDEX, index);
        bundle.putBoolean(KEY_IS_SHOW_OPTION, isShowOption);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_gallery_pic_show);
        Bundle bun = getIntent().getExtras();
        if (bun == null) {
            LogUtil.w("bundle is null!");
            return;
        }
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_del).setOnClickListener(this);
        mNum = (TextView) findViewById(R.id.nav_title);
        int index = bun.getInt(KEY_INDEX, 0);
        mData = new Gson().fromJson(bun.getString("data"), new TypeToken<List<String>>() {
        }.getType());

        boolean isShowOption = bun.getBoolean(KEY_IS_SHOW_OPTION, false);
        if (!isShowOption) {
            findViewById(R.id.btn_del).setVisibility(View.INVISIBLE);
        }
        initData(index);
        setSelectNum(index);
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    private void initData(int index) {
        vp = (ViewPager) findViewById(R.id.view_pager);
        mAdapter = new PicPagerAdapter();
        vp.setAdapter(mAdapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == mData.size()) {
                    return;
                }
                setSelectNum(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        if (index != mData.size()) {
            vp.setCurrentItem(index);
            setSelectNum(index);
        }
    }

    /**
     * 获得总数
     *
     * @return
     */
    private int getDataCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    /**
     * 获得url
     */
    private String getDataUrl(int posi) {
        if (mData != null) {
            String url = mData.get(posi);
            if (!TextUtils.isEmpty(url) && !url.startsWith("http://")) {
                url = "file://" + url;
            }
            return url;
        }
        return null;
    }

    /**
     * 更新标题
     *
     * @param i
     */
    private void setSelectNum(int i) {
        mSelectIndex = i;
        mNum.setText(StringUtil.getString(i + 1, "/", getDataCount()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_del:
            {
                FriendsActionSheet actionSheet = FriendsActionSheet.getInstance(this);
                actionSheet.setItems(new String[]{getString(R.string.delete),
                                getString(R.string.cancel)}, new int[]{R.drawable.btn_del, R.drawable.btn_cancle});
                actionSheet.setListener(new FriendsActionSheet.IListener() {

                    @Override
                    public void onItemClicked(int index, String item) {
                        switch (index) {
                            case 0: // 删除
                            {
                                if (mData.size() <= 1) {
                                    mData.clear();
                                    finish();
                                    return;
                                }
                                mData.remove(mSelectIndex);
                                mAdapter.notifyDataSetChanged();
                                if (mSelectIndex >= mData.size()) {
                                    mSelectIndex--;
                                }
                                setSelectNum(mSelectIndex);
                            }
                                break;
                        }
                    }
                });
                actionSheet.show();
            }
                break;
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("result", (Serializable) mData);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private class PicPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return getDataCount();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            PicItemView view = new PicItemView(container.getContext(), getDataUrl(position), null);
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;

        }

        @Override
        public int getItemPosition(Object object) {
            if (mData != null && object instanceof PicItemView) {
                PicItemView view = (PicItemView)object;
                if (!TextUtils.isEmpty(view.getUrl()) && mData.contains(view.getUrl())) {
                    return POSITION_UNCHANGED;
                }
            }
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}