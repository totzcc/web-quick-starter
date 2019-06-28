package com.fxit.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ExceptionRes handler(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        String href = request.getHeader("href");
        String referrer = request.getHeader("Referrer");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        ExceptionRes res;
        if (exception instanceof BizException) {
            res = new ExceptionRes((BizException) exception);
        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) exception;
            ObjectError checkError = validException.getBindingResult().getAllErrors().get(0);
            String errorMsg = String.format("[%s] %s", checkError.getObjectName(), checkError.getDefaultMessage());
            if (checkError instanceof FieldError) {
                FieldError checkError1 = (FieldError) checkError;
                errorMsg = String.format("[%s] %s", checkError1.getField(), checkError1.getDefaultMessage());
            }
            res = new ExceptionRes(400, 400, "参数有误: " + errorMsg, exception.getMessage() + "", IBizError.Level.INFO);
            res = new ExceptionRes(new BizException(IBizError.BizCommonError.PARAM_ERROR, errorMsg, exception));
        } else {
            res = new ExceptionRes(500, 500, "系统繁忙", exception.getMessage(), IBizError.Level.ERROR);
        }
        response.setStatus(res.getCode());
        String method = request.getMethod();
        String url = request.getRequestURI();
        String queryString = request.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            url += "?" + queryString;
        }
        String errorParams = String.format("(%s - %s - %s - %s)", method, url, href, referrer);
        if (exception instanceof BizException) {
            BizException bizException = (BizException) exception;
            Throwable throwable = bizException.getThrowable();
            switch (bizException.getLevel()) {
                case INFO:
                    log.info(errorParams + exception.getMessage());
                    break;
                case WARN:
                    log.warn(errorParams + exception.getMessage(), throwable);
                    break;
                default:
                    log.error(errorParams + exception.getMessage(), throwable);
                    break;
            }
        } else {
            log.error(errorParams, exception);
        }
        return res;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
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
}
