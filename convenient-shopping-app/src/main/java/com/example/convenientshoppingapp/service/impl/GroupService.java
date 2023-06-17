package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.entity.Group;
import com.example.convenientshoppingapp.entity.Users;
import com.example.convenientshoppingapp.repository.GroupRepository;
import com.example.convenientshoppingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    public void deleteGroupById(Long id) {
        groupRepository.deleteGroupById(id);
    }


    @Modifying
    public Group save(Group group) {
        if(groupRepository.findByName(group.getName()).isPresent()){
            throw new RuntimeException("Group name already exists");
        }
        log.info("Group: {}", group);
        return groupRepository.save(group);
    }

    @Modifying
    public Group update(Group group){
        Group oldGroup = groupRepository.findById(group.getId())
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + group.getId()));
        oldGroup.setName(group.getName());
        oldGroup.setGroupLeader(group.getGroupLeader());
        log.info("Group: {}", group);
        return groupRepository.save(oldGroup);
    }

    @Modifying
    public void addMember(String nameNember, String nameGroup) {
        Group group = groupRepository.findByName(nameGroup)
                .orElseThrow(() -> new RuntimeException("Group not found with name: " + nameGroup));
        Optional<Users> user = userRepository.findByUsername(nameNember);
        if (group != null && user.isPresent()) {
            if (group.getUsers() == null) {
                group.setUsers(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
            }
            group.getUsers().add(user.get());
        }
    }

    @Modifying
    public void deleteMember(String nameMember, String nameGroup) {
        Group group = groupRepository.findByName(nameGroup)
                .orElseThrow(() -> new RuntimeException("Group not found with name: " + nameGroup));
        Optional<Users> user = userRepository.findByUsername(nameMember);
        if (group != null && user.isPresent()) {
            if (group.getUsers() == null) {
                group.setUsers(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
            }
            group.getUsers().remove(user.get());
        }
    }
}
