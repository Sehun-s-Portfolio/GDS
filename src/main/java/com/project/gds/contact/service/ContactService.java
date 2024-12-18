package com.project.gds.contact.service;

import com.project.gds.contact.domain.Contact;
import com.project.gds.contact.repository.ContactRepository;
import com.project.gds.contact.request.ContactRequestDto;
import com.project.gds.contact.response.ContactResponseDto;
import com.project.gds.email.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final MailService mailService;

    // 문의 작성 기능 service
    public ContactResponseDto contactInquiry(ContactRequestDto contactRequestDto) {
        log.info("문의 작성 기능 service 진입 - 문의자 : {}", contactRequestDto.getContactor());

        Contact contact = Contact.builder()
                .contactor(contactRequestDto.getContactor()) // 문의자
                .address(contactRequestDto.getAddress()) // 문의자 주소
                .phoneNumber(contactRequestDto.getPhoneNumber()) // 문의자 전화번호
                .email(contactRequestDto.getEmail()) // 문의자 이메일
                .additionalInfo(contactRequestDto.getAdditionalInfo()) // 문의자 추가 정보
                .build();

        // 문의 작성 내역 저장
        contactRepository.save(contact);

        log.info("[Success] 문의 사항 생성 완료");

        // 이메일 보내기
        if(mailService.sendSimpleEmail(contactRequestDto)){
            return ContactResponseDto.builder()
                    .contactor(contact.getContactor())
                    .address(contact.getAddress())
                    .phoneNumber(contact.getPhoneNumber())
                    .email(contact.getEmail())
                    .additionalInfo(contact.getAdditionalInfo())
                    .build();
        }else{
            return null;
        }
    }
}
