package com.conduit.modules.user;

import com.conduit.modules.user.dto.request.UserRequestDTO;
import com.conduit.modules.user.dto.response.UserProfileResponseDTO;
import com.conduit.modules.user.dto.response.UserResponseDTO;
import com.conduit.utils.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserEntity createUser(UserRequestDTO request) {
        if(userMapper.findByEmail(request.getUser().getEmail()) != null) {
            throw new RuntimeException("Email already in use");
        }
        UserEntity user = new UserEntity();
        user.setUsername(request.getUser().getUsername().orElse(null));
        user.setEmail(request.getUser().getEmail());
        user.setPassword(request.getUser().getPassword().orElse(null));

        userMapper.createUser(user);
        return user;
    }

    public UserResponseDTO loginUser(String email, String password) {
        UserEntity user = userMapper.findByEmail(email);
        if(user == null) {
            throw new RuntimeException("User not found");
        }
        if(!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        return new UserResponseDTO(
                user.getUsername(),
                user.getEmail(),
                jwtUtil.generateToken(user.getEmail()),
                user.getBio(),
                user.getImage());
    }

    public UserResponseDTO getUser(String email, String token) {
        UserEntity user = userMapper.findByEmail(email);
        return new UserResponseDTO(
                user.getUsername(),
                user.getEmail(),
                token,
                user.getBio(),
                user.getImage());
    }

    public UserProfileResponseDTO getProfile(String username) {
        UserEntity user = userMapper.findByUsername(username);
        return new UserProfileResponseDTO(
                user.getUsername(),
                user.getBio(),
                user.getImage(),
                false
        );
    }

}
