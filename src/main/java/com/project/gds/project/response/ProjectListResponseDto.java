package com.project.gds.project.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProjectListResponseDto { // -- 프로젝트 페이지 진입 시 보여질 프로젝트들의 일부 정보들을 담고있을 Dto 객체
    private Long projectId; // 프로젝트 id
    private String location;// 프로젝트의 수행 위치
    private String title; // 프로젝트 타이틀
    private String date; // 프로젝트 수행 일자
    private String uploadUrl; // 프로젝트 썸네일 이미지 절대 경로
    private String localUploadUrl; // 프로젝트 썸네일 이미지 static 프로젝트 내부 업로드 경로
}
