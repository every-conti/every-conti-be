package my.everyconti.every_conti.modules.recommendation.repository;

import my.everyconti.every_conti.constant.batch.BatchStatus;
import my.everyconti.every_conti.constant.batch.BatchJobType;
import my.everyconti.every_conti.modules.recommendation.domain.BatchLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BatchLogRepository extends JpaRepository<BatchLog, Long> {
    @Override
    BatchLog save(BatchLog batchLog);

    Optional<BatchLog> findTopByBatchNameAndStatusOrderByEndTimeDesc(BatchJobType batchName, BatchStatus status);
}
