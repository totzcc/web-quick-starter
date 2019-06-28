package com.fxit.base.exception;

import static com.fxit.base.exception.IBizError.Level.ERROR;
import static com.fxit.base.exception.IBizError.Level.INFO;

public interface IBizError {
    int getCode();

    int getBizCode();

    String getMessage();

    Level getLevel();

    enum Level {
        INFO, WARN, ERROR
    }

    enum BizCommonError implements IBizError {
        PARAM_ERROR(400, 40001, INFO, "参数有误"),
        NEED_LOGIN(403, 40301, INFO, "需要登录"),
        AUTH_NEED(403, 40302, INFO, "权限不足"),
        RESOURCE_NOT_FOUND(404, 40401, ERROR, "数据不存在"),
        SYSTEM_ERROR(500, 50001, ERROR, "系统异常"),
        ;

        private int code;
        private int bizCode;
        private Level level;
        private String message;

        BizCommonError(int code, int bizCode, Level level, String message) {
            this.code = code;
            this.bizCode = bizCode;
            this.message = message;
            this.level = level;
        }

        public int getCode() {
            return this.code;
        }

        public int getBizCode() {
            return this.bizCode;
        }

        public String getMessage() {
            return this.message;
        }

        public Level getLevel() {
            return this.level;
        }
    }
}
