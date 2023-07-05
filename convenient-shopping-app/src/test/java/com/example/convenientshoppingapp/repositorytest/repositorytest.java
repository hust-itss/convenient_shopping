package com.example.convenientshoppingapp.repositorytest;

import com.example.convenientshoppingapp.repository.RoleRepository;
import com.example.convenientshoppingapp.repository.UserRepository;
import com.example.convenientshoppingapp.service.impl.auth.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.example.convenientshoppingapp.entity.auth.Users;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class repositorytest {
    @Test
    public void testFindByUsername() {
        // Arrange
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Users expectedUser = new Users();
        expectedUser.setUsername("anhdz");
        expectedUser.setEmail("theanh@gmail.com");
        Mockito.when(userRepository.findByUsername(expectedUser.getUsername())).thenReturn(Optional.of(expectedUser));

        // Act
       //Optional<Users> actualUser = userRepository.findByUsername(expectedUser.getUsername());
       Users actualUser = userRepository.findByUsername(expectedUser.getUsername()).orElseThrow();
       boolean result = actualUser == expectedUser;
        // Assert
        Assertions.assertEquals(expectedUser, actualUser, "User should be found by username");
        Assertions.assertTrue(result);
    }

    @Test
    public void testSave() {
        // Arrange
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UserServiceImpl usererviceImpl = new UserServiceImpl(userRepository, roleRepository, passwordEncoder); //Mockito.mock(UserServiceImpl.class);
        Users userToSave = new Users();
        userToSave.setUsername("theanh");

        // Act
        usererviceImpl.saveUser(userToSave);
        Optional<Users> user= userRepository.findByUsername(userToSave.getUsername());

        // Assert
        Assertions.assertEquals(usererviceImpl, user, "User should be found by username");
    }
}

