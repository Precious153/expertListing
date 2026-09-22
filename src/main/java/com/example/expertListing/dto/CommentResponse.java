package com.example.expertListing.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private Long userId;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
}
