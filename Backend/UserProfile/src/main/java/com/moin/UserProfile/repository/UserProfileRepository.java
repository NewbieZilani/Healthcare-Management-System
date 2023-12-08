package com.moin.UserProfile.repository;

import com.moin.UserProfile.entity.UserProfileEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity,String> {
    Optional<UserProfileEntity> findByUserId(String userId);
    List<UserProfileEntity> findByUserIdIn(List<String> userId);
    @Query("SELECT u FROM UserProfileEntity u WHERE u.name LIKE %:name%")
    List<UserProfileEntity> findByName(@Param("name") String name);
    long countBy();
}
