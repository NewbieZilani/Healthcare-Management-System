package com.moin.CommunityPortal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Post")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "post_title", nullable = false, length = 250)
    private String postTitle;

    @Column(name = "post_content", nullable = false)
    @Lob
    private String postContent;

    @Column(name = "post_time", nullable = false)
    private LocalDateTime postTime;
    private Boolean isAvailable;
}
