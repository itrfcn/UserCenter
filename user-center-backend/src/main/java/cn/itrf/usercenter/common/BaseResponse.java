package cn.itrf.usercenter.common;

import lombok.Data;

import java.io.Serializable;
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -6570039582627387993L;

    private int code;
    private String msg;
    private T data;
    private String description;

    public BaseResponse(int code, String msg, T data, String description) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.description = description;
    }
    public BaseResponse(int code, String msg, String description) {
        this(code, msg, null, description);
    }

    public BaseResponse(int code, String msg, T data){
        this(code,msg,data,"");
    }
    public BaseResponse(int code, String msg){
        this(code,msg,null,"");
    }
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),errorCode.getMsg(),null,errorCode.getDescription());
    }


}
