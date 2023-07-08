package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.Utils.UserUtil;
import com.example.convenientshoppingapp.dto.food.CreateFoodHistoryRequest;
import com.example.convenientshoppingapp.dto.food.FoodResponse;
import com.example.convenientshoppingapp.dto.food_history.FoodAvailableResponse;
import com.example.convenientshoppingapp.dto.food_history.FoodHistoryResponse;
import com.example.convenientshoppingapp.dto.food_history.UpdateFoodHistoryRequest;
import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.FoodHistory;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.repository.FoodHistoryRepository;
import com.example.convenientshoppingapp.repository.FoodMeasureRespository;
import com.example.convenientshoppingapp.repository.FoodRepository;
import com.example.convenientshoppingapp.repository.GroupRepository;
import com.example.convenientshoppingapp.repository.spec.FoodHistorySpecification;
import com.example.convenientshoppingapp.repository.spec.MySpecification;
import com.example.convenientshoppingapp.repository.spec.SearchCriteria;
import com.example.convenientshoppingapp.repository.spec.SearchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodHistoryService {
    private final FoodHistoryRepository foodHistoryRepository;
    private final FoodRepository foodRepository;
    private final FoodMeasureRespository foodMeasureRespository;
    private final GroupRepository groupRepository;
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

        Long userId = UserUtil.getCurrentUserId();
        if(foodRequest.getGroupId() != null) {
            if(!groupRepository.existsByOwnerIdAndId(userId, foodRequest.getGroupId())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("error", "Bạn không thể thêm thực phẩm vào nhóm này", ""));
            }
        }
        FoodHistory food = modelMapper.map(foodRequest, FoodHistory.class);
        food.setUserId(userId);
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
            spec = spec.and(FoodHistorySpecification.hasFoodContainName(name));
        }

        Long userId = UserUtil.getCurrentUserId();
        MySpecification esEmployeeCode = new MySpecification();
        esEmployeeCode.add(new SearchCriteria("userId", userId, SearchOperation.EQUAL));
        spec = spec.and(esEmployeeCode);

        MySpecification<FoodHistory> esGroupId = new MySpecification();
        esEmployeeCode.add(new SearchCriteria("groupId", null, SearchOperation.IS_NULL));
        spec = spec.and(esGroupId);


        Page<FoodHistory> pageFood;
        List<FoodHistory> foods = new ArrayList<FoodHistory>();
        pageFood = foodHistoryRepository.findAll(spec,paging);

        Map<String, Object> response = new HashMap<>();
        List<FoodHistoryResponse> employeesDto = Arrays.asList(modelMapper.map(pageFood.getContent(), FoodHistoryResponse[].class));

//        employeesDto.forEach(e -> {
//            Food food = foodRepository.findFoodById(e.getFoodId());
//            e.setFoodName(food.getName());
//            e.setFoodImage(food.getPosterLink());
//            e.setMeasureName(foodMeasureRespository.findFoodMeasureById(e.getMeasureId()).getName());
//        });
        response.put("items", employeesDto);
        response.put("currentPage", pageFood.getNumber());
        response.put("totalItems", pageFood.getTotalElements());
        response.put("totalPages", pageFood.getTotalPages());
        log.info("Get Food by name use paging successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", response));
    }

    public ResponseEntity<ResponseObject> update(Long id, UpdateFoodHistoryRequest newValue) {
        Long userId = UserUtil.getCurrentUserId();
        Optional<FoodHistory> foodHistoryOptional;
        if(newValue.getGroupId() != null) {
            foodHistoryOptional = foodHistoryRepository.findFoodHistoryByIdAndGroupId(id, newValue.getGroupId());
        } else {
            foodHistoryOptional = foodHistoryRepository.findFoodHistoryByIdAndUserId(id, userId);
        }

        if(!foodHistoryOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Thực phẩm này không tồn tại trong kho của bạn", newValue));
        }

        FoodHistory foodHistory = foodHistoryOptional.get();

        if(newValue.getIsBought()) {
            foodHistory.setPrice(newValue.getPrice());
            foodHistory.setBuyAddress(newValue.getBuyAddress());
            foodHistory.setBuyAt(newValue.getBuyAt());
            foodHistory.setExpireAt(newValue.getExpireAt());
            foodHistory.setIsStoredInFridge(newValue.getIsStoredInFridge());
            foodHistory.setMeasureId(newValue.getMeasureId());
            foodHistory.setQuantity(newValue.getQuantity());
            foodHistory.setBoughtBy(userId);
        } else {
            foodHistory.setBuyAt(null);
            foodHistory.setBoughtBy(null);
        }

        foodHistoryRepository.save(foodHistory);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Cập nhật thông tin thực phẩm thành công", null));
    }

    public ResponseEntity<ResponseObject> getDetail(Long id) {
        Long userId = UserUtil.getCurrentUserId();
        Optional<FoodHistory> foodHistoryOptional = foodHistoryRepository.findFoodHistoryByIdAndUserId(id, userId);
        if(!foodHistoryOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Thực phẩm này không tồn tại trong kho của bạn", null));
        }

        FoodHistory foodHistory = foodHistoryOptional.get();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", foodHistory));
    }

    public ResponseEntity<ResponseObject> delete(Long id) {
        Long userId = UserUtil.getCurrentUserId();
        Optional<FoodHistory> foodHistoryOptional = foodHistoryRepository.findFoodHistoryByIdAndUserId(id, userId);
        if(!foodHistoryOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Thực phẩm này không tồn tại trong kho của bạn", null));
        }

        foodHistoryRepository.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Xóa thực phẩm thành công", null));
    }

    public ResponseEntity<ResponseObject> getAvailable(List<Long> foods) {
        Long userId = UserUtil.getCurrentUserId();
        LocalDate tomorrow = LocalDate.now();
        Date today = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Timestamp startOfDay =new Timestamp(today.getTime());
        List<FoodHistory> foodsExist = foodHistoryRepository.findAllDistinctByFoodIdInAndUserIdAndExpireAtGreaterThanEqualAndBuyAtNotNullOrderByBuyAtAsc(foods, userId, startOfDay);

        // Danh sách thực phẩm đã có
        List<FoodHistoryResponse> foodExist = Arrays.asList(modelMapper.map(foodsExist, FoodHistoryResponse[].class));
        // Lấy các ID thực phẩm chưa có
        List<Long> foodNotExist = new ArrayList<>();
        foodsExist.forEach((food) -> {
            Long foodId = food.getFood().getId();
            foods.remove(Long.valueOf(foodId));
        });

        // Lấy thông tin các thực phẩm chưa có
        List<Food> foodListNotExist = foodRepository.getAllByIdIn(foods);
        List<FoodResponse> foodResponsesNotExist = Arrays.asList(modelMapper.map(foodListNotExist, FoodResponse[].class));
        FoodAvailableResponse response = new FoodAvailableResponse();
        response.setExist(foodExist);
        response.setNotExist(foodResponsesNotExist);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", response));
    }

    public ResponseEntity<ResponseObject> getStatistics(String startDate, String endDate, Long userId, Long groupId) {
        List<Object> statisticsFoods = foodHistoryRepository.statisticsFood(startDate, endDate, userId, groupId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", statisticsFoods));
    }



}
