package com.cplatform.xhxw.ui.ui.base.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.NewsResponse;
import com.cplatform.xhxw.ui.http.net.RecommendImagesRequest;
import com.cplatform.xhxw.ui.model.RecommendAtlas;
import com.cplatform.xhxw.ui.ui.picShow.PicShowActivity;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 推荐图集布局
 * Created by wjt on 14-04-05.
 */
public class RecommendAtlasView extends RelativeLayout {

	private Context context;

	private AsyncHttpResponseHandler mLoadHandler;
	
	private List<RecommendAtlas> recommendPics;
	
	/** 暂定宽高比例 */
	private float scale = 0.75f;

	@InjectView(R.id.recommend_linear_first)
	RelativeLayout linearFirst;
	@InjectView(R.id.recommend_linear_second)
	RelativeLayout linearSecond;
	@InjectView(R.id.recommend_linear_third)
	RelativeLayout linearThird;
	@InjectView(R.id.recommend_linear_forth)
	RelativeLayout linearForth;

	@InjectView(R.id.recommendatlas_first)
	ImageView imageFirst;
	@InjectView(R.id.recommendatlas_second)
	ImageView imageSecond;
	@InjectView(R.id.recommendatlas_third)
	ImageView imageThird;
	@InjectView(R.id.recommendatlas_forth)
	ImageView imageForth;

	@InjectView(R.id.recommendtext_first)
	TextView textFirst;
	@InjectView(R.id.recommendtext_second)
	TextView textSecond;
	@InjectView(R.id.recommendtext_third)
	TextView textThird;
	@InjectView(R.id.recommendtext_forth)
	TextView textForth;

	/** 测试用的图片地址 */
	String[] imageUrl = { "http://img0.bdstatic.com/img/image/daren/sheying1.jpg",
	 "http://img0.bdstatic.com/img/image/daren/sheying1.jpg",
	 "http://img0.bdstatic.com/img/image/daren/sheying1.jpg",
	 "http://img0.bdstatic.com/img/image/daren/sheying1.jpg"
	};

	private boolean isEnterprise = false;
	
	public RecommendAtlasView(Context context) {
		super(context);
		init(context);
	}

	public RecommendAtlasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RecommendAtlasView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public RecommendAtlasView(Context context, List<RecommendAtlas> recommendPics) {
		super(context);
		this.recommendPics = recommendPics;
		init(context);
	}

	private void init(Context context) {

		this.context = context;

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.recommendatlas, this);

