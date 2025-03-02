package cn.lakecode.web.enums;

public enum Status {

    NORMAL((byte) 1),

    DISABLE((byte) 0),
    ;

    private byte code;

    Status(byte code) {
        this.code = code;
    }

    public boolean test(byte code) {
        return this.code == code;
    }

}
