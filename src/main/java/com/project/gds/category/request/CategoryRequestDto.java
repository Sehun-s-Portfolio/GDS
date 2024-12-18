package com.project.gds.category.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryRequestDto {
    private String orgCategory; //대분류 카테고리
    private String categoryName; // 카테고리 명
    private String categoryKorDescription; // 카테고리 한글 설명
    private String categoryEngDescription; // 카테고리 영어 설명
}
