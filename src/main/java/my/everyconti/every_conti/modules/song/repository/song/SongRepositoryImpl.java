package my.everyconti.every_conti.modules.song.repository.song;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.song.domain.QSong;
import my.everyconti.every_conti.modules.song.domain.QSongSongTheme;
import my.everyconti.every_conti.modules.song.domain.Song;

@AllArgsConstructor
public class SongRepositoryImpl implements SongRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Song getSongByIdWithJoin(Long innerSongId){
        QSong song = QSong.song;
        QSongSongTheme songSongTheme = QSongSongTheme.songSongTheme;

        Song existingSong = queryFactory.selectFrom(song)
                .leftJoin(song.songThemes, songSongTheme).fetchJoin()
                .leftJoin(songSongTheme.songTheme).fetchJoin()
                .leftJoin(song.creator).fetchJoin()
                .leftJoin(song.praiseTeam).fetchJoin()
                .where(song.id.eq(innerSongId))
                .fetchOne();

        if (existingSong == null) throw new EntityNotFoundException(ResponseMessage.notFoundMessage("찬양"));

        return existingSong;
    }
}
