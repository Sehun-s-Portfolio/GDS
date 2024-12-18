package com.project.gds.share;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum StatusCode {

    // 성공적인 요청
    OK("J-200", "정상 수행되었습니다."),

    // 잘못된 요청
    CANNOT_CREATE_PRODUCT("J-400", "프로덕트를 생성하지 못했습니다."),
    DUPLICATE_PRODUCT("J-401", "기존에 존재하고 있는 중복된 프로덕트 입니다."),
    DUPLICATE_CATEGORY("J-402", "기존에 존재하고 있는 중복된 카테고리 명입니다.");

    private String statusCode;
    private String statusMessage;

}
