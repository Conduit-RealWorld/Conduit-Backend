package com.conduit.application.user;

import com.conduit.application.user.data.*;
import com.conduit.domain.user.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRegisterRequestDTO request) {
        UserEntity user = userService.createUser(request);
        return ResponseEntity.ok(new UserResponseDTO(user, null));
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserAuthRequestDTO request) {
        UserResponseDTO response = userService.loginUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getUser(HttpServletRequest request, @AuthenticationPrincipal User user) {
        //get token from header
        String authHeader = request.getHeader("Authorization");
        String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;

        UserResponseDTO response = userService.getUser(user.getUsername(), token);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user")
    public ResponseEntity<UserResponseDTO> updateUser(HttpServletRequest httpServletRequestrequest, @RequestBody @Valid UserUpdateRequestDTO request, @AuthenticationPrincipal User user) {
        //get token from header
        String authHeader = httpServletRequestrequest.getHeader("Authorization");
        String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;

        UserResponseDTO response = userService.updateUser(user.getUsername(), token, request);
        return ResponseEntity.ok(response);
    }

}
