package com.project.gds.token.controller;

import com.project.gds.config.FileConfig;
import com.project.gds.token.request.TokenRequestDto;
import com.project.gds.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/token")
@RestController
public class TokenController {

    private final TokenService tokenService;

    // 토큰 생성 및 업데이트
    @PutMapping("/update/insta")
    public String createAndUpdateToken(HttpServletRequest request, @RequestBody TokenRequestDto tokenRequestDto){

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {
            return null;

        }else {

            log.info("업데이트한 토큰 : {}", tokenRequestDto.getToken());

            return tokenService.createAndUpdateToken(tokenRequestDto).getToken();
        }
    }

    // 토큰 호출
    @GetMapping("/get/insta")
    public String callToken(){
        return tokenService.callToken();
    }
}
