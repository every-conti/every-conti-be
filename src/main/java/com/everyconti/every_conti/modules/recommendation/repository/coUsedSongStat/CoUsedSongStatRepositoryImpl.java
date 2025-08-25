package com.everyconti.every_conti.modules.recommendation.repository.coUsedSongStat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import com.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;
import com.everyconti.every_conti.modules.recommendation.domain.QCoUsedSongStat;

import java.util.List;

@RequiredArgsConstructor
public class CoUsedSongStatRepositoryImpl implements CoUsedSongStatRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CoUsedSongStat> findCoUsedSongsBySongId(Long songId){
        QCoUsedSongStat coUsedSongStat = QCoUsedSongStat.coUsedSongStat;

        return queryFactory
                .selectFrom(coUsedSongStat)
                .where(coUsedSongStat.songIdA.eq(songId).or(coUsedSongStat.songIdB.eq(songId)))
                .limit(5)
                .orderBy(coUsedSongStat.usageCount.desc())
                .fetch();
    }


}
