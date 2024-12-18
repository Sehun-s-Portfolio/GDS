package com.project.gds.contact.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContactResponseDto {
    private String contactor;
    private String address;
    private String phoneNumber;
    private String email;
    private String additionalInfo;
}
