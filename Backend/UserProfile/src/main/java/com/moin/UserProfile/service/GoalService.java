package com.moin.UserProfile.service;

import com.moin.UserProfile.dto.GoalDto;
import com.moin.UserProfile.exceptions.CustomException;

public interface GoalService {
    void setGoal(GoalDto goalDto) throws CustomException;
    String trackGoal(GoalDto goalDto) throws CustomException;
}
