package com.project.gds.translate.response.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@PropertySource(value = "classpath:/application-translate.properties")
@Builder
@Getter
@Component
public class ContactPageTranslateEngPageResponseDto {
    @Value("${gds.contact.eng.contactHeadText}")
    private String contactHeadText;

    @Value("${gds.contact.eng.contactInquiryTitleText}")
    private String contactInquiryTitleText;

    @Value("${gds.contact.eng.contactInquiryText}")
    private String contactInquiryText;

    @Value("${gds.contact.eng.contactButtonText}")
    private String contactButtonText;

    @Value("${gds.contact.eng.phone}")
    private String phone;

    @Value("${gds.contact.eng.email}")
    private String email;

    @Value("${gds.contact.eng.helpDesk}")
    private String helpDesk;

    @Value("${gds.contact.eng.placeholder.contactor}")
    private String contactorPlaceHolder;

    @Value("${gds.contact.eng.placeholder.address}")
    private String addressPlaceHolder;

    @Value("${gds.contact.eng.placeholder.phone}")
    private String phonePlaceHolder;

    @Value("${gds.contact.eng.placeholder.email}")
    private String emailPlaceHolder;

    @Value("${gds.contact.eng.placeholder.info}")
    private String additionalInfoPlaceHolder;
}
