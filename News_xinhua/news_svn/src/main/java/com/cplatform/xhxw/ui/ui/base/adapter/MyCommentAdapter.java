package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.MyComment;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.util.ActivityUtil;
import com.cplatform.xhxw.ui.util.DateUtil;

public class MyCommentAdapter extends BaseAdapter<MyComment>{
	
	private Context context;

	public MyCommentAdapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {

		final MyComment myComment = getItem(position);
		ViewHodler hodler;
		if (view == null) {
			hodler = new ViewHodler();
			view = LayoutInflater.from(mContext).inflate(R.layout.my_comment_item, null);
			
			hodler.commentTime = (TextView) view.findViewById(R.id.my_comment_time);
			hodler.commentText = (TextView) view.findViewById(R.id.my_comment_text);
			hodler.commentTitle = (TextView) view.findViewById(R.id.my_comment_from_text);
			hodler.titleLayout = (LinearLayout) view.findViewById(R.id.news_title_linear);
			
			view.setTag(hodler);
		}else {
			hodler = (ViewHodler) view.getTag();
		}
		
		if (myComment.getNewsTitle() != null) {
			hodler.commentTitle.setText(String.format(context.getResources().getString(R.string.comment_content_from), myComment.getNewsTitle()));
		}
		
		if (myComment.getPublished() != null) {
			hodler.commentTime.setText(DateUtil.getXHAPPDetailFormmatString(Long.valueOf(myComment.getPublished()) * 1000));
		}
		
		String cotent = myComment.getContent();
		if (cotent != null) {
			hodler.commentText.setText(XWExpressionUtil.generateSpanComment(
					context, cotent, 
					(int) hodler.commentText.getTextSize()));
		}
		
		hodler.titleLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (myComment.getNewsId() == null) {
					Toast.makeText(context, "无法获取新闻Id", Toast.LENGTH_LONG).show();
					return;
				}
				
				if (myComment.getNewsTitle() == null) {
					Toast.makeText(context, "无法获取新闻标题", Toast.LENGTH_LONG).show();
					return;
				}
				
				int newsType = (myComment.getNewstype() == null) ? 0 : Integer.valueOf(myComment.getNewstype());
				boolean isSaas = myComment.getSource() != null && myComment.getSource().equals("saas");
				ActivityUtil.goToNewsDetailPageByNewstype(context, newsType, 
						myComment.getNewsId(), null, isSaas, myComment.getNewsTitle());
			}
		});
		
		return view;
	}

	private class ViewHodler{
		TextView commentTitle;
		TextView commentTime;
		TextView commentText;
		LinearLayout titleLayout;
	}
	
}