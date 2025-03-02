package cn.lakecode.web.manager.plat.bo;

public enum PlatType {

    WX(1);

    private Integer code;

    PlatType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
