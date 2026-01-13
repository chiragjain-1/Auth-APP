package com.subString.Auth.Auth_app.services;

import com.subString.Auth.Auth_app.dtos.UserDto;

import javax.swing.text.html.Option;
import java.util.UUID;

public interface userService {

    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto,Long id );
    UserDto getUserByEmail(String username);
    UserDto getUserById(Long id);
    UserDto getUserByUsername(String username);
    UserDto getUserByUsernameAndPassword(String username, String password);
    UserDto getUserByEmailAndPassword(String email, String password);
    Iterable<UserDto> getAllUsers();
    String deleteUser(Long id);


   }
