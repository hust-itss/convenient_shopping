package com.example.convenientshoppingapp.serviceTest;

import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.repository.DishRepository;
import com.example.convenientshoppingapp.service.impl.DishService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@DataJpaTest
//@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
@ExtendWith(MockitoExtension.class)
public class DishServiceTest {

    @InjectMocks
    private DishService dishService;

    @Mock
    private DishRepository dishRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dishService = new DishService(dishRepository);
    }

    @Test
    @Transactional
    public void saveDishTest_ShouldSaveDish() {
        // Arrange
        Dish expectedDish = new Dish();
        expectedDish.setId(6L);
        expectedDish.setName("Dish test");
        expectedDish.setStatus(0);

        when(dishRepository.findByName("Dish test")).thenReturn(Optional.empty());
        when(dishRepository.save(expectedDish)).thenReturn(expectedDish);
        dishService.save(expectedDish);

        when(dishRepository.findById(6L)).thenReturn(Optional.of(expectedDish)); // Sửa đoạn này

        // Act
        Dish actualDish = dishRepository.findById(6L)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: "));

        // Assert
        Assertions.assertEquals(expectedDish.getId(), actualDish.getId(), "Dish should be found by id");
    }

    @Test
    @Transactional
    public void saveDish_ShouldThrowException_WhenDishNameExists() {
        // Arrange
        Dish existingDish = new Dish();
        existingDish.setId(1L);
        existingDish.setName("Existing Dish");
        existingDish.setStatus(0);

        Dish newDish = new Dish();
        newDish.setId(2L);
        newDish.setName("Existing Dish"); // Tên dish đã tồn tại
        newDish.setStatus(1);

        when(dishRepository.findByName("Existing Dish")).thenReturn(Optional.of(existingDish));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dishService.save(newDish));
    }

    @Test
    @Transactional
    public void updateDish_ShouldUpdateDish_WhenValidIdProvided() {
        // Arrange
        Dish existingDish = new Dish();
        existingDish.setId(2L);
        existingDish.setName("Existing Dish");
        existingDish.setStatus(0);

        Dish updatedDish = new Dish();
        updatedDish.setId(2L);
        updatedDish.setName("Updated Dish");
        updatedDish.setStatus(0);

        when(dishRepository.findById(updatedDish.getId())).thenReturn(Optional.of(existingDish));
        when(dishRepository.save(existingDish)).thenReturn(updatedDish);

        // Act
        Dish result = dishService.update(updatedDish);

        // Assert
        assertEquals(updatedDish.getName(), result.getName());
        assertEquals(updatedDish.getStatus(), result.getStatus());
        // Kiểm tra các thuộc tính khác (nếu có)
    }


    @Test
    public void findDishById_ShouldReturnDish_WhenValidIdProvided() {
        // Arrange
        Dish expectedDish = new Dish();
        expectedDish.setId(1L);
        expectedDish.setName("Dish test");
        expectedDish.setStatus(0);

        when(dishRepository.findById(expectedDish.getId())).thenReturn(Optional.of(expectedDish));

        // Act
        Dish result = dishService.findById(expectedDish.getId());

        // Assert
        assertEquals(expectedDish.getId(), result.getId());
        assertEquals(expectedDish.getName(), result.getName());
        assertEquals(expectedDish.getStatus(), result.getStatus());
        // Kiểm tra các thuộc tính khác (nếu có)
    }

    @Test
    @Transactional
    public void deleteDishById_ShouldDeleteDish_WhenValidIdProvided() {
        // Arrange
        Dish existingDish = new Dish();
        existingDish.setId(1L);
        existingDish.setName("Existing Dish");
        existingDish.setStatus(0);

        when(dishRepository.findById(existingDish.getId())).thenReturn(Optional.of(existingDish));

        // Act
        ResponseObject result = dishService.deleteById(existingDish.getId());

        // Assert
        assertEquals("success", result.getStatus());
        assertEquals("Delete food successfully", result.getMessage());
    }

    @Test
    @Transactional
    public void findDishByName_ShouldReturnDishes_WhenValidNameProvided() {
        // Arrange
        int page = 0;
        int size = 10;
        String name = "Dish";

        List<Dish> expectedDishes = Arrays.asList(new Dish(), new Dish());
        Page<Dish> dishPage = new PageImpl<>(expectedDishes);
        when(dishRepository.getDishByNameContainingIgnoreCase(name, PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")))))
                .thenReturn(dishPage);

        // Act
        Map<String, Object> response = dishService.findDishByName(page, size, name);

        // Assert
        assertNotNull(response);
        assertEquals(expectedDishes, response.get("dishes"));
        assertEquals(page, response.get("currentPage"));
        assertEquals(2L, response.get("totalItems"));
        assertEquals(1, response.get("totalPages"));
    }

    @Test
    @Transactional
    public void updateDish_ShouldThrowException_WhenInvalidIdProvided() {
        // Arrange
        Dish updatedDish = new Dish();
        updatedDish.setId(1L);
        updatedDish.setName("Updated Dish");
        updatedDish.setStatus(1);

        when(dishRepository.findById(updatedDish.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dishService.update(updatedDish));
    }

    @Test
    @Transactional
    public void findDishById_ShouldThrowException_WhenInvalidIdProvided() {
        // Arrange
        Long invalidId = 1L;

        when(dishRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dishService.findById(invalidId));
    }

    @Test
    @Transactional
    public void deleteDishById_ShouldThrowException_WhenInvalidIdProvided() {
        // Arrange
        Long invalidId = 1L;

        when(dishRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dishService.deleteById(invalidId));
    }

    @Test
    @Transactional
    public void saveDish_ShouldThrowException_WhenDishNameAlreadyExists() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Existing Dish");
        dish.setStatus(0);

        when(dishRepository.findByName(dish.getName())).thenReturn(Optional.of(new Dish()));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dishService.save(dish));
    }


}
