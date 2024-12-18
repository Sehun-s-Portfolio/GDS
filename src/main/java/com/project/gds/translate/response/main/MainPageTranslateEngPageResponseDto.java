package com.project.gds.translate.response.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@PropertySource(value = "classpath:/application-translate.properties")
@Builder
@Getter
@Component
public class MainPageTranslateEngPageResponseDto {
    @Value("${gds.main.eng.headCoverText1}")
    private String headCoverText1; // 메인 페이지 헤드 커버 텍스트

    @Value("${gds.main.eng.headCoverText2}")
    private String headCoverText2; // 메인 페이지 헤드 커버 텍스트

    @Value("${gds.main.eng.productNameText}")
    private String productNameText; // 제품 이름 텍스트

    @Value("${gds.main.eng.productDescriptText}")
    private String productDescriptText; // 제품 설명 텍스트

    @Value("${gds.main.eng.mainAboutText}")
    private String mainAboutText; // 메인 페이지 소개 텍스트

    @Value("${gds.main.eng.mainContactTitleText}")
    private String mainContactTitleText; // 메인 페이지 문의 텍스트

    @Value("${gds.main.eng.mainContactText}")
    private String mainContactText; // 메인 페이지 문의 텍스트

    @Value("${gds.main.eng.getInTouchText}")
    private String getInTouchText; // 메인 페이지 문의 버튼 텍스트

    @Value("${gds.main.eng.footCoverText1}")
    private String footCoverText1; // 메인 페이지 최하단 커버 텍스트

    @Value("${gds.main.eng.footCoverText2}")
    private String footCoverText2; // 메인 페이지 최하단 커버 텍스트 2

    @Value("${gds.main.eng.footCoverText3}")
    private String footCoverText3; // 메인 페이지 최하단 커버 텍스트 3

    @Value("${gds.main.eng.footCoverText4}")
    private String footCoverText4; // 메인 페이지 최하단 커버 텍스트 4
}
