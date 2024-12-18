package com.project.gds.translate.response.main;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MainPageTranslateDataResponseDto {
    private String headCoverText1; // 메인 페이지 헤드 커버 텍스트2
    private String headCoverText2; // 메인 페이지 헤드 커버 텍스트2
    private String productNameText; // 제품 이름 텍스트
    private String productDescriptText; // 제품 설명 텍스트 V
    private List<String> productDescript; // 개행시킨 제품 설명 텍스트 모음
    private String mainAboutText; // 메인 페이지 소개 텍스트 V
    private List<String> mainAbout; // 개행시킨 메인 페이지 소개 텍스트 모음
    private String mainContactText; // 메인 페이지 문의 텍스트 V
    private List<String> mainContact; // 개행시킨 메인 페이지 문의 텍스트 모음
    private String getInTouchText; // 메인 페이지 문의 버튼 텍스트
    private String footCoverText1; // 메인 페이지 최하단 커버 텍스트
    private String footCoverText2; // 메인 페이지 최하단 커버 텍스트 2
    private String footCoverText3; // 메인 페이지 최하단 커버 텍스트 3
    private String footCoverText4; // 메인 페이지 최하단 커버 텍스트 4
}
