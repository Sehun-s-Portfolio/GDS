package com.project.gds.product.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductRequestDto {
    private String categoryName; // 카테고리 명
    //private String menuExpression; // 메뉴 노출 여부
    private String productName; // 프로덕트 명
    private String productDescriptKor; // 프로덕트 설명 한글
    private String productDescriptEng; // 프로덕트 설명 영어
    private String productSpecKor; // 프로덕트 스펙 (한글)
    private String productSpecEng; // 프로덕트 스펙 (영어)
}
