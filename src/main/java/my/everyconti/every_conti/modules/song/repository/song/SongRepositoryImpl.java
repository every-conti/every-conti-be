package my.everyconti.every_conti.modules.song.repository.song;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.song.domain.QSong;
import my.everyconti.every_conti.modules.song.domain.QSongSongTheme;
import my.everyconti.every_conti.modules.song.domain.QSongTheme;
import my.everyconti.every_conti.modules.song.domain.Song;
import my.everyconti.every_conti.modules.song.dto.request.SearchSongDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class SongRepositoryImpl implements SongRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Song findSongByIdWithJoin(Long innerSongId){
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

    @Override
    public List<Song> findLastSongsWithPraiseTeam(Integer count){
        QSong song = QSong.song;

        return queryFactory.selectFrom(song)
                .leftJoin(song.praiseTeam).fetchJoin()
                .orderBy(song.createdAt.desc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<Song> findSongsWithSearchParams(SearchSongDto searchSongDto, HashIdUtil hashIdUtil){
        String text = searchSongDto.getText();

        SongType songType = searchSongDto.getSongType();
        String praiseTeamId = searchSongDto.getPraiseTeamId();
        List<String> themeIds = searchSongDto.getThemeIds();
        String seasonId = searchSongDto.getSeasonId();
        SongTempo tempo = searchSongDto.getTempo();
        String bibleId = searchSongDto.getBibleId();
        String bibleChapterId = searchSongDto.getBibleChapterId();
        String bibleVerseId = searchSongDto.getBibleVerseId();
        Integer offset = searchSongDto.getOffset() != null ? searchSongDto.getOffset() : 0;

        QSong song = QSong.song;
        BooleanBuilder builder = new BooleanBuilder();

        if (text != null && !text.isBlank()) {
            builder.and(song.songName.containsIgnoreCase(text)
                    .or(song.lyrics.containsIgnoreCase(text)));
        }

        if (songType != null) {
            builder.and(song.songType.eq(songType));
        }

        if (praiseTeamId != null && !praiseTeamId.isBlank()) {
            builder.and(song.praiseTeam.id.eq(hashIdUtil.decode(praiseTeamId)));
        }

        if (seasonId != null && !seasonId.isBlank()) {
            builder.and(song.season.id.eq(hashIdUtil.decode(seasonId)));
        }

        if (tempo != null) {
            builder.and(song.tempo.eq(tempo));
        }

        if (bibleId != null && !bibleId.isBlank()) {
            System.out.println("bibleId 있음 처리");
            builder.and(song.bible.id.eq(hashIdUtil.decode(bibleId)));
        }

        if (bibleChapterId != null && !bibleChapterId.isBlank()) {
            builder.and(song.bibleChapter.id.eq(hashIdUtil.decode(bibleChapterId)));
        }

        if (bibleVerseId != null && !bibleVerseId.isBlank()) {
            builder.and(song.bibleVerse.id.eq(hashIdUtil.decode(bibleVerseId)));
        }

        JPAQuery<Song> query = queryFactory
                .selectFrom(song)
                .leftJoin(song.creator).fetchJoin()
                .leftJoin(song.praiseTeam).fetchJoin()
                .leftJoin(song.season).fetchJoin()
                .leftJoin(song.songThemes, QSongSongTheme.songSongTheme).fetchJoin()
                .leftJoin(QSongSongTheme.songSongTheme.songTheme).fetchJoin()
                .distinct();

        if (themeIds != null && !themeIds.isEmpty()) {
            Set<Long> themeIdLongs = new HashSet<>(themeIds.stream().map(hashIdUtil::decode).toList());

            QSongSongTheme songSongTheme = QSongSongTheme.songSongTheme;
            QSongTheme songTheme = QSongTheme.songTheme;

            List<Long> matchedSongIds = queryFactory
                    .select(song.id)
                    .from(song)
                    .leftJoin(song.songThemes, songSongTheme)
                    .leftJoin(songSongTheme.songTheme, songTheme)
                    .where(songTheme.id.in(themeIdLongs))
                    .groupBy(song.id)
                    .having(songTheme.id.countDistinct().eq((long) themeIdLongs.size()))
                    .fetch();

            query.where(song.id.in(matchedSongIds));
        }

        return query
                .where(builder)
                .offset(offset != null ? offset : 0)
                .limit(20)
                .fetch();
    }
}
