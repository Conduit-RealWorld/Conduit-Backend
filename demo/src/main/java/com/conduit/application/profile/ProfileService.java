package com.conduit.application.profile;

import com.conduit.application.profile.data.UserProfileResponseDTO;
import com.conduit.common.exception.FollowNotAllowedException;
import com.conduit.common.exception.UserNotFoundException;
import com.conduit.domain.user.UserEntity;
import com.conduit.infrastructure.persistence.mapper.FollowsMapper;
import com.conduit.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserMapper userMapper;
    private final FollowsMapper followsMapper;

    public UserProfileResponseDTO getProfile(String username, String currentUser) {
        if (currentUser == null) {currentUser = username; }
        UserEntity user = userMapper.findByUsername(username);
        if (user == null) throw new UserNotFoundException("User" + username + " not found");
        UserEntity currentUserEntity = userMapper.findByUsername(currentUser);

        if(username.equals(currentUser)) return new UserProfileResponseDTO(user, true);
        boolean following = followsMapper.ifFollow(currentUserEntity.getId(), user.getId());
        return new UserProfileResponseDTO(user, following);
    }

    public UserProfileResponseDTO followUser(String followee, String follower) {
        if(follower.equals(followee)) throw new FollowNotAllowedException(follower);
        UserEntity followerUser = userMapper.findByUsername(follower);
        UserEntity followeeUser = userMapper.findByUsername(followee);
        if (followeeUser == null) throw new UserNotFoundException("User" + followee + " not found");
        if(!followsMapper.ifFollow(followerUser.getId(), followeeUser.getId())) {
            followsMapper.followUser(followerUser.getId(), followeeUser.getId());
        }
        return new UserProfileResponseDTO(followeeUser, true);
    }

    public UserProfileResponseDTO unfollowUser(String followee, String follower) {
        if(follower.equals(followee)) throw new FollowNotAllowedException(follower);
        UserEntity followerUser = userMapper.findByUsername(follower);
        UserEntity followeeUser = userMapper.findByUsername(followee);
        if (followeeUser == null) throw new UserNotFoundException("User" + followee + " not found");
        if(followsMapper.ifFollow(followerUser.getId(), followeeUser.getId())) {
            followsMapper.unfollowUser(followerUser.getId(), followeeUser.getId());
        }
        return new UserProfileResponseDTO(followeeUser, false);
    }
}
