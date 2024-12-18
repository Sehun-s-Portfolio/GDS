package com.project.gds.project.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectRequestDto {
    private String titleKor; // 프로젝트 타이틀
    private String titleEng; // 프로젝트 영어 타이틀
    private String locationKor; // 프로젝트 수행 장소 위치
    private String locationEng; // 프로젝트 수행 장소 위치 (영어)
    private String date; // 프로젝트 수행 일자
    private String description; // 프로젝트 상세 설명
    private String descriptionEng; // 프로젝트 상세 설명 (영어)
}
