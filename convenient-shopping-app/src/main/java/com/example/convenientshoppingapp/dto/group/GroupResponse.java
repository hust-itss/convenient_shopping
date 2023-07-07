package com.example.convenientshoppingapp.dto.group;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupResponse {
    private Long id;
    private String name;
    private Boolean isOwner;
    private Integer totalMember;
    private Long ownerId;
    private String ownerName;
}
