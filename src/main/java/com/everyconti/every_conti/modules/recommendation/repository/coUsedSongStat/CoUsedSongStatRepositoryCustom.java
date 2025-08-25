package com.everyconti.every_conti.modules.recommendation.repository.coUsedSongStat;

import com.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;

import java.util.List;

public interface CoUsedSongStatRepositoryCustom {
    List<CoUsedSongStat> findCoUsedSongsBySongId(Long songId);
}
