package com.everyconti.every_conti.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 현재 시간 기준 Column 생성 시에 extends로 사용
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class NowTimeForJpa {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
