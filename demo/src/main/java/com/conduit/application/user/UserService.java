package com.conduit.application.user;

import com.conduit.application.user.data.UserRegisterRequestDTO;
import com.conduit.application.user.data.UserUpdateRequestDTO;
import com.conduit.common.exception.AlreadyExistsException;
import com.conduit.common.exception.UserNotFoundException;
import com.conduit.domain.user.UserEntity;
import com.conduit.infrastructure.persistence.mapper.UserMapper;
import com.conduit.application.user.data.UserResponseDTO;
import com.conduit.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private UserEntity getUserByEmail(String email) {
        UserEntity user = userMapper.findByEmail(email);
        if (user == null) {throw new UserNotFoundException("User" + email + "not found");}
        return user;
    }

    public UserResponseDTO createUser(UserRegisterRequestDTO request) {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        try {
            userMapper.createUser(user);
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistsException("Email or Username already exists");
        }
        return new UserResponseDTO(user, null);
    }

    public UserResponseDTO loginUser(String email, String password) {
        UserEntity user = getUserByEmail(email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), password)
        );
        return new UserResponseDTO(user, jwtUtil.generateToken(user.getId()));
    }

    public UserResponseDTO getUser(String username, String token) {
        UserEntity user = getUserByEmail(username);
        return new UserResponseDTO(user, token);
    }

    public UserResponseDTO updateUser(String username, String token, UserUpdateRequestDTO request) {
        UserEntity user = getUserByEmail(username);
        if(!user.getEmail().equalsIgnoreCase(request.getEmail()) && userMapper.emailExists(request.getEmail()) > 0) {
            throw new RuntimeException("Email already in use");
        }
        user.setEmail(request.getEmail());
        user.setBio(request.getBio());
        user.setImage(request.getImage());
        userMapper.updateUser(user);
        return new UserResponseDTO(user, token);
    }
}