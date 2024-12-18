package com.project.gds.category.domain;

import com.project.gds.share.TimeStamped;
import lombok.*;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "tbl_category")
public class Category extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long categoryId; // 카테고리 id

    @Column
    private Long orgCategoryId; // 속한 대분류 카테고리 id

    @Column(nullable = false)
    private String categoryName; // 카테고리 명

    @Column(nullable = false)
    private String categoryKorDescription; // 카테고리 한글 설명

    @Column(nullable = false)
    private String categoryEngDescription; // 카테고리 영어 설명

}
