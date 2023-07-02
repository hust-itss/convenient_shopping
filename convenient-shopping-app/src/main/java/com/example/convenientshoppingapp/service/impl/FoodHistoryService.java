package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.Utils.UserUtil;
import com.example.convenientshoppingapp.dto.food.CreateFoodHistoryRequest;
import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.FoodHistory;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.repository.FoodHistoryRepository;
import com.example.convenientshoppingapp.repository.FoodMeasureRespository;
import com.example.convenientshoppingapp.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodHistoryService {
    private final FoodHistoryRepository foodHistoryRepository;
    private final FoodRepository foodRepository;
    private final FoodMeasureRespository foodMeasureRespository;
    @Autowired
    private final ModelMapper modelMapper;
    public ResponseEntity<ResponseObject> create(CreateFoodHistoryRequest foodRequest) {
        // Kiểm tra xem thực phẩm có tồn tại hay không
        if(!foodRepository.existsById(foodRequest.getFoodId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Thực phẩm này không tồn tại trong hệ thống", ""));
        }
        if(!foodMeasureRespository.existsById(foodRequest.getMeasureId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Đơn vị thực phẩm này không tồn tại trong hệ thống", ""));
        }

        FoodHistory food = modelMapper.map(foodRequest, FoodHistory.class);
        food.setUserId(UserUtil.getCurrentUserId());
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "", foodHistoryRepository.save(food)));
    }

}
