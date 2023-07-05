package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.Utils.UserUtil;
import com.example.convenientshoppingapp.dto.PaginationResponse;
import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.Group;
import com.example.convenientshoppingapp.entity.GroupMember;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.entity.auth.User;
import com.example.convenientshoppingapp.repository.FoodRepository;
import com.example.convenientshoppingapp.repository.GroupMemberRespository;
import com.example.convenientshoppingapp.repository.GroupRepository;
import com.example.convenientshoppingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final FoodRepository foodRepository;

    private final GroupMemberRespository groupMemberRespository;

    public void deleteGroupById(Long id) {
        groupRepository.deleteGroupById(id);
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Optional<Group> getGroupByGroupLeader(Long id) {
        return groupRepository.findByOwnerId(id);
    }

    public ResponseEntity<ResponseObject> getAllGroupByNameAndPaging(int page, int size, String name){
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        Page<Group> groupPage;
        groupPage = groupRepository.findAll(paging);
        List<Group> groups = groupPage.getContent();

        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(groupPage.getNumber());
        response.setTotalItem(groupPage.getTotalElements());
        response.setTotalPage(groupPage.getTotalPages());
        response.setItems(groups);
        return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "", response));
    }

    public Group getGroupByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Group not found with name: " + name));
    }


    @Modifying
    public Group save(Group group) {
        if(groupRepository.findByName(group.getName()).isPresent()){
            throw new RuntimeException("Group name already exists");
        }
        if(userRepository.findById(group.getOwnerId()).isEmpty()){
            throw new RuntimeException("Group leader not found");
        }
        log.info("Group: {}", group);
        return groupRepository.save(group);
    }

    @Modifying
    public Group update(Group group){
        Group oldGroup = groupRepository.findById(group.getId())
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + group.getId()));
        oldGroup.setName(group.getName());
        oldGroup.setOwnerId(group.getOwnerId());
        log.info("Group: {}", group);
        return groupRepository.save(oldGroup);
    }

    @Modifying
    public void addMember(String email, String nameGroup) {
//        Group group = groupRepository.findByName(nameGroup)
//                .orElseThrow(() -> new RuntimeException("Group not found with name: " + nameGroup));
//        Optional<User> user = userRepository.findByEmail(email);
//        if (group != null && user.isPresent()) {
//            if (group.getUsers() == null) {
//                group.setUsers(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
//            }
//            group.getUsers().add(user.get());
//        }
//
//        log.info("Add member to group: {}", group);
    }

    public ResponseEntity<ResponseObject> addMember(ArrayList<Long> listUserId, Long groupId) {
        // Check xem mình có phải chủ group đó không
        Long ownerId = UserUtil.getCurrentUserId();
        Boolean isGroupExist = groupRepository.existsByOwnerIdAndId(ownerId, groupId);
        if(!isGroupExist) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Nhóm không tồn tại hoặc không thuộc quyền sở hữu của bạn", ""));
        }

        ArrayList<User> listUserExist = userRepository.findByIdIn(listUserId);
        // Lấy những user id tồn tại trong CSDL
        ArrayList<Long> listIdExist = new ArrayList<>();
        listUserExist.forEach(e -> {
            // Chỉ thêm các id khác với chủ sở hữu
            if(e.getId() != ownerId) {
                listIdExist.add(e.getId());
            }
        });
        if (listIdExist.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("error", "Danh sách ID User không được để trống", ""));
        }

        // Loại bỏ những user đã có trong nhóm
        ArrayList<GroupMember> groupMembers = groupMemberRespository.findAllByUserIdInAndGroupId(listIdExist, groupId);
        List<Long> listIdJoined = groupMembers.stream().map((g) -> g.getUserId()).collect(Collectors.toList());
        var groupMember = new GroupMember().builder().groupId(groupId);

        listIdExist.forEach(id -> {
            if(!listIdJoined.contains(id)) {
                groupMember.userId(id);
                groupMemberRespository.save(groupMember.build());
            }

        });

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Thêm thành viên vào nhóm thành công ", ""));

    }

    @Modifying
    public void deleteMember(String email, String nameGroup) {
//        Group group = groupRepository.findByName(nameGroup)
//                .orElseThrow(() -> new RuntimeException("Group not found with name: " + nameGroup));
//        Optional<User> user = userRepository.findByEmail(email);
//        if (group != null && user.isPresent()) {
//            if (group.getUsers() == null) {
//                group.setUsers(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
//            }
//            group.getUsers().remove(user.get());
//        }
//        log.info("Delete member from group: {}", group);
    }

    @Modifying
    public void addLeader(String nameMember, String nameGroup) {
        Group group = groupRepository.findByName(nameGroup)
                .orElseThrow(() -> new RuntimeException("Group not found with name: " + nameGroup));
        Optional<User> user = userRepository.findByUsername(nameMember);
        if (group != null && user.isPresent()) {
            group.setOwnerId(user.get().getId());
        }

        log.info("Add leader to group: {}", group);
    }


    /**
     * Thêm food vào group
     * @param groupId
     * @param idFoods
     */
    public void addFoodsToGroup(Long groupId, Long idFoods) {
//        Group group = groupRepository.findById(groupId)
//                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
//        if (group != null) {
//            if (group.getFoods() == null) {
//                group.setFoods(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
//            }
//            group.getFoods().add(foodRepository.findById(idFoods).get());
//        }
//        groupRepository.save(group);
//        log.info("Add food to group: {}", group);
    }

    /**
     * Xóa food khỏi group
     * @param groupId
     * @param idFoods
     */
    public void removeFoodsFromGroup(Long groupId, Long idFoods) {
//        Group group = groupRepository.findById(groupId)
//                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
//        if (group != null) {
//            if (group.getFoods() == null) {
//                group.setFoods(new HashSet<>()); // Khởi tạo user là một HashSet mới nếu roles là null
//            }
//            group.getFoods().remove(foodRepository.findById(idFoods).get());
//        }
//        groupRepository.save(group);
//        log.info("Remove food to group: {}", group);
    }

    /**
     * Lấy ra danh sách food của group
     * @param groupId
     * @return
     */
    public Set<Food> getFoodsByGroupId(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
//        return group.getFoods();
        return null;
    }

}
