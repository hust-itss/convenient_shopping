package com.example.convenientshoppingapp.serviceTest;

import com.example.convenientshoppingapp.config.ModelMapperConfig;
import com.example.convenientshoppingapp.dto.food.CreateFoodRequest;
import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.entity.auth.User;
import com.example.convenientshoppingapp.repository.FoodRepository;
import com.example.convenientshoppingapp.repository.RecipeRepository;
import com.example.convenientshoppingapp.repository.spec.MySpecification;
import com.example.convenientshoppingapp.repository.spec.SearchCriteria;
import com.example.convenientshoppingapp.repository.spec.SearchOperation;
import com.example.convenientshoppingapp.service.impl.FoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @MockBean
    private ModelMapper modelMapper;

    @InjectMocks
    private FoodService foodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void save_WhenFoodDoesNotExist_ShouldReturnCreatedResponse() {
        // Arrange
        CreateFoodRequest foodRequest = new CreateFoodRequest();
        foodRequest.setName("New Food");
        // Tạo giá trị id cho Owner
        Long ownerId = 123L;

        // Tạo đối tượng User giả lập
        User user = new User();
        user.setId(ownerId);
        user.setUsername("user123");

        // Thiết lập thông tin xác thực cho đối tượng User giả lập
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

        // Thiết lập thông tin xác thực vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Set up mock behavior
        when(foodRepository.findByName(foodRequest.getName())).thenReturn(null);
        when(modelMapper.map(foodRequest, Food.class)).thenReturn(new Food());

        // Act
        ResponseEntity<ResponseObject> response = foodService.save(foodRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Tạo thực phẩm mới thành công", response.getBody().getMessage());
        assertEquals("", response.getBody().getData());

    }



    @Test
    void update_WhenFoodDoesNotExist_ShouldReturnNotFoundResponse() {
        // Arrange
        Food newFood = new Food();
        newFood.setId(1L);
        newFood.setName("New Food");
        newFood.setDescription("New Description");
        newFood.setPosterLink("New Poster Link");
        newFood.setOwnerId(456L);

        // Set up mock behavior
        when(foodRepository.findById(newFood.getId())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ResponseObject> response = foodService.update(newFood);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Thực phẩm này đã tồn tại", response.getBody().getMessage());
        assertEquals("", response.getBody().getData());

        // Verify that the foodRepository.findById method was called
//        verify(foodRepository, times(1)).findById(newFood.getId());
        // Verify that the foodRepository.save method was not called
        verify(foodRepository, never()).save(any(Food.class));
    }


    @Test
    void findById_WhenFoodDoesNotExist_ShouldThrowException() {
        // Arrange
        Long foodId = 1L;

        // Set up mock behavior
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            foodService.findById(foodId);
        });

    }


    @Test
    void deleteById_WhenFoodDoesNotExistOrUserIsNotOwner_ShouldReturnNotFoundResponse() {
        // Arrange
        Long foodId = 1L;
        Long ownerId = 123L;

        User user = new User();
        user.setId(ownerId);
        user.setUsername("user123");

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Set up mock behavior
        when(foodRepository.existsByIdAndOwnerId(foodId, ownerId)).thenReturn(false);

        // Act
        ResponseEntity<ResponseObject> response = foodService.deleteById(foodId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Bạn không phải người thêm nên không thể xóa thực phẩm này", response.getBody().getMessage());
        assertEquals("", response.getBody().getData());

//        // Verify that the foodRepository.existsByIdAndOwnerId method was called
//        verify(foodRepository, times(1)).existsByIdAndOwnerId(foodId, ownerId);
//        // Verify that the foodRepository.deleteByIdAndOwnerId method was not called
//        verify(foodRepository, never()).deleteByIdAndOwnerId(anyLong(), anyLong());
    }
}
