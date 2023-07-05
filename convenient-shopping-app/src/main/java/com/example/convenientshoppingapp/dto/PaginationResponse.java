package com.example.convenientshoppingapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginationResponse<T> {
    private List<T> items;
    private Integer currentPage;
    private Long totalItem;

    private Integer totalPage;
}
