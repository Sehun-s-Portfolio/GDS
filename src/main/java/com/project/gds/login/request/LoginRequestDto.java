package com.project.gds.login.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    private String loginId;
    private String loginPwd;
}
