package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface GroupMemberRespository extends JpaRepository<GroupMember, Long> {
    ArrayList<GroupMember> findAllByUserIdInAndGroupId(List<Long> listId, Long groupId);

    void deleteAllByUserIdInAndGroupId(List<Long> listId, Long groupId);
}
