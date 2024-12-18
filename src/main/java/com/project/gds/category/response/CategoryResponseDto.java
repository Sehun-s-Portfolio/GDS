package com.project.gds.category.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CategoryResponseDto {
    private Long orgCategoryId; // 대분류 카테고리 id
    private Long categoryId; // 카테고리 id
    private String categoryName; // 카테고리 명
    private List<String> productList; // 카테고리에 속한 프로덕트
}
