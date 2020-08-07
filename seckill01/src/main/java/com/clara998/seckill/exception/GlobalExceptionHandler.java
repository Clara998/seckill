package com.clara998.seckill.exception;

import com.clara998.seckill.result.CodeMsg;
import com.clara998.seckill.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author clara
 * @date 2020/8/7
 */

// Controller层的异常处理
// https://blog.csdn.net/kinginblue/article/details/70186586
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class) //拦截所有异常
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        //在命令行打印异常信息在程序中出错的位置及原因
        e.printStackTrace();
        if (e instanceof GlobalException) {
            //强制类型转换
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCodeMsg());
            //端口被占用
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            //绑定错误返回很多错误，是一个错误列表，只需要第一个错误
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            //给状态码填充参数
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
