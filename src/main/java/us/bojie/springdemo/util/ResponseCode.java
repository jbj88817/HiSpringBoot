package us.bojie.springdemo.util;

public enum ResponseCode {
    RC_SUCCESS(0, "SUCCESS"),
    RC_ACCOUNT_INVALID(5001, "账号不存在"),
    RC_PWD_INVALID(5002, "密码错误"),
    RC_NEED_LOGIN(5003, "请先登录"),
    RC_USER_FORBID(5004, "用户非法");


    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
