package my.everyconti.every_conti.modules.song;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
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

import java.util.ArrayList;
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

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Song> cq = cb.createQuery(Song.class);
        Root<Song> song = cq.from(Song.class);

        song.fetch("creator", JoinType.LEFT);
        song.fetch("praiseTeam", JoinType.LEFT);
        song.fetch("season", JoinType.LEFT);
        Fetch<Song, SongSongTheme> songThemesFetch = song.fetch("songThemes", JoinType.LEFT);

        songThemesFetch.fetch("songTheme", JoinType.LEFT);
        cq.distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (text != null && !text.isBlank()) {
            String pattern = "%" + text.toLowerCase() + "%";
            Predicate titlePredicate = cb.like(song.get("songName"), pattern);
            Predicate lyricsPredicate = cb.like(song.get("lyrics"), pattern);
            predicates.add(cb.or(titlePredicate, lyricsPredicate));
        }

        if (songType != null) {
            predicates.add(cb.equal(song.get("songType"), songType));
        }

        if (praiseTeamId != null) {
            Join<Song, PraiseTeam> praiseTeamJoin = song.join("praiseTeam", JoinType.INNER);
            predicates.add(cb.equal(praiseTeamJoin.get("id"), praiseTeamId));
        }

        if (seasonId != null) {
            Join<Song, Season> seasonJoin = song.join("season", JoinType.INNER);
            predicates.add(cb.equal(seasonJoin.get("id"), seasonId));
        }

        if (tempo != null) {
            predicates.add(cb.equal(song.get("tempo"), tempo));
        }

        if (bibleBook != null && !bibleBook.isBlank()) {
            predicates.add(cb.equal(song.get("bibleBook"), bibleBook));
        }

        if (bibleChapter != null) {
            predicates.add(cb.equal(song.get("bibleChapter"), bibleChapter));
        }

        if (bibleVerse != null) {
            predicates.add(cb.equal(song.get("bibleVerse"), bibleVerse));
        }

        if (themeIds != null && !themeIds.isEmpty()) {
            Join<Song, SongSongTheme> SongthemeJoin = song.join("songThemes", JoinType.INNER);
            predicates.add(SongthemeJoin.get("id").in(themeIds));
            cq.distinct(true);
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Song> query = em.createQuery(cq);

        // 페이징 offset 기본 0, limit 20 (필요시 조정)
        query.setFirstResult(offset != null ? offset : 0);
        query.setMaxResults(20);

        List<Song> resultList = query.getResultList();
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
