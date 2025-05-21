package cn.itrf.usercenter.exception;

import cn.itrf.usercenter.common.BaseResponse;
import cn.itrf.usercenter.common.ErrorCode;
import cn.itrf.usercenter.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("BusinessException"+e.getMessage(), e);
        return ResultUtil.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException", e);
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
