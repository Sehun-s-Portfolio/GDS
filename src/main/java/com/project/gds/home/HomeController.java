package com.project.gds.home;

import com.project.gds.project.response.ProjectListResponseDto;
import com.project.gds.project.service.ProjectService;
import com.project.gds.translate.response.main.MainPageTranslateDataResponseDto;
import com.project.gds.translate.service.TranslateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final TranslateService translateService;
    private final ProjectService projectService;

    @Value("${instagram.token}")
    private String instagramToken;

    // main 페이지
    @RequestMapping("/")
    public ModelAndView mainPage(HttpServletRequest request, HttpServletResponse response){
        log.info("메인 페이지 이동 api 진입");

        HttpSession session = request.getSession();

        // 세션 추가 (일반 고객)
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
            mav.setViewName("thymeleaf/new"); // 메인 페이지 이동

            // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
            MainPageTranslateDataResponseDto mainPageTranslateData = translateService.translateLanguage("English");
            List<ProjectListResponseDto> threeProjects =  projectService.callPojects("English");

            mav.addObject("languageData", mainPageTranslateData); // 번역된 언어 데이터 저장
            mav.addObject("instagramToken", instagramToken);

            if(threeProjects != null){
                if(threeProjects.size() == 3){
                    mav.addObject("firstProject",threeProjects.get(0));
                    mav.addObject("secondProject",threeProjects.get(1));
                    mav.addObject("thirdProject",threeProjects.get(2));
                }else if(threeProjects.size() == 2){
                    mav.addObject("firstProject",threeProjects.get(0));
                    mav.addObject("secondProject",threeProjects.get(1));
                }else if(threeProjects.size() == 1){
                    mav.addObject("firstProject",threeProjects.get(0));
                }
            }else{
                mav.addObject("firstProject",null);
                mav.addObject("secondProject",null);
                mav.addObject("thirdProject",null);
            }

            return mav;

        }else if(session.getAttribute("language") != null){
            log.info("현재 선택한 언어 정보 : {}", session.getAttribute("language").toString());

            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/new"); // 메인 페이지 이동

            if(session.getAttribute("language").toString().equals("Korean")){

                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                MainPageTranslateDataResponseDto mainPageTranslateData = translateService.translateLanguage("Korean");
                List<ProjectListResponseDto> threeProjects =  projectService.callPojects("Korean");

                mav.addObject("languageData", mainPageTranslateData); // 번역된 언어 데이터 저장
                mav.addObject("firstProject",threeProjects.get(0));
                mav.addObject("secondProject",threeProjects.get(1));
                mav.addObject("thirdProject",threeProjects.get(2));
            }else{

                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                MainPageTranslateDataResponseDto mainPageTranslateData = translateService.translateLanguage("English");
                List<ProjectListResponseDto> threeProjects =  projectService.callPojects("English");

                mav.addObject("languageData", mainPageTranslateData); // 번역된 언어 데이터 저장
                mav.addObject("firstProject",threeProjects.get(0));
                mav.addObject("secondProject",threeProjects.get(1));
                mav.addObject("thirdProject",threeProjects.get(2));
            }

            mav.addObject("instagramToken", instagramToken);

            return mav;
        }

        return null;
    }

    
    // 메인 페이지 (admin)
    @RequestMapping("/admin")
    public String adminMainPage(){
        log.info("관리자용 관리자 로그인 페이지 접근");
        
        return "thymeleaf/newAdmin";
    }

}
