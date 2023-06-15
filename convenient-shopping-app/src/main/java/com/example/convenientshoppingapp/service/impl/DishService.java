package com.example.convenientshoppingapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishService {

    public <Optional> Optional findById(Long id) {
        return null;
    }

    public <Optional> Optional findByNameContainingIgnoreCase(String name) {
        return null;
    }
}
