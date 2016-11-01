package com.yuntong.here.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/5/26.
 */

@DatabaseTable(tableName = "tb_message")
public class MessageData {

    @DatabaseField(columnName = "Bid")
    private String Bid;
    @DatabaseField(columnName = "content")
    private String content;
    @DatabaseField(columnName = "createTime")
    private String createTime;
    @DatabaseField(columnName = "advicePic")
    private String advicePic;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "type")
    private String type;
    @DatabaseField(columnName = "desc")
    private String desc;
    @DatabaseField(columnName = "adviceId")
    private String adviceId;
    @DatabaseField(columnName = "isClick")
    private String isClick;
    @DatabaseField(columnName = "introduction")
    private String introduction;
    @DatabaseField(columnName = "link")
    private String link;
    @DatabaseField(columnName = "userName")
    private String userName;
    @DatabaseField(columnName = "userId")
    private String userId;
    @DatabaseField(columnName = "hereId")
    private String hereId;
    @DatabaseField(columnName = "sceneName")
    private String sceneName;

    public String getYuntongid() {
        return yuntongid;
    }

    public void setYuntongid(String yuntongid) {
        this.yuntongid = yuntongid;
    }

    @DatabaseField(columnName = "yuntongid")
    private String yuntongid;

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getHereId() {
        return hereId;
    }

    public void setHereId(String hereId) {
        this.hereId = hereId;
    }



    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getIsClick() {
        return isClick;
    }

    public void setIsClick(String isClick) {
        this.isClick = isClick;
    }

    public String getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(String adviceId) {
        this.adviceId = adviceId;
    }

    public String getBid() {
        return Bid;
    }

    public void setBid(String bid) {
        Bid = bid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAdvicePic() {
        return advicePic;
    }

    public void setAdvicePic(String advicePic) {
        this.advicePic = advicePic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}

