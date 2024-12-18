package com.project.gds.product.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ShowProductResponseDto {
    private Long productId; // 프로덕트 id
    private String productName; // 프로덕트 명
    private String productImgUrl; // 프로덕트 썸네일 이미지 url
    private String productDescriptKor; // 프로덕트 설명 한글
    private List<String> descriptKorGroup; // 프로덕트 설명 한글
    private String productDescriptEng; // 프로덕트 설명 영어
    private List<String> descriptEngGroup; // 프로덕트 설명 영어
}
