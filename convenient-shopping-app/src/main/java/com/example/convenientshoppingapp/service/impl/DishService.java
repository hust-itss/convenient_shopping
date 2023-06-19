package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.repository.DishRepository;
import com.example.convenientshoppingapp.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishService {

    private final DishRepository dishRepository;


    /**
     * Lưu dish vào database
     * Nếu dish đã tồn tại thì sẽ throw ra lỗi
     * Nếu dish chưa tồn tại thì sẽ lưu vào database
     * Được sử dụng trong DishController
     * @param dish
     * @return
     */
    @Modifying
    public Dish save(Dish dish) {
        if(dishRepository.findByName(dish.getName()).isPresent()){
            throw new RuntimeException("Dish name already exists");
        }
        log.info("Dish: {}", dish);
        return dishRepository.save(dish);
    }

    /**
     * Cập nhật dish
     * Nếu không tìm thấy id của dish thì sẽ throw ra lỗi
     * @param dish
     * @return
     */
    @Modifying
    public Dish update(Dish dish) {
        // Check nếu không tìm thấy id của dish thì sẽ throw ra lỗi
        Dish oldDish = dishRepository.findById(dish.getId())
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + dish.getId()));
        //set các thuộc tính mới cho oldDish để lưu vào database
        oldDish.setDescriptions(dish.getDescriptions());
        oldDish.setName(dish.getName());
        oldDish.setCookDate(dish.getCookDate());
        oldDish.setExpired(dish.getExpired());

        log.info("Dish: {}", oldDish);
        return dishRepository.save(oldDish);
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
    public ResponseObject deleteById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
        dishRepository.delete(dish);
        return new ResponseObject("success", "Delete food successfully", "");
    }

    /**
     * Tìm kiếm food theo tên có phân trang và sắp xếp theo id
     * @param page
     * @param size
     * @param name
     * @return
     */
    public Map<String, Object> findDishByName(int page, int size, String name) {
        Pageable paging = PageRequest.of(page, size, Sort.by(
                        Sort.Order.asc("id")
                )
        );

        Page<Dish> pageDish;
        List<Dish> dishes = new ArrayList<Dish>();
        pageDish = dishRepository.getDishByNameContainingIgnoreCase(name, paging);
        dishes = pageDish.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("dishes", dishes);
        response.put("currentPage", pageDish.getNumber());
        response.put("totalItems", pageDish.getTotalElements());
        response.put("totalPages", pageDish.getTotalPages());
        log.info("Get Dish by name use paging successfully");
        return response;
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
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long threeDaysInMillis = 3 * 24 * 60 * 60 * 1000; // 3 days in milliseconds
        long timeDifference = dish.getExpired().getTime() - currentTime.getTime();

        return timeDifference < threeDaysInMillis;
    }
}

