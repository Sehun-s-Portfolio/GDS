package com.project.gds.project.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProjectResponseDto {
    private Long projectId; // 프로젝트 id
    private String titleKor; // 프로젝트 타이틀
    private String titleEng; // 프로젝트 타이틀
    private String locationKor; // 프로젝트 수행 위치
    private String locationEng; // 프로젝트 수행 위치
    private String date; // 프로젝트 수행 일자
    private String description; // 프로젝트 상세 정보
    private List<String> descriptionTextGroup; // 프로젝트 상세 정보
    private String descriptionEng; // 영어 상세 정보
    private List<String> descriptionEngTextGroup; // 영어 상세 정보
    private List<String> mediaList; // 프로젝트에 속한 이미지들의 업로드 경로
    private List<Long> mediaIdList; // 프로젝트에 속한 이미지들의 id 리스트
}
