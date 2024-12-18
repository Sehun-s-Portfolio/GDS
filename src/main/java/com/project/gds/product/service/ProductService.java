package com.project.gds.product.service;

import com.project.gds.category.domain.Category;
import com.project.gds.category.repository.CategoryRepository;
import com.project.gds.exception.product.ProductExceptionInterface;
import com.project.gds.media.domain.Media;
import com.project.gds.media.repository.MediaRepository;
import com.project.gds.media.response.MediaResponseDto;
import com.project.gds.media.service.MediaUploadInterface;
import com.project.gds.product.domain.Product;
import com.project.gds.product.repository.ProductRepository;
import com.project.gds.product.request.ProductRequestDto;
import com.project.gds.product.request.ProductUpdateRequestDto;
import com.project.gds.product.response.ProductListResponseDto;
import com.project.gds.product.response.ProductResponseDto;
import com.project.gds.share.ResponseBody;
import com.project.gds.share.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final MediaUploadInterface mediaUploadInterface;
    private final ProductExceptionInterface productExceptionInterface;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MediaRepository mediaRepository;
    private final EntityManager entityManager;

    @Value("${deploy.upload.path}")
    private String deployServerPath;

    // 프로덕트 추가 - (admin)
    @Transactional
    public void productAdd(
            ProductRequestDto productRequestDto,
            MultipartFile korFile,
            MultipartFile engFile,
            List<MultipartFile> imgFiles) throws IOException {

        // 만약 기존에 존재한 프로덕트 명으로 중복 요청이 올 경우
        if (productExceptionInterface.checkDuplicateProduct(productRequestDto.getProductName())) {
            log.info("{}", new ResponseEntity<>(new ResponseBody(StatusCode.DUPLICATE_PRODUCT, null), HttpStatus.BAD_REQUEST));
        } else {
            log.info("프로덕트 추가 service 진입 - 프로덕트 명 : {}", productRequestDto.getProductName());

            // 프로덕트 한글 / 영어 파일을 HashMap 에 묶어 업로드 인터페이스에 전달
            HashMap<String, MultipartFile> fileSet = new HashMap<>();
            fileSet.put("korFile", korFile);
            fileSet.put("engFile", engFile);

            // 파일과 이미지 파일들 프로덕트 요청 정보들을 업로드 인터페이스 함수에 넘겨 프로덕트 생성 및 파일, 이미지 등록
            mediaUploadInterface.productUploadFile(fileSet, imgFiles, "product", productRequestDto);
        }

    }


    // 선택한 카테고리에 따른 프로덕트 페이지 이동
    public List<ProductListResponseDto> categoryProductPage(String categoryName, String language) {

        log.info("카테고리 명 상태 확인 : {}", categoryName);

        // 선택한 카테고리 정보 추출
        Category category = categoryRepository.findByCategoryName(categoryName);
        // 프로덕트들을 최신순으로 정렬하여 리스트화
        List<Product> productList = new ArrayList<>();
        if (!productRepository.findAllByCategoryId(category.getCategoryId()).isEmpty()) {
            productList = productRepository.findAllByCategoryId(category.getCategoryId());
        }

        // 조회될 프로덕트들의 정보들을 담을 리스트 객체 생성
        List<ProductListResponseDto> products = new ArrayList<>();
        log.info("여기까지 오나?");

        if (productList != null) {
            log.info("null 값이 아닐 경우 진입");

            // 카테고리에 따라 조회된 프로덕트들을 하나씩 조회
            for (Product eachProduct : productList) {

                // 프로덕트에 속한 활용 이미지들을 조회
                List<Media> imageList = mediaRepository.findAllByContentIdAndClassification(eachProduct.getProductId(), "product");
                // 활용 이미지들을 담을 리스트 객체 생성
                List<MediaResponseDto> images = new ArrayList<>();

                // 조회한 활용 이미지들을 하나씩 조회하여 리스트에 담는 작업 수행
                for (Media eachImg : imageList) {
                    images.add(
                            MediaResponseDto.builder()
                                    .mediaId(eachImg.getMediaId()) // 활용 이미지 id
                                    .mediaUuidTitle(eachImg.getMediaUuidTitle()) // 활용 이미지 난수화된 파일 명
                                    .mediaName(eachImg.getClassification()) // 활용 이미지의 용도
                                    .mediaUploadUrl(eachImg.getMediaUploadUrl()) // 활용 이미지가 저장된 서버 업로드 경로
                                    .localUploadUrl(eachImg.getLocalUploadUrl()) // 로컬 환경에서 활용 이미지가 저장된 프로젝트 내부 경로
                                    .contentId(eachProduct.getProductId()) //속한 프로덕트의 id
                                    .build()
                    );
                }

                if (language.equals("Korean")) {
                    String[] descriptK = eachProduct.getProductDescriptKor().split("!");
                    List<String> descriptKor = new ArrayList<>();
                    Collections.addAll(descriptKor, descriptK);

                    String[] specK = eachProduct.getProductSpecKor().split("!");
                    List<String> specKor = new ArrayList<>();
                    Collections.addAll(specKor, specK);

                    File file = new File(eachProduct.getFileKorUploadUrl());

                    String productFileName = eachProduct.getProductName() + ".pdf";

                    if(eachProduct.getFileKorName() != null){
                        productFileName = eachProduct.getFileKorName();
                    }

                    // 리스트 객체에 조회한 프로덕트들의 정보들을 담는 로직 수행
                    products.add(
                            ProductListResponseDto.builder()
                                    .productId(eachProduct.getProductId()) // 프로덕트 id
                                    .thumbnailImg(eachProduct.getProductImgUrl()) // 프로덕트 썸네일 이미지가 저장된 서버 경로
                                    .productName(eachProduct.getProductName()) // 프로덕트 명
                                    .realFileName(file.getName())
                                    .productDescript(descriptKor) // 프로덕트 설명 (페이지 번역 요청 언어에 따라 다른 스펙 정보가 저장되어야 함.)
                                    .productSpec(specKor) // 프로덕트 스펙 (페이지 번역 요청 언어에 따라 다른 스펙 정보가 저장되어야 함.)
                                    .productFile(eachProduct.getFileKorUploadUrl()) // 프로덕트 관련 파일이 업로드된 서버 경로
                                    .productFileName(productFileName)
                                    .imgList(images)// 프로덕트에 속한 활용 이미지들 정보
                                    .categoryName(categoryName) // 프로덕트가 속한 카테고리 명
                                    .categoryId(eachProduct.getProductId())
                                    .build()
                    );

                } else if (language.equals("English")) {
                    String[] descriptE = eachProduct.getProductDescriptEng().split("!");
                    List<String> descriptEng = new ArrayList<>();
                    Collections.addAll(descriptEng, descriptE);

                    String[] specE = eachProduct.getProductSpecEng().split("!");
                    List<String> specEng = new ArrayList<>();
                    Collections.addAll(specEng, specE);

                    File file = new File(eachProduct.getFileEngUploadUrl());

                    String productFileName = eachProduct.getProductName() + ".pdf";

                    if(eachProduct.getFileKorName() != null){
                        productFileName = eachProduct.getFileEngName();
                    }

                    // 리스트 객체에 조회한 프로덕트들의 정보들을 담는 로직 수행
                    products.add(
                            ProductListResponseDto.builder()
                                    .productId(eachProduct.getProductId()) // 프로덕트 id
                                    .thumbnailImg(eachProduct.getProductImgUrl()) // 프로덕트 썸네일 이미지가 저장된 서버 경로
                                    .productName(eachProduct.getProductEngName()) // 프로덕트 명
                                    .realFileName(file.getName())
                                    .productDescript(descriptEng) // 프로덕트 설명 (페이지 번역 요청 언어에 따라 다른 스펙 정보가 저장되어야 함.)
                                    .productSpec(specEng) // 프로덕트 스펙 (페이지 번역 요청 언어에 따라 다른 스펙 정보가 저장되어야 함.)
                                    .productFile(eachProduct.getFileEngUploadUrl()) // 프로덕트 관련 파일이 업로드된 서버 경로
                                    .productFileName(productFileName)
                                    .imgList(images)// 프로덕트에 속한 활용 이미지들 정보
                                    .categoryName(categoryName) // 프로덕트가 속한 카테고리 명
                                    .categoryId(eachProduct.getCategoryId())
                                    .build()
                    );
                }

            }
        } else {
            log.info("null 값일 경우 진입");

            products = null;
        }

        return products;
    }


    // 프로덕트 상세 페이지 진입 service
    public ProductResponseDto productDetailInfo(Long productId) {
        Product product = productRepository.findByProductId(productId);

        List<MediaResponseDto> imageList = new ArrayList<>();
        List<Media> useImages = mediaRepository.findAllByContentIdAndClassification(product.getProductId(), "product");

        for (Media eachMedia : useImages) {
            imageList.add(
                    MediaResponseDto.builder()
                            .mediaId(eachMedia.getMediaId())
                            .mediaUuidTitle(eachMedia.getMediaUuidTitle())
                            .mediaName(eachMedia.getMediaName())
                            .classification(eachMedia.getClassification())
                            .mediaUploadUrl(eachMedia.getMediaUploadUrl())
                            .localUploadUrl(eachMedia.getLocalUploadUrl())
                            .contentId(eachMedia.getContentId())
                            .build()
            );
        }

        String[] descriptK = product.getProductSpecEng().split("!");
        List<String> descriptKor = new ArrayList<>();
        Collections.addAll(descriptKor, descriptK);

        String[] descriptE = product.getProductSpecEng().split("!");
        List<String> descriptEng = new ArrayList<>();
        Collections.addAll(descriptEng, descriptE);

        String[] specK = product.getProductSpecEng().split("!");
        List<String> specKor = new ArrayList<>();
        Collections.addAll(specKor, specK);

        String[] specE = product.getProductSpecEng().split("!");
        List<String> specEng = new ArrayList<>();
        Collections.addAll(specEng, specE);

        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescriptKor(product.getProductDescriptKor())
                .descriptKorGroup(descriptKor)
                .productDescriptEng(product.getProductDescriptEng())
                .descriptEngGroup(descriptEng)
                .productSpecKor(product.getProductSpecKor())
                .specKorGroup(specKor)
                .productSpecEng(product.getProductSpecEng())
                .specEngGroup(specEng)
                //.menuExpression(product.getMenuExpression())
                .fileKorUploadUrl(product.getFileKorUploadUrl())
                .fileKorLocalUploadUrl(product.getFileKorLocalUploadUrl())
                .fileEngUploadUrl(product.getFileEngUploadUrl())
                .fileEngLocalUploadUrl(product.getFileEngLocalUploadUrl())
                .productImgUrl(product.getProductImgUrl())
                .localProductImgUrl(product.getLocalProductImgUrl())
                .categoryId(product.getCategoryId())
                .mediaList(imageList)
                .build();

    }


    // 프로덕트 업데이트
    @Transactional
    public String updateProductDetail(String language, Long productId, MultipartHttpServletRequest updateFileRequest, ProductUpdateRequestDto updateRequestDto) throws IOException {

        MultipartFile reUploadFile = updateFileRequest.getFile("reUploadFiles");
        List<MultipartFile> reUploadImageFiles = updateFileRequest.getFiles("reUploadImageFiles");

        // 파일 저장 절대 경로
        /** 회사 자체 업로드 서버 경로 **/
        //String uploadFilePath = File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "product" + File.separator;
        /** GDS 측 가비아 업로드 서버 경로 **/
        String uploadFilePath = File.separator + "web" + File.separator + "home" + File.separator + "gds_korea" + File.separator + "gds_korea" + File.separator + "webapp" + File.separator + "product" + File.separator;
        String callFilePath = deployServerPath + "product/file/";
        String callImageFilePath = deployServerPath + "product/img/";
        String fileUploadPath = uploadFilePath + "file" + File.separator;
        String imgFileUploadPath = uploadFilePath + "img" + File.separator;

        Product product = productRepository.findByProductId(productId);
        Category category = categoryRepository.findByCategoryId(product.getCategoryId());
        String categoryName = category.getCategoryName();

        log.info("프로덕트 수정 service 쪽에서 카테고리 추출 확인");
        log.info("=========================================");
        log.info("카테고리 명 : {}", category.getCategoryName());
        log.info("카테고리 id : {}", category.getCategoryId());
        log.info("=========================================");

        // 한글
        if (language.equals("Korean")) {

            // 제품 명 수정
            product.setProductName(updateRequestDto.getReUploadProductRealName());

            // 제품 설명 수정
            if (updateRequestDto.getReUploadProductDescript().contains("\n")) {
                String[] descriptK = updateRequestDto.getReUploadProductDescript().split("\n");
                StringBuilder descriptKor = new StringBuilder();

                for (int i = 0; i < descriptK.length; i++) {
                    if (i != descriptK.length - 1) {
                        descriptKor.append(descriptK[i]).append("!");
                    } else {
                        descriptKor.append(descriptK[i]);
                    }
                }

                product.setProductDescriptKor(descriptKor.toString());
            } else {
                product.setProductDescriptKor(updateRequestDto.getReUploadProductDescript());
            }

            // 제품 스펙 수정
            if (updateRequestDto.getReUploadSpec().contains("\n")) {
                String[] specK = updateRequestDto.getReUploadSpec().split("\n");
                StringBuilder specKor = new StringBuilder();

                for (int i = 0; i < specK.length; i++) {
                    if (i != specK.length - 1) {
                        specKor.append(specK[i]).append("!");
                    } else {
                        specKor.append(specK[i]);
                    }
                }

                product.setProductSpecKor(specKor.toString());
            } else {
                product.setProductSpecKor(updateRequestDto.getReUploadSpec());
            }

            // 제품 파일 수정
            assert reUploadFile != null;
            if (!reUploadFile.isEmpty() && !Objects.equals(reUploadFile.getOriginalFilename(), "")) {
                String originalKorFilename = reUploadFile.getOriginalFilename();
                String korUploadFileName = mediaUploadInterface.createServerFileName(originalKorFilename);
                File korfile = new File(mediaUploadInterface.getFullPath(korUploadFileName, fileUploadPath));
                reUploadFile.transferTo(korfile);

                File korFile = new File(product.getFileKorLocalUploadUrl());
                if(korFile.delete()){
                    log.info("재업로드 시 기존 한글 파일 삭제");
                }

                product.setFileKorName(originalKorFilename);
                product.setFileKorUploadUrl(mediaUploadInterface.getFullPath(korUploadFileName, callFilePath));
                product.setFileKorLocalUploadUrl(mediaUploadInterface.getFullPath(korUploadFileName, fileUploadPath));
            }

        } else if (language.equals("English")) { // 영문

            // 제품 명 수정
            product.setProductEngName(updateRequestDto.getReUploadProductRealName());

            // 제품 영문 설명 수정
            if (updateRequestDto.getReUploadProductDescript().contains("\n")) {
                String[] descriptE = updateRequestDto.getReUploadProductDescript().split("\n");
                StringBuilder descriptEng = new StringBuilder();

                for (int i = 0; i < descriptE.length; i++) {
                    if (i != descriptE.length - 1) {
                        descriptEng.append(descriptE[i]).append("!");
                    } else {
                        descriptEng.append(descriptE[i]);
                    }
                }

                product.setProductDescriptEng(descriptEng.toString());
            } else {
                product.setProductDescriptEng(updateRequestDto.getReUploadProductDescript());
            }

            // 제품 영문 스펙 수정
            if (updateRequestDto.getReUploadSpec().contains("\n")) {
                String[] specE = updateRequestDto.getReUploadSpec().split("\n");
                StringBuilder specEng = new StringBuilder();

                for (int i = 0; i < specE.length; i++) {
                    if (i != specE.length - 1) {
                        specEng.append(specE[i]).append("!");
                    } else {
                        specEng.append(specE[i]);
                    }
                }

                product.setProductSpecEng(specEng.toString());
            } else {
                product.setProductSpecEng(updateRequestDto.getReUploadSpec());
            }

            // 제품 영문 파일 수정
            assert reUploadFile != null;
            if (!reUploadFile.isEmpty() && !Objects.equals(reUploadFile.getOriginalFilename(), "")) {
                String originalEngFilename = reUploadFile.getOriginalFilename();
                String engUploadFileName = mediaUploadInterface.createServerFileName(originalEngFilename);
                File engfile = new File(mediaUploadInterface.getFullPath(engUploadFileName, fileUploadPath));
                reUploadFile.transferTo(engfile);

                File engFile = new File(product.getFileEngLocalUploadUrl());
                if(engFile.delete()){
                    log.info("재업로드 시 기존 영어 파일 삭제");
                }

                product.setFileEngName(originalEngFilename);
                product.setFileEngUploadUrl(mediaUploadInterface.getFullPath(engUploadFileName, callFilePath));
                product.setFileEngLocalUploadUrl(mediaUploadInterface.getFullPath(engUploadFileName, fileUploadPath));
            }

        }

        // 썸네일 이미지 삭제 여부 판단 변수
        boolean deletePrevThumbImgCheck = false;

        // 만약 기존에 프로덕트에 썸네일 이미지를 제외한 나머지 활용 이미지들이 존재할 경우 업데이트 시 기존의 활용 이미지들을 삭제한다.
        if (!updateRequestDto.getDeleteImgList().isEmpty() || !updateRequestDto.getDeleteImgList().equals("")) {
            String[] deleteImgList = updateRequestDto.getDeleteImgList().split("!!");

            // 전달받은 기존 이미지들의 id를 가지고 썸네일 이미지(0L)이 아니면 삭제 / 썸네일 이미지면 true 값을 부여
            for (String s : deleteImgList) {
                if (Long.parseLong(s) == 0L) {
                    deletePrevThumbImgCheck = true;
                } else {
                    Media eachMedia = mediaRepository.findByMediaId(Long.parseLong(s));

                    File image = new File(eachMedia.getLocalUploadUrl());
                    if(image.delete()){
                        log.info("프로젝트 이미지 삭제");
                    }

                    mediaRepository.deleteByMediaId(Long.parseLong(s)); // 활용 이미지들 삭제
                    /**
                    entityManager.flush();
                    entityManager.clear();
                     **/
                }
            }

            // 썸네일 이미지가 삭제될 경우
            if (deletePrevThumbImgCheck) {
                // 썸네일 이미지가 삭제될 경우, 만약 해당 프로덕트에 연관된 활용이미지들이 아예 존재하지 않고 썸네일 이미지만 원래 존재했을 경우, 새로 추가하고자 하는 이미지 파일의 첫 번째 파일을 썸네일 이미지로 세팅
                if (mediaRepository.findAllByContentIdAndClassification(productId, "product") == null ||
                        mediaRepository.findAllByContentIdAndClassification(productId, "product").isEmpty()) {
                    // 첫 번째로 들어오는 이미지 정보를 tbl_product 썸네일 정보에 업데이트하기 위해 데이터 추출
                    String originalReuploadImageFilename = reUploadImageFiles.get(0).getOriginalFilename();
                    String reUploadImageFileName = mediaUploadInterface.createServerFileName(originalReuploadImageFilename);
                    File reUploadImagefile = new File(mediaUploadInterface.getFullPath(reUploadImageFileName, imgFileUploadPath));
                    reUploadImageFiles.get(0).transferTo(reUploadImagefile);

                    File thumbImg = new File(product.getLocalProductImgUrl());
                    if(thumbImg.delete()){
                        log.info("재업로드 시 썸네일 이미지를 변경할 경우 기존 썸네일 이미지 삭제");
                    }

                    // 썸네일 이미지 로컬 및 서버 업로드 경로 업데이트
                    product.setLocalProductImgUrl(mediaUploadInterface.getFullPath(reUploadImageFileName, uploadFilePath));
                    product.setProductImgUrl(mediaUploadInterface.getFullPath(reUploadImageFileName, callImageFilePath));

                    // 썸네일 이미지가 삭제될 경우, 만약 해당 프로덕트에 연관된 활용이미지들이 존재할 경우, 기존 남아있는 활용 이미지 중 가장 먼저 업로드된 이미지를 썸네일 이미지로 지정
                } else {

                    File thumbImg = new File(product.getLocalProductImgUrl());
                    if(thumbImg.delete()){
                        log.info("재업로드 시 썸네일 이미지를 변경할 경우 기존 썸네일 이미지 삭제");
                    }

                    // 남아있는 활용 이미지들 중 첫번쨰로 들어온 이미지를 가져온다.
                    Media newThumbImg = mediaRepository.findAllByContentIdAndClassification(productId, "product").get(0);

                    // 썸네일 이미지 로컬 및 서버 업로드 경로 업데이트
                    product.setLocalProductImgUrl(newThumbImg.getLocalUploadUrl());
                    product.setProductImgUrl(newThumbImg.getMediaUploadUrl());

                    mediaRepository.deleteByMediaId(newThumbImg.getMediaId());
                }

                /**
                entityManager.flush();
                entityManager.clear();
                 **/
            }
        }

        int uploadCount = 0;

        // 새롭게 추가한 이미지들이 하나라도 있어야 하며, 파일명이 공백이지 않아야 한다.
        if (!reUploadImageFiles.isEmpty() && !Objects.equals(reUploadImageFiles.get(0).getOriginalFilename(), "")) {
            for (int i = 0; i < reUploadImageFiles.size(); i++) {
                // 업로드할 파일의 진짜 이름 추출
                String originalImgFilename = reUploadImageFiles.get(i).getOriginalFilename();

                // 난수화된 파일 이름과 확장자를 합친 파일명 추출
                String imgFileName = mediaUploadInterface.createServerFileName(originalImgFilename);

                if(i == 0) {
                    if (deletePrevThumbImgCheck &&
                            (mediaRepository.findAllByContentIdAndClassification(productId, "product") == null ||
                                    mediaRepository.findAllByContentIdAndClassification(productId, "product").isEmpty())) {
                        // 업로드한 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
                        File imgfile = new File(mediaUploadInterface.getFullPath(imgFileName, imgFileUploadPath));

                        // multipartfile에서 지원하는 transferTo 함수 사용. (해당 파일을 지정한 경로로 전송 - 현재 프로젝트 내부에 전송하고 있으므로 배포하게 되었을 때 수정해줘야 할 필요가 있음.)
                        reUploadImageFiles.get(i).transferTo(imgfile);

                        File thumbImg = new File(product.getLocalProductImgUrl());
                        if(thumbImg.delete()){
                            log.info("재업로드 시 썸네일 이미지를 변경할 경우 기존 썸네일 이미지 삭제");
                        }

                        // 썸네일 이미지 로컬 및 서버 업로드 경로 업데이트
                        product.setLocalProductImgUrl(mediaUploadInterface.getFullPath(imgFileName, imgFileUploadPath));
                        product.setProductImgUrl(mediaUploadInterface.getFullPath(imgFileName, callImageFilePath));

                    }else{
                        // 업로드한 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
                        File imgfile = new File(mediaUploadInterface.getFullPath(imgFileName, imgFileUploadPath));

                        // multipartfile에서 지원하는 transferTo 함수 사용. (해당 파일을 지정한 경로로 전송 - 현재 프로젝트 내부에 전송하고 있으므로 배포하게 되었을 때 수정해줘야 할 필요가 있음.)
                        reUploadImageFiles.get(i).transferTo(imgfile);

                        // 등록시키려는 파일들의 정보를 엔티티에 담아 저장
                        Media imageMedia = Media.builder()
                                .mediaUuidTitle(imgFileName) // 파일의 난수화된 이름
                                .mediaName(originalImgFilename) // 파일의 원래 이름
                                .classification("product") // 파일 용도 구분 (project, product, contact)
                                .mediaUploadUrl(mediaUploadInterface.getFullPath(imgFileName, callImageFilePath)) // 파일의 업로드 절대 경로
                                .localUploadUrl(mediaUploadInterface.getFullPath(imgFileName, imgFileUploadPath)) // 로컬 환경에서 업로드될 static 경로
                                .contentId(product.getProductId()) // 속한 컨텐츠의 id
                                .build();

                        // 미디어 정보 저장
                        mediaRepository.save(imageMedia);
                    }

                }else{
                    // 업로드한 파일과 경로를 합친 업로드 경로를 File 객체에 넣어 생성
                    File imgfile = new File(mediaUploadInterface.getFullPath(imgFileName, imgFileUploadPath));

                    // multipartfile에서 지원하는 transferTo 함수 사용. (해당 파일을 지정한 경로로 전송 - 현재 프로젝트 내부에 전송하고 있으므로 배포하게 되었을 때 수정해줘야 할 필요가 있음.)
                    reUploadImageFiles.get(i).transferTo(imgfile);

                    // 등록시키려는 파일들의 정보를 엔티티에 담아 저장
                    Media imageMedia = Media.builder()
                            .mediaUuidTitle(imgFileName) // 파일의 난수화된 이름
                            .mediaName(originalImgFilename) // 파일의 원래 이름
                            .classification("product") // 파일 용도 구분 (project, product, contact)
                            .mediaUploadUrl(mediaUploadInterface.getFullPath(imgFileName, callImageFilePath)) // 파일의 업로드 절대 경로
                            .localUploadUrl(mediaUploadInterface.getFullPath(imgFileName, imgFileUploadPath)) // 로컬 환경에서 업로드될 static 경로
                            .contentId(product.getProductId()) // 속한 컨텐츠의 id
                            .build();

                    // 미디어 정보 저장
                    mediaRepository.save(imageMedia);
                }

                uploadCount += 1;
            }
        }

        entityManager.flush();
        entityManager.clear();

        if (uploadCount == reUploadImageFiles.size()) {
            log.info("프로덕트 이미지 파일 업데이트 성공");
        } else {
            log.info("프로덕트 이미지 파일 업데이트 실패");
            return null;
        }


        return categoryName;
    }


    // 프로덕트 삭제
    @Transactional
    public Category deleteProduct(Long productId) {
        // 삭제할 프로덕트를 조회
        Product product = productRepository.findByProductId(productId);
        Long categoryId = product.getCategoryId();

        File engfile = new File(product.getFileEngLocalUploadUrl());
        File korfile = new File(product.getFileKorLocalUploadUrl());
        File thumbImg = new File(product.getLocalProductImgUrl());

        if(engfile.delete()){
            log.info("제품 영어 파일 삭제");
        }

        if(korfile.delete()){
            log.info("제품 한글 파일 삭제");
        }

        if(thumbImg.delete()){
            log.info("썸네일 이미지 삭제");
        }

        List<Media> forImages = mediaRepository.findAllByContentIdAndClassification(product.getProductId(), "product");

        for(Media eachMedia : forImages){
            File image = new File(eachMedia.getLocalUploadUrl());

            if(image.delete()){
                log.info("제품 관련 활용 이미지 삭제");
            }
        }

        // 삭제하고자 하는 프로덕트에 연관된 media 데이터 삭제
        mediaRepository.deleteAllByClassificationAndContentId("product", productId);
        // 프로덕트 삭제
        productRepository.deleteByProductId(productId);

        return categoryRepository.findByCategoryId(categoryId);
    }


    // 프로덕트 수정 시 보여질 기존의 활용 이미지들
    public List<MediaResponseDto> callPrevImages(Long productId) {

        // 활용 이미지들을 불러올 프로덕트 조회
        Product product = productRepository.findByProductId(productId);
        // 프로덕트와 연관된 등록된 활용 이미지들 조회
        List<Media> prevImages = mediaRepository.findAllByContentIdAndClassification(product.getProductId(), "product");

        // 최종적으로 기존에 등록된 이미지들이 저장되어 호출될 리스트
        List<MediaResponseDto> callPrevImages = new ArrayList<>();

        // 처음 프로덕트의 썸네일 이미지부터 넣기
        callPrevImages.add(
                MediaResponseDto.builder()
                        .mediaId(0L) // 썸네일 용 이미지는 Media 엔티티에 존재하지 않으므로 각 프로덕트의 썸네일 이미지의 id는 0으로 고정
                        .mediaUploadUrl(product.getProductImgUrl())
                        .contentId(product.getProductId())
                        .build()
        );

        // 썸네일 이미지 이외의 활용 이미지들을 리스트에 담기
        for (int p = 0; p < prevImages.size(); p++) {
            callPrevImages.add(
                    MediaResponseDto.builder()
                            .mediaId(prevImages.get(p).getMediaId())
                            .mediaUploadUrl(prevImages.get(p).getMediaUploadUrl())
                            .contentId(product.getProductId())
                            .build()
            );
        }

        log.info("조회하는 프로덕트 id : {}, 프로덕트 썸네일 이미지 경로 : {}", product.getProductId(), callPrevImages.get(0).getMediaUploadUrl());

        return callPrevImages;
    }
}
