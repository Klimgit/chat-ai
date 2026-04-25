package com.project.common.dto;

public record SearchRequestDto(
    String question,
    String userId,
    String groupId,
    int limit
) {
    public SearchRequestDto {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("Cannot perform search without userId");
        }
    }
}