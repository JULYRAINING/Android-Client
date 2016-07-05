package com.md.util.net;

/**
 * Created by SECONDHEAVEN on 2016/1/3.0
 */
public class UrlString {
    //    private static String ip = "http://192.168.137.1:8080";
    private static String ip = "http://139.129.47.134:8080";
    private static String project = "/CUPServer";
    private static String uploadController = "/upload";
    private static String downloadController = "/download";
    private static String messageController = "/message";
    private static String userController = "/user";
    private static String collectionController = "/collection";
    private static String commentController = "/comment";


    private static String uploadRoot = ip + project + uploadController;
    private static String downloadRoot = ip + project + downloadController;
    private static String messageRoot = ip + project + messageController;
    private static String userRoot = ip + project + userController;
    private static String collectionRoot = ip + project + collectionController;
    private static String commentRoot = ip + project + commentController;
    //url地址
    public static String userLoginUrl = userRoot + "/login.do";
    public static String userGetDetailUrl = userRoot + "/getUserInfo.do";

    //url地址
    //上传图片文件
    public static String uploadImageUrl = uploadRoot + "/image.do";
    //上传Json字符串
    public static String uploadDataUrl = uploadRoot + "/dataJson.do";
    //获取图片
    public static String getImageUrl = downloadRoot + "/getImage.do";
    //发表文章 包括内容及图片
    public static String messageAddDataUrl = messageRoot + "/addBody.do";
    //通过用户id获取图片
    public static String messageGetDataUrl = messageRoot + "/getBodyByUserId.do";
    //获取文章图片
    public static String messageGetImageUrl = messageRoot + "/getImage.do";
    //ToUp获取文章列表
    public static String messageGetBodyUrl = messageRoot + "/getBody.do";
    //ToDown获取文章列表

    public static String messageGetToDownBodyUrl = messageRoot + "/getToDownBody.do";
    //完善用户信息
    public static String userRegisterUrl = userRoot + "/register.do";
    //获取用户头像图片
    public static String userGetImageUrl = userRoot + "/getImage.do";
    //用户注册
    public static String userSimpleRegisterUrl = userRoot + "/simpleRegister.do";
    //获取用户信息
    public static String userGetInfoUrl = userRoot + "/getUserInfo.do";

    //收藏
    public static String addCollectionUrl = collectionRoot + "/add.do";
    //取消收藏
    public static String removeCollectionUrl = collectionRoot + "/remove.do";

    public static String addCommentUrl = commentRoot + "/add.do";

    public static String getAllCommentByMessageIdUrl = commentRoot + "/getAll.do";


    public static final String LoginRequestParam = "loginInfo";
    // 通过用户id请求用户详细信息时，表单的参数
    public static final String GetUserDetailByIdRequestParam = "userid";


    //提交数据时，post的标志字符串
    //上传Data时，表单的参数
    public static String uploadDataTag = "data";
    //上传图片文件时，表单的参数
    public static String uploadImageTag = "uploadImage";
    //token参数
    public static String tokenTag = "token";

    //发表文章时，表单的参数
    public static String messageAddDataTag = "data";
    //使用Gson解析文章内容时，其List<HashMap<String,String>>中Hash的标识
    public static String imageMapTag = "Image";
    //请求图片时，表单的参数
    public static final String ImageRequestParam = "Image";
    //注册用户时，表单的参数
    public static final String SimpleRegisteUserRequestParam = "simpleUserInfo";

    public static final String TokenValidateRequestParam = "tokenValidate";

    public static final String CollectionRequestParam = "collectionInfo";

    public static final String CommentRequestParam = "commentInfo";

    public static final String GetCommentRequestParam = "messageId";
}
