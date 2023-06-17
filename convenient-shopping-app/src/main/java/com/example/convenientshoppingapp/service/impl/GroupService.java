package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.entity.Group;
import com.example.convenientshoppingapp.entity.Users;
import com.example.convenientshoppingapp.repository.FoodRepository;
import com.example.convenientshoppingapp.repository.GroupRepository;
import com.example.convenientshoppingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final FoodRepository foodRepository;

    public void deleteGroupById(Long id) {
        groupRepository.deleteGroupById(id);
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Optional<Group> getGroupByGroupLeader(Long id) {
        return groupRepository.findByGroupLeader(id);
    }

    public Map<String, Object> getAllGroupByNameAndPaging(int page, int size, String name){
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        Page<Group> groupPage;
        List<Group> groups = new ArrayList<>();
        groupPage = groupRepository.findAllByNameContainingIgnoreCase(name, paging);
        groups = groupPage.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("groups", groups);
        response.put("currentPage", groupPage.getNumber());
        response.put("totalItems", groupPage.getTotalElements());
        response.put("totalPages", groupPage.getTotalPages());
        log.info("Get all group by name: {}", name);
        return response;
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
    public void addMember(String email, String nameGroup) {
        Group group = groupRepository.findByName(nameGroup)
                .orElseThrow(() -> new RuntimeException("Group not found with name: " + nameGroup));
        Optional<Users> user = userRepository.findByEmail(email);
        if (group != null && user.isPresent()) {
            if (group.getUsers() == null) {
                group.setUsers(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
            }
            group.getUsers().add(user.get());
        }

        log.info("Add member to group: {}", group);
    }

    @Modifying
    public void deleteMember(String email, String nameGroup) {
        Group group = groupRepository.findByName(nameGroup)
                .orElseThrow(() -> new RuntimeException("Group not found with name: " + nameGroup));
        Optional<Users> user = userRepository.findByEmail(email);
        if (group != null && user.isPresent()) {
            if (group.getUsers() == null) {
                group.setUsers(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
            }
            group.getUsers().remove(user.get());
        }
        log.info("Delete member from group: {}", group);
    }

    @Modifying
    public void addLeader(String nameMember, String nameGroup) {
        Group group = groupRepository.findByName(nameGroup)
                .orElseThrow(() -> new RuntimeException("Group not found with name: " + nameGroup));
        Optional<Users> user = userRepository.findByUsername(nameMember);
        if (group != null && user.isPresent()) {
            group.setGroupLeader(user.get().getId());
        }

        log.info("Add leader to group: {}", group);
    }


    public void addFoodsToGroup(Long id, Set<Long> idFoods) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));
        if (group != null) {
            if (group.getFoods() == null) {
                group.setFoods(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
            }
            for (Long idFood : idFoods) {
                group.getFoods().add(foodRepository.findById(idFood).get());
            }
        }
        log.info("Add food to group: {}", group);
    }

    public void removeFoodsToGroup(Long id, Set<Long> idFoods) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));
        if (group != null) {
            if (group.getFoods() == null) {
                group.setFoods(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
            }
            for (Long idFood : idFoods) {
                group.getFoods().remove(foodRepository.findById(idFood).get());
            }
        }
        log.info("Remove food to group: {}", group);
    }


}
