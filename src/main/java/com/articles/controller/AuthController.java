package com.articles.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.articles.dto.AuthRequest;
import com.articles.dto.AuthResponse;
import com.articles.dto.RefreshTokenRequest;
import com.articles.dto.UserProfileResponse;
import com.articles.dto.UserRegisterRequest;
import com.articles.dto.UserRegisterResponse;
import com.articles.model.User;
import com.articles.security.JwtUtil;
import com.articles.security.service.CustomUserDetailsService;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
	
	@Autowired
    private  AuthenticationManager authenticationManager;
	
    @Autowired
    private  JwtUtil jwtUtil;
    
    @Autowired
    private  CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(@RequestBody UserRegisterRequest request) {
        User newUser = userDetailsService.registerUser(request);
        UserRegisterResponse response = new UserRegisterResponse(
                newUser.getId(), newUser.getUsername(), "User registered successfully"
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = jwtUtil.generateToken(userDetails.getUsername(), false);
        String refreshToken = jwtUtil.generateToken(userDetails.getUsername(), true);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        String username = jwtUtil.extractUsername(request.getRefreshToken(), true);

        if (!jwtUtil.validateToken(request.getRefreshToken(), true)) {
            return ResponseEntity.status(401).body(null);
        }
        String newAccessToken = jwtUtil.generateToken(username, false);
        return ResponseEntity.ok(new AuthResponse(newAccessToken, request.getRefreshToken()));
    }
    
    
    @GetMapping("/get-profile")
    public UserProfileResponse getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.getUserByUsername(userDetails.getUsername());
        return new UserProfileResponse(user.getId(), user.getUsername(), user.getRoles());
    }
}
