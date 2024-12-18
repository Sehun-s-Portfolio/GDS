package com.project.gds.product.repository;

import com.project.gds.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 프로덕트 이름에 따른 조회
    Product findByProductName(String productName);

    // 프로덕트 id에 따른 조회
    Product findByProductId(Long productId);

    // 카테고리에 따른 프로덕트 리스트 조회
    List<Product> findAllByCategoryId(Long categoryId);

    // 프로덕트 삭제
    void deleteByProductId(Long productId);

    // 카테고리에 속한 프로덕트 삭제
    void deleteAllByCategoryId(Long categoryId);

}
