package com.project.gds.translate.response.contact;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ContactPageTranslateDataResponseDto {
    private String contactHeadText; // 문의 페이지 헤드 텍스트
    private List<String> contactHead; // 개행시킨 문의 페이지 헤드 텍스트
    private String contactInquiryTitleText; // 문의 작성 폼 타이틀 명
    private String contactInquiryText; // 문의 작성 내용 구성 텍스트
    private List<String> contactInquiry; // 개행시킨 문의 작성 내용 구성 텍스트
    private String contactButtonText; // 문의 제출 버튼 텍스트
    private String phone; // 상담원 전화번호
    private String email; // 이메일 주소
    private String helpDesk; // 프론트 전화번호
    private String contactorPlaceHolder;
    private String addressPlaceHolder;
    private String phonePlaceHolder;
    private String emailPlaceHolder;
    private String additionalInfoPlaceHolder;
}
