package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Group;
import com.example.convenientshoppingapp.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);
    Optional<Group> findById(Long id);

    Optional<Group> findByGroupLeader(Long id);
    Optional<Group> deleteGroupById(Long id);

    Page<Group> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

}
