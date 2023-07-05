package com.example.convenientshoppingapp.servicetest;

import com.example.convenientshoppingapp.entity.Group;
import com.example.convenientshoppingapp.entity.auth.Users;
import com.example.convenientshoppingapp.repository.GroupRepository;
import com.example.convenientshoppingapp.repository.UserRepository;
import com.example.convenientshoppingapp.service.impl.GroupService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
@ExtendWith(MockitoExtension.class)
public class GroupServiceIntegrationTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupService groupService;

    // ...

    @Test
    @Transactional
    public void saveGroup_ShouldSaveGroup() {
        // Tạo đối tượng Group mới
        Group group = new Group();
        group.setId(1L);
        group.setName("Test Group");
        group.setGroupLeader(1L); // Thiết lập giá trị Group Leader

        // Tạo giá trị id cho Group Leader
        Long groupLeaderId = 1L;

        // Thiết lập behavior cho mock groupRepository
        when(userRepository.findById(groupLeaderId)).thenReturn(Optional.of(new Users()));
//        when(groupRepository.findById(groupLeaderId)).thenReturn(Optional.of(new Group()));

        // Khi gọi phương thức saveGroup
        when(groupRepository.save(group)).thenReturn(group);
        Group savedGroup = groupService.save(group);

        // Xác nhận rằng phương thức save của groupRepository đã được gọi với đúng tham số
        verify(groupRepository).save(group);

        // Xác nhận rằng đối tượng Group đã được lưu thành công và được trả về
        assertNotNull(savedGroup);
        assertEquals(group.getId(), savedGroup.getId());
        assertEquals(group.getName(), savedGroup.getName());
        assertEquals(group.getGroupLeader(), savedGroup.getGroupLeader());
        // Kiểm tra các thuộc tính khác của đối tượng Group (nếu có)

        // ...
    }




    @Test
    @Transactional
    public void deleteGroupById_ShouldDeleteGroup() {
        Long groupId = 2L;

        // Khi gọi phương thức deleteGroupById
        groupService.deleteGroupById(groupId);

        // Xác nhận rằng phương thức deleteGroupById của groupRepository đã được gọi với đúng tham số
        verify(groupRepository).deleteGroupById(groupId);
    }

    @Test
    @Transactional
    public void getGroupById_ShouldReturnGroup() {
        Long groupId = 2L;
        Group expectedGroup = new Group();
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(expectedGroup));

        // Khi gọi phương thức getGroupById
        Optional<Group> actualGroup = groupService.getGroupById(groupId);

        // Xác nhận rằng phương thức findById của groupRepository đã được gọi với đúng tham số và kết quả trả về là đúng
        verify(groupRepository).findById(groupId);
        assertTrue(actualGroup.isPresent());
        assertEquals(expectedGroup, actualGroup.get());
    }

    @Test
    @Transactional
    public void getGroupByGroupLeader_ShouldReturnGroup() {
        Long groupLeaderId = 1L;
        Group expectedGroup = new Group();
        when(groupRepository.findByGroupLeader(groupLeaderId)).thenReturn(Optional.of(expectedGroup));

        // Khi gọi phương thức getGroupByGroupLeader
        Optional<Group> actualGroup = groupService.getGroupByGroupLeader(groupLeaderId);

        // Xác nhận rằng phương thức findByGroupLeader của groupRepository đã được gọi với đúng tham số và kết quả trả về là đúng
        verify(groupRepository).findByGroupLeader(groupLeaderId);
        assertTrue(actualGroup.isPresent());
        assertEquals(expectedGroup, actualGroup.get());
    }

    @Test
    @Transactional
    public void getAllGroupByNameAndPaging_ShouldReturnGroups() {
        int page = 0;
        int size = 10;
        String name = "Nhóm";

        List<Group> expectedGroups = Arrays.asList(new Group(), new Group());
        Page<Group> groupPage = new PageImpl<>(expectedGroups);
        when(groupRepository.findAllByNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(groupPage);

        // Khi gọi phương thức getAllGroupByNameAndPaging
        Map<String, Object> response = groupService.getAllGroupByNameAndPaging(page, size, name);

        // Xác nhận rằng phương thức findAllByNameContainingIgnoreCase của groupRepository đã được gọi với đúng tham số
        verify(groupRepository).findAllByNameContainingIgnoreCase(name, PageRequest.of(page, size, Sort.by(Sort.Order.asc("id"))));
        assertNotNull(response);
        assertEquals(expectedGroups, response.get("groups"));
        assertEquals(2, ((List<Group>) response.get("groups")).size());
        assertEquals(0, response.get("currentPage"));
        assertEquals(2L, response.get("totalItems"));
        assertEquals(1, response.get("totalPages"));
        // ...
    }

}
