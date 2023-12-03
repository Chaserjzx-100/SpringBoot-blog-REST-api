package com.springboot.blog.blogrestapi.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {
    private String name;
    private String userName;
    private String email;
    private String password;

}
