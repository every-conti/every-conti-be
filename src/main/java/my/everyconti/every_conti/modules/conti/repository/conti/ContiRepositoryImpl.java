package my.everyconti.every_conti.modules.conti.repository.conti;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.conti.domain.Conti;
import my.everyconti.every_conti.modules.conti.domain.QConti;
import my.everyconti.every_conti.modules.conti.domain.QContiSong;
import my.everyconti.every_conti.modules.song.domain.QSong;

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
                .fetchOne();
        if (existingConti == null) throw new EntityNotFoundException(ResponseMessage.notFoundMessage("콘티"));

        return existingConti;
    }

    @Override
    public Conti getContiAndContiSongByContiId(Long innerContiSongId){
        QConti conti = QConti.conti;
        QContiSong contiSong = QContiSong.contiSong;

        Conti existingConti = queryFactory.selectFrom(QConti.conti)
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .leftJoin(conti.creator).fetchJoin()
                .where(conti.id.eq(innerContiSongId))
                .fetchOne();
        if (existingConti == null) throw new EntityNotFoundException(ResponseMessage.notFoundMessage("콘티"));

        return existingConti;
    }
}
