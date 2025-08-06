package my.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.song.SongKey;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.bible.dto.response.BibleChapterDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleVerseDto;
import my.everyconti.every_conti.modules.member.dto.MemberNicknameDto;
import my.everyconti.every_conti.modules.song.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongDto {

    private String id;

    private String songName;

    private String lyrics;

    private String youtubeVId;

    private SongType songType;

    private PraiseTeamDto praiseTeam;

    private MemberNicknameDto creatorNickname;

    private List<SongThemeDto> songThemes;

    /*
    --------Sub columns--------
     */
    private String thumbnail;

    private SongTempo tempo;

    private SeasonDto season;

    private SongKey songKey;

    private Integer duration;

    private BibleDto bible;

    private BibleChapterDto bibleChapter;

    private BibleVerseDto bibleVerse;

    private LocalDateTime createdAt;


    public SongDto(Song song, HashIdUtil hashIdUtil) {
        id= hashIdUtil.encode(song.getId());
        songName = song.getSongName();
        lyrics = song.getLyrics();
        youtubeVId = song.getYoutubeVId();
        songType = song.getSongType();
        praiseTeam = new PraiseTeamDto(song.getPraiseTeam(), hashIdUtil);
        creatorNickname = new MemberNicknameDto(song.getCreator());
        songThemes = song.getSongThemes().stream()
                .map(s -> new SongThemeDto(s.getSongTheme(), hashIdUtil))
                .collect(Collectors.toList());
        tempo = song.getTempo();
        season = song.getSeason() == null ? null : new SeasonDto(song.getSeason(), hashIdUtil);
        songKey = song.getKey();
        thumbnail = song.getThumbnail();
        duration = song.getDuration();
        bible = song.getBible() == null ? null : new BibleDto(song.getBible(), hashIdUtil);
        bibleChapter = song.getBibleChapter() == null ? null : new BibleChapterDto(song.getBibleChapter(), hashIdUtil);
        bibleVerse = song.getBibleVerse() == null ? null : new BibleVerseDto(song.getBibleVerse(), hashIdUtil);
        createdAt = song.getCreatedAt();
    }

    @Override
    public String toString() {
        String string = "SongDto{" +
                "id='" + id + '\'' +
                "songName='" + songName + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", youtubeVId='" + youtubeVId + '\'' +
                ", songType=" + songType + '\'' +
                ", thumbnail=" + thumbnail + '\'' +
                ", creator='" + creatorNickname + '\'' +
                ", praiseTeam='" + praiseTeam.toString() + '\'' +
                ", songThemes=" + songThemes.toString() + '\'' +
                ", tempo=" + tempo  + '\'' +
                ", season='" + (season != null ? season.toString() : "null") + '\'' +
                ", bible='" + (bible == null ? "null" : bible.toString()) + '\'' +
                ", bibleChapter=" + (bibleChapter == null ? "null" : bibleChapter.toString()) + '\'' +
                ", bibleVerse=" + (bibleVerse == null ? "null" : bibleVerse.toString()  + '\'' +
                ", createdAt=" + createdAt
        );
        if (season == null){
            string += "null";
        } else {
            string += season.toString();
        }
        return string + '}';
    }
}
