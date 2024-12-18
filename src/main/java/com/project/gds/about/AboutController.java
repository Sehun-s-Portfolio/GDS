package com.project.gds.about;

import com.project.gds.translate.response.about.AboutPageTranslateDataResponseDto;
import com.project.gds.translate.service.TranslateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gds")
@Controller
public class AboutController {

    private final TranslateService translateService;

    // 소개 페이지 (일반 유저)
    @GetMapping("/about")
    public ModelAndView aboutPage(HttpServletRequest request){
        log.info("소개 페이지 이동 api 진입");

        HttpSession session = request.getSession();

        if(session.getAttribute("auth") == null){
            session.setAttribute("auth", "user");
        }else if(session.getAttribute("auth").equals("admin")){
            session.setAttribute("auth", "admin");
        }

        // 세션에 저장된 언어 정보에 따른 텍스트 번역
        if(session.getAttribute("language") == null){
            session.setAttribute("language", "English");

            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/newAbout"); // 소개 페이지 이동

            // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
            AboutPageTranslateDataResponseDto aboutPageTranslateDataResponseDto = translateService.translateAboutLanguage("English");
            mav.addObject("languageData", aboutPageTranslateDataResponseDto); // 번역된 언어 데이터 저장

            return mav;

        }else if(session.getAttribute("language") != null){
            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/newAbout"); // 소개 페이지 이동

            if(session.getAttribute("language").equals("Korean")){
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                AboutPageTranslateDataResponseDto aboutPageTranslateDataResponseDto = translateService.translateAboutLanguage("Korean");
                mav.addObject("languageData", aboutPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
            }else{
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                AboutPageTranslateDataResponseDto aboutPageTranslateDataResponseDto = translateService.translateAboutLanguage("English");
                mav.addObject("languageData", aboutPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
            }

            return mav;
        }

        return null;
    }

}
