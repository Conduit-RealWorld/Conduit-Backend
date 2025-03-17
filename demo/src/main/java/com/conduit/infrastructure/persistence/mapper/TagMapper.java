package com.conduit.infrastructure.persistence.mapper;

import com.conduit.domain.article.TagEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.UUID;

@Mapper
public interface TagMapper {
    @Select("SELECT EXISTS(SELECT 1 FROM tag WHERE tag_name = #{tagName})")
    boolean exists(String tagName);

    @Select("SELECT tag_id FROM tag WHERE tag_name = #{tagName}")
    UUID getByTagName(String tagName);

    @Select("SELECT tag_name FROM tag WHERE tag_id = #{tagId}")
    String getById(UUID tagId);

    @Insert("INSERT INTO tag (tag_id, tag_name) VALUES (#{tagId}, #{tagName})")
    void create(TagEntity tag);

    @Select("Select tag_name From tag")
    List<String> getAllTags();
}
