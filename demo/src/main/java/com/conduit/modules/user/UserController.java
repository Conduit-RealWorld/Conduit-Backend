package com.conduit.modules.user;

import com.conduit.modules.user.dto.request.UserRequestDTO;
import com.conduit.modules.user.dto.response.UserProfileResponseDTO;
import com.conduit.modules.user.dto.response.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
        System.out.println(request);
        UserEntity user = userService.createUser(request);
        return ResponseEntity.ok(new UserResponseDTO(
                user.getUsername(),
                user.getEmail(),
                null,
                user.getBio(),
                user.getImage()));
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO response = userService.loginUser(request.getUser().getEmail(), request.getUser().getPassword().orElse(null));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getUser(HttpServletRequest request, @AuthenticationPrincipal User user) {
        String authHeader = request.getHeader("Authorization");
        String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : "No JWT";
        UserResponseDTO response = userService.getUser(user.getUsername(), token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profiles/{username}")
    public ResponseEntity<UserProfileResponseDTO> getProfile(@PathVariable String username) {
        UserProfileResponseDTO response = userService.getProfile(username);
        return ResponseEntity.ok(response);
    }
}
