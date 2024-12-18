package com.project.gds.project.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectUpdateRequestDto {
    private String updateTitleKor; // 프로젝트 타이틀
    private String updateTitleEng; // 프로젝트 영어 타이틀
    private String updateLocationKor; // 프로젝트 수행 장소 위치
    private String updateLocationEng; // 프로젝트 수행 장소 위치 (영어)
    private String updateDate; // 프로젝트 수행 일자
    private String updateDescription; // 프로젝트 상세 설명
    private String updateDescriptionEng; // 프로젝트 상세 설명 (영어)
    private String deleteImgList; // 기존 이미지들 중에서 삭제하고자 하는 이미지들의 id 가 담길 변수
}
