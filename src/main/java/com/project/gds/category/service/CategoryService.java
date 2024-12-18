package com.project.gds.category.service;

import com.project.gds.category.domain.Category;
import com.project.gds.category.repository.CategoryRepository;
import com.project.gds.category.request.CategoryRequestDto;
import com.project.gds.category.request.CategoryUpdateRequestDto;
import com.project.gds.category.response.CategoryResponseDto;
import com.project.gds.exception.category.CategoryExceptionInterface;
import com.project.gds.media.domain.Media;
import com.project.gds.media.repository.MediaRepository;
import com.project.gds.product.domain.Product;
import com.project.gds.product.repository.ProductRepository;
import com.project.gds.share.ResponseBody;
import com.project.gds.share.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryExceptionInterface categoryExceptionInterface;
    private final ProductRepository productRepository;
    private final MediaRepository mediaRepository;

    // 카테고리 생성
    public void categoryAdd(CategoryRequestDto categoryRequestDto) {
        // 기존에 존재하고 있는 중복된 카테고리 명이라면 예외 처리
        if (categoryExceptionInterface.checkDuplicateCategory(categoryRequestDto.getCategoryName())) {
            log.info("{}", new ResponseEntity<>(new ResponseBody<>(StatusCode.DUPLICATE_CATEGORY, null), HttpStatus.BAD_REQUEST));
        } else {
            log.info("카테고리 생성 service 진입 - 카테고리 : {}", categoryRequestDto.getCategoryName());

            // 요청 카테고리 정보 반영
            Category category = Category.builder()
                    .orgCategoryId(Long.parseLong(categoryRequestDto.getOrgCategory()))
                    .categoryName(categoryRequestDto.getCategoryName()) // 카테고리 명
                    .categoryKorDescription(categoryRequestDto.getCategoryKorDescription()) // 카테고리 한글 설명
                    .categoryEngDescription(categoryRequestDto.getCategoryEngDescription()) // 카테고리 영어 설명
                    .build();

            // 카테고리 등록
            categoryRepository.save(category);
        }
    }


    // 카테고리 삭제
    @Transactional
    public Category categoryDelete(Long categoryId) {
        // 카테고리에 속한 프로덕트들 조회
        List<Product> productList = productRepository.findAllByCategoryId(categoryId);

        if (!productList.isEmpty()) {
            // 조회된 프로덕트들이 가지고 있는 파일들 삭제 처리
            for (Product eachProduct : productList) {
                File engfile = new File(eachProduct.getFileEngLocalUploadUrl());
                File korfile = new File(eachProduct.getFileKorLocalUploadUrl());
                File thumbImg = new File(eachProduct.getLocalProductImgUrl());

                if(engfile.delete()){
                    log.info("제품 영어 파일 삭제");
                }

                if(korfile.delete()){
                    log.info("제품 한글 파일 삭제");
                }

                if(thumbImg.delete()){
                    log.info("썸네일 이미지 삭제");
                }

                List<Media> forImages = mediaRepository.findAllByContentIdAndClassification(eachProduct.getProductId(), "product");

                for(Media eachMedia : forImages){
                    File image = new File(eachMedia.getLocalUploadUrl());

                    if(image.delete()){
                        log.info("제품 관련 활용 이미지 삭제");
                    }
                }

                mediaRepository.deleteAllByClassificationAndContentId("product", eachProduct.getProductId());
            }

            // 카테고리에 속한 프로덕트들 삭제
            productRepository.deleteAllByCategoryId(categoryId);
        }

        // 카테고리 삭제
        categoryRepository.deleteByCategoryId(categoryId);

        return categoryRepository.findByCategoryId(categoryId);
    }

    // 카테고리 메뉴 호버 리스트 조회
    public List<CategoryResponseDto> categoryList() {

        // 카테고리 명 기준 정렬 리스트 조회
        List<Category> categoryList = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "categoryName"));
        // 확인하기 위한 반환 리스트 객체 생성
        List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();


        // 반환 리스트 객체에 전체 카테고리 메뉴 정보 저장
        for (Category eachCategory : categoryList) {
            List<Product> productList = productRepository.findAllByCategoryId(eachCategory.getCategoryId());
            List<String> productNameList = new ArrayList<>();

            for (Product eachProduct : productList) {
                productNameList.add(eachProduct.getProductName());
            }

            categoryResponseDtos.add(
                    CategoryResponseDto.builder()
                            .orgCategoryId(eachCategory.getOrgCategoryId()) // 대분류 카테고리 id
                            .categoryId(eachCategory.getCategoryId()) // 카테고리 id
                            .categoryName(eachCategory.getCategoryName()) // 카테고리 명
                            .productList(productNameList) // 카테고리에 속한 프로덕트 리스트
                            .build()
            );

        }

        return categoryResponseDtos;
    }


    // 카테고리 수정
    @Transactional
    public Category categoryUpdate(Long categoryId, CategoryUpdateRequestDto categoryRequestDto) {

        // 수정하고자 하는 카테고리 호출
        Category category = categoryRepository.findByCategoryId(categoryId);

        if(categoryExceptionInterface.checkUpdateCategoryInfo(category, categoryRequestDto)){
            return null;
        }else{
            // 카테고리 수정
            category.setOrgCategoryId(Long.parseLong(categoryRequestDto.getUpdateOrgCategory()));
            category.setCategoryName(categoryRequestDto.getUpdateCategoryName());
            category.setCategoryEngDescription(categoryRequestDto.getUpdateCategoryEngDescription());
            category.setCategoryKorDescription(categoryRequestDto.getUpdateCategoryKorDescription());

            log.info("수정 진행합니다.");
            return category;
        }

    }

}
