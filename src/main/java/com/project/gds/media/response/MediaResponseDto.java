package com.project.gds.media.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MediaResponseDto {
    private Long mediaId;
    private String mediaUuidTitle; // 난수화된 사진 명
    private String mediaName; // 이미지 명
    private String classification; // 이미지 용도 구분
    private String mediaUploadUrl; // 업로드된 사진 경로 url
    private String localUploadUrl; // 프로젝트 내부에서 업로드된 경로 url (로컬 환경에서 페이지 빌드 시 불러올 프로젝트 이미지 경로)
    private Long contentId; // 속한 프로젝트 id
}
