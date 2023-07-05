package com.example.convenientshoppingapp.servicetest;

import com.example.convenientshoppingapp.controller.DishController;
import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.repository.DishRepository;
import com.example.convenientshoppingapp.service.impl.DishService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DishServiceTest {
    private DishService dishService;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishController dishController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dishService = new DishService(dishRepository);
        dishController = new DishController(dishService);
    }

    @Test
    public void saveDishTest() {
        // Arrange
        Dish expectedDish = new Dish();
        expectedDish.setId(1L);
        expectedDish.setName("Cha ca");
        expectedDish.setStatus(0);

        when(dishRepository.save(expectedDish)).thenReturn(expectedDish);
        dishService.save(expectedDish);

        // Act
        //Dish actualDish = dishService.findById(1L);
        Dish actualDish = dishRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: "));

        // Assert
        Assertions.assertEquals(expectedDish.getId(), actualDish, "User should be found by username");
    }
}
