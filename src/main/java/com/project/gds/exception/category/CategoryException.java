package com.project.gds.exception.category;

import com.project.gds.category.domain.Category;
import com.project.gds.category.repository.CategoryRepository;
import com.project.gds.category.request.CategoryRequestDto;
import com.project.gds.category.request.CategoryUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class CategoryException implements CategoryExceptionInterface{

    private final CategoryRepository categoryRepository;

    // 중복 카테고리 예외 처리
    @Override
    public boolean checkDuplicateCategory(String categoryName) {

        if(categoryRepository.findByCategoryName(categoryName) != null){
            return true;
        }

        return false;
    }

    // 카테고리 수정 정보 확인
    @Override
    public boolean checkUpdateCategoryInfo(Category category, CategoryUpdateRequestDto categoryRequestDto) {

        if(category.getCategoryName().equals(categoryRequestDto.getUpdateCategoryName()) &&
                category.getOrgCategoryId().equals(Long.parseLong(categoryRequestDto.getUpdateOrgCategory())) &&
                category.getCategoryEngDescription().equals(categoryRequestDto.getUpdateCategoryEngDescription()) &&
                category.getCategoryKorDescription().equals(categoryRequestDto.getUpdateCategoryKorDescription())){
            log.info("기존의 카테고리 정보와 변경점이 없어 수정할 수 없습니다.");
            return true;
        }else if(categoryRequestDto.getUpdateCategoryName() == null ||
                categoryRequestDto.getUpdateCategoryEngDescription() == null ||
                categoryRequestDto.getUpdateCategoryKorDescription() == null ||
                categoryRequestDto.getUpdateOrgCategory() == null){
            log.info("변경하고자 하는 정보는 반드시 입력해야 합니다. (null값 X)");
            return true;
        }

        return false;
    }
}
