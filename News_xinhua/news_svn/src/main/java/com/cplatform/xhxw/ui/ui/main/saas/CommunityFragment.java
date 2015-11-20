package com.cplatform.xhxw.ui.ui.main.saas;

import android.app.Activity;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.saas.CommentSubData;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneList;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.view.FriendsActionSheet;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.*;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.AppCoomentUtil;

import java.util.ArrayList;

/**
 * 社区
 */
public class CommunityFragment extends BaseFragment {

    private ImageView mCameraBtn;

    private ImageView ivBottomLine1, ivBottomLine2;
    private TextView mNum1, mNum2;
    private int currIndex;
    private TextView tvTabfriends_comments, tvTabactivities_recommended;
    private ViewPager mPager;
    private Uri mPhotoUri;
    private CompanyCircleFragment mF1;
    private CompanyFriendsFragment mF2;
    private OnCommunityCommentaryListener mComLis;
    private MyReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community,
                container, false);
        mCameraBtn = (ImageView) view.findViewById(R.id.company_circle_camera);
        mNum1 = (TextView) view.findViewById(R.id.tv_num_1);
        mNum2 = (TextView) view.findViewById(R.id.tv_num_2);
        ivBottomLine1 = (ImageView) view.findViewById(R.id.iv_bottom_line_1);
        ivBottomLine2 = (ImageView) view.findViewById(R.id.iv_bottom_line_2);
        tvTabfriends_comments = (TextView) view.findViewById(R.id.friends_comments_text);
        tvTabfriends_comments.setText(Constants.userInfo.getEnterprise().getAliases().getC_community_alias());
        tvTabactivities_recommended = (TextView) view.findViewById(R.id.activities_recommended_text);
        tvTabactivities_recommended.setText(Constants.userInfo.getEnterprise().getAliases().getF_community_alias());
        mPager = (ViewPager) view.findViewById(R.id.vPager);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCameraBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CircleCreateType type = currIndex == 0 ? CircleCreateType.company : CircleCreateType.friend;
                mAct.startActivityForResult(SendTextActivity.newIntent(getActivity(), type), EnterpriseMainFragment.CREATE);
                return true;
            }
        });
        mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoActionSheet(getActivity());
            }
        });
        tvTabfriends_comments.setOnClickListener(new MyOnClickListener(0));
        tvTabactivities_recommended.setOnClickListener(new MyOnClickListener(1));
        //初始化ViewPager
        InitViewPager();
    }

    /**
     * 初始化ViewPager，创建2个Fragment，为ViewPager设置适配器，为ViewPager设置监听器
     */
    private void InitViewPager() {

        ArrayList fragmentsList = new ArrayList<BaseFragment>();
        //不同的Fragment传入的是不同的值，这个值用来在TestFragment类中的onCreatView()方法中根据这个
        //传进来的int值返回不同的View
        //		 Fragment activityfragment = TestFragment.newInstance(CASE_FRIEND);
        //	     Fragment groupFragment = TestFragment.newInstance(CASE_MOVE);
        mF1 = new CompanyCircleFragment();
        mF1.setCommunityCommentaryListener(mComLis);
        fragmentsList.add(mF1);
        mF2 = new CompanyFriendsFragment();
        mF2.setCommunityCommentaryListener(mComLis);
        fragmentsList.add(mF2);
        //设置ViewPager的适配器（自定义的继承自FragmentPagerAdapter的adapter）
        //参数分别是FragmentManager和装载着Fragment的容器
        mPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragmentsList));
        //设置ViewPager的页面切换监听事件
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        //设置默认是第一页
        mPager.setCurrentItem(0);
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<BaseFragment> fragmentsList;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    }

    //这个是ViewPager的页面切换事件监听器
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        ivBottomLine1.setVisibility(View.VISIBLE);
                        ivBottomLine2.setVisibility(View.INVISIBLE);
                        tvTabactivities_recommended.setTextColor(getResources().getColor(R.color.community_unselect_text_color));
                    }
                    tvTabfriends_comments.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 1:
                    if (currIndex == 0) {
                        ivBottomLine1.setVisibility(View.INVISIBLE);
                        ivBottomLine2.setVisibility(View.VISIBLE);
                        tvTabfriends_comments.setTextColor(getResources().getColor(R.color.community_unselect_text_color));
                    }
                    tvTabactivities_recommended.setTextColor(getResources().getColor(R.color.black));
                    break;
            }
            currIndex = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    /**
     * 拍照或者从相册获取图片
     */
    private void showPhotoActionSheet(final Activity activity) {
        FriendsActionSheet actionSheet = FriendsActionSheet.getInstance(activity);
        actionSheet
                .setItems(new String[]{getString(R.string.company_circle_camera),
                        getString(R.string.company_circle_phone),
                        getString(R.string.cancel)});
        actionSheet.setListener(new FriendsActionSheet.IListener() {

            @Override
            public void onItemClicked(int index, String item) {
                switch (index) {
                    case 0: // 拍照
                        takePhoto();
                        break;
                    case 1: // 相册
                        int requestCode = currIndex == 0 ? EnterpriseMainFragment.REQUEST_CODE_COMPANT_ALBUMGROUP : EnterpriseMainFragment.REQUEST_CODE_FRIEND_ALBUMGROUP;
                        mAct.startActivityForResult(CompanyCircleAlbumGroupActivity.newIntent(getActivity(), null), requestCode);
                        break;
                }
            }
        });
        actionSheet.show();
    }

    /** 拍照 */
    private void takePhoto() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ContentResolver contentResolver = getActivity().getContentResolver();
            mPhotoUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new ContentValues());
            int requestCode = currIndex == 0 ? EnterpriseMainFragment.REQUEST_CODE_COMPANT_CAMERA : EnterpriseMainFragment.REQUEST_CODE_FRIEND_CAMERA;
            AppCoomentUtil.takePhoto(mAct, mPhotoUri, requestCode);
        } else {
            showToast("请安装sd卡");
        }
    }

    public Uri getPhotoUri() {
        return mPhotoUri;
    }

    /**
     * 评论回复
     */
    public void setCompanyCircleCommentary(CommentSubData resutl) {
        if (resutl.isCompanyCircle()) {
            if (mF1 != null) {
                mF1.setCompanyCircleCommentary(resutl);
            }
        } else {
            if (mF2 != null) {
                mF2.setCompanyCircleCommentary(resutl);
            }
        }

    }

    public void setCommunityCommentaryListener(OnCommunityCommentaryListener mComLis) {
        this.mComLis = mComLis;
    }

    /**
     * 社区发布内容数据返回
     */
    public void sendCompanyZoneListItem(boolean isFriends, CompanyZoneList item) {
        if (!isFriends) {
            if (mF1 != null) {
                mF1.sendCompanyZoneListItem(item);
            }
            mPager.setCurrentItem(0);
        } else {
            if (mF2 != null) {
                mF2.sendCompanyZoneListItem(item);
            }
            mPager.setCurrentItem(1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNewMsgCount();
        String fAlias = "朋友圈";
        String cAlias = "公司圈";
        if (Constants.hasLogin() && Constants.userInfo.getEnterprise() != null && Constants.userInfo.getEnterprise().getAliases() != null) {
            // 朋友圈子
            fAlias = Constants.userInfo.getEnterprise().getAliases().getF_community_alias();
            cAlias = Constants.userInfo.getEnterprise().getAliases().getC_community_alias();
        }
        tvTabactivities_recommended.setText(fAlias);
        tvTabfriends_comments.setText(cAlias);
        mReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, new IntentFilter(Actions.ACTION_MSG_NEW_UPDATE));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        mReceiver = null;
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Actions.ACTION_MSG_NEW_UPDATE.equals(intent.getAction())) {
                updateNewMsgCount();
            }
        }
    }

    private void updateNewMsgCount() {
        int msgCircle = App.getPreferenceManager().getMsgNewCompanyCircleCount();
        if (msgCircle > 0) {
            mNum1.setVisibility(View.VISIBLE);
            msgCircle = msgCircle > 99 ? 99 : msgCircle;
            mNum1.setText(String.valueOf(msgCircle));
        } else {
            mNum1.setVisibility(View.GONE);
        }

        int msgFriends = App.getPreferenceManager().getMsgNewFriendsCircleCount();
        if (msgFriends > 0) {
            mNum2.setVisibility(View.VISIBLE);
            msgFriends = msgFriends > 99 ? 99 : msgFriends;
            mNum2.setText(String.valueOf(msgFriends));
        } else {
            mNum2.setVisibility(View.GONE);
        }
    }
}
