package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.entity.Group;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {

    private GroupService groupService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> insert(@RequestBody @Valid Group group) {
        groupService.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "Insert dữ liệu thành công", group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long id, @RequestBody @Valid Group group) {
        group.setId(id);
        groupService.update(group);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Cập nhật dữ liệu thành công", group));
    }

    @DeleteMapping("/removeMember")
    public ResponseEntity<ResponseObject> removeMember(@PathVariable String nameMember, @RequestBody @Valid String nameGroup) {
        groupService.deleteMember(nameMember, nameGroup);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Cập nhật dữ liệu thành công", ""));
    }
}
