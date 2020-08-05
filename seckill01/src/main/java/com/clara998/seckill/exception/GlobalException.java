package com.clara998.seckill.exception;

import com.clara998.seckill.result.CodeMsg;
import lombok.Getter;
import lombok.Setter;

/**
 * @author clara
 * @date 2020/8/1
 */
@Getter
@Setter
public class GlobalException extends RuntimeException {
    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
    }
}
