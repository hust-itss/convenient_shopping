package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.Utils.UserUtil;
import com.example.convenientshoppingapp.dto.PaginationResponse;
import com.example.convenientshoppingapp.dto.dish.CreateDishRequest;
import com.example.convenientshoppingapp.dto.dish.DishResponse;
import com.example.convenientshoppingapp.dto.dish.UpdateDishRequest;
import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.FoodHistory;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.repository.DishRepository;
import com.example.convenientshoppingapp.repository.spec.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishService {

    private final DishRepository dishRepository;
    private final ModelMapper modelMapper;

    /**
     * Lưu dish vào database
     * Nếu dish đã tồn tại thì sẽ throw ra lỗi
     * Nếu dish chưa tồn tại thì sẽ lưu vào database
     * Được sử dụng trong DishController
     * @return
     */
    @Modifying
    public ResponseEntity<ResponseObject> save(CreateDishRequest dishRequest) {
        Dish dish = modelMapper.map(dishRequest, Dish.class);
        Long userId = UserUtil.getCurrentUserId();
        dish.setUserId(userId);
        dishRepository.save(dish);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "Thêm dự định nấu thành công", null));
    }

    /**
     * Cập nhật dish
     * Nếu không tìm thấy id của dish thì sẽ throw ra lỗi
     * @param dish
     * @return
     */
    @Modifying
    public ResponseEntity<ResponseObject> update(Long dishId, UpdateDishRequest dishRequest) {
        Long userId = UserUtil.getCurrentUserId();
        Optional<Dish> dishOptional = dishRepository.findDishByIdAndUserId(dishId, userId);
        if(!dishOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Dự định này không tồn tại hoặc không thuộc tài khoản của bạn", null));
        }

        Dish dish = dishOptional.get();
        if(dishRequest.getIsCook() == false) {
            dish.setExpireAt(null);
            dish.setIsCook(false);
            dish.setIsStoredFridge(false);
        } else {
            dish.setDescriptions(dishRequest.getDescriptions());
            dish.setCookDate(dishRequest.getCookDate());
            dish.setCookMeal(dishRequest.getCookMeal());
            dish.setExpireAt(dishRequest.getExpireAt());
            dish.setIsStoredFridge(dishRequest.getIsStoredFridge());
            dish.setIsCook(dishRequest.getIsCook());
        }

        dishRepository.save(dish);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Cập nhật dự định thành công", null));
    }


    /**
     * Tìm kiếm dish theo id
     * @param id
     * @return
     */
    public Dish findById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));
        log.info("On find food");
        return dish;
    }

    /**
     * Xóa món ăn theo ID
     * @param id
     * @return
     */
    @Modifying
    public ResponseEntity<ResponseObject> deleteById(Long id) {
        Long userId = UserUtil.getCurrentUserId();
        Optional<Dish> dishOptional = dishRepository.findDishByIdAndUserId(id, userId);
        if(!dishOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Dự định này không tồn tại hoặc không thuộc tài khoản của bạn", null));
        }

        dishRepository.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Xóa thành công", null));

    }

    /**
     * Tìm kiếm food theo tên có phân trang và sắp xếp theo id
     * @param page
     * @param size
     * @param name
     * @return
     */
    public ResponseEntity<ResponseObject> getAll(int page, int size, String name, String startDate, String endDate, int meal) {
        Specification<Dish> spec = where(null);
        Pageable paging = PageRequest.of(page, size, Sort.by(
                        Sort.Order.asc("id")
                )
        );
        if(!startDate.isEmpty()) {
            LocalDate date = LocalDate.parse(startDate);
            MySpecification esFoodStartDate = new MySpecification();
            esFoodStartDate.add(new SearchCriteria("cookDate", date, SearchOperation.DATE_START));
            spec = spec.and(esFoodStartDate);
        }
        if(!endDate.isEmpty()) {
            LocalDate date = LocalDate.parse(endDate);
            MySpecification esFoodEndDate = new MySpecification();
            esFoodEndDate.add(new SearchCriteria("createAt", date, SearchOperation.DATE_END));
            spec = spec.and(esFoodEndDate);
        }

        if(meal > 0) {
            MySpecification esFoodEndDate = new MySpecification();
            esFoodEndDate.add(new SearchCriteria("cookMeal", meal, SearchOperation.EQUAL));
            spec = spec.and(esFoodEndDate);
        }

        if(name.length() > 0) {
            spec = spec.and(DishSpecification.hasFoodContainName(name));
        }

        MySpecification esFoodEndDate = new MySpecification();
        esFoodEndDate.add(new SearchCriteria("userId", UserUtil.getCurrentUserId(), SearchOperation.EQUAL));
        spec = spec.and(esFoodEndDate);

        Page<Dish> pageDish = dishRepository.findAll(spec,paging);;
        List<Dish> dishes = pageDish.getContent();
        List<DishResponse> dishResponses = Arrays.asList(modelMapper.map(dishes, DishResponse[].class));
        PaginationResponse response = new PaginationResponse();
        response.setItems(dishResponses);
        response.setTotalItem(pageDish.getTotalElements());
        response.setTotalPage(pageDish.getTotalPages());
        response.setCurrentPage(pageDish.getNumber());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", response));
    }

    /**
     * Xóa một loạt các food theo danh sách các id
     * @param ids
     */
    public void delete(Long[] ids){
        for (Long id : ids){
            dishRepository.deleteById(id);
        }
    }

    /**
     * Thông báo món ăn sắp hết hạn nếu hạn sử dụng tính đến hôm này còn dưới 3 ngày
     * @param dish
     * @return
     */
    public boolean isExpiringSoon(Dish dish) {
//        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//        long threeDaysInMillis = 3 * 24 * 60 * 60 * 1000; // 3 days in milliseconds
//        long timeDifference = dish.getExpired().getTime() - currentTime.getTime();
//
//        return timeDifference < threeDaysInMillis;
        return false;
    }


}

