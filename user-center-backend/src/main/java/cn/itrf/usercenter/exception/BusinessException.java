package cn.itrf.usercenter.exception;

import cn.itrf.usercenter.common.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 7607604514046781488L;
    private final int code;
    private final String description;

    public BusinessException(int code, String msg, String description) {
        super(msg);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = description;
    }

}
