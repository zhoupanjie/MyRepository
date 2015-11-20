package com.cplatform.xhxw.ui.ui.main.saas.picShow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneItemExrta;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.util.*;

import java.io.Serializable;
import java.util.List;

/**
 * 社区图集查看查看
 * Created by cy-love on 13-12-31.
 */
public class SaasPicShowActivity extends BaseActivity implements DefaultView.OnTapListener {

    private static final String TAG = SaasPicShowActivity.class.getSimpleName();
    @InjectView(R.id.view_pager)
    ViewPager mVp;
    @InjectView(R.id.def_view)
    DefaultView mDefView;
    private List<CompanyZoneItemExrta> mData;
    private int mSelectIndex;
    private TextView mNum;

    public static Intent newIntent(Context context, List<CompanyZoneItemExrta> list, int index) {
        Intent intent = new Intent(context, SaasPicShowActivity.class);
        Bundle bun = new Bundle();
        bun.putInt("index", index);
        bun.putSerializable("objs", (Serializable)list);
        intent.putExtras(bun);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "SaasPicShowActivity";
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssas_pic_show);
        ButterKnife.inject(this);

        Bundle bun = getIntent().getExtras();
        if (bun == null) {
            LogUtil.w(TAG, "bundle is null!");
            return;
        }
        int index = bun.getInt("index", 0);
        mData = (List<CompanyZoneItemExrta>) bun.getSerializable("objs");
        initActionBar();

//        mDefView.setHidenOtherView(mVp);
//        mDefView.setListener(this);
//        mDefView.setStatus(DefaultView.Status.showData);
        initData(index);
    }

    private void initData(int index) {
    	mNum = (TextView) findViewById(R.id.nav_title);
        mVp.setAdapter(new PicPagerAdapter());
        mVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
            	if(i == mData.size()){
            		return;
            	}
                setSelectNum(i + 1);
                mSelectIndex = i;
            }


			@Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        if(index!=mData.size()){
        	mVp.setCurrentItem(index);
            setSelectNum(index + 1);
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
     *
     * @param posi
     * @return
     */
    private String getDataUrl(int posi) {
        if (mData != null) {
            return mData.get(posi).getFile();
        }
        return null;
    }

    /**
     * 选中的位置 显示如：4/5
     *
     * @param i
     */
    private void setSelectNum(int i) {
        mNum.setText(StringUtil.getString(i, "/", getDataCount()));
    }


    @Override
    public void onTapAction() {
//        loadData(mNewsId,0);
    }

    private class PicPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return getDataCount();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            SaasPicItemView view = new SaasPicItemView(container.getContext(), getDataUrl(position));
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
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