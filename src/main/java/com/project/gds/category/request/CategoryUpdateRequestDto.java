package com.project.gds.category.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryUpdateRequestDto {
    private String updateOrgCategory; //대분류 카테고리
    private String updateCategoryName; // 카테고리 명
    private String updateCategoryKorDescription; // 카테고리 한글 설명
    private String updateCategoryEngDescription; // 카테고리 영어 설명
}
