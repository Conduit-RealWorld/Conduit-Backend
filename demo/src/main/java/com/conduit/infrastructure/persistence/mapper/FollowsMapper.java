package com.conduit.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.*;
import java.util.UUID;

@Mapper
public interface FollowsMapper {
    @Select("SELECT EXISTS(SELECT 1 FROM follows WHERE follower_id = #{followerId, jdbcType=OTHER} AND followee_id = #{followeeId, jdbcType=OTHER})")
    boolean ifFollow(@Param("followerId") UUID followerId, @Param("followeeId") UUID followeeId);

    @Insert("INSERT INTO follows (id, follower_id, followee_id) VALUES (gen_random_uuid(), #{followerId, jdbcType=OTHER}, #{followeeId, jdbcType=OTHER})")
    void followUser(@Param("followerId") UUID followerId, @Param("followeeId") UUID followeeId);

    @Delete("DELETE FROM follows WHERE follower_id = #{followerId, jdbcType=OTHER} AND followee_id = #{followeeId, jdbcType=OTHER}")
    void unfollowUser(@Param("followerId") UUID followerId, @Param("followeeId") UUID followeeId);
}