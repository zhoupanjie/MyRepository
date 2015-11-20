package com.cplatform.xhxw.ui.ui.base.view;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.NewsResponse;
import com.cplatform.xhxw.ui.http.net.RecommendImagesRequest;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecommendImages extends RelativeLayout {

	private Context context;

	private AsyncHttpResponseHandler mLoadHandler;
	
	/** 暂定宽高比例 */
	private float scale = 0.75f;

	@Bind(R.id.recommend_linear_first)
	LinearLayout linearFirst;
	@Bind(R.id.recommend_linear_second)
	LinearLayout linearSecond;
	@Bind(R.id.recommend_linear_third)
	LinearLayout linearThird;
	@Bind(R.id.recommend_linear_forth)
	LinearLayout linearForth;

	@Bind(R.id.recommendimage_first)
	ImageView imageFirst;
	@Bind(R.id.recommendimage_second)
	ImageView imageSecond;
	@Bind(R.id.recommendimage_third)
	ImageView imageThird;
	@Bind(R.id.recommendimage_forth)
	ImageView imageForth;

	@Bind(R.id.recommendtext_first)
	TextView textFirst;
	@Bind(R.id.recommendtext_second)
	TextView textSecond;
	@Bind(R.id.recommendtext_third)
	TextView textThird;
	@Bind(R.id.recommendtext_forth)
	TextView textForth;

	/** 测试用的图片地址 */
	String[] imageUrl = { "http://img0.bdstatic.com/img/image/daren/sheying1.jpg",
	// "http://img0.bdstatic.com/img/image/daren/sheying1.jpg",
	// "http://img0.bdstatic.com/img/image/daren/sheying1.jpg",
	// "http://img0.bdstatic.com/img/image/daren/sheying1.jpg",
	// "http://img0.bdstatic.com/img/image/daren/sheying1.jpg"
	};

	public RecommendImages(Context context) {
		super(context);
		init(context);
	}

	public RecommendImages(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RecommendImages(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {

		this.context = context;

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.recommendimages, this);

		ButterKnife.bind(this);

//		dynamicLayout(imageUrl);
//		loadData();

	}

	@OnClick(R.id.recommendimage_first)
	public void first() {

	}

	@OnClick(R.id.recommendimage_second)
	public void second() {

	}

	@OnClick(R.id.recommendimage_third)
	public void third() {

	}

	@OnClick(R.id.recommendimage_forth)
	public void forth() {

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
			linearSecond.setVisibility(View.VISIBLE);
			linearThird.setVisibility(View.VISIBLE);
			linearForth.setVisibility(View.VISIBLE);
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
				linearSecond.setVisibility(View.GONE);
				linearThird.setVisibility(View.GONE);
				linearForth.setVisibility(View.GONE);

				setBigShape(imageFirst, url[0]);
			} else if (url.length == 2) {
				linearFirst.setVisibility(View.VISIBLE);
				linearSecond.setVisibility(View.VISIBLE);
				linearThird.setVisibility(View.GONE);
				linearForth.setVisibility(View.GONE);

				setSmallShape(imageFirst, url[0]);
				setSmallShape(imageSecond, url[1]);
			} else if (url.length == 3) {
				linearFirst.setVisibility(View.VISIBLE);
				linearSecond.setVisibility(View.VISIBLE);
				linearThird.setVisibility(View.VISIBLE);
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
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
				.getLayoutParams();
		params.width = getSmallWidht();
		params.height = (int) (getSmallWidht() * (Constants.RecommendSmallScale.scale));
		imageView.setLayoutParams(params);
		ImageLoader.getInstance().displayImage(url, imageView,
				DisplayImageOptionsUtil.recommendImagesOptions);
	}

	/** 设置大图片的宽高，以及加载图片 */
	private void setBigShape(ImageView imageView, String url) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
				.getLayoutParams();
		params.width = getBigWidht();
		params.height = (int) (getBigWidht() * (Constants.RecommendBigScale.scale));
		// ((ViewGroup)imageView.getParent()).set;
		imageView.setLayoutParams(params);
		ImageLoader.getInstance().displayImage(url, imageView,
				DisplayImageOptionsUtil.recommendImagesOptions);
	}
}
