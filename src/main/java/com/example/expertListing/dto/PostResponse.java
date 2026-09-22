package com.example.expertListing.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {
    private Long id;
    private Long userId;
    private String authorName;
    private String content;
    private String imageUrl;
    private String location;
    private String transactionType;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
}
