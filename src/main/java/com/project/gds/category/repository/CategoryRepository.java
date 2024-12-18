package com.project.gds.category.repository;

import com.project.gds.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 카테고리 조회
    Category findByCategoryName(String categoryName);

    // 카테고리 리스트 조회
    List<Category> findAll();

    // 프로덕트에 저장된 카테고리 id를 통한 카테고리 호출
    Category findByCategoryId(Long categoryId);

    // 카테고리 삭제
    void deleteByCategoryId(Long categoryId);

}
