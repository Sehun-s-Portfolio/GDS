package com.project.gds.media.repository;

import com.project.gds.media.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    // 프로젝트에 속한 이미지들 조회
    List<Media> findAllByContentIdAndClassification(Long contentId, String classification);

    // 썸네일 이미지 삭제 시 삭제할 등록된 썸네일 이미지 조회
    Media findByContentIdAndClassification(Long contentId, String classification);

    // 저장된 이미지 파일을 찾기위한 jpa 함수
    Media findByMediaId(Long mediaId);

    // 프로덕트 삭제 시 같이 삭제될 media 데이터
    void deleteAllByClassificationAndContentId(String classification, Long contentId);

    // 프로덕트 수정 시 노출된 기존 이미지들 중에서 제외시킬 이미지들을 삭제 처리
    void deleteByMediaId(Long mediaId);
}
