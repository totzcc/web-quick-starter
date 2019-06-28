package com.fxit.base.exception;

import lombok.Data;

@Data
public class ExceptionRes {
    private int code;
    private int bizCode;
    private String message;
    private String detailMessage;
    private IBizError.Level level;

    ExceptionRes(BizException exception) {
        this.code = exception.getCode();
        this.bizCode = exception.getBizCode();
        this.message = exception.getMessage();
        this.level = exception.getLevel();
    }

}