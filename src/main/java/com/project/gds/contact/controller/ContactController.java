package com.project.gds.contact.controller;

import com.project.gds.contact.request.ContactRequestDto;
import com.project.gds.contact.service.ContactService;
import com.project.gds.translate.response.contact.ContactPageTranslateDataResponseDto;
import com.project.gds.translate.service.TranslateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gds")
@Controller
public class ContactController {

    private final ContactService contactService;
    private final TranslateService translateService;

    // 문의 사항 페이지
    @GetMapping("/contact")
    public ModelAndView contactPage(HttpServletRequest request) {
        log.info("문의 사항 페이지 이동 api");

        HttpSession session = request.getSession();

        if(session.getAttribute("auth") == null){
            session.setAttribute("auth", "user");
        }else if(session.getAttribute("auth").equals("admin")){
            session.setAttribute("auth", "admin");
        }

        // 세션에 저장된 언어 정보에 따른 텍스트 번역
        if (session.getAttribute("language") == null) {
            session.setAttribute("language", "English");

            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/newContact"); // 문의 페이지 이동

            // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
            ContactPageTranslateDataResponseDto contactPageTranslateDataResponseDto = translateService.translateContactLanguage("English");

            mav.addObject("languageData", contactPageTranslateDataResponseDto); // 번역된 언어 데이터 저장

            return mav;

        } else if (session.getAttribute("language") != null) {
            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/newContact"); // 문의 페이지 이동

            if (session.getAttribute("language").equals("Korean")) {
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                ContactPageTranslateDataResponseDto contactPageTranslateDataResponseDto = translateService.translateContactLanguage("Korean");
                mav.addObject("languageData", contactPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
            } else {
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                ContactPageTranslateDataResponseDto contactPageTranslateDataResponseDto = translateService.translateContactLanguage("English");
                mav.addObject("languageData", contactPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
            }

            return mav;
        }

        return null;
    }


    // 문의 기능 api
    @PostMapping("/contact/inquiry")
    public RedirectView contactInquiry(
            @ModelAttribute("contactRequest") ContactRequestDto contactRequestDto) {
        log.info("문의 작성 기능 api");

        // 문의용 이미지 파일을 리스트에 저장
        //List<MultipartFile> contactImage = imageRequest.getFiles("contactImage");
        // service 단으로 진입하여 실질적으로 문의 사항을 등록시킬 비즈니스로직 수행 (이후 이메일로 직접적으로 문의 메일을 보낼 로직 추가 필요)
        contactService.contactInquiry( contactRequestDto);

        return new RedirectView("/gds/contact");
    }


}
