package my.everyconti.every_conti.modules.song;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.constant.song.SongKey;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.bible.BibleService;
import my.everyconti.every_conti.modules.bible.domain.Bible;
import my.everyconti.every_conti.modules.bible.domain.BibleChapter;
import my.everyconti.every_conti.modules.bible.domain.BibleVerse;
import my.everyconti.every_conti.modules.bible.repository.BibleChapterRepository;
import my.everyconti.every_conti.modules.bible.repository.BibleRepository;
import my.everyconti.every_conti.modules.bible.repository.BibleVerseRepository;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.member.MemberRepository;
import my.everyconti.every_conti.modules.song.domain.*;
import my.everyconti.every_conti.modules.song.dto.request.CreateSongDto;
import my.everyconti.every_conti.modules.song.dto.request.SearchSongDto;
import my.everyconti.every_conti.modules.song.dto.response.*;
import my.everyconti.every_conti.modules.song.dto.response.song.SongWithPraiseTeamDto;
import my.everyconti.every_conti.modules.song.repository.PraiseTeamRepository;
import my.everyconti.every_conti.modules.song.repository.SeasonRepository;
import my.everyconti.every_conti.modules.song.repository.song.SongRepository;
import my.everyconti.every_conti.modules.song.repository.SongThemeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
    private final HashIdUtil hashIdUtil;
    private final JPAQueryFactory queryFactory;
    private final BibleService bibleService;
    private final BibleChapterRepository bibleChapterRepository;
    private final BibleRepository bibleRepository;
    private final BibleVerseRepository bibleVerseRepository;

    @Transactional
    public SongDto createSong(CreateSongDto createSongDto){
        Member creator = memberRepository.findById(hashIdUtil.decode(createSongDto.getCreatorId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        PraiseTeam team = praiseTeamRepository.findById(hashIdUtil.decode(createSongDto.getPraiseTeamId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 찬양팀입니다"));

        List<SongTheme> themes = null;
        Bible bible = null;
        BibleChapter bibleChapter = null;
        BibleVerse bibleVerse = null;
        String bibleId = createSongDto.getBibleId();
        String bibleChapterId = createSongDto.getBibleChapterId();
        String bibleVerseId = createSongDto.getBibleVerseId();

        if (bibleId != null && !bibleId.isBlank()) {
            bible = bibleRepository.findById(hashIdUtil.decode(bibleId)).orElseThrow();
        }
        if (bibleChapterId != null && !bibleChapterId.isBlank()) bibleChapter = bibleChapterRepository.findById(hashIdUtil.decode(bibleChapterId)).orElseThrow();
        if (bibleVerseId != null && !bibleVerseId.isBlank()) bibleVerse = bibleVerseRepository.findById(hashIdUtil.decode(bibleVerseId)).orElseThrow();

        Season season = null;
        if (createSongDto.getSeasonId() != null) {
            season = seasonRepository.findById(hashIdUtil.decode(createSongDto.getSeasonId()))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 절기입니다"));
        }
        if (createSongDto.getThemeIds() != null){
            themes = songThemeRepository.findAllById(createSongDto.getThemeIds().stream().map(hashIdUtil::decode).toList());
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
                .bible(bible)
                .bibleChapter(bibleChapter)
                .bibleVerse(bibleVerse)
                .thumbnail(createSongDto.getThumbnail())
                .build();

        // 중간 테이블 매핑
        if (themes.size() > 0){
            Set<SongSongTheme> songThemes = themes.stream()
                    .map(theme -> SongSongTheme.builder().song(song).songTheme(theme).build())
                    .collect(Collectors.toSet());

            song.setSongThemes(songThemes);
        }


        Song result = songRepository.save(song);
        return new SongDto(result, hashIdUtil);
    }

    public List<SongDto> searchSong(SearchSongDto searchSongDto) {
        List<Song> resultList = songRepository.findSongsWithSearchParams(searchSongDto, hashIdUtil);

        return resultList.stream()
                .map(s -> new SongDto(s, hashIdUtil))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommonResponseDto<String> deleteSong(Long innerSongId) {
        songRepository.deleteById(innerSongId);
        return new CommonResponseDto<>(true, ResponseMessage.DELETED);
    }

    public List<SongWithPraiseTeamDto> getLastFourSongs(){
        List<Song> lastSongs = songRepository.findLastSongsWithPraiseTeam(4);
        return lastSongs.stream().map(s -> new SongWithPraiseTeamDto(s, hashIdUtil)).toList();
    }

    public SearchPropertiesDto getSearchProperties(){
        return SearchPropertiesDto
                .builder()
                .praiseTeams(getPraiseTeamLists())
                .seasons(getSeasonLists())
                .songThemes(getSongThemeLists())
                .songTypes(List.of(SongType.values()))
                .songTempos(List.of(SongTempo.values()))
                .songKeys(List.of(SongKey.values()))
                .bibles(bibleService.getBibleLists())
                .build();
    }
    public List<PraiseTeamDto> getPraiseTeamLists(){
        return praiseTeamRepository.findAll().stream().map(t -> new PraiseTeamDto(t, hashIdUtil)).toList();
    }
    public List<SeasonDto> getSeasonLists(){
        return seasonRepository.findAll().stream().map(s -> new SeasonDto(s, hashIdUtil)).toList();
    }
    public List<SongThemeDto> getSongThemeLists(){
        return songThemeRepository.findAll().stream().map(th -> new SongThemeDto(th, hashIdUtil)).toList();
    }



//    public CommonResponseDto<String> reportSong(String songId){
//        Long innerSongId = hashIdUtil.decode(songId);
//    }
}