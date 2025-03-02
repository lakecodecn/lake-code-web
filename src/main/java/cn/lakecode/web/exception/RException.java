package cn.lakecode.web.exception;

import cn.lakecode.web.resp.ResponseCode;

public class RException extends RuntimeException {

    private final ResponseCode responseCode;

    public RException(ResponseCode responseCode) {
        super(responseCode.getDesc());
        this.responseCode = responseCode;
    }

    public RException(String msg, Throwable e) {
        super(msg, e);
        this.responseCode = ResponseCode.FAIL.setDesc(msg);
    }

    public RException(String msg) {
        super(msg);
        this.responseCode = ResponseCode.FAIL.setDesc(msg);
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
