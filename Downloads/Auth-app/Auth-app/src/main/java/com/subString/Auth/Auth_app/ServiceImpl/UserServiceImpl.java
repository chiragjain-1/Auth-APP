package com.subString.Auth.Auth_app.ServiceImpl;

import com.subString.Auth.Auth_app.GlobalException.ResourceNotFoundException;
import com.subString.Auth.Auth_app.dtos.UserDto;
import com.subString.Auth.Auth_app.entities.User;
import com.subString.Auth.Auth_app.repositories.userRepository;
import com.subString.Auth.Auth_app.services.userService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements userService {

    @Autowired
    private ModelMapper modelMapper;
    private final userRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        User user1 = modelMapper.map(userDto, User.class);
        User user2 = userRepository.save(user1);
        return modelMapper.map(user2, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setImage(userDto.getImage());
        user.setProvider(userDto.getProvider());
        user.setEnabled(userDto.isEnabled());
        user.setUsername(userDto.getUsername());
        User newuser = userRepository.save(user);
        return modelMapper.map(newuser, UserDto.class);


    }


    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Resource Not found"));
        return modelMapper.map(user, UserDto.class);

    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource Not found"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user1 = (User) userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not fount"));
        return modelMapper.map(user1, UserDto.class);
    }

    @Override
    public UserDto getUserByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Override
    public UserDto getUserByEmailAndPassword(String email, String password) {
        return null;
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
     /*   // 1. Fetch all entities from the repository
        Iterable<User> users = userRepository.findAll();

        // 2. Create a fresh list INSIDE the method to avoid "singleton" data leaks
        List<UserDto> dtos = new ArrayList<>();

        // 3. Map and add each user
        for (User userEntity : users) {
            UserDto mappedDto = modelMapper.map(userEntity, UserDto.class);
            dtos.add(mappedDto);
        }

        return dtos;*/
        return userRepository.findAll()
                .stream()
                .map(users -> modelMapper.map(users, UserDto.class))
                .toList();
    }

    @Override
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "delete Success.";
    }

}
