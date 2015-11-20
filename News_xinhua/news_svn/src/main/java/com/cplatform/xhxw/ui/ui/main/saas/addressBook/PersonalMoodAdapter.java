package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneList;
import com.cplatform.xhxw.ui.plugin.gallery.PicShowActivity;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.main.saas.picShow.SaasPicShowActivity;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PersonalMoodAdapter extends BaseAdapter<PersonalMoods> {

	public PersonalMoodAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final PersonalMoods item = getItem(position);
		ViewHodler hodler = null;
		if (view == null) {
			hodler = new ViewHodler();

			view = LayoutInflater.from(mContext).inflate(
					R.layout.activity_personal_mood_item, null);

			hodler.line = (View) view.findViewById(R.id.title_line);
			hodler.time = (TextView) view.findViewById(R.id.personal_mood_time);
			hodler.content = (TextView) view
					.findViewById(R.id.personal_mood_content);
			hodler.firstImage = (ImageView) view
					.findViewById(R.id.personal_mood_first_image);
			hodler.secondImage = (ImageView) view
					.findViewById(R.id.personal_mood_second_image);
			hodler.thirdImage = (ImageView) view
					.findViewById(R.id.personal_mood_third_image);
			hodler.forthImage = (ImageView) view
					.findViewById(R.id.personal_mood_forth_image);
			hodler.fiveImage = (ImageView) view
					.findViewById(R.id.personal_mood_five_image);
			hodler.sixImage = (ImageView) view
					.findViewById(R.id.personal_mood_six_image);
			hodler.imageCountText = (TextView) view
					.findViewById(R.id.personal_mood_images_count);
			view.setTag(hodler);
		} else {
			hodler = (ViewHodler) view.getTag();
		}

		if (!TextUtils.isEmpty(item.getShowtime())) {
			if (item.getShowtime().length() > 3) {
				SpannableString spanText = new SpannableString(
						item.getShowtime());
				spanText.setSpan(new AbsoluteSizeSpan(25, true), spanText.length() - 2,
						spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				spanText.setSpan(new AbsoluteSizeSpan(15, true), spanText.length() - 3,
						spanText.length() - 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				spanText.setSpan(new AbsoluteSizeSpan(15, true), 0,
						spanText.length() - 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				
				hodler.time.setText(spanText);
			} else {
				hodler.time.setText(item.getShowtime());
				hodler.time.setTextSize(25);
			}
		} else {
			hodler.time.setText("");
			hodler.time.setTextSize(25);
		}

		if (position == 0) {
			hodler.time.setVisibility(View.VISIBLE);
			hodler.line.setVisibility(View.VISIBLE);
		} else {
			if (item.getShowtime().equals(getItem(position - 1).getShowtime())) {// 同一天
				hodler.time.setVisibility(View.GONE);
				hodler.line.setVisibility(View.GONE);
			} else {
				hodler.time.setVisibility(View.VISIBLE);
				hodler.line.setVisibility(View.VISIBLE);
			}
		}

		if (!TextUtils.isEmpty(item.getContent())) {
			hodler.content.setText(XWExpressionUtil.generateSpanComment(
					mContext, item.getContent(),
					(int) hodler.content.getTextSize()));
			hodler.content.setVisibility(View.VISIBLE);
		} else {
			hodler.content.setVisibility(View.GONE);
		}

		setImageLayout(view, item);

		if (item.getExrta() != null && item.getExrta().size() > 3) {
			hodler.imageCountText.setVisibility(View.VISIBLE);
			hodler.imageCountText.setText("共"
					+ String.valueOf(item.getExrta().size()) + "张");
		} else {
			hodler.imageCountText.setVisibility(View.GONE);
		}

		return view;
	}

	class ViewHodler {
		View line;
		ImageView userLogo;
		TextView content;
		TextView time;

		ImageView firstImage;
		ImageView secondImage;
		ImageView thirdImage;
		ImageView forthImage;
		ImageView fiveImage;
		ImageView sixImage;

		TextView imageCountText;
	}

	/**
	 * 动态设置图片布局
	 */
	private void setImageLayout(View view, final PersonalMoods item) {
		ViewHodler viewHodler = (ViewHodler) view.getTag();
		/** 动态设置布局 */

		if (ListUtil.isEmpty(item.getExrta())) {
			setGone(viewHodler.firstImage);
			setGone(viewHodler.secondImage);
			setGone(viewHodler.thirdImage);
			setGone(viewHodler.forthImage);
			setGone(viewHodler.fiveImage);
			setGone(viewHodler.sixImage);
			return;
		}

		switch (item.getExrta().size()) {
		case 1:
			setVisibility(item.getExrta().get(0).getThumb(),
					viewHodler.firstImage);
			setGone(viewHodler.secondImage);
			setGone(viewHodler.thirdImage);
			setGone(viewHodler.forthImage);
			setGone(viewHodler.fiveImage);
			setGone(viewHodler.sixImage);
			break;
		case 2:
			setVisibility(item.getExrta().get(0).getThumb(),
					viewHodler.firstImage);
			setVisibility(item.getExrta().get(1).getThumb(),
					viewHodler.secondImage);
			setGone(viewHodler.thirdImage);
			setGone(viewHodler.forthImage);
			setGone(viewHodler.fiveImage);
			setGone(viewHodler.sixImage);
			break;
		case 3:
			setVisibility(item.getExrta().get(0).getThumb(),
					viewHodler.firstImage);
			setVisibility(item.getExrta().get(1).getThumb(),
					viewHodler.secondImage);
			setVisibility(item.getExrta().get(2).getThumb(),
					viewHodler.thirdImage);
			setGone(viewHodler.forthImage);
			setGone(viewHodler.fiveImage);
			setGone(viewHodler.sixImage);
			break;
		/**
		 * 效果图最多显示三张图片， 根据效果图，临时去掉多余的图片，为防止以后有还原的可能，故先注释掉
		 */
		case 4:
			setVisibility(item.getExrta().get(0).getThumb(),
					viewHodler.firstImage);
			setVisibility(item.getExrta().get(1).getThumb(),
					viewHodler.secondImage);
			setVisibility(item.getExrta().get(2).getThumb(),
					viewHodler.thirdImage);
			/** 临时 */
			setGone(viewHodler.forthImage);
			// setVisibility(item.getExrta().get(3).getThumb(),
			// viewHodler.forthImage);
			setGone(viewHodler.fiveImage);
			setGone(viewHodler.sixImage);
			break;
		case 5:
			setVisibility(item.getExrta().get(0).getThumb(),
					viewHodler.firstImage);
			setVisibility(item.getExrta().get(1).getThumb(),
					viewHodler.secondImage);
			setVisibility(item.getExrta().get(2).getThumb(),
					viewHodler.thirdImage);
			/** 临时 */
			setGone(viewHodler.forthImage);
			setGone(viewHodler.fiveImage);
			// setVisibility(item.getExrta().get(3).getThumb(),
			// viewHodler.forthImage);
			// setVisibility(item.getExrta().get(4).getThumb(),
			// viewHodler.fiveImage);
			setGone(viewHodler.sixImage);
			break;
		case 6:
			setVisibility(item.getExrta().get(0).getThumb(),
					viewHodler.firstImage);
			setVisibility(item.getExrta().get(1).getThumb(),
					viewHodler.secondImage);
			setVisibility(item.getExrta().get(2).getThumb(),
					viewHodler.thirdImage);
			/** 临时 */
			setGone(viewHodler.forthImage);
			setGone(viewHodler.fiveImage);
			setGone(viewHodler.sixImage);
			// setVisibility(item.getExrta().get(3).getThumb(),
			// viewHodler.forthImage);
			// setVisibility(item.getExrta().get(4).getThumb(),
			// viewHodler.fiveImage);
			// setVisibility(item.getExrta().get(5).getThumb(),
			// viewHodler.sixImage);
			break;
		default:
			break;
		}

		viewHodler.firstImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(item, 0);
			}
		});

		viewHodler.secondImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(item, 1);
			}
		});

		viewHodler.thirdImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(item, 2);
			}
		});

		viewHodler.forthImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(item, 3);
			}
		});

		viewHodler.fiveImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(item, 4);
			}
		});

		viewHodler.sixImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(item, 5);
			}
		});

	}

	/**
	 * 显示图片
	 */
	private void setVisibility(String url, ImageView imageView) {
		imageView.setBackgroundDrawable(null);
		// mContext.imageLoader.displayImage(url, imageView);

		setShape(imageView);
		ImageLoader.getInstance().displayImage(url, imageView,
				DisplayImageOptionsUtil.newsMultiHorImgOptions);
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
	 * 根据屏幕的宽度，设置小图片的大小
	 */
	private void setShape(ImageView imageView) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
				.getLayoutParams();
		params.width = getSmallWight();
		params.height = getSmallWight();
		imageView.setLayoutParams(params);
	}

	/**
	 * 根据屏幕的宽度，动态获得小图片的宽度
	 */
	private int getSmallWight() {
		int layoutMarginLeft = mContext.getResources().getDimensionPixelOffset(
				R.dimen.person_mood_layout_margin_left);
		int layoutMarginRight = mContext.getResources()
				.getDimensionPixelOffset(
						R.dimen.person_mood_layout_margin_right);
		int dayTimeWidth = mContext.getResources().getDimensionPixelOffset(
				R.dimen.person_mood_day_time_width);
		int dayTimeContentDistance = mContext.getResources()
				.getDimensionPixelOffset(
						R.dimen.person_mood_day_time_content_distance);
		int contentMarginLeft = mContext.getResources()
				.getDimensionPixelOffset(
						R.dimen.person_mood_content_margin_left);
		int contentMarginRight = mContext.getResources()
				.getDimensionPixelOffset(
						R.dimen.person_mood_content_margin_right);
		int imageBetweenDistance = mContext.getResources()
				.getDimensionPixelOffset(R.dimen.person_mood_image_distance);
		return (Constants.screenWidth - layoutMarginLeft - layoutMarginRight
				- dayTimeWidth - dayTimeContentDistance - contentMarginLeft
				- contentMarginRight - imageBetweenDistance * 2) / 3;
	}

	/**
	 * 跳转图片查看页面
	 */
	private void startActivity(PersonalMoods item, int position) {
		List<String> list = new ArrayList<String>();
		if (item.getExrta() != null && item.getExrta().size() > 0) {
			for (int i = 0; i < item.getExrta().size(); i++) {
				list.add(item.getExrta().get(i).getFile());
			}
		}
		mContext.startActivity(PicShowActivity.newIntent(mContext, list,
				position, false));
	}
}
