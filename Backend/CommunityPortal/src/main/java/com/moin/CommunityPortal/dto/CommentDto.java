package com.moin.CommunityPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private String patientId;
    private Long postId;  // Assuming you want to include postId in the DTO
    private String commentContent;
    private LocalDateTime commentTime;
}
