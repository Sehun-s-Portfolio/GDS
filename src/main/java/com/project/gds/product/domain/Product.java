package com.project.gds.product.domain;

import com.project.gds.share.TimeStamped;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "tbl_product")
public class Product extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long productId; // 프로덕트 id

    @Column(nullable = false)
    private String productName; // 프로덕트 명

    @Column
    private String productEngName; // 프로덕트 영문 명

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String productDescriptKor; // 프로덕트 설명 한글

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String productDescriptEng; // 프로덕트 설명 영어

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String productSpecKor; // 프로덕트 스펙 (한글)

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String productSpecEng; // 프로덕트 스펙 (영어)

    @Column
    private String menuExpression; // 프로덕트 메뉴 노출 여부

    @Column
    private String fileKorName;

    @Column
    private String fileEngName;

    @Column(nullable = false)
    private String fileKorUploadUrl; // 프로덕트 한글 파일 업로드 절대 경로

    @Column(nullable = false)
    private String fileEngUploadUrl; // 프로덕트 영어 파일 업로드 절대 경로

    @Column(nullable = false)
    private String fileKorLocalUploadUrl; // 프로덕트 한글 파일 프로젝트 내부 업로드 경로

    @Column(nullable = false)
    private String fileEngLocalUploadUrl; // 프로덕트 영어 파일 프로젝트 내부 업로드 경로

    @Column(nullable = false)
    private String productImgUrl; // 프로덕트 썸네일 이미지 url

    @Column(nullable = false)
    private String localProductImgUrl; // 프로덕트 썸네일 이미지 프로젝트 내부 업로드 경로

    @Column(nullable = false)
    private Long categoryId; // 카테고리 id
}
