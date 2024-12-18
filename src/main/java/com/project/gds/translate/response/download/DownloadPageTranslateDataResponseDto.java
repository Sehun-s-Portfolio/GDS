package com.project.gds.translate.response.download;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DownloadPageTranslateDataResponseDto {
    private String downloadHeadText;
    private List<String> downloadHeadTextGroup;
}
