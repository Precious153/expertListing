package com.example.expertListing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message = "Content is required")
    private String content;
    private String imageUrl;
    private String location;
    private String transactionType;
}
