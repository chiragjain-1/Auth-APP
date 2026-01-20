package com.subString.Auth.Auth_app.Security;

import com.subString.Auth.Auth_app.repositories.userRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{
    private final userRepository userrepository;

    @Override
    public UserDetails loadUserByUsername( String username) throws UsernameNotFoundException {

        return userrepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));


    }
}


