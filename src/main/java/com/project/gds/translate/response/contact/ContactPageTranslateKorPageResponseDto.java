package com.project.gds.translate.response.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@PropertySource(value = "classpath:/application-translate.properties")
@Builder
@Getter
@Component
public class ContactPageTranslateKorPageResponseDto {
    @Value("${gds.contact.kor.contactHeadText}")
    private String contactHeadText;

    @Value("${gds.contact.kor.contactInquiryTitleText}")
    private String contactInquiryTitleText;

    @Value("${gds.contact.kor.contactInquiryText}")
    private String contactInquiryText;

    @Value("${gds.contact.kor.contactButtonText}")
    private String contactButtonText;

    @Value("${gds.contact.kor.phone}")
    private String phone;

    @Value("${gds.contact.kor.email}")
    private String email;

    @Value("${gds.contact.kor.helpDesk}")
    private String helpDesk;

    @Value("${gds.contact.kor.placeholder.contactor}")
    private String contactorPlaceHolder;

    @Value("${gds.contact.kor.placeholder.address}")
    private String addressPlaceHolder;

    @Value("${gds.contact.kor.placeholder.phone}")
    private String phonePlaceHolder;

    @Value("${gds.contact.kor.placeholder.email}")
    private String emailPlaceHolder;

    @Value("${gds.contact.kor.placeholder.info}")
    private String additionalInfoPlaceHolder;
}
