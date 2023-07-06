package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.dto.group.AddMemberRequest;
import com.example.convenientshoppingapp.dto.group.CreateGroupRequest;
import com.example.convenientshoppingapp.dto.group.RemoveMemberRequest;
import com.example.convenientshoppingapp.entity.Group;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/group")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * Tạo mới group
     *
     * @param group
     * @return
     */
    @PostMapping("")
    public ResponseEntity<ResponseObject> create(@RequestBody @Valid CreateGroupRequest group) {
        return groupService.save(group);
    }

    /**
     * Cập nhật group
     *
     * @param id
     * @param group
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long id, @RequestBody @Valid Group group) {
        group.setId(id);
        groupService.update(group);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("success", "Cập nhật dữ liệu thành công", group));
    }

    /**
     * Lấy dữ liệu group theo id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }

    @GetMapping("/{id}/foods")
    public ResponseEntity<ResponseObject> getListFoods(@PathVariable Long id,
       @RequestParam(defaultValue = "", name = "name") String name,
       @RequestParam(defaultValue = "0", name = "page") int page,
       @RequestParam(defaultValue = "10", name = "size") int size)
    {
        return groupService.getListFoods(id, name, page, size);
    }

    /**
     * Tìm kiếm group theo tên có phân trang
     *
     * @param name
     * @param page
     * @param size
     * @return
     */
    @GetMapping("")
    public ResponseEntity<ResponseObject> findAll(
            @RequestParam(defaultValue = "", name = "name") String name,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size) {
        return groupService.getAll();
    }

    @GetMapping("group-name/{name}")
    public ResponseEntity<ResponseObject> getGroupByName(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Lấy dữ liệu thành công", groupService.getGroupByName(name)));
    }

    /**
     * Xóa group
     *
     * @param ids
     * @return
     */
    @DeleteMapping("")
    public ResponseEntity<ResponseObject> delete(@RequestBody Long ids) {
        groupService.deleteGroupById(ids);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "Xóa dữ liệu thành công", ""));
    }


    /**
     * Lấy danh sách món ăn trong group
     *
     * @param groupId
     * @return
     */
    @GetMapping("/getFoods/{groupId}")
    public ResponseEntity<ResponseObject> getFoods(@PathVariable Long groupId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("success", "Lấy dữ liệu thành công", groupService.getFoodsByGroupId(groupId)));
    }

    /**
     * Thêm thành viên vào nhóm
     * @return
     */
    @PostMapping("{groupId}/member")
    public ResponseEntity<ResponseObject> addMember(@PathVariable Long groupId ,@Valid @RequestBody AddMemberRequest request) {
        return groupService.addMember(request.getUsername(), groupId);
    }

    @DeleteMapping("{groupId}/member")
    public ResponseEntity<ResponseObject> deleteMember(@PathVariable Long groupId ,@Valid @RequestBody RemoveMemberRequest request) {
        return groupService.removeMember(request.getUserId(), groupId);
    }
}