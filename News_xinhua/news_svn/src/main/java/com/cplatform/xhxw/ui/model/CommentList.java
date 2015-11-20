package com.cplatform.xhxw.ui.model;

import java.util.List;

public class CommentList {

	private List<CommentHot> hotComments;
	private List<NewComment> comments;
	private int isnext;
	private int comment_sum_total;
	private int hotComment_sum_total;
	
	public int getIsnext() {
		return isnext;
	}
	public void setIsnext(int isnext) {
		this.isnext = isnext;
	}
	public List<CommentHot> getHotComments() {
		return hotComments;
	}
	public void setHotComments(List<CommentHot> hotComments) {
		this.hotComments = hotComments;
	}
	public List<NewComment> getComments() {
		return comments;
	}
	public void setComments(List<NewComment> comments) {
		this.comments = comments;
	}
	public int getComment_sum_total() {
		return comment_sum_total;
	}
	public void setComment_sum_total(int comment_sum_total) {
		this.comment_sum_total = comment_sum_total;
	}
	public int getHotComment_sum_total() {
		return hotComment_sum_total;
	}
	public void setHotComment_sum_total(int hotComment_sum_total) {
		this.hotComment_sum_total = hotComment_sum_total;
	}
	
}
