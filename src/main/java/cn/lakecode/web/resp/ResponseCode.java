package cn.lakecode.web.resp;

public enum ResponseCode {

    SUCCESS(0, "success"),

    FAIL(-1, "fail"),

    SERVER_ERROR(500, "服务维护中"),

    PARAMS_ERROR(400, "参数异常"),

    DATA_ERROR(400, "数据错误"),

    PAGE_ERROR(404, "请求地址不存在"),

    METHOD_ERROR(404, "请求方法不支持"),

    AUTH_BAD(401, "请先登录"),

    AUTH_INVALID(401, "token已失效,请重新登录"),

    PERMISSION(403, "无访问权限"),

    AUTH_LIMIT(402, "服务维护中，联系管理员"),

    GOODS_NOT_EXIST(1000, "商品不存在"),

    GOODS_EXIST(1001, "商品已存在"),

    DATA_EXIST(3000, "数据已存在"),

    DATA_NOT_EXIST(3001, "数据不存在"),

    LIMIT_UPDATE(2000, "该参数禁止修改");

    private int code;

    private String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public ResponseCode setCode(int code) {
        this.code = code;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public ResponseCode setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public boolean test(Integer code) {
        for (ResponseCode value : values()) {
            if (value.getCode() == code) {
                return true;
            }
        }
        return false;
    }
}
