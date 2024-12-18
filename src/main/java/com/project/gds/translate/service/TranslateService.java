package com.project.gds.translate.service;

import com.project.gds.translate.response.about.AboutPageTranslateDataResponseDto;
import com.project.gds.translate.response.about.AboutPageTranslateEngPageResponseDto;
import com.project.gds.translate.response.about.AboutPageTranslateKorPageResponseDto;
import com.project.gds.translate.response.contact.ContactPageTranslateDataResponseDto;
import com.project.gds.translate.response.contact.ContactPageTranslateEngPageResponseDto;
import com.project.gds.translate.response.contact.ContactPageTranslateKorPageResponseDto;
import com.project.gds.translate.response.download.DownloadPageTranslateDataResponseDto;
import com.project.gds.translate.response.download.DownloadPageTranslateEngResponseDto;
import com.project.gds.translate.response.download.DownloadPageTranslateKorResponseDto;
import com.project.gds.translate.response.main.MainPageTranslateDataResponseDto;
import com.project.gds.translate.response.main.MainPageTranslateEngPageResponseDto;
import com.project.gds.translate.response.main.MainPageTranslateKorPageResponseDto;
import com.project.gds.translate.response.product.ProductPageTranslateDataResponseDto;
import com.project.gds.translate.response.product.ProductPageTranslateEngPageResponseDto;
import com.project.gds.translate.response.product.ProductPageTranslateKorPageResponseDto;
import com.project.gds.translate.response.project.ProjectPageTranslateDataResponseDto;
import com.project.gds.translate.response.project.ProjectPageTranslateEngPageResponseDto;
import com.project.gds.translate.response.project.ProjectPageTranslateKorPageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslateService {

    // 메인 페이지 언어 텍스트 데이터
    private final MainPageTranslateKorPageResponseDto korMainPage;
    private final MainPageTranslateEngPageResponseDto engMainPage;

    // 소개 페이지 언어 텍스트 데이터
    private final AboutPageTranslateKorPageResponseDto korAboutPage;
    private final AboutPageTranslateEngPageResponseDto engAboutPage;

    // 프로덕트 페이지 언어 텍스트 데이터
    private final ProductPageTranslateKorPageResponseDto korProductPage;
    private final ProductPageTranslateEngPageResponseDto engProductPage;

    // 문의 페이지 언어 텍스트 데이터
    private final ContactPageTranslateKorPageResponseDto korContactPage;
    private final ContactPageTranslateEngPageResponseDto engContactPage;

    // 프로젝트 페이지 언어 텍스트 데이터
    private final ProjectPageTranslateKorPageResponseDto korProjectPage;
    private final ProjectPageTranslateEngPageResponseDto engProjectPage;

    // 다운로드 페이지 언어 텍스트 데이터
    private final DownloadPageTranslateKorResponseDto korDownloadPage;
    private final DownloadPageTranslateEngResponseDto engDownloadPage;


    // [메인 페이지] 선택한 언어에 따른 메인 페이지 내용 번역 api
    public MainPageTranslateDataResponseDto translateLanguage(String language) {
        log.info("번역 service 진입 - {}", language);

        // 요청 언어가 한글일 경우 번역
        if (language.equals("Korean")) {
            log.info("확인 2");
            log.info("한글 번역 리스폰스 객체 들어오는지 확인 : {}", korMainPage.getHeadCoverText2());

            // 메인 페이지에 게시된 프로덕트 설명 텍스트 데이터를 . 기호를 기준으로 잘라서 리스트에 저장
            String[] productDescript = korMainPage.getProductDescriptText().split("\\.");
            List<String> productDescriptGroup = new ArrayList<>(Arrays.asList(productDescript));

            // 메인 페이지에 게시된 소개 텍스트 데이터를 . 기호를 기준으로 잘라서 리스트에 저장
            String[] mainAbout = korMainPage.getMainAboutText().split("\\.");
            List<String> mainAboutTextGroup = new ArrayList<>(Arrays.asList(mainAbout));

            // 메인 페이지에 게시된 문의 텍스트 데이터를 . 기호를 기준으로 잘라서 리스트에 저장
            String[] mainContact = korMainPage.getMainContactText().split("\\.");
            List<String> mainContactTextGroup = new ArrayList<>(Arrays.asList(mainContact));

            // 반환 객체에 번역된 데이터 저장
            return MainPageTranslateDataResponseDto.builder()
                    .headCoverText1(korMainPage.getHeadCoverText1()) // 메인 페이지 헤더 커버 텍스트1
                    .headCoverText2(korMainPage.getHeadCoverText2()) // 메인 페이지 헤더 커버 텍스트2
                    .productNameText(korMainPage.getProductNameText()) // 메인 페이지 프로덕트 명 텍스트
                    .productDescriptText(korMainPage.getProductDescriptText()) // 메인 페이지 프로덕트 설명 텍스트
                    .productDescript(productDescriptGroup) // 개행시킬 프로덕트 소개 텍스트 리스트
                    .mainAboutText(korMainPage.getMainAboutText()) // 메인 페이지 소개 텍스트
                    .mainAbout(mainAboutTextGroup) // 개행시킬 소개 텍스트 리스트
                    .mainContactText(korMainPage.getMainContactText()) // 메인 페이지 문의 텍스트
                    .mainContact(mainContactTextGroup) // 개행시킬 문의 텍스트 리스트
                    .getInTouchText(korMainPage.getGetInTouchText()) // 문의 버튼 텍스트
                    .footCoverText1(korMainPage.getFootCoverText1()) // 최하단에 나올 GDS 주소 정보 텍스트1
                    .footCoverText2(korMainPage.getFootCoverText2()) // 최하단에 나올 GDS 주소 정보 텍스트2
                    .footCoverText3(korMainPage.getFootCoverText3()) // 최하단에 나올 GDS 주소 정보 텍스트3
                    .footCoverText4(korMainPage.getFootCoverText4()) // 최하단에 나올 GDS 주소 정보 텍스트4
                    .build();

        // 요청 언어가 영어일 경우
        } else if (language.equals("English")) {

            log.info("확인 2");
            log.info("영어 번역 리스폰스 객체 들어오는지 확인 : {}", engMainPage.getHeadCoverText2());

            // 메인 페이지에 게시된 프로덕트 설명 텍스트 데이터를 . 기호를 기준으로 잘라서 리스트에 저장
            String[] productDescript = engMainPage.getProductDescriptText().split("\\.");
            List<String> productDescriptGroup = new ArrayList<>(Arrays.asList(productDescript));

            // 메인 페이지에 게시된 소개 텍스트 데이터를 . 기호를 기준으로 잘라서 리스트에 저장
            String[] mainAbout = engMainPage.getMainAboutText().split("\\.");
            List<String> mainAboutTextGroup = new ArrayList<>(Arrays.asList(mainAbout));

            // 메인 페이지에 게시된 문의 텍스트 데이터를 . 기호를 기준으로 잘라서 리스트에 저장
            String[] mainContact = engMainPage.getMainContactText().split("\\.");
            List<String> mainContactTextGroup = new ArrayList<>(Arrays.asList(mainContact));

            // 반환 객체에 번역된 데이터 저장
            return MainPageTranslateDataResponseDto.builder()
                    .headCoverText1(engMainPage.getHeadCoverText1()) // 메인 페이지 헤더 커버 텍스트1
                    .headCoverText2(engMainPage.getHeadCoverText2()) // 메인 페이지 헤더 커버 텍스트2
                    .productNameText(engMainPage.getProductNameText()) // 메인 페이지 프로덕트 명 텍스트
                    .productDescriptText(engMainPage.getProductDescriptText()) // 메인 페이지 프로덕트 설명 텍스트
                    .productDescript(productDescriptGroup) // 개행시킬 프로덕트 소개 텍스트 리스트
                    .mainAboutText(engMainPage.getMainAboutText()) // 메인 페이지 소개 텍스트
                    .mainAbout(mainAboutTextGroup) // 개행시킬 소개 텍스트 리스트
                    .mainContactText(engMainPage.getMainContactText()) // 메인 페이지 문의 텍스트
                    .mainContact(mainContactTextGroup) // 개행시킬 문의 텍스트 리스트
                    .getInTouchText(engMainPage.getGetInTouchText()) // 문의 버튼 텍스트
                    .footCoverText1(engMainPage.getFootCoverText1()) // 최하단에 나올 GDS 주소 정보 텍스트1
                    .footCoverText2(engMainPage.getFootCoverText2()) // 최하단에 나올 GDS 주소 정보 텍스트2
                    .footCoverText3(engMainPage.getFootCoverText3()) // 최하단에 나올 GDS 주소 정보 텍스트3
                    .footCoverText4(engMainPage.getFootCoverText4()) // 최하단에 나올 GDS 주소 정보 텍스트4
                    .build();

        }

        return null;
    }


    // [소개 페이지] 선택한 언어에 따른 소개 페이지 내용 번역 api
    public AboutPageTranslateDataResponseDto translateAboutLanguage(String language) {
        log.info("번역 service 진입 - {}", language);

        // 소개 페이지를 한글로 번역할 경우 요청될 한글 텍스트 데이터 반환
        if (language.equals("Korean")) {

            log.info("확인 2");
            log.info("한글 번역 리스폰스 객체 들어오는지 확인 : {}", korAboutPage.getAboutHeadText());

            // 소개 페이지의 본문 내용 중 Our Vision 텍스트를 . 기호로 잘라 리스트에 저장
            String[] aboutSecondText = korAboutPage.getAboutSecondText().split("\\.");
            List<String> aboutSecondGroup = new ArrayList<>(Arrays.asList(aboutSecondText));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] aboutThirdText = korAboutPage.getAboutThirdText().split("\\.");
            List<String> aboutThirdGroup = new ArrayList<>(Arrays.asList(aboutThirdText));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] manufacturing = korAboutPage.getManufacturing().split("\\.");
            List<String> manufacturingTextGroup = new ArrayList<>(Arrays.asList(manufacturing));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] randd = korAboutPage.getRandd().split("\\.");
            List<String> randdTextGroup = new ArrayList<>(Arrays.asList(randd));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] sales = korAboutPage.getSales().split("\\.");
            List<String> saleTextGroup = new ArrayList<>(Arrays.asList(sales));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] account = korAboutPage.getAccount().split("\\.");
            List<String> accountTextGroup = new ArrayList<>(Arrays.asList(account));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] techEngineering = korAboutPage.getTechEngineering().split("\\.");
            List<String> techEngineeringTextGroup = new ArrayList<>(Arrays.asList(techEngineering));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] projectEngineering = korAboutPage.getProjectEngineering().split("\\.");
            List<String> projectEngineeringTextGroup = new ArrayList<>(Arrays.asList(projectEngineering));

            // 반환 객체에 번역된 텍스트 데이터를 저장
            return AboutPageTranslateDataResponseDto.builder()
                    .aboutHeadText(korAboutPage.getAboutHeadText()) // 소개 페이지 헤드 텍스트
                    .aboutFirstText(korAboutPage.getAboutFirstText()) // 소개 페이지 헤드 밑에 기입된 소개 페이지 소개 텍스트
                    .aboutSecondText(korAboutPage.getAboutSecondText()) // 소개 페이지 Our Vision 텍스트 원문
                    .aboutSecond(aboutSecondGroup) // 개행시킬 소개 페이지 Our Vision 텍스트 리스트
                    .aboutThirdText(korAboutPage.getAboutThirdText()) // 소개 페이지 Heritage 텍스트 원문
                    .aboutThird(aboutThirdGroup) // 개행시킬 소개 페이지 Heritage 텍스트 리스트
                    .aboutFourthText(korAboutPage.getAboutFourthText()) // 임시 텍스트
                    .manufacturing(korAboutPage.getManufacturing())
                    .manufacturingTextGroup(manufacturingTextGroup)
                    .randd(korAboutPage.getRandd())
                    .randdTextGroup(randdTextGroup)
                    .sales(korAboutPage.getSales())
                    .saleTextGroup(saleTextGroup)
                    .account(korAboutPage.getAccount())
                    .accountTextGroup(accountTextGroup)
                    .techEngineering(korAboutPage.getTechEngineering())
                    .techEngineeringTextGroup(techEngineeringTextGroup)
                    .projectEngineering(korAboutPage.getProjectEngineering())
                    .projectEngineeringTextGroup(projectEngineeringTextGroup)
                    .build();

        // 소개 페이지를 영문으로 번역할 경우 요청될 한글 텍스트 데이터 반환
        } else if (language.equals("English")) {

            log.info("확인 2");
            log.info("영어 번역 리스폰스 객체 들어오는지 확인 : {}", engAboutPage.getAboutHeadText());

            // 소개 페이지의 본문 내용 중 Our Vision 텍스트를 . 기호로 잘라 리스트에 저장
            String[] aboutSecondText = engAboutPage.getAboutSecondText().split("\\.");
            List<String> aboutSecondGroup = new ArrayList<>(Arrays.asList(aboutSecondText));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] aboutThirdText = engAboutPage.getAboutThirdText().split("\\.");
            List<String> aboutThirdGroup = new ArrayList<>(Arrays.asList(aboutThirdText));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] manufacturing = engAboutPage.getManufacturing().split("\\.");
            List<String> manufacturingTextGroup = new ArrayList<>(Arrays.asList(manufacturing));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] randd = engAboutPage.getRandd().split("\\.");
            List<String> randdTextGroup = new ArrayList<>(Arrays.asList(randd));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] sales = engAboutPage.getSales().split("\\.");
            List<String> saleTextGroup = new ArrayList<>(Arrays.asList(sales));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] account = engAboutPage.getAccount().split("\\.");
            List<String> accountTextGroup = new ArrayList<>(Arrays.asList(account));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] techEngineering = engAboutPage.getTechEngineering().split("\\.");
            List<String> techEngineeringTextGroup = new ArrayList<>(Arrays.asList(techEngineering));

            // 소개 페이지의 본문 내용 중 Heritage 텍스트를 . 기호로 잘라 리스트에 저장
            String[] projectEngineering = engAboutPage.getProjectEngineering().split("\\.");
            List<String> projectEngineeringTextGroup = new ArrayList<>(Arrays.asList(projectEngineering));

            // 반환 객체에 번역된 텍스트 데이터를 저장
            return AboutPageTranslateDataResponseDto.builder()
                    .aboutHeadText(engAboutPage.getAboutHeadText()) // 소개 페이지 헤드 텍스트
                    .aboutFirstText(engAboutPage.getAboutFirstText()) // 소개 페이지 헤드 밑에 기입된 소개 페이지 소개 텍스트
                    .aboutSecondText(engAboutPage.getAboutSecondText()) // 소개 페이지 Our Vision 텍스트 원문
                    .aboutSecond(aboutSecondGroup) // 개행시킬 소개 페이지 Our Vision 텍스트 리스트
                    .aboutThirdText(engAboutPage.getAboutThirdText()) // 소개 페이지 Heritage 텍스트 원문
                    .aboutThird(aboutThirdGroup) // 개행시킬 소개 페이지 Heritage 텍스트 리스트
                    .aboutFourthText(engAboutPage.getAboutFourthText()) // 임시 텍스트
                    .manufacturing(engAboutPage.getManufacturing())
                    .manufacturingTextGroup(manufacturingTextGroup)
                    .randd(engAboutPage.getRandd())
                    .randdTextGroup(randdTextGroup)
                    .sales(engAboutPage.getSales())
                    .saleTextGroup(saleTextGroup)
                    .account(engAboutPage.getAccount())
                    .accountTextGroup(accountTextGroup)
                    .techEngineering(engAboutPage.getTechEngineering())
                    .techEngineeringTextGroup(techEngineeringTextGroup)
                    .projectEngineering(engAboutPage.getProjectEngineering())
                    .projectEngineeringTextGroup(projectEngineeringTextGroup)
                    .build();
        }

        return null;
    }


    // [프로덕트 페이지] 선택한 언어에 따른 프로덕트 페이지 내용 번역 api
    public ProductPageTranslateDataResponseDto translateProductLanguage(String language) {
        log.info("번역 service 진입 - {}", language);

        // 프로덕트 페이지를 한글로 번역할 경우 요청될 한글 텍스트 데이터 반환
        if (language.equals("Korean")) {

            log.info("확인 2");
            log.info("한글 번역 리스폰스 객체 들어오는지 확인 : {}", korProductPage.getProductNameText());

            // 프로덕트 페이지 헤드 텍스트를 . 기호를 기준으로 잘라 담은 리스트 객체
            String[] productHeadText = korProductPage.getProductHeadText().split("\\.");
            List<String> productHeadTextGroup =  new ArrayList<>(Arrays.asList(productHeadText));

            // 프로덕트 페이지 제품 설명 텍스트를 . 기호를 기준으로 잘라 리스트에 저장
            String[] productDescriptText = korProductPage.getProductDescriptText().split("\\.");
            List<String> productDescriptTextGroup = new ArrayList<>(Arrays.asList(productDescriptText));

            // 반환 객체에 번역된 텍스트 데이터 저장
            return ProductPageTranslateDataResponseDto.builder()
                    .productHeadText(korProductPage.getProductHeadText()) // 프로덕트 페이지 헤드 텍스트
                    .productHead(productHeadTextGroup) // 개행시킨 프로덕트 페이지 헤드 텍스트
                    .productNameText(korProductPage.getProductNameText()) // 프로덕트 명 텍스트
                    .productDescriptText(korProductPage.getProductDescriptText()) // 프로덕트 설명 텍스트
                    .productDescript(productDescriptTextGroup) // 개행시킨 프로덕트 설명 텍스트
                    .build();

        // 영어로 번역 요청될 경우 텍스트 데이터 반환
        } else if (language.equals("English")) {

            log.info("확인 2");
            log.info("영어 번역 리스폰스 객체 들어오는지 확인 : {}", engProductPage.getProductNameText());

            // 프로덕트 페이지 헤드 텍스트를 . 기호를 기준으로 잘라 담은 리스트 객체
            String[] productHeadText = engProductPage.getProductHeadText().split("\\.");
            List<String> productHeadTextGroup =  new ArrayList<>(Arrays.asList(productHeadText));

            // 프로덕트 페이지 제품 설명 텍스트를 . 기호를 기준으로 잘라 리스트에 저장
            String[] productDescriptText = engProductPage.getProductDescriptText().split("\\.");
            List<String> productDescriptTextGroup = new ArrayList<>(Arrays.asList(productDescriptText));

            // 반환 객체에 번역된 텍스트 데이터 저장
            return ProductPageTranslateDataResponseDto.builder()
                    .productHeadText(engProductPage.getProductHeadText())// 프로덕트 페이지 헤드 텍스트
                    .productHead(productHeadTextGroup) // 개행시킨 프로덕트 페이지 헤드 텍스트
                    .productNameText(engProductPage.getProductNameText()) // 프로덕트 명 텍스트
                    .productDescriptText(engProductPage.getProductDescriptText()) // 프로덕트 설명 텍스트
                    .productDescript(productDescriptTextGroup) // 개행시킨 프로덕트 설명 텍스트
                    .build();

        }

        return null;
    }


    // [문의 페이지] 선택한 언어에 따른 문의 페이지 내용 번역 api
    public ContactPageTranslateDataResponseDto translateContactLanguage(String language) {
        log.info("번역 service 진입 - {}", language);

        // 문의 페이지를 한글로 번역할 경우 텍스트 데이터 객체 반환
        if (language.equals("Korean")) {

            log.info("확인 2");
            log.info("한글 번역 리스폰스 객체 들어오는지 확인 : {}", korContactPage.getContactHeadText());

            // 문의 페이지 헤드 텍스트를 . 기호를 기준으로 잘라 리스트 객체에 저장
            String[] contactHeadText = korContactPage.getContactHeadText().split("\\.");
            List<String> contactHeadTextGroup =  new ArrayList<>(Arrays.asList(contactHeadText));

            // 문의 페이지 문의 요청 텍스트를 . 기호를 기준으로 잘라 리스트 객체에 저장
            String[] contactInquiryText = korContactPage.getContactInquiryText().split("\\. ");
            List<String> contactInquiryTextGroup = new ArrayList<>(Arrays.asList(contactInquiryText));

            // 반환 객체에 번역된 텍스트 데이터 저장
            return ContactPageTranslateDataResponseDto.builder()
                    .contactHeadText(korContactPage.getContactHeadText()) // 문의 페이지 헤드 텍스트 원문
                    .contactHead(contactHeadTextGroup) // 개행시킬 문의 페이지 헤드 텍스트 리스트
                    .contactInquiryTitleText(korContactPage.getContactInquiryTitleText()) // 문의 페이지 문의 작성 폼 타이틀 명
                    .contactInquiryText(korContactPage.getContactInquiryText()) // 문의 페이지 문의 작성 내용 텍스트 원문
                    .contactInquiry(contactInquiryTextGroup) // 개행시킬 문의 페이지 문의 작성 내용 텍스트 리스트
                    .contactButtonText(korContactPage.getContactButtonText()) // 문의 제출 버튼 텍스트
                    .phone(korContactPage.getPhone()) // 상담원 전화번호 정보 텍스트
                    .email(korContactPage.getEmail()) // 이메일 정보 텍스트
                    .helpDesk(korContactPage.getHelpDesk()) // 프론트 전화번호 텍스트
                    .contactorPlaceHolder(korContactPage.getContactorPlaceHolder())
                    .addressPlaceHolder(korContactPage.getAddressPlaceHolder())
                    .phonePlaceHolder(korContactPage.getPhonePlaceHolder())
                    .emailPlaceHolder(korContactPage.getEmailPlaceHolder())
                    .additionalInfoPlaceHolder(korContactPage.getAdditionalInfoPlaceHolder())
                    .build();

        // 문의 페이지를 영어로 번역 요청했을 경우 텍스트 데이터 반환
        } else if (language.equals("English")) {

            log.info("확인 2");
            log.info("영어 번역 리스폰스 객체 들어오는지 확인 : {}", engProductPage.getProductNameText());

            // 문의 페이지 헤드 텍스트를 . 기호를 기준으로 잘라 리스트 객체에 저장
            String[] contactHeadText = engContactPage.getContactHeadText().split("\\.");
            List<String> contactHeadTextGroup =  new ArrayList<>(Arrays.asList(contactHeadText));

            // 문의 페이지 문의 요청 텍스트를 . 기호를 기준으로 잘라 리스트 객체에 저장
            String[] contactInquiryText = engContactPage.getContactInquiryText().split("\\. ");
            List<String> contactInquiryTextGroup = new ArrayList<>(Arrays.asList(contactInquiryText));

            // 반환 객체에 번역된 텍스트 데이터 저장
            return ContactPageTranslateDataResponseDto.builder() // 문의 페이지 헤드 텍스트 원문
                    .contactHeadText(engContactPage.getContactHeadText())
                    .contactHead(contactHeadTextGroup) // 개행시킬 문의 페이지 헤드 텍스트 리스트
                    .contactInquiryTitleText(engContactPage.getContactInquiryTitleText()) // 문의 페이지 문의 작성 폼 타이틀 명
                    .contactInquiryText(engContactPage.getContactInquiryText()) // 문의 페이지 문의 작성 내용 텍스트 원문
                    .contactInquiry(contactInquiryTextGroup) // 개행시킬 문의 페이지 문의 작성 내용 텍스트 리스트
                    .contactButtonText(engContactPage.getContactButtonText()) // 문의 제출 버튼 텍스트
                    .phone(engContactPage.getPhone()) // 상담원 전화번호 정보 텍스트
                    .email(engContactPage.getEmail()) // 이메일 정보 텍스트
                    .helpDesk(engContactPage.getHelpDesk()) // 프론트 전화번호 텍스트
                    .contactorPlaceHolder(engContactPage.getContactorPlaceHolder())
                    .addressPlaceHolder(engContactPage.getAddressPlaceHolder())
                    .phonePlaceHolder(engContactPage.getPhonePlaceHolder())
                    .emailPlaceHolder(engContactPage.getEmailPlaceHolder())
                    .additionalInfoPlaceHolder(engContactPage.getAdditionalInfoPlaceHolder())
                    .build();

        }

        return null;
    }


    // [프로젝트 페이지] 선택한 언어에 따른 프로젝트 페이지 내용 번역 api
    public ProjectPageTranslateDataResponseDto translateProjectLanguage(String language) {
        log.info("번역 service 진입 - {}", language);

        // 프로젝트 페이지 한글로 번역 요청했을 경우 텍스트 데이터 반환
        if (language.equals("Korean")) {

            log.info("확인 2");
            log.info("한글 번역 리스폰스 객체 들어오는지 확인 : {}", korProjectPage.getProjectHeadText());

            // 프로젝트 페이지 헤드 텍스트를  .기호를 기준으로 잘라 리스트 객체에 저장
            String[] projectHeadText = korProjectPage.getProjectHeadText().split("\\.");
            List<String> projectHeadTextGroup =  new ArrayList<>(Arrays.asList(projectHeadText));

            // 반환 객체에 텍스트 데이터 저장
            return ProjectPageTranslateDataResponseDto.builder()
                    .projectHeadText(korProjectPage.getProjectHeadText()) // 프로젝트 페이지 헤드 텍스트 원문
                    .projectHead(projectHeadTextGroup) // 개행시킬 프로젝트 페이지 헤드 텍스트 리스트
                    .build();

        // 프로젝트 페이지 영어로 번역 요청했을 경우 텍스트 데이터 반환
        } else if (language.equals("English")) {

            log.info("확인 2");
            log.info("영어 번역 리스폰스 객체 들어오는지 확인 : {}", engProjectPage.getProjectHeadText());

            // 프로젝트 페이지 헤드 텍스트를  .기호를 기준으로 잘라 리스트 객체에 저장
            String[] projectHeadText = engProjectPage.getProjectHeadText().split("\\.");
            List<String> projectHeadTextGroup =  new ArrayList<>(Arrays.asList(projectHeadText));

            // 반환 객체에 텍스트 데이터 저장
            return ProjectPageTranslateDataResponseDto.builder()
                    .projectHeadText(engProjectPage.getProjectHeadText()) // 프로젝트 페이지 헤드 텍스트 원문
                    .projectHead(projectHeadTextGroup) // 개행시킬 프로젝트 페이지 헤드 텍스트 리스트
                    .build();

        }

        return null;
    }


    // [다운로드 페이지] 선택한 언어에 따른 다운로드 페이지 내용 번역 api
    public DownloadPageTranslateDataResponseDto translateDownloadLanguage(String language) {
        log.info("번역 service 진입 - {}", language);

        // 다운로드 페이지 한글로 번역 요청했을 경우 텍스트 데이터 반환
        if (language.equals("Korean")) {

            log.info("확인 2");
            log.info("한글 번역 리스폰스 객체 들어오는지 확인 : {}", korDownloadPage.getDownloadHeadText());

            // 다운로드 페이지 헤드 텍스트를  .기호를 기준으로 잘라 리스트 객체에 저장
            String[] downloadHeadText = korDownloadPage.getDownloadHeadText().split("\\.");
            List<String> downloadHeadTextGroup =  new ArrayList<>(Arrays.asList(downloadHeadText));

            // 반환 객체에 텍스트 데이터 저장
            return DownloadPageTranslateDataResponseDto.builder()
                    .downloadHeadText(korDownloadPage.getDownloadHeadText())
                    .downloadHeadTextGroup(downloadHeadTextGroup)
                    .build();

            // 다운로드 페이지 영어로 번역 요청했을 경우 텍스트 데이터 반환
        } else if (language.equals("English")) {

            log.info("확인 2");
            log.info("영어 번역 리스폰스 객체 들어오는지 확인 : {}", engDownloadPage.getDownloadHeadText());

            // 다운로드 페이지 헤드 텍스트를  .기호를 기준으로 잘라 리스트 객체에 저장
            String[] downloadHeadText = engDownloadPage.getDownloadHeadText().split("\\.");
            List<String> downloadHeadTextGroup =  new ArrayList<>(Arrays.asList(downloadHeadText));

            // 반환 객체에 텍스트 데이터 저장
            return DownloadPageTranslateDataResponseDto.builder()
                    .downloadHeadText(engDownloadPage.getDownloadHeadText())
                    .downloadHeadTextGroup(downloadHeadTextGroup)
                    .build();

        }

        return null;
    }
}
