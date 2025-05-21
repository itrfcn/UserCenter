package cn.itrf.usercenter.common;

public enum ErrorCode {
    PARAMS_ERROR("请求参数错误","",40000),
    NULL_ERROR("请求参数为空","",40001),
    NOT_LOGIN("未登录","",40100),
    NO_AUTH("无权限","",40101),
    SYSTEM_ERROR("系统内部异常","",50000),
    ;
    private final int code;
    private final String msg;
    private final String description;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }

    ErrorCode(String msg, String description, int code) {
        this.msg = msg;
        this.description = description;
        this.code = code;
    }
}
