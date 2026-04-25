package com.project.common.dto;

public record SearchResultDto(
    String text,
    String documentName,
    String sourceUrl,
    double relevanceScore
) {
}