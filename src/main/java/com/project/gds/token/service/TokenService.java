package com.project.gds.token.service;

import com.project.gds.token.domain.Token;
import com.project.gds.token.repository.TokenRepository;
import com.project.gds.token.request.TokenRequestDto;
import com.project.gds.token.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    // 토큰 생성 혹은 업데이트
    @Transactional
    public TokenResponseDto createAndUpdateToken(TokenRequestDto tokenRequestDto){

        if(!tokenRepository.findAll().isEmpty()){
            tokenRepository.deleteAll();
        }

        Token token = tokenRepository.save(
                Token.builder()
                        .token(tokenRequestDto.getToken())
                        .build()
        );

        return TokenResponseDto.builder()
                .token(token.getToken())
                .build();
    }


    // 토큰 호출
    public String callToken(){
        Token token = tokenRepository.findAll().get(0);
        return token.getToken();
    }
}
