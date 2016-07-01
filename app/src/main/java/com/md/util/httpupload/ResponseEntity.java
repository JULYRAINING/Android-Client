package com.md.util.httpupload;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by SECONDHEAVEN on 2016/2/11.
 */
@HttpResponse(parser = ResultParser.class)
public class ResponseEntity {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
