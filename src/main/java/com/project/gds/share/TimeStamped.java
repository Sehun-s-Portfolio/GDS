package com.project.gds.share;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeStamped {

  @JsonFormat(timezone = "Asia/Seoul")
  @CreatedDate
  private LocalDateTime createdAt; // 생성 일자

  @JsonFormat(timezone = "Asia/Seoul")
  @LastModifiedDate
  private LocalDateTime modifiedAt; // 수정 일자

}
