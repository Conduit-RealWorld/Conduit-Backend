package com.conduit.application.profile;

import com.conduit.application.profile.data.UserProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/celeb_{username}")
    public ResponseEntity<UserProfileResponseDTO> getProfile(@PathVariable String username, @AuthenticationPrincipal User user) {
        UserProfileResponseDTO response = profileService.getProfile(username, user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/celeb_{username}/follow")
    public ResponseEntity<UserProfileResponseDTO> follow(@PathVariable String username, @AuthenticationPrincipal User user) {
        UserProfileResponseDTO response = profileService.followUser(username, user.getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/celeb_{username}/follow")
    public ResponseEntity<UserProfileResponseDTO> unfollow(@PathVariable String username, @AuthenticationPrincipal User user) {
        UserProfileResponseDTO response = profileService.unfollowUser(username, user.getUsername());
        return ResponseEntity.ok(response);
    }
}