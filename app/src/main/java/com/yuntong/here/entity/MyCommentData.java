package com.yuntong.here.entity;

/**
 * Created by Administrator on 2016/5/16.
 */
public class MyCommentData {
    private String comment_content;
    private String here_name;
    private String comment_time;
    private String hereId;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    private String commentId;

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    private String isDelete;

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getHereId() {
        return hereId;
    }

    public void setHereId(String hereId) {
        this.hereId = hereId;
    }



    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getHere_name() {
        return here_name;
    }

    public void setHere_name(String here_name) {
        this.here_name = here_name;
    }
}
