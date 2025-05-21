package cn.itrf.usercenter.common;

public class ResultUtil {
    public static <T> BaseResponse<T> sucess(T data){
        return new BaseResponse<>(0,"请求成功",data);
    }
    public static <T> BaseResponse<T> sucess(T data,String description){
        return new BaseResponse<>(0,"请求成功",data,description);
    }
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }
    public static BaseResponse error(int code, String message,String description){
        return new BaseResponse<>(code,message,description);
    }
    public static BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse<>(errorCode.getCode(),message,description);
    }

}
