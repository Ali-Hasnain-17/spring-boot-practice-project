package com.videoHub.videoHub.controllers;

import com.videoHub.videoHub.DTOs.AuthRequest;
import com.videoHub.videoHub.DTOs.AuthResponse;
import com.videoHub.videoHub.DTOs.RegisterRequest;
import com.videoHub.videoHub.DTOs.UserDto;
import com.videoHub.videoHub.services.UserService;
import com.videoHub.videoHub.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    @PostMapping("/register")
    public UserDto registerUser(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/authenticate")
    public AuthResponse authenticateUser(@RequestBody AuthRequest request) throws Exception {
        return userService.authenticateUser(request);
    }
}
