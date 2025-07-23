package my.everyconti.every_conti.modules.conti.repository.conti;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import my.everyconti.every_conti.common.exception.types.NotFoundException;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.conti.domain.Conti;
import my.everyconti.every_conti.modules.conti.domain.QConti;
import my.everyconti.every_conti.modules.conti.domain.QContiSong;
import my.everyconti.every_conti.modules.member.domain.QMember;
import my.everyconti.every_conti.modules.song.domain.QPraiseTeam;
import my.everyconti.every_conti.modules.song.domain.QSong;
import my.everyconti.every_conti.modules.song.domain.QSongSongTheme;

import java.util.List;

@AllArgsConstructor
public class ContiRepositoryImpl implements ContiRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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

    @Override
    public List<Conti> findContisByPraiseTeam_Id(Long praiseTeamId){
        QConti conti = QConti.conti;
        QMember member = QMember.member;
        QPraiseTeam praiseTeam = QPraiseTeam.praiseTeam;

        List<Conti> contis = queryFactory
                .selectFrom(conti)
                .leftJoin(conti.contiSongs).fetchJoin()
                .leftJoin(conti.creator, member).fetchJoin()
                .leftJoin(member.praiseTeam, praiseTeam).fetchJoin()
                .where(praiseTeam.id.eq(praiseTeamId))
                .distinct()
                .fetch();
        if (contis.isEmpty()) throw new NotFoundException(ResponseMessage.notFoundMessage("콘티"));
        return contis;
    }

    @Override
    public List<Conti> findLastContiOfFamousPraiseTeams(){
        QConti conti = QConti.conti;
        QContiSong contiSong = QContiSong.contiSong;
        QConti contiSub = new QConti("contiSub");
        QMember member = QMember.member;
        QPraiseTeam praiseTeam = QPraiseTeam.praiseTeam;

        List<Long> lastContiIds = queryFactory
                .select(conti.id)
                .from(conti)
                .join(conti.creator, member)
                .join(member.praiseTeam, praiseTeam)
                .where(praiseTeam.isFamous.isTrue())
                .groupBy(praiseTeam.id)
                .select(conti.id.max()) // 또는 order by + limit if dialect supports
                .fetch();

        if (lastContiIds.isEmpty()) throw new NotFoundException(ResponseMessage.notFoundMessage("콘티"));

        // 본 쿼리에서는 해당 ID들만 가져오고 필요한 데이터 fetchJoin
        return queryFactory
                .selectFrom(conti)
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .leftJoin(contiSong.song).fetchJoin()
                .leftJoin(conti.creator, member).fetchJoin()
                .leftJoin(member.praiseTeam, praiseTeam).fetchJoin()
                .where(conti.id.in(lastContiIds))
                .fetch();
    }
}
