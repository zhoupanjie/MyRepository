package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.CompanyZoneCommentData;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneItemUserInfo;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneList;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.base.widget.CircleImageView;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.main.saas.picShow.SaasPicShowActivity;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友圈适配器
 */
public class CompanyFriendsAdapter extends BaseAdapter<CompanyZoneList> {

    /**
     * 一张图片时，宽高比例2:1
     */
    private float bigScale = 0.5f;

    /**
     * 多张图片时，宽高比例1:1
     */
    private float smallScale = 1f;
    private OnCommentClickListener mLis;
    private View.OnClickListener mOptionLis;
    private View.OnClickListener mUserInfoLis;

    public CompanyFriendsAdapter(Context context, OnCommentClickListener lis, View.OnClickListener optionLis) {
        super(context);
        mLis = lis;
        mOptionLis = optionLis;
    }

    /**
     * 用户详情回调点击事件(触控控件:头像\姓名)
     * @param lis 返回用户id
     */
    public void setUserInfoListener(View.OnClickListener lis) {
        this.mUserInfoLis = lis;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        CompanyZoneList item = getItem(position);
        ViewHodler hodler;
        if (view == null) {
            hodler = new ViewHodler();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.fragment_company_zone_item, null);
            hodler.comments = (LinearLayout) view
                    .findViewById(R.id.ly_comments);
            hodler.userImage = (CircleImageView) view
                    .findViewById(R.id.event_meeting_record_item_image);
            hodler.userName = (TextView) view
                    .findViewById(R.id.event_meeting_record_item_name);
            hodler.content = (TextView) view
                    .findViewById(R.id.event_meeting_record_item_content);
            hodler.time = (TextView) view
                    .findViewById(R.id.event_meeting_record_item_time);
            hodler.imageFirst = (ImageView) view
                    .findViewById(R.id.event_meeting_record_item_first);
            hodler.imageSecond = (ImageView) view
                    .findViewById(R.id.event_meeting_record_item_second);
            hodler.imageThird = (ImageView) view
                    .findViewById(R.id.event_meeting_record_item_third);
            hodler.imageforth = (ImageView) view
                    .findViewById(R.id.event_meeting_record_item_forth);
            hodler.imageFive = (ImageView) view
                    .findViewById(R.id.event_meeting_record_item_five);
            hodler.imageSix = (ImageView) view
                    .findViewById(R.id.event_meeting_record_item_six);
            hodler.imageSevent = (ImageView) view
                    .findViewById(R.id.event_meeting_record_item_seven);
            hodler.imageEight = (ImageView) view
                    .findViewById(R.id.event_meeting_record_item_eight);
            hodler.imageNine = (ImageView) view
                    .findViewById(R.id.event_meeting_record_item_nine);
            hodler.mNumZan = (TextView) view
                    .findViewById(R.id.tv_num_zan);
            hodler.option = (ImageView) view
                    .findViewById(R.id.btn_commentary_option);
            hodler.option.setOnClickListener(mOptionLis);
            hodler.lyLike = view.findViewById(R.id.ly_like);
            hodler.userImage.setOnClickListener(mUserInfoLis);
            hodler.userName.setOnClickListener(mUserInfoLis);
            hodler.lyLikeLine = view.findViewById(R.id.ly_like_line);
            hodler.lyCommentsArrow = view.findViewById(R.id.ly_comments_arrow);
            hodler.lyCommentsContent = view.findViewById(R.id.ly_comments_content);
            hodler.mCommentViews = new ArrayList<CommentTextView>();
            view.setTag(hodler);
        } else {
            hodler = (ViewHodler) view.getTag();
        }
        hodler.option.setTag(item);

        CompanyZoneItemUserInfo info = item.getUserinfo();
        if (info != null) {
            ImageLoader.getInstance().displayImage(info.getLogo(), hodler.userImage, DisplayImageOptionsUtil.avatarSaasImagesOptions);
            hodler.userName.setText(SelectNameUtil.getName(info.getComment(), info.getNickname(), info.getName()));
        } else {
            hodler.userImage.setImageResource(R.drawable.s_msg_user_def);
            hodler.userName.setText("");
        }
        hodler.userName.setTag(info.getUserid());
        hodler.userImage.setTag(info.getUserid());
        hodler.content.setText(XWExpressionUtil.generateSpanComment(
                mContext, item.getContent(),
                (int) hodler.content.getTextSize()));
        hodler.time.setText(item.getFtime());

