package com.project.gds.download.response;

import com.project.gds.media.response.MediaResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DownloadResponseDto {
    private List<MediaResponseDto> medias; // 제품 활용 이미지들
    private String categoryName; // 카테고리 명
    private List<DownloadProductListResponseDTo> products;
}
