package com.project.gds.login.controller;

import com.project.gds.config.FileConfig;
import com.project.gds.login.request.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gds")
@Controller
public class LoginController {

    @Value("${login.admin.account.id}")
    private String adminId;

    @Value("${login.admin.account.pwd}")
    private String adminPwd;

    // 어드민 계정 로그인
    @PostMapping("/login")
    public void loginAdmin(
            @ModelAttribute("loginRequest") LoginRequestDto loginRequestDto,
            @RequestParam("page") String page,
           HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("관리자 계정 로그인 api 진입");

        HttpSession session = request.getSession();

        if (adminId.equals(loginRequestDto.getLoginId()) && adminPwd.equals(loginRequestDto.getLoginPwd())) {
            log.info("로그인 성공");

            FileConfig.totalWord = adminPwd + adminId;

            session.setAttribute("auth", "admin");
            session.setAttribute("language", "English");

            if (page.equals("main")) {
                log.info("로그인 후 메인 페이지 이동 시도");

                response.sendRedirect("/");
            } else if (page.contains("project") && page.contains("detail")) {
                String project = page.replace("project/detail/", "");
                log.info("프로젝트 디테일 로그인 확인 : {}", project);

                response.sendRedirect("/gds/project/detail/" + Long.parseLong(project));
                //return "redirect:/gds/project/detail/" + Long.parseLong(project);
            } else if (page.contains("product")) {
                String productCategory = page.replace("product/", "");
                String category = URLEncoder.encode(productCategory, "UTF-8");
                category = URLDecoder.decode(category, "ISO-8859-1"); //B서버에서 디코딩
                category = URLDecoder.decode(category, "UTF-8");
                log.info("프로덕트 리스트 로그인 확인 : {}", category);

                response.sendRedirect("/gds/product/page/" + category);
                //return "redirect:/gds/product/page/" + category;
            } else {
                log.info("나머지 페이지 로그인 확인 : {}", page);

                response.sendRedirect("/gds/" + page);
                //return "redirect:/gds/" + page;
            }

        } else {
            log.info("로그인 실패");

            session.setAttribute("auth", "user");

            response.sendRedirect("/admin");
            //return "redirect:/admin";
        }

    }

    // 계정 로그아웃
    @DeleteMapping("/logout")
    public String logoutAdmin(
            HttpServletRequest request,
            @RequestParam("page") String page) throws UnsupportedEncodingException {
        log.info("관리자 계정 로그아웃 api 진입");

        HttpSession session = request.getSession();
        session.setAttribute("auth", "user");

        if (page.equals("main")) {
            return "redirect:/";
        } else if (page.contains("project") && page.contains("detail")) {
            String project = page.replace("project/detail/", "");
            log.info("프로젝트 디테일 로그아웃 확인 : {}", project);

            return "redirect:/gds/project/detail/" + Long.parseLong(project);
        } else if (page.contains("product")) {
            String productCategory = page.replace("product/", "");
            String category = URLEncoder.encode(productCategory, "UTF-8");
            category = URLDecoder.decode(category, "ISO-8859-1"); //B서버에서 디코딩
            category = URLDecoder.decode(category, "UTF-8");
            log.info("프로덕트 리스트 로그아웃 확인 : {}", category);

            return "redirect:/gds/product/" + category;
        } else {
            log.info("나머지 페이지 로그아웃 확인 : {}", page);

            return "redirect:/gds/" + page;
        }
    }

}
