package com.project.gds.project.domain;

import com.project.gds.share.TimeStamped;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_project")
public class Project extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Id
    private Long projectId; // 프로젝트 id

    @Column(nullable = false)
    private String titleKor; // 프로젝트 한글타이틀

    @Column(nullable = false)
    private String titleEng; // 프로젝트 영문 타이틀

    @Column(nullable = false)
    private String locationKor; // 위치 (한글)

    @Column(nullable = false)
    private String locationEng; // 위치 (영문)

    @Column(nullable = false)
    private String date; // 날짜

    @Column(columnDefinition = "LONGTEXT")
    private String description; // 상세 정보

    @Column(columnDefinition = "LONGTEXT")
    private String descriptionEng; // 상세 정보 (영문)

    @Column
    private String projectImg; // 프로젝트 썸네일 이미지 (url)

    @Column
    private String localImgUrl; // 프로젝트 썸네일 이미지 내부 업로드 경로 (resources/static/upload)
}
