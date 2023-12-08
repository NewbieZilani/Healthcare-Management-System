package com.moin.UserProfile;

import com.moin.UserProfile.dto.UserDto;
import com.moin.UserProfile.entity.UserEntity;
import com.moin.UserProfile.exceptions.AlreadyExistsException;
import com.moin.UserProfile.repository.UserRepository;
import com.moin.UserProfile.service.serviceImplementation.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserRegistrationTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private UserServiceImplementation userService; // Assuming the class containing createUser is UserService
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        // Given
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setRole("USER");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        UserDto createdUser = userService.createUser(userDto);

        // Then
        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testCreateUser_EmailExists_ThrowsException() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setEmail("existing@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));

        // When + Then
        assertThrows(AlreadyExistsException.class, () -> {
            userService.createUser(userDto);
        });
    }

}
