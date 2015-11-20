package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.http.responseType.SpecialModelType;
import com.cplatform.xhxw.ui.model.SpecialDetail;
import com.cplatform.xhxw.ui.model.SpecialDetailData;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.base.view.*;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;

/**
 * 新闻专题适配器
 * Created by cy-love on 13-12-25.
 */
public class SpecialTopicAdapter extends BaseAdapter<Object>  {

    private OnSpecialTopicClickListener mListener;

    public SpecialTopicAdapter(Context context, OnSpecialTopicClickListener listener) {
        super(context);
        this.mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object obj = getItem(position);
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case SpecialModelType.SPECIAL_TOPIC: // 推荐
                {
                    SpecialDetailData item = (SpecialDetailData) obj;
                    convertView = initReco(mContext, item.getWidth(), item.getHeight());
                }
                    break;
                case SpecialModelType.NEWS:
                    convertView = new SpecialTopicNewItem(mContext);
                    break;
                case SpecialModelType.ATLAS:
                    convertView = new SpecialTopicPicNewItem(mContext);
                    break;
                case SpecialModelType.VIDEO:
                    convertView = new SpecialTopicVideoNewItem(mContext);
                    break;
                case SpecialModelType.IMAGE_5:
                    convertView = new SpecialTopicImageNewItem(mContext);
                    break;
                case SpecialModelType.SLIDER:
                    convertView = new SpecialTopicSliderView(mContext, mListener);
                    break;
                case SpecialModelType.NEWS_TITLE:
                default:
                    convertView = new SpecialTopicTextNewItem(mContext);
                    break;
            }
        }
        switch (type) {
            case SpecialModelType.SPECIAL_TOPIC: // 推荐
            {
                ImageView mReco = (ImageView)convertView;
                SpecialDetailData item = (SpecialDetailData) obj;
                ImageLoader.getInstance().displayImage(item.getSpecialimage(), mReco,
                        DisplayImageOptionsUtil.SpecialTopicRecoOptions);
            }
                break;
            case SpecialModelType.NEWS:
            {
                SpecialTopicNewItem view = (SpecialTopicNewItem)convertView;
                SpecialDetailData item = (SpecialDetailData) obj;
                view.setData(item, item.is_showHeadler());
            }
                break;
            case SpecialModelType.ATLAS:
            {
                SpecialTopicPicNewItem view = (SpecialTopicPicNewItem)convertView;
                SpecialDetail item = (SpecialDetail) obj;
                view.setData(item, item.is_showHeadler(), mListener);
            }
                break;
            case SpecialModelType.VIDEO:
            {
                SpecialTopicVideoNewItem view = (SpecialTopicVideoNewItem)convertView;
                SpecialDetail item = (SpecialDetail) obj;
                view.setData(item, item.is_showHeadler(), mListener);
            }
                break;
            case SpecialModelType.IMAGE_5:
            {
                SpecialTopicImageNewItem view = (SpecialTopicImageNewItem)convertView;
                SpecialDetail item = (SpecialDetail) obj;
                view.setData(item, mListener);
            }
                break;
            case SpecialModelType.SLIDER:
            {
                SpecialTopicSliderView view = (SpecialTopicSliderView)convertView;
                SpecialDetail item = (SpecialDetail) obj;
                view.setDate(item);
            }
                break;
            case SpecialModelType.NEWS_TITLE:
            default:
            {
                SpecialTopicTextNewItem view = (SpecialTopicTextNewItem)convertView;
                SpecialDetailData item = (SpecialDetailData) obj;
                view.setData(item, item.is_showHeadler());
            }
                break;
        }
        return convertView;
    }

    /**
     * 初始化推荐图
     */
    private ImageView initReco(Context context, int width, int height) {
        ImageView mReco = new ImageView(context);
        double hTmp;
        if(width == 0) {
        	hTmp = Constants.screenWidth * 3 / 4;
        } else {
        	hTmp = Constants.screenWidth * height / width;
        }
        int heightTmp = (int) Math.ceil(hTmp) + 1;
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(Constants.screenWidth, heightTmp);
        mReco.setLayoutParams(lp);
        mReco.setScaleType(ImageView.ScaleType.FIT_XY);
        mReco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 禁止点击
            }
        });
        return mReco;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = getItem(position);
        int type;
        if (obj instanceof SpecialDetail) {
            type =  ((SpecialDetail)obj).getSpecialmodeltype();
        } else {
            type = ((SpecialDetailData)obj).getSpecialmodeltype();
        }

        return type > 6 ? SpecialModelType.NEWS_TITLE : type;
    }

    @Override
    public int getViewTypeCount() {
        return 7;
    }
}
