package com.project.gds.product.response;

import com.project.gds.media.response.MediaResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProductResponseDto {
    // [product]
    private Long productId; // 프로덕트 id
    private String productName; // 프로덕트 명
    private String productDescriptKor; // 프로덕트 설명 한글
    private List<String> descriptKorGroup; // 프로덕트 설명 한글
    private String productDescriptEng; // 프로덕트 설명 영어
    private List<String> descriptEngGroup; // 프로덕트 설명 영어
    private String productSpecKor; // 프로덕트 스펙 (한글)
    private List<String> specKorGroup; // 프로덕트 스펙 (한글)
    private String productSpecEng; // 프로덕트 스펙 (영어)
    private List<String> specEngGroup; // 프로덕트 스펙 (한글)
    //private String menuExpression; // 프로덕트 메뉴 노출 여부
    private String fileKorUploadUrl; // 프로덕트 한글 파일 업로드 절대 경로
    private String fileEngUploadUrl; // 프로덕트 영어 파일 업로드 절대 경로
    private String fileKorLocalUploadUrl; // 프로덕트 한글 파일 프로젝트 내부 업로드 경로
    private String fileEngLocalUploadUrl; // 프로덕트 영어 파일 프로젝트 내부 업로드 경로
    private String productImgUrl; // 프로덕트 썸네일 이미지 url
    private String localProductImgUrl; // 프로덕트 썸네일 이미지 프로젝트 내부 업로드 경로
    private Long categoryId; // 프로덕트 카테고리

    // [Image]
    private List<MediaResponseDto> mediaList; // 제품에 속한 이미지 파일 및 일반 파일들 리스트
}
