package com.moin.UserProfile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "goal")
public class GoalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;
    private Integer walkM;
    private Integer pushUp;
    private String userId;
    private Boolean isCompleted;
//    @OneToMany
//    @JoinColumn(name = "userId")
//    private UserEntity user;
}
