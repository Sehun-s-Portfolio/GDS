package com.project.gds.contact.domain;

import com.project.gds.share.TimeStamped;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "tbl_contact")
public class Contact extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long contactId; // 문의 id

    @Column(nullable = false)
    private String contactor; // 문의자

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private String phoneNumber; // 전화번호

    @Column(nullable = false)
    private String email; // 이메일

    @Column(nullable = false)
    private String additionalInfo; // 추가 정보

}
