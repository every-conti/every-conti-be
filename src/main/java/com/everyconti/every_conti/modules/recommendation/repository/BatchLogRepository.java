package com.everyconti.every_conti.modules.recommendation.repository;

import com.everyconti.every_conti.constant.batch.BatchStatus;
import com.everyconti.every_conti.constant.batch.BatchJobType;
import com.everyconti.every_conti.modules.recommendation.domain.BatchLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatchLogRepository extends JpaRepository<BatchLog, Long> {
    @Override
    BatchLog save(BatchLog batchLog);

    Optional<BatchLog> findTopByBatchNameAndStatusOrderByEndTimeDesc(BatchJobType batchName, BatchStatus status);
}
