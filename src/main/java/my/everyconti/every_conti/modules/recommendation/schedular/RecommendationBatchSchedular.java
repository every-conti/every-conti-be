package my.everyconti.every_conti.modules.recommendation.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.everyconti.every_conti.constant.batch.BatchJobType;
import my.everyconti.every_conti.constant.batch.BatchStatus;
import my.everyconti.every_conti.modules.conti.domain.Conti;
import my.everyconti.every_conti.modules.conti.repository.conti.ContiRepository;
import my.everyconti.every_conti.modules.recommendation.domain.BatchLog;
import my.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;
import my.everyconti.every_conti.modules.recommendation.repository.BatchLogRepository;
import my.everyconti.every_conti.modules.recommendation.repository.coUsedSongStat.CoUsedSongStatRepository;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationBatchSchedular {

    private final BatchLogRepository batchLogRepository;
    private final CoUsedSongStatRepository coUsedSongStatRepository;
    private final ContiRepository contiRepository;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void runCoUsedSongStatBatch() {
        coUsedSongStatBatch();
    }

    private void coUsedSongStatBatch(){
        log.info("CO_USED_SONG_STAT : 1. 새 배치 로그 생성");
        System.out.println("CO_USED_SONG_STAT : 1. 새 배치 로그 생성");
        BatchLog batchLog = new BatchLog(BatchJobType.CO_USED_SONG_STAT, LocalDateTime.now(), null, BatchStatus.RUNNING);
        batchLogRepository.save(batchLog);

        // 2. 전체 삭제
        log.info("CO_USED_SONG_STAT : 2. co_sued_song_stat 전체 삭제");
        System.out.println("CO_USED_SONG_STAT : 2. co_sued_song_stat 전체 삭제");
        coUsedSongStatRepository.deleteAll();
        coUsedSongStatRepository.flush();

        try {
            // 3. 모든 콘티 조회, 콘티 곡으로 조합 만들어 통계 데이터 생성
            log.info("CO_USED_SONG_STAT : 3. 콘티 전체 조회, 통계 데이터 생성");
            System.out.println("CO_USED_SONG_STAT : 3. 콘티 전체 조회, 통계 데이터 생성");
            List<Conti> contis = contiRepository.findContisWithJoin();
            Map<Pair<Long, Long>, Long> statMap = new HashMap<>();

            for (Conti conti : contis) {
                List<Long> songIds = conti.getContiSongs().stream().map(cs -> cs.getSong().getId()).toList();

                for (int i = 0; i < songIds.size(); i++) {
                    for (int j = i + 1; j < songIds.size(); j++) {
                        Long a = Math.min(songIds.get(i), songIds.get(j));
                        Long b = Math.max(songIds.get(i), songIds.get(j));
                        Pair<Long, Long> key = Pair.of(a, b);
                        statMap.put(key, statMap.getOrDefault(key, 0L) + 1);
                    }
                }
            }
            System.out.println("statMap = " + statMap);

            // 4. 전체 stat 저장
            log.info("CO_USED_SONG_STAT : 전체 stat 저장");
            System.out.println("CO_USED_SONG_STAT : 전체 stat 저장");
            List<CoUsedSongStat> stats = statMap.entrySet().stream()
                    .map(entry -> new CoUsedSongStat(null, entry.getKey().getFirst(), entry.getKey().getSecond(), entry.getValue())).toList();

            coUsedSongStatRepository.saveAll(stats);

            // 5. 성공 처리
            System.out.println("CO_USED_SONG_STAT : 5. 배치 성공 처리");
            log.info("CO_USED_SONG_STAT : 5. 배치 성공 처리");
            batchLog.setEndTime(LocalDateTime.now());
            batchLog.setStatus(BatchStatus.SUCCESS);
            batchLogRepository.save(batchLog);

        } catch (Exception e) {
            log.error("CO_USED_SONG_STAT : 에러 발생, stackTrace: " + Arrays.toString(e.getStackTrace()));
            batchLog.setEndTime(LocalDateTime.now());
            batchLog.setStatus(BatchStatus.FAILED);
            batchLogRepository.save(batchLog);
            throw e;
        }
    }
}
