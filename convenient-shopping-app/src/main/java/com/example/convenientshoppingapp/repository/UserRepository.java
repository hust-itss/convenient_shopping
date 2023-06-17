package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<List<Users>> findByUsernameContainingIgnoreCase(String name);

    Optional<Users> findByUsername(String nameMember);

    Optional<Users> findByEmail(String email);
}
