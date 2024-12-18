package com.project.gds.category.controller;

import com.project.gds.category.domain.Category;
import com.project.gds.category.request.CategoryRequestDto;
import com.project.gds.category.request.CategoryUpdateRequestDto;
import com.project.gds.category.response.CategoryResponseDto;
import com.project.gds.category.service.CategoryService;
import com.project.gds.config.FileConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gds")
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 생성 - (admin)
    @PostMapping("/category/add")
    public RedirectView categoryAdd(
            HttpServletRequest request,
            @ModelAttribute("categoryRequest") CategoryRequestDto categoryRequestDto,
            @RequestParam("page") String page) throws UnsupportedEncodingException {
        log.info("카테고리 생성 api 진입 - 카테고리 : {}", categoryRequestDto.getCategoryName());
        log.info("카테고리 생성 후 리다이렉트 될 페이지 : {}", page);

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {

            return null;

        } else {

            categoryService.categoryAdd(categoryRequestDto);

            HttpSession session = request.getSession();

            if (!page.equals("main")) {
                if (!page.contains("project") && !page.contains("about") && !page.contains("download") && !page.contains("contact")) {

                    String category = URLEncoder.encode(page, "UTF-8");
                    category = URLDecoder.decode(category, "ISO-8859-1"); //B서버에서 디코딩
                    category = URLDecoder.decode(category, "UTF-8"); //A서버에서 디코딩

                    log.info("카테고리 re 파싱 : {}", category);

                    return new RedirectView("/gds/product/" + category);
                }
                return new RedirectView("/gds/" + page);
            } else {
                // 프로젝트가 무사히 생성되면 프로젝트 페이지로 리다이렉트
                return new RedirectView("/");
            }
        }

    }


    // 카테고리 삭제 - (admin)
    @DeleteMapping("/category/delete/{categoryId}")
    public RedirectView categoryDelete(
            HttpServletRequest request,
            @PathVariable Long categoryId) {
        log.info("카테고리 삭제 api 진입 - 카테고리 id : {}", categoryId);

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {
            return null;

        } else {
            HttpSession session = request.getSession();

            if (categoryService.categoryDelete(categoryId) == null) {
                log.info("[SUCCESS] 정상적으로 카테고리가 삭제되었습니다.");

                // 프로젝트가 무사히 생성되면 프로젝트 페이지로 리다이렉트
                return new RedirectView("/");
            } else {
                log.info("[ERROR] 정상적으로 삭제되지 않았습니다.");

                return null;
            }
        }
    }


    // 카테고리 수정 - (admin)
    @PutMapping("/category/update")
    public RedirectView categoryUpdate(
            HttpServletRequest request,
            @RequestParam("categoryId") Long categoryId,
            @ModelAttribute("categoryUpdateRequest") CategoryUpdateRequestDto categoryRequestDto) throws UnsupportedEncodingException {
        log.info("카테고리 수정 api 진입 - 카테고리 id : {}", categoryId);

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {
            return null;

        } else {
            HttpSession session = request.getSession();

            Category updateCategory = categoryService.categoryUpdate(categoryId, categoryRequestDto);

            if (updateCategory != null) {
                log.info("[SUCCESS] 정상적으로 카테고리 수정이 완료되었습니다.");

                String category = URLEncoder.encode(updateCategory.getCategoryName(), "UTF-8");
                category = URLDecoder.decode(category, "ISO-8859-1"); //B서버에서 디코딩
                category = URLDecoder.decode(category, "UTF-8"); //A서버에서 디코딩

                // 프로젝트가 무사히 생성되면 프로젝트 페이지로 리다이렉트
                return new RedirectView("/gds/product/" + category);
            } else {
                log.info("[ERROR] 카테고리 수정에 실패하였습니다.");

                return null;
            }
        }

    }


    // 메뉴 호버 시 보여질 프로덕트 카테고리 리스트
    @ResponseBody
    @GetMapping("/category")
    public List<CategoryResponseDto> categoryList() {
        log.info("메뉴 호버 카테고리 리스트 조회 api 진입");

        return categoryService.categoryList();
    }
}
