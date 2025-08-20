package my.everyconti.every_conti.modules.conti.repository.contiSong;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import my.everyconti.every_conti.modules.conti.domain.ContiSong;
import my.everyconti.every_conti.modules.conti.domain.QContiSong;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ContiSongRepositoryImpl implements ContiSongRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ContiSong> findAllByContiIdOrderByOrderIndexAsc(Long contiId){
        QContiSong contiSong = QContiSong.contiSong;

        return queryFactory
                .selectFrom(contiSong)
                .where(contiSong.conti.id.eq(contiId))
                .orderBy(contiSong.orderIndex.asc())
                .fetch();
    }


    @Override
    public Integer findMaxOrderIndexByContiId(Long contiId) {
        QContiSong contiSong = QContiSong.contiSong;

        Integer max = queryFactory
                .select(contiSong.orderIndex.max())
                .from(contiSong)
                .where(contiSong.conti.id.eq(contiId))
                .fetchOne();

        return max != null ? max : -1; // 아무 곡도 없으면 -1 (다음 인덱스는 0부터 시작)
    }

    @Override
    public Set<Long> findSongIdsByContiId(Long contiId) {
        QContiSong contiSong = QContiSong.contiSong;

        List<Long> ids = queryFactory
                .select(contiSong.song.id)
                .from(contiSong)
                .where(contiSong.conti.id.eq(contiId))
                .distinct()
                .fetch();

        return new HashSet<>(ids);
    }
}
