package com.project.gds.translate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/translate")
@Controller
public class TraslateController {

    // [메인 페이지] 선택한 언어에 따른 메인 페이지 내용 번역 api
    @RequestMapping("/{language}/{page}")
    public RedirectView translateMainLanguage(HttpServletRequest request, @PathVariable String language, @PathVariable String page) {
        log.info("페이지 번역 api 진입");
        log.info("번역 언어 : {}", language);
        log.info("번역 페이지 : {}", page);

        // 세션에 현재 번역된 언어 정보 저장
        if (language.contains("Korean")) {
            request.getSession().setAttribute("language", "Korean");
        } else if (language.contains("English")) {
            request.getSession().setAttribute("language", "English");
        }

        if (request.getSession().getAttribute("language") == null) {
            log.info("언어 정보 존재하지 않음 ㅜㅜ");
        } else {
            log.info("언어 정보 존재 ! - {}", request.getSession().getAttribute("language"));
        }

        // 번역 요청 페이지에 따른 각 페이지 진입 경로
        switch (page) {
            case "main":
                log.info("번역 페이지 - {}", page);
                return new RedirectView("/");
            case "about": // 소개 페이지
                log.info("번역 페이지 - {}", page);
                return new RedirectView("/gds/about");
            case "contact": // 문의 페이지
                log.info("번역 페이지 - {}", page);
                return new RedirectView("/gds/contact");
            case "download": // 다운로드 페이지
                log.info("번역 페이지 - {}", page);
                return new RedirectView("/gds/download");
            case "project": // 다운로드 페이지
                log.info("번역 페이지 - {}", page);
                return new RedirectView("/gds/project");
        }

        // 프로덕트 페이지 및 프로덕트 상세 페이지 번역 설정 시 타고 들어갈 경로
        if (page.contains("product")) {
            if (page.contains("detail")) { // 프로덕트 상세 페이지
                log.info("번역 페이지 - {}", page);
                return new RedirectView("/gds/" + page);
            }
            log.info("번역 페이지 - {}", page);
            return new RedirectView("/gds/" + page); // 프로덕트 카테고리 페이지
        }

        return null;
    }


    // 선택 프로젝트 조회 페이지 번역
    @RequestMapping("/project/detail")
    public RedirectView translateProjectDetail(HttpServletRequest request, @RequestParam("language") String language, @RequestParam("projectId") String projectId) {
        log.info("프로젝트 디테일 페이지 번역 api 진입");
        log.info("번역 언어 : {}", language);
        log.info("번역 프로젝트 id : {}", Long.parseLong(projectId));

        // 세션에 현재 번역된 언어 정보 저장
        if (language.contains("Korean")) {
            request.getSession().setAttribute("language", "Korean");
        } else if (language.contains("English")) {
            request.getSession().setAttribute("language", "English");
        }

        if (request.getSession().getAttribute("language") == null) {
            log.info("언어 정보 존재하지 않음");
        } else {
            log.info("언어 정보 존재 ! - {}", request.getSession().getAttribute("language"));
        }

        return new RedirectView("/gds/project/detail/" + Long.parseLong(projectId));

    }

    // 프로덕트 카테고리 조회 페이지 번역
    @RequestMapping("/product")
    public RedirectView translateProductCategory(HttpServletRequest request, @RequestParam("language") String language, @RequestParam("category") String category) throws UnsupportedEncodingException {
        log.info("프로덕트 카테고리 페이지 번역 api 진입");
        log.info("번역 언어 : {}", language);
        log.info("번역 카테고리: {}", category);

        // 세션에 현재 번역된 언어 정보 저장
        if (language.contains("Korean")) {
            request.getSession().setAttribute("language", "Korean");
        } else if (language.contains("English")) {
            request.getSession().setAttribute("language", "English");
        }

        String categoryName = URLEncoder.encode(category, "UTF-8");
        categoryName = URLDecoder.decode(categoryName, "ISO-8859-1"); //B서버에서 디코딩
        categoryName = URLDecoder.decode(categoryName, "UTF-8"); //A서버에서 디코딩

        if (request.getSession().getAttribute("language") == null) {
            log.info("언어 정보 존재하지 않음 ㅜㅜ");
        } else {
            log.info("언어 정보 존재 ! - {}", request.getSession().getAttribute("language"));
        }

        // session이 관리자일 경우
        return new RedirectView("/gds/product/page/" + categoryName);
    }

    // 프로덕트 디테일 조회 페이지 번역
    @RequestMapping("/product/detail/{language}/{productId}")
    public RedirectView translateProductDetail(HttpServletRequest request, @PathVariable String language, @PathVariable String productId) {
        log.info("프로덕트 상세 페이지 번역 api 진입");
        log.info("번역 언어 : {}", language);
        log.info("번역 프로덕트 id : {}", Long.parseLong(productId));

        // 세션에 현재 번역된 언어 정보 저장
        if (language.contains("Korean")) {
            request.getSession().setAttribute("language", "Korean");
        } else if (language.contains("English")) {
            request.getSession().setAttribute("language", "English");
        }

        if (request.getSession().getAttribute("language") == null) {
            log.info("언어 정보 존재하지 않음 ㅜㅜ");
        } else {
            log.info("언어 정보 존재 ! - {}", request.getSession().getAttribute("language"));
        }

        return new RedirectView("/gds/product/detail/" + Long.parseLong(productId));

    }

}
