package my.everyconti.every_conti.modules.recommendation.repository.coUsedSongStat;

import my.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;

import java.util.List;

public interface CoUsedSongStatRepositoryCustom {
    List<CoUsedSongStat> findCoUsedSongsBySongId(Long songId);
}
