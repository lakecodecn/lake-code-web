package cn.lakecode.web.resp;


import cn.lakecode.web.constant.Constants;
import lombok.Data;
import org.slf4j.MDC;

@Data
public class R<T> {

    private Integer errCode;

    private String errMsg;

    private String traceId;

    private T data;


    public R(T data) {
        this(ResponseCode.SUCCESS, data);
    }

    public R(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.traceId = MDC.get(Constants.TRACE_ID);
    }

    public R(ResponseCode responseCode) {
        this.errCode = responseCode.getCode();
        this.errMsg = responseCode.getDesc();
        this.traceId = MDC.get(Constants.TRACE_ID);
    }

    public R(ResponseCode responseCode, T data) {
        this.errCode = responseCode.getCode();
        this.errMsg = responseCode.getDesc();
        this.data = data;
        this.traceId = MDC.get(Constants.TRACE_ID);
    }

    public static R<String> ok() {
        return new R<>(ResponseCode.SUCCESS);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(data);
    }

    public static R<String> fail(String msg) {
        return new R<>(ResponseCode.FAIL.getCode(), msg);
    }

    public static R<String> fail() {
        return new R<>(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getDesc());
    }

    public static R<String> fail(ResponseCode responseCode) {
        return new R<>(responseCode.getCode(), responseCode.getDesc());
    }

}
