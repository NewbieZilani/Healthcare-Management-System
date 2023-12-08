package com.moin.UserProfile.service.serviceImplementation;

import com.moin.UserProfile.dto.GoalDto;
import com.moin.UserProfile.dto.UserProfileDto;
import com.moin.UserProfile.entity.GoalEntity;
import com.moin.UserProfile.entity.UserEntity;
import com.moin.UserProfile.entity.UserProfileEntity;
import com.moin.UserProfile.exceptions.AlreadyExistsException;
import com.moin.UserProfile.exceptions.CustomException;
import com.moin.UserProfile.repository.GoalRepository;
import com.moin.UserProfile.repository.UserProfileRepository;
import com.moin.UserProfile.repository.UserRepository;
import com.moin.UserProfile.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoalServiceImpl implements GoalService {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Override
    public void setGoal(GoalDto goalDto) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        if(user.isEmpty())
            throw new CustomException("User is not found");
        GoalEntity existingGoal = goalRepository.findByUserIdAndIsCompleted(user.get().getId(), false);
        if (existingGoal != null) {
            throw new CustomException("You already have an incomplete goal. Complete or update it before setting a new one.");
        }
        String userId = user.get().getId();
        GoalEntity goalEntity = new GoalEntity();
        goalEntity.setWalkM(goalDto.getWalkM());
        goalEntity.setPushUp(goalDto.getPushUp());
        goalEntity.setUserId(userId);
        goalEntity.setIsCompleted(false);
        goalRepository.save(goalEntity);
    }

    @Override
    public String trackGoal(GoalDto goalDto) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> userOptional = userRepository.findByEmail(authentication.getName());

        if (userOptional.isEmpty()) {
            throw new CustomException("User is not found");
        }

        UserEntity user = userOptional.get();
        String userId = user.getId();

        GoalEntity goalOptional = goalRepository.findByUserIdAndIsCompleted(userId,false);
        if (goalOptional == null) {
            throw new CustomException("You don't have any goal or previously set goal is already completed. You cannot track it. Set your goal again.");
        }


        double result = ((((double) goalDto.getWalkM() / goalOptional.getWalkM()) +
                ((double) goalDto.getPushUp() / goalOptional.getPushUp())) / 2.00) * 100.00;

        if (result < 50.00) {
            return "You have to work hard to fulfill your goal. Completion Percentage: " + result;
        } else if (result < 80.00) {
            return "You have done a great job but you have to go more. Completion Percentage: " + result;
        } else {
            goalOptional.setIsCompleted(true);
            goalRepository.save(goalOptional);
            return "Congratulations! You fulfilled your target. Completion Percentage: " + result;
        }
    }
}