		ButterKnife.inject(this);
		for(int i=0;i<recommendPics.size();i++){
			imageUrl[i]=recommendPics.get(i).getThumbnail();
		}
		dynamicLayout(imageUrl);
//		loadData();

	}

	@OnClick(R.id.recommendatlas_first)
	public void first() {
		if (CommonUtils.isFastDoubleClick()) {
            return;
        }
		 Intent intent = PicShowActivity.getIntent(context.getApplicationContext(), 
				 recommendPics.get(0).getNewsId(), true, isEnterprise, recommendPics.get(0).getTitle());
		 context.startActivity(intent);
	}

	@OnClick(R.id.recommendatlas_second)
	public void second() {
		if (CommonUtils.isFastDoubleClick()) {
            return;
        }
		Intent intent = PicShowActivity.getIntent(context.getApplicationContext(), 
				recommendPics.get(1).getNewsId(), true, isEnterprise, recommendPics.get(1).getTitle());
		context.startActivity(intent);
	}

	@OnClick(R.id.recommendatlas_third)
	public void third() {
		if (CommonUtils.isFastDoubleClick()) {
            return;
        }
		Intent intent = PicShowActivity.getIntent(context.getApplicationContext(), 
				recommendPics.get(2).getNewsId(), true, isEnterprise, recommendPics.get(2).getTitle());
		context.startActivity(intent);
	}

	@OnClick(R.id.recommendatlas_forth)
	public void forth() {
		if (CommonUtils.isFastDoubleClick()) {
            return;
        }
		Intent intent = PicShowActivity.getIntent(context.getApplicationContext(), 
				recommendPics.get(3).getNewsId(), true, isEnterprise, recommendPics.get(3).getTitle());
		context.startActivity(intent);
	}

	/** 联网，获取数据 */
	private void loadData(String channelid, String currentnewsid) {
		
		RecommendImagesRequest request = new RecommendImagesRequest();
		request.setChannelid(channelid);
		request.setCurrentnewsid(currentnewsid);
		
		APIClient.recommendImages(request, new AsyncHttpResponseHandler() {
			@Override
			public void onFinish() {
				mLoadHandler = null;
			}

			@Override
			protected void onPreExecute() {
				if (mLoadHandler != null)
					mLoadHandler.cancle();
				mLoadHandler = this;
			}

			@Override
			public void onFailure(Throwable error, String content) {
//				mDefView.setStatus(DefaultView.Status.error);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				NewsResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content, NewsResponse.class);
				} catch (Exception e) {
//					mDefView.setStatus(DefaultView.Status.error);
//					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					if (response.getData() != null)
						dynamicLayout(imageUrl);
//					mDefView.setStatus(DefaultView.Status.showData);
				} else {
//					mDefView.setStatus(DefaultView.Status.error);
				}
			}
		});
	}

	/** 动态设置布局和界面赋值 */
	private void dynamicLayout(String[] url) {
		if (url.length >= 4) {
			linearFirst.setVisibility(View.VISIBLE);
			textFirst.setText(recommendPics.get(0).getTitle());
			linearSecond.setVisibility(View.VISIBLE);
			textSecond.setText(recommendPics.get(1).getTitle());
			linearThird.setVisibility(View.VISIBLE);
			textThird.setText(recommendPics.get(2).getTitle());
			linearForth.setVisibility(View.VISIBLE);
			textForth.setText(recommendPics.get(3).getTitle());
			for (int i = 0; i < 4; i++) {
				// 提取前4个url,
				if (i == 0) {
					setSmallShape(imageFirst, url[i]);
				} else if (i == 1) {
					setSmallShape(imageSecond, url[i]);
				} else if (i == 2) {
					setSmallShape(imageThird, url[i]);
				} else if (i == 3) {
					setSmallShape(imageForth, url[i]);
				}
			}
		} else if (url.length <= 4) {
			if (url.length == 0) {
				linearFirst.setVisibility(View.GONE);
				linearSecond.setVisibility(View.GONE);
				linearThird.setVisibility(View.GONE);
				linearForth.setVisibility(View.GONE);
			} else if (url.length == 1) {
				linearFirst.setVisibility(View.VISIBLE);
				textFirst.setText(recommendPics.get(0).getTitle());
				linearSecond.setVisibility(View.GONE);
				linearThird.setVisibility(View.GONE);
				linearForth.setVisibility(View.GONE);

				setBigShape(imageFirst, url[0]);
			} else if (url.length == 2) {
				linearFirst.setVisibility(View.VISIBLE);
				textFirst.setText(recommendPics.get(0).getTitle());
				linearSecond.setVisibility(View.VISIBLE);
				textSecond.setText(recommendPics.get(1).getTitle());
				linearThird.setVisibility(View.GONE);
				linearForth.setVisibility(View.GONE);

				setSmallShape(imageFirst, url[0]);
				setSmallShape(imageSecond, url[1]);
			} else if (url.length == 3) {
				linearFirst.setVisibility(View.VISIBLE);
				textFirst.setText(recommendPics.get(0).getTitle());
				linearSecond.setVisibility(View.VISIBLE);
				textSecond.setText(recommendPics.get(1).getTitle());
				linearThird.setVisibility(View.VISIBLE);
				textThird.setText(recommendPics.get(2).getTitle());
				linearForth.setVisibility(View.GONE);

				setSmallShape(imageFirst, url[0]);
				setSmallShape(imageSecond, url[1]);
				setBigShape(imageThird, url[2]);
			}
		}
	}

	/** 根据屏幕的大小，获得小图片应有的宽高 */
	private int getSmallWidht() {
		int widht = 0;
		int distance = 0;

		/** 距离转换为像素 */
		distance = getResources().getDimensionPixelOffset(
				R.dimen.image_distance);
		widht = Constants.screenWidth - distance * 3;

		return widht / 2;
	}

	/** 根据屏幕的大小，获得大图片应有的宽高 */
	private int getBigWidht() {
		int widht = 0;
		int distance = 0;

		/** 距离转换为像素 */
		distance = getResources().getDimensionPixelOffset(
				R.dimen.image_distance);
		widht = Constants.screenWidth - distance * 2;

		return widht;
	}

	/** 设置小图片的宽高，以及加载图片 */
	private void setSmallShape(ImageView imageView, String url) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView
				.getLayoutParams();
		params.width = getSmallWidht();
		params.height = (int) (getSmallWidht() * (Constants.RecommendSmallScale.scale));
		imageView.setLayoutParams(params);
		ImageLoader.getInstance().displayImage(url, imageView,
				DisplayImageOptionsUtil.recommendImagesOptions);
	}

	/** 设置大图片的宽高，以及加载图片 */
	private void setBigShape(ImageView imageView, String url) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView
				.getLayoutParams();
		params.width = getBigWidht();
		params.height = (int) (getBigWidht() * (Constants.RecommendBigScale.scale));
		// ((ViewGroup)imageView.getParent()).set;
		imageView.setLayoutParams(params);
		ImageLoader.getInstance().displayImage(url, imageView,
				DisplayImageOptionsUtil.recommendImagesOptions);
	}

	public boolean isEnterprise() {
		return isEnterprise;
	}

	public void setEnterprise(boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}
}
