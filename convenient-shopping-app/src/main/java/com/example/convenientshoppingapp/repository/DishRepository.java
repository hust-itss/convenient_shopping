package com.example.convenientshoppingapp.repository;


import com.example.convenientshoppingapp.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

}
