package com.project.gds.download.controller;

import com.project.gds.download.response.DownloadResponseDto;
import com.project.gds.download.service.DownloadService;
import com.project.gds.translate.response.download.DownloadPageTranslateDataResponseDto;
import com.project.gds.translate.service.TranslateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gds")
@Controller
public class DownloadController {

    private final DownloadService downloadService;
    private final TranslateService translateService;


    // 다운로드 페이지 이동
    @ResponseBody
    @GetMapping("/download")
    public ModelAndView downloadPage(HttpServletRequest request){
        log.info("다운로드 페이지 이동 api 진입");

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
            mav.setViewName("thymeleaf/newDownload"); // 프로덕트 페이지 이동

            // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
            DownloadPageTranslateDataResponseDto downloadPageTranslateDataResponseDto = translateService.translateDownloadLanguage("English");

            // 프로덕트 정보 리스트들 호출
            List<DownloadResponseDto> productFiles = downloadService.downloadFileList("English");
            mav.addObject("productFiles", productFiles);
            mav.addObject("languageData", downloadPageTranslateDataResponseDto); // 번역된 언어 데이터 저장

            return mav;
        }else if(session.getAttribute("language") != null){
            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/newDownload"); // 프로덕트 페이지 이동

            if(session.getAttribute("language").equals("Korean")){
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                DownloadPageTranslateDataResponseDto downloadPageTranslateDataResponseDto = translateService.translateDownloadLanguage("Korean");
                // 프로덕트 정보 리스트들 호출
                List<DownloadResponseDto> productFiles = downloadService.downloadFileList("Korean");

                mav.addObject("languageData", downloadPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
                mav.addObject("productFiles", productFiles);
            }else{
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                DownloadPageTranslateDataResponseDto downloadPageTranslateDataResponseDto = translateService.translateDownloadLanguage("English");
                // 프로덕트 정보 리스트들 호출
                List<DownloadResponseDto> productFiles = downloadService.downloadFileList("English");

                mav.addObject("languageData", downloadPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
                mav.addObject("productFiles", productFiles);
            }

            return mav;
        }

        return null;

    }


    // 메뉴 호버 시 보여질 프로덕트 카테고리 리스트
    @ResponseBody
    @GetMapping("/download/catalogs")
    public List<DownloadResponseDto>  getDownloadCatalogList(){
        log.info("다운로드 페이지에 노출되어야 할 다운로드 카탈로그 리스트들 추출 api 진입");

        return downloadService.downloadFileList("English");
    }

}
