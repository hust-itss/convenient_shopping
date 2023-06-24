package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.dto.auth.AuthenticationRequest;
import com.example.convenientshoppingapp.dto.auth.AuthenticationResponse;
import com.example.convenientshoppingapp.dto.auth.RegisterRequest;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.auth.AuthenticationService;
import com.example.convenientshoppingapp.service.impl.auth.UserService;
import com.example.convenientshoppingapp.service.impl.auth.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final AuthenticationService authenticationService;

    @PostMapping("signup")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("signin")
    public ResponseEntity<ResponseObject> register(
         @Valid @RequestBody RegisterRequest request
    ) {
        return authenticationService.register(request);
    }



}
