package com.everyconti.every_conti.modules.song.repository.song;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.constant.ResponseMessage;
import com.everyconti.every_conti.constant.song.SongKey;
import com.everyconti.every_conti.constant.song.SongTempo;
import com.everyconti.every_conti.constant.song.SongType;
import com.everyconti.every_conti.modules.song.domain.QSong;
import com.everyconti.every_conti.modules.song.domain.QSongSongTheme;
import com.everyconti.every_conti.modules.song.domain.QSongTheme;
import com.everyconti.every_conti.modules.song.domain.Song;
import com.everyconti.every_conti.modules.song.domain.es.SongDocument;
import com.everyconti.every_conti.modules.song.dto.request.SearchSongDto;
import com.everyconti.every_conti.modules.song.repository.es.SongSearchRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class SongRepositoryImpl implements SongRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final SongSearchRepository songSearchRepository;

    @Override
    public Song findSongByIdWithJoin(Long innerSongId){
        QSong song = QSong.song;
        QSongSongTheme songSongTheme = QSongSongTheme.songSongTheme;

        Song existingSong = queryFactory.selectFrom(song)
                .leftJoin(song.songThemes, songSongTheme).fetchJoin()
//                .leftJoin(songSongTheme.songTheme).fetchJoin()
                .leftJoin(song.creator).fetchJoin()
                .leftJoin(song.praiseTeam).fetchJoin()
                .leftJoin(song.bible).fetchJoin()
                .leftJoin(song.bibleChapter).fetchJoin()
                .leftJoin(song.bibleVerse).fetchJoin()
                .where(song.id.eq(innerSongId))
                .fetchOne();

        if (existingSong == null) throw new EntityNotFoundException(ResponseMessage.notFoundMessage("찬양"));

        return existingSong;
    }

    @Override
    public Song findSongByIdWithJoinAll(Long innerSongId){
        QSong song = QSong.song;
        QSongSongTheme songSongTheme = QSongSongTheme.songSongTheme;

        Song existingSong = queryFactory.selectFrom(song)
                .leftJoin(song.songThemes, songSongTheme).fetchJoin()
//                .leftJoin(songSongTheme.songTheme).fetchJoin()
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
        SongKey songKey =  searchSongDto.getSongKey();
        Long offset = searchSongDto.getOffset() != null ? searchSongDto.getOffset() : 0;
        Integer minDuration = searchSongDto.getMinDuration();
        Integer maxDuration = searchSongDto.getMaxDuration();

        QSong song = QSong.song;
        BooleanBuilder builder = new BooleanBuilder();

        List<Long> idsInOrder = null;
        if (text != null && !text.isBlank()) {
            int pageIndex = (int)(offset / 21);
            var page = songSearchRepository.fullTextSearch(text, org.springframework.data.domain.PageRequest.of(pageIndex, 21));
            idsInOrder = page.getContent().stream().map(SongDocument::getId).toList();

            if (idsInOrder.isEmpty()) return List.of();
            builder.and(song.id.in(idsInOrder));
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

        if (songKey != null){
            builder.and(song.key.eq(songKey));
        }

        if (bibleId != null && !bibleId.isBlank()) {
            builder.and(song.bible.id.eq(hashIdUtil.decode(bibleId)));
        }

        if (bibleChapterId != null && !bibleChapterId.isBlank()) {
            builder.and(song.bibleChapter.id.eq(hashIdUtil.decode(bibleChapterId)));
        }

        if (bibleVerseId != null && !bibleVerseId.isBlank()) {
            builder.and(song.bibleVerse.id.eq(hashIdUtil.decode(bibleVerseId)));
        }

        if (minDuration != null && maxDuration != null) {
            builder.and(song.duration.between(minDuration, maxDuration));
        }

        JPAQuery<Song> query = queryFactory
                .selectFrom(song)
                .leftJoin(song.creator).fetchJoin()
                .leftJoin(song.praiseTeam).fetchJoin()
                .leftJoin(song.season).fetchJoin()
                .leftJoin(song.bible).fetchJoin()
                .leftJoin(song.bibleChapter).fetchJoin()
                .leftJoin(song.bibleVerse).fetchJoin();

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

        query
            .where(builder)
            .offset(offset)
            .limit(21);

        if (idsInOrder != null) {
            com.querydsl.core.types.dsl.NumberExpression<Integer> orderExpr =
                    com.querydsl.core.types.dsl.Expressions.asNumber(Integer.MAX_VALUE);
            for (int i = idsInOrder.size() - 1; i >= 0; i--) {
                Long id = idsInOrder.get(i);
                orderExpr = new com.querydsl.core.types.dsl.CaseBuilder()
                        .when(song.id.eq(id)).then(i)
                        .otherwise(orderExpr);
            }
            query.orderBy(orderExpr.asc(), song.id.desc());
        } else {
            query.orderBy(song.id.desc());
        }

        return query.fetch();
    }
}
