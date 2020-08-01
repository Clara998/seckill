package com.clara998.seckill.mapper;

import com.clara998.seckill.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author clara
 * @date 2020/7/27
 */


//这里是一个接口~
@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User getById(@Param("id") int id);

    @Insert("insert into user(id, name) values (#{id}, #{name})")
    int insert(User user);
}
