package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.util.TextViewUtil;

/**
 * 单张新闻图片布局
 * Created by cy-love on 13-12-25.
 */
public class PhotoSingleItem extends RelativeLayout {

    @Bind(R.id.iv_single_img) ImageView mImg;
    @Bind(R.id.tv_img_title) TextView mTitle;
   /* @Bind(R.id.tv_comment) TextView mComment;
*/
    public PhotoSingleItem(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_photo_single_item, this);
        ButterKnife.bind(this);
    }

    public void setData(New item) {
        ImageLoader.getInstance().displayImage(item.getBigthumbnail(), mImg, DisplayImageOptionsUtil.listNewImgOptions);
        mTitle.setText(item.getTitle());
        mTitle.setTextSize(NewItem.getTitleTextSize());
       /* mComment.setText(item.getPicInfo().size()+"");*/
        TextViewUtil.setDisplayModel(getContext(), mTitle, item.isRead());
        TextViewUtil.setDisplayBgModel(getContext(), this);
    }

}
