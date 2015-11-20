package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.cplatform.xhxw.ui.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.model.SpecialDetail;
import com.cplatform.xhxw.ui.util.ListUtil;

public class SpecialTopicImageNewItem extends RelativeLayout {

    @Bind(R.id.imageView1)
    ImageView imageView1;
    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.imageView3)
    ImageView imageView3;
    @Bind(R.id.imageView4)
    ImageView imageView4;
    @Bind(R.id.imageView5)
    ImageView imageView5;
    @Bind(R.id.title)
    TextView text;

    private Context context;

    private DisplayImageOptions opts = new DisplayImageOptions.Builder()
            .showStubImage(R.drawable.def_img_16_9)
            .showImageForEmptyUri(R.drawable.def_img_16_9)
            .showImageOnFail(R.drawable.def_img_16_9).cacheInMemory()
            .cacheOnDisc().build();

    /**
     * 小图片宽高比例1:1
     */
    private float smallScale = 1f;

    public SpecialTopicImageNewItem(Context context) {
        super(context);
        this.init(context);
    }

    private void init(Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_special_topic_image_new_item, this);
        ButterKnife.bind(this);
    }

    public void setData(SpecialDetail item, OnSpecialTopicClickListener listener) {
        setBigShape(imageView1);
        setSmallShape(imageView2);
        setSmallShape(imageView3);
        setSmallShape(imageView4);
        setSmallShape(imageView5);

        text.setText(item.getTitle());

        showImage(item, listener);
            /*setVisibility(item.getData().get(0).getImg(), imageView1);
			setVisibility(item.getData().get(1).getImg(), imageView2);
			setVisibility(item.getData().get(2).getImg(), imageView3);
			setVisibility(item.getData().get(3).getImg(), imageView4);
			setVisibility(item.getData().get(4).getImg(), imageView5);*/
    }

    /**
     * 根据屏幕的宽度，获得小图片的宽度
     */
    private int getSmallWight() {
        /** 获得屏幕的宽度 */
        int distance = context.getResources().getDimensionPixelOffset(
                R.dimen.special_topic_image_spacing);
        return (Constants.screenWidth - distance * 4) / 3;
    }

    /**
     * 根据屏幕的宽度，获得大图片的宽度
     */
    private int getBigWight() {
        /** 获得屏幕的宽度 */
        int distance = context.getResources().getDimensionPixelOffset(
                R.dimen.special_topic_image_spacing);

        /**获得小图片的宽度*/
        return getSmallWight() * 2 + distance;
    }

    /**
     * 根据屏幕的宽度，设置小图片的大小
     */
    private void setSmallShape(ImageView imageView) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
                .getLayoutParams();
        params.width = getSmallWight();
        params.height = (int) (getSmallWight() * smallScale);
        imageView.setLayoutParams(params);
    }

    /**
     * 根据屏幕的宽度，设置大图片的大小
     */
    private void setBigShape(ImageView imageView) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
                .getLayoutParams();
        params.width = getBigWight();
        params.height = (int) (getSmallWight() * smallScale);
        imageView.setLayoutParams(params);
    }

    /**
     * 显示图片
     */
    private void setVisibility(String url, ImageView imageView) {
        imageView.setBackgroundDrawable(null);
        ImageLoader.getInstance().displayImage(url, imageView, opts);
        imageView.setVisibility(View.VISIBLE);
    }

    private void showImage(final SpecialDetail item, final OnSpecialTopicClickListener listener) {
        if (!ListUtil.isEmpty(item.getData())) {
            int count = item.getData().size();
            switch (count) {
                case 1:
                    setVisibility(item.getData().get(0).getImg(), imageView1);
                    break;
                case 2:
                    setVisibility(item.getData().get(0).getImg(), imageView1);
                    setVisibility(item.getData().get(1).getImg(), imageView2);
                    break;
                case 3:
                    setVisibility(item.getData().get(0).getImg(), imageView1);
                    setVisibility(item.getData().get(1).getImg(), imageView2);
                    setVisibility(item.getData().get(2).getImg(), imageView3);
                    break;
                case 4:
                    setVisibility(item.getData().get(0).getImg(), imageView1);
                    setVisibility(item.getData().get(1).getImg(), imageView2);
                    setVisibility(item.getData().get(2).getImg(), imageView3);
                    setVisibility(item.getData().get(3).getImg(), imageView4);
                    break;
                case 5:
                    setVisibility(item.getData().get(0).getImg(), imageView1);
                    setVisibility(item.getData().get(1).getImg(), imageView2);
                    setVisibility(item.getData().get(2).getImg(), imageView3);
                    setVisibility(item.getData().get(3).getImg(), imageView4);
                    setVisibility(item.getData().get(4).getImg(), imageView5);
                    break;
            }
        } else {
            setVisibility("", imageView1);
            setVisibility("", imageView2);
            setVisibility("", imageView3);
            setVisibility("", imageView4);
            setVisibility("", imageView5);
        }

        imageView1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goToContent(item, 1, listener);
            }
        });

        imageView2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goToContent(item, 2, listener);
            }
        });

        imageView3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goToContent(item, 3, listener);
            }
        });

        imageView4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goToContent(item, 4, listener);
            }
        });

        imageView5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goToContent(item, 5, listener);
            }
        });
    }

    private void goToContent(SpecialDetail item, int position, OnSpecialTopicClickListener listener) {
        if (item.getData() != null) {
            int count = item.getData().size();

            if (count >= position && listener != null) {
                listener.onSpecialTopicPicViewClick(item.getData().get(position - 1));
            }
        }
    }
}

