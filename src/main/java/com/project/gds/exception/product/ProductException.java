package com.project.gds.exception.product;

import com.project.gds.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductException implements ProductExceptionInterface{

    private final ProductRepository productRepository;

    // 중복 프로덕트 예외 처리
    @Override
    public boolean checkDuplicateProduct(String productName) {

        if(productRepository.findByProductName(productName) != null){
            return true;
        }

        return false;
    }
}
