package com.moin.UserProfile.controller;

import com.moin.UserProfile.dto.GoalDto;
import com.moin.UserProfile.exceptions.CustomException;
import com.moin.UserProfile.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients/goal")
public class GoalController {
    @Autowired
    private GoalService goalService;
    @PostMapping("/set")
    public ResponseEntity<?> setGoal(@RequestBody GoalDto goalDto) throws CustomException {
        goalService.setGoal(goalDto);
        return ResponseEntity.status(HttpStatus.OK).body("Goal successfully set");
    }
    @PostMapping("/track")
    public ResponseEntity<String> trackGoal(@RequestBody GoalDto goalDto) {
        try {
            String result = goalService.trackGoal(goalDto);
            return ResponseEntity.ok(result);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
