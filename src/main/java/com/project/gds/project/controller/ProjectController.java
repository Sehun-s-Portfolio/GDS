package com.project.gds.project.controller;

import com.project.gds.config.FileConfig;
import com.project.gds.media.response.MediaResponseDto;
import com.project.gds.project.request.ProjectUpdateRequestDto;
import com.project.gds.project.response.ProjectListResponseDto;
import com.project.gds.project.response.ProjectResponseDto;
import com.project.gds.project.service.ProjectService;
import com.project.gds.project.request.ProjectRequestDto;
import com.project.gds.translate.response.project.ProjectPageTranslateDataResponseDto;
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
import java.io.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gds")
@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final TranslateService translateService;

    // 프로젝트 페이지
    @GetMapping("/project")
    public ModelAndView projectPage(HttpServletRequest request) {
        log.info("프로젝트 페이지 api");

        HttpSession session = request.getSession();

        if (session.getAttribute("auth") == null) {
            session.setAttribute("auth", "user");
        } else if (session.getAttribute("auth").equals("admin")) {
            session.setAttribute("auth", "admin");
        }

        // 세션에 저장된 언어 정보에 따른 텍스트 번역
        if (session.getAttribute("language") == null) {
            session.setAttribute("language", "English");

            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/newProject"); // 이동할 프로젝트 페이지 뷰 이름 설정

            // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
            ProjectPageTranslateDataResponseDto projectPageTranslateDataResponseDto = translateService.translateProjectLanguage("English");
            // service 단에서 프로젝트 리스트 가져오기
            List<ProjectListResponseDto> projects = projectService.getProjectList("English");

            mav.addObject("languageData", projectPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
            mav.addObject("projectListInfo", projects); // 페이지로 이동하면서 전달할 프로젝트들 정보

            return mav;

        } else if (session.getAttribute("language") != null) {
            // 선택한 언어 데이터에 따른 텍스트 데이터를 가지고 view 로 넘기기 위한 ModelAndView 생성
            ModelAndView mav = new ModelAndView();
            mav.setViewName("thymeleaf/newProject"); // 이동할 프로젝트 페이지 뷰 이름 설정

            if (session.getAttribute("language").equals("Korean")) {
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                ProjectPageTranslateDataResponseDto projectPageTranslateDataResponseDto = translateService.translateProjectLanguage("Korean");
                // service 단에서 프로젝트 리스트 가져오기
                List<ProjectListResponseDto> projects = projectService.getProjectList("Korean");

                mav.addObject("languageData", projectPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
                mav.addObject("projectListInfo", projects); // 페이지로 이동하면서 전달할 프로젝트들 정보

            } else {
                // 선택한 언어에 따른 번역 반환 (초기에는 한글어 설정)
                ProjectPageTranslateDataResponseDto projectPageTranslateDataResponseDto = translateService.translateProjectLanguage("English");
                // service 단에서 프로젝트 리스트 가져오기
                List<ProjectListResponseDto> projects = projectService.getProjectList("English");

                mav.addObject("languageData", projectPageTranslateDataResponseDto); // 번역된 언어 데이터 저장
                mav.addObject("projectListInfo", projects); // 페이지로 이동하면서 전달할 프로젝트들 정보

            }

            return mav;
        }

        return null;
    }


    // 프로젝트 조회
    @GetMapping("/project/detail/{projectId}")
    public ModelAndView getProject(HttpServletRequest request, @PathVariable Long projectId) {
        log.info("프로젝트 조회 api");
        log.info("프로젝트 id : {}", projectId);

        HttpSession session = request.getSession();

        if (session.getAttribute("auth") == null) {
            session.setAttribute("auth", "user");
        } else if (session.getAttribute("auth").equals("admin")) {
            session.setAttribute("auth", "admin");
        }

        if (session.getAttribute("language") == null) {
            session.setAttribute("language", "English");
        }

        // service 단에서 조회하고자 하는 프로젝트의 정보 호출
        ProjectResponseDto project = projectService.getProject(request, projectId);

        // 선택한 프로젝트의 정보 페이지로 진입 시 보여질 해당 프로젝트의 정보와 이동할 선택 프로젝트 상세 페이지 명 반환할 ModelAndView 객체 생성
        ModelAndView mav = new ModelAndView();
        mav.setViewName("thymeleaf/newProjectDetail"); // 이동할 프로젝트 상세 페이지 뷰 이름 설정
        mav.addObject("projectInfo", project); // 페이지로 이동하면서 전달할 선택한 프로젝트의 상세 정보

        return mav;
    }


    // 프로젝트 추가
    @PostMapping("/project/add")
    public RedirectView projectAdd(
            HttpServletRequest request,
            MultipartHttpServletRequest imageRequest,
            @ModelAttribute("projectRequest") ProjectRequestDto projectRequestDto) throws IOException {
        log.info("프로젝트 추가 api");

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {
            return null;

        } else {
            // view 단으로 프로젝트 생성 시 등록시킬 같이 들어온 이미지 파일들을 리스트 객체에 담아 저장
            List<MultipartFile> projectImages = imageRequest.getFiles("projectImages");
            // service 단으로 등록시킬 이미지들과 requestDto 로 전달받은 프로젝트 정보들으 전달하여 프로젝트 생성 수행
            projectService.addProject(projectImages, projectRequestDto);

            // 프로젝트가 무사히 생성되면 프로젝트 페이지로 리다이렉트
            return new RedirectView("/gds/project");
        }
    }


    // 프로젝트 수정
    @PutMapping("/project/update")
    public RedirectView projectUpdate(
            HttpServletRequest request,
            MultipartHttpServletRequest imageRequest,
            @RequestParam("projectId") String projectId,
            @ModelAttribute("projectRequest") ProjectUpdateRequestDto projectRequestDto) throws IOException {

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {
            return null;

        } else {
            log.info("프로젝트 수정 api");
            log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            log.info("수정한 프로젝트의 설명 : {}", projectRequestDto.getUpdateDescription());
            log.info("수정한 프로젝트의 영어 설명 : {}", projectRequestDto.getUpdateDescriptionEng());
            log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            // 업데이트할 이미지들
            List<MultipartFile> updateImages = imageRequest.getFiles("updateProjectImages");

            if (projectService.projectUpdate(updateImages, Long.parseLong(projectId), projectRequestDto)) {
                log.info("[SUCCESS] 프로젝트 수정이 완료되었습니다.");
            } else {
                log.info("[ERROR] 프로젝트 수정 되지 않았습니다.");
            }

            return new RedirectView("/gds/project/detail/" + Long.parseLong(projectId));
        }

    }

    // 프로젝트 삭제
    @DeleteMapping("/project/delete/{projectId}")
    public RedirectView projectDelete(
            HttpServletRequest request,
            @PathVariable("projectId") String projectId) {
        log.info("프로젝트 삭제 api");

        // 정합성이 검증된 토큰인지 확인
        if (FileConfig.totalWord.isEmpty()) {
            return null;

        } else {
            // 프로젝트 삭제
            if (projectService.projectDelete(Long.parseLong(projectId))) {
                log.info("[SUCCESS] 프로젝트 삭제에 성공하였습니다.");

                return new RedirectView("/gds/project");
            } else {
                log.info("[ERROR] 프로젝트 삭제에 실패하였습니다.");

                return null;
            }
        }
    }


    // 메인 페이지에 보여지게 될 최신 프로젝트 3개
    @ResponseBody
    @GetMapping("/project/list")
    public List<ProjectListResponseDto> getProjectList(HttpServletRequest request) {
        log.info("메인 페이지에 노출시킬 최신  프로젝트 3개 호출 api 진입");

        HttpSession session = request.getSession();

        if (session.getAttribute("language") == null) {
            session.setAttribute("language", "English");
        }

        String language = session.getAttribute("language").toString();

        return projectService.callPojects(language);
    }


    // 프로젝트 이미지 수정 시 보여질 기존 등록된 이미지들
    @ResponseBody
    @GetMapping("/project/images/{projectId}")
    public List<MediaResponseDto> callProjectImages(@PathVariable("projectId") Long projectId) {
        log.info("프로젝트 이미지 수정 시 보여질 기존 등록된 이미지들 조회 api");

        return projectService.callProjectImages(projectId);
    }

}
