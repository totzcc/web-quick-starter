package com.fxit.base.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BizException extends RuntimeException {
    private int code;
    private int bizCode;
    private String bizCodeName;
    private IBizError.Level level;
    private String message;
    private Throwable throwable;

    public BizException(IBizError error) {
        this(error.getCode(), error.getBizCode(), error.getMessage(), error.getLevel(), new Throwable());
    }

    public BizException(IBizError error, String extendMessage) {
        this(error.getCode(), error.getBizCode(), error.getBizCodeName(), error.getMessage() + ": " + extendMessage, error.getLevel(), new Throwable());
    }

    public BizException(IBizError error, Throwable throwable) {
        this(error.getCode(), error.getBizCode(), error.getBizCodeName(), error.getMessage(), error.getLevel(), throwable);
    }

    public BizException(IBizError error, String extendMessage, Throwable throwable) {
        this(error.getCode(), error.getBizCode(), error.getBizCodeName(), error.getMessage(), error.getLevel(), throwable);
    }

    public BizException(int code, int bizCode, String message, IBizError.Level level, Throwable throwable) {
        this(code, bizCode, "", message, level, throwable);
    }

    public BizException(int code, int bizCode, String bizCodeName, String message, IBizError.Level level, Throwable throwable) {
        this.code = code;
        this.bizCodeName = bizCodeName;
        this.bizCode = bizCode;
        this.message = message;
        this.throwable = throwable;
        this.level = level;
    }
}
