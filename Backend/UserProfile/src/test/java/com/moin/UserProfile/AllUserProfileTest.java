package com.moin.UserProfile;

import com.moin.UserProfile.dto.UserProfileDto;
import com.moin.UserProfile.entity.UserEntity;
import com.moin.UserProfile.entity.UserProfileEntity;
import com.moin.UserProfile.repository.UserProfileRepository;
import com.moin.UserProfile.repository.UserRepository;
import com.moin.UserProfile.service.serviceImplementation.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllUserProfileTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllUserProfiles() {
        // Arrange
        UserEntity userEntity1 = new UserEntity(); // Assume UserEntity has a constructor or setters to set properties
        userEntity1.setId("user1");
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId("user2");
        List<UserEntity> userEntities = Arrays.asList(userEntity1, userEntity2);

        UserProfileEntity userProfile1 = new UserProfileEntity(); // Similarly setup UserProfileEntity
        UserProfileEntity userProfile2 = new UserProfileEntity();
        List<UserProfileEntity> userProfileEntities = Arrays.asList(userProfile1, userProfile2);

        when(userRepository.findByRole("PATIENT")).thenReturn(userEntities);
        when(userProfileRepository.findByUserIdIn(Arrays.asList("user1", "user2"))).thenReturn(userProfileEntities);

        // Act
        List<UserProfileDto> result = userService.getAllUserProfiles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findByRole("PATIENT");
        verify(userProfileRepository).findByUserIdIn(Arrays.asList("user1", "user2"));
    }
}
