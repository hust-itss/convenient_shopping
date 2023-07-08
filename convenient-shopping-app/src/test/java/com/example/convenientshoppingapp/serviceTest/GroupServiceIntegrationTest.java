package com.example.convenientshoppingapp.serviceTest;

import com.example.convenientshoppingapp.dto.group.CreateGroupRequest;
import com.example.convenientshoppingapp.dto.group.GroupDetailResponse;
import com.example.convenientshoppingapp.dto.group.GroupResponse;
import com.example.convenientshoppingapp.dto.group.UpdateGroupRequest;
import com.example.convenientshoppingapp.entity.Group;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.entity.auth.User;
import com.example.convenientshoppingapp.repository.FoodHistoryRepository;
import com.example.convenientshoppingapp.repository.GroupRepository;
import com.example.convenientshoppingapp.repository.UserRepository;
import com.example.convenientshoppingapp.service.impl.GroupService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GroupServiceIntegrationTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupService groupService;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FoodHistoryRepository foodHistoryRepository;



// ...

    @Test
    public void saveGroup_ShouldSaveGroup() {
        // Arrange
        CreateGroupRequest groupRequest = new CreateGroupRequest();
        groupRequest.setName("Test Group");

        Group group = new Group();
        group.setId(1L);
        group.setName("Test Group");
        group.setOwnerId(123L);

        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setId(1L);
        groupResponse.setOwnerId(123L);
        groupResponse.setName("Test Group");
        groupResponse.setIsOwner(true);
        groupResponse.setTotalMember(0);

        // Tạo giá trị id cho Owner
        Long ownerId = 123L;

        // Tạo đối tượng User giả lập
        User user = new User();
        user.setId(ownerId);
        user.setUsername("user123");

        // Thiết lập thông tin xác thực cho đối tượng User giả lập
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

        // Thiết lập thông tin xác thực vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Thiết lập behavior cho mock userRepository
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(groupRepository.save(group)).thenReturn(group);
        when(modelMapper.map(group, GroupResponse.class)).thenReturn(groupResponse);

        // Act
        ResponseEntity<ResponseObject> response = groupService.save(groupRequest);
        GroupResponse actualGroupResponse = (GroupResponse) response.getBody().getData();

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Tạo nhóm thành công", response.getBody().getMessage());
        assertEquals(groupResponse.getOwnerId(), actualGroupResponse.getOwnerId());
        assertEquals(groupResponse.getName(), actualGroupResponse.getName());
        assertEquals(groupResponse.getIsOwner(), actualGroupResponse.getIsOwner());
        assertEquals(groupResponse.getTotalMember(), actualGroupResponse.getTotalMember());

    }



    @Test
    public void deleteGroupById_ShouldDeleteGroup() {
        Long groupId = 2L;

        // Khi gọi phương thức deleteGroupById
        groupService.deleteGroupById(groupId);

        // Xác nhận rằng phương thức deleteGroupById của groupRepository đã được gọi với đúng tham số
        verify(groupRepository).deleteGroupById(groupId);
    }

    @Test
    public void getGroupById_GroupExists() {
        // Arrange
        Long groupId = 1L;

        Group group = new Group();
        group.setId(groupId);
        group.setName("Test Group");
        group.setOwnerId(123L);

        GroupDetailResponse groupDetailResponse = new GroupDetailResponse();
        groupDetailResponse.setId(groupId);
        groupDetailResponse.setName("Test Group");
        groupDetailResponse.setIsOwner(true);
        // Set other properties if necessary

        User user = new User();
        user.setId(123L);
        user.setUsername("user123");

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(modelMapper.map(group, GroupDetailResponse.class)).thenReturn(groupDetailResponse);

        // Act
        ResponseEntity<ResponseObject> response = groupService.getGroupById(groupId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("", response.getBody().getMessage());

        GroupDetailResponse actualGroupDetailResponse = (GroupDetailResponse) response.getBody().getData();
        assertEquals(groupId, actualGroupDetailResponse.getId());
        assertEquals("Test Group", actualGroupDetailResponse.getName());
        assertEquals(true, actualGroupDetailResponse.getIsOwner());
        // Perform additional assertions for other properties

        // Verify
        verify(groupRepository).findById(groupId);
        verify(modelMapper).map(group, GroupDetailResponse.class);
    }

    @Test
    public void getGroupById_GroupNotExists() {
        // Arrange
        Long groupId = 1L;

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ResponseObject> response = groupService.getGroupById(groupId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Bạn không thể xem thông tin của nhóm này", response.getBody().getMessage());
        assertEquals("", response.getBody().getData());

        // Verify
        verify(groupRepository).findById(groupId);
    }


    @Test
    public void getGroupById_NonExistingGroup_ReturnsNotFoundError() {
        // Arrange
        Long groupId = 1L;

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ResponseObject> response = groupService.getGroupById(groupId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Bạn không thể xem thông tin của nhóm này", response.getBody().getMessage());
        assertEquals("", response.getBody().getData());

        verify(groupRepository).findById(groupId);
    }

    @Test
    public void update_Successful() {
        // Arrange
        Long groupId = 1L;
        String updatedGroupName = "Updated Group Name";

        UpdateGroupRequest updateGroupRequest = new UpdateGroupRequest();
        updateGroupRequest.setName(updatedGroupName);

        Long userId = 123L;

        Group oldGroup = new Group();
        oldGroup.setId(groupId);
        oldGroup.setName("Old Group Name");
        oldGroup.setOwnerId(userId);

        Optional<Group> groupOptional = Optional.of(oldGroup);

        // Mock authentication
        User user = new User();
        user.setId(userId);
        // Thiết lập thông tin xác thực cho đối tượng User giả lập
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

        // Thiết lập thông tin xác thực vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        when(UserUtil.getCurrentUserId()).thenReturn(userId);
        when(groupRepository.findByOwnerIdAndId(userId, groupId)).thenReturn(groupOptional);

        // Act
        ResponseEntity<ResponseObject> response = groupService.update(groupId, updateGroupRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Cập nhật nhóm thành công", response.getBody().getMessage());
        assertEquals("", response.getBody().getData());

        assertEquals(updatedGroupName, oldGroup.getName());

        verify(groupRepository).findByOwnerIdAndId(userId, groupId);

    }

    @Test
    public void update_GroupNotFound() {
        // Arrange
        Long groupId = 1L;
        UpdateGroupRequest updateGroupRequest = new UpdateGroupRequest();
        updateGroupRequest.setName("Updated Group Name");

        Long userId = 123L;

        User user = new User();
        user.setId(userId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(groupRepository.findByOwnerIdAndId(userId, groupId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ResponseObject> response = groupService.update(groupId, updateGroupRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Nhóm này không tồn tại hoặc không thuộc quyền sở hữu của bạn", response.getBody().getMessage());
        assertEquals("", response.getBody().getData());

        verify(groupRepository).findByOwnerIdAndId(userId, groupId);
    }

}
