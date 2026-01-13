package com.subString.Auth.Auth_app.dtos;

import com.subString.Auth.Auth_app.entities.Provider;
import com.subString.Auth.Auth_app.entities.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private Long userId;

    private String username;
    private String password;
    private String email;
    private String image;
    private boolean enabled = true;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Provider provider = Provider.LOCAL;
    private Set<RoleDto> roles = new HashSet<>();
}
