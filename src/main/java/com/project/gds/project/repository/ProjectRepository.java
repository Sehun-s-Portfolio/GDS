package com.project.gds.project.repository;

import com.project.gds.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // 프로젝트 조회
    Project findByProjectId(Long projectId);

    // 프로젝트 리스트 조회
    List<Project> findAll();

    // 메인 페이지 노출 프로젝트 3개
    List<Project> findTop3ByOrderByCreatedAt();
}
