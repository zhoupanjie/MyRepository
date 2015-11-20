package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import com.cplatform.xhxw.ui.model.saas.CommentSubData;

/**
 * Created by cy-love on 14-8-30.
 */
public interface OnCommunityCommentaryListener {
    /**
     * 显示评论布局
     */
    public void onShowCommunityCommentary(boolean isCompanyCircle, String infoId, String infoUserId, String userId, String commentId, String hint);

    /**
     * 隐藏布局
     */
    public void onHideCommunityCommentary();

    /**
     * 提交评论
     */
    public void onCommentarySutmit(CommentSubData result);
}
