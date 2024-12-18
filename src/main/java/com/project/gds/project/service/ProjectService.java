package com.project.gds.project.service;

import com.project.gds.media.domain.Media;
import com.project.gds.media.repository.MediaRepository;
import com.project.gds.media.response.MediaResponseDto;
import com.project.gds.media.service.MediaUploadInterface;
import com.project.gds.project.domain.Project;
import com.project.gds.project.repository.ProjectRepository;
import com.project.gds.project.request.ProjectRequestDto;
import com.project.gds.project.request.ProjectUpdateRequestDto;
import com.project.gds.project.response.ProjectListResponseDto;
import com.project.gds.project.response.ProjectResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {

    private final MediaUploadInterface mediaUploadInterface;
    private final ProjectRepository projectRepository;
    private final MediaRepository mediaRepository;
    private final EntityManager entityManager;

    @Value("${deploy.upload.path}")
    private String deployServerPath; // 업로드한 서버의 url 주소


    // 프로젝트 페이지 리스트 조회
    public List<ProjectListResponseDto> getProjectList(String language) {
        log.info("조회 프로젝트 페이지 service 진입 : 프로젝트 id");

        // 프로젝트들을 생성일자 기준으로 최신순으로 정렬하여 리스트에 담기
        List<Project> projectList = projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        // 불러온 프로젝트들을 반환할 객체에 저장하여 넘길 리스트 객체 생성
        List<ProjectListResponseDto> projects = new ArrayList<>();

        // 불러온 프로젝트들을 조회하여 반환 리스트 객체에 저장
        for (Project eachProject : projectList) {

            log.info("이미지 업로드 절대 경로 : {}", eachProject.getProjectImg());
            log.info("이미지 프로젝트 내부 업로드 경로 : {}", eachProject.getLocalImgUrl());

            // 한글 번역 상태일 경우 한글로 번역된 프로젝트의 데이터 정보를 projects에 저장
            if (language.equals("Korean")) {

                projects.add(
                        ProjectListResponseDto.builder()
                                .projectId(eachProject.getProjectId()) // 프로젝트 id
                                .location(eachProject.getLocationKor()) // 프로젝트 수행 위치
                                .title(eachProject.getTitleKor()) // 프로젝트 타이틀
                                .date(eachProject.getDate()) // 프로젝트 수행 일자
                                .uploadUrl(eachProject.getProjectImg()) // 프로젝트 썸네일 이미지 업로드 절대 경로
                                .localUploadUrl(eachProject.getLocalImgUrl()) // 프로젝트 썸네일 이미지 업로드 static 프로젝트 내부 경로
                                .build()
                );

            // 영어 번역 상태일 경우 영어로 번역된 프로젝트의 데이터 정보를 projects에 저장
            } else if (language.equals("English")) {

                projects.add(
                        ProjectListResponseDto.builder()
                                .projectId(eachProject.getProjectId()) // 프로젝트 id
                                .location(eachProject.getLocationEng()) // 프로젝트 수행 위치
                                .title(eachProject.getTitleEng()) // 프로젝트 타이틀
                                .date(eachProject.getDate()) // 프로젝트 수행 일자
                                .uploadUrl(eachProject.getProjectImg()) // 프로젝트 썸네일 이미지 업로드 절대 경로
                                .localUploadUrl(eachProject.getLocalImgUrl()) // 프로젝트 썸네일 이미지 업로드 static 프로젝트 내부 경로
                                .build()
                );

            }

        }

        return projects;
    }


    // 선택 프로젝트 조회
    public ProjectResponseDto getProject(HttpServletRequest request, Long projectId) {
        log.info("조회 프로젝트 service 진입 : 프로젝트 id = {}", projectId);

        // 프로젝트 조회
        Project project = projectRepository.findByProjectId(projectId);
        // 프로젝트에 속한 이미지들의 경로와 이미지의 고유 id를 뽑아 저장하기 위한 list 객체 생성
        List<String> imageList = new ArrayList<>(); // 이미지 경로
        List<Long> imageIdLIst = new ArrayList<>(); // 이미지 고유 id

        // 선택한 프로젝트에 해당되는 이미지 파일들을 조회
        List<Media> detailImageList = mediaRepository.findAllByContentIdAndClassification(project.getProjectId(), "project");

        // 불러온 이미지 파일들을 하나씩 조회
        for (int k = 0 ; k < detailImageList.size() ; k++) {
            // 각 이미지 파일들의 경로를 imageList에 저장
            imageList.add(detailImageList.get(k).getMediaUploadUrl());

            // 만약 불러온 이미지들 중에서 프로젝트의 썸네일 이미지와 일치하는 media 가 존재한다면 imageIdList에 고정 id 0을 저장
            if(project.getLocalImgUrl().contains(detailImageList.get(k).getMediaUuidTitle())){
                imageIdLIst.add(0L);

            // 썸네일 이미지 파일을 제외한 다른 활용 이미지들의 경우 imageIdList에 각 이미지들의 고유 id를 저장
            }else{
                imageIdLIst.add(detailImageList.get(k).getMediaId());
            }
        }

        // 프로젝트 페이지 제품 설명 텍스트를 <> 기호를 기준으로 잘라 리스트에 저장
        String[] descriptK = project.getDescription().split("<>");
        List<String> descriptKor = new ArrayList<>(); // <> 기호로 개행 분리시킨 각 설명 텍스트들을 담을 List 생성
        Collections.addAll(descriptKor, descriptK); // 저장된 프로젝트의 한글 설명을 <> 기호 기준으로 List에 저장

        // 프로덕트 페이지 제품 설명 텍스트를 <> 기호를 기준으로 잘라 리스트에 저장
        String[] descriptE = project.getDescriptionEng().split("<>");
        List<String> descriptEng = new ArrayList<>(); // <> 기호로 개행 분리시킨 각 설명 텍스트들을 담을 List 생성
        Collections.addAll(descriptEng, descriptE); // 저장된 프로젝트의 한글 설명을 <> 기호 기준으로 List에 저장

        // 선택한 프로젝트의 정보들을 반환 객체에 담아 반환
        return ProjectResponseDto.builder()
                .titleKor(project.getTitleKor()) // 프로젝트 한글 타이틀
                .titleEng(project.getTitleEng()) // 프로젝트 영어 타이틀
                .locationKor(project.getLocationKor()) // 프로젝트 수행 위치 (한글)
                .locationEng(project.getLocationEng()) // 프로젝트 수행 위치 (영어)
                .date(project.getDate()) // 프로젝트 수행 일자
                .description(project.getDescription()) // 프로젝트 원래 한글 설명
                .descriptionTextGroup(descriptKor) // 개행되어 List에 저장된 프로젝트 한글 설명 정보
                .descriptionEng(project.getDescriptionEng()) // 프로젝트 원래 영어 설명
                .descriptionEngTextGroup(descriptEng) // 개행되어 List에 저장된 프로젝트 영어 설명 정보
                .mediaList(imageList) // 프로젝트에 속한 이미지들 업로드 경로
                .mediaIdList(imageIdLIst) // 프로젝트에 속한 이미지들 id 리스트
                .build();

    }

    // 프로젝트 추가
    public void addProject(List<MultipartFile> projectImages, ProjectRequestDto projectRequestDto) throws IOException {
        log.info("프로젝트 추가 service 진입");

        // 이미지 파일들 업로드 절대 경로
        /** 회사 자체 업로드 서버 경로 **/
        //String uploadFilePath = File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "project" + File.separator;
        /** GDS 측 가비아 업로드 서버 경로 **/
        String uploadFilePath = File.separator + "web" + File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "project" + File.separator;

        // 썸네일 이미지 파일 난수화 파일 명
        String thumbnailUuidFileName = mediaUploadInterface.createServerFileName(projectImages.get(0).getOriginalFilename());
        // 배포 서버 외부에 저장된 이미지를 호출하는 콜 path
        String callImagePath = deployServerPath + "project/" + thumbnailUuidFileName;

        // DB에 저장시켜 놓을 썸네일 이미지 업로드 경로
        String localUploadUrl = mediaUploadInterface.getFullPath(thumbnailUuidFileName, uploadFilePath);

        // 실제 들어온 첫 번째 썸네일 이미지를 업로드 경로에 업로드
        File reUploadImagefile = new File(mediaUploadInterface.getFullPath(thumbnailUuidFileName, uploadFilePath));
        projectImages.get(0).transferTo(reUploadImagefile);

        // 프로젝트를 생성하면서 작성한 한글 설명을 개행 기준으로 잘라 리스트화하여 저장
        String[] descriptK = projectRequestDto.getDescription().split("\n");
        StringBuilder descKor = new StringBuilder();

        // 리스트로 저장된 한글 설명들을 <> 기호로 구분될 수 있도록 설정
        for (int i = 0; i < descriptK.length; i++) {
            if (i != descriptK.length - 1) {
                // 한글 설명이 마지막 줄이 아니면 <> 기호 추가
                descKor.append(descriptK[i]).append("<>");
            } else {
                // 한글 설명이 마지막 줄이면 설명만 추가
                descKor.append(descriptK[i]);
            }
        }

        // 프로젝트를 생성하면서 작성한 영어 설명을 개행 기준으로 잘라 리스트화하여 저장
        String[] descriptE = projectRequestDto.getDescriptionEng().split("\n");
        StringBuilder descEng = new StringBuilder();

        // 리스트로 저장된 영어 설명들을 <> 기호로 구분될 수 있도록 설정
        for (int i = 0; i < descriptE.length; i++) {
            if (i != descriptE.length - 1) {
                // 영어 설명이 마지막 줄이 아니면 <> 기호 추가
                descEng.append(descriptE[i]).append("<>");
            } else {
                // 영어 설명이 마지막 줄이면 설명만 추가
                descEng.append(descriptE[i]);
            }
        }

        // 프로젝트 생성 정보 저장
        Project project = Project.builder()
                .titleKor(projectRequestDto.getTitleKor()) // 생성할 프로젝트 타이틀
                .titleEng(projectRequestDto.getTitleEng()) // 생성할 프로젝트 타이틀 (영어)
                .locationKor(projectRequestDto.getLocationKor()) // 생성할 프로젝트 수행 위치
                .locationEng(projectRequestDto.getLocationEng()) // 생성할 프로젝트 수행 위치 (영어)
                .date(projectRequestDto.getDate()) // 생성할 프로젝트 수행 일자
                .description(descKor.toString()) // 생성할 프로젝트 상세 정보
                .descriptionEng(descEng.toString()) // 프로젝트 영문 정보
                .projectImg(callImagePath) // 썸네일 이미지 파일 업로드 절대 경로
                .localImgUrl(localUploadUrl) // 썸네일 이미지 파일 업로드 프로젝트 내부 경로
                .build();

        // 프로젝트 생성
        projectRepository.save(project);

        log.info("[Success] 프로젝트 생성 완료");

        // 프로젝트를 생성하기 위한 이미지들 등록 및 썸네일 이미지 경로들 반환
        if (mediaUploadInterface.projectUploadFile(projectImages, project, thumbnailUuidFileName, "project", "create")) {
            log.info("[Success] 프로젝트의 미디어 파일들이 정상적으로 등록 되었습니다.");
        } else {
            log.info("[Error] 프로젝트의 미디어 파일들의 업로드가 실패하였습니다.");
        }

    }


    // 메인 페이지에 노출시킬 최신 프로젝트 3개 호출 (슬라이드)
    public List<ProjectListResponseDto> callPojects(String language) {

        // 최신 순으로 3개의 프로젝트들 조회
        List<Project> projects = projectRepository.findTop3ByOrderByCreatedAt();
        // 반환할 리스트 객체 생성
        List<ProjectListResponseDto> projectList = new ArrayList<>();

        // 생성된 프로젝트가 존재할 경우
        if (!projects.isEmpty()) {
            // 호출한 3개의 프로젝트들을 조회하며 반환 객체에 정보 저장
            for (Project eachProject : projects) {

                // 언어 정보가 한글이면 한글 정보 저장
                if (language.equals("Korean")) {
                    projectList.add(
                            ProjectListResponseDto.builder()
                                    .projectId(eachProject.getProjectId()) // 프로젝트 고유 id
                                    .location(eachProject.getLocationKor()) // 프로젝트 한글 위치 주소
                                    .title(eachProject.getTitleKor() + " 프로젝트") // 프로젝트 한글 타이틀 명
                                    .date(eachProject.getDate()) // 프로젝트 수행 일자
                                    .uploadUrl(eachProject.getProjectImg()) // 프로젝트 썸네일 이미지 경로
                                    .localUploadUrl(eachProject.getLocalImgUrl()) // 프로젝트 썸네일 이미지 내부 업로드 경로
                                    .build()
                    );

                    // 언어 정보가 영어면 영어 정보 저장
                } else if (language.equals("English")) {
                    projectList.add(
                            ProjectListResponseDto.builder()
                                    .projectId(eachProject.getProjectId()) // 프로젝트 고유 id
                                    .location(eachProject.getLocationEng()) // 프로젝트 영어 위치 주소
                                    .title(eachProject.getTitleEng() + " Project") // 프로젝트 영어 타이틀 명
                                    .date(eachProject.getDate()) // 프로젝트 수행 일자
                                    .uploadUrl(eachProject.getProjectImg()) // 프로젝트 썸네일 이미지 경로
                                    .localUploadUrl(eachProject.getLocalImgUrl()) // 프로젝트 썸네일 이미지 내부 업로드 경로
                                    .build()
                    );
                }
            }
        }

        return projectList;
    }


    // 프로젝트 수정
    @Transactional
    public boolean projectUpdate(List<MultipartFile> updateImages, Long projectId, ProjectUpdateRequestDto projectRequestDto) throws IOException {

        // 수정하고자 하는 프로젝트 조회
        Project project = projectRepository.findByProjectId(projectId);

        // 이미지 파일들 업로드 절대 경로
        //String uploadFilePath = File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "project" + File.separator;

        // 기존 프로젝트에서 썸네일 이미지를 수정할 경우 확인을 위한 확인용 난수화 파일 명
        String thumbnailUuidFileName = "";

        // 기존 등록된 이미지들 중에 삭제시킬 이미지가 있는지 확인 후 그에 따라 후속 처리
        if (!projectRequestDto.getDeleteImgList().isEmpty() || !projectRequestDto.getDeleteImgList().equals("")) {

            String[] deleteImgList = {};

            if(projectRequestDto.getDeleteImgList().contains("!!")){
                deleteImgList = projectRequestDto.getDeleteImgList().split("!!");
                log.info("제외시킬 기존 이미지들 : {}", deleteImgList.toString());

            }else{
                deleteImgList = new String[]{projectRequestDto.getDeleteImgList()};
                log.info("제외시킬 기존 단일 이미지 : {}", deleteImgList.toString());
            }

            // 썸네일 이미지 삭제 여부 판단 변수
            boolean deletePrevThumbImgCheck = false;

            List<Media> mediaList = mediaRepository.findAllByContentIdAndClassification(project.getProjectId(), "project");

            // 전달받은 기존 이미지들의 id를 가지고 썸네일 이미지(0L)이 아니면 삭제 / 썸네일 이미지면 true 값을 부여
            for (String s : deleteImgList) { // 제외시킬 이미지들의 리스트를 가지고 하나씩 조회한다
                if (Long.parseLong(s) == 0L) { // 그 중 썸네일 이미지를 지울 경우
                    deletePrevThumbImgCheck = true; // deletePrevThumbImgCheck 변수를 true로 지정하고,
                    // 수정하고자 하는 프로젝트의 첫 번쨰로 등록된 썸네일 이미지 media를 조회
                    Media firstImage = mediaList.get(0);

                    File thumbImg = new File(firstImage.getLocalUploadUrl());
                    if(thumbImg.delete()){
                        log.info("썸네일 이미지 변경 시 기존 썸네일 이미지를 삭제");
                    }

                    mediaRepository.deleteByMediaId(firstImage.getMediaId()); // 해당 썸네일 이미지 media를 삭제
                    log.info("새로 등록될 썸네일 이미지 id : {}", Long.parseLong(s));

                }

                if(mediaRepository.findByMediaId(Long.parseLong(s)) != null){
                    log.info("새로 등록될 활용 이미지 id : {}", Long.parseLong(s));

                    Media eachMedia = mediaRepository.findByMediaId(Long.parseLong(s));
                    File image = new File(eachMedia.getLocalUploadUrl());

                    if(image.delete()){
                        log.info("썸네일 이미지 변경 시 기존 썸네일 이미지를 삭제");
                    }

                    mediaRepository.deleteByMediaId(eachMedia.getMediaId()); // 활용 이미지 삭제
                }
            }

            List<Media> remainMediaList = mediaRepository.findAllByContentIdAndClassification(project.getProjectId(), "project");

            // 썸네일 이미지가 삭제될 경우 deletePrevThumbImgCheck가 true로 지정되었으면 진입
            if (deletePrevThumbImgCheck) {

                if(!remainMediaList.isEmpty()) {

                    // 남아있는 활용 이미지들 중 첫번쨰로 들어온 이미지를 가져온다.
                    Media newThumbImg = remainMediaList.get(0);
                    thumbnailUuidFileName = newThumbImg.getMediaUuidTitle();

                    // 썸네일 이미지 로컬 및 서버 업로드 경로 업데이트
                    project.setLocalImgUrl(newThumbImg.getLocalUploadUrl());
                    project.setProjectImg(newThumbImg.getMediaUploadUrl());

                }

            }

        }

        String[] descriptK = projectRequestDto.getUpdateDescription().split("\n");
        StringBuilder descKor = new StringBuilder();

        for (int i = 0; i < descriptK.length; i++) {
            if (i != descriptK.length - 1) {
                descKor.append(descriptK[i]).append("<>");
            } else {
                descKor.append(descriptK[i]);
            }
        }

        String[] descriptE = projectRequestDto.getUpdateDescriptionEng().split("\n");
        StringBuilder descEng = new StringBuilder();

        for (int i = 0; i < descriptE.length; i++) {
            if (i != descriptE.length - 1) {
                descEng.append(descriptE[i]).append("<>");
            } else {
                descEng.append(descriptE[i]);
            }
        }

        project.setDescription(descKor.toString());
        project.setDescriptionEng(descEng.toString());


        // 프로젝트에 속한 이미지들 등록 및 썸네일 이미지 경로들 반환
        if (mediaUploadInterface.projectUploadFile(updateImages, project, thumbnailUuidFileName, "project", "update")) {
            log.info("[Success] 프로젝트의 미디어 파일들이 정상적으로 업데이트 되었습니다.");
        } else {
            log.info("[Error] 프로젝트의 미디어 파일들의 업데이트가 실패하였습니다.");
        }

        if (projectRequestDto.getUpdateTitleKor() != null || !projectRequestDto.getUpdateTitleKor().isEmpty()) {
            project.setTitleKor(projectRequestDto.getUpdateTitleKor());
        }

        if (projectRequestDto.getUpdateTitleEng() != null || !projectRequestDto.getUpdateTitleEng().isEmpty()) {
            project.setTitleEng(projectRequestDto.getUpdateTitleEng());
        }

        if (projectRequestDto.getUpdateLocationKor() != null || !projectRequestDto.getUpdateLocationKor().isEmpty()) {
            project.setLocationKor(projectRequestDto.getUpdateLocationKor());
        }

        if (projectRequestDto.getUpdateLocationEng() != null || !projectRequestDto.getUpdateLocationEng().isEmpty()) {
            project.setLocationEng(projectRequestDto.getUpdateLocationEng());
        }

        if (projectRequestDto.getUpdateDate() != null || !projectRequestDto.getUpdateDate().isEmpty()) {
            project.setDate(projectRequestDto.getUpdateDate());
        }

        entityManager.flush();
        entityManager.clear();

        if (!project.getCreatedAt().equals(project.getModifiedAt())) {
            return true;
        } else {
            return false;
        }

    }


    // 프로젝트 삭제
    @Transactional
    public boolean projectDelete(Long projectId) {
        // 삭제 하고자 하는 프로젝트 조회
        Project project = projectRepository.findByProjectId(projectId);

        File thumbImg = new File(project.getLocalImgUrl());
        if(thumbImg.delete()){
            log.info("썸네일 이미지 변경 시 기존 썸네일 이미지를 삭제");
        }

        List<Media> forImages = mediaRepository.findAllByContentIdAndClassification(project.getProjectId(), "project");

        for(Media eachMedia : forImages){
            File image = new File(eachMedia.getLocalUploadUrl());

            if(image.exists()){
                if(image.delete()){
                    log.info("프로젝트 관련 이미지 삭제");
                }
            }
        }

        // 프로젝트에 관계된 이미지 파일들과 프로젝트 삭제
        mediaRepository.deleteAllByClassificationAndContentId("project", project.getProjectId());
        projectRepository.deleteById(project.getProjectId());

        if (projectRepository.findByProjectId(project.getProjectId()) == null) {
            return true;
        } else {
            return false;
        }
    }


    // 프로젝트 이미지 수정 시 보여질 기존 등록된 이미지들
    public List<MediaResponseDto> callProjectImages(Long projectId) {

        // 최종적으로 반환할 활용 이미지들이 담길 리스트
        List<MediaResponseDto> returnUseImages = new ArrayList<>();
        // 관련 프로젝트 조회
        Project project = projectRepository.findByProjectId(projectId);
        // 프로젝트에 연관된 등록된 이미지들 조회
        List<Media> useImages = mediaRepository.findAllByContentIdAndClassification(projectId, "project");

        // 기등록된 썸네일 이미지부터 리스트에 담기
        returnUseImages.add(
                MediaResponseDto.builder()
                        .mediaId(0L) // 이미지 id
                        .mediaUploadUrl(project.getProjectImg()) // 이미지 경로
                        .contentId(project.getProjectId()) // 프로젝트 id
                        .build()
        );

        // 활용 이미지들 리스트에 담기
        for (Media eachMedia : useImages) {
            if (!project.getLocalImgUrl().contains(eachMedia.getMediaUuidTitle())) {
                returnUseImages.add(
                        MediaResponseDto.builder()
                                .mediaId(eachMedia.getMediaId()) // 이미지 id
                                .mediaUploadUrl(eachMedia.getMediaUploadUrl()) // 이미지 경로
                                .contentId(eachMedia.getContentId()) // 프로젝트 id
                                .build()
                );
            }

        }

        return returnUseImages;
    }

}

