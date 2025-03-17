package com.conduit.application.tag;


import com.conduit.application.profile.data.UserProfileResponseDTO;
import com.conduit.application.tag.data.TagResponseDTO;
import com.conduit.infrastructure.persistence.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagMapper tagMapper;

    public TagResponseDTO getTags() {
        return new TagResponseDTO(tagMapper.getAllTags());
    }
}
