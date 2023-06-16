package com.example.convenientshoppingapp.repository;


import com.example.convenientshoppingapp.entity.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    Optional<Dish> findByName(String name);
    Optional<Dish> findById(Long id);

    Page<Dish> getDishByNameContainingIgnoreCase(String name, Pageable pageable);
}
