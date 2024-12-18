package com.project.gds.translate.response.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@PropertySource(value = "classpath:/application-translate.properties")
@Builder
@Getter
@Component
public class ProjectPageTranslateEngPageResponseDto {
    @Value("${gds.project.eng.projectHeadText}")
    private String projectHeadText;
}