        /** 动态设置图片布局 */
        setImageLayout(view, item);

        if (item.getParisadata() != null && !ListUtil.isEmpty(item.getParisadata().getList())) {
            hodler.lyLike.setVisibility(View.VISIBLE);
            hodler.mNumZan.setText(item.getParisadata().getList().size()+"人觉得很赞");
        } else {
            hodler.lyLike.setVisibility(View.GONE);
        }

        List<CommentTextView> views = hodler.mCommentViews;
        hodler.comments.removeAllViews();
        int size = views.size();
        if (!ListUtil.isEmpty(item.getCommentdata())) {
            int liuyanSize = item.getCommentdata().size();
            for (int i = 0; i < liuyanSize; i++) {
                CompanyZoneCommentData ly = item.getCommentdata().get(i);
                CommentTextView v = getCommentTextView(i, size, views);
                String userId = null, userName = null, reUserName = null, reUserId = null;
                if (ly.getSenduser() != null) {
                    CompanyZoneItemUserInfo userInfo = ly.getSenduser();
                    userId = userInfo.getUserid();
                    userName = SelectNameUtil.getName(userInfo.getComment(), userInfo.getNickname(), userInfo.getName());
                }
                if (ly.getReplyuser() != null) {
                    CompanyZoneItemUserInfo userInfo = ly.getReplyuser();
                    reUserId = userInfo.getUserid();
                    reUserName = SelectNameUtil.getName(userInfo.getComment(), userInfo.getNickname(), userInfo.getName());
                }
                v.setData(position, ly.getContent(), userId, userName, reUserId, reUserName);
                v.setOnClickData(ly, i);
                hodler.comments.addView(v,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        if (ListUtil.isEmpty(item.getCommentdata()) && hodler.lyLike.getVisibility() == View.GONE) {
            hodler.lyCommentsArrow.setVisibility(View.GONE);
            hodler.lyCommentsContent.setVisibility(View.GONE);
        } else if (ListUtil.isEmpty(item.getCommentdata()) || hodler.lyLike.getVisibility() == View.GONE) {
            hodler.lyCommentsArrow.setVisibility(View.VISIBLE);
            hodler.lyCommentsContent.setVisibility(View.VISIBLE);
            hodler.lyLikeLine.setVisibility(View.GONE);
        } else {
            hodler.lyCommentsArrow.setVisibility(View.VISIBLE);
            hodler.lyCommentsContent.setVisibility(View.VISIBLE);
            hodler.lyLikeLine.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private CommentTextView getCommentTextView(int i, int viewSize, List<CommentTextView> views) {
        CommentTextView v;
        if (i < viewSize) {
            v = views.get(i);
        } else {
            v = new CommentTextView(mContext);
            views.add(v);
            v.setOnCommentClickListener(mLis);
        }
        return v;
    }

    private class ViewHodler {
        CircleImageView userImage;
        TextView userName;
        TextView content;
        TextView time;

        ImageView imageFirst;
        ImageView imageSecond;
        ImageView imageThird;
        ImageView imageforth;
        ImageView imageFive;
        ImageView imageSix;
        ImageView imageSevent;
        ImageView imageEight;
        ImageView imageNine;

        View lyLike;
        View lyLikeLine;
        View lyCommentsArrow;
        View lyCommentsContent;

        TextView mNumZan;
        ImageView option;
        LinearLayout comments; // 评论布局
        List <CommentTextView> mCommentViews; // 评论缓存
    }

    /**
     * 动态设置图片布局
     */
    private void setImageLayout(View view, final CompanyZoneList item) {
        ViewHodler viewHodler = (ViewHodler) view.getTag();
        /** 动态设置布局 */

        if (ListUtil.isEmpty(item.getExrta())) {
            setGone(viewHodler.imageFirst);
            setGone(viewHodler.imageSecond);
            setGone(viewHodler.imageThird);
            setGone(viewHodler.imageforth);
            setGone(viewHodler.imageFive);
            setGone(viewHodler.imageSix);
            setGone(viewHodler.imageSevent);
            setGone(viewHodler.imageEight);
            setGone(viewHodler.imageNine);
            return;
        }
        // setImageLayout(view,
        // list.get((Integer)viewHodler.deleteLayout.getTag()));

        switch (item.getExrta().size()) {
            case 1:
                // setBigShape(viewHodler.imageFirst);
                /** 暂时设为小图片 */
                setSmallShape(viewHodler.imageFirst);
                setVisibility(item.getExrta().get(0).getThumb(), viewHodler.imageFirst);
                setGone(viewHodler.imageSecond);
                setGone(viewHodler.imageThird);
                setGone(viewHodler.imageforth);
                setGone(viewHodler.imageFive);
                setGone(viewHodler.imageSix);
                setGone(viewHodler.imageSevent);
                setGone(viewHodler.imageEight);
                setGone(viewHodler.imageNine);
                break;
            case 2:
                setSmallShape(viewHodler.imageFirst);
                setSmallShape(viewHodler.imageSecond);
                setVisibility(item.getExrta().get(0).getThumb(), viewHodler.imageFirst);
                setVisibility(item.getExrta().get(1).getThumb(),
                        viewHodler.imageSecond);
                setGone(viewHodler.imageThird);
                setGone(viewHodler.imageforth);
                setGone(viewHodler.imageFive);
                setGone(viewHodler.imageSix);
                setGone(viewHodler.imageSevent);
                setGone(viewHodler.imageEight);
                setGone(viewHodler.imageNine);
                break;
            case 3:
                setSmallShape(viewHodler.imageFirst);
                setSmallShape(viewHodler.imageSecond);
                setSmallShape(viewHodler.imageThird);
                setVisibility(item.getExrta().get(0).getThumb(), viewHodler.imageFirst);
                setVisibility(item.getExrta().get(1).getThumb(),
                        viewHodler.imageSecond);
                setVisibility(item.getExrta().get(2).getThumb(), viewHodler.imageThird);
                setGone(viewHodler.imageforth);
                setGone(viewHodler.imageFive);
                setGone(viewHodler.imageSix);
                setGone(viewHodler.imageSevent);
                setGone(viewHodler.imageEight);
                setGone(viewHodler.imageNine);
                break;
            case 4:
                /** 当有四张图片时，每行显示两个， 所有这个特殊处理 */
                setSmallShape(viewHodler.imageFirst);
                setSmallShape(viewHodler.imageSecond);
                setSmallShape(viewHodler.imageforth);
                setSmallShape(viewHodler.imageFive);
                setVisibility(item.getExrta().get(0).getThumb(), viewHodler.imageFirst);
                setVisibility(item.getExrta().get(1).getThumb(),
                        viewHodler.imageSecond);
                setVisibility(item.getExrta().get(2).getThumb(), viewHodler.imageforth);
                setVisibility(item.getExrta().get(3).getThumb(), viewHodler.imageFive);
                setGone(viewHodler.imageThird);
                setGone(viewHodler.imageSix);
                setGone(viewHodler.imageSevent);
                setGone(viewHodler.imageEight);
                setGone(viewHodler.imageNine);
                break;
            case 5:
                setSmallShape(viewHodler.imageFirst);
                setSmallShape(viewHodler.imageSecond);
                setSmallShape(viewHodler.imageThird);
                setSmallShape(viewHodler.imageforth);
                setSmallShape(viewHodler.imageFive);
                setVisibility(item.getExrta().get(0).getThumb(), viewHodler.imageFirst);
                setVisibility(item.getExrta().get(1).getThumb(),
                        viewHodler.imageSecond);
                setVisibility(item.getExrta().get(2).getThumb(), viewHodler.imageThird);
                setVisibility(item.getExrta().get(3).getThumb(), viewHodler.imageforth);
                setVisibility(item.getExrta().get(4).getThumb(), viewHodler.imageFive);
                setGone(viewHodler.imageSix);
                setGone(viewHodler.imageSevent);
                setGone(viewHodler.imageEight);
                setGone(viewHodler.imageNine);
                break;
            case 6:
                setSmallShape(viewHodler.imageFirst);
                setSmallShape(viewHodler.imageSecond);
                setSmallShape(viewHodler.imageThird);
                setSmallShape(viewHodler.imageforth);
                setSmallShape(viewHodler.imageFive);
                setSmallShape(viewHodler.imageSix);
                setVisibility(item.getExrta().get(0).getThumb(), viewHodler.imageFirst);
                setVisibility(item.getExrta().get(1).getThumb(),
                        viewHodler.imageSecond);
                setVisibility(item.getExrta().get(2).getThumb(), viewHodler.imageThird);
                setVisibility(item.getExrta().get(3).getThumb(), viewHodler.imageforth);
                setVisibility(item.getExrta().get(4).getThumb(), viewHodler.imageFive);
                setVisibility(item.getExrta().get(5).getThumb(), viewHodler.imageSix);
                setGone(viewHodler.imageSevent);
                setGone(viewHodler.imageEight);
                setGone(viewHodler.imageNine);
                break;
            case 7:
                setSmallShape(viewHodler.imageFirst);
                setSmallShape(viewHodler.imageSecond);
                setSmallShape(viewHodler.imageThird);
                setSmallShape(viewHodler.imageforth);
                setSmallShape(viewHodler.imageFive);
                setSmallShape(viewHodler.imageSix);
                setSmallShape(viewHodler.imageSevent);
                setVisibility(item.getExrta().get(0).getThumb(), viewHodler.imageFirst);
                setVisibility(item.getExrta().get(1).getThumb(),
                        viewHodler.imageSecond);
                setVisibility(item.getExrta().get(2).getThumb(), viewHodler.imageThird);
                setVisibility(item.getExrta().get(3).getThumb(), viewHodler.imageforth);
                setVisibility(item.getExrta().get(4).getThumb(), viewHodler.imageFive);
                setVisibility(item.getExrta().get(5).getThumb(), viewHodler.imageSix);
                setVisibility(item.getExrta().get(6).getThumb(),
                        viewHodler.imageSevent);
                setGone(viewHodler.imageEight);
                setGone(viewHodler.imageNine);
                break;
            case 8:
                setSmallShape(viewHodler.imageFirst);
                setSmallShape(viewHodler.imageSecond);
                setSmallShape(viewHodler.imageThird);
                setSmallShape(viewHodler.imageforth);
                setSmallShape(viewHodler.imageFive);
                setSmallShape(viewHodler.imageSix);
                setSmallShape(viewHodler.imageSevent);
                setSmallShape(viewHodler.imageEight);
                setVisibility(item.getExrta().get(0).getThumb(), viewHodler.imageFirst);
                setVisibility(item.getExrta().get(1).getThumb(),
                        viewHodler.imageSecond);
                setVisibility(item.getExrta().get(2).getThumb(), viewHodler.imageThird);
                setVisibility(item.getExrta().get(3).getThumb(), viewHodler.imageforth);
                setVisibility(item.getExrta().get(4).getThumb(), viewHodler.imageFive);
                setVisibility(item.getExrta().get(5).getThumb(), viewHodler.imageSix);
                setVisibility(item.getExrta().get(6).getThumb(),
                        viewHodler.imageSevent);
                setVisibility(item.getExrta().get(7).getThumb(), viewHodler.imageEight);
                setGone(viewHodler.imageNine);
                break;
            case 9:
                setSmallShape(viewHodler.imageFirst);
                setSmallShape(viewHodler.imageSecond);
                setSmallShape(viewHodler.imageThird);
                setSmallShape(viewHodler.imageforth);
                setSmallShape(viewHodler.imageFive);
                setSmallShape(viewHodler.imageSix);
                setSmallShape(viewHodler.imageSevent);
                setSmallShape(viewHodler.imageEight);
                setSmallShape(viewHodler.imageNine);
                setVisibility(item.getExrta().get(0).getThumb(), viewHodler.imageFirst);
                setVisibility(item.getExrta().get(1).getThumb(),
                        viewHodler.imageSecond);
                setVisibility(item.getExrta().get(2).getThumb(), viewHodler.imageThird);
                setVisibility(item.getExrta().get(3).getThumb(), viewHodler.imageforth);
                setVisibility(item.getExrta().get(4).getThumb(), viewHodler.imageFive);
                setVisibility(item.getExrta().get(5).getThumb(), viewHodler.imageSix);
                setVisibility(item.getExrta().get(6).getThumb(),
                        viewHodler.imageSevent);
                setVisibility(item.getExrta().get(7).getThumb(), viewHodler.imageEight);
                setVisibility(item.getExrta().get(8).getThumb(), viewHodler.imageNine);
                break;
            default:
                break;
        }

        viewHodler.imageFirst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(item, 0);
            }
        });

        viewHodler.imageSecond.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(item, 1);
            }
        });

        viewHodler.imageThird.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(item, 2);
            }
        });

        viewHodler.imageforth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(item, 3);
            }
        });

        viewHodler.imageFive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(item, 4);
            }
        });

        viewHodler.imageSix.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(item, 5);
            }
        });

        viewHodler.imageSevent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(item, 6);
            }
        });

        viewHodler.imageEight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(item, 7);
            }
        });

        viewHodler.imageNine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(item, 8);
            }
        });
    }

    /**
     * 根据屏幕的宽度，获得小图片的宽度
     * <p/>
     * 屏幕的宽度 - 左右两测的距离(margin) - 左侧个人头像所占的宽度 - 图片之间的宽度
     */
    private int getSmallWight() {
        /** 获得屏幕的宽度 */
        int leftMargin = mContext.getResources().getDimensionPixelOffset(
                R.dimen.event_meeting_record_left_margin);
        int rightMargin = mContext.getResources().getDimensionPixelOffset(
                R.dimen.event_meeting_record_right_margin);
        int leftLayoutDistance = mContext.getResources()
                .getDimensionPixelOffset(
                        R.dimen.event_meeting_record_left_layout);
        int imageBetweenDistance = mContext.getResources()
                .getDimensionPixelOffset(
                        R.dimen.event_meeting_record_iamge_distance);
        return (Constants.screenWidth - leftMargin - rightMargin
                - leftLayoutDistance - imageBetweenDistance * 2) / 3;
    }

    /**
     * 根据屏幕的宽度，获得大图片的宽度
     */
    // private int getBigWight() {
    // /** 获得屏幕的宽度 */
    // int distance = mContext.getResources().getDimensionPixelOffset(
    // R.dimen.image_distance);
    // return Constants.screenWidth - distance * 2;
    // }

    /**
     * 根据屏幕的宽度，设置小图片的大小
     */
    private void setSmallShape(ImageView imageView) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
                .getLayoutParams();
        params.width = getSmallWight();
        params.height = (int) (getSmallWight() * smallScale);
        imageView.setLayoutParams(params);
        // context.imageLoader.cancelDisplayTask(imageView);
        // context.imageLoader.displayImage(url, imageView);
        // ImageLoader.getInstance().displayImage(url, imageView);
        // context.imageLoader.loadImage(url, new ImageLoadingListener() {
        //
        // @Override
        // public void onLoadingStarted(String arg0, View arg1) {
        // }
        //
        // @Override
        // public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
        // {
        // }
        //
        // @Override
        // public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
        // // imageView.setImageBitmap(arg2);
        // imageView.setTag(arg2);
        // imageView.setImageBitmap((Bitmap)imageView.getTag());
        // }
        //
        // @Override
        // public void onLoadingCancelled(String arg0, View arg1) {
        // }
        // });
    }

    /**
     * 根据屏幕的宽度，设置大图片的大小
     */
    private void setBigShape(ImageView imageView) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
                .getLayoutParams();
        // params.width = (int) (getBigWight() * bigScale);
        // params.height = (int) (getBigWight() * bigScale);
        imageView.setLayoutParams(params);
    }

    /**
     * 显示图片
     */
    private void setVisibility(String url, ImageView imageView) {
        imageView.setBackgroundDrawable(null);
        // mContext.imageLoader.displayImage(url, imageView);

        ImageLoader.getInstance().displayImage(url, imageView, DisplayImageOptionsUtil.listNewImgOptions);
        imageView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏图片
     */
    private void setGone(ImageView imageView) {
        // imageView.setBackgroundResource(R.drawable.add_album_bg);
        // mContext.imageLoader.cancelDisplayTask(imageView);
        // imageView.setImageBitmap(null);

        imageView.setVisibility(View.GONE);
    }

    /**
     * 跳转图片查看页面
     */
    private void startActivity(CompanyZoneList meetingRecord, int position) {
        Intent intent = SaasPicShowActivity.newIntent(mContext, meetingRecord.getExrta(), position);
        mContext.startActivity(intent);
    }
}
