package com.subString.Auth.Auth_app.ServiceImpl;

import com.subString.Auth.Auth_app.dtos.UserDto;
import com.subString.Auth.Auth_app.entities.User;
import com.subString.Auth.Auth_app.repositories.userRepository;
import com.subString.Auth.Auth_app.services.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto registerUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return  modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public UserDto loginUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
           if(user.getUsername().equals(userDto.getUsername()) && user.getPassword().equals(userDto.getPassword())){}
        return null;
    }
}
