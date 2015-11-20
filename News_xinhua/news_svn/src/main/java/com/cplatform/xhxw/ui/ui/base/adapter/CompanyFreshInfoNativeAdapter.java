package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.CompanyFreshsMoodInfoComment;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.base.widget.CircleImageView;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CompanyFreshInfoNativeAdapter extends
		BaseAdapter<CompanyFreshsMoodInfoComment> {

	private boolean haveParis = false;
	private onDeleteMoodCommentListener listener;

	public CompanyFreshInfoNativeAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		final CompanyFreshsMoodInfoComment item = getItem(position);
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater
					.from(mContext)
					.inflate(
							R.layout.activity_friends_fresh_mood_native_info_item,
							null);

			holder.line = (View) view.findViewById(R.id.line);
			holder.biaojiImage = (ImageView) view.findViewById(R.id.tubiao);
			holder.tubiaoLayout = (LinearLayout) view.findViewById(R.id.tubiao_layout);
			holder.userLogo = (CircleImageView) view
					.findViewById(R.id.user_logo);
			holder.userName = (TextView) view.findViewById(R.id.user_name);
			holder.contentText = (TextView) view.findViewById(R.id.content);
			holder.timeText = (TextView) view.findViewById(R.id.time);
			holder.deleteText = (TextView) view.findViewById(R.id.delete);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		ImageLoader.getInstance().displayImage(item.getUserinfo().getLogo(),
				holder.userLogo,
				DisplayImageOptionsUtil.avatarSaasImagesOptions);

		holder.contentText.setText(XWExpressionUtil.generateSpanComment(
				mContext, item.getContent(),
				(int) holder.contentText.getTextSize()));
		holder.timeText.setText(item.getFriendtime());

		if (!TextUtils.isEmpty(item.getUserinfo().getUserid())) {
			if (item.getUserinfo().getUserid().equals(Constants.getUid())) {
				holder.deleteText.setVisibility(View.VISIBLE);
			} else {
				holder.deleteText.setVisibility(View.GONE);
			}
		} else {
			holder.deleteText.setVisibility(View.GONE);
		}

		SpannableStringBuilder span = new SpannableStringBuilder();
		String userId = Constants.getUid();
		if (!TextUtils.isEmpty(item.getUserinfo().getUserid())) {

			if (userId.equals(item.getUserinfo().getUserid())) {
				span.append("我");
			} else if (!TextUtils
					.isEmpty(SelectNameUtil.getName("", item.getUserinfo()
							.getNickname(), item.getUserinfo().getName()))) {
				span.append(SelectNameUtil.getName("", item.getUserinfo()
						.getNickname(), item.getUserinfo().getName()));
			}

			span.setSpan(new ForegroundColorSpan(mContext.getResources()
					.getColor(R.color.blue)), 0, span.length(),
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			if (!TextUtils.isEmpty(item.getFriendinfo().getUserid())) {
				span.append("回复");
				if (userId.equals(item.getFriendinfo().getUserid())) {
					span.append("我");
					span.setSpan(new ForegroundColorSpan(mContext
							.getResources().getColor(R.color.blue)),
							span.length() - "我".length(), span.length(),
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				} else if (!TextUtils.isEmpty(SelectNameUtil.getName("", item
						.getFriendinfo().getNickname(), item.getFriendinfo()
						.getName()))) {
					span.append(SelectNameUtil.getName("", item.getFriendinfo()
							.getNickname(), item.getFriendinfo().getName()));
					span.setSpan(
							new ForegroundColorSpan(mContext.getResources()
									.getColor(R.color.blue)),
							span.length()
									- SelectNameUtil.getName("",
											item.getFriendinfo().getNickname(),
											item.getFriendinfo().getName())
											.length(), span.length(),
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				}
			}
			span.append(" : ");
		}

//		holder.userName.setText(XWExpressionUtil.generateSpanComment(mContext,
//				span.toString(), (int) holder.userName.getTextSize()));
		holder.userName.setText(span);
		
		holder.deleteText.setTag(position);
		holder.deleteText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (listener != null) {
					listener.onDeleteMoodComment(item.getCommentid(), position);
				}
			}
		});
		
		if (position == 0) {
			holder.biaojiImage.setVisibility(View.VISIBLE);
		}else {
			holder.biaojiImage.setVisibility(View.INVISIBLE);
		}
		
		if (haveParis) {//有赞
			if (position == 0) {
				holder.tubiaoLayout.setBackgroundResource(R.drawable.freshs_mood_info_not_round);
			}else if (position == getCount() - 1) {
				holder.tubiaoLayout.setBackgroundResource(R.drawable.freshs_mood_info_bottom_round);
			}else {
				holder.tubiaoLayout.setBackgroundResource(R.drawable.freshs_mood_info_not_round);
			}
		}else {//没有赞
			if (position == 0) {
				holder.tubiaoLayout.setBackgroundResource(R.drawable.freshs_mood_info_top_round);
			}else if (position == getCount() - 1) {
				holder.tubiaoLayout.setBackgroundResource(R.drawable.freshs_mood_info_bottom_round);
			}else {
				holder.tubiaoLayout.setBackgroundResource(R.drawable.freshs_mood_info_not_round);
			}
		}
		
		if (position == getCount() - 1) {
			holder.line.setVisibility(View.GONE);
		}else {
			holder.line.setVisibility(View.VISIBLE);
		}
		
		return view;
	}

	private class ViewHolder {
		View line;
		ImageView biaojiImage;
		LinearLayout tubiaoLayout;
		CircleImageView userLogo;
		TextView userName;
		TextView contentText;
		TextView timeText;
		TextView deleteText;
	}

	public boolean isHaveParis() {
		return haveParis;
	}

	public void setHaveParis(boolean haveParis) {
		this.haveParis = haveParis;
	}
	
	public interface onDeleteMoodCommentListener {
		/** 删除新鲜事评论 */
		public void onDeleteMoodComment(String commentId, int position);
	}

	public void setOnDeleteMoodListener(onDeleteMoodCommentListener listener) {
		this.listener = listener;
	}
}
