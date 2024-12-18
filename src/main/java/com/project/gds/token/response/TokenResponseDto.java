package com.project.gds.token.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponseDto {
    private String token;
}
