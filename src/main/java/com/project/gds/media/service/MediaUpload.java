package com.project.gds.media.service;


import com.project.gds.category.domain.Category;
import com.project.gds.category.repository.CategoryRepository;
import com.project.gds.contact.domain.Contact;
import com.project.gds.media.domain.Media;
import com.project.gds.media.repository.MediaRepository;
import com.project.gds.media.response.MediaResponseDto;
import com.project.gds.product.domain.Product;
import com.project.gds.product.repository.ProductRepository;
import com.project.gds.product.request.ProductRequestDto;
import com.project.gds.product.response.ProductResponseDto;
import com.project.gds.project.domain.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class MediaUpload implements MediaUploadInterface {

    private final MediaRepository mediaRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    @Value("${deploy.upload.path}")
    private String deployServerPath;

    // 업로드 될 경로
    @Override
    public String getFullPath(String filename, String fileDir) {
        // 파일명과 업로드될 경로 반환
        return fileDir + filename;
    }

    // 업로드 시킬 프로젝트 이미지 파일
    @Transactional
    @Override
    public boolean projectUploadFile(List<MultipartFile> projectImages, Project project, String thumbnailUuidFileName, String classification, String purpose) throws IOException {

        // 파일 및 썸네일 이미지 파일 저장 경로 (이후 배포 시 배포 서버를 연동하면서 경로를 따로 지정해주어야함.)
        /** 회사 자체 서버 업로드 경로 **/
        //String uploadFilePath = File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "project" + File.separator;
        /** GDS 측 가비아 서버 업로드 경로 **/
        String uploadFilePath = File.separator + "web" + File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "project" + File.separator;
        String callImagePath = deployServerPath + "project/";

        // 정상적으로 이미지 파일들이 저장 되었는지 확인하지 위한 리스트
        List<MediaResponseDto> mediaCheckList = new ArrayList<>();

        List<Media> mediaList = mediaRepository.findAllByContentIdAndClassification(project.getProjectId(), "project");

        // 프로젝트나 프로덕트를 생성할 때 들어온 업로드 이미지들을 조회
        if (!projectImages.isEmpty() && !Objects.equals(projectImages.get(0).getOriginalFilename(), "")) {
            for (MultipartFile eachImage : projectImages) {
                if (mediaCheckList.isEmpty()) {
                    log.info("처음 썸네일 이미지 파일 업로드 될 때 진입");

                    if(thumbnailUuidFileName.isEmpty()) {
                        // 업로드할 이미지 파일의 진짜 이름 추출
                        String originalFilename = eachImage.getOriginalFilename();
                        // 난수화된 이미지 파일 이름과 확장자를 합친 파일명 추출
                        String serverUploadFileName = createServerFileName(originalFilename);

                        // 업로드한 이미지 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
                        File file = new File(getFullPath(serverUploadFileName, uploadFilePath));

                        // multipartfile에서 지원하는 transferTo 함수 사용. (해당 파일을 지정한 경로로 전송 - 현재 프로젝트 내부에 전송하고 있으므로 배포하게 되었을 때 수정해줘야 할 필요가 있음.)
                        eachImage.transferTo(file);

                        // 등록시키려는 이미지들의 정보를 엔티티에 담아 저장
                        Media imageMedia = Media.builder()
                                .mediaUuidTitle(serverUploadFileName) // 이미지 파일의 난수화된 이름
                                .mediaName(eachImage.getOriginalFilename()) // 이미지 파일의 원래 이름
                                .classification(classification) // 이미지 용도 구분 (project, product, contact)
                                .mediaUploadUrl(getFullPath(serverUploadFileName, callImagePath)) // 이미지 파일의 업로드 절대 경로
                                .localUploadUrl(getFullPath(serverUploadFileName, uploadFilePath)) // 로컬 환경에서 업로드될 static 경로
                                .contentId(project.getProjectId()) // 속한 프로젝트의 id
                                .build();

                        // 미디어 정보 저장
                        mediaRepository.save(imageMedia);

                        // 확인용 리스트 객체에 정보들 저장
                        mediaCheckList.add(
                                MediaResponseDto.builder()
                                        .mediaId(imageMedia.getMediaId())
                                        .mediaUuidTitle(imageMedia.getMediaUuidTitle())
                                        .mediaName(imageMedia.getMediaName())
                                        .classification(imageMedia.getClassification())
                                        .mediaUploadUrl(imageMedia.getMediaUploadUrl())
                                        .localUploadUrl(imageMedia.getLocalUploadUrl())
                                        .contentId(imageMedia.getContentId())
                                        .build()
                        );

                        // 썸네일 이미지 로컬 및 서버 업로드 경로 업데이트
                        if(mediaList.isEmpty()){
                            File thumbImg = new File(project.getLocalImgUrl());
                            if(thumbImg.delete()){
                                log.info("썸네일 이미지 변경 시 기존 썸네일 이미지를 삭제");
                            }

                            project.setLocalImgUrl(getFullPath(serverUploadFileName, uploadFilePath));
                            project.setProjectImg(getFullPath(serverUploadFileName, callImagePath));
                        }
                    }else{
                        if(purpose.equals("update")){
                            // 업로드할 이미지 파일의 진짜 이름 추출
                            String originalFilename = eachImage.getOriginalFilename();

                            if(projectImages.indexOf(eachImage) == 0){
                                // 난수화된 이미지 파일 이름과 확장자를 합친 파일명 추출
                                String serverUploadFileName = createServerFileName(originalFilename);
                                String reUploadImageFileName = createServerFileName(serverUploadFileName); // 새로 저장 시킬 용도의 난수화된 썸네일 이미지 파일명

                                // 업로드한 이미지 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
                                File file = new File(getFullPath(reUploadImageFileName, uploadFilePath));

                                eachImage.transferTo(file);

                                Media imageMedia = Media.builder()
                                        .mediaUuidTitle(reUploadImageFileName) // 이미지 파일의 난수화된 이름
                                        .mediaName(eachImage.getOriginalFilename()) // 이미지 파일의 원래 이름
                                        .classification(classification) // 이미지 용도 구분 (project, product, contact)
                                        .mediaUploadUrl(getFullPath(reUploadImageFileName, callImagePath)) // 이미지 파일의 업로드 절대 경로
                                        .localUploadUrl(getFullPath(reUploadImageFileName, uploadFilePath)) // 로컬 환경에서 업로드될 static 경로
                                        .contentId(project.getProjectId()) // 속한 프로젝트의 id
                                        .build();

                                // 미디어 정보 저장
                                mediaRepository.save(imageMedia);

                                // 확인용 리스트 객체에 정보들 저장
                                mediaCheckList.add(
                                        MediaResponseDto.builder()
                                                .mediaId(imageMedia.getMediaId())
                                                .mediaUuidTitle(imageMedia.getMediaUuidTitle())
                                                .mediaName(imageMedia.getMediaName())
                                                .classification(imageMedia.getClassification())
                                                .mediaUploadUrl(imageMedia.getMediaUploadUrl())
                                                .localUploadUrl(imageMedia.getLocalUploadUrl())
                                                .contentId(imageMedia.getContentId())
                                                .build()
                                );

                                // 썸네일 이미지 로컬 및 서버 업로드 경로 업데이트
                                if(mediaList.isEmpty()){
                                    File thumbImg = new File(project.getLocalImgUrl());
                                    if(thumbImg.delete()){
                                        log.info("썸네일 이미지 변경 시 기존 썸네일 이미지를 삭제");
                                    }

                                    project.setLocalImgUrl(getFullPath(reUploadImageFileName, uploadFilePath));
                                    project.setProjectImg(getFullPath(reUploadImageFileName, callImagePath));
                                }

                            }else{
                                // 난수화된 이미지 파일 이름과 확장자를 합친 파일명 추출
                                String serverUploadFileName = createServerFileName(originalFilename);

                                // 업로드한 이미지 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
                                File file = new File(getFullPath(serverUploadFileName, uploadFilePath));

                                // multipartfile에서 지원하는 transferTo 함수 사용. (해당 파일을 지정한 경로로 전송 - 현재 프로젝트 내부에 전송하고 있으므로 배포하게 되었을 때 수정해줘야 할 필요가 있음.)
                                eachImage.transferTo(file);

                                Media imageMedia = Media.builder()
                                        .mediaUuidTitle(serverUploadFileName) // 이미지 파일의 난수화된 이름
                                        .mediaName(eachImage.getOriginalFilename()) // 이미지 파일의 원래 이름
                                        .classification(classification) // 이미지 용도 구분 (project, product, contact)
                                        .mediaUploadUrl(getFullPath(serverUploadFileName, callImagePath)) // 이미지 파일의 업로드 절대 경로
                                        .localUploadUrl(getFullPath(serverUploadFileName, uploadFilePath)) // 로컬 환경에서 업로드될 static 경로
                                        .contentId(project.getProjectId()) // 속한 프로젝트의 id
                                        .build();

                                // 미디어 정보 저장
                                mediaRepository.save(imageMedia);

                                // 확인용 리스트 객체에 정보들 저장
                                mediaCheckList.add(
                                        MediaResponseDto.builder()
                                                .mediaId(imageMedia.getMediaId())
                                                .mediaUuidTitle(imageMedia.getMediaUuidTitle())
                                                .mediaName(imageMedia.getMediaName())
                                                .classification(imageMedia.getClassification())
                                                .mediaUploadUrl(imageMedia.getMediaUploadUrl())
                                                .localUploadUrl(imageMedia.getLocalUploadUrl())
                                                .contentId(imageMedia.getContentId())
                                                .build()
                                );
                            }

                        }else{
                            Media imageMedia = Media.builder()
                                    .mediaUuidTitle(thumbnailUuidFileName) // 이미지 파일의 난수화된 이름
                                    .mediaName(eachImage.getOriginalFilename()) // 이미지 파일의 원래 이름
                                    .classification(classification) // 이미지 용도 구분 (project, product, contact)
                                    .mediaUploadUrl(getFullPath(thumbnailUuidFileName, callImagePath)) // 이미지 파일의 업로드 절대 경로
                                    .localUploadUrl(getFullPath(thumbnailUuidFileName, uploadFilePath)) // 로컬 환경에서 업로드될 static 경로
                                    .contentId(project.getProjectId()) // 속한 프로젝트의 id
                                    .build();

                            // 미디어 정보 저장
                            mediaRepository.save(imageMedia);

                            // 확인용 리스트 객체에 정보들 저장
                            mediaCheckList.add(
                                    MediaResponseDto.builder()
                                            .mediaId(imageMedia.getMediaId())
                                            .mediaUuidTitle(imageMedia.getMediaUuidTitle())
                                            .mediaName(imageMedia.getMediaName())
                                            .classification(imageMedia.getClassification())
                                            .mediaUploadUrl(imageMedia.getMediaUploadUrl())
                                            .localUploadUrl(imageMedia.getLocalUploadUrl())
                                            .contentId(imageMedia.getContentId())
                                            .build()
                            );
                        }


                    }

                } else {
                    log.info("이후 파일들 업로드 될 때 진입");

                    // 업로드할 이미지 파일의 진짜 이름 추출
                    String originalFilename = eachImage.getOriginalFilename();
                    // 난수화된 이미지 파일 이름과 확장자를 합친 파일명 추출
                    String serverUploadFileName = createServerFileName(originalFilename);

                    // 업로드한 이미지 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
                    File file = new File(getFullPath(serverUploadFileName, uploadFilePath));

                    // multipartfile에서 지원하는 transferTo 함수 사용. (해당 파일을 지정한 경로로 전송 - 현재 프로젝트 내부에 전송하고 있으므로 배포하게 되었을 때 수정해줘야 할 필요가 있음.)
                    eachImage.transferTo(file);

                    // 등록시키려는 이미지들의 정보를 엔티티에 담아 저장
                    Media imageMedia = Media.builder()
                            .mediaUuidTitle(serverUploadFileName) // 이미지 파일의 난수화된 이름
                            .mediaName(originalFilename) // 이미지 파일의 원래 이름
                            .classification(classification) // 이미지 용도 구분 (project, product, contact)
                            .mediaUploadUrl(getFullPath(serverUploadFileName, callImagePath)) // 이미지 파일의 업로드 절대 경로
                            .localUploadUrl(getFullPath(serverUploadFileName, uploadFilePath)) // 로컬 환경에서 업로드될 static 경로
                            .contentId(project.getProjectId()) // 속한 프로젝트의 id
                            .build();

                    // 미디어 정보 저장
                    mediaRepository.save(imageMedia);

                    // 확인용 리스트 객체에 정보들 저장
                    mediaCheckList.add(
                            MediaResponseDto.builder()
                                    .mediaId(imageMedia.getMediaId())
                                    .mediaUuidTitle(imageMedia.getMediaUuidTitle())
                                    .mediaName(imageMedia.getMediaName())
                                    .classification(imageMedia.getClassification())
                                    .mediaUploadUrl(imageMedia.getMediaUploadUrl())
                                    .localUploadUrl(imageMedia.getLocalUploadUrl())
                                    .contentId(imageMedia.getContentId())
                                    .build()
                    );
                }
            }
        }

        /**
        entityManager.flush();
        entityManager.clear();
         **/

        return mediaCheckList.size() == projectImages.size() &&
                !mediaCheckList.isEmpty() &&
                mediaCheckList.get(0).getMediaName().equals(projectImages.get(0).getOriginalFilename()) &&
                mediaCheckList.get(mediaCheckList.size() - 1).getMediaName().equals(projectImages.get(projectImages.size() - 1).getOriginalFilename());

    }

    // 업로드 시킬 문의 사항 관련 파일
    @Override
    public boolean contactUploadFile(List<MultipartFile> contactFile, Contact contact, String classification) throws IOException {

        // 파일 저장 경로 (이후 배포 시 배포 서버를 연동하면서 경로를 따로 지정해주어야함.)
        /** 회사 자체 업로드 서버 경로 **/
        //String uploadFilePath = File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "contact" + File.separator;
        /** GDS 측 가비아 서버 업로드 서버 경로 **/
        String uploadFilePath = File.separator + "web" + File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "contact" + File.separator;
        String callImagePath = deployServerPath + "contact/";

        // 정상적으로 이미지 파일들이 저장 되었는지 확인하지 위한 리스트
        List<MediaResponseDto> mediaCheckList = new ArrayList<>();

        // 문의 사항을 생성할 때 들어온 업로드 이미지들을 조회
        for (MultipartFile eachFile : contactFile) {
            // 업로드할 파일의 진짜 이름 추출
            String originalFilename = eachFile.getOriginalFilename();
            // 난수화된 파일 이름과 확장자를 합친 파일명 추출
            String serverUploadFileName = createServerFileName(originalFilename);

            // 업로드한 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
            File file = new File(getFullPath(serverUploadFileName, uploadFilePath));

            // multipartfile에서 지원하는 transferTo 함수 사용. (해당 파일을 지정한 경로로 전송 - 현재 프로젝트 내부에 전송하고 있으므로 배포하게 되었을 때 수정해줘야 할 필요가 있음.)
            eachFile.transferTo(file);

            // 등록시키려는 파일들의 정보를 엔티티에 담아 저장
            Media fileMedia = Media.builder()
                    .mediaUuidTitle(serverUploadFileName) // 파일의 난수화된 이름
                    .mediaName(originalFilename) // 파일의 원래 이름
                    .classification(classification) // 용도 구분 (project, product, contact)
                    .mediaUploadUrl(getFullPath(serverUploadFileName, callImagePath)) // 파일의 콜 path
                    .localUploadUrl(getFullPath(serverUploadFileName, uploadFilePath)) // 배포 서버에 업로드된 경로
                    .contentId(contact.getContactId()) // 속한 프로젝트의 id
                    .build();

            // 미디어 정보 저장
            mediaRepository.save(fileMedia);

            // 확인용 리스트 객체에 정보들 저장
            mediaCheckList.add(
                    MediaResponseDto.builder()
                            .mediaId(fileMedia.getMediaId())
                            .mediaUuidTitle(fileMedia.getMediaUuidTitle())
                            .mediaName(fileMedia.getMediaName())
                            .classification(fileMedia.getClassification())
                            .mediaUploadUrl(fileMedia.getMediaUploadUrl())
                            .localUploadUrl(fileMedia.getLocalUploadUrl())
                            .contentId(fileMedia.getContentId())
                            .build()
            );
        }

        return mediaCheckList.size() == contactFile.size() &&
                !mediaCheckList.isEmpty() &&
                mediaCheckList.get(0).getMediaName().equals(contactFile.get(0).getOriginalFilename()) &&
                mediaCheckList.get(mediaCheckList.size() - 1).getMediaName().equals(contactFile.get(contactFile.size() - 1).getOriginalFilename());
    }

    // 업로드 시킬 프로덕트의 이미지 및 파일
    @Override
    public ProductResponseDto productUploadFile(HashMap<String, MultipartFile> fileSet, List<MultipartFile> imgFiles, String classification, ProductRequestDto productRequestDto) throws IOException {
        log.info("프로덕트 이미지 및 파일 업로드 인터페이스 함수 진입");

        // 파일 및 썸네일 이미지 파일 저장 절대 경로 (이후 배포 시 배포 서버를 연동하면서 경로를 따로 지정해주어야함.)
        /** 회사 자체 업로드 서버 경로 **/
        //String uploadFilePath = File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "product" + File.separator;
        /** GDS 측 가비어 업로드 서버 경로 **/
        String uploadFilePath = File.separator + "web" + File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "product" + File.separator;
        String callImgPath = deployServerPath + "product/img/";
        String callFilePath = deployServerPath + "product/file/";

        // 프로덕트 생성 시 요청되는 파일에 따른 업로드 절대 경로 변경
        String fileUploadPath = uploadFilePath + "file" + File.separator;
        String imgUploadPath = uploadFilePath + "img" + File.separator;

        // 업로드할 파일의 진짜 이름 추출
        String originalKorFilename = fileSet.get("korFile").getOriginalFilename();
        String originalEngFilename = fileSet.get("engFile").getOriginalFilename();

        // 난수화된 파일 이름과 확장자를 합친 파일명 추출
        String korUploadFileName = createServerFileName(originalKorFilename);
        String engUploadFileName = createServerFileName(originalEngFilename);

        // 업로드한 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
        File korfile = new File(getFullPath(korUploadFileName, fileUploadPath));
        File engfile = new File(getFullPath(engUploadFileName, fileUploadPath));

        // multipartfile에서 지원하는 transferTo 함수 사용. (해당 파일을 지정한 경로로 전송 - 현재 프로젝트 내부에 전송하고 있으므로 배포하게 되었을 때 수정해줘야 할 필요가 있음.)
        fileSet.get("korFile").transferTo(korfile);
        fileSet.get("engFile").transferTo(engfile);

        // 프로젝트 및 프로덕트 생성 완료 후 썸네일 이미지들을 불러오기 위한 업로드 경로를 저장할 HashMap 객체 생성
        HashMap<String, String> imageUploadUrl = new HashMap<>();

        // 업로드할 파일의 진짜 이름 추출
        String thumbnailImgName = imgFiles.get(0).getOriginalFilename();

        // 난수화된 파일 이름과 확장자를 합친 파일명 추출
        String thumbnailImgFileName = createServerFileName(thumbnailImgName);

        // 썸네일 이미지 등록 및 서버에 업로드
        File thumbImg = new File(getFullPath(thumbnailImgFileName, imgUploadPath));
        imgFiles.get(0).transferTo(thumbImg);

        // 썸네일 이미지 업로드 경로 HashMap 에 저장
        imageUploadUrl.put("uploadUrl", getFullPath(thumbnailImgFileName, callImgPath));
        imageUploadUrl.put("localUploadUrl", getFullPath(thumbnailImgFileName, uploadFilePath));

        // 프로덕트 카테고리 호출
        Category category = categoryRepository.findByCategoryName(productRequestDto.getCategoryName());

        // 스펙 세분화
        String[] specK = productRequestDto.getProductSpecKor().split("\n");
        StringBuilder specKor = new StringBuilder();

        for (int i = 0; i < specK.length; i++) {
            if (i != specK.length - 1) {
                specKor.append(specK[i]).append("!");
            } else {
                specKor.append(specK[i]);
            }
        }

        log.info("한글 스펙 잘라서 구분 키워드 중간 삽입 문장 - {}", specKor.toString());

        String[] specE = productRequestDto.getProductSpecEng().split("\n");
        StringBuilder specEng = new StringBuilder();

        for (int i = 0; i < specE.length; i++) {
            if (i != specE.length - 1) {
                specEng.append(specE[i]).append("!");
            } else {
                specEng.append(specE[i]);
            }
        }
        log.info("영어 스펙 잘라서 구분 키워드 중간 삽입 문장 - {}", specEng);

        //String[] descriptK = productRequestDto.getProductDescriptKor().split("\\.");
        String[] descriptK = productRequestDto.getProductDescriptKor().split("\n");
        StringBuilder descriptKor = new StringBuilder();

        for (int i = 0; i < descriptK.length; i++) {
            if (i != descriptK.length - 1) {
                descriptKor.append(descriptK[i]).append("!");
            } else {
                descriptKor.append(descriptK[i]);
            }
        }

        //String[] descriptE = productRequestDto.getProductDescriptEng().split("\\.");
        String[] descriptE = productRequestDto.getProductDescriptEng().split("\n");
        StringBuilder descriptEng = new StringBuilder();

        for (int i = 0; i < descriptE.length; i++) {
            if (i != descriptE.length - 1) {
                descriptEng.append(descriptE[i]).append("!");
            } else {
                descriptEng.append(descriptE[i]);
            }
        }

        // 프로덕트 정보 저장
        Product product = Product.builder()
                .productName(productRequestDto.getProductName()) // 프로덕트 명
                .productEngName(productRequestDto.getProductName())
                .productDescriptKor(descriptKor.toString()) // 프로덕트 설명 한글
                .productDescriptEng(descriptEng.toString()) // 프로덕트 설명 영어
                .productSpecKor(specKor.toString()) // 프로덕트 한글 스펙
                .productSpecEng(specEng.toString()) // 프로덕트 영어 스펙
                .menuExpression("on") // 프로덕트 메뉴 노출 여부
                .fileKorName(originalKorFilename)
                .fileKorUploadUrl(getFullPath(korUploadFileName, callFilePath)) // 프로덕트 한글 파일 업로드 절대 경로
                .fileKorLocalUploadUrl(getFullPath(korUploadFileName, fileUploadPath)) // 프로덕트 한글 파일 프로젝트 내부 업로드 경로
                .fileEngName(originalEngFilename)
                .fileEngUploadUrl(getFullPath(engUploadFileName, callFilePath)) // 프로덕트 영어 파일 업로드 절대 경로
                .fileEngLocalUploadUrl(getFullPath(engUploadFileName, fileUploadPath)) // 프로덕트 영어 파일 프로젝트 내부 업로드 경로
                .productImgUrl(imageUploadUrl.get("uploadUrl")) // 프로덕트 썸네일 이미지 업로드 절대 경로
                .localProductImgUrl(imageUploadUrl.get("localUploadUrl")) // 프로덕트 썸네일 이미지 프로젝트 업로드 내부 경로
                .categoryId(category.getCategoryId()) // 프로덕트 카테고리
                .build();

        // 프로덕트 생성
        productRepository.save(product);

        log.info("프로덕트 생성 - {}", product.getProductName());

        // 프로덕트 생성과 동시에 같이 속해진 파일 및 이미지들을 확인하기 위한 반환 리스트 객체 생성
        List<MediaResponseDto> mediaList = new ArrayList<>();

        // 프로덕트를 등록하면서 같이 등록될 이미지들 조회
        for (int i = 1; i < imgFiles.size(); i++) {
            // 업로드할 파일의 진짜 이름 추출
            String originalImgFilename = imgFiles.get(i).getOriginalFilename();

            // 난수화된 파일 이름과 확장자를 합친 파일명 추출
            String imgFileName = createServerFileName(originalImgFilename);

            // 업로드한 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
            File imgfile = new File(getFullPath(imgFileName, imgUploadPath));

            // multipartfile에서 지원하는 transferTo 함수 사용. (해당 파일을 지정한 경로로 전송 - 현재 프로젝트 내부에 전송하고 있으므로 배포하게 되었을 때 수정해줘야 할 필요가 있음.)
            imgFiles.get(i).transferTo(imgfile);

            // 등록시키려는 파일들의 정보를 엔티티에 담아 저장
            Media imageMedia = Media.builder()
                    .mediaUuidTitle(imgFileName) // 파일의 난수화된 이름
                    .mediaName(originalImgFilename) // 파일의 원래 이름
                    .classification(classification) // 파일 용도 구분 (project, product, contact)
                    .mediaUploadUrl(getFullPath(imgFileName, callImgPath)) // 파일의 업로드 절대 경로
                    .localUploadUrl(getFullPath(imgFileName, imgUploadPath)) // 로컬 환경에서 업로드될 static 경로
                    .contentId(product.getProductId()) // 속한 컨텐츠의 id
                    .build();

            // 미디어 정보 저장
            mediaRepository.save(imageMedia);

            // 반환 리스트 객체에 이미지 파일 정보들을 담은 ResponseDto를 저장
            mediaList.add(
                    MediaResponseDto.builder()
                            .mediaId(imageMedia.getMediaId())
                            .mediaUuidTitle(imageMedia.getMediaUuidTitle())
                            .mediaName(imageMedia.getMediaName())
                            .classification(imageMedia.getClassification())
                            .mediaUploadUrl(imageMedia.getMediaUploadUrl())
                            .localUploadUrl(imageMedia.getLocalUploadUrl())
                            .contentId(imageMedia.getContentId())
                            .build()
            );

            log.info("프로덕트 이미지 파일 등록 - {}", imgFiles.indexOf(imgFiles.get(i)));
        }

        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescriptKor(product.getProductDescriptKor())
                .productDescriptEng(product.getProductDescriptEng())
                .productSpecKor(product.getProductSpecKor())
                .productSpecEng(product.getProductSpecEng())
                //.menuExpression(product.getMenuExpression())
                .fileKorUploadUrl(product.getFileKorUploadUrl())
                .fileEngUploadUrl(product.getFileEngUploadUrl())
                .fileKorLocalUploadUrl(product.getFileKorLocalUploadUrl())
                .fileEngLocalUploadUrl(product.getFileEngLocalUploadUrl())
                .productImgUrl(product.getProductImgUrl())
                .localProductImgUrl(product.getLocalProductImgUrl())
                .categoryId(product.getCategoryId())
                .mediaList(mediaList)
                .build();
    }

    // 난수화한 업로드할 파일 이름
    @Override
    public String createServerFileName(String originalFilename) {
        // 원래 이름이 아닌 난수화한 uuid 이름 추출
        String uuid = UUID.randomUUID().toString();
        // 파일의 원래 이름 중에 . 기호 기준으로 확장자 추출
        String ext = extractExt(originalFilename);

        // 난수화된 이름과 확장자를 합쳐 난수화된 파일명 반환
        return uuid + "." + ext;
    }

    // 업로드 파일 확장자 정보 추출
    @Override
    public String extractExt(String originalFilename) {
        // 파일명의 . 기호가 몇번째에 존재하는지 인덱스 값 추출
        int pos = originalFilename.lastIndexOf(".");

        // 원래 이름에서 뽑은 인덱스값에 위치한 . 기호 다음 확장자 추출
        return originalFilename.substring(pos + 1);
    }
}
