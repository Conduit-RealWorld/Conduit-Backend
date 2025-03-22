package com.conduit.infrastructure.persistence.mapper;

import com.conduit.domain.user.UserEntity;
import org.apache.ibatis.annotations.*;
import java.util.UUID;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE email = #{email}")
    UserEntity findByEmail(@Param("email") String email);

    @Select("SELECT * FROM users WHERE username = #{username}")
    UserEntity findByUsername(@Param("username") String username);

    @Select("SELECT * FROM users WHERE id = #{id, jdbcType=OTHER}")
    UserEntity findByUserId(@Param("id") UUID id);

    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int emailExists(@Param("email") String email);

    @Insert("INSERT INTO users (id, username, email, password) VALUES (#{id, jdbcType=OTHER}, #{username}, #{email}, #{password})")
    void createUser(UserEntity user);


    @Update("""
        UPDATE users\s
        SET username = #{username},\s
            password = #{password},\s
            bio = #{bio},\s
            image = #{image},\s
            email = #{email}
        WHERE id = #{id, jdbcType=OTHER}
   \s""")
    void updateUser(UserEntity user);
}