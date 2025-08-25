package com.everyconti.every_conti.modules.recommendation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.everyconti.every_conti.constant.batch.BatchStatus;
import com.everyconti.every_conti.constant.batch.BatchJobType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BatchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BatchJobType batchName;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private BatchStatus status;

    public BatchLog(BatchJobType batchName, LocalDateTime startTime, LocalDateTime endTime, BatchStatus status) {
        this.batchName = batchName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
