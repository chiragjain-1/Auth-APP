package com.subString.Auth.Auth_app.services;

import com.subString.Auth.Auth_app.dtos.UserDto;
import com.subString.Auth.Auth_app.entities.User;
import org.springframework.stereotype.Service;

 public interface AuthService {

    UserDto registerUser(UserDto userDto);
    UserDto loginUser(UserDto userDto);
}
