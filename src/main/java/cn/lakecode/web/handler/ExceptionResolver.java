package cn.lakecode.web.handler;

import cn.lakecode.web.exception.RException;
import cn.lakecode.web.resp.R;
import cn.lakecode.web.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class ExceptionResolver {

    @ExceptionHandler(RException.class)
    public R<?> handlerRException(RException e) {
        return R.fail(e.getResponseCode());
    }

    @ExceptionHandler(Exception.class)
    public R<?> handlerException(Exception e) {
        log.error("unKnow error=>", e);
        return R.fail(ResponseCode.SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public R<?> handlerException(NoResourceFoundException e) {
        log.error("url '{}' not exist", e.getResourcePath());
        return R.fail(ResponseCode.PAGE_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("method ={} not support", e.getMethod());
        return R.fail(ResponseCode.METHOD_ERROR);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public R<?> handleValidationExceptions(HandlerMethodValidationException ex) {
        String error = ex.getAllValidationResults().get(0).getResolvableErrors().get(0).getDefaultMessage();
        return R.fail(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String error = ex.getAllErrors().get(0).getDefaultMessage();
        return R.fail(error);
    }

}
