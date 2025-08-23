package my.everyconti.every_conti.modules.conti.repository.conti;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import my.everyconti.every_conti.common.exception.types.NotFoundException;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.conti.domain.Conti;
import my.everyconti.every_conti.modules.conti.domain.QConti;
import my.everyconti.every_conti.modules.conti.domain.QContiSong;
import my.everyconti.every_conti.modules.conti.domain.es.ContiDocument;
import my.everyconti.every_conti.modules.conti.dto.request.SearchContiDto;
import my.everyconti.every_conti.modules.conti.repository.es.ContiSearchRepository;
import my.everyconti.every_conti.modules.member.domain.QMember;
import my.everyconti.every_conti.modules.song.domain.QPraiseTeam;
import my.everyconti.every_conti.modules.song.domain.QSong;
import my.everyconti.every_conti.modules.song.domain.QSongSongTheme;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ContiRepositoryImpl implements ContiRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final ContiSearchRepository contiSearchRepository;
    private final HashIdUtil hashIdUtil;

    @Override
    public Conti getContiByIdWithJoin(Long innerContiId){
        QConti conti = QConti.conti;
        QContiSong contiSong = QContiSong.contiSong;
        QSong song = QSong.song;

        Conti existingConti = queryFactory.selectFrom(QConti.conti)
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .leftJoin(contiSong.song, song).fetchJoin()
                .leftJoin(conti.creator).fetchJoin()
                .where(QConti.conti.id.eq(innerContiId))
                .distinct()
                .fetchOne();
        if (existingConti == null) throw new EntityNotFoundException(ResponseMessage.notFoundMessage("콘티"));

        return existingConti;
    }

    @Override
    public Conti getContiAndContiSongByContiId(Long innerContiId){
        QConti conti = QConti.conti;
        QContiSong contiSong = QContiSong.contiSong;

        Conti existingConti = queryFactory.selectFrom(QConti.conti)
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .leftJoin(conti.creator).fetchJoin()
                .where(conti.id.eq(innerContiId))
                .distinct()
                .fetchOne();
        if (existingConti == null) throw new EntityNotFoundException(ResponseMessage.notFoundMessage("콘티"));

        return existingConti;
    }

    @Override
    public List<Conti> findContisWithJoin(){
        QConti conti = QConti.conti;
        QContiSong contiSong = QContiSong.contiSong;

        return queryFactory.selectFrom(QConti.conti)
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .distinct()
                .fetch();
    }

    @Override
    public Conti getContiDetail(Long innerContiId){
        QConti conti = QConti.conti;
        QContiSong  contiSong = QContiSong.contiSong;
        QSong song = QSong.song;
        QSongSongTheme songSongTheme = QSongSongTheme.songSongTheme;

        return queryFactory
                .selectFrom(conti)
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .leftJoin(contiSong.song, song).fetchJoin()
                .leftJoin(song.songThemes, songSongTheme).fetchJoin()
                .leftJoin(songSongTheme.songTheme).fetchJoin()
                .leftJoin(conti.creator).fetchJoin()
                .leftJoin(song.praiseTeam).fetchJoin()
                .where(conti.id.eq(innerContiId))
                .distinct()
                .fetch()
                .getFirst();
    }

