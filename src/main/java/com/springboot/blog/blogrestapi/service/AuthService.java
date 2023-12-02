package com.springboot.blog.blogrestapi.service;

import com.springboot.blog.blogrestapi.payload.LoginDto;
import com.springboot.blog.blogrestapi.payload.SignupDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String signup(SignupDto signupDto);
}
