package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.Utils.UserUtil;
import com.example.convenientshoppingapp.dto.PaginationResponse;
import com.example.convenientshoppingapp.dto.auth.UserResponse;
import com.example.convenientshoppingapp.dto.food_history.FoodHistoryResponse;
import com.example.convenientshoppingapp.dto.group.GroupDetailResponse;
import com.example.convenientshoppingapp.dto.group.GroupResponse;
import com.example.convenientshoppingapp.entity.*;
import com.example.convenientshoppingapp.entity.auth.User;
import com.example.convenientshoppingapp.repository.*;
import com.example.convenientshoppingapp.repository.spec.MySpecification;
import com.example.convenientshoppingapp.repository.spec.SearchCriteria;
import com.example.convenientshoppingapp.repository.spec.SearchOperation;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupService {
    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final FoodRepository foodRepository;

    private final FoodHistoryRepository foodHistoryRepository;

    private final GroupMemberRespository groupMemberRespository;

    private final ModelMapper modelMapper;

    public void deleteGroupById(Long id) {
        groupRepository.deleteGroupById(id);
    }

    public ResponseEntity<ResponseObject> getGroupById(Long id) {
        Optional<Group> groupOptional =  groupRepository.findById(id);
        if(!groupOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Bạn không thể xem thông tin của nhóm này", ""));
        }

        Group group = groupOptional.get();
        GroupDetailResponse response = modelMapper.map(group, GroupDetailResponse.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", response));
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

    public ResponseEntity<ResponseObject> getAll() {
        Long userId = UserUtil.getCurrentUserId();
        List<GroupResponse> groupResponses = new ArrayList<>();
        List<Group> ownerGroup = groupRepository.findAllByOwnerId(userId);
        List<GroupResponse> groupResponseOwer = Arrays.asList(modelMapper.map(ownerGroup, GroupResponse[].class));
        // Lấy group do mình làm chủ
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();

        // Lấy group mình tham gia
        List<Long> groupJoined = groupMemberRespository.findAllJoinedGroup(userId);
        List<Group> joinGroup = groupRepository.findAllByIdIn(groupJoined);
        List<GroupResponse> groupResponseJoin = Arrays.asList(modelMapper.map(joinGroup, GroupResponse[].class));

        // Gộp 2 List
        groupResponses.addAll(groupResponseOwer);
        groupResponses.addAll(groupResponseJoin);

        groupResponses.forEach(e -> {
            //e.setOwnerName(user.getFullname());
            e.setIsOwner(e.getOwnerId() == userId);
            e.setTotalMember(groupMemberRespository.countByGroupId(e.getId()));
        });

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", groupResponses));
    }

    public Group getGroupByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Group not found with name: " + name));
    }

    public ResponseEntity<ResponseObject> getListFoods(Long groupId, String name, int page, int size) {
        Specification<FoodHistory> spec = where(null);

        Pageable paging = PageRequest.of(page, size, Sort.by(
                        Sort.Order.asc("id")
                )
        );

        MySpecification esGroupId = new MySpecification();
        esGroupId.add(new SearchCriteria("groupId", groupId, SearchOperation.EQUAL));
        spec = spec.and(esGroupId);

        Page<FoodHistory> foodHistoryPage = foodHistoryRepository.findAll(spec, paging);
        List<FoodHistory> foods;
        foods = foodHistoryPage.getContent();
        List<FoodHistoryResponse> foodHistoryResponses = Arrays.asList(modelMapper.map(foods, FoodHistoryResponse[].class));
        PaginationResponse response = new PaginationResponse();
        response.setItems(foodHistoryResponses);
        response.setCurrentPage(foodHistoryPage.getNumber());
        response.setTotalItem(foodHistoryPage.getTotalElements());
        response.setTotalPage(foodHistoryPage.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "", response));
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

    public ResponseEntity<ResponseObject> addMember(String username, Long groupId) {
        // Check xem mình có phải chủ group đó không
        Long ownerId = UserUtil.getCurrentUserId();
        Boolean isGroupExist = groupRepository.existsByOwnerIdAndId(ownerId, groupId);
        if(!isGroupExist) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Nhóm không tồn tại hoặc không thuộc quyền sở hữu của bạn", ""));
        }

        Optional<User> userOptional = userRepository.findUserByUsernameOrEmail(username, username);
        // Lấy những user id tồn tại trong CSDL
        if(!userOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Người dùng này không tồn tại trong hệ thống", ""));
        }

        User user = userOptional.get();
        if(user.getId() == ownerId) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("error", "Bạn đã ở trong nhóm", ""));
        }

        // Loại bỏ những user đã có trong nhóm
        if(groupMemberRespository.existsByGroupIdAndUserId(groupId, user.getId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("error", "Người dùng này ở trong nhóm", ""));
        }

        GroupMember groupMember = new GroupMember().builder().groupId(groupId).userId(user.getId()).build();
        groupMemberRespository.save(groupMember);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Thêm thành viên vào nhóm thành công ", userResponse));

    }

    public ResponseEntity<ResponseObject> removeMember(Long userId, Long groupId) {
        // Check xem mình có phải chủ group đó không
        Long ownerId = UserUtil.getCurrentUserId();
        Boolean isGroupExist = groupRepository.existsByOwnerIdAndId(ownerId, groupId);
        if(!isGroupExist) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Nhóm không tồn tại hoặc không thuộc quyền sở hữu của bạn", ""));
        }

        if(!userRepository.existsById(userId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Người dùng này không tồn tại trong hệ thống", ""));
        }

        if(!groupMemberRespository.existsByGroupIdAndUserId(groupId, userId)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("error", "Người dùng này không có ở trong nhóm này", ""));
        }

        groupMemberRespository.deleteByGroupIdAndUserId(groupId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Xóa người dùng khỏi nhóm thành công", ""));
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
