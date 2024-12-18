package com.project.gds.share;

import lombok.Getter;

@Getter
public class ResponseBody <T> {

    private final String statusMessage;
    private final String statusCode;
    private final T data;

    public ResponseBody(StatusCode statusCode, T data){
        this.statusMessage = statusCode.getStatusMessage();
        this.statusCode = statusCode.getStatusCode();
        this.data = data;
    }
}
