package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.Utils.UserUtil;
import com.example.convenientshoppingapp.dto.food.CreateFoodHistoryRequest;
import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.FoodHistory;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.repository.FoodHistoryRepository;
import com.example.convenientshoppingapp.repository.FoodMeasureRespository;
import com.example.convenientshoppingapp.repository.FoodRepository;
import com.example.convenientshoppingapp.repository.spec.MySpecification;
import com.example.convenientshoppingapp.repository.spec.SearchCriteria;
import com.example.convenientshoppingapp.repository.spec.SearchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodHistoryService {
    private final FoodHistoryRepository foodHistoryRepository;
    private final FoodRepository foodRepository;
    private final FoodMeasureRespository foodMeasureRespository;
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

    public ResponseEntity<ResponseObject> getAll(int page, int size, String name, String startDate, String endDate) {
        Specification<FoodHistory> spec = where(null);

        Pageable paging = PageRequest.of(page, size, Sort.by(
                        Sort.Order.asc("id")
                )
        );

        if(!startDate.isEmpty()) {
            LocalDate date = LocalDate.parse(startDate);
            MySpecification esFoodStartDate = new MySpecification();
            esFoodStartDate.add(new SearchCriteria("createAt", date, SearchOperation.DATE_START));
            spec = spec.and(esFoodStartDate);
        }

        if(!endDate.isEmpty()) {
            LocalDate date = LocalDate.parse(endDate);
            MySpecification esFoodEndDate = new MySpecification();
            esFoodEndDate.add(new SearchCriteria("createAt", date, SearchOperation.DATE_END));
            spec = spec.and(esFoodEndDate);
        }

        if(name.length() > 0) {
            MySpecification esEmployeeCode = new MySpecification();
            esEmployeeCode.add(new SearchCriteria("name", name, SearchOperation.MATCH));
            spec = spec.and(esEmployeeCode);
        }

        Long userId = UserUtil.getCurrentUserId();
        MySpecification esEmployeeCode = new MySpecification();
        esEmployeeCode.add(new SearchCriteria("userId", userId, SearchOperation.EQUAL));
        spec = spec.and(esEmployeeCode);

        Page<FoodHistory> pageFood;
        List<FoodHistory> foods = new ArrayList<FoodHistory>();
        pageFood = foodHistoryRepository.findAll(spec, paging);
        foods = pageFood.getContent();
        for(FoodHistory food : foods){
            food.setFood(foodRepository.findFoodById(food.getId()));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("foods", foods);
        response.put("currentPage", pageFood.getNumber());
        response.put("totalItems", pageFood.getTotalElements());
        response.put("totalPages", pageFood.getTotalPages());
        log.info("Get Food by name use paging successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", response));
    }

}
