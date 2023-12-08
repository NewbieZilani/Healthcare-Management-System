package com.moin.CommunityPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long postId;
    private String patientId;
    private String postTitle;
    private String postContent;
    private LocalDateTime postTime;
}
