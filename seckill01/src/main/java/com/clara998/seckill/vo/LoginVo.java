package com.clara998.seckill.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author clara
 * @date 2020/8/1
 */
@Setter
@Getter
public class LoginVo {
    private String mobile;
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
