package com.project.gds.download.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DownloadProductListResponseDTo {
    private String productName;
    private String productFile;
    private String productFileName;
}
