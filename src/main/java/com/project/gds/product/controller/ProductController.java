package com.project.gds.product.controller;

import com.project.gds.category.domain.Category;
import com.project.gds.category.repository.CategoryRepository;
import com.project.gds.config.FileConfig;
import com.project.gds.media.response.MediaResponseDto;
import com.project.gds.product.request.ProductRequestDto;
import com.project.gds.product.request.ProductUpdateRequestDto;
import com.project.gds.product.response.ProductListResponseDto;
import com.project.gds.product.response.ProductResponseDto;
import com.project.gds.product.service.ProductService;

import com.project.gds.translate.response.product.ProductPageTranslateDataResponseDto;
import com.project.gds.translate.service.TranslateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gds")
@Controller
public class ProductController {

    private final ProductService productService;
    private final TranslateService translateService;
    private final CategoryRepository categoryRepository;

    // [USER]
    // 선택한 카테고리에 따른 프로덕트 페이지 이동 - (2차 업데이트 반영 후)
    // 프로덕트 페이지 (업데이트 후 반영 api)
    @GetMapping("/product/page/{categoryName}")
    public ModelAndView categoryProductPage(@PathVariable String categoryName, HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("카테고리에 따른 프로덕트 페이지 api 진입");

        HttpSession session = request.getSession();

        if (session.getAttribute("auth") == null) {
            session.setAttribute("auth", "user");
        } else if (session.getAttribute("auth").equals("admin")) {
            session.setAttribute("auth", "admin");
        }

        if(session.getAttribute("language") == null){
            session.setAttribute("language", "English");
        }

        String category = URLEncoder.encode(categoryName, "UTF-8");
        category = URLDecoder.decode(category, "ISO-8859-1"); //B서버에서 디코딩
        category = URLDecoder.decode(category, "UTF-8"); //A서버에서 디코딩

        Category accessCategory = categoryRepository.findByCategoryName(categoryName);

        // 세션에 저장된 언어 정보에 따른 텍스트 번역
        if (session.getAttribute("language") == null) {
            session.setAttribute("language", "English");

            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/newProduct"); // 프로덕트 페이지 이동

            // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
            ProductPageTranslateDataResponseDto productPageTranslateDataResponseDto = translateService.translateProductLanguage("English");
            // 프로덕트 정보 리스트들 호출
            List<ProductListResponseDto> productsInfo = productService.categoryProductPage(categoryName, "English");

            mav.addObject("languageData", productPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
            mav.addObject("productsInfo", productsInfo); // 번역된 언어 데이터 저장
            mav.addObject("categoryName", categoryName); // 번역된 언어 데이터 저장
            mav.addObject("categoryId", accessCategory.getCategoryId());
            mav.addObject("categoryKorDescription", accessCategory.getCategoryKorDescription());
            mav.addObject("categoryEngDescription", accessCategory.getCategoryEngDescription());
            mav.addObject("orgCategoryId", accessCategory.getOrgCategoryId().toString());

            return mav;

        } else if (session.getAttribute("language") != null) {
            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/newProduct"); // 프로덕트 페이지 이동

            if (session.getAttribute("language").equals("Korean")) {
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                ProductPageTranslateDataResponseDto productPageTranslateDataResponseDto = translateService.translateProductLanguage("Korean");
                // 프로덕트 정보 리스트들 호출
                List<ProductListResponseDto> productsInfo = productService.categoryProductPage(categoryName, "Korean");

                mav.addObject("languageData", productPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
                mav.addObject("productsInfo", productsInfo); // 번역된 언어 데이터 저장
            } else {
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                ProductPageTranslateDataResponseDto productPageTranslateDataResponseDto = translateService.translateProductLanguage("English");
                // 프로덕트 정보 리스트들 호출
                List<ProductListResponseDto> productsInfo = productService.categoryProductPage(categoryName, "English");

                mav.addObject("languageData", productPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
                mav.addObject("productsInfo", productsInfo); // 번역된 언어 데이터 저장
            }

            mav.addObject("categoryName", categoryName); // 번역된 언어 데이터 저장
            mav.addObject("categoryId", accessCategory.getCategoryId());
            mav.addObject("categoryKorDescription", accessCategory.getCategoryKorDescription());
            mav.addObject("categoryEngDescription", accessCategory.getCategoryEngDescription());
            mav.addObject("orgCategoryId", accessCategory.getOrgCategoryId().toString());

            if (accessCategory.getOrgCategoryId() == 1) {
                mav.addObject("orgCategoryName", "LED HOUSE LIGHT");
            } else if (accessCategory.getOrgCategoryId() == 2) {
                mav.addObject("orgCategoryName", "RD PANEL");
            } else if (accessCategory.getOrgCategoryId() == 3) {
                mav.addObject("orgCategoryName", "DMX SPLITTER");
            } else if (accessCategory.getOrgCategoryId() == 4) {
                mav.addObject("orgCategoryName", "DMX CONTROLLER");
            } else if (accessCategory.getOrgCategoryId() == 5) {
                mav.addObject("orgCategoryName", "THEATRICAL LIGHTING");
            }

            return mav;
        }

        return null;
    }


    // 프로덕트 추가 - (admin, 업데이트 후 반영 api)
    @PostMapping("/product/add")
    public RedirectView productAdd(
            HttpServletRequest request,
            @ModelAttribute("productRequest") ProductRequestDto productRequestDto,
            @RequestParam("categoryName") String categoryName,
            MultipartHttpServletRequest fileRequest) throws IOException {
        log.info("프로덕트 추가 api 진입 - 프로덕트 명 : {}", productRequestDto.getProductName());

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {
            return null;

        } else {
            // 프로덕트 한글 설명
            MultipartFile korFile = fileRequest.getFile("korFile");
            // 프로덕트 영어 설명
            MultipartFile engFile = fileRequest.getFile("engFile");
            // 프로덕트 활용 이미지들
            List<MultipartFile> imgFiles = fileRequest.getFiles("imgFiles");

            // 프로덕트 생성 service 호출
            productService.productAdd(productRequestDto, korFile, engFile, imgFiles);

            String category = URLEncoder.encode(categoryName, "UTF-8");
            category = URLDecoder.decode(category, "ISO-8859-1"); //B서버에서 디코딩
            category = URLDecoder.decode(category, "UTF-8"); //A서버에서 디코딩

            return new RedirectView("/gds/product/page/" + category);
        }

    }


    // 프로덕트 상세 페이지
    @GetMapping("/product/detail/{productId}")
    public ModelAndView productDetailPage(HttpServletRequest request, @PathVariable Long productId) {
        log.info("프로덕트 상세 페이지 api 진입 - 제품 id : {}", productId);

        HttpSession session = request.getSession();

        if (session.getAttribute("language") == null) {
            session.setAttribute("language", "English");
        }

        ProductResponseDto productResponseDto = productService.productDetailInfo(productId);

        // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
        ModelAndView mav = new ModelAndView();
        mav.setViewName("thymeleaf/newProductDetail"); // 프로덕트 상세 페이지 이동
        mav.addObject("productInfo", productResponseDto);

        return mav;
    }


    // 프로덕트 수정
    @PutMapping("/product/update")
    public RedirectView updateProductDetail(
            HttpServletRequest request,
            @RequestParam("categoryName") String categoryName,
            @RequestParam Long productId,
            @ModelAttribute("productUpdateRequest") ProductUpdateRequestDto updateRequestDto,
            MultipartHttpServletRequest updateFileRequest) throws IOException {
        log.info("프로덕트 수정 api 진입 - 제품 id : {}", productId);
        log.info("수정할 설명 : {}", updateRequestDto.getReUploadProductDescript());
        log.info("수정할 스펙 : {}", updateRequestDto.getReUploadSpec());
        log.info("삭제할 이미지들의 id 리스트 값 : {}", updateRequestDto.getDeleteImgList());
        log.info("수정 업데이트할 파일 : {}", updateFileRequest.getFile("reUploadFiles"));
        log.info("수정 업데이트할 이미지 파일 : {}", updateFileRequest.getFiles("reUploadImageFiles").get(0).getOriginalFilename());
        log.info("새로 추가한 이미지 파일 수 : {}", updateFileRequest.getFiles("reUploadImageFiles").size());

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {
            return null;

        } else {
            HttpSession session = request.getSession();

            if (session.getAttribute("language") == null) {
                session.setAttribute("language", "English");
            }

            productService.updateProductDetail(session.getAttribute("language").toString(), productId, updateFileRequest, updateRequestDto);

            String category = URLEncoder.encode(categoryName, "UTF-8");
            category = URLDecoder.decode(category, "ISO-8859-1"); //B서버에서 디코딩
            category = URLDecoder.decode(category, "UTF-8"); //A서버에서 디코딩

            return new RedirectView("/gds/product/page/" + category);
        }
    }


    // 프로덕트 삭제
    @DeleteMapping("/admin/delete/{productId}")
    public String deleteProduct(HttpServletRequest request, @PathVariable Long productId) throws UnsupportedEncodingException {
        log.info("프로덕트 삭제 api 진입 - 삭제 제품 id : {}", productId);

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {
            return null;

        } else {
            HttpSession session = request.getSession();

            if (session.getAttribute("language") == null) {
                session.setAttribute("language", "English");
            }

            Category category = productService.deleteProduct(productId);

            if (category != null) {
                log.info("[SUCCESS] 정상적으로 프로덕트가 삭제되었습니다.");

                String categoryName = URLEncoder.encode(category.getCategoryName(), "UTF-8");
                categoryName = URLDecoder.decode(categoryName, "ISO-8859-1"); //B서버에서 디코딩
                categoryName = URLDecoder.decode(categoryName, "UTF-8"); //A서버에서 디코딩

                return "/gds/product/page/" + category.getCategoryName();

                //return new RedirectView("/gds/admin/product/" + categoryName);
            } else {
                log.info("[ERROR] 프로덕트가 삭제되지 못했습니다.");

                return null;
            }
        }

    }


    // 프로덕트 페이지에 노출되는 활용 이미지들을 따로 분리하기 위해 호출하고자 하는 함수(프로덕트 이미지 리스트 호출)
    @ResponseBody
    @GetMapping("/product/images/{productId}")
    public List<MediaResponseDto> callPrevImages(@PathVariable Long productId) {
        log.info("프로덕트 이미지 수정 시 호출할 기존 등록된 이미지 리스트");

        return productService.callPrevImages(productId);
    }


}
