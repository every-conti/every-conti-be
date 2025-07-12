package my.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.member.dto.MemberNicknameDto;
import my.everyconti.every_conti.modules.song.domain.*;

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

    private String reference;

    private SongType songType;

    private PraiseTeamDto praiseTeam;

    private MemberNicknameDto creatorNickname;

    private List<SongSongThemeDto> songThemes;

    /*
    --------Sub columns--------
     */
    private SongTempo tempo;

    private SeasonDto season;

    private String bibleBook;

    private Integer bibleChapter;

    private Integer bibleVerse;



    public SongDto(Song song, HashIdUtil hashIdUtil) {
        id= hashIdUtil.encode(song.getId());
        songName = song.getSongName();
        lyrics = song.getLyrics();
        reference = song.getReference();
        songType = song.getSongType();
        praiseTeam = new PraiseTeamDto(song.getPraiseTeam());
        creatorNickname = new MemberNicknameDto(song.getCreator());
        songThemes = song.getSongThemes().stream()
                .map(SongSongThemeDto::new)
                .collect(Collectors.toList());
        tempo = song.getTempo();
        season = song.getSeason() == null ? null : new SeasonDto(song.getSeason());
        bibleBook = song.getBibleBook();
        bibleChapter = song.getBibleChapter();
        bibleVerse = song.getBibleVerse();
    }

    @Override
    public String toString() {
        String string = "SongDto{" +
                "id='" + id + '\'' +
                "songName='" + songName + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", reference='" + reference + '\'' +
                ", songType=" + songType +
                ", creator='" + creatorNickname + '\'' +
                ", praiseTeam='" + praiseTeam.toString() + '\'' +
                ", songThemes=" + songThemes.toString() +
                ", tempo=" + tempo +
                ", bibleBook='" + bibleBook + '\'' +
                ", bibleChapter=" + bibleChapter +
                ", bibleVerse=" + bibleVerse + ", season='";
        if (season == null){
            string += "null";
        } else {
            string += season.toString();
        }
        return string + '}';
    }
}
