package com.project.gds.translate.response.about;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AboutPageTranslateDataResponseDto {
    private String aboutHeadText; // 소개 페이지 헤드 텍스트
    private String aboutFirstText; // 소개 페이지 본문 첫번째 내용 텍스트
    private String aboutSecondText; // Our vision 원문
    private List<String> aboutSecond; // Our vision 스플릿 한 텍스트 모음
    private String aboutThirdText; // Heritage 원문
    private List<String> aboutThird; // Heritage 스플릿 한 텍스트 모음
    private String aboutFourthText; // 마지막 본문 내용
    private String manufacturing;
    private List<String> manufacturingTextGroup;
    private String randd;
    private List<String> randdTextGroup;
    private String sales;
    private List<String> saleTextGroup;
    private String account;
    private List<String> accountTextGroup;
    private String techEngineering;
    private List<String> techEngineeringTextGroup;
    private String projectEngineering;
    private List<String> projectEngineeringTextGroup;
}
