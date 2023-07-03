package com.example.convenientshoppingapp.config;

import com.example.convenientshoppingapp.dto.food.CreateFoodHistoryRequest;
import com.example.convenientshoppingapp.dto.food.CreateFoodMeasureRequest;
import com.example.convenientshoppingapp.dto.food.CreateFoodRequest;
import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.FoodHistory;
import com.example.convenientshoppingapp.entity.FoodMeasure;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    public void ModelMapperConfig(){};
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setAmbiguityIgnored(true);
        modelMapper.createTypeMap(CreateFoodMeasureRequest.class, FoodMeasure.class);
        modelMapper.createTypeMap(CreateFoodRequest.class, Food.class);
        modelMapper.createTypeMap(CreateFoodHistoryRequest.class, FoodHistory.class);
        //.addMapping(Employee::getEmployeeID, EmployeeDto::setId);
        return modelMapper;
    }
}
