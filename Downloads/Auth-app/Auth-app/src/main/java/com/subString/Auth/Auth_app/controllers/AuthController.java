package com.subString.Auth.Auth_app.controllers;

import com.subString.Auth.Auth_app.ServiceImpl.AuthServiceImpl;
import com.subString.Auth.Auth_app.dtos.UserDto;
import com.subString.Auth.Auth_app.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
 @Autowired
private AuthServiceImpl authService;

@PostMapping("/register")
public ResponseEntity<UserDto> signup(@RequestBody UserDto userDto){
    return new ResponseEntity<>(authService.registerUser(userDto), HttpStatus.CREATED);
}

@PostMapping("login")
public ResponseEntity<UserDto> login(@RequestBody UserDto userDto){
    return new ResponseEntity<>(authService.loginUser(userDto), HttpStatus.FOUND);
}

}
