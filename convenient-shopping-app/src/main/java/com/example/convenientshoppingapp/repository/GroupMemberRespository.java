package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface GroupMemberRespository extends JpaRepository<GroupMember, Long> {
    ArrayList<GroupMember> findAllByUserIdInAndGroupId(List<Long> listId, Long groupId);

    void deleteAllByUserIdInAndGroupId(List<Long> listId, Long groupId);

    Integer countByGroupId(Long groupId);

    @Query(value = "SELECT gm.groupId FROM GroupMember gm WHERE gm.userId = ?1")
    List<Long> findAllJoinedGroup(Long userId);

    Boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    void deleteByGroupIdAndUserId(Long groupId, Long userId);

}
