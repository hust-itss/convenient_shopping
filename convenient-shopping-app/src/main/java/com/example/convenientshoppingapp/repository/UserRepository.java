package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<List<User>> findByUsernameContainingIgnoreCase(String name);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    ArrayList<User> findByIdIn(List<Long> listId);

    Optional<User> findUserByUsernameOrEmail(String username, String email);
}
