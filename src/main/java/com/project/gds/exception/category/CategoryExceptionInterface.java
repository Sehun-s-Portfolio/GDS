package com.project.gds.exception.category;

import com.project.gds.category.domain.Category;
import com.project.gds.category.request.CategoryRequestDto;
import com.project.gds.category.request.CategoryUpdateRequestDto;
import org.springframework.stereotype.Component;

@Component
public interface CategoryExceptionInterface {
    /** 중복된 카테고리 예외 처리 **/
    boolean checkDuplicateCategory(String categoryName);

    /** 카테고리 수정 정보 확인 **/
    boolean checkUpdateCategoryInfo(Category category, CategoryUpdateRequestDto categoryRequestDto);
}
