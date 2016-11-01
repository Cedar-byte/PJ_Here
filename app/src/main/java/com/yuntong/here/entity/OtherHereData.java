package com.yuntong.here.entity;

/**
 * Created by Administrator on 2016/5/19.
 */
public class OtherHereData {


    private String upvoteNum;// 点赞数
    private String commentNum;// 评论数
    private String sceneName;// 场景名字
    private String sceneId;// 场景id
    private String hereId;// here id
    private String text;// here内容
    private String createTime;// 创建时间
    private String[] hereMediaDTOList;// 多张图片地址集合

    private String firstImg;// 显示的第一张照片

    private String isFriend;// 判断是否为好友，0、1

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public OtherHereData(){
        super();
    }

    public String getUpvoteNum() {
        return upvoteNum;
    }
    public void setUpvoteNum(String upvoteNum) {
        this.upvoteNum = upvoteNum;
    }
    public String getCommentNum() {
        return commentNum;
    }
    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }
    public String getSceneName() {
        return sceneName;
    }
    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
    public String getSceneId() {
        return sceneId;
    }
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    public String getHereId() {
        return hereId;
    }
    public void setHereId(String hereId) {
        this.hereId = hereId;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String[] getHereMediaDTOList() {
        return hereMediaDTOList;
    }
    public void setHereMediaDTOList(String[] hereMediaDTOList) {
        this.hereMediaDTOList = hereMediaDTOList;
    }
}
