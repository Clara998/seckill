package com.clara998.seckill.mapper;

import com.clara998.seckill.bean.User;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

/**
 * @author clara
 * @date 2020/7/27
 */


//这里是一个接口~
@Mapper
public interface UserMapper {

    @Select("select * from sk_user where id = #{id}")
    User getById(@Param("id") long id);

    //更新密码
    @Update("update sk_user set password = #{password} where id = #{id}")
    public void update(User toBeUpdate);

}
