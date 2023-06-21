package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.auth.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<List<Users>> findByUsernameContainingIgnoreCase(String name);

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    ArrayList<Users> findByIdIn(List<Long> listId);
}
