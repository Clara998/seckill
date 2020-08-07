package com.clara998.seckill.vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author clara
 * @date 2020/8/1
 */
@Setter
@Getter
public class LoginVo {
    @NotNull
    private String mobile;
    @NotNull
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
