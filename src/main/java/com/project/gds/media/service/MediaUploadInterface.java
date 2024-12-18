package com.project.gds.media.service;

import com.project.gds.contact.domain.Contact;
import com.project.gds.product.request.ProductRequestDto;
import com.project.gds.product.response.ProductResponseDto;
import com.project.gds.project.domain.Project;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public interface MediaUploadInterface {

    /** 업로드 될 경로 **/
    String getFullPath(String filename, String fileDir);

    /** 파일 업로드 후 썸네일 이미지 경로 반환 **/
    boolean projectUploadFile(List<MultipartFile> projectImages, Project project, String thumbnailUuidFileName, String classification, String purpose) throws IOException;

    /** 문의 사항 관련 파일 업로드 **/
    boolean contactUploadFile(List<MultipartFile> projectImages, Contact contact, String classification) throws IOException;

    /** 프로덕트 관련 파일 업로드 **/
    ProductResponseDto productUploadFile(HashMap<String, MultipartFile> fileSet, List<MultipartFile> productFile, String classification, ProductRequestDto productRequestDto) throws IOException;

    /** 난수화한 업로드할 파일 이름 **/
    String createServerFileName(String originalFilename);

    /** 업로드 파일 확장자 정보 추출 **/
    String extractExt(String originalFilename);
}
