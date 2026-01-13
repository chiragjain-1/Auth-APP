package com.subString.Auth.Auth_app.repositories;

import com.subString.Auth.Auth_app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface userRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Object> findByUsername(String username);
}
