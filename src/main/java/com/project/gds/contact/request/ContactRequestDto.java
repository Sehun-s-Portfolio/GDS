package com.project.gds.contact.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactRequestDto {
    private String contactor; // 문의자
    private String address; // 문의자 주소
    private String phoneNumber; // 문의자 전화번호
    private String email; // 문의자 이메일
    private String additionalInfo; // 문의자 추가 정보
}
