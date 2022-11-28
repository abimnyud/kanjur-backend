package com.enjoy.kanjurbackend.shared;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private boolean success;
    private HttpStatus status;
    private T payload;

    public BaseResponse(boolean success, HttpStatus status, T payload) {
        this.payload = payload;
        this.success = success;
        this.status = status;
    }

    public BaseResponse(boolean success, HttpStatus status) {
        this.success = success;
        this.status = status;
    }
}