//    @Override
//    public List<Conti> findContisByPraiseTeam_Id(Long praiseTeamId){
//        QConti conti = QConti.conti;
//        QMember member = QMember.member;
//        QPraiseTeam praiseTeam = QPraiseTeam.praiseTeam;
//
//        List<Conti> contis = queryFactory
//                .selectFrom(conti)
//                .leftJoin(conti.contiSongs).fetchJoin()
//                .leftJoin(conti.creator, member).fetchJoin()
//                .leftJoin(member.praiseTeam, praiseTeam).fetchJoin()
//                .where(praiseTeam.id.eq(praiseTeamId))
//                .distinct()
//                .fetch();
//        if (contis.isEmpty()) throw new NotFoundException(ResponseMessage.notFoundMessage("콘티"));
//        return contis;
//    }

    @Override
    public List<Conti> findLastContiOfFamousPraiseTeams(){
        QConti conti = QConti.conti;
        QContiSong contiSong = QContiSong.contiSong;
//        QConti contiSub = new QConti("contiSub");
        QMember creator = QMember.member;
        QPraiseTeam praiseTeam = QPraiseTeam.praiseTeam;

        List<Long> lastContiIds = queryFactory
                .select(conti.id)
                .from(conti)
                .join(conti.creator, creator)
                .join(creator.praiseTeam, praiseTeam)
                .where(praiseTeam.isFamous.isTrue())
                .groupBy(praiseTeam.id)
                .select(conti.id.max())
                .fetch();

        if (lastContiIds.isEmpty()) throw new NotFoundException(ResponseMessage.notFoundMessage("콘티"));

        // 본 쿼리에서는 해당 ID들만 가져오고 필요한 데이터 fetchJoin
        return queryFactory
                .selectFrom(conti)
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .leftJoin(contiSong.song).fetchJoin()
                .leftJoin(conti.creator, creator).fetchJoin()
                .leftJoin(creator.praiseTeam, praiseTeam).fetchJoin()
                .where(conti.id.in(lastContiIds))
                .orderBy(conti.date.desc())
                .fetch();
    }

    @Override
    public List<Conti> findContisWithMemberId(Long memberId, Long offset){
        QConti conti = QConti.conti;
        QMember creator = QMember.member;
        QContiSong contiSong = QContiSong.contiSong;
        QPraiseTeam praiseTeam = QPraiseTeam.praiseTeam;

        List<Conti> contis = queryFactory
                .selectFrom(conti)
                .leftJoin(conti.creator, creator).fetchJoin()
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .leftJoin(contiSong.song).fetchJoin()
                .leftJoin(conti.creator.praiseTeam, praiseTeam).fetchJoin()
                .where(conti.creator.id.eq(memberId))
                .orderBy(conti.date.desc())
                .offset(offset)
                .limit(11)
                .fetch();
        return contis;
    }

    @Override
    public List<Conti> findContisWithSearchParams(SearchContiDto dto) {
        String text = dto.getText();
        List<String> songIds = dto.getSongIds();
        String praiseTeamId = dto.getPraiseTeamId();
        Boolean isFamous = dto.getIsFamous();
        Boolean includePersonalConti = dto.getIncludePersonalConti();
        Integer minTotalDuration = dto.getMinTotalDuration();
        Integer maxTotalDuration = dto.getMaxTotalDuration();
        long offset = dto.getOffset() != null ? dto.getOffset() : 0L;

        final int LIMIT = 21;

        QConti conti = QConti.conti;
        QContiSong contiSong = QContiSong.contiSong;
        QSong song = QSong.song;
        QMember creator = QMember.member;
        QPraiseTeam praiseTeam = QPraiseTeam.praiseTeam;

        BooleanBuilder where = new BooleanBuilder();

        List<Long> esOrderedIds = null;
        if (text != null && !text.isBlank()) {
            int pageIndex = (int) (offset / LIMIT);
            var page = contiSearchRepository.fullTextSearch(
                    text, org.springframework.data.domain.PageRequest.of(pageIndex, LIMIT));
            esOrderedIds = page.getContent().stream().map(ContiDocument::getId).toList();
            if (esOrderedIds.isEmpty()) return List.of();

            where.and(conti.id.in(esOrderedIds));
        }

        if (songIds != null && !songIds.isEmpty()) {
            List<Long> songIdLongs = songIds.stream().map(hashIdUtil::decode).toList();
            where.and(contiSong.song.id.in(songIdLongs));
        }

        if (isFamous != null) {
            where.and(praiseTeam.isFamous.eq(isFamous));
        }

        if (praiseTeamId != null && !praiseTeamId.isBlank()) {
            where.and(creator.praiseTeam.id.eq(hashIdUtil.decode(praiseTeamId)));
        }

        if (Boolean.FALSE.equals(includePersonalConti)) {
            where.and(creator.praiseTeam.isNotNull());
        }

        List<Long> contiIds;
        if (esOrderedIds == null) {
            JPAQuery<Long> idQuery = queryFactory
                    .select(conti.id)
                    .from(conti)
                    .join(conti.contiSongs, contiSong)
                    .leftJoin(contiSong.song, song)
                    .leftJoin(conti.creator, creator)
                    .leftJoin(creator.praiseTeam, praiseTeam)
                    .where(where);

            if (minTotalDuration != null && maxTotalDuration != null) {
                idQuery.groupBy(conti.id, conti.date)
                        .having(song.duration.sum().between(minTotalDuration, maxTotalDuration));
            } else {
                idQuery.groupBy(conti.id, conti.date);
            }

            contiIds = idQuery
                    .orderBy(conti.date.desc())
                    .offset(offset)
                    .limit(LIMIT)
                    .fetch();

            if (contiIds.isEmpty()) return List.of();
        } else {
            contiIds = esOrderedIds;
        }

        // CASE 정렬식 구성 (없으면 뒤로 보내기)
        com.querydsl.core.types.dsl.NumberExpression<Integer> orderExpr =
                com.querydsl.core.types.dsl.Expressions.asNumber(Integer.MAX_VALUE);
        for (int i = contiIds.size() - 1; i >= 0; i--) {
            Long id = contiIds.get(i);
            orderExpr = new com.querydsl.core.types.dsl.CaseBuilder()
                    .when(conti.id.eq(id)).then(i)
                    .otherwise(orderExpr);
        }

        var fetchQuery = queryFactory
                .selectFrom(conti)
                .leftJoin(conti.creator, creator).fetchJoin()
                .leftJoin(creator.praiseTeam, praiseTeam).fetchJoin()
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .leftJoin(contiSong.song, song).fetchJoin()
                .where(conti.id.in(contiIds))
                .orderBy(orderExpr.asc(), conti.date.desc());

        return fetchQuery.fetch();
    }
}
