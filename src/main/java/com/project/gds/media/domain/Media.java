package com.project.gds.media.domain;

import javax.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "tbl_media")
public class Media {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long mediaId;

    @Column(nullable = false)
    private String mediaUuidTitle; // 난수화된 사진 명

    @Column(nullable = false)
    private String mediaName; // 이미지 명

    @Column(nullable = false)
    private String classification; // 이미지 용도 구분

    @Column(nullable = false)
    private String mediaUploadUrl; // 업로드된 사진 경로 url

    @Column(nullable = false)
    private String localUploadUrl; // 프로젝트 내부에서 업로드된 경로 url (로컬 환경에서 페이지 빌드 시 불러올 프로젝트 이미지 경로)

    @Column
    private Long contentId; // 속한 프로젝트 id
}
