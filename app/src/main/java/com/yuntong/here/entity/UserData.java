package com.yuntong.here.entity;

/**
 * Created by Administrator on 2016/5/17.
 * 用户查询
 */
public class UserData {

    private String userId;
    private String name;
    private String nickname;
    private String headPic;
    private String gender;
    private String friend;



    private String isadd;// 判断是否点击

    public UserData(String userId, String name, String nickname, String headPic, String gender, String friend,String isadd) {
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.headPic = headPic;
        this.gender = gender;
        this.friend = friend;
        this.isadd=isadd;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headPic='" + headPic + '\'' +
                ", gender='" + gender + '\'' +
                ", friend='" + friend + '\'' +
                ", isadd='" + isadd + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
    public String getIsadd() {
        return isadd;
    }

    public void setIsadd(String isadd) {
        this.isadd = isadd;
    }
}
