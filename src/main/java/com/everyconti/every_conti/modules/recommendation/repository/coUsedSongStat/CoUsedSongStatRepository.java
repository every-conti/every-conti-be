package com.everyconti.every_conti.modules.recommendation.repository.coUsedSongStat;

import com.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoUsedSongStatRepository extends JpaRepository<CoUsedSongStat, Long>, CoUsedSongStatRepositoryCustom {
    @Override
    CoUsedSongStat save(CoUsedSongStat coUsedSongStat);
}
