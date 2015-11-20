package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

public interface OnCommentClickListener {

	/**
	 * 
	 * @param posi 行
	 * @param tag
	 * @param index 评论行
	 */
	public void onCommentClick(int posi, Object tag, int index);
	
	/**取消编辑状态*/
	public void onUnComment();
}
