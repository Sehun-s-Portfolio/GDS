package com.project.gds.download.service;

import com.project.gds.category.domain.Category;
import com.project.gds.category.repository.CategoryRepository;
import com.project.gds.download.response.DownloadProductListResponseDTo;
import com.project.gds.download.response.DownloadResponseDto;
import com.project.gds.media.domain.Media;
import com.project.gds.media.repository.MediaRepository;
import com.project.gds.media.response.MediaResponseDto;
import com.project.gds.product.domain.Product;
import com.project.gds.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DownloadService {

    private final MediaRepository mediaRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // 다운로드 페이지에 나올 프로덕트들의 정보 묶음
    public List<DownloadResponseDto> downloadFileList(String language) {
        // 전체 카테고리 조회
        List<Category> categoryList = categoryRepository.findAll();
        // 최종적으로 반환시킬 리스트 객체 생성
        List<DownloadResponseDto> productFiles = new ArrayList<>();

        for (Category eachCategory : categoryList) {
            // 전체 제품들을 최신 순으로 전체 조회
            List<Product> productList = productRepository.findAllByCategoryId(eachCategory.getCategoryId());
            List<DownloadProductListResponseDTo> containProductList = new ArrayList<>();
            List<MediaResponseDto> catalogImages = new ArrayList<>();

            if(!productList.isEmpty()){
                // 전체 제품들을 조회
                for (Product eachProduct : productList) {

                    // 각 제품에 해당하는 이미지들을 조회
                    List<Media> useImages = mediaRepository.findAllByContentIdAndClassification(eachProduct.getProductId(), "product");
                    // 반환시킬 이미지 리스트 객체 생성
                    List<MediaResponseDto> useImagesList = new ArrayList<>();

                    // 이미지가 썸네일용 하나만 존재할 경우 썸네일 이미지를 가져온다.
                    if(useImages.isEmpty()){
                        // 프로덕트 엔티티에 저장된 썸네일 이미지 업로드 경로를 넣는다.
                        useImagesList.add(
                                MediaResponseDto.builder()
                                        .mediaUploadUrl(eachProduct.getProductImgUrl()) // 이미지 서버 업로드 경로
                                        .build()
                        );

                        catalogImages = useImagesList;
                    }else{
                        // 제품에 속한 이미지들을 하나씩 조회
                        for (Media eachImage : useImages) {

                            // 이미지들을 조회하여 반환 리스트 객체에 저장
                            useImagesList.add(
                                    MediaResponseDto.builder()
                                            .mediaId(eachImage.getMediaId()) // 이미지 id
                                            .mediaUuidTitle(eachImage.getMediaUuidTitle()) // 이미지의 난수화된 이름
                                            .mediaName(eachImage.getMediaName()) // 이미지의 원래 이름
                                            .classification(eachImage.getClassification()) // 이미지 용도 구분
                                            .mediaUploadUrl(eachImage.getMediaUploadUrl()) // 이미지 서버 업로드 경로
                                            .localUploadUrl(eachImage.getLocalUploadUrl()) // 이미지 로컬 환경 업로드 경로
                                            .contentId(eachImage.getContentId()) // 이미지가 속한 용도 구분 id
                                            .build()
                            );
                        }

                        catalogImages = useImagesList;
                    }


                    if (language.equals("Korean")) {

                        String productFileName = eachProduct.getProductName() + ".pdf";

                        if(eachProduct.getFileEngName() != null){
                            productFileName = eachProduct.getFileKorName();
                        }

                        containProductList.add(
                                DownloadProductListResponseDTo.builder()
                                        .productName(eachProduct.getProductName())
                                        .productFile(eachProduct.getFileKorUploadUrl())
                                        .productFileName(productFileName)
                                        .build()
                        );
                    } else if (language.equals("English")) {

                        String productFileName = eachProduct.getProductEngName() + ".pdf";

                        if(eachProduct.getFileEngName() != null){
                            productFileName = eachProduct.getFileEngName();
                        }

                        containProductList.add(
                                DownloadProductListResponseDTo.builder()
                                        .productName(eachProduct.getProductName())
                                        .productFile(eachProduct.getFileEngUploadUrl())
                                        .productFileName(productFileName)
                                        .build()
                        );
                    }
                }

                // 최종적으로 반환될 제품 리스트에 제품들의 노출 정보들을 저장
                productFiles.add(
                        DownloadResponseDto.builder()
                                .medias(catalogImages) // 제품에 해당하는 활용 이미지들
                                .categoryName(eachCategory.getCategoryName())
                                .products(containProductList)
                                .build()
                );
            }

        }

        return productFiles;
    }
}
