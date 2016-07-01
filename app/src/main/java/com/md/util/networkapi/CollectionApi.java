package com.md.util.networkapi;

import com.md.entity.CollectionBO;
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
public class CollectionApi {
    public static void collect(CollectionBO collectionBO, ResponseListener responseListener) {

        String collectionInfo = JsonConverter.toJson(collectionBO);

        Map<String, String> params = new HashMap<>();
        params.put(UrlString.CollectionRequestParam, collectionInfo);
        StringPostRequest stringPostRequest = new StringPostRequest(UrlString.addCollectionUrl,
                params,
                responseListener);
        VolleyQueueApi.getVolleyQueue().add(stringPostRequest);
    }

    public static void unCollect(CollectionBO collectionBO, ResponseListener responseListener) {
        String collectionInfo = JsonConverter.toJson(collectionBO);

        Map<String, String> params = new HashMap<>();
        params.put(UrlString.CollectionRequestParam, collectionInfo);
        StringPostRequest stringPostRequest = new StringPostRequest(UrlString.removeCollectionUrl,
                params,
                responseListener);
        VolleyQueueApi.getVolleyQueue().add(stringPostRequest);
    }
}
