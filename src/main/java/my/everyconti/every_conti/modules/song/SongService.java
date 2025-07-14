package my.everyconti.every_conti.modules.song;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import my.everyconti.every_conti.modules.song.domain.*;
import my.everyconti.every_conti.modules.song.dto.request.CreateSongDto;
import my.everyconti.every_conti.modules.song.dto.request.SearchSongDto;
import my.everyconti.every_conti.modules.song.dto.response.SongDto;
import my.everyconti.every_conti.modules.song.repository.PraiseTeamRepository;
import my.everyconti.every_conti.modules.song.repository.SeasonRepository;
import my.everyconti.every_conti.modules.song.repository.SongRepository;
import my.everyconti.every_conti.modules.song.repository.SongThemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final MemberRepository memberRepository;
    private final PraiseTeamRepository praiseTeamRepository;
    private final SongThemeRepository songThemeRepository;
    private final SeasonRepository seasonRepository;
    private final EntityManager em;
    private final HashIdUtil hashIdUtil;
    private final JPAQueryFactory queryFactory;

    public SongDto createSong(CreateSongDto createSongDto){
        Member creator = memberRepository.findById(Long.valueOf(createSongDto.getCreatorId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        PraiseTeam team = praiseTeamRepository.findById(Long.valueOf(createSongDto.getPraiseTeamId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 찬양팀입니다"));

        List<SongTheme> themes = songThemeRepository.findAllById(createSongDto.getThemeIds().stream().map(Long::valueOf).toList());

        Season season = null;
        if (createSongDto.getSeasonId() != null) {
            season = seasonRepository.findById(Long.valueOf(createSongDto.getSeasonId()))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 절기입니다"));
        }

        Song song = Song.builder()
                .songName(createSongDto.getSongName())
                .lyrics(createSongDto.getLyrics())
                .reference(createSongDto.getReference())
                .songType(createSongDto.getSongType())
                .tempo(createSongDto.getTempo())
                .creator(creator)
                .praiseTeam(team)
                .season(season)
                .bibleBook(createSongDto.getBibleBook())
                .bibleChapter(createSongDto.getBibleChapter())
                .bibleVerse(createSongDto.getBibleVerse())
                .build();

        // 중간 테이블 매핑
        Set<SongSongTheme> songThemes = themes.stream()
                .map(theme -> SongSongTheme.builder().song(song).songTheme(theme).build())
                .collect(Collectors.toSet());

        song.setSongThemes(songThemes);

        Song result = songRepository.save(song);
        return new SongDto(result, hashIdUtil);
    }

    public List<SongDto> searchSong(SearchSongDto searchSongDto) {
        String text = searchSongDto.getText();

        SongType songType = searchSongDto.getSongType();
        Integer praiseTeamId = searchSongDto.getPraiseTeamId();
        List<Integer> themeIds = searchSongDto.getThemeIds();
        Integer seasonId = searchSongDto.getSeasonId();
        SongTempo tempo = searchSongDto.getTempo();
        String bibleBook = searchSongDto.getBibleBook();
        Integer bibleChapter = searchSongDto.getBibleChapter();
        Integer bibleVerse = searchSongDto.getBibleVerse();
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

        if (praiseTeamId != null) {
            builder.and(song.praiseTeam.id.eq(Long.valueOf(praiseTeamId)));
        }

        if (seasonId != null) {
            builder.and(song.season.id.eq(Long.valueOf(seasonId)));
        }

        if (tempo != null) {
            builder.and(song.tempo.eq(tempo));
        }

        if (bibleBook != null && !bibleBook.isBlank()) {
            builder.and(song.bibleBook.eq(bibleBook));
        }

        if (bibleChapter != null) {
            builder.and(song.bibleChapter.eq(bibleChapter));
        }

        if (bibleVerse != null) {
            builder.and(song.bibleVerse.eq(bibleVerse));
        }

        if (themeIds != null && !themeIds.isEmpty()) {
            builder.and(QSongSongTheme.songSongTheme.songTheme.id.in(themeIds.stream().map(Long::valueOf).toList()));
        }

        List<Song> resultList = queryFactory
                .selectFrom(song)
                .leftJoin(song.creator).fetchJoin()
                .leftJoin(song.praiseTeam).fetchJoin()
                .leftJoin(song.season).fetchJoin()
                .leftJoin(song.songThemes, QSongSongTheme.songSongTheme).fetchJoin()
                .leftJoin(QSongSongTheme.songSongTheme.songTheme).fetchJoin()
                .distinct()
                .where(builder)
                .offset(offset != null ? offset : 0)
                .limit(20)
                .fetch();
        return resultList.stream()
                .map(s -> new SongDto(s, hashIdUtil))
                .collect(Collectors.toList());
    }

    public CommonResponseDto<String> deleteSong(Long innerSongId) {
        songRepository.deleteById(innerSongId);
        return new CommonResponseDto<>(true, ResponseMessage.DELETED);
    }

//    public CommonResponseDto<String> reportSong(String songId){
//        Long innerSongId = hashIdUtil.decode(songId);
//    }
}
