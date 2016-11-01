package com.yuntong.here.util;

/**
 * Created by me on 2016/4/25.
 * 存放应用中的常量
 */
public class Constants {

    public static final String FILE_PATH = "/sdcard/syscamera";
    // 相机，图库的选择
    public static final int TAKE_PICTURE_FROM_CAMERA = 100;
    public static final int TAKE_PICTURE_FROM_GALLERY = 200;
    public static final int DELETE_IMAGE = 300;
    public static final int REQUEST_CODE = 1000;// 从相册选择完照片后
    public static final int SELECT_FROM_PHOTO = 0;// 从相册选择
    public static final int SELECT_FROM_CAMERA = 1;// 从相机拍照
    public static final int PIC_EDIT_REQUEST_DATA = 2;// 图片编辑请求参数
    public final static String SCENE_BROADCAST_ACTION = "com.yuntong.here.sceneforpicture";
    public final static String ISEDITACTIVITYFINISH_BROADCAST_ACTION = "com.yuntong.here.isb";

    public static final String PICTURE_URL = "http://image.panocooker.com/file/upload";//

//    public static final String BASE_URL = "http://172.16.199.177:8080/";//  服务器地址  无线
//    public static final String BASE_URL = "http://172.16.180.55:8080/";// 服务器地址  有线 发发
//    public static final String BASE_URL = "http://172.16.180.47/";// 服务器地址  有线 振振
    public static final String BASE_URL = "http://testc.panocooker.com/";// 服务器地址  在线服务器

    //=================================================================
    public static final String LOGIN_URL = BASE_URL + "login/doLogin";// 登录接口
    public static final String LOGINOUT_URL = BASE_URL + "login/loginOut";// 登录接口
    public static final String REGISTER_URL = BASE_URL + "registerController/register";// 注册接口
    public static final String RETRIEVE_URL = BASE_URL + "login/retrieve";// 重置密码接口
    public static final String GETVERIFYCODE_URL = BASE_URL + "login/verifyCode";// 获取验证码接口
    public static final String PERSONALINFO_URL = BASE_URL + "completeInformationController/complete";// 完善资料接口
    public static final String SCENELIST_URL = BASE_URL + "scene/index";// 场景列表接口
    public static final String SCENEINTRODUCE_URL = BASE_URL + "scene/introduce";// 场景介绍接口
    public static final String COLLECTION_URL=BASE_URL+"favoriteController/add";//收藏接口
    public static final String COLLECTIONCANCEL_URL=BASE_URL+"favoriteController/delete";//取消收藏接口
    public static final String MYHERE_URL=BASE_URL+"hereController/queryAllMyHere";//我的here接口
    public static final String MYCOLLECTION_URL=BASE_URL+"favoriteController/queryAllMyFavorite";//我的收藏接口
    public static final String MYHERECANCEL_URL=BASE_URL+"hereController/delete";//删除here
    public static final String HEREDETAIL_URL=BASE_URL+"hereController/queryHereAndCommentDetail";//删除here
    public static final String MYCOMMENT_URL=BASE_URL+"commentController/queryAllCommentByUserId";//我的评论
    public static final String MYCOMMENTCANCEL_URL=BASE_URL+"commentController/delete";//删除评论
    public static final String FRIENDSLIST_URL=BASE_URL+"friend/myFriends";//好友列表
    public static final String DELETEFRIENDS_URL=BASE_URL+"friend/delete";//删除好友
    public static final String SEARCHUSERS_URL=BASE_URL+"user/fuzzy";//查询用户
    public static final String MODIFYPWD_URL=BASE_URL+"login/modifyPwd";//修改密码
    public static final String OTHERHERE_URL=BASE_URL+"hereController/queryAllOtherHere";//修改密码
    public static final String UPVOTE_URL=BASE_URL+"upvoteController/add";//点赞
    public static final String COMMENT_URL=BASE_URL+"commentController/add";//评论
    public static final String HERE_URL=BASE_URL+"hereController/add";// 发布here
    public static final String VERIFYHERE_URL=BASE_URL+"hereController/verifyHere";// 验证here
    public static final String GERCOLLECTION_URL=BASE_URL+"favoriteController/isFavorite";//是否收藏过
    public static final String GETUNREAD=BASE_URL+"adv/selectUnread";//未读消息
    public static final String GETRECEIVED=BASE_URL+"adv/received";//未读变已读
    public static final String GETREADED=BASE_URL+"adv/selectRead";//已读消息
    public static final String ADDFRIEND=BASE_URL+"friend/agree";//
    public static final String ADDFRIENDADD=BASE_URL+"friend/addFriend";//
    public static final String DELETEMESSAGE_URL=BASE_URL+"adv/delete/";//
    public static final String UPDATA_URL=BASE_URL+"version/versionInfo";//
    public static final String FEEDBACK_URL=BASE_URL+"version/feedback";// 意见反馈
    public static final String YANZHENGCODE_URL=BASE_URL+"registerController/verifyCode";// 验证码是否正确
    public static final String SCENENUM_URL=BASE_URL+"scene/browse";// 统计场景的浏览数










}
