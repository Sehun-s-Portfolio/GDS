package com.project.gds.translate.response.about;

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
public class AboutPageTranslateKorPageResponseDto {
    @Value("${gds.about.kor.aboutHeadText}")
    private String aboutHeadText; // 메인 페이지 헤드 커버 텍스트

    @Value("${gds.about.kor.aboutFirstText}")
    private String aboutFirstText; // 제품 이름 텍스트

    @Value("${gds.about.kor.aboutSecondText}")
    private String aboutSecondText; // 제품 설명 텍스트

    @Value("${gds.about.kor.aboutThirdText}")
    private String aboutThirdText; // 메인 페이지 소개 텍스트

    @Value("${gds.about.kor.aboutFourthText}")
    private String aboutFourthText; // 메인 페이지 문의 텍스트

    @Value("${gds.about.kor.manufacturing}")
    private String manufacturing;

    @Value("${gds.about.kor.r&d}")
    private String randd;

    @Value("${gds.about.kor.sales}")
    private String sales;

    @Value("${gds.about.kor.account}")
    private String account;

    @Value("${gds.about.kor.tech.engineering}")
    private String techEngineering;

    @Value("${gds.about.kor.project.engineering}")
    private String projectEngineering;

}
