package com.project.gds.translate.response.download;

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
public class DownloadPageTranslateEngResponseDto {
    @Value("${gds.download.eng.downloadHeadText}")
    private String downloadHeadText; // 다운로드 페이지 헤드 텍스트 영어 번역
}
