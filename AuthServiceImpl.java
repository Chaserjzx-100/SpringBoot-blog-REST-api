package com.springboot.blog.blogrestapi.service.impl;

import com.springboot.blog.blogrestapi.entity.Role;
import com.springboot.blog.blogrestapi.entity.User;
import com.springboot.blog.blogrestapi.exception.BlogAPIException;
import com.springboot.blog.blogrestapi.payload.LoginDto;
import com.springboot.blog.blogrestapi.payload.SignupDto;
import com.springboot.blog.blogrestapi.repository.RoleRepository;
import com.springboot.blog.blogrestapi.repository.UserRepository;
import com.springboot.blog.blogrestapi.security.JwtTokenProvider;
import com.springboot.blog.blogrestapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService{

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
       Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String signup(SignupDto signupDto) {
        //Check if 'username' already exists in database...
        if (userRepository.existsByUserName(signupDto.getUserName())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username already exists!.");
        }

        //Check if 'email' already exists in database...
        if (userRepository.existsByEmail(signupDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email already exists!.");
        }

        User user = new User();
        user.setName(signupDto.getName());
        user.setUserName(signupDto.getUserName());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "New user created!";
    }
}
