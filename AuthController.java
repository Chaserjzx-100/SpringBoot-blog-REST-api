package com.springboot.blog.blogrestapi.controller;

import com.springboot.blog.blogrestapi.payload.JWTAuthResponse;
import com.springboot.blog.blogrestapi.payload.LoginDto;
import com.springboot.blog.blogrestapi.payload.SignupDto;
import com.springboot.blog.blogrestapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Build login REST API
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login (@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
    //Build signup API
    //*****CREATES NEW USER****
    @PostMapping(value = {"/signUp", "/register"})
    public ResponseEntity<String> signup(@RequestBody SignupDto signupDto){
        String response = authService.signup(signupDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
