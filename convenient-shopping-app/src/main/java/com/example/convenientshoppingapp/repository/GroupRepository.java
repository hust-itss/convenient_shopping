package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);
    Optional<Group> findById(Long id);

    Optional<Group> findByOwnerId(Long id);
    Optional<Group> findByOwnerIdAndId(Long ownerId, Long id);

    List<Group> findAllByOwnerId(Long id);
    Optional<Group> deleteGroupById(Long id);

    Page<Group> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    Boolean existsByOwnerIdAndId(Long userId, Long groupId);

    List<Group> findAllByIdIn(List<Long> listId);
}
