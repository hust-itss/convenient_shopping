package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.dto.food.CreateFoodMeasureRequest;
import com.example.convenientshoppingapp.entity.FoodMeasure;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.repository.FoodMeasureRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodMeasureService {
    private final FoodMeasureRespository foodMeasureRespository;
    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<ResponseObject> create(CreateFoodMeasureRequest foodMeasureRequest) {
        if(foodMeasureRespository.existsByName(foodMeasureRequest.getName())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("error", "Tên đơn vị thực phẩm đã tồn tại", ""));
        }

        FoodMeasure foodMeasure = modelMapper.map(foodMeasureRequest, FoodMeasure.class);
        foodMeasureRespository.save(foodMeasure);
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "Tạo mới đơn vị thực phẩm thành công", ""));
    }

    public ResponseEntity<ResponseObject> getAll() {
        List<FoodMeasure> foodMeasureList = foodMeasureRespository.findAll();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "", foodMeasureList));
    }
}
