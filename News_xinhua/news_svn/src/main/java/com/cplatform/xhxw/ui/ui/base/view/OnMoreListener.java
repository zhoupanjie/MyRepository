package com.cplatform.xhxw.ui.ui.base.view;

/**
 * 
 * @author zc
 *
 */
public interface OnMoreListener {
	// 评论编辑框点击
	public void onEditTextClick();

	// 评论提交或者进入评论页
	public void onClickCommentBtn();

	// 表情区域收起显示
	public void onClickExprBtn();

	// 选择文字大小
	public void changeTextSize(int textSize);

	// 日间、夜间模式切换
	public void changeDisplayModel();

	// 圈阅新闻
	public void showSignNewsAlert();

	// 收藏新闻
	public void collect();

}
