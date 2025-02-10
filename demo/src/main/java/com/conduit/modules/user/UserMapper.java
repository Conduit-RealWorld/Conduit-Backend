package com.conduit.modules.user;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE email = #{email}")
    UserEntity findByEmail(@Param("email") String email);

    @Select("SELECT * FROM users WHERE username = #{username}")
    UserEntity findByUsername(@Param("username") String username);


//    @Insert("INSERT INTO users(username, email, password) VALUES(#{username}, #{email}, #{password}")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
//    void createUser(UserEntity user);

    @Insert("INSERT INTO users(username, email, password) VALUES (#{username}, #{email}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @SelectKey(statement = "SELECT lastval()", keyProperty = "id", before = false, resultType = Long.class)
    void createUser(UserEntity user);


}
