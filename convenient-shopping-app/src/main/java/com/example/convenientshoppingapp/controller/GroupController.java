package com.example.convenientshoppingapp.controller;

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
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> insert(@RequestBody @Valid Group group) {
        groupService.save(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long id, @RequestBody @Valid Group group) {
        group.setId(id);
        groupService.update(group);
    }

    @DeleteMapping("/removeMember")
            @RequestBody @Valid String nameGroup) {
        groupService.deleteMember(nameMember, nameGroup);
                .body(new ResponseObject("success", "Cập nhật dữ liệu thành công", ""));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Group> group = groupService.getGroupById(id);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> findAll(
            @RequestParam(defaultValue = "", name = "name") String name,
            @RequestParam(defaultValue = "0", name = "page") int page,
    }

    @PutMapping("/addMember")
        groupService.addMember(nameMember, nameGroup);
    }

    @PutMapping("/addLeader")
        groupService.addLeader(nameMember, nameGroup);
    }
}
