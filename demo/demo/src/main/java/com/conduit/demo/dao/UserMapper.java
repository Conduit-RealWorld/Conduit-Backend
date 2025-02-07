package com.conduit.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import com.conduit.demo.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(@Param("id") Long id);
}
