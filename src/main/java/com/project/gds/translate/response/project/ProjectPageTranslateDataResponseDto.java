package com.project.gds.translate.response.project;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProjectPageTranslateDataResponseDto {
    private String projectHeadText;
    private List<String> projectHead;
}
