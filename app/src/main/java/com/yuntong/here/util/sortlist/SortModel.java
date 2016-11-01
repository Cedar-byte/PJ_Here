package com.yuntong.here.util.sortlist;

public class SortModel {

//	        "gender": 1,
//			"headpic": null,
//			"status": 0,
//			"nickname": "版本",
//			"pinyin": "bb",
//			"name": "18684655058",
//			"userId": 51
//
//	  //显示的数�?
//	private String sortLetters;  //显示数据拼音的首字母
//
//
//
	public SortModel(){
		super();
	}


	private String gender;
	private String headpic;
	private String status;
	private String nickname;
	private String pinyin;
	private String name;
	private String userId;
	private String sortLetters;  //显示数据拼音的首字母

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public SortModel(String gender, String headpic, String status, String nickname, String pinyin, String name, String userId) {
		this.gender = gender;
		this.headpic = headpic;
		this.status = status;
		this.nickname = nickname;
		this.pinyin = pinyin;
		this.name = name;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "SortModel{" +
				"gender='" + gender + '\'' +
				", headpic='" + headpic + '\'' +
				", status='" + status + '\'' +
				", nickname='" + nickname + '\'' +
				", pinyin='" + pinyin + '\'' +
				", name='" + name + '\'' +
				", userId='" + userId + '\'' +
				'}';
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
