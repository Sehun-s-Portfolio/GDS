package com.project.gds.product.response;

import com.project.gds.media.response.MediaResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProductListResponseDto {
    private Long productId; // 프로덕트 id
    private String thumbnailImg; // 프로덕트 썸네일 이미지
    private String productName; // 프로덕트 제품명
    private String realFileName; // pdf 파일 명
    private List<String> productDescript; // 프로덕트 설명
    private List<String> productSpec; // 프로덕트 스펙
    private String productFile; // 프로덕트 파일
    private String productFileName; // 프로덕트 파일
    private List<MediaResponseDto> imgList; // 프로덕트 활용 이미지들
    private String categoryName; // 카테고리 명
    private Long categoryId;
}
