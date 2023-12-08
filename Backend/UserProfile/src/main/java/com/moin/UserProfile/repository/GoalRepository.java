package com.moin.UserProfile.repository;

import com.moin.UserProfile.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalRepository extends JpaRepository<GoalEntity, Long> {
    Optional<GoalEntity> findByUserId(String userId);

    GoalEntity findByUserIdAndIsCompleted(String id, Boolean b);
}
