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
public class AboutPageTranslateEngPageResponseDto {
    @Value("${gds.about.eng.aboutHeadText}")
    private String aboutHeadText; // 메인 페이지 헤드 커버 텍스트

    @Value("${gds.about.eng.aboutFirstText}")
    private String aboutFirstText; // 제품 이름 텍스트

    @Value("${gds.about.eng.aboutSecondText}")
    private String aboutSecondText; // 제품 설명 텍스트

    @Value("${gds.about.eng.aboutThirdText}")
    private String aboutThirdText; // 메인 페이지 소개 텍스트

    @Value("${gds.about.eng.aboutFourthText}")
    private String aboutFourthText; // 메인 페이지 문의 텍스트

    @Value("${gds.about.eng.manufacturing}")
    private String manufacturing;

    @Value("${gds.about.eng.r&d}")
    private String randd;

    @Value("${gds.about.eng.sales}")
    private String sales;

    @Value("${gds.about.eng.account}")
    private String account;

    @Value("${gds.about.eng.tech.engineering}")
    private String techEngineering;

    @Value("${gds.about.eng.project.engineering}")
    private String projectEngineering;
}
