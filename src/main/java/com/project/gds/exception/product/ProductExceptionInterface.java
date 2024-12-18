package com.project.gds.exception.product;

import org.springframework.stereotype.Component;

@Component
public interface ProductExceptionInterface {
    /** 중복 프로덕트 예외 처리 **/
    boolean checkDuplicateProduct(String productName);
}
