package com.md.util.networkapi;

import com.md.entity.CommentBO;
import com.md.util.net.JsonConverter;
import com.md.util.net.UrlString;
import com.md.util.request.ResponseListener;
import com.md.util.request.StringPostRequest;
import com.md.util.request.VolleyQueueApi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SECONDHEAVEN on 2016/1/26.
 */
public class CommentApi {
    public static void comment(CommentBO commentBO, ResponseListener responseListener) {

        String commentInfo = JsonConverter.toJson(commentBO);

        Map<String, String> params = new HashMap<>();
        params.put(UrlString.CommentRequestParam, commentInfo);
        StringPostRequest stringPostRequest = new StringPostRequest(UrlString.addCommentUrl,
                params,
                responseListener);
        VolleyQueueApi.getVolleyQueue().add(stringPostRequest);
    }

    public static void getAllComment(int messageId, ResponseListener responseListener) {

        Map<String, String> params = new HashMap<>();
        params.put(UrlString.GetCommentRequestParam, String.valueOf(messageId));
        StringPostRequest stringPostRequest = new StringPostRequest(UrlString.getAllCommentByMessageIdUrl,
                params,
                responseListener);
        VolleyQueueApi.getVolleyQueue().add(stringPostRequest);
    }
}